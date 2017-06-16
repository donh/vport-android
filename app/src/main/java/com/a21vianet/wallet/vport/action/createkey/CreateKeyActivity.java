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
import com.a21vianet.wallet.vport.library.commom.crypto.callback.GenerateCallBack;
import com.a21vianet.wallet.vport.library.commom.crypto.callback.MultisigCallback;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.bean.UserInfoIPFS;
import com.a21vianet.wallet.vport.library.commom.http.vchain.CreateResponse;
import com.a21vianet.wallet.vport.library.commom.http.vchain.TransactionResponse;
import com.a21vianet.wallet.vport.library.commom.http.vchain.VChainRequest;
import com.a21vianet.wallet.vport.library.commom.http.vchain.bean.TxIdResponse;
import com.littlesparkle.growler.core.am.ActivityManager;
import com.littlesparkle.growler.core.http.BaseHttpSubscriber;
import com.littlesparkle.growler.core.ui.activity.BaseActivity;
import com.littlesparkle.growler.core.ui.toast.ToastFactory;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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
        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(final Subscriber<? super String> subscriber) {
                        CryptoManager.getInstance().generateBitcoinKeyPair(pass, new
                                GenerateCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        subscriber.onNext("");
                                    }

                                    @Override
                                    public void onError() {
                                        subscriber.onError(new Exception("公私钥创建失败"));
                                    }
                                });
                    }
                })
                .flatMap(new Func1<String, Observable<Contract>>() {
                    @Override
                    public Observable<Contract> call(String s) {
                        return nativeSubmit();
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
        return Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(final Subscriber<? super String> subscriber) {
                        try {
                            final String bitcoinAddress = CryptoManager.getInstance()
                                    .generateBitcoinAddress();
                            new VChainRequest(Api.vChainFarmApi).sendToAddress(new BaseHttpSubscriber<TxIdResponse>() {
                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    subscriber.onError(e);
                                }

                                @Override
                                protected void onSuccess(TxIdResponse txIdResponse) {
                                    super.onSuccess(txIdResponse);
                                    subscriber.onNext(bitcoinAddress);
                                }
                            }, bitcoinAddress);
                        } catch (NoDecryptException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(final String multiSig) {
                        return Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(final Subscriber<? super String> subscriber) {
                                new VChainRequest().create(new BaseHttpSubscriber<CreateResponse>
                                        () {
                                    @Override
                                    protected void onSuccess(CreateResponse createResponse) {
                                        subscriber.onNext(createResponse.rawTx);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);
                                        subscriber.onError(e);
                                    }
                                }, multiSig, multiSig, multiSig);
                            }
                        });
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(final String tx) {
                        return Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(final Subscriber<? super String> subscriber) {
                                try {
                                    CryptoManager.getInstance().sign(CreateKeyActivity.this,
                                            tx, new MultisigCallback() {
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
                .flatMap(new Func1<String, Observable<Contract>>() {
                    @Override
                    public Observable<Contract> call(final String signedRawTransaction) {
                        return Observable.create(new Observable.OnSubscribe<Contract>() {
                            @Override
                            public void call(final Subscriber<? super Contract>
                                                     subscriber) {
                                try {
                                    new VChainRequest().transaction(
                                            new BaseHttpSubscriber<TransactionResponse>() {
                                                @Override
                                                public void onSuccess(TransactionResponse
                                                                              transactionResponse) {
                                                    Contract contract = new Contract();
                                                    contract.get();
                                                    contract.setController(transactionResponse
                                                            .contract.getController());
                                                    contract.setProxy(transactionResponse
                                                            .contract.getProxy());
                                                    contract.setRecover(transactionResponse
                                                            .contract.getRecover());
                                                    subscriber.onNext(contract);
                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    super.onError(e);
                                                    subscriber.onError(e);
                                                }
                                            },
                                            CryptoManager.getInstance().generateBitcoinAddress(),
                                            CryptoManager.getInstance().generateBitcoinAddress(),
                                            signedRawTransaction);
                                } catch (NoDecryptException e) {
                                    e.printStackTrace();
                                    subscriber.onError(e);
                                }
                            }
                        });
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

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "请耐心等待用户注册完毕", Toast.LENGTH_SHORT).show();
    }
}
