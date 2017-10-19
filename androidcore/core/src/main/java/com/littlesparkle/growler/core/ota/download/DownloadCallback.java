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

public abstract class DownloadCallback {
    public void onStart(int downloadId, long totalBytes) {
    }

    public void onProgress(int downloadId, long bytesWritten, long totalBytes) {
    }

    public void onSuccess(int downloadId, String filePath) {
    }

    public void onFailure(int downloadId, int statusCode, String errMsg) {
    }
}
