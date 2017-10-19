package com.a21vianet.wallet.vport.dao.bean;

/**
 * Created by wang.rongqiang on 2017/6/28.
 */

public enum OperationStateEnum {
    /**
     * 成功
     */
    Success(1),
    /**
     * 失败
     */
    Error(2),
    /**
     * 取消
     */
    Cancel(3);

    public final int state;

    OperationStateEnum(int l) {
        state = l;
    }
}
