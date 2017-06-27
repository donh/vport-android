package com.a21vianet.wallet.vport.dao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wang.rongqiang on 2017/6/27.
 */

@Entity
public class OperationType {
    @Id(autoincrement = true)
    private long id;

    private String name;

    @Generated(hash = 1769149080)
    public OperationType(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 1023853362)
    public OperationType() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
