package com.a21vianet.wallet.vport.library.commom.http.ipfs.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by wang.rongqiang on 2017/6/12.
 */

public class UserInfoIPFSGET {

    /**
     * registry : 0x77fe3b9325c5fbbb8e8f4d42c0555d3806b05283985ac13c61a32fac2ad0173e
     * ipfsHex : QmWR452efCXH8q9wkM73ckbAxpmRm2iNF2jVkS5v3dssxq
     * value : {"address":"1G6DLbZi1A1s1utakX3mLpbkrqe9HtBozg","@context":"http://schema.org",
     * "description":"","name":"哈哈","network":"vChain",
     * "publicKey":"03e4c8693106d5b76a7534a16a0789bd394ee9dde6367b59dffcee5ba73f5611c2",
     * "@type":"Person"}
     */

    @SerializedName("registry")
    private String registry;
    @SerializedName("ipfsHex")
    private String ipfsHex;
    @SerializedName("value")
    private String value;

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public String getIpfsHex() {
        return ipfsHex;
    }

    public void setIpfsHex(String ipfsHex) {
        this.ipfsHex = ipfsHex;
    }

    public UserInfoIPFS getValue() {
        return new Gson().fromJson(value, UserInfoIPFS.class);
    }

    public void setValue(String value) {
        this.value = value;
    }
}
