package com.a21vianet.wallet.vport.action.scan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.a21vianet.wallet.vport.R;
import com.a21vianet.wallet.vport.action.mian.MainActivity;
import com.a21vianet.wallet.vport.library.event.ScanResultEvent;
import com.littlesparkle.growler.core.ui.fragment.BaseFragmentActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;

import qiu.niorgai.StatusBarCompat;

public class ScanActivity extends BaseFragmentActivity {
    CodeUtils.AnalyzeCallback analyzeCallback = null;

    @Override
    protected void initData() {
        initScanCallBack();
        CaptureFragment captureFragment = new CaptureFragment();
        CodeUtils.setFragmentArgs(captureFragment, R.layout.fragment_scan);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.wallet_scan_frame_layout, captureFragment).commit();
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarCompat.translucentStatusBar(this);
    }

    private void initScanCallBack() {
        analyzeCallback = new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                result = "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NksifQ.eyJpc3MiOiJ2cG9ydC5jaGFuY2hlbmcuc2VydmVyIiwiYXVkIjoidnBvcnQuY2hhbmNoZW5nLnVzZXIiLCJpYXQiOjE0OTgwNDUwNDUsImV4cCI6MTQ5ODA0NTM0NSwic3ViIjoibG9naW4gdG9rZW4iLCJjb250ZXh0Ijp7ImNsaWVudElEIjoidjAwMWNoYW5jaGVuZyIsImNsaWVudFVSTCI6Imh0dHBzOi8vZGFwcC5leGFtcGxlLnZwb3J0LmNvbSIsInNjb3BlIjoibmFtZSxwaG9uZSxwdWJsaWNLZXkscHJveHksY29udHJvbGxlcixyZWNvdmVyeSxjaXR5Iiwic2VydmVyUHVibGljS2V5IjoiMDQ2MDJlMzU1ODY2NGU2NDQ2MGU0NjkwYmVjZTdhZGJiZDMwNmI3NjU0MDk0NjBhYTYxYzJkMjcyYmI5NzNiYTAwZDRmODMzY2RiYjFhNWZlMDM1MTM5MWFjN2UxMDA2ZmVmZmYyZDA1OTBlOTg5YWE1ZTdhYTUzN2NmODZiZWU0YSIsInNlcnZlclVSTCI6Imh0dHBzOi8vbWVzc2FnaW5nLXNlcnZlci5leGFtcGxlLnZwb3J0LmNvbSIsInRva2VuIjoiNDFkMjQzZmFmM2IyNDI5ZDgwNzdmYjA5MjhiMTliMjQifX0.cYsEa9dvHbTxOVCmTWBex5Kv1nEm_pfmAq_NOR_62sBkyo-fvVZU9NlNndChyT_z6AsiW2ulKe9i3li0XPSwyw";
                startActivity(new Intent(ScanActivity.this, MainActivity.class));
                EventBus.getDefault().postSticky(new ScanResultEvent(result));
                ScanActivity.this.finish();
            }

            @Override
            public void onAnalyzeFailed() {
                Toast.makeText(ScanActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                ScanActivity.this.finish();
            }
        };
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_scan;
    }

}
