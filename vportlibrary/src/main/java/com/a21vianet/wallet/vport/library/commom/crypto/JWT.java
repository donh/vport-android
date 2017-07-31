package com.a21vianet.wallet.vport.library.commom.crypto;

import android.content.Context;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.a21vianet.wallet.vport.library.commom.crypto.callback.OnFinishedListener;
import com.a21vianet.wallet.vport.library.commom.crypto.callback.OnVerifiedListener;

/**
 * Created by wang.rongqiang on 2017/6/22.
 */

public class JWT {

    /**
     * 对JTW进行签名
     *
     * @param context
     * @param privateKey
     * @param token
     * @param listener
     */
    public static void signToken(Context context, String privateKey, String token, final OnFinishedListener listener) {
        final String js = "javascript:signToken('" + privateKey + "', " +
                token + ")";

        SingleWebView.getInstance(context, new SingleWebView.WebViewInitCallback() {
            @Override
            public void onSuccess(WebView webView) {
                webView.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(final String s) {
                        listener.onFinish(s);
                    }
                });
            }
        });
    }

    /**
     * 对 JWT 解谜
     *
     * @param context
     * @param tokenString
     * @param listener
     */
    public static void decodeToken(Context context, String tokenString, final OnFinishedListener listener) {
        final String js = "javascript:decodeToken('" + tokenString + "')";

        SingleWebView.getInstance(context, new SingleWebView.WebViewInitCallback() {
            @Override
            public void onSuccess(WebView webView) {
                webView.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(final String s) {
                        listener.onFinish(s);
                    }
                });
            }
        });
    }

    /**
     * 验证 JWT
     *
     * @param context
     * @param publicKey
     * @param token
     * @param listener
     */
    public static void verifyToken(Context context, String publicKey, String token, final OnVerifiedListener listener) {
        final String js = "javascript:verifyToken('" + publicKey + "',\"" +
                token + "\")";

        SingleWebView.getInstance(context, new SingleWebView.WebViewInitCallback() {
            @Override
            public void onSuccess(WebView webView) {
                webView.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(final String s) {
                        boolean isValid = false;
                        if (s.equals("true")) {
                            isValid = true;
                        }
                        listener.onVerified(isValid);
                    }
                });
            }
        });
    }
}
