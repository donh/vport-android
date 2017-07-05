package com.a21vianet.wallet.vport.common;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.littlesparkle.growler.core.ui.activity.BaseActivity;
import com.littlesparkle.growler.core.ui.listener.PermissionRequestCallBack;
import com.littlesparkle.growler.core.ui.toast.ToastFactory;

import java.util.List;


/**
 * Created by wang.rongqiang on 2017/7/5.
 */

public class ActivityStartUtility {
    public static void startScanActivity(final Activity activity, final Intent intent, final int code) {
        String[] permissions = {Manifest.permission.CAMERA};
        BaseActivity.requestRuntimePermission(permissions, new PermissionRequestCallBack() {
            @Override
            public void onGranted() {
                activity.startActivityForResult(intent, code);
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                ToastFactory.getInstance(activity).makeTextShow("没有使用相机的权限，请在权限管理中开启", Toast.LENGTH_LONG);
            }
        });

    }
}
