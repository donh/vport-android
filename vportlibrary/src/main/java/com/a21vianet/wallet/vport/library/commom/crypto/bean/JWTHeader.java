package com.a21vianet.wallet.vport.library.commom.crypto.bean;


public class JWTHeader {

    public String alg;
    public String typ;

    public JWTHeader(String alg, String typ) {
        this.alg = alg;
        this.typ = typ;
    }

    @Override
    public String toString() {
        return "JWTHeader{" +
                "alg='" + alg + '\'' +
                ", typ='" + typ + '\'' +
                '}';
    }
}
