package com.a21vianet.wallet.vport.library.commom.crypto;

import android.content.Context;

import com.a21vianet.quincysx.library.crypto.BitcoinKeyPair;
import com.a21vianet.quincysx.library.crypto.BitcoinKeyPairGenerator;
import com.a21vianet.quincysx.library.crypto.DHCreate;
import com.a21vianet.quincysx.library.crypto.DatatypeConverter;
import com.a21vianet.quincysx.library.crypto.MultiSigBitcoinAddressGenerator;
import com.a21vianet.quincysx.library.crypto.crypto.mnemonic.MnemonicCode;
import com.a21vianet.quincysx.library.crypto.crypto.mnemonic.MnemonicException;
import com.a21vianet.quincysx.library.crypto.messagedigesters.MessageDigestAlgorithm;
import com.a21vianet.quincysx.library.crypto.messagedigesters.MessageDigester;
import com.a21vianet.quincysx.library.crypto.script.Script;
import com.a21vianet.quincysx.library.crypto.script.ScriptBuilder;
import com.a21vianet.wallet.vport.library.BaseApplication;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.BitcoinKey;
import com.a21vianet.wallet.vport.library.commom.crypto.callback.GenerateCallBack;
import com.a21vianet.wallet.vport.library.commom.crypto.callback.MultisigCallback;
import com.a21vianet.wallet.vport.library.commom.crypto.callback.OnFinishedListener;
import com.a21vianet.wallet.vport.library.commom.crypto.callback.OnVerifiedListener;
import com.google.gson.Gson;
import com.littlesparkle.growler.core.utility.PrefUtility;
import com.scottyab.aescrypt.AESCrypt;

import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wang.rongqiang on 2017/6/5.
 * 产生私钥公钥
 */

public final class CryptoManager {
    private final static String SAVE_KEY_FLAG = "save_key_flag";
    private BitcoinKeyPairGenerator generator;
    private MultiSigBitcoinAddressGenerator multiSigBitcoinAddressGenerator;
    private BitcoinKey mBitcoinKey;
    private Gson mGson = new Gson();
    //加密的资源
    private String keyInfoStr;

    private static volatile CryptoManager mCryptoManager;

    private CryptoManager() {
        try {
            initKeyInfoStr();
            initGenerator();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    public static CryptoManager getInstance() {
        if (mCryptoManager == null) {
            synchronized (CryptoManager.class) {
                if (mCryptoManager == null) {
                    mCryptoManager = new CryptoManager();
                }
            }
        }
        return mCryptoManager;
    }


    /**
     * 初始化公私钥生成器
     *
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws InvalidAlgorithmParameterException
     */
    private void initGenerator() throws NoSuchAlgorithmException, NoSuchProviderException,
            InvalidAlgorithmParameterException {
        if (multiSigBitcoinAddressGenerator == null || generator == null) {
            SecureRandom secureRandom = new SecureRandom();
            Provider provider = new BouncyCastleProvider();
            Set<MessageDigestAlgorithm> messageDigestAlgorithms = new HashSet<>(Arrays.asList
                    (MessageDigestAlgorithm.values()));

            MessageDigester messageDigester = new MessageDigester(messageDigestAlgorithms,
                    provider);

            if (generator == null) {
                generator = new BitcoinKeyPairGenerator(secureRandom, provider, messageDigester);
            }
            if (multiSigBitcoinAddressGenerator == null) {
                multiSigBitcoinAddressGenerator = new MultiSigBitcoinAddressGenerator
                        (messageDigester);
            }
        }
    }

    /**
     * 生成密钥对
     *
     * @param password 密码
     */
    public void generateBitcoinKeyPair(final String password, final GenerateCallBack callBack) {
        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        try {
                            MnemonicCode.setInstance(new MnemonicCodeAndroid());
                            DHCreate hdAccount = new DHCreate(MnemonicCode.instance());
                            KeyPair keyPair = generator.generateEcdsaKeyPair(hdAccount.getPrivKey
                                    ());
                            BitcoinKeyPair bitcoinKeyPair = generator.generateBitcoinKeyPair
                                    (keyPair);

                            String rawpric = generator.generateRawBitcoinPrivateKey(
                                    (ECPrivateKey) keyPair.getPrivate());

                            mBitcoinKey = new BitcoinKey(rawpric, bitcoinKeyPair
                                    .getPrivateKey(), bitcoinKeyPair.getPublicKey(), hdAccount
                                    .getWords());
                            encrypt(password, mBitcoinKey);
                            subscriber.onNext("");
                        } catch (IOException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        } catch (MnemonicException.MnemonicLengthException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        callBack.onSuccess();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        callBack.onError();
                    }
                });
    }

    /**
     * 生成密钥对 RxJava 样式
     *
     * @param password 密码
     */
    public Observable<String> generateBitcoinKeyPair(final String password) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {

                generateBitcoinKeyPair(password, new GenerateCallBack() {
                    @Override
                    public void onSuccess() {
                        subscriber.onNext("");
                    }

                    @Override
                    public void onError() {
                        subscriber.onError(new Exception("密钥对生成失败"));
                    }
                });
            }
        });
    }

    /**
     * 通过助记词回复私钥
     *
     * @param words
     */
    public void resetBitcoinKeyPair(final String password, final List<String> words, final
    GenerateCallBack
            callBack) {
        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        try {
                            MnemonicCode.setInstance(new MnemonicCodeAndroid());
                            BigInteger bigInteger = DHCreate.resetPrivKey(words);
                            KeyPair keyPair = generator.generateEcdsaKeyPair(bigInteger);
                            BitcoinKeyPair bitcoinKeyPair = generator.generateBitcoinKeyPair
                                    (keyPair);

                            String rawpriv = generator.generateRawBitcoinPrivateKey(
                                    (ECPrivateKey) keyPair.getPrivate());

                            mBitcoinKey = new BitcoinKey(rawpriv, bitcoinKeyPair
                                    .getPrivateKey(), bitcoinKeyPair.getPublicKey(), words);
                            encrypt(password, mBitcoinKey);
                            subscriber.onNext("");
                        } catch (IOException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        callBack.onSuccess();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        callBack.onError();
                    }
                });
    }

    /**
     * 通过助记词回复私钥 RxJava 样式
     *
     * @param words
     */
    public Observable<String> resetBitcoinKeyPair(final String password, final List<String> words) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                resetBitcoinKeyPair(password, words, new GenerateCallBack() {
                    @Override
                    public void onSuccess() {
                        subscriber.onNext("");
                    }

                    @Override
                    public void onError() {
                        subscriber.onError(new Exception("生成失败"));
                    }
                });
            }
        });
    }

    /**
     * 生成地址
     *
     * @return
     */
    public String generateBitcoinAddress() throws NoDecryptException {
        checkEnCode();
        return generator.generateBitcoinAddress(mBitcoinKey.getPubKey());
    }

    /**
     * 生成多重签名地址
     *
     * @return
     */
    public String generateMultiSigAddress() throws NoDecryptException {
        checkEnCode();
        return multiSigBitcoinAddressGenerator.generateMultiSigAddress(generateMultiSigScript
                (mBitcoinKey.getPubKey()));
    }

    /**
     * 签名
     *
     * @param context
     * @param tx
     * @param callback
     */
    public void sign(Context context, final String tx, final
    MultisigCallback callback) throws NoDecryptException {
        checkEnCode();
        Signer.sign(context, mBitcoinKey.getPrivKey(), tx, new OnFinishedListener() {
            @Override
            public void onFinished(String s) {
                callback.onSinged(s);
            }
        });
    }

    /**
     * 签名 RxJava 样式
     *
     * @param context
     * @param tx
     */
    public Observable<String> sign(final Context context, final String tx) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                try {
                    checkEnCode();
                } catch (NoDecryptException e) {
                    subscriber.onError(e);
                }
                Signer.sign(context, mBitcoinKey.getPrivKey(), tx, new
                        OnFinishedListener() {
                            @Override
                            public void onFinished(String s) {
                                subscriber.onNext(s);
                            }
                        });
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.io());
    }

    /**
     * 多重签名
     *
     * @param context
     * @param tx
     * @param callback
     */
    public void signMultisig(Context context, final String tx,
                             final MultisigCallback callback) throws NoDecryptException {
        checkEnCode();
        String script = generateMultiSigScriptStr(mBitcoinKey.getPubKey());
        Signer.signMultisig(context, mBitcoinKey.getPrivKey(), script, tx, new OnFinishedListener
                () {
            @Override
            public void onFinished(String s) {
                callback.onSinged(s);
            }
        });
    }

    /**
     * 多重签名 RxJava 样式
     *
     * @param context
     * @param tx
     */
    public Observable<String> signMultisig(final Context context, final String tx) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                try {
                    checkEnCode();
                } catch (NoDecryptException e) {
                    subscriber.onError(e);
                }
                String script = generateMultiSigScriptStr(mBitcoinKey.getPubKey());
                Signer.signMultisig(context, mBitcoinKey.getPrivKey(), script, tx,
                        new OnFinishedListener() {
                            @Override
                            public void onFinished(String s) {
                                subscriber.onNext(s);
                            }
                        });
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.io());
    }

    /**
     * 签名消息
     *
     * @param context
     * @param msg
     * @param callback
     * @throws NoDecryptException
     */
    public void signMessage(final Context context, String msg, final
    MultisigCallback callback) throws NoDecryptException {
        checkEnCode();
        Signer.signMessage(context, mBitcoinKey.getPrivKey(), msg, new OnFinishedListener() {
            @Override
            public void onFinished(String s) {
                callback.onSinged(s);
            }
        });
    }

    /**
     * 签名消息 RxJava 样式
     *
     * @param context
     * @param msg
     * @throws NoDecryptException
     */
    public Observable<String> signMessage(final Context context, final String msg) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                try {
                    checkEnCode();
                } catch (NoDecryptException e) {
                    e.printStackTrace();
                }
                Signer.signMessage(context, mBitcoinKey.getPrivKey(), msg,
                        new OnFinishedListener() {
                            @Override
                            public void onFinished(String s) {
                                subscriber.onNext(s);
                            }
                        });
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.io());
    }

    /**
     * JWT 签名
     *
     * @param context
     * @param jsonStr
     * @param listener
     * @throws NoDecryptException
     */
    public void signJWTToken(final Context context, String jsonStr, OnFinishedListener listener)
            throws NoDecryptException {
        checkEnCode();
        JWT.signToken(context, mBitcoinKey.getRawPrivKey(), jsonStr, listener);
    }

    /**
     * JWT 解密
     *
     * @param context
     * @param tokenString
     * @param listener
     */
    public void decodeJWTToken(final Context context, String tokenString, OnFinishedListener
            listener) {
        JWT.decodeToken(context, tokenString, listener);
    }

    /**
     * JWT 验证签名
     *
     * @param context
     * @param pubkey
     * @param jsonStr
     * @param listener
     * @throws NoDecryptException
     */
    public void verifyJWTToken(final Context context, String pubkey, String jsonStr,
                               OnVerifiedListener listener)
            throws NoDecryptException {
        checkEnCode();
        JWT.verifyToken(context, pubkey, jsonStr, listener);
    }

    /**
     * JWT 验证签名
     *
     * @param context
     * @param jsonStr
     * @param listener
     * @throws NoDecryptException
     */
    public void verifyJWTToken(final Context context, String jsonStr, OnVerifiedListener
            listener) throws NoDecryptException {
        verifyJWTToken(context, mBitcoinKey.getPubKey(), jsonStr, listener);
    }

    /**
     * 是否存在公私钥
     *
     * @return true:存在  false：不存在
     */
    public boolean isExistsKey() {
        if (keyInfoStr.trim().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否加密过
     *
     * @return true：已加密  false：没有加密
     */
    public boolean isEncrypted() {
        if (mBitcoinKey == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 解密
     *
     * @param password
     */
    public void decrypt(String password) throws GeneralSecurityException {
        if (password == null) {
            return;
        }
        initKeyInfoStr();
        if (keyInfoStr.trim().equals("")) {
            return;
        }
        String s = com.scottyab.aescrypt.AESCrypt.decrypt(password, keyInfoStr);
        mBitcoinKey = mGson.fromJson(s, BitcoinKey.class);
    }

    /**
     * 加密
     *
     * @param password
     */
    public void encrypt(String password) {
        if (password == null) {
            return;
        }
        if (mBitcoinKey == null) {
            return;
        }
        try {
            keyInfoStr = AESCrypt.encrypt(password, mGson.toJson(mBitcoinKey));
            PrefUtility.setString(BaseApplication.getContext(), SAVE_KEY_FLAG, keyInfoStr);
        } catch (GeneralSecurityException e) {
            //handle error
        }
    }

    /**
     * 加密
     *
     * @param password
     * @param bitcoinKey
     */
    public void encrypt(String password, BitcoinKey bitcoinKey) {
        if (password == null) {
            return;
        }
        if (bitcoinKey == null) {
            return;
        }
        try {
            keyInfoStr = AESCrypt.encrypt(password, mGson.toJson(bitcoinKey));
            PrefUtility.setString(BaseApplication.getContext(), SAVE_KEY_FLAG, keyInfoStr);
        } catch (GeneralSecurityException e) {
            //handle error
        }
    }

    public void cleanBitcoinKey() {
        keyInfoStr = "";
        mBitcoinKey = null;
        PrefUtility.delete(BaseApplication.getContext(), SAVE_KEY_FLAG);
    }

    /**
     * 获得私钥密钥等
     *
     * @return
     */
    public BitcoinKey getCoinKey() throws NoDecryptException {
        checkEnCode();
        return mBitcoinKey;
    }

    /**
     * 检查私钥是否解密
     */
    private void checkEnCode() throws NoDecryptException {
        if (mBitcoinKey == null) {
            throw new NoDecryptException();
        }
    }

    /**
     * 生成Script
     *
     * @param pubkey
     * @return
     */
    private Script generateMultiSigScript(String pubkey) {
        List<byte[]> publicKeys = new LinkedList<>();
        publicKeys.add(DatatypeConverter.parseHexBinary
                (pubkey));
        return ScriptBuilder.createMultiSigOutputScript(publicKeys.size(), publicKeys);
    }

    /**
     * 生成Script字符串
     *
     * @param pubkey
     * @return
     */
    private String generateMultiSigScriptStr(String pubkey) {
        Script script = generateMultiSigScript(pubkey);
        return DatatypeConverter.printHexBinary(script.getProgram());
    }


    /**
     * 初始化加密的字符串
     */
    private void initKeyInfoStr() {
        keyInfoStr = PrefUtility.getString(BaseApplication.getContext(), SAVE_KEY_FLAG, "");
    }
}
