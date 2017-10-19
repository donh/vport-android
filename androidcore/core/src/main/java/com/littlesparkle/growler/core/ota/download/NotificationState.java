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

public class NotificationState {
    /**
     * true:显示通知栏消息
     * false:不显示
     */
    private Boolean NotificationShow;

    /**
     * true:下载完立马安装
     * false:显示通知栏提示安装
     */
    private Boolean InstallNow;

    /**
     * 显示通知栏的标题
     */
    private String NotificationTitle;

    public NotificationState(boolean notificationShow, boolean installNow, String
            notificationTitle) {
        NotificationShow = notificationShow;
        InstallNow = installNow;
        NotificationTitle = notificationTitle;
    }

    public boolean isInstallNow() {
        if (null == this.InstallNow) {
            return false;
        }
        return InstallNow;
    }

    public void setInstallNow(boolean installNow) {
        InstallNow = installNow;
    }

    public boolean isNotificationShow() {
        if (null == this.NotificationShow) {
            return false;
        }
        return NotificationShow;
    }

    public void setNotificationShow(boolean notificationShow) {
        NotificationShow = notificationShow;
    }

    public String getNotificationTitle() {
        if (null == NotificationTitle) {
            return "";
        }
        return NotificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        NotificationTitle = notificationTitle;
    }
}
