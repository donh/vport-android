package com.a21vianet.wallet.vport.action.info.changename

import android.widget.Toast
import com.a21vianet.wallet.vport.R
import com.a21vianet.wallet.vport.biz.CryptoBiz
import com.a21vianet.wallet.vport.http.Api
import com.a21vianet.wallet.vport.library.commom.crypto.bean.Contract
import com.a21vianet.wallet.vport.library.commom.http.ipfs.IPFSRequest
import com.a21vianet.wallet.vport.library.commom.http.ipfs.bean.UserInfoIPFS
import com.google.gson.Gson
import com.littlesparkle.growler.core.ui.activity.BaseTitleBarActivity
import com.littlesparkle.growler.core.ui.mvp.BasePresenter
import com.littlesparkle.growler.core.ui.mvp.BaseView
import kotlinx.android.synthetic.main.activity_personal_change_name.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class PersonalChangeNameActivity : BaseTitleBarActivity<BasePresenter<BaseView>>() {

    override fun getLayoutResId(): Int {
        return R.layout.activity_personal_change_name
    }

    override fun selfTitleResId(): Int {
        return R.string.title_personal_change_name
    }

    override fun initData() {
        super.initData()
    }

    override fun initView() {
        super.initView()
        tv_change.setOnClickListener {
            val nickname = edit_nickname.text.trim().toString()
            update(nickname)
        }

    }

    fun update(nickname: String) {
        showProgress()
        val contract = Contract()
        contract.get()
        IPFSRequest(Api.IPFSWebApi)
                .ipfsGetJson(contract.ipfsHex)
                .map({
                    val userinfo = Gson().fromJson(it, UserInfoIPFS::class.java)
                    userinfo.name = nickname
                    userinfo
                })
                .flatMap { CryptoBiz.signIPFSTx(contract, it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    contract.nickname = nickname
                    contract.save()
                    dismissProgress()
                    finish()
                }, {
                    dismissProgress()
                    Toast.makeText(this@PersonalChangeNameActivity, "保存失败", Toast.LENGTH_LONG).show()
                })
    }
}
