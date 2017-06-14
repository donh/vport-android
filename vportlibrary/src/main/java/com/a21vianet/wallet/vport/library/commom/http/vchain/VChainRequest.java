package com.a21vianet.wallet.vport.library.commom.http.vchain;

import android.support.annotation.NonNull;

import com.a21vianet.wallet.vport.library.commom.http.vchain.bean.TxIdResponse;
import com.littlesparkle.growler.core.http.Request;

import retrofit2.http.GET;
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

    interface VChainApi {
        @GET("sendtoaddress/{address}/{amount}/{color}")
        Observable<TxIdResponse> sendToAddress(
                @Path("address") String address,
                @Path("amount") int zmount,
                @Path("color") int color
        );
    }
}
