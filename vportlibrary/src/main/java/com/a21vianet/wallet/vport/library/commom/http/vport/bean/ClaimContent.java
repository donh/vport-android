package com.a21vianet.wallet.vport.library.commom.http.vport.bean;


public class ClaimContent {

    public String content;
    public String proxy;
    public String status;
    public String type;

    @Override
    public String toString() {
        return "ClaimContent{" +
                "content='" + content + '\'' +
                ", proxy='" + proxy + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
