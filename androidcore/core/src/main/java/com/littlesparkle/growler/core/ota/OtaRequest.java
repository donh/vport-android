package com.littlesparkle.growler.core.ota;

import android.support.annotation.NonNull;

import com.littlesparkle.growler.core.http.Request;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

public class OtaRequest extends Request<OtaRequest.UpdateApi> {

    @Override
    protected Class getServiceClass() {
        return UpdateApi.class;
    }

    public Subscription update(@NonNull Subscriber<OtaResponse> subscriber,
                               String platform, String identification, String version_name, int
                                       version_code) {
        return mService.checkUpdate(platform, identification, version_name, version_code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public interface UpdateApi {
        @POST("ota/update")
        @FormUrlEncoded
        Observable<OtaResponse> checkUpdate(
                @Field("platform") String platform,
                @Field("identification") String identification,
                @Field("version_name") String version_name,
                @Field("version_code") int version_code
        );
    }
}


