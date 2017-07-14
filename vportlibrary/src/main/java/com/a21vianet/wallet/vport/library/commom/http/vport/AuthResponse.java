package com.a21vianet.wallet.vport.library.commom.http.vport;

import com.a21vianet.wallet.vport.library.commom.http.vport.bean.AuthResult;


public class AuthResponse {

    public AuthResult result;
    public String time;

    @Override
    public String toString() {
        return "AuthResponse{" +
                "authResult=" + result +
                ", time='" + time + '\'' +
                '}';
    }
}
