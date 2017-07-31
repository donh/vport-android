package com.a21vianet.wallet.vport.library.event;

/**
 * 二维码扫描结果返回时间
 */
public class ScanResultEvent {

    public String result;

    public ScanResultEvent(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ScanResultEvent{" +
                "loginResult='" + result + '\'' +
                '}';
    }
}
