package com.a21vianet.wallet.vport.library.event;


import com.a21vianet.wallet.vport.library.commom.http.ipfs.bean.UserInfoIPFS;

/**
 * Created by wang.rongqiang on 2017/6/13.
 */

public class ChangeUserInfoEvent {
    public final UserInfoIPFS mInfoIPFS;

    public ChangeUserInfoEvent(UserInfoIPFS infoIPFS) {
        mInfoIPFS = infoIPFS;
    }
}
