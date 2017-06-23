package com.a21vianet.wallet.vport.action.mian;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.a21vianet.wallet.vport.R;
import com.a21vianet.wallet.vport.action.historyoperation.HistoryOperationActivity;
import com.a21vianet.wallet.vport.action.identityinfo.IdentityInfoActivity;
import com.a21vianet.wallet.vport.action.info.PersonalInfoActivity;
import com.a21vianet.wallet.vport.action.password.PasswordManager;
import com.a21vianet.wallet.vport.action.scan.ScanActivity;
import com.a21vianet.wallet.vport.action.scan.data.ScanDataTask;
import com.a21vianet.wallet.vport.action.setting.SettingActivity;
import com.a21vianet.wallet.vport.library.commom.crypto.CryptoManager;
import com.a21vianet.wallet.vport.library.commom.crypto.NoDecryptException;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.JWTBean;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.LoginTokenContext;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.UserLoginTokenContext;
import com.a21vianet.wallet.vport.library.commom.crypto.callback.OnFinishedListener;
import com.a21vianet.wallet.vport.library.event.ScanResultEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.littlesparkle.growler.core.am.ActivityUtility;
import com.littlesparkle.growler.core.ui.activity.BaseMainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.GeneralSecurityException;

import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;

import static com.github.orangegangsters.lollipin.lib.managers.AppLockActivity.EXT_DATA;

public class MainActivity extends BaseMainActivity {

    private static final int REQUEST_ENTER_PASSWORD = 0x1;
    public static final int REQUEST_CODE_SCAN = 8080;
    public static final String SUB_LOGIN_TOKEN = "login token";
    public static final String SUB_AUTHORIZATION_TOKEN = "authorization token";

    @Override
    protected void initData() {
        checkEnCode();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScanResultGet(final ScanResultEvent scanResultEvent) {
        CryptoManager.getInstance().decodeJWTToken(this, scanResultEvent.result, new OnFinishedListener() {
            @Override
            public void onFinished(String s) {
                Gson gson = new Gson();
                JWTBean<Object> jwtBean = gson.fromJson(s, new TypeToken<JWTBean<Object>>() {
                }.getType());
                switch (jwtBean.payload.sub) {
                    case SUB_LOGIN_TOKEN:
                        JWTBean<LoginTokenContext> loginTokenContextJWTBean = gson.fromJson(s, new TypeToken<JWTBean<LoginTokenContext>>() {
                        }.getType());
                        JWTBean<UserLoginTokenContext> userLoginTokenContextJWTBean = new ScanDataTask().getJWTBeanFromSeverJWTBean(loginTokenContextJWTBean);
                        if (userLoginTokenContextJWTBean != null) {
                            try {
                                CryptoManager.getInstance().signJWTToken(MainActivity.this
                                        , gson.toJson(userLoginTokenContextJWTBean)
                                        , new OnFinishedListener() {
                                            @Override
                                            public void onFinished(String s) {
                                                System.out.println(s);
                                            }
                                        });
                            } catch (NoDecryptException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case SUB_AUTHORIZATION_TOKEN:
                        break;
                }
            }
        });
    }
}
