package com.a21vianet.wallet.vport.library.commom.http.vport;


import com.a21vianet.wallet.vport.library.commom.http.vport.bean.Result;

public class LoginResponse {

    public Result result;
    public String time;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "result=" + result +
                ", time='" + time + '\'' +
                '}';
    }

}
