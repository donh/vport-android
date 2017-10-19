package com.a21vianet.wallet.vport.dao.bean;

/**
 * Created by wang.rongqiang on 2017/6/28.
 */

public enum OperationTypeEnum {
    /**
     * 登录
     */
    Login(1l),
    /**
     * 认证
     */
    Approve(2l),
    /**
     * 授权
     */
    Accredit(3l),
    /**
     * 声明
     */
    Statement(4L);


    public final long typeId;

    OperationTypeEnum(long l) {
        typeId = l;
    }
}
