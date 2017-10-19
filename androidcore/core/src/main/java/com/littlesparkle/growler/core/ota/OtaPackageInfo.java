package com.littlesparkle.growler.core.ota;

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

public class OtaPackageInfo {
    private String platform;
    private String identification; // package name
    private String update_log;
    private String version_name;
    private int version_code;
    private boolean is_force;
    private int size;
    private String url;
    private String md5;
    private String channel;

    public OtaPackageInfo(String platform, String identification, String update_log,
                          String version_name, int version_code, boolean is_force,
                          int size, String url, String md5, String channel) {
        this.platform = platform;
        this.identification = identification;
        this.update_log = update_log;
        this.version_name = version_name;
        this.version_code = version_code;
        this.is_force = is_force;
        this.size = size;
        this.url = url;
        this.md5 = md5;
        this.channel = channel;
    }

    public String getPlatform() {
        return platform;
    }

    public String getIdentification() {
        return identification;
    }

    public String getUpdateLog() {
        return update_log;
    }

    public String getVersionName() {
        return version_name;
    }

    public int getVersionCode() {
        return version_code;
    }

    public boolean isForce() {
        return is_force;
    }

    public int getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public String getMd5() {
        return md5;
    }

    public String getChannel() {
        return channel;
    }

    public boolean isValidated() {
        if (url == null || "".equals(url)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OtaPackageInfo{" +
                "platform='" + platform + '\'' +
                ", identification='" + identification + '\'' +
                ", update_log='" + update_log + '\'' +
                ", version_name='" + version_name + '\'' +
                ", version_code=" + version_code +
                ", is_force=" + is_force +
                ", size=" + size +
                ", url='" + url + '\'' +
                ", md5='" + md5 + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }
}
