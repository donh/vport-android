package com.a21vianet.wallet.vport.dao.bean;

/**
 * Created by wang.rongqiang on 2017/6/28.
 */

public enum OperationTypeEnum {
    Login(1l), Approve(2l), Accredit(3l);

    public final long typeId;

    OperationTypeEnum(long l) {
        typeId = l;
    }
}
