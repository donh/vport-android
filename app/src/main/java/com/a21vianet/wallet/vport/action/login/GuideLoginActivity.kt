package com.a21vianet.wallet.vport.action.login

import android.content.Intent
import android.view.View
import android.widget.Toast
import com.a21vianet.wallet.vport.action.mian.MainActivity
import com.a21vianet.wallet.vport.R
import com.a21vianet.wallet.vport.action.password.HintPasswordActivity
import com.a21vianet.wallet.vport.library.commom.crypto.CryptoManager
import com.a21vianet.wallet.vport.library.commom.crypto.bean.Contract
import com.littlesparkle.growler.core.ui.activity.BaseActivity
import com.littlesparkle.growler.core.ui.mvp.BasePresenter
import com.littlesparkle.growler.core.ui.mvp.BaseView
import kotlinx.android.synthetic.main.activity_guide_login.*

class GuideLoginActivity : BaseActivity<BasePresenter<BaseView>>() {

    override fun initData() {
        val contract = Contract()
        if (CryptoManager.getInstance().isExistsKey && !contract.isEmpty) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_guide_login
    }

    fun onViewClicked(view: View) {
        if (!edit_nickname.text.toString().trim().equals("")) {
            val contract = Contract()
            contract.get()
            contract.nickname = edit_nickname.text.toString().trim()
            contract.save()
            HintPasswordActivity.startActivity(this)
        } else {
            Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show()
        }
    }
}
