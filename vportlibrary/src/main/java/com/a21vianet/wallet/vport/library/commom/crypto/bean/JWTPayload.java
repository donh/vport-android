package com.a21vianet.wallet.vport.library.commom.crypto.bean;


public class JWTPayload<T> {

    public String iss;
    public String aud;
    public int iat;
    public int exp;
    public String sub;
    public T context;

    public JWTPayload() {
    }

    public JWTPayload(String iss, String aud, int iat, int exp, String sub, T context) {
        this.iss = iss;
        this.aud = aud;
        this.iat = iat;
        this.exp = exp;
        this.sub = sub;
        this.context = context;
    }

    @Override
    public String toString() {
        return "JWTPayload{" +
                "iss='" + iss + '\'' +
                ", aud='" + aud + '\'' +
                ", iat='" + iat + '\'' +
                ", exp='" + exp + '\'' +
                ", sub='" + sub + '\'' +
                ", context=" + context +
                '}';
    }
}
