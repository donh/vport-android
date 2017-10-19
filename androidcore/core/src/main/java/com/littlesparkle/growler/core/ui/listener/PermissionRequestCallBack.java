package com.littlesparkle.growler.core.ui.listener;

import java.util.List;

/**
 * Created by wang.rongqiang on 2017/7/5.
 */

public interface PermissionRequestCallBack {
    /**
     * 当前请求的权限全部授权时回调此方法
     */
    void onGranted();

    /**
     * 当前请求的权限有部分或全部没有授权时回调此方法
     */
    void onDenied(List<String> deniedPermission);
}
