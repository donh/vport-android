package com.littlesparkle.growler.core.am;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ManagedBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().onCreate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivityManager.getInstance().onStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityManager.getInstance().onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager.getInstance().onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ActivityManager.getInstance().onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().onDestroy(this);
    }
}
