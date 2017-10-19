package com.a21vianet.wallet.vport.action.info.data;

import android.support.annotation.NonNull;

import com.littlesparkle.growler.core.http.Request;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class PersonalInfoRequest extends Request<PersonalInfoRequest.PersonalInfoApi> {

    public PersonalInfoRequest() {
        super();
    }

    public PersonalInfoRequest(@NonNull String baseUrl) {
        super(baseUrl);
    }

    public Subscription setThumbToIPFS(Subscriber<IPFSResponse> subscriber, File thumb) {
        MultipartBody.Part photo = null;
        if (thumb != null) {
            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/*"), thumb);
            photo = MultipartBody.Part.createFormData("thumb_pic", System.currentTimeMillis() + "_header.jpg", photoRequestBody);
        }
        return mService.setThumbToIPFS(photo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }


    @Override
    protected Class<PersonalInfoApi> getServiceClass() {
        return PersonalInfoApi.class;
    }

    interface PersonalInfoApi {

        @POST("api/v0/add")
        @Multipart
        Observable<IPFSResponse> setThumbToIPFS(
                @Part MultipartBody.Part thumb_pic
        );

    }

}
