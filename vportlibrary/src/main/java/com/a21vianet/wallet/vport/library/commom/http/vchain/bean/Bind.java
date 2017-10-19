package com.a21vianet.wallet.vport.library.commom.http.vchain.bean;


public class Bind {

    public Controller controller;
    public Proxy proxy;
    public Recover recover;

    @Override
    public String toString() {
        return "Bind{" +
                "controller=" + controller +
                ", proxy=" + proxy +
                ", recover=" + recover +
                '}';
    }
}
