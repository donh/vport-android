package com.a21vianet.wallet.vport.library.commom.http.vchain.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wang.rongqiang on 2017/6/16.
 */

public class GetColor1Response {

    /**
     * loginResult : Successful!
     * tx_id : 2f135cbc55173edaa0d65deb2ca6bd0efbbb420f92799f6d9697b2f312a58490
     */

    @SerializedName("loginResult")
    private String result;
    @SerializedName("tx_id")
    private String txId;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }
}
