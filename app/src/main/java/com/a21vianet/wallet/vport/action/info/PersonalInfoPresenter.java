package com.a21vianet.wallet.vport.action.info;


import android.net.Uri;

import com.a21vianet.wallet.vport.action.info.data.IPFSResponse;
import com.a21vianet.wallet.vport.action.info.data.PersonalInfoRequest;
import com.a21vianet.wallet.vport.biz.CryptoBiz;
import com.a21vianet.wallet.vport.http.Api;
import com.a21vianet.wallet.vport.library.commom.crypto.CryptoManager;
import com.a21vianet.wallet.vport.library.commom.crypto.NoDecryptException;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.Contract;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.IPFSRequest;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.bean.UserInfoIPFS;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.bean.UserInfoIPFSGET;
import com.a21vianet.wallet.vport.library.constant.SysConstant;
import com.a21vianet.wallet.vport.library.event.ChangeHeadImageEvent;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TResult;
import com.littlesparkle.growler.core.common.TempDirectory;
import com.littlesparkle.growler.core.http.BaseHttpSubscriber;
import com.littlesparkle.growler.core.http.ErrorResponse;
import com.littlesparkle.growler.core.ui.mvp.BasePresenter;
import com.littlesparkle.growler.core.utility.FileUtility;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PersonalInfoPresenter extends BasePresenter<PersonalInfoActivity> implements
        PersonalInfoContract.Presenter {


    private TakePhoto takePhoto;
    private CompressConfig compressConfig;
    private LubanOptions lubanOptions;
    private CropOptions cropOptions;

    @Override
    public void onStart() {
        takePhoto = getView().getTakePhoto();
//        初始化压缩配置
        lubanOptions = new LubanOptions.Builder()
                .setMaxHeight(800)
                .setMaxWidth(800)
                .setMaxSize(100 * 1024)
                .create();
//        初始化裁剪配置
        cropOptions = new CropOptions.Builder()
                .setAspectX(1)
                .setAspectY(1)
                .setWithOwnCrop(true)
                .create();
        compressConfig = CompressConfig.ofLuban(lubanOptions);
        takePhoto.onEnableCompress(compressConfig, true);
//        如果文件夹不存在，初始化文件夹
        File file = new File(TempDirectory.getFileDir());
        if (!file.exists()) {
            FileUtility.mkdir(TempDirectory.getFileDir());
        }
        getUserInfo();
    }

    @Override
    public void getPhotoByCamera() {
        File file = new File(TempDirectory.getFileDir()
                + File.separator
                + "user_head"
                + System.currentTimeMillis()
                + ".jpg");
        takePhoto.onPickFromCaptureWithCrop(Uri.fromFile(file), cropOptions);
    }

    @Override
    public void getPhotoByAlbums() {
        File file = new File(TempDirectory.getFileDir()
                + File.separator
                + "user_head"
                + System.currentTimeMillis()
                + ".jpg");
        takePhoto.onPickFromGalleryWithCrop(Uri.fromFile(file), cropOptions);
    }

    @Override
    public void onTakeSuccess(final TResult tResult) {
        final Contract contract = new Contract();
        contract.get();

        getView().showProgress();

        Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                subscriber.onNext(new File(tResult.getImage().getCompressPath()));
            }
        }).flatMap(new Func1<File, Observable<IPFSResponse>>() {
            @Override
            public Observable<IPFSResponse> call(final File file) {
                return Observable.create(new Observable.OnSubscribe<IPFSResponse>() {

                    @Override
                    public void call(final Subscriber<? super IPFSResponse> subscriber) {
                        new PersonalInfoRequest(Api.IPFSApi).setThumbToIPFS(new BaseHttpSubscriber<IPFSResponse>() {
                            @Override
                            protected void onError(ErrorResponse error) {
                                super.onError(error);
                                subscriber.onError(new Throwable());
                            }

                            @Override
                            public void onNext(IPFSResponse IPFSResponse) {
                                SysConstant.setHradImageUrlHash(IPFSResponse.Hash);
                                EventBus.getDefault().post(new ChangeHeadImageEvent());
                                super.onNext(IPFSResponse);
                                subscriber.onNext(IPFSResponse);
                            }
                        }, file);
                    }
                });
            }
        }).flatMap(new Func1<IPFSResponse, Observable<UserInfoIPFS>>() {
            @Override
            public Observable<UserInfoIPFS> call(final IPFSResponse ipfsResponse) {
                return Observable.create(new Observable.OnSubscribe<UserInfoIPFS>() {
                    @Override
                    public void call(final Subscriber<? super UserInfoIPFS> subscriber) {
                        try {
                            new IPFSRequest().get(new BaseHttpSubscriber<UserInfoIPFSGET>() {
                                @Override
                                public void onNext(UserInfoIPFSGET userInfoIPFSGET) {
                                    super.onNext(userInfoIPFSGET);
                                    UserInfoIPFS.ImageBean imageBean = new UserInfoIPFS.ImageBean();
                                    imageBean.setContentUrl(ipfsResponse.Hash);
                                    UserInfoIPFS userInfoIPFS = userInfoIPFSGET.getValue();
                                    userInfoIPFS.setImage(imageBean);
                                    subscriber.onNext(userInfoIPFS);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    subscriber.onError(e);
                                }
                            }, CryptoManager.getInstance().generateBitcoinAddress(), contract
                                    .getProxy());
                        } catch (NoDecryptException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).flatMap(new Func1<UserInfoIPFS, Observable<Contract>>() {
            @Override
            public Observable<Contract> call(UserInfoIPFS userInfoIPFS) {
                return CryptoBiz.signIPFSTx(contract, userInfoIPFS);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpSubscriber<Contract>() {
                    @Override
                    public void onNext(Contract rawTxSignedResponse) {
                        super.onNext(rawTxSignedResponse);
                        getView().dismissProgress();
                        getView().showHardImage(SysConstant.getHradImageUrlHash());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getView().dismissProgress();
                    }
                });

    }

    private void getUserInfo() {
        getView().setViewData();
    }
}
