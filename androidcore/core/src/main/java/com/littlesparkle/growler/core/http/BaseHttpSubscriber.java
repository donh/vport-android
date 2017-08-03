package com.littlesparkle.growler.core.http;

import android.util.Log;

import com.littlesparkle.growler.core.http.api.Api;
import com.littlesparkle.growler.core.ui.mvp.ViewIsNullException;

import rx.Subscriber;

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

public abstract class BaseHttpSubscriber<T> extends Subscriber<T> {
    private IProgress mProgress;
    private IPrompt mPrompt;

    public BaseHttpSubscriber() {
        this(null, null);
    }

    public BaseHttpSubscriber(IPrompt prompt) {
        this(null, prompt);
    }

    public BaseHttpSubscriber(IProgress progress) {
        this(progress, null);
    }

    public BaseHttpSubscriber(IProgress progress, IPrompt prompt) {
        mProgress = progress;
        mPrompt = prompt;
    }

    private void end() {
        if (isUnsubscribed()) {
            this.unsubscribe();
        }
        if (mProgress != null) {
            mProgress.dismissProgress();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mProgress != null) {
            mProgress.showProgress();
        }
    }

    @Override
    public void onNext(T t) {
        try {
            onSuccess(t);
        } catch (ViewIsNullException e) {
        }
    }

    @Override
    public void onCompleted() {
        end();
    }

    @Override
    public void onError(Throwable e) {
        Log.e("BaseHttpSubscriber", e.toString());
        end();
        onError(Api.handleError(e));
    }

    protected void onError(ErrorResponse error) {
        if (mPrompt != null) {
            mPrompt.showPrompt(error.data.err_msg);
        }
    }

    protected void onSuccess(T t) {
    }

    public interface IProgress {
        void showProgress();

        void dismissProgress();
    }

    public interface IPrompt {
        void showPrompt(String msg);
    }
}
