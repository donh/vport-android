package com.a21vianet.wallet.vport;

import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.webkit.WebView;

import com.a21vianet.wallet.vport.action.password.CustomPinActivity;
import com.a21vianet.wallet.vport.dao.GreenDaoManager;
import com.a21vianet.wallet.vport.dao.OperationTypeManager;
import com.a21vianet.wallet.vport.library.BaseApplication;
import com.a21vianet.wallet.vport.library.commom.crypto.SingleWebView;
import com.a21vianet.wallet.vport.library.constant.SysConstant;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;
import com.littlesparkle.growler.core.common.TempDirectory;
import com.littlesparkle.growler.core.http.api.Api;
import com.littlesparkle.growler.core.utility.PrefUtility;

import java.io.File;

public class WalletApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, CustomPinActivity.class);
        lockManager.getAppLock().setLogoId(R.drawable.security_lock);

        Api.Config config = new Api.Config();
        config.setBaseUrl(com.a21vianet.wallet.vport.http.Api.vChainApi);
        config.setTimeout(120);
        Api.init(this, config);

        TempDirectory.init(Environment.getExternalStorageDirectory().getPath()
                + File.separator + "gcoin_wallet");

        GreenDaoManager.getInstance();

        if (!PrefUtility.getBoolean(this, SysConstant.IS_INIT_TABLE, false)) {
            OperationTypeManager.inittable();
            PrefUtility.setBoolean(this, SysConstant.IS_INIT_TABLE, true);
        }

        SingleWebView.getInstance(this, new SingleWebView.WebViewInitCallback() {
            @Override
            public void onSuccess(WebView webView) {

            }
        });

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
