package com.a21vianet.wallet.vport.library.event;


public class ScanResultEvent {

    public String result;

    public ScanResultEvent(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ScanResultEvent{" +
                "result='" + result + '\'' +
                '}';
    }
}
