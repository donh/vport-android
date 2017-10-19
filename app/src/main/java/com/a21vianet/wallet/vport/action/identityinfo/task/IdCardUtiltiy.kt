package com.a21vianet.wallet.vport.action.identityinfo.task

import android.support.annotation.ColorInt
import android.support.v4.util.Pair
import com.a21vianet.wallet.vport.dao.bean.IdentityCardState

/**
 * Created by wang.rongqiang on 2017/7/4.
 */

fun verdictIdState(i: Int): Pair<String, Int> {
    val staetStr: String
    @ColorInt
    val typeColor: Int
    when (i) {
        IdentityCardState.NONE.state -> {
            //待认证
            staetStr = "待认证"
            typeColor = 0xFF7d7d7d.toInt()
        }
        IdentityCardState.PENDING.state -> {
            //认证中
            staetStr = "认证中"
            typeColor = 0xFF7d7d7d.toInt()
        }
        IdentityCardState.APPROVED.state -> {
            //认证成功
            staetStr = "认证成功"
            typeColor = 0xFF1b93ef.toInt()
        }
        else -> {
            //认证失败
            staetStr = "认证失败"
            typeColor = 0xFFeb212e.toInt()
        }
    }
    return Pair(staetStr, typeColor)
}
