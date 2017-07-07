package com.a21vianet.wallet.vport.library.commom.http.vport.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wang.rongqiang on 2017/7/5.
 */

public class CertificationResult {


    /**
     * result : {"status":"PENDING"}
     * time : 2017-07-07 10:47:20
     */

    @SerializedName("result")
    private ResultBean result;
    @SerializedName("time")
    private String time;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static class ResultBean {
        /**
         * status : PENDING
         */

        @SerializedName("status")
        private String status;
        @SerializedName("attestation")
        private String attestation;

        public String getAttestation() {
            return attestation;
        }

        public void setAttestation(String attestation) {
            this.attestation = attestation;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
