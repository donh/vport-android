package com.a21vianet.wallet.vport.library.commom.crypto.bean;


public class UserLoginTokenContext {

    public String scope;
    public String token;
    public String userProxy;
    public String userPublicKey;

    public UserLoginTokenContext(String scope, String token, String userProxy, String userPublicKey) {
        this.scope = scope;
        this.token = token;
        this.userProxy = userProxy;
        this.userPublicKey = userPublicKey;
    }

    @Override
    public String toString() {
        return "UserLoginTokenContext{" +
                "scope='" + scope + '\'' +
                ", token='" + token + '\'' +
                ", userProxy='" + userProxy + '\'' +
                ", userPublicKey='" + userPublicKey + '\'' +
                '}';
    }
}
