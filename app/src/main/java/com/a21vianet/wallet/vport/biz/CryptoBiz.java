package com.a21vianet.wallet.vport.biz;

import com.a21vianet.wallet.vport.http.Api;
import com.a21vianet.wallet.vport.library.BaseApplication;
import com.a21vianet.wallet.vport.library.commom.crypto.CryptoManager;
import com.a21vianet.wallet.vport.library.commom.crypto.NoDecryptException;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.Contract;
import com.a21vianet.wallet.vport.library.commom.crypto.callback.MultisigCallback;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.IPFSRequest;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.RawTxResponse;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.bean.AddResult;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.bean.UserInfoIPFS;
import com.a21vianet.wallet.vport.library.commom.http.transaction.TransactionRequest;
import com.a21vianet.wallet.vport.library.commom.http.transaction.bean.RawTxSignedResponse;
import com.a21vianet.wallet.vport.library.event.ChangeUserInfoEvent;
import com.google.gson.Gson;
import com.littlesparkle.growler.core.http.BaseHttpSubscriber;

import org.greenrobot.eventbus.EventBus;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wang.rongqiang on 2017/6/12.
 */

public class CryptoBiz {
    /**
     * 对个人信息进行 上传 签名
     *
     * @param contract
     * @return
     */
    public static Observable<Contract> signIPFSTx(final Contract contract, final
    UserInfoIPFS userInfoIPFS) {
        return Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(final Subscriber<? super String> subscriber) {
                        final String json = new Gson().toJson(userInfoIPFS);
                        new IPFSRequest(Api.IPFSApi).ipfsAddJson(new BaseHttpSubscriber<AddResult>() {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                subscriber.onError(e);
                            }

                            @Override
                            protected void onSuccess(AddResult addResult) {
                                super.onSuccess(addResult);
                                contract.setIpfsHex(addResult.getHash());
                                subscriber.onNext(json);
                            }
                        }, json);
                    }
                })
                .flatMap(new Func1<String, Observable<RawTxResponse>>() {
                    @Override
                    public Observable<RawTxResponse> call(final String json) {
                        return Observable
                                .create(new Observable.OnSubscribe<RawTxResponse>() {
                                    @Override
                                    public void call(final Subscriber<? super RawTxResponse>
                                                             subscriber) {
                                        try {
                                            new IPFSRequest().set(
                                                    new BaseHttpSubscriber<RawTxResponse>() {
                                                        @Override
                                                        public void onError(Throwable e) {
                                                            super.onError(e);
                                                            subscriber.onError(e);
                                                        }

                                                        @Override
                                                        protected void onSuccess(RawTxResponse
                                                                                         rawTxResponse) {
                                                            subscriber.onNext(rawTxResponse);
                                                        }
                                                    }, CryptoManager.getInstance()
                                                            .generateBitcoinAddress(),
                                                    contract.getProxy(), json);
                                        } catch (NoDecryptException e) {
                                            e.printStackTrace();
                                            subscriber.onError(e);
                                        }
                                    }
                                });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<RawTxResponse, Observable<String>>() {
                    @Override
                    public Observable<String> call(final RawTxResponse rawTxResponse) {
                        return Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(final Subscriber<? super String> subscriber) {
                                try {
                                    CryptoManager.getInstance().sign(BaseApplication.getContext(),
                                            rawTxResponse.rawTx, new MultisigCallback() {
                                                @Override
                                                public void onSinged(String signedRawTransaction) {
                                                    subscriber.onNext(signedRawTransaction);
                                                }

                                                @Override
                                                public void onError(Exception e) {
                                                    subscriber.onError(e);
                                                }
                                            });
                                } catch (NoDecryptException e) {
                                    e.printStackTrace();
                                    subscriber.onError(e);
                                }
                            }
                        });
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String,
                        Observable<RawTxSignedResponse>>() {
                    @Override
                    public Observable<RawTxSignedResponse> call(final String rawTx) {
                        return Observable.create(new Observable.OnSubscribe<RawTxSignedResponse>() {
                            @Override
                            public void call(final Subscriber<? super RawTxSignedResponse>
                                                     subscriber) {
                                new TransactionRequest().signed(new BaseHttpSubscriber<RawTxSignedResponse>() {
                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);
                                        subscriber.onError(e);
                                    }

                                    @Override
                                    protected void onSuccess(RawTxSignedResponse
                                                                     rawTxSignedResponse) {
                                        EventBus.getDefault().post(new ChangeUserInfoEvent
                                                (userInfoIPFS));
                                        subscriber.onNext(rawTxSignedResponse);
                                    }
                                }, rawTx);
                            }
                        });
                    }
                }).map(new Func1<RawTxSignedResponse, Contract>() {
                    @Override
                    public Contract call(RawTxSignedResponse rawTxSignedResponse) {
                        return contract;
                    }
                });
    }
}
