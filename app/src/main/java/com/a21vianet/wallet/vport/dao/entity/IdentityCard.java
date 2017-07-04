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
    private Long id;

    private String name;
    private String number;
    private String begintime;
    private String endtime;
    private String agencies;
    //1. 待认证 2.认证成功 3.认证失败
    private int state;
    private String token;
    private String jwt;

    public IdentityCard(Long id, String name, String number, String begintime,
                        String endtime, String agencies, int state) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.begintime = begintime;
        this.endtime = endtime;
        this.agencies = agencies;
        this.state = state;
        this.token = "";
        this.jwt = "";
    }

    @Generated(hash = 634971083)
    public IdentityCard(Long id, String name, String number, String begintime,
                        String endtime, String agencies, int state, String token, String jwt) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.begintime = begintime;
        this.endtime = endtime;
        this.agencies = agencies;
        this.state = state;
        this.token = token;
        this.jwt = jwt;
    }

    @Generated(hash = 1367070082)
    public IdentityCard() {
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

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
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

    public String getJwt() {
        return this.jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
