package com.a21vianet.wallet.vport.action.historyoperation.bean;

import android.support.annotation.DrawableRes;

import com.a21vianet.wallet.vport.R;


/**
 * Created by wang.rongqiang on 2017/6/2.
 */

public class History {
    @DrawableRes
    private int headimg;
    private String title;
    private String time;
    private String content;
    private String msg;

    public History() {
        this.headimg = R.drawable.icon_logo;
        this.title = "1G6DLbZi1A1s1utakX3mLpbkrqe9HtBozgSDSDDS";
        this.time = "10分钟之前";
        this.content = "CONTRAC";
        this.msg = "YOU SIGNED A TRANSACTION";
    }

    public History(@DrawableRes int headimg, String title, String time, String content) {
        this.headimg = headimg;
        this.title = title;
        this.time = time;
        this.content = content;
    }

    @DrawableRes
    public int getHeadimg() {
        return headimg;
    }

    public void setHeadimg(@DrawableRes int headimg) {
        this.headimg = headimg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
