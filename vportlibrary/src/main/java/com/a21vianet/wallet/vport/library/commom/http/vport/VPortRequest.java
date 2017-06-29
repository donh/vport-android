package com.a21vianet.wallet.vport.library.commom.http.vport;

import com.littlesparkle.growler.core.http.BaseHttpSubscriber;
import com.littlesparkle.growler.core.http.Request;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VPortRequest extends Request<VPortRequest.VPortApi> {

    public VPortRequest(String url) {
        super(url);
    }

    public Subscription login(BaseHttpSubscriber<LoginResponse> subscriber, String userJWT) {
        return mService.login(userJWT)
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

        @POST(" ")
        @FormUrlEncoded
        Observable<LoginResponse> login(
                @Field("userJWT") String userJWT
        );

    }
}
