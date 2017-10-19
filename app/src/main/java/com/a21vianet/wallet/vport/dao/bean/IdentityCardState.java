package com.a21vianet.wallet.vport.dao.bean;

/**
 * Created by wang.rongqiang on 2017/7/4.
 */

public enum IdentityCardState {
    /**
     * 待认证
     */
    NONE(1),
    /**
     * 认证成功
     */
    APPROVED(2),
    /**
     * 认证中
     */
    PENDING(3),
    /**
     * 认证失败
     */
    REJECTED(4);

    public int state;

    IdentityCardState(int i) {
        state = i;
    }
}
