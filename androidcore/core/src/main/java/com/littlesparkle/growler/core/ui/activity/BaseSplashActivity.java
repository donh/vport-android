package com.littlesparkle.growler.core.ui.activity;

import android.os.Bundle;

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

public abstract class BaseSplashActivity extends BaseHandlerActivity {
    public static final int ONE_SECOND = 1000;
    private static final int MAX_COUNTER = 2;
    private int mRemainingSeconds = 0;

    private Runnable mUpdateRunnable = new Runnable() {
        public void run() {
            if (mRemainingSeconds == 0) {
                onSplashEnd();
            } else {
                mRemainingSeconds = mRemainingSeconds - 1;
                mHandler.postDelayed(this, ONE_SECOND);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateRunnable);
    }

    protected abstract void onSplashEnd();

    protected int getMaxCounter() {
        return MAX_COUNTER;
    }

    @Override
    protected void initData() {
        mRemainingSeconds = getMaxCounter();
        mHandler.postDelayed(mUpdateRunnable, ONE_SECOND);
    }

    protected int getRemainingSeconds() {
        return mRemainingSeconds;
    }
}
