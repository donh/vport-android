package com.a21vianet.wallet.vport.library.commom.http.ipfs;

import android.support.annotation.NonNull;

import com.a21vianet.wallet.vport.library.commom.http.ipfs.bean.AddResult;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.bean.UserInfoIPFS;
import com.a21vianet.wallet.vport.library.commom.http.ipfs.bean.UserInfoIPFSGET;
import com.google.gson.Gson;
import com.littlesparkle.growler.core.http.Request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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

    public Subscription set(
            Subscriber<RawTxResponse> subscriber,
            String senderAddress,
            String proxyAddress,
            String userInfo) {
        return mService.set(senderAddress, proxyAddress, "PROFILE", userInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public Observable<RawTxResponse> set(
            String senderAddress,
            String proxyAddress,
            String userInfo) {
        return mService.set(senderAddress, proxyAddress, "PROFILE", userInfo);
    }

    public Subscription get(Subscriber<UserInfoIPFSGET> subscriber, String senderAddress, String
            proxyAddress) {
        return mService.get(senderAddress, proxyAddress, "PROFILE")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public Subscription ipfsGetJson(Subscriber<String> subscriber, String hash) {
        return mService.ipfsGetJson(hash)
                .map(new Func1<ResponseBody, String>() {
                    @Override
                    public String call(ResponseBody responseBody) {
                        return getResponseBody(responseBody);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public Subscription ipfsAddJson(Subscriber<AddResult> subscriber,
                                    String json) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), json);
        MultipartBody.Part formData = MultipartBody.Part.createFormData("file", "ipfsjson",
                requestFile);
        return mService.ipfsAddJson(formData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public Observable<AddResult> ipfsAddJson(String json) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), json);
        MultipartBody.Part formData = MultipartBody.Part.createFormData("file", "ipfsjson",
                requestFile);
        return mService.ipfsAddJson(formData);
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

        @POST("/api/v0/add")
        @Multipart
        Observable<AddResult> ipfsAddJson(@Part MultipartBody.Part file);

        @Streaming
        @GET("/ipfs/{hash}")
        Observable<ResponseBody> ipfsGetJson(@Path("hash") String hash);
    }

    private String getResponseBody(ResponseBody body) {
        try {
            // 改成自己需要的存储位置
            InputStream inputStream = null;
            ByteArrayOutputStream outputStream = null;
            byte[] fileReader = new byte[4096];

            inputStream = body.byteStream();
            outputStream = new ByteArrayOutputStream();

            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
            }
            outputStream.flush();
            String json = outputStream.toString();
            outputStream.close();
            inputStream.close();
            return json;
        } catch (IOException e) {

        }
        return "";
    }
}
