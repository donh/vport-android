package com.a21vianet.wallet.vport.library;

import android.app.Application;
import android.content.Context;

/**
 * Created by wang.rongqiang on 2017/6/14.
 */

public class BaseApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    public static Context getContext() {
        return sContext;
    }
}
