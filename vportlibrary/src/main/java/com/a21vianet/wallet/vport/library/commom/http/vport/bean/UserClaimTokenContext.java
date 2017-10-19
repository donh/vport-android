package com.a21vianet.wallet.vport.library.commom.http.vport.bean;


public class UserClaimTokenContext {

    public String authority;
    public String expiryDate;
    public String gender;
    public String ID;
    public String issueDate;
    public String name;
    public String token;
    public String userProxy;
    public String userPublicKey;

    public UserClaimTokenContext(String authority, String expiryDate, String gender, String ID, String issueDate, String name, String token, String userProxy, String userPublicKey) {
        this.authority = authority;
        this.expiryDate = expiryDate;
        this.gender = gender;
        this.ID = ID;
        this.issueDate = issueDate;
        this.name = name;
        this.token = token;
        this.userProxy = userProxy;
        this.userPublicKey = userPublicKey;
    }

    @Override
    public String toString() {
        return "UserClaimTokenContext{" +
                "authority='" + authority + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", gender='" + gender + '\'' +
                ", ID='" + ID + '\'' +
                ", issueDate='" + issueDate + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", userProxy='" + userProxy + '\'' +
                ", userPublicKey='" + userPublicKey + '\'' +
                '}';
    }
}
