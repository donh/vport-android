package com.a21vianet.wallet.vport.library.commom.crypto.bean;


public class UserAuthTokenContext {

    public String scope;
    public String token;
    public String userProxy;
    public String userPublicKey;

    public UserAuthTokenContext(String scope, String token, String userProxy, String userPublicKey) {
        this.scope = scope;
        this.token = token;
        this.userProxy = userProxy;
        this.userPublicKey = userPublicKey;
    }

    @Override
    public String toString() {
        return "UserAuthTokenContext{" +
                "scope='" + scope + '\'' +
                ", token='" + token + '\'' +
                ", userProxy='" + userProxy + '\'' +
                ", userPublicKey='" + userPublicKey + '\'' +
                '}';
    }
}
