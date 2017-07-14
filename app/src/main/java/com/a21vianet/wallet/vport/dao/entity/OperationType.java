package com.a21vianet.wallet.vport.dao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wang.rongqiang on 2017/6/27.
 */

@Entity
public class OperationType {
    @Id
    private Long id;

    private String name;

    @Generated(hash = 1731733871)
    public OperationType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 1023853362)
    public OperationType() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
