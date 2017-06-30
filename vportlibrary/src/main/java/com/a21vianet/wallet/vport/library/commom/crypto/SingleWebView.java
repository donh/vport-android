package com.a21vianet.wallet.vport.library.commom.crypto;

import android.content.Context;
import android.webkit.WebView;

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

    public static WebView getInstance(Context context, String htmlname) {
        if (sSingleWebView == null) {
            synchronized (SingleWebView.class) {
                if (sSingleWebView == null) {
                    sSingleWebView = new SingleWebView(context);
                }
            }
        }
        sSingleWebView.mWebView.loadUrl("file:///android_asset/html/" + htmlname);
        return sSingleWebView.mWebView;
    }
}
