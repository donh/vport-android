package com.a21vianet.wallet.vport.library.commom.http.ipfs;

import android.support.annotation.NonNull;

import com.a21vianet.wallet.vport.library.commom.http.ipfs.bean.UserInfoIPFS;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.bean.UserInfoIPFSGET;
import com.google.gson.Gson;
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

public class IPFSRequest extends Request<IPFSRequest.IPFSApi> {
    @Override
    protected Class<IPFSApi> getServiceClass() {
        return IPFSApi.class;
    }

    public IPFSRequest() {
        super();
    }

    public IPFSRequest(@NonNull String baseUrl) {
        super(baseUrl);
    }

    public Subscription set(
            Subscriber<RawTxResponse> subscriber,
            String senderAddress,
            String proxyAddress,
            UserInfoIPFS userInfo) {
        return mService.set(senderAddress, proxyAddress, "PROFILE", new Gson().toJson(userInfo))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public Subscription get(Subscriber<UserInfoIPFSGET> subscriber, String senderAddress, String proxyAddress) {
        return mService.get(senderAddress, proxyAddress, "PROFILE")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    interface IPFSApi {
        @POST("vport/registry/set")
        @FormUrlEncoded
        Observable<RawTxResponse> set(
                @Field("senderAddress") String senderAddress,
                @Field("proxyAddress") String proxyAddress,
                @Field("registrationIdentifier") String registrationIdentifier,
                @Field("value") String value
        );

        @POST("vport/registry/get")
        @FormUrlEncoded
        Observable<UserInfoIPFSGET> get(
                @Field("senderAddress") String senderAddress,
                @Field("proxyAddress") String proxyAddress,
                @Field("registrationIdentifier") String registrationIdentifier
        );
    }
}
