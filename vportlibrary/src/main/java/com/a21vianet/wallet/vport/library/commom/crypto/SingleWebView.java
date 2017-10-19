package com.a21vianet.wallet.vport.library.commom.crypto;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by wang.rongqiang on 2017/6/30.
 */

public class SingleWebView {
    private static volatile SingleWebView sSingleWebView;

    private WebView mWebView;

    private SingleWebView(Context context) {
        mWebView = new WebView(context);
        mWebView.getSettings().setJavaScriptEnabled(true);
    }

    public static void getInstance(Context context, final WebViewInitCallback callback) {
        if (sSingleWebView == null) {
            synchronized (SingleWebView.class) {
                if (sSingleWebView == null) {
                    sSingleWebView = new SingleWebView(context);
                    sSingleWebView.mWebView.loadUrl("file:///android_asset/html/index.html");
                    sSingleWebView.mWebView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            callback.onSuccess(view);
                        }
                    });
                } else {
                    callback.onSuccess(sSingleWebView.mWebView);
                }
            }
        } else {
            callback.onSuccess(sSingleWebView.mWebView);
        }
    }

    public interface WebViewInitCallback {
        void onSuccess(WebView webView);
    }
}
