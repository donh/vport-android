package com.littlesparkle.growler.core.ota;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.littlesparkle.growler.core.http.BaseHttpSubscriber;
import com.littlesparkle.growler.core.ota.download.DownloadCallback;
import com.littlesparkle.growler.core.ota.download.DownloadManagerDecorator;
import com.littlesparkle.growler.core.ota.download.NetworkTypes;
import com.littlesparkle.growler.core.ota.download.NotificationDownloadManager;
import com.littlesparkle.growler.core.ota.download.NotificationState;
import com.littlesparkle.growler.core.utility.PackageUtility;

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

public class OtaManager {
    private static String sUpdateApkLocation =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/update.apk";
    private String mPackageName;
    private int mVersionCode;
    private DownloadManagerDecorator mDownloaManager;

    public OtaManager(Context context) {
        mPackageName = PackageUtility.selfPackageName(context);
        mVersionCode = PackageUtility.selfVersionCode(context);
        mDownloaManager = NotificationDownloadManager.getInstance(context);
    }

    public void isUpdate(final UpdateResultCallback updateResultCallback) {
        new OtaRequest().update(new BaseHttpSubscriber<OtaResponse>() {
            @Override
            public void onNext(final OtaResponse otaResponse) {
                if (otaResponse.data.ota == null || !otaResponse.data.ota.isValidated()) {
                    updateResultCallback.onCheckResult(false, null);
                } else {
                    updateResultCallback.onCheckResult(true, otaResponse.data.ota);
                }
            }
        }, "Android", mPackageName, "", mVersionCode);
    }

    public void download(@NonNull final String url, String path,@NonNull NotificationState state, String md5,
                         DownloadCallback callback) {
        if (path == null) {
            path = sUpdateApkLocation;
        }
        mDownloaManager.startDownload(url, path, NetworkTypes.NETWORK_WIFI,md5, state);
        mDownloaManager.addDownloadCallback(callback);
    }

    public interface UpdateResultCallback {
        void onCheckResult(Boolean aBoolean, OtaPackageInfo otaPackageInfo);
    }
}
