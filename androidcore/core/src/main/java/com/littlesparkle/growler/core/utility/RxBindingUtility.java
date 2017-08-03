package com.littlesparkle.growler.core.utility;

import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

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
public class RxBindingUtility {
    private static final long MICROSECONDS = 500;//需要延迟的描述

    public interface OnMyClickListener {
        void Call();
    }

    public static void Click(View view, final OnMyClickListener listener) {
        RxView.clicks(view).throttleFirst(MICROSECONDS, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                listener.Call();
            }
        });
    }

    public static void Click(View view, Action1<Void> action1) {
        RxView.clicks(view).throttleFirst(MICROSECONDS, TimeUnit.MILLISECONDS).subscribe(action1);
    }
}
