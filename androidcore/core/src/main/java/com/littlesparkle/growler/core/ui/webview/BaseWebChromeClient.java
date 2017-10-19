package com.littlesparkle.growler.core.ui.webview;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

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
public class BaseWebChromeClient extends WebChromeClient {
    private OnOpenFileChooserListener mOpenFileChooserListener = null;

    public void setOnOpenFileChooserListener(OnOpenFileChooserListener openFileChooserListener) {
        mOpenFileChooserListener = openFileChooserListener;
    }

    // Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        if (mOpenFileChooserListener != null) {
            mOpenFileChooserListener.openFileChooserWithOneFile(uploadMsg);
        }
    }

    // Android 4.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        openFileChooser(uploadMsg, acceptType);
    }

    // Android 5.0+
    @Override
    @SuppressLint("NewApi")
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        if (mOpenFileChooserListener != null) {
            mOpenFileChooserListener.openFileChooserWithMultiFiles(filePathCallback);
        }
        return true;
    }

    public interface OnOpenFileChooserListener {
        void openFileChooserWithOneFile(ValueCallback<Uri> uploadMsg);

        void openFileChooserWithMultiFiles(ValueCallback<Uri[]> filePathCallback);
    }
}
