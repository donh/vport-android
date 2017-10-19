package com.littlesparkle.growler.core.ui.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.littlesparkle.growler.core.R;
import com.littlesparkle.growler.core.common.TempDirectory;
import com.littlesparkle.growler.core.ui.activity.BaseActivity;
import com.littlesparkle.growler.core.ui.view.dialog.DialogHelper;
import com.littlesparkle.growler.core.user.BaseUser;
import com.littlesparkle.growler.core.utility.FileUtility;

import java.io.File;
import java.util.Date;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;

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

public class WebViewActivity extends BaseActivity implements BaseWebChromeClient.OnOpenFileChooserListener {
    private static final boolean DEBUG = true;

    private static final int PHOTO_REQUEST_CAMERA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;

    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private static final String UPLOAD_PHOTO_FILE_NAME = "upload.jpg";
    private String mRootDir = TempDirectory.getFileDir();
    private File mCaptureFile;
    private File mGalleryFile;


    private RelativeLayout mTitleBar;
    private WebView mWebView;
    private NumberProgressBar mNpb;
    private AppCompatImageView mBack;
    private AppCompatImageView mClose;
    private TextView mTitle;

    private ValueCallback<Uri> mOneFileUploadCallback;
    private ValueCallback<Uri[]> mMultiFilesUploadCallback;

    private String mUserId;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!mRootDir.endsWith("/")) {
            mRootDir += "/";
        }
        FileUtility.mkdir(mRootDir);

        mWebView = (WebView) findViewById(R.id.webview);
        if (DEBUG) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebView.setWebContentsDebuggingEnabled(true);
            }
        }
        mTitleBar = (RelativeLayout) findViewById(R.id.title_bar);
        if (getTitleBarBackGroundColor() != 0) {
            mTitleBar.setBackgroundColor(getResources().getColor(getTitleBarBackGroundColor()));
        }
        mNpb = (NumberProgressBar) findViewById(R.id.number_progress_bar);
        mBack = (AppCompatImageView) findViewById(R.id.back);
        if (getTitleBarBackBackground() != 0) {
            mBack.setImageResource(getTitleBarBackBackground());
        }
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mClose = (AppCompatImageView) findViewById(R.id.close);
        if (getTitleBarCloseBackground() != 0) {
            mClose.setImageResource(getTitleBarCloseBackground());
        }
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitle = (TextView) findViewById(R.id.title_text);
        if (getTitleTextColor() != 0) {
            mTitle.setTextColor(getResources().getColor(getTitleTextColor()));
        }
        mNpb.setMax(100);
        mNpb.setProgress(0);

        Intent it = getIntent();
        if (it != null) {
            String title = it.getStringExtra("title");
            mTitle.setText(title);

            String url = it.getStringExtra("url");
            int user_id = it.getIntExtra("user_id", 0);
            String token = it.getStringExtra("token");
            if (user_id != 0 && !"".equals(token)) {
                mUserId = String.valueOf(user_id);
                mToken = token;
                url += "?";
                url += "user_id=" + user_id;
                url += "&token=" + token;
            }

            initWebView();
            mWebView.loadUrl(url);
        } else {
            // exception
        }
    }

    /**
     * 设置标题颜色
     *
     * @return
     */
    protected int getTitleTextColor() {
        return 0;
    }

    /**
     * 设置返回按钮图片
     *
     * @return
     */
    protected int getTitleBarCloseBackground() {
        return 0;
    }

    /**
     * 设置Title的背景
     *
     * @return
     */
    protected int getTitleBarBackBackground() {
        return 0;
    }

    /**
     * 设置返回按钮背景
     *
     * @return
     */
    protected int getTitleBarBackGroundColor() {
        return 0;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_web_view;
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        if (DEBUG) {
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        mWebView.setDownloadListener(new BaseWebViewDownloadListener(this));
        mWebView.setWebViewClient(new BaseWebViewClient());
        BaseWebChromeClient webChromeClient = new BaseWebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mNpb.setProgress(newProgress);
                if (newProgress == 100) {
                    mNpb.setVisibility(View.GONE);
                } else {
                    mNpb.setVisibility(View.VISIBLE);
                }
            }
        };
        webChromeClient.setOnOpenFileChooserListener(this);
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.addJavascriptInterface(new WebViewResultInterface(this, mUserId, mToken),
                "WebViewResultInterface");

        mWebView.requestFocusFromTouch();
    }

    public static void startWebViewActivity(Context context, Class aClass, String title, String
            url, BaseUser baseUser) {
        Intent it = new Intent(context, aClass);
        it.putExtra("title", title);
        it.putExtra("url", url);
        if (baseUser != null) {
            it.putExtra("user_id", baseUser.user_id);
            it.putExtra("token", baseUser.token);
        }
        context.startActivity(it);
    }

    private class WebViewResultInterface {
        private Context mContext;
        private String userId;
        private String token;

        public WebViewResultInterface(Context context, String userId, String token) {
            this.mContext = context;
            this.userId = userId;
            this.token = token;
        }

        @JavascriptInterface
        public String getUserId() {
            return userId;
        }

        @JavascriptInterface
        public String getToken() {
            return token;
        }

        @JavascriptInterface
        public void onJsResult(int result, String message) {
            switch (result) {
                case 0:
                    onJsSuccess(message);
                    break;
                case 1:
                    onJsFailed(message);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private final void getImage() {
        if (!FileUtility.hasSdcard()) {
            return;
        }

        DialogHelper.showOptionsDialog(this, new int[]{R.string.capture, R.string.album},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                camera();
                                break;
                            case 1:
                                gallery();
                                break;
                            default:
                                break;
                        }
                    }
                },
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if (mOneFileUploadCallback != null) {
                            mOneFileUploadCallback.onReceiveValue(null);
                            mOneFileUploadCallback = null;
                        }
                        if (mMultiFilesUploadCallback != null) {
                            mMultiFilesUploadCallback.onReceiveValue(null);
                            mMultiFilesUploadCallback = null;
                        }
                    }
                }, null
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (null == mOneFileUploadCallback && null == mMultiFilesUploadCallback) {
            return;
        }
        Uri uri = null;
        File file = null;
        if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (FileUtility.hasSdcard()) {
                uri = Uri.fromFile(mCaptureFile);
                file = mCaptureFile;
            } else {
                Toast.makeText(this, R.string.sdcard_not_found, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (intent != null) {
                uri = intent.getData();
                if (uri == null) {
                    return;
                }
                Cursor cursor = getContentResolver().query(uri, null, null,
                        null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    mGalleryFile = new File(cursor.getString(1));
                    file = mGalleryFile;
                }
            }
        }

        if (file == null && uri != null) {
            file = new File(uri.getPath());
        } else if ((file == null && uri == null) || !file.exists()) {
            if (mMultiFilesUploadCallback != null) {
                mMultiFilesUploadCallback.onReceiveValue(null);
                mMultiFilesUploadCallback = null;
            }
            if (mOneFileUploadCallback != null) {
                mOneFileUploadCallback = null;
            }
            return;
        }
        String uploadFilePath = mRootDir + new Date().getTime() + UPLOAD_PHOTO_FILE_NAME;
        File uploadFile = new File(uploadFilePath);

        FileUtility.copyfile(file, uploadFile, true);
        Luban.get(this)
                .load(uploadFile)
                .putGear(Luban.THIRD_GEAR)
                .launch()
                .asObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        Uri uri1 = Uri.fromFile(file);
                        if (mMultiFilesUploadCallback != null) {
                            if (uri1 == null) {
                                mMultiFilesUploadCallback.onReceiveValue(null);
                            } else {
                                Uri[] uris = {uri1};
                                mMultiFilesUploadCallback.onReceiveValue(uris);
                            }
                            mMultiFilesUploadCallback = null;
                        }
                        if (mOneFileUploadCallback != null) {
                            mOneFileUploadCallback.onReceiveValue(uri1);
                            mOneFileUploadCallback = null;
                        }
                    }
                });
    }

    private void camera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mCaptureFile = new File(mRootDir +
                new Date().getTime() + PHOTO_FILE_NAME);
        Uri uri = Uri.fromFile(mCaptureFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    private void gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    @Override
    public void openFileChooserWithOneFile(ValueCallback<Uri> uploadMsg) {
        mOneFileUploadCallback = uploadMsg;
        getImage();
    }

    @Override
    public void openFileChooserWithMultiFiles(ValueCallback<Uri[]> filePathCallback) {
        mMultiFilesUploadCallback = filePathCallback;
        getImage();
    }


    public void onJsSuccess(String result) {
    }

    public void onJsFailed(String result) {
    }
}
