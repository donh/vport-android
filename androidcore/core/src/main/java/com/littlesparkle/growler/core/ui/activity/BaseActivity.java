package com.littlesparkle.growler.core.ui.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.a21vianet.quincysx.library.SweetAlertDialog;
import com.littlesparkle.growler.core.R;
import com.littlesparkle.growler.core.am.ActivityManager;
import com.littlesparkle.growler.core.am.ManagedBaseActivity;
import com.littlesparkle.growler.core.http.BaseHttpSubscriber;
import com.littlesparkle.growler.core.ui.listener.PermissionRequestCallBack;
import com.littlesparkle.growler.core.ui.mvp.BasePresenter;
import com.littlesparkle.growler.core.ui.mvp.BaseView;
import com.littlesparkle.growler.core.ui.toast.ToastFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


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

public abstract class BaseActivity<P extends BasePresenter>
        extends ManagedBaseActivity
        implements BaseHttpSubscriber.IPrompt, BaseHttpSubscriber.IProgress, BaseView {
    /**
     * 请求权限的请求码
     */
    private static final int PermissionRequestCode = 0x001;

    /**
     * 权限处理结果的回调接口
     */
    private static PermissionRequestCallBack sPermissionRequestCallBack;

    private static final int MSG_SHOW_PROGRESS = 1;
    private static final int MSG_HIDE_PROGRESS = 2;
    private static final int MSG_SHOW_TOAST = 3;
    private static final int MSG_HIDE_TOAST = 4;

    private SweetAlertDialog mProgressDialog = null;

    protected P mPresenter;
    private Unbinder mUnbinder;

    protected Handler mPromptHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_PROGRESS:
                    if (mProgressDialog == null) {
                        try {
                            mProgressDialog = new SweetAlertDialog(BaseActivity.this,
                                    SweetAlertDialog.PROGRESS_TYPE);
                            mProgressDialog.getProgressHelper().setBarColor(Color.parseColor
                                    ("#a5dc86"));
                            mProgressDialog.setTitleText(getString(R.string.please_waiting));
                            mProgressDialog.setCancelable(true);
                            mProgressDialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case MSG_HIDE_PROGRESS:
                    if (mProgressDialog != null) {
                        try {
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case MSG_SHOW_TOAST:
                    try {
                        ToastFactory.getInstance(BaseActivity.this)
                                .makeTextShow(msg.obj.toString(), Toast.LENGTH_SHORT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_HIDE_TOAST:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        mUnbinder = ButterKnife.bind(this);
        mPresenter = initPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        initData();
        initView();
    }

    protected P initPresenter() {
        return null;
    }

    protected void initView() {
    }

    protected abstract void initData();

    protected abstract int getLayoutResId();

    @Override
    public void showProgress() {
        mPromptHandler.removeMessages(MSG_SHOW_PROGRESS);
        mPromptHandler.sendEmptyMessage(MSG_SHOW_PROGRESS);
    }

    @Override
    public void dismissProgress() {
        mPromptHandler.removeMessages(MSG_HIDE_PROGRESS);
        mPromptHandler.sendEmptyMessage(MSG_HIDE_PROGRESS);
    }

    @Override
    public void showPrompt(String msg) {
        mPromptHandler.removeMessages(MSG_SHOW_TOAST);
        mPromptHandler.sendMessage(mPromptHandler.obtainMessage(MSG_SHOW_TOAST, msg));
    }

    @Override
    protected void onDestroy() {
        dismissProgress();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.finish_activity_enter, R.anim.finish_activity_exit);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.finish_activity_enter, R.anim.finish_activity_exit);
    }

    /**
     * <p>检测权限并向用户请求权限</p>
     *
     * @param permissions 需要检测的权限
     * @param listener    权限状态回调接口
     */
    public static void requestRuntimePermission(
            @NonNull String[] permissions,
            @NonNull PermissionRequestCallBack listener) {
        //获取现在栈顶的Activity
        Activity topActivity = ActivityManager.getInstance().getCurrentActivity();
        if (topActivity == null) {
            return;
        }
        sPermissionRequestCallBack = listener;
        List<String> permissionList = new ArrayList<>();
        //判断是否有相应权限，把未授权的权限放在一个List里
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(topActivity, permission) != PackageManager
                    .PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        //当有未授权的权限进行权限请求
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(topActivity, permissionList.toArray(new
                    String[permissionList.size()]), PermissionRequestCode);
        } else {
            sPermissionRequestCallBack.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionRequestCode:
                if (grantResults.length > 0) {
                    List<String> deniedPermission = new ArrayList<>();
                    //判读没有授予权限的权限，并放到一个集合里
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            deniedPermission.add(permissions[i]);
                        }
                    }
                    //判断是否全部通过授权
                    if (deniedPermission.isEmpty()) {
                        sPermissionRequestCallBack.onGranted();
                    } else {
                        sPermissionRequestCallBack.onDenied(deniedPermission);
                    }
                }
            default:
                break;
        }

    }
}
