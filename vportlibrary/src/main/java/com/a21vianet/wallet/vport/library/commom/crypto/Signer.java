package com.a21vianet.wallet.vport.library.commom.crypto;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.a21vianet.wallet.app.commom.crypto.callback.MultisigCallback;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wang.rongqiang on 2017/5/31.
 */

public class Signer {
    private static Signer signer = new Signer();
    private WebView webView;

    private Signer() {
    }

    public static Signer getInstance() {
        return signer;
    }

    public void sign(Context context, final String priv, final String tx,
                     MultisigCallback callback) {
        webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsToJava(callback), "android");
        webView.loadUrl("file:///android_asset/html/index.html");
        webView.setWebChromeClient(new WebChromeClient() {
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                String call = "javascript:sign('" + priv + "','" +
                        tx + "')";
                webView.loadUrl(call);
            }
        });
    }

    public void signMultisig(Context context, final String priv, final String script, final
    String tx, MultisigCallback callback) {
        webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsToJava(callback), "android");
        webView.loadUrl("file:///android_asset/html/index.html");
        webView.setWebChromeClient(new WebChromeClient() {
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String call = "javascript:signMultisig('" + priv + "','" + script + "','" +
                        tx + "')";
                webView.loadUrl(call);
            }
        });
    }

    private class JsToJava {
        MultisigCallback callback;

        public JsToJava(MultisigCallback callback) {
            this.callback = callback;
        }

        @JavascriptInterface
        public void onSinged(final String signedRawTransaction) {
            Observable
                    .create(new Observable.OnSubscribe<String>() {
                        @Override
                        public void call(Subscriber<? super String> subscriber) {
                            subscriber.onNext("");
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            callback.onSinged(signedRawTransaction);
                        }
                    });
        }

        @JavascriptInterface
        public void onError() {
            callback.onError(new Exception("签名失败"));
        }
    }
}
