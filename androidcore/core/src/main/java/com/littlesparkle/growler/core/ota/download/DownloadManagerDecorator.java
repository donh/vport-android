package com.littlesparkle.growler.core.ota.download;

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

public abstract class DownloadManagerDecorator implements DownLoad {
    protected DownLoad mDownloadManager;

    public DownloadManagerDecorator(DownLoad downloadManager) {
        mDownloadManager = downloadManager;
    }

    public abstract void startDownload(String url, String path, NetworkTypes types, String md5,
                                       NotificationState notificationShow);

    @Override
    @Deprecated
    public int startDownload(String url, String path, String md5, NetworkTypes types) {
        return mDownloadManager.startDownload(url, path, md5, types);
    }

    @Override
    public void stopDownload(int downloadId) {
        mDownloadManager.stopDownload(downloadId);
    }

    @Override
    public void addDownloadCallback(DownloadCallback callback) {
        mDownloadManager.addDownloadCallback(callback);
    }

    @Override
    public void removeDownloadCallback(DownloadCallback callback) {
        mDownloadManager.removeDownloadCallback(callback);
    }

    @Override
    public void removeAllDownloadCallback() {
        mDownloadManager.removeAllDownloadCallback();
    }

    @Override
    public void stopDownload(String url) {
        mDownloadManager.stopDownload(url);
    }

    @Override
    public int getDownloadId(String url) {
        return mDownloadManager.getDownloadId(url);
    }
}
