package com.a21vianet.wallet.vport.dao.bean;

import com.a21vianet.wallet.vport.dao.entity.OperatingData;

import java.util.List;

/**
 * Created by wang.rongqiang on 2017/6/27.
 */

public class OperatingDataPage {
    private List<OperatingData> mOperatingDatas;
    private int size = 0;
    private int offset;

    public List<OperatingData> getOperatingDatas() {
        return mOperatingDatas;
    }

    public void setOperatingDatas(List<OperatingData> operatingDatas) {
        mOperatingDatas = operatingDatas;
        if (mOperatingDatas != null) {
            this.size = mOperatingDatas.size();
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
