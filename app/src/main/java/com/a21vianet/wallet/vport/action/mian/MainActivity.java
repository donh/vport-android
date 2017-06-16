package com.a21vianet.wallet.vport.action.mian;

import android.content.Intent;
import android.view.View;

import com.a21vianet.wallet.vport.R;
import com.a21vianet.wallet.vport.action.scan.ScanActivity;
import com.littlesparkle.growler.core.am.ActivityUtility;
import com.littlesparkle.growler.core.ui.activity.BaseMainActivity;

import butterknife.OnClick;

public class MainActivity extends BaseMainActivity {

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }


    @OnClick({R.id.relative_visiting, R.id.relative_identity, R.id.relative_operation, R.id
            .imgv_main_scan})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.relative_visiting:
                break;
            case R.id.relative_identity:
                break;
            case R.id.relative_operation:
                break;
            case R.id.imgv_main_scan:
                intent = new Intent(this, ScanActivity.class);
                break;
        }
        if (intent != null) {
            ActivityUtility.startActivityWithAnim(this, intent);
        }
    }
}
