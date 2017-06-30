package com.a21vianet.wallet.vport.action.scan;

import android.content.Intent;

import com.a21vianet.wallet.vport.R;
import com.a21vianet.wallet.vport.action.mian.MainActivity;
import com.a21vianet.wallet.vport.library.event.ScanResultEvent;
import com.littlesparkle.growler.core.ui.fragment.BaseFragmentActivity;

import org.greenrobot.eventbus.EventBus;

import me.dm7.barcodescanner.zbar.Result;
import qiu.niorgai.StatusBarCompat;

public class ScanActivity extends BaseFragmentActivity implements ScannerCallback {

    @Override
    protected void initData() {
        ScannerFragment scannerFragment = new ScannerFragment();
        scannerFragment.setScannerCallback(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.wallet_scan_frame_layout,
                scannerFragment).commit();
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarCompat.translucentStatusBar(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_scan;
    }

    @Override
    public void onSuccess(Result result) {
        startActivity(new Intent(ScanActivity.this, MainActivity.class));
        EventBus.getDefault().postSticky(new ScanResultEvent(result.getContents()));
        ScanActivity.this.finish();
    }
}
