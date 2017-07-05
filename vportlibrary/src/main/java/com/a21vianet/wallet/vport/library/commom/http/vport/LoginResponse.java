package com.a21vianet.wallet.vport.library.commom.http.vport;


import com.a21vianet.wallet.vport.library.commom.http.vport.bean.LoginResult;

public class LoginResponse {

    public LoginResult loginResult;
    public String time;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "loginResult=" + loginResult +
                ", time='" + time + '\'' +
                '}';
    }

}
