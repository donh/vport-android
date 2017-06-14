package com.a21vianet.wallet.vport.library.commom.http.transaction;

import android.support.annotation.NonNull;

import com.a21vianet.wallet.app.commom.http.transaction.bean.RawTxSignedResponse;
import com.littlesparkle.growler.core.http.Request;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wang.rongqiang on 2017/6/9.
 */

public class TransactionRequest extends Request<TransactionRequest.TransactionApi> {

    public TransactionRequest() {
    }

    public TransactionRequest(@NonNull String baseUrl) {
        super(baseUrl);
    }

    @Override
    protected Class<TransactionApi> getServiceClass() {
        return TransactionApi.class;
    }

    public Subscription signed(Subscriber<RawTxSignedResponse> subscriber, String rawTxSigned) {
        return mService.signed(rawTxSigned)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    interface TransactionApi {
        @POST("vchain/transaction/send")
        @FormUrlEncoded
        Observable<RawTxSignedResponse> signed(
                @Field("rawTxSigned") String rawTxSigned
        );
    }
}
