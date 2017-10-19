package com.littlesparkle.growler.core.http.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.littlesparkle.growler.core.R;
import com.littlesparkle.growler.core.http.ErrorResponse;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;

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

public class Api {
    static final int DEFAULT_TIMEOUT = 120;
    static final int DEFAULT_CODE_SUCCESS = 0;
    static final int DEFAULT_CODE_ERROR = 1;
    static final String DEFAULT_TOKEN_ERROR = "user_token_error";

    private static Config sConfig = null;
    private static Context sContext;
    private static int sTimeout;
    private static String sBaseUrl = "";
    private static String sUnderDeveloping = "file:///android_asset/h5/under_developing.html";

    public static void init(Context context, Config config) {
        if (config == null || context == null) {
            throw new RuntimeException("Illegal init parameters!!!");
        }
        sContext = context;
        sConfig = config;
        sTimeout = config.timeout;
        sBaseUrl = config.baseUrl;
    }

    public static int getTimeout() {
        return sTimeout;
    }

    public static String getBaseUrl() {
        return sBaseUrl;
    }

    public static String getsUnderDeveloping() {
        return sUnderDeveloping;
    }

    public static ErrorResponse handleError(Throwable e) {
        ErrorResponse res;
        if (e instanceof ApiException) {
            Gson gson = new Gson();
            res = gson.fromJson(((ApiException) e).message, ErrorResponse.class);
            EventBus.getDefault().post(new ApiServiceErrorEvent(res.data.err_no, res.data.err_msg));
        } else {
            e.printStackTrace();
            res = new ErrorResponse();
            if (sContext != null) {
                if (e instanceof HttpException) {
                    HttpException he = (HttpException) e;
                    res.data.err_msg = sContext.getString(R.string.api_error_connect);
                    res.data.err_no = String.valueOf(he.code());
                } else if (e instanceof UnknownHostException) {
                    res.data.err_msg = sContext.getString(R.string.api_error_connect);
                    res.data.err_no = e.getLocalizedMessage();
                } else if (e instanceof SocketTimeoutException) {
                    res.data.err_msg = sContext.getString(R.string.api_error_connect);
                    res.data.err_no = e.getLocalizedMessage();
                } else if (e instanceof ConnectException) {
                    res.data.err_msg = sContext.getString(R.string.api_error_connect);
                    res.data.err_no = e.getLocalizedMessage();
                } else if (e instanceof IOException) {
                    res.data.err_msg = sContext.getString(R.string.api_error_connect);
                    res.data.err_no = e.getLocalizedMessage();
                } else if (e instanceof IllegalStateException) {
                    res.data.err_msg = sContext.getString(R.string.api_error_state);
                    res.data.err_no = e.getLocalizedMessage();
                } else if (e instanceof JsonSyntaxException) {
                    res.data.err_msg = sContext.getString(R.string.api_error_json);
                    res.data.err_no = e.getLocalizedMessage();
                } else {
                    res.data.err_msg = e.getMessage();
                    res.data.err_no = e.getLocalizedMessage();
                }
            } else {
                res.data.err_msg = e.getMessage();
                res.data.err_no = e.getLocalizedMessage();
            }
        }
        return res;
    }

    public static boolean checkResult(int result) {
        return result == (sConfig == null ? DEFAULT_CODE_SUCCESS : sConfig.successCode);
    }

    public static class Config {
        private int successCode = DEFAULT_CODE_SUCCESS;
        private int errorCode = DEFAULT_CODE_ERROR;
        private int timeout = DEFAULT_TIMEOUT;
        private String token_error = DEFAULT_TOKEN_ERROR;
        private String baseUrl = "";

        public Config() {
        }

        public Config setSuccessCode(int code) {
            successCode = code;
            return this;
        }

        public Config setErrorCode(int code) {
            errorCode = code;
            return this;
        }

        public Config setBaseUrl(String url) {
            baseUrl = url;
            return this;
        }

        public Config setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

    }
}
