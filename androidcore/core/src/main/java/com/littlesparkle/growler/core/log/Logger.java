package com.littlesparkle.growler.core.log;

import android.util.Log;

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
public class Logger {
    private static String TAG = "growler-library";

    public static void init(String tag) {
        if (!"".equals(tag)) {
            TAG = tag;
        }
    }

    public static void log(String msg) {
        log(TAG, msg);
    }

    public static void log(String tag, String msg) {
        w(tag, msg);
    }

    public static void i(String msg) {
        Log.d(TAG, msg);
    }

    public static void i(String tag, String msg) {
        Log.d(TAG + ":" + tag, msg);
    }

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(TAG + ":" + tag, msg);
    }

    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(TAG + ":" + tag, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(TAG + ":" + tag, msg);
    }
}
