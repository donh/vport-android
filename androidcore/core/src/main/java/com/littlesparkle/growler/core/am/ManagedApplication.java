package com.littlesparkle.growler.core.am;

import android.app.Application;

public class ManagedApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActivityManager.getInstance().configure(this);
    }
}
