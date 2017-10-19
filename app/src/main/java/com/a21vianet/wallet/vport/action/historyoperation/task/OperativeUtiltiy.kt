package com.a21vianet.wallet.vport.action.historyoperation.task

import android.support.annotation.ColorInt
import com.a21vianet.wallet.vport.dao.bean.OperationStateEnum

/**
 * Created by wang.rongqiang on 2017/7/4.
 */

fun verdictOperativeState(i: Int): Int {
    @ColorInt
    val typeColor: Int

    when (i) {
        OperationStateEnum.Cancel.state -> {
            typeColor = 0xFF7d7d7d.toInt()
        }
        OperationStateEnum.Error.state -> {
            typeColor = 0xFFeb212e.toInt()
        }
        OperationStateEnum.Success.state -> {
            typeColor = 0xFF1b93ef.toInt()
        }
        else -> {
            typeColor = 0xFF7d7d7d.toInt()
        }
    }
    return typeColor
}
