package com.a21vianet.wallet.vport.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wang.rongqiang on 2017/6/27.
 */

@Entity
public class OperatingData {
    @Id
    private int id;

    private String username;
    private String userimg;
    private String appname;
    private String appimg;
    private String appurl;
    private String operationtypeid;
    private String operationtypename;
    private String operationtime;

    @Generated(hash = 1290495007)
    public OperatingData(int id, String username, String userimg, String appname,
            String appimg, String appurl, String operationtypeid,
            String operationtypename, String operationtime) {
        this.id = id;
        this.username = username;
        this.userimg = userimg;
        this.appname = appname;
        this.appimg = appimg;
        this.appurl = appurl;
        this.operationtypeid = operationtypeid;
        this.operationtypename = operationtypename;
        this.operationtime = operationtime;
    }

    @Generated(hash = 1127590806)
    public OperatingData() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimg() {
        return this.userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public String getAppname() {
        return this.appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getAppimg() {
        return this.appimg;
    }

    public void setAppimg(String appimg) {
        this.appimg = appimg;
    }

    public String getAppurl() {
        return this.appurl;
    }

    public void setAppurl(String appurl) {
        this.appurl = appurl;
    }

    public String getOperationtime() {
        return this.operationtime;
    }

    public void setOperationtime(String operationtime) {
        this.operationtime = operationtime;
    }

    public String getOperationtypename() {
        return this.operationtypename;
    }

    public void setOperationtypename(String operationtypename) {
        this.operationtypename = operationtypename;
    }

    public String getOperationtypeid() {
        return this.operationtypeid;
    }

    public void setOperationtypeid(String operationtypeid) {
        this.operationtypeid = operationtypeid;
    }
}
