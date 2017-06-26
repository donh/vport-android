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
