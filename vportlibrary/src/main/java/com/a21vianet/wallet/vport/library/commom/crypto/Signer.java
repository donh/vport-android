package com.a21vianet.wallet.vport.library.commom.crypto;

import android.content.Context;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.a21vianet.wallet.vport.library.commom.crypto.callback.OnFinishedListener;

/**
 * Created by wang.rongqiang on 2017/5/31.
 */

public class Signer {
    public static void sign(Context context, final String priv, final String tx,
                            final OnFinishedListener callback) {
        final String call = "javascript:sign('" + priv + "','" +
                tx + "')";
        SingleWebView.getInstance(context, new SingleWebView.WebViewInitCallback() {
            @Override
            public void onSuccess(WebView webView) {
                webView.evaluateJavascript(call, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(final String s) {
                        callback.onFinish(s);
                    }
                });
            }
        });
    }

    public static void signMultisig(Context context, final String priv, final String script, final
    String tx, final OnFinishedListener callback) {
        final String call = "javascript:signMultisig('" + priv + "','" + script + "','" +
                tx + "')";
        SingleWebView.getInstance(context, new SingleWebView.WebViewInitCallback() {
            @Override
            public void onSuccess(WebView webView) {
                webView.evaluateJavascript(call, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(final String s) {
                        callback.onFinish(s);
                    }
                });
            }
        });
    }

    public static void signMessage(Context context, String priv, String msg, final
    OnFinishedListener
            listener) {
        final String js = "javascript:signMessage('" + priv + "', " +
                "'" + msg + "')";

        SingleWebView.getInstance(context, new SingleWebView.WebViewInitCallback() {
            @Override
            public void onSuccess(WebView webView) {
                webView.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        listener.onFinish(s);
                    }
                });
            }
        });
    }
}
