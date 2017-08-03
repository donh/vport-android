package com.littlesparkle.growler.core.user;

import android.content.Context;

import com.littlesparkle.growler.core.bean.Bean;
import com.littlesparkle.growler.core.bean.BeanHelper;
import com.littlesparkle.growler.core.utility.PrefUtility;

import org.greenrobot.eventbus.EventBus;


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

public class UserManager {
    private static final String SIGN_IN_KEY = "is-signed-in";
    private static final String TOKEN_KEY = "token";
    private static final String USER_ID_KEY = "user_id";

    private static Class<Bean>[] CLEAN_UP_CLASS_ARRAY = new Class[]{
            BaseUser.class
    };

    public static boolean isSignedIn(Context context) {
        return PrefUtility.getBoolean(context, SIGN_IN_KEY, false);
    }

    public static void signIn(Context context, BaseUser baseUser) {
        baseUser.persist(context);
        PrefUtility.setBoolean(context, SIGN_IN_KEY, true);
        EventBus.getDefault().post(new UserEvent(true, baseUser));
    }

    public static void signOut(Context context) {
        if (!PrefUtility.getBoolean(context, SIGN_IN_KEY)) {
            return;
        }
        BaseUser baseUser = new BaseUser();
        baseUser.load(context);
        for (Class<Bean> cls : CLEAN_UP_CLASS_ARRAY) {
            BeanHelper.clean(context, cls);
        }
        PrefUtility.setBoolean(context, SIGN_IN_KEY, false);
        EventBus.getDefault().post(new UserEvent(false, baseUser));
    }

    public static String getToken(Context context) {
        return PrefUtility.getString(context, TOKEN_KEY);
    }

    public static void setToken(Context context, String token) {
        PrefUtility.setString(context, TOKEN_KEY, token);
    }

    public static int getUserId(Context context) {
        return PrefUtility.getInteger(context, USER_ID_KEY);
    }

    public static String getUserIdString(Context context) {
        return String.valueOf(PrefUtility.getInteger(context, USER_ID_KEY));
    }

    public static void setUserId(Context context, int userid) {
        PrefUtility.setInteger(context, USER_ID_KEY, userid);
    }
}
