package com.littlesparkle.growler.core.http;


import android.content.Context;

import com.littlesparkle.growler.core.http.api.ApiServiceErrorEvent;
import com.littlesparkle.growler.core.user.UserManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


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

public class ServerErrorManager {
    //服务器错误信息处理

    private static Context mContext;
    private static ServerErrorManager mServerErrorManager;

    public ServerErrorManager() {
        EventBus.getDefault().register(this);
    }

    public synchronized static ServerErrorManager init(Context context) {
        if (mServerErrorManager == null) {
            mContext = context;
            mServerErrorManager = new ServerErrorManager();
        }
        return mServerErrorManager;
    }

    @Subscribe
    public void onApiServiceErrorEvent(ApiServiceErrorEvent apiServiceErrorEvent) {
        String errorNo = apiServiceErrorEvent.getError_no();
        String errorMsg = apiServiceErrorEvent.getError_msg();
        if ("user_token_error".equals(errorNo)) {
            UserManager.signOut(mContext);
        }
    }

    public static void unRegister() {
        EventBus.getDefault().unregister(mServerErrorManager);
    }

}
