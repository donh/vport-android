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

public interface DownLoad {
    /**
     * 开始下载
     *
     * @param url   下载路径
     * @param path  存放路径
     * @param md5   文件MD5值 如果不需要可以为null
     * @param types 下载是的网络类型
     * @return 下载的表示ID
     */
    public int startDownload(final String url, String path,String md5,
                              NetworkTypes types);

    public void stopDownload(final int downloadId);

    public void addDownloadCallback(DownloadCallback callback);

    public void removeDownloadCallback(DownloadCallback callback);

    public void removeAllDownloadCallback();

    public void stopDownload(final String url);

    public int getDownloadId(final String url);
}
