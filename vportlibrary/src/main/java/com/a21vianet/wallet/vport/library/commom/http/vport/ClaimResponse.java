package com.a21vianet.wallet.vport.library.commom.http.vport;


import com.a21vianet.wallet.vport.library.commom.http.vport.bean.ClaimResult;

public class ClaimResponse {

    public ClaimResult result;
    public String time;

    @Override
    public String toString() {
        return "ClaimResponse{" +
                "result=" + result +
                ", time='" + time + '\'' +
                '}';
    }
}
