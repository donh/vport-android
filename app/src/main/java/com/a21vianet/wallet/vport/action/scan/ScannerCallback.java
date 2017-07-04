package com.a21vianet.wallet.vport.action.scan;

import com.google.zxing.Result;

/**
 * Created by wang.rongqiang on 2017/6/30.
 */

public interface ScannerCallback {
    void onSuccess(Result rawResult);
}
