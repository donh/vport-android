package com.a21vianet.wallet.vport.action.identityinfo.task;

import android.support.annotation.ColorInt;
import android.support.v4.util.Pair;

/**
 * Created by wang.rongqiang on 2017/7/4.
 */

public class IdCardUtiltiy {
    public static Pair<String, Integer> verdictIdState(int i) {
        String staetStr;
        @ColorInt
        int typeColor;
        switch (i) {
            case 1:
                //待认证
                staetStr = "待认证";
                typeColor = 0xFF7d7d7d;
                break;
            case 4:
                //认证中
                staetStr = "认证中";
                typeColor = 0xFF7d7d7d;
                break;
            case 2:
                //认证成功
                staetStr = "认证成功";
                typeColor = 0xFF1b93ef;
                break;
            case 3:
            default:
                //认证失败
                staetStr = "认证失败";
                typeColor = 0xFFeb212e;
        }
        return new Pair<>(staetStr, typeColor);
    }
}
