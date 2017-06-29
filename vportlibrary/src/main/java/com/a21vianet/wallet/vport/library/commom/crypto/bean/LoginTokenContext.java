package com.a21vianet.wallet.vport.library.commom.crypto.bean;


public class LoginTokenContext {

    public String clientID;
    public String clientURL;
    public String scope;
    public String serverPublicKey;
    public String serverURL;
    public String token;
    public String clientName;

    public LoginTokenContext(String clientID, String clientURL, String scope, String serverPublicKey, String serverURL, String token, String clientName) {
        this.clientID = clientID;
        this.clientURL = clientURL;
        this.scope = scope;
        this.serverPublicKey = serverPublicKey;
        this.serverURL = serverURL;
        this.token = token;
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return "LoginTokenContext{" +
                "clientID='" + clientID + '\'' +
                ", clientURL='" + clientURL + '\'' +
                ", scope='" + scope + '\'' +
                ", serverPublicKey='" + serverPublicKey + '\'' +
                ", serverURL='" + serverURL + '\'' +
                ", token='" + token + '\'' +
                ", clientName='" + clientName + '\'' +
                '}';
    }
}
