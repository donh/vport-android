package com.a21vianet.wallet.vport.action.mian;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.a21vianet.wallet.vport.R;
import com.a21vianet.wallet.vport.action.historyoperation.HistoryOperationActivity;
import com.a21vianet.wallet.vport.action.identityinfo.IdentityInfoActivity;
import com.a21vianet.wallet.vport.action.info.PersonalInfoActivity;
import com.a21vianet.wallet.vport.action.password.PasswordManager;
import com.a21vianet.wallet.vport.action.scan.ScanActivity;
import com.a21vianet.wallet.vport.action.setting.SettingActivity;
import com.a21vianet.wallet.vport.library.commom.crypto.CryptoManager;
import com.littlesparkle.growler.core.am.ActivityUtility;
import com.littlesparkle.growler.core.ui.activity.BaseMainActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.security.GeneralSecurityException;

import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;

import static com.github.orangegangsters.lollipin.lib.managers.AppLockActivity.EXT_DATA;

public class MainActivity extends BaseMainActivity {
    private static final int REQUEST_ENTER_PASSWORD = 0x1;
    public static final int REQUEST_CODE_SCAN = 8080;

    @Override
    protected void initData() {
        checkEnCode();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarCompat.translucentStatusBar(this);
    }

    @OnClick({R.id.relative_visiting, R.id.relative_identity, R.id.relative_operation, R.id
            .imgv_main_scan, R.id.imgv_main_setting})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.relative_visiting:
                intent = new Intent(this, PersonalInfoActivity.class);
                break;
            case R.id.relative_identity:
                intent = new Intent(this, IdentityInfoActivity.class);
                break;
            case R.id.relative_operation:
                intent = new Intent(this, HistoryOperationActivity.class);
                break;
            case R.id.imgv_main_scan:
                startActivityForResult(new Intent(this, ScanActivity.class), REQUEST_CODE_SCAN);
                break;
            case R.id.imgv_main_setting:
                intent = new Intent(this, SettingActivity.class);
                break;
        }
        if (intent != null) {
            ActivityUtility.startActivityWithAnim(this, intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String stringExtra = null;
        if (data != null) {
            stringExtra = data.getStringExtra(EXT_DATA);
        }
        switch (requestCode) {
            case REQUEST_ENTER_PASSWORD:
                if (stringExtra != null) {
                    try {
                        CryptoManager.getInstance().decrypt(stringExtra);
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }
                } else {
                    finish();
                }
                break;
            case REQUEST_CODE_SCAN:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    /**
     * 检查私钥是否已解密
     */
    private void checkEnCode() {
        if (CryptoManager.getInstance().isEncrypted()) {
            PasswordManager.startCheckPassword(this, REQUEST_ENTER_PASSWORD);
        }
    }
}
