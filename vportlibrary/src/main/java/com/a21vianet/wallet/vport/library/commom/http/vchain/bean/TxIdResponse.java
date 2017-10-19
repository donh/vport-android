package com.a21vianet.wallet.vport.library.commom.http.vchain.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wang.rongqiang on 2017/6/14.
 */

public class TxIdResponse {

    /**
     * tx_id : 08702c48d3e6d90490faf15d57458c461a5780b7796506c96156c9a219f58971
     */

    @SerializedName("tx_id")
    private String txId;

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }
}
