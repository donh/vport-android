package com.a21vianet.wallet.vport.library.commom.crypto.callback;

/**
 * Created by wang.rongqiang on 2017/5/31.
 */

public interface MultisigCallback {
    void onSinged(String signedRawTransaction);

    void onError(Exception e);
}
