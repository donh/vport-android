package com.a21vianet.wallet.vport.scan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.a21vianet.wallet.vport.R;
import com.littlesparkle.growler.core.ui.fragment.BaseFragmentActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

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

    private void initScanCallBack() {
        analyzeCallback = new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
                bundle.putString(CodeUtils.RESULT_STRING, result);
                resultIntent.putExtras(bundle);
                ScanActivity.this.setResult(RESULT_OK, resultIntent);
                ScanActivity.this.finish();
            }

            @Override
            public void onAnalyzeFailed() {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
                bundle.putString(CodeUtils.RESULT_STRING, "");
                resultIntent.putExtras(bundle);
                ScanActivity.this.setResult(RESULT_OK, resultIntent);
                ScanActivity.this.finish();
            }
        };
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_scan;
    }

}
