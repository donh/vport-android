package com.littlesparkle.growler.core.ui.activity;

import android.widget.Toast;

import com.littlesparkle.growler.core.R;

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

public abstract class BaseMainActivity extends BaseActivity {
    private static final int QUIT_CHECK_INTERNAL = 2000;
    private long mQuitTimePoint = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mQuitTimePoint) > QUIT_CHECK_INTERNAL) {
            Toast.makeText(getApplicationContext(), R.string.quit_confirmation,
                    Toast.LENGTH_SHORT).show();
            mQuitTimePoint = System.currentTimeMillis();
        } else {
            // work around for https://code.google.com/p/android/issues/detail?id=176265
            try {
                super.onBackPressed();
            } catch (IllegalStateException ex) {
                super.supportFinishAfterTransition();
                ex.printStackTrace();
            }
        }
    }
}
