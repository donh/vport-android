package com.a21vianet.wallet.vport.library.commom.crypto.bean;

import java.util.List;

/**
 * Created by wang.rongqiang on 2017/6/5.
 */

public class BitcoinKey {
    private String privKey;
    private String pubKey;
    private List<String> works;
    private String phone;
    private String IDnumber;

    public BitcoinKey() {
    }

    public BitcoinKey(String rawPrivKey, String privKey, String pubKey,
                      List<String> works) {
        this.privKey = privKey;
        this.pubKey = pubKey;
        this.works = works;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setIDnumber(String IDnumber) {
        this.IDnumber = IDnumber;
    }

    public String getPhone() {
        return phone;
    }

    public String getIDnumber() {
        return IDnumber;
    }

    public String getPrivKey() {
        return privKey;
    }

    public String getPubKey() {
        return pubKey;
    }

    public List<String> getWorks() {
        return works;
    }
}
