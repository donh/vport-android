package com.a21vianet.wallet.vport.library.commom.http.vport;

import com.a21vianet.wallet.vport.library.commom.http.vport.bean.CertificationResult;
import com.google.gson.JsonObject;
import com.littlesparkle.growler.core.http.BaseHttpSubscriber;
import com.littlesparkle.growler.core.http.Request;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VPortRequest extends Request<VPortRequest.VPortApi> {

    public VPortRequest() {
        super();
    }

    public VPortRequest(String url) {
        super(url);
    }

    public Subscription login(BaseHttpSubscriber<LoginResponse> subscriber, String userJWT, String url) {

        JsonObject json = new JsonObject();
        json.addProperty("userJWT", userJWT);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json" +
                "charset=utf-8"), json.toString());

        return mService.login(url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public Subscription claim(BaseHttpSubscriber<ClaimResponse> subscriber, String claimJWT) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("claimJWT", claimJWT);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json" +
                "charset=utf-8"), jsonObject.toString());
        return mService.claim(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public Subscription certificate(BaseHttpSubscriber<CertificationResult> subscriber, String tx) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("attestationJWT", tx);

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json" +
                "charset=utf-8"), jsonObject.toString());
        return mService.certificate(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }


    @Override
    protected Class<VPortApi> getServiceClass() {
        return VPortApi.class;
    }

    interface VPortApi {

        @POST
        @Headers({"Content-Type: application/json"})
        Observable<LoginResponse> login(
                @Url String path,
                @Body RequestBody login
        );

        @POST("claims/add")
        @Headers({"Content-Type: application/json"})
        Observable<ClaimResponse> claim(
                @Body RequestBody claim
        );

        @POST("attestations")
        @Headers({"Content-Type: application/json"})
        Observable<CertificationResult> certificate(
                @Body RequestBody claim
        );
    }
}
