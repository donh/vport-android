package com.a21vianet.wallet.vport.dao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wang.rongqiang on 2017/6/27.
 */
@Entity
public class IdentityCard {
    @Id(autoincrement = true)
    private long id;

    private String name;
    private long number;
    private String begintime;
    private String endtime;
    private String agencies;
    private int state;
    @Generated(hash = 854298168)
    public IdentityCard(long id, String name, long number, String begintime,
            String endtime, String agencies, int state) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.begintime = begintime;
        this.endtime = endtime;
        this.agencies = agencies;
        this.state = state;
    }
    @Generated(hash = 1367070082)
    public IdentityCard() {
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
    public long getNumber() {
        return this.number;
    }
    public void setNumber(long number) {
        this.number = number;
    }
    public String getBegintime() {
        return this.begintime;
    }
    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }
    public String getEndtime() {
        return this.endtime;
    }
    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
    public String getAgencies() {
        return this.agencies;
    }
    public void setAgencies(String agencies) {
        this.agencies = agencies;
    }
    public int getState() {
        return this.state;
    }
    public void setState(int state) {
        this.state = state;
    }

}
