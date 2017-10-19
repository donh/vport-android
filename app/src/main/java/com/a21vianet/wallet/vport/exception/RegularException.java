package com.a21vianet.wallet.vport.exception;

/**
 * Created by wang.rongqiang on 2017/6/8.
 * 正则匹配不通过抛出的异常
 */

public class RegularException extends Exception {
    public RegularException(String message) {
        super(message);
    }
}
