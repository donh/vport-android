package com.a21vianet.wallet.vport.action.createkey;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.a21vianet.wallet.vport.R;
import com.a21vianet.wallet.vport.action.mian.MainActivity;
import com.a21vianet.wallet.vport.biz.CryptoBiz;
import com.a21vianet.wallet.vport.http.Api;
import com.a21vianet.wallet.vport.library.commom.crypto.CryptoManager;
import com.a21vianet.wallet.vport.library.commom.crypto.NoDecryptException;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.BitcoinKey;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.Contract;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.IPFSRequest;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.bean.UserInfoIPFS;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.bean.UserInfoIPFSGET;
import com.a21vianet.wallet.vport.library.commom.http.vchain.CreateResponse;
import com.a21vianet.wallet.vport.library.commom.http.vchain.TransactionResponse;
import com.a21vianet.wallet.vport.library.commom.http.vchain.VChainRequest;
import com.a21vianet.wallet.vport.library.commom.http.vchain.bean.GetColor1Response;
import com.a21vianet.wallet.vport.library.commom.http.vchain.bean.VPortCreateRequestBean;
import com.a21vianet.wallet.vport.library.commom.http.vchain.bean.VPortCreateResponse;
import com.littlesparkle.growler.core.am.ActivityManager;
import com.littlesparkle.growler.core.http.BaseHttpSubscriber;
import com.littlesparkle.growler.core.ui.activity.BaseActivity;
import com.littlesparkle.growler.core.ui.toast.ToastFactory;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class CreateKeyActivity extends BaseActivity {

    private static final String PASSWORD = "PASS";

    public static void startActivity(Context context, String pass) {
        Intent intent = new Intent(context, CreateKeyActivity.class);
        intent.putExtra(PASSWORD, pass);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_create_key;
    }

    @Override
    protected void initView() {
        super.initView();
        createKey(getIntent().getStringExtra(PASSWORD));
    }


    private void createKey(final String pass) {
        CryptoManager.getInstance().generateBitcoinKeyPair(pass)
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<Contract>>() {
                    @Override
                    public Observable<Contract> call(String s) {
                        return vPortSubmit();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpSubscriber<Contract>() {
                    @Override
                    public void onError(Throwable e) {
                        Contract contract = new Contract();
                        contract.clear();
                        ToastFactory.getInstance(getApplicationContext()).makeTextShow("创建新用户失败",
                                Toast.LENGTH_SHORT);
                        finish();
                    }

                    @Override
                    public void onNext(Contract contract) {
                        contract.save();
                        startActivity(new Intent(CreateKeyActivity.this, MainActivity.class));
                        ActivityManager.getInstance().finishAll();
                    }
                });

    }

    /**
     * 直接去 vChain 链上注册用户身份
     *
     * @return
     */
    private Observable<Contract> nativeSubmit() {
        String bitcoinAddress = "";
        try {
            bitcoinAddress = CryptoManager.getInstance()
                    .generateBitcoinAddress();
        } catch (NoDecryptException e) {
            e.printStackTrace();
        }
        final String address = bitcoinAddress;
        return new VChainRequest(Api.vChainGetColor1Api).getColor1(bitcoinAddress)
                .map(new Func1<GetColor1Response, String>() {
                    @Override
                    public String call(GetColor1Response getColor1Response) {
                        return address;
                    }
                })
                .flatMap(new Func1<String, Observable<CreateResponse>>() {
                    @Override
                    public Observable<CreateResponse> call(final String multiSig) {
                        return new VChainRequest().create(multiSig, multiSig, multiSig);
                    }
                })
                .map(new Func1<CreateResponse, String>() {
                    @Override
                    public String call(CreateResponse response) {
                        return response.rawTx;
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(final String tx) {
                        return CryptoManager.getInstance().sign(CreateKeyActivity.this, tx);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<TransactionResponse>>() {
                    @Override
                    public Observable<TransactionResponse> call(final String signedRawTransaction) {
                        String address = "";
                        try {
                            address = CryptoManager.getInstance().generateBitcoinAddress();
                        } catch (NoDecryptException e) {
                            e.printStackTrace();
                        }
                        return new VChainRequest().transaction(address, address,
                                signedRawTransaction);
                    }
                })
                .map(new Func1<TransactionResponse, Contract>() {
                    @Override
                    public Contract call(TransactionResponse transactionResponse) {
                        Contract contract = new Contract();
                        contract.get();
                        contract.setController(transactionResponse
                                .contract.getController());
                        contract.setProxy(transactionResponse
                                .contract.getProxy());
                        contract.setRecover(transactionResponse
                                .contract.getRecover());
                        return contract;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Contract, Observable<Contract>>() {
                    @Override
                    public Observable<Contract> call(Contract contract) {
                        UserInfoIPFS userInfoIPFS = new UserInfoIPFS();
                        userInfoIPFS.setName(contract.getNickname());

                        try {
                            BitcoinKey coinKey = CryptoManager.getInstance().getCoinKey();
                            userInfoIPFS.setAddress(CryptoManager.getInstance()
                                    .generateBitcoinAddress());
                            userInfoIPFS.setPublicKey(coinKey.getPubKey());
                        } catch (NoDecryptException e) {
                            e.printStackTrace();
                        }

                        return CryptoBiz.signIPFSTx(contract, userInfoIPFS);
                    }
                });
    }

    /**
     * vPort 代理注册 用户身份
     *
     * @return
     */
    private Observable<Contract> vPortSubmit() {
        final Contract contract = new Contract();
        contract.get();
        VPortCreateRequestBean vPortCreateRequestBean = new VPortCreateRequestBean();
        try {
            BitcoinKey coinKey = CryptoManager.getInstance().getCoinKey();
            vPortCreateRequestBean.setName(contract.getNickname());
            vPortCreateRequestBean.setPhone("");
            vPortCreateRequestBean.setPrivateKey(coinKey.getPrivKey());
            vPortCreateRequestBean.setPublicKey(coinKey.getPubKey());
            vPortCreateRequestBean.setAddress(CryptoManager.getInstance().generateBitcoinAddress());
        } catch (NoDecryptException e) {
            e.printStackTrace();
        }
        return new VChainRequest(Api.vChainServiceApi)
                .vPortCreate(vPortCreateRequestBean)
                .map(new Func1<VPortCreateResponse, Contract>() {
                    @Override
                    public Contract call(VPortCreateResponse vPortCreateResponse) {
                        contract.setController(vPortCreateResponse.getResult().getUser()
                                .getController());
                        contract.setProxy(vPortCreateResponse.getResult().getUser().getProxy());
                        contract.setRecover(vPortCreateResponse.getResult().getUser().getRecovery
                                ());
                        vPortCreateGetHash(contract.getProxy());
                        return contract;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "请耐心等待用户注册完毕", Toast.LENGTH_SHORT).show();
    }

    /**
     * 延迟获取 IPFS Hash
     *
     * @param proxy
     */
    public void vPortCreateGetHash(final String proxy) {
        try {
            new IPFSRequest()
                    .get(CryptoManager.getInstance().generateBitcoinAddress(), proxy)
                    .delay(10, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(new Action1<UserInfoIPFSGET>() {
                        @Override
                        public void call(UserInfoIPFSGET userInfoIPFSGET) {
                            if (userInfoIPFSGET.getIpfsHex() == null || userInfoIPFSGET
                                    .getIpfsHex().equals("")) {
                                vPortCreateGetHash(proxy);
                                return;
                            }
                            Contract contract = new Contract();
                            contract.get();
                            contract.setIpfsHex(userInfoIPFSGET.getIpfsHex());
                            contract.save();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            vPortCreateGetHash(proxy);
                        }
                    });
        } catch (NoDecryptException e) {
            e.printStackTrace();
        }
    }
}
