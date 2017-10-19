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

import com.littlesparkle.growler.core.log.Logger;
import com.littlesparkle.growler.core.ota.OtaEvent;
import com.littlesparkle.growler.core.utility.DownloadNotificationUtility;
import com.littlesparkle.growler.core.utility.PackageUtility;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import static com.littlesparkle.growler.core.ota.download.DownloadManager.FILE_EXIXTS;

public class NotificationDownloadManager extends DownloadManagerDecorator {
    private static NotificationDownloadManager sNotificationDownloadManager;
    private NotificationState notificationShowTemp;
    private Context mContext;
    private Map<Integer, NotificationState> mNotificationStateMap = new HashMap<>();

    public static NotificationDownloadManager getInstance(Context context) {
        if (sNotificationDownloadManager == null) {
            synchronized (NotificationDownloadManager.class) {
                if (sNotificationDownloadManager == null) {
                    DownloadManager instance = DownloadManager.getInstance(context);
                    sNotificationDownloadManager = new NotificationDownloadManager(instance);
                    sNotificationDownloadManager.mContext = context;
                }
            }
        }
        return sNotificationDownloadManager;
    }

    private NotificationDownloadManager(DownLoad downLoad) {
        super(downLoad);
        mDownloadManager.addDownloadCallback(sDownloadCallback);
    }

    @Override
    public void startDownload(String url, String path, NetworkTypes types, String md5,
                              NotificationState notificationShow) {
        notificationShowTemp = notificationShow;
        int downloadId = mDownloadManager.startDownload(url, path, md5, types);
        if (downloadId != FILE_EXIXTS) {
            mNotificationStateMap.put(downloadId, notificationShow);
        }
    }

    private DownloadCallback sDownloadCallback = new DownloadCallback() {
        @Override
        public void onStart(int downloadId, long totalBytes) {
            NotificationState notificationState = getNotificationState(downloadId);
            if (notificationState.isNotificationShow()) {
                //显示通知栏消息
                DownloadNotificationUtility.getInstance(mContext).showNotification(downloadId,
                        notificationState.getNotificationTitle());
            }
        }

        @Override
        public void onProgress(int downloadId, long bytesWritten, long totalBytes) {
            NotificationState notificationState = getNotificationState(downloadId);
            if (notificationState.isNotificationShow()) {
                //显示通知栏消息
                int schedule = (int) ((bytesWritten / 1f / totalBytes) * 100);
                Logger.e("========正在下载=======downloadId=" + downloadId + "    schedule=" +
                        schedule);
                if (DownloadNotificationUtility.getInstance(mContext).isNotificationExists
                        (downloadId)) {
                    //此通知栏存在
                    DownloadNotificationUtility.getInstance(mContext).updateProgress(downloadId,
                            schedule);
                } else {
                    //此通知栏不存在

                    DownloadNotificationUtility.getInstance(mContext).showNotification(downloadId,
                            notificationState.getNotificationTitle());
                }
            }
        }

        @Override
        public void onSuccess(int downloadId, String filePath) {
            DownloadNotificationUtility.getInstance(mContext).cancel(downloadId);
            NotificationState notificationState = getNotificationState(downloadId);
            if (notificationState.isInstallNow()) {
                //立即安装
                PackageUtility.installPackage(mContext, filePath);
            } else {
                //显示通知栏消息用户自行安装
                EventBus.getDefault().post(
                        new OtaEvent(OtaEvent.PRE_INSTALL_OTA_PACKAGE,
                                null, filePath));
            }
            mNotificationStateMap.remove(downloadId);
        }
    };

    private NotificationState getNotificationState(int downloadId) {
        NotificationState notificationState = mNotificationStateMap.get(downloadId);
        if (notificationState == null) {
            notificationState = notificationShowTemp;
        }
        return notificationState;
    }
}
