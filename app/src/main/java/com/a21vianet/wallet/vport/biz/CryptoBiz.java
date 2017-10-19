package com.a21vianet.wallet.vport.biz;

import com.a21vianet.wallet.vport.http.Api;
import com.a21vianet.wallet.vport.library.BaseApplication;
import com.a21vianet.wallet.vport.library.commom.crypto.CryptoManager;
import com.a21vianet.wallet.vport.library.commom.crypto.NoDecryptException;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.Contract;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.IPFSRequest;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.RawTxResponse;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.bean.AddResult;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.bean.UserInfoIPFS;
import com.a21vianet.wallet.vport.library.commom.http.transaction.TransactionRequest;
import com.a21vianet.wallet.vport.library.commom.http.transaction.bean.RawTxSignedResponse;
import com.a21vianet.wallet.vport.library.event.ChangeUserInfoEvent;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import rx.Observable;
import rx.functions.Action0;
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
        final String json = new Gson().toJson(userInfoIPFS);
        return new IPFSRequest(Api.IPFSApi).ipfsAddJson(json)
                .map(new Func1<AddResult, String>() {
                    @Override
                    public String call(AddResult addResult) {
                        contract.setIpfsHex(addResult.getHash());
                        return addResult.getHash();
                    }
                })
                .flatMap(new Func1<String, Observable<RawTxResponse>>() {
                    @Override
                    public Observable<RawTxResponse> call(final String rawtx) {
                        String address = "";
                        try {
                            address = CryptoManager.getInstance().generateBitcoinAddress();
                        } catch (NoDecryptException e) {
                            e.printStackTrace();
                        }
                        return new IPFSRequest().set(address, contract.getProxy(), json);
                    }
                })
                .flatMap(new Func1<RawTxResponse, Observable<String>>() {
                    @Override
                    public Observable<String> call(final RawTxResponse rawTxResponse) {
                        return CryptoManager.getInstance().sign(BaseApplication.getContext(),
                                rawTxResponse.rawTx);
                    }
                })
                .flatMap(new Func1<String, Observable<RawTxSignedResponse>>() {
                    @Override
                    public Observable<RawTxSignedResponse> call(final String rawTx) {
                        return new TransactionRequest().getService().signed(rawTx)
                                .doOnSubscribe(new Action0() {
                                    @Override
                                    public void call() {
                                        EventBus.getDefault().post(new ChangeUserInfoEvent
                                                (userInfoIPFS));
                                    }
                                });
                    }
                })
                .map(new Func1<RawTxSignedResponse, Contract>() {
                    @Override
                    public Contract call(RawTxSignedResponse rawTxSignedResponse) {
                        return contract;
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}
