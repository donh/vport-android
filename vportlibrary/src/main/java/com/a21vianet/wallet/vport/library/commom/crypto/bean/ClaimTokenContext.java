package com.a21vianet.wallet.vport.library.commom.crypto.bean;


public class ClaimTokenContext {

    public String clientName;
    public String serverPublicKey;
    public String token;


    public ClaimTokenContext(String clientName, String serverPublicKey, String token) {
        this.clientName = clientName;
        this.serverPublicKey = serverPublicKey;
        this.token = token;
    }

    @Override
    public String toString() {
        return "ClaimTokenContext{" +
                "clientName='" + clientName + '\'' +
                ", serverPublicKey='" + serverPublicKey + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
