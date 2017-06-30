package com.a21vianet.wallet.vport;

import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.a21vianet.wallet.vport.action.password.CustomPinActivity;
import com.a21vianet.wallet.vport.dao.GreenDaoManager;
import com.a21vianet.wallet.vport.library.BaseApplication;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;
import com.littlesparkle.growler.core.common.TempDirectory;
import com.littlesparkle.growler.core.http.api.Api;

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
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
