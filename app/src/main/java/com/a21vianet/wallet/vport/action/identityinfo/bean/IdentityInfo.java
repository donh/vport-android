package com.a21vianet.wallet.vport.action.identityinfo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wang.rongqiang on 2017/6/19.
 */

public class IdentityInfo implements Parcelable {
    private int state;

    private String name;
    private String id;
    private String beginTime;
    private String endTime;
    private String agencies;

    public IdentityInfo() {
    }

    public IdentityInfo(int state, String name, String id, String beginTime, String endTime,
                        String agencies) {
        this.state = state;
        this.name = name;
        this.id = id;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.agencies = agencies;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAgencies() {
        return agencies;
    }

    public void setAgencies(String agencies) {
        this.agencies = agencies;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.state);
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.beginTime);
        dest.writeString(this.endTime);
        dest.writeString(this.agencies);
    }

    protected IdentityInfo(Parcel in) {
        this.state = in.readInt();
        this.name = in.readString();
        this.id = in.readString();
        this.beginTime = in.readString();
        this.endTime = in.readString();
        this.agencies = in.readString();
    }

    public static final Parcelable.Creator<IdentityInfo> CREATOR = new Parcelable
            .Creator<IdentityInfo>() {
        @Override
        public IdentityInfo createFromParcel(Parcel source) {
            return new IdentityInfo(source);
        }

        @Override
        public IdentityInfo[] newArray(int size) {
            return new IdentityInfo[size];
        }
    };
}
