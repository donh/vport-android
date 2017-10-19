package com.a21vianet.wallet.vport.action.password;

import android.app.Activity;
import android.content.Intent;

import com.a21vianet.wallet.vport.library.commom.crypto.CryptoManager;
import com.github.orangegangsters.lollipin.lib.managers.AppLock;

/**
 * Created by wang.rongqiang on 2017/6/7.
 */

public class PasswordManager {
    /**
     * 输入密码框
     *
     * @param activity
     * @param requestcode
     */
    public static void startEnterPassword(Activity activity, int requestcode) {
        Intent intent = new Intent(activity, CustomPinActivity.class);
        intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
        activity.startActivityForResult(intent, requestcode);
    }

    /**
     * 修改密码
     *
     * @param activity
     * @param requestcode
     */
    public static void startChangePassword(Activity activity, int requestcode) {
        Intent intent = new Intent(activity, CustomPinActivity.class);
        intent.putExtra(AppLock.EXTRA_TYPE, AppLock.CHANGE_PIN);
        activity.startActivityForResult(intent, requestcode);
    }

    /**
     * 有指纹验证的密码框
     *
     * @param activity
     * @param requestcode
     */
    public static void startCheckPassword(Activity activity, int requestcode) {
        Intent intent = new Intent(activity, CustomPinActivity.class);
        if (CryptoManager.getInstance().isEncrypted()) {
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_NOFINGER_PIN);
        } else {
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
        }
        activity.startActivityForResult(intent, requestcode);
    }

    /**
     * 有指纹验证的密码框
     *
     * @param activity
     * @param requestcode
     */
    public static void startCheckPassword(Activity activity, int requestcode, boolean isNoFinger) {
        Intent intent = new Intent(activity, CustomPinActivity.class);
        if (isNoFinger) {
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_NOFINGER_PIN);
        } else {
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
        }
        activity.startActivityForResult(intent, requestcode);
    }
}
