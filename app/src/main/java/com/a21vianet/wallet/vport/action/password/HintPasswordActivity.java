package com.a21vianet.wallet.vport.action.password;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.a21vianet.wallet.vport.action.mian.MainActivity;
import com.a21vianet.wallet.vport.R;
import com.a21vianet.wallet.vport.http.Api;
import com.a21vianet.wallet.vport.library.commom.crypto.CryptoManager;
import com.a21vianet.wallet.vport.library.commom.crypto.NoDecryptException;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.Contract;
import com.a21vianet.wallet.vport.library.commom.crypto.callback.GenerateCallBack;
import com.a21vianet.wallet.vport.library.commom.crypto.callback.MultisigCallback;
import com.a21vianet.wallet.vport.library.commom.http.vchain.CreateResponse;
import com.a21vianet.wallet.vport.library.commom.http.vchain.TransactionResponse;
import com.a21vianet.wallet.vport.library.commom.http.vchain.VChainRequest;
import com.a21vianet.wallet.vport.library.commom.http.vchain.bean.TxIdResponse;
import com.littlesparkle.growler.core.am.ActivityManager;
import com.littlesparkle.growler.core.http.BaseHttpSubscriber;
import com.littlesparkle.growler.core.ui.activity.BaseActivity;
import com.littlesparkle.growler.core.ui.toast.ToastFactory;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.github.orangegangsters.lollipin.lib.managers.AppLockActivity.EXT_DATA;

public class HintPasswordActivity extends BaseActivity {
    private static final String WORD_LIST = "word_list";
    /**
     * 设置密码
     */
    private static final int REQUEST_CODE_ENABLE = 11;

    private ArrayList<String> mBitcoinWordList;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, HintPasswordActivity.class));
    }

    public static void startActivity(Context context, ArrayList<String> list) {
        Intent intent = new Intent(context, HintPasswordActivity.class);
        intent.putStringArrayListExtra(WORD_LIST, list);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        mBitcoinWordList = getIntent().getStringArrayListExtra(WORD_LIST);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_hint_password;
    }

    public void onClick(View view) {
        PasswordManager.startEnterPassword(this, REQUEST_CODE_ENABLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String stringExtra = null;
        if (data != null) {
            stringExtra = data.getStringExtra(EXT_DATA);
        }
        switch (requestCode) {
            case REQUEST_CODE_ENABLE:
                if (stringExtra != null) {
                    if (mBitcoinWordList == null) {
                        createKey(stringExtra);
                    } else {
                        resetKey(stringExtra);
                    }
                }
                break;
        }
    }

    private void createKey(final String pass) {
        showProgress();
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
                        dismissProgress();
                        ToastFactory.getInstance(getApplicationContext()).makeTextShow("创建新用户失败",
                                Toast.LENGTH_SHORT);
                        finish();
                    }

                    @Override
                    public void onNext(Contract contract) {
                        contract.save();
                        dismissProgress();
                        startActivity(new Intent(HintPasswordActivity.this, MainActivity.class));
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
                                    CryptoManager.getInstance().sign(HintPasswordActivity.this,
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
                });
    }


    private void resetKey(String pass) {
        showProgress();
        CryptoManager.getInstance().resetBitcoinKeyPair(pass, mBitcoinWordList, new
                GenerateCallBack() {
                    @Override
                    public void onSuccess() {
                        dismissProgress();
                        ToastFactory.getInstance(getApplicationContext()).makeTextShow("用户恢复成功",
                                Toast
                                        .LENGTH_SHORT);
                        ActivityManager.getInstance().finishAll();
                        startActivity(new Intent(HintPasswordActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onError() {
                        dismissProgress();
                        ToastFactory.getInstance(getApplicationContext()).makeTextShow("用户恢复失败",
                                Toast
                                        .LENGTH_SHORT);
                        finish();
                    }
                });

    }
}
