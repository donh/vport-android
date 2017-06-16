package com.a21vianet.wallet.vport.library.commom.http.vchain;

import android.support.annotation.NonNull;

import com.a21vianet.wallet.vport.library.commom.http.vchain.bean.TxIdResponse;
import com.a21vianet.wallet.vport.library.commom.http.vchain.bean.VPortCreateResponse;
import com.littlesparkle.growler.core.http.Request;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wang.rongqiang on 2017/6/14.
 */

public class VChainRequest extends Request<VChainRequest.VChainApi> {
    public VChainRequest() {
    }

    public VChainRequest(@NonNull String baseUrl) {
        super(baseUrl);
    }

    @Override
    protected Class<VChainApi> getServiceClass() {
        return VChainApi.class;
    }

    public Subscription sendToAddress(Subscriber<TxIdResponse> subscriber, String address) {
        return mService.sendToAddress(address, 100, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public Subscription create(Subscriber<CreateResponse> subscriber, String senderAddress,
                               String userKey,
                               String delegates) {
        return mService.create(senderAddress, userKey
                , delegates)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public Subscription transaction(Subscriber<TransactionResponse> subscriber, String
            senderAddress, String userKey, String rawTxSigned) {
        return mService.transaction(senderAddress, userKey, rawTxSigned)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public Subscription vPortCreate(Subscriber<VPortCreateResponse> subscriber, String
            json) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; " +
                "charset=utf-8"), json);
        return mService.vPortCreate(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    interface VChainApi {
        @GET("sendtoaddress/{address}/{amount}/{color}")
        Observable<TxIdResponse> sendToAddress(
                @Path("address") String address,
                @Path("amount") int zmount,
                @Path("color") int color
        );

        @POST("vport/identityFactory/create")
        @FormUrlEncoded
        Observable<CreateResponse> create(
                @Field("senderAddress") String senderAddress,
                @Field("userKey") String userKey,
                @Field("delegates") String delegates
        );

        @POST("vport/identityFactory/transaction")
        @FormUrlEncoded
        Observable<TransactionResponse> transaction(
                @Field("senderAddress") String senderAddress,
                @Field("userKey") String userKey,
                @Field("rawTxSigned") String rawTxSigned
        );

        @Headers({"Content-Type: application/json", "Accept: application/json"})
        @POST("api/v1/users/add")
        Observable<VPortCreateResponse> vPortCreate(
                @Body RequestBody route
        );
    }
}



