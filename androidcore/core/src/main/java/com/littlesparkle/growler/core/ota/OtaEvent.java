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

public class OtaEvent {
    public static final int NOT_FOUND_OTA_PACKAGE = 0;
    public static final int FOUND_OTA_PACKAGE = 1;
    public static final int DOWNLOADING_OTA_PACKAGE = 2;
    public static final int DOWNLOAD_OTA_PACKAGE_SUCCESS = 3;
    public static final int DOWNLOAD_OTA_PACKAGE_FAILED = 4;
    public static final int PRE_INSTALL_OTA_PACKAGE = 5;

    public final int status;
    public final OtaPackageInfo ota;
    public final String ext;

    public OtaEvent(int status, OtaPackageInfo ota) {
        this.status = status;
        this.ota = ota;
        this.ext = null;
    }

    public OtaEvent(int status, OtaPackageInfo ota, String ext) {
        this.status = status;
        this.ota = ota;
        this.ext = ext;
    }

    @Override
    public String toString() {
        return "OtaEvent{" +
                "status=" + status +
                ", ota=" + ota +
                '}';
    }
}
