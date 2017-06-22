package com.a21vianet.wallet.vport.library.commom.crypto;

import android.content.Context;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.a21vianet.wallet.vport.library.commom.crypto.callback.OnFinishedListener;
import com.a21vianet.wallet.vport.library.commom.crypto.callback.OnVerifiedListener;

/**
 * Created by wang.rongqiang on 2017/6/22.
 */

public class JWT {

    public static void signToken(Context context, String privateKey, String token, final OnFinishedListener listener) {
        final String js = "javascript:signToken('" + privateKey + "', " +
                token + ")";

        final WebView webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/html/index.html");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(final String s) {
                        listener.onFinish(s);
                    }
                });
            }
        });
    }

    public static void decodeToken(Context context, String tokenString, final OnFinishedListener listener) {
        final String js = "javascript:decodeToken('" + tokenString + "')";

        final WebView webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/html/index.html");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(final String s) {
                        listener.onFinish(s);
                    }
                });
            }
        });
    }

    public static void verifyToken(Context context, String publicKey, String token, final OnVerifiedListener listener) {
        final String js = "javascript:verifyToken('" + publicKey + "',\"" +
                token + "\")";

        final WebView webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/html/index.html");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
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
