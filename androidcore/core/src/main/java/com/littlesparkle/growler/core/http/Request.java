package com.littlesparkle.growler.core.http;

import android.support.annotation.NonNull;

import com.littlesparkle.growler.core.http.api.Api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * Copyright (C) 2016-2016, The Little-Sparkle Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS-IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public abstract class Request<T> {

    protected static Retrofit mRetrofit = null;

    protected T mService = null;

    public T getService() {
        return mService;
    }

    public Request() {
        this(Api.getBaseUrl());
    }

    public Request(@NonNull String baseUrl) {
        synchronized (Request.class) {
            if (mRetrofit == null) {
                mRetrofit = new Retrofit.Builder()
                        .client(createHttpClientBuilder().build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .baseUrl(baseUrl)
                        .build();

            } else {
                mRetrofit = mRetrofit.newBuilder().baseUrl(baseUrl).build();
            }
        }
        mService = mRetrofit.create(getServiceClass());
    }

    protected OkHttpClient.Builder createHttpClientBuilder() {
        okhttp3.logging.HttpLoggingInterceptor httpLoggingInterceptor = new
                HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .connectTimeout(Api.getTimeout(), TimeUnit.SECONDS)
                .readTimeout(Api.getTimeout(), TimeUnit.SECONDS)
                .writeTimeout(Api.getTimeout(), TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor);
    }

    protected abstract Class<T> getServiceClass();
}
