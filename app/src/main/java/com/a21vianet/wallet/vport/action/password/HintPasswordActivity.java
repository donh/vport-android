package com.a21vianet.wallet.vport.action.password;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.a21vianet.wallet.vport.R;
import com.a21vianet.wallet.vport.action.createkey.CreateKeyActivity;
import com.a21vianet.wallet.vport.action.mian.MainActivity;
import com.a21vianet.wallet.vport.library.commom.crypto.CryptoManager;
import com.a21vianet.wallet.vport.library.commom.crypto.callback.GenerateCallBack;
import com.littlesparkle.growler.core.am.ActivityManager;
import com.littlesparkle.growler.core.am.ActivityUtility;
import com.littlesparkle.growler.core.ui.activity.BaseActivity;
import com.littlesparkle.growler.core.ui.toast.ToastFactory;

import java.util.ArrayList;

import qiu.niorgai.StatusBarCompat;

import static com.github.orangegangsters.lollipin.lib.managers.AppLockActivity.EXT_DATA;

public class HintPasswordActivity extends BaseActivity {
    private static final String WORD_LIST = "word_list";
    /**
     * 设置密码
     */
    private static final int REQUEST_CODE_ENABLE = 11;

    private ArrayList<String> mBitcoinWordList;

    public static void startActivity(Activity context) {
        ActivityUtility.startActivityWithAnim(context, new Intent(context, HintPasswordActivity.class));
    }

    public static void startActivity(Context context, ArrayList<String> list) {
        Intent intent = new Intent(context, HintPasswordActivity.class);
        intent.putStringArrayListExtra(WORD_LIST, list);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        mBitcoinWordList = getIntent().getStringArrayListExtra(WORD_LIST);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarCompat.translucentStatusBar(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_hint_password;
    }

    public void onClick(View view) {
        PasswordManager.startEnterPassword(this, REQUEST_CODE_ENABLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String stringExtra = null;
        if (data != null) {
            stringExtra = data.getStringExtra(EXT_DATA);
        }
        switch (requestCode) {
            case REQUEST_CODE_ENABLE:
                if (stringExtra != null) {
                    if (mBitcoinWordList == null) {
                        CreateKeyActivity.startActivity(this, stringExtra);
                        finish();
                    } else {
                        resetKey(stringExtra);
                    }
                }
                break;
        }
    }


    private void resetKey(String pass) {
        showProgress();
        CryptoManager.getInstance().resetBitcoinKeyPair(pass, mBitcoinWordList, new
                GenerateCallBack() {
                    @Override
                    public void onSuccess() {
                        dismissProgress();
                        ToastFactory.getInstance(getApplicationContext()).makeTextShow("用户恢复成功",
                                Toast
                                        .LENGTH_SHORT);
                        ActivityManager.getInstance().finishAll();
                        startActivity(new Intent(HintPasswordActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onError() {
                        dismissProgress();
                        ToastFactory.getInstance(getApplicationContext()).makeTextShow("用户恢复失败",
                                Toast
                                        .LENGTH_SHORT);
                        finish();
                    }
                });

    }
}
