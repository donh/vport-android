package com.a21vianet.wallet.vport.library.commom.crypto.bean;


public class JWTBean<T> {

    public JWTHeader header;
    public JWTPayload<T> payload;
    public String signature;

    public JWTBean() {
    }

    public JWTBean(JWTHeader header, JWTPayload payload, String signature) {
        this.header = header;
        this.payload = payload;
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "JWTBean{" +
                "header=" + header +
                ", payload=" + payload +
                ", signature='" + signature + '\'' +
                '}';
    }
}
