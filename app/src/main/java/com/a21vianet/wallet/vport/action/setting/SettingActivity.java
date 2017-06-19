package com.a21vianet.wallet.vport.action.setting;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.a21vianet.wallet.vport.R;
import com.a21vianet.wallet.vport.action.login.GuideLoginActivity;
import com.a21vianet.wallet.vport.action.password.CustomPinActivity;
import com.a21vianet.wallet.vport.library.commom.crypto.CryptoManager;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.Contract;
import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.littlesparkle.growler.core.am.ActivityManager;
import com.littlesparkle.growler.core.ui.activity.BaseTitleBarActivity;

import butterknife.BindView;
import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;

import static com.a21vianet.wallet.vport.library.constant.SysConstant.clearHradImageUrlHash;

public class SettingActivity extends BaseTitleBarActivity {
    @BindView(R.id.app_version)
    TextView mTvAppVersion;

    @Override
    protected int selfTitleResId() {
        return R.string.setting;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary));
        try {
            // ---get the package info---
            PackageManager pm = this.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(this.getPackageName(), 0);
            mTvAppVersion.setText(pi.versionName);
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }

    }

    @OnClick({R.id.tv_setting_policies, R.id.tv_setting_version, R.id
            .tv_setting_about, R.id.tv_setting_logout})
    public void onViewClicked(View view) {
        Intent customPinintent = new Intent(this, CustomPinActivity.class);
        customPinintent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);

        switch (view.getId()) {
            case R.id.tv_setting_policies:
                break;
            case R.id.tv_setting_version:
                break;
            case R.id.tv_setting_about:
                break;
            case R.id.tv_setting_logout:
                CryptoManager.getInstance().cleanBitcoinKey();
                new Contract().clear();
                clearHradImageUrlHash();
                ActivityManager.getInstance().finishAll();
                startActivity(new Intent(SettingActivity.this, GuideLoginActivity.class));
                break;
        }
    }

}
