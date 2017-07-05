package com.a21vianet.wallet.vport.library.commom.http.vport.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wang.rongqiang on 2017/7/5.
 */

public class CertificationResult {

    /**
     * status : PENDING
     * attestation :
     * time : 2017-07-03 13:59:28
     */

    @SerializedName("status")
    private String status;
    @SerializedName("attestation")
    private String attestation;
    @SerializedName("time")
    private String time;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAttestation() {
        return attestation;
    }

    public void setAttestation(String attestation) {
        this.attestation = attestation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
