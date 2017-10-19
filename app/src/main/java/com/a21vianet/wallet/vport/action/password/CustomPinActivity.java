package com.a21vianet.wallet.vport.action.password;

import com.github.orangegangsters.lollipin.lib.managers.AppLockActivity;

/**
 * Created by wang.rongqiang on 2017/6/6.
 */

public class CustomPinActivity extends AppLockActivity {

    @Override
    public void onPinFailure(int attempts) {

    }

    @Override
    public void onPinSuccess(int attempts) {

    }

    @Override
    public int getPinLength() {
        return super.getPinLength();//you can override this method to change the pin length from
        // the default 4
    }
}
