package com.a21vianet.wallet.vport.library.commom.http.vchain.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wang.rongqiang on 2017/6/9.
 */

public class VPortCreateRequestBean {

    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("privateKey")
    private String privateKey;
    @SerializedName("publicKey")
    private String publicKey;
    @SerializedName("address")
    private String address;

    public VPortCreateRequestBean() {
    }

    public VPortCreateRequestBean(String name, String phone, String privateKey, String publicKey,
                                  String address) {
        this.name = name;
        this.phone = phone;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
