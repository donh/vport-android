package com.a21vianet.wallet.vport.library.commom.http.vchain.bean;

/**
 * Created by wang.rongqiang on 2017/6/9.
 */

public class VPortCreateRequestBean {

    public VPortCreateRequestBean() {
    }

    public VPortCreateRequestBean(String name, String phone, String email, String id, String
            privateKey, String publicKey, String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.id = id;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.address = address;
    }

    private String name;
    private String phone;
    private String email;
    private String id;
    private String privateKey;
    private String publicKey;
    private String address;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
