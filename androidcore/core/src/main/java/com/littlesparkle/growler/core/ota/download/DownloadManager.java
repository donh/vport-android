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

import android.content.Context;

import com.coolerfall.download.DownloadRequest;
import com.coolerfall.download.OkHttpDownloader;
import com.coolerfall.download.Priority;
import com.littlesparkle.growler.core.utility.EncryptUtils;
import com.littlesparkle.growler.core.utility.FileUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DownloadManager implements DownLoad {
    /**
     * 表示需要下载的文件已经存在
     */
    public static final int FILE_EXIXTS = 0x001;
    private static final int THREEDPOOL_SIZE = 2;
    private static DownloadManager sDownloadManager;

    private List<DownloadCallback> mDownloadCallbackList = new ArrayList<>();
    private Map<Integer, String> DownLoadRecordMap = new HashMap<>();
    private com.coolerfall.download.DownloadManager mCoolerDownloadManager;

    private DownloadManager(Context context) {
        mCoolerDownloadManager =
                new com.coolerfall.download.DownloadManager
                        .Builder()
                        .context(context)
                        .downloader(OkHttpDownloader.create())
                        .threadPoolSize(THREEDPOOL_SIZE)
                        .build();
    }

    public static DownloadManager getInstance(Context context) {
        if (sDownloadManager == null) {
            synchronized (DownloadManager.class) {
                if (sDownloadManager == null) {
                    sDownloadManager = new DownloadManager(context);
                }
            }
        }
        return sDownloadManager;
    }

    public int getDownloadId(final String url) {
        int downloadId;
        for (Map.Entry<Integer, String> entry : DownLoadRecordMap.entrySet()) {
            if (entry.getValue().trim().equals(url.trim())) {
                downloadId = entry.getKey();
                return downloadId;
            }
        }
        return -1;
    }

    @Override
    public int startDownload(final String url, final String path, final String md5,
                             NetworkTypes types) {
        if (checkMd5(path, md5)) {
            //发出下载成功的消息(文件已存在无需下载)
            downloadSuccess(FILE_EXIXTS, path);
            return FILE_EXIXTS;
        }

        if (!sDownloadManager.mCoolerDownloadManager.isDownloading(url)) {
            //文件夹是否存在
            File file = new File(path);
            FileUtility.mkdir(file.getParentFile().getAbsolutePath());

            //文件存在删除重新下载
            if (file.exists()) {
                file.delete();
            }

            //判断允许在那种网络环境下下载
            int netWorkTypes;
            if (types == NetworkTypes.NETWORK_MOBILE) {
                netWorkTypes = DownloadRequest.NETWORK_MOBILE;
            } else {
                netWorkTypes = DownloadRequest.NETWORK_WIFI;
            }

            DownloadRequest request = new DownloadRequest.Builder()
                    .url(url)
                    .retryTime(5)
                    .retryInterval(5, TimeUnit.SECONDS)
                    .progressInterval(1, TimeUnit.SECONDS)
                    .priority(Priority.HIGH)
                    .allowedNetworkTypes(netWorkTypes)
                    .destinationFilePath(path)
                    .downloadCallback(mDownloadCallback)
                    .build();
            int downLoadId = sDownloadManager.mCoolerDownloadManager.add(request);
            DownLoadRecordMap.put(downLoadId, url);
            return downLoadId;
        } else {
            return getDownloadId(url);
        }
    }

    public void stopDownload(final String url) {
        stopDownload(getDownloadId(url));
    }

    @Override
    public void stopDownload(final int downloadId) {
        sDownloadManager.mCoolerDownloadManager.cancel(downloadId);
    }

    @Override
    public void addDownloadCallback(DownloadCallback callback) {
        if (callback == null) {
            return;
        }
        boolean repeat = false;
        for (DownloadCallback downloadCallback : mDownloadCallbackList) {
            if (downloadCallback == callback) {
                repeat = true;
                return;
            }
        }
        if (!repeat) {
            mDownloadCallbackList.add(callback);
        }
    }

    @Override
    public void removeDownloadCallback(DownloadCallback callback) {
        mDownloadCallbackList.remove(callback);
    }

    @Override
    public void removeAllDownloadCallback() {
        mDownloadCallbackList.clear();
    }

    com.coolerfall.download.DownloadCallback mDownloadCallback = new com.coolerfall.download
            .DownloadCallback() {
        @Override
        public void onStart(int downloadId, long totalBytes) {
            super.onStart(downloadId, totalBytes);
            for (DownloadCallback callback : mDownloadCallbackList) {
                if (callback != null) {
                    callback.onStart(downloadId, totalBytes);
                }
            }
        }

        @Override
        public void onRetry(int downloadId) {
            super.onRetry(downloadId);
        }

        @Override
        public void onProgress(int downloadId, long bytesWritten, long totalBytes) {
            super.onProgress(downloadId, bytesWritten, totalBytes);
            for (DownloadCallback callback : mDownloadCallbackList) {
                if (callback != null) {
                    callback.onProgress(downloadId, bytesWritten, totalBytes);
                }
            }
        }

        @Override
        public void onSuccess(int downloadId, String filePath) {
            super.onSuccess(downloadId, filePath);
            DownLoadRecordMap.remove(downloadId);
            downloadSuccess(downloadId, filePath);
        }

        @Override
        public void onFailure(int downloadId, int statusCode, String errMsg) {
            super.onFailure(downloadId, statusCode, errMsg);
            DownLoadRecordMap.remove(downloadId);
            for (DownloadCallback callback : mDownloadCallbackList) {
                if (callback != null) {
                    callback.onFailure(downloadId, statusCode, errMsg);
                }
            }
        }
    };

    private void downloadSuccess(int downloadId, String filePath) {
        for (DownloadCallback callback : mDownloadCallbackList) {
            if (callback != null) {
                callback.onSuccess(downloadId, filePath);
            }
        }
    }

    private boolean checkMd5(String path, String md5) {
        if (null == md5) {
            return false;
        }
        File file = new File(path);
        if (file.exists()) {
            if (md5.trim().equals(EncryptUtils.encryptMD5File2String(file))) {
                return true;
            } else {
                file.delete();
            }
        }
        return false;
    }

}
