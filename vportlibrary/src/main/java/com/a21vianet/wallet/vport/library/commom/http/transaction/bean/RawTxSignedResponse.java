package com.a21vianet.wallet.vport.library.commom.http.transaction.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wang.rongqiang on 2017/6/9.
 */

public class RawTxSignedResponse {

    /**
     * contractSubscription : {"callback_url":"http://smart-contract.test1.vchain
     * .io:7787/notify/f3e98bde380704763e8321fdc8b7e58ca9323e4c970c444976efe6e38e3457a4",
     * "confirmation_count":1,"created_time":"2017-04-17T06:30:19.376Z",
     * "id":"f065569c-93ca-4d41-aa25-099288c4b745",
     * "tx_hash":"f3e98bde380704763e8321fdc8b7e58ca9323e4c970c444976efe6e38e3457a4"}
     * oracleSubscription : {"callback_url":"http://smart-contract.test1.vchain
     * .io:7788/notify/f3e98bde380704763e8321fdc8b7e58ca9323e4c970c444976efe6e38e3457a4",
     * "confirmation_count":1,"created_time":"2017-04-17T06:30:19.529Z",
     * "id":"2681841a-1276-42e5-a1d0-a879520d0dd3",
     * "tx_hash":"f3e98bde380704763e8321fdc8b7e58ca9323e4c970c444976efe6e38e3457a4"}
     * rawTxSend : f3e98bde380704763e8321fdc8b7e58ca9323e4c970c444976efe6e38e3457a4
     */

    @SerializedName("contractSubscription")
    private ContractSubscriptionBean contractSubscription;
    @SerializedName("oracleSubscription")
    private OracleSubscriptionBean oracleSubscription;
    @SerializedName("rawTxSend")
    private String rawTxSend;

    public ContractSubscriptionBean getContractSubscription() {
        return contractSubscription;
    }

    public void setContractSubscription(ContractSubscriptionBean contractSubscription) {
        this.contractSubscription = contractSubscription;
    }

    public OracleSubscriptionBean getOracleSubscription() {
        return oracleSubscription;
    }

    public void setOracleSubscription(OracleSubscriptionBean oracleSubscription) {
        this.oracleSubscription = oracleSubscription;
    }

    public String getRawTxSend() {
        return rawTxSend;
    }

    public void setRawTxSend(String rawTxSend) {
        this.rawTxSend = rawTxSend;
    }

    public static class ContractSubscriptionBean {
        /**
         * callback_url : http://smart-contract.test1.vchain
         * .io:7787/notify/f3e98bde380704763e8321fdc8b7e58ca9323e4c970c444976efe6e38e3457a4
         * confirmation_count : 1
         * created_time : 2017-04-17T06:30:19.376Z
         * id : f065569c-93ca-4d41-aa25-099288c4b745
         * tx_hash : f3e98bde380704763e8321fdc8b7e58ca9323e4c970c444976efe6e38e3457a4
         */

        @SerializedName("callback_url")
        private String callbackUrl;
        @SerializedName("confirmation_count")
        private int confirmationCount;
        @SerializedName("created_time")
        private String createdTime;
        @SerializedName("id")
        private String id;
        @SerializedName("tx_hash")
        private String txHash;

        public String getCallbackUrl() {
            return callbackUrl;
        }

        public void setCallbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
        }

        public int getConfirmationCount() {
            return confirmationCount;
        }

        public void setConfirmationCount(int confirmationCount) {
            this.confirmationCount = confirmationCount;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTxHash() {
            return txHash;
        }

        public void setTxHash(String txHash) {
            this.txHash = txHash;
        }
    }

    public static class OracleSubscriptionBean {
        /**
         * callback_url : http://smart-contract.test1.vchain
         * .io:7788/notify/f3e98bde380704763e8321fdc8b7e58ca9323e4c970c444976efe6e38e3457a4
         * confirmation_count : 1
         * created_time : 2017-04-17T06:30:19.529Z
         * id : 2681841a-1276-42e5-a1d0-a879520d0dd3
         * tx_hash : f3e98bde380704763e8321fdc8b7e58ca9323e4c970c444976efe6e38e3457a4
         */

        @SerializedName("callback_url")
        private String callbackUrl;
        @SerializedName("confirmation_count")
        private int confirmationCount;
        @SerializedName("created_time")
        private String createdTime;
        @SerializedName("id")
        private String id;
        @SerializedName("tx_hash")
        private String txHash;

        public String getCallbackUrl() {
            return callbackUrl;
        }

        public void setCallbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
        }

        public int getConfirmationCount() {
            return confirmationCount;
        }

        public void setConfirmationCount(int confirmationCount) {
            this.confirmationCount = confirmationCount;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTxHash() {
            return txHash;
        }

        public void setTxHash(String txHash) {
            this.txHash = txHash;
        }
    }
}
