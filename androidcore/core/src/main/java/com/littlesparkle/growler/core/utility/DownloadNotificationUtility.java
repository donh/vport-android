package com.littlesparkle.growler.core.utility;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.littlesparkle.growler.core.R;
import com.littlesparkle.growler.core.log.Logger;

import java.util.HashMap;
import java.util.Map;

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

public class DownloadNotificationUtility {
    private static DownloadNotificationUtility mDownloadNotificationUtil;

    public static DownloadNotificationUtility getInstance(Context context) {
        if (mDownloadNotificationUtil == null) {
            synchronized (DownloadNotificationUtility.class) {
                if (mDownloadNotificationUtil == null) {
                    mDownloadNotificationUtil = new DownloadNotificationUtility(context);
                }
            }
        }
        return mDownloadNotificationUtil;
    }

    private Context mContext;
    // NotificationManager ： 是状态栏通知的管理类，负责发通知、清楚通知等。
    private NotificationManager manager;
    // 定义Map来保存Notification对象
    private Map<Integer, Notification.Builder> map = null;

    private DownloadNotificationUtility(Context context) {
        this.mContext = context;
        // NotificationManager 是一个系统Service，必须通过 getSystemService()方法来获取。
        manager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        map = new HashMap<Integer, Notification.Builder>();
    }

    /**
     * 显示通知栏
     *
     * @param notificationId
     * @param title          通知栏标题
     */
    public void showNotification(int notificationId, String title) {
        // 判断对应id的Notification是否已经显示， 以免同一个Notification出现多次
        if (!map.containsKey(notificationId)) {
            // 创建通知对象
            Bitmap LargeBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap
                    .ic_launcher);
            Notification.Builder mBuilder = new Notification.Builder(mContext);
            mBuilder.setContentTitle(title)
                    //标题
                    //收到信息后状态栏显示的文字信息
                    .setWhen(System.currentTimeMillis())           //设置通知时间
                    .setSmallIcon(R.mipmap.ic_launcher)            //设置小图标
                    .setLargeIcon(LargeBitmap)
                    .setProgress(100, 0, false) //设置大图标
                    .setAutoCancel(false);
            //设置点击后取消Notification
            manager.notify(notificationId, mBuilder.build());
            map.put(notificationId, mBuilder);// 存入Map中
        }
    }

    /**
     * 取消通知栏
     *
     * @param notificationId
     */
    public void cancel(int notificationId) {
        manager.cancel(notificationId);
        map.remove(notificationId);
    }

    /**
     * 更新通知栏进度条
     *
     * @param notificationId
     * @param progress       进度数值
     */
    public void updateProgress(int notificationId, int progress) {
        Notification.Builder builder = map.get(notificationId);
        Logger.e("=====================进度============" + progress);
        if (null != builder) {
            builder.setProgress(100, progress, false);
            manager.notify(notificationId, builder.build());
        }
    }

    /**
     * 判断该通知栏只否存在
     * @param notificationId
     * @return
     */
    public boolean isNotificationExists(int notificationId) {
        return map.get(notificationId) == null ? false : true;
    }
}
