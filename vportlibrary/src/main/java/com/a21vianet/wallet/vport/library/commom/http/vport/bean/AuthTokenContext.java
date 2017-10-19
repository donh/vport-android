package com.a21vianet.wallet.vport.library.commom.http.vport.bean;


public class AuthTokenContext {

    public String requesterName;
    public String scope;
    public String token;
    public String serverPublicKey;

    public AuthTokenContext(String requesterName, String scope, String token, String serverPublicKey) {
        this.requesterName = requesterName;
        this.scope = scope;
        this.token = token;
        this.serverPublicKey = serverPublicKey;
    }

    @Override
    public String toString() {
        return "AuthTokenContext{" +
                "requesterName='" + requesterName + '\'' +
                ", scope='" + scope + '\'' +
                ", token='" + token + '\'' +
                ", serverPublicKey='" + serverPublicKey + '\'' +
                '}';
    }
}
