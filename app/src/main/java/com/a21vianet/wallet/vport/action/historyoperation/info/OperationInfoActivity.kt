package com.a21vianet.wallet.vport.action.historyoperation.info

import android.graphics.drawable.GradientDrawable
import android.support.annotation.ColorInt
import com.a21vianet.wallet.vport.R
import com.a21vianet.wallet.vport.dao.OperatingDataManager
import com.a21vianet.wallet.vport.dao.entity.OperatingData
import com.a21vianet.wallet.vport.http.Api
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.bumptech.glide.Glide
import com.littlesparkle.growler.core.ui.activity.BaseTitleBarActivity
import com.littlesparkle.growler.core.ui.mvp.BasePresenter
import com.littlesparkle.growler.core.ui.mvp.BaseView
import com.littlesparkle.growler.core.ui.view.GlideCircleImage
import com.littlesparkle.growler.core.utility.DensityUtility
import kotlinx.android.synthetic.main.activity_operation_info.*
import kotlinx.android.synthetic.main.activity_perfect_identity_info.*
import qiu.niorgai.StatusBarCompat
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.text.SimpleDateFormat


class OperationInfoActivity : BaseTitleBarActivity<BasePresenter<BaseView>>() {

    companion object {
        @JvmStatic
        val EXT_IDENTITY_ID = "identity_id"
    }

    val strokeWidth by lazy {
        DensityUtility.dp2px(this, 1f)
    }
    val roundRadius by lazy {
        DensityUtility.dp2px(this, 4f)
    }

    val mSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    var mIdentityId = -1L

    override fun initData() {
        super.initData()
        mIdentityId = intent.getLongExtra(EXT_IDENTITY_ID, -1)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_operation_info
    }

    override fun selfTitleResId(): Int {
        return 0
    }

    override fun initView() {
        super.initView()
        StatusBarCompat.setStatusBarColor(this, resources.getColor(R.color.colorPrimary))
        Observable
                .create(Observable.OnSubscribe<OperatingData> {
                    val operatingData = OperatingDataManager.get(mIdentityId)
                    it.onNext(operatingData)
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    title_bar_text.text = it.operationtype.name
                    tv_accredit_name.text = it.appname
                    tv_accredit_login_name.text = it.username
                    tv_accredit_login_url_name.text = it.appname
                    tv_accredit_login_url_path.text = it.appurl
                    tv_accredit_login_time.text = mSimpleDateFormat.format(it.operationtime)

                    @ColorInt
                    val typeColor: Int

                    if (it.operationmsg.indexOf("失败") != -1) {
                        //待认证
                        typeColor = 0xFFeb212e.toInt()
                    } else if (it.operationmsg.indexOf("成功") != -1) {
                        //认证成功
                        typeColor = 0xFF1b93ef.toInt()
                    } else {
                        //认证失败
                        typeColor = 0xFF7d7d7d.toInt()
                    }

                    val gradientDrawableType = GradientDrawable()
                    gradientDrawableType.cornerRadius = roundRadius.toFloat()
                    gradientDrawableType.setStroke(strokeWidth, typeColor)
                    tv_accredit_state.text = it.operationmsg
                    tv_accredit_state.setTextColor(typeColor)
                    tv_accredit_state.background = gradientDrawableType

                    Glide.with(this).load(Api.IPFSWebApi + it.userimg)
                            .transform(GlideCircleImage(this))
                            .into(imgv_shared_user_header)
                    if (it.appimg == null || it.appimg.equals("")) {
                        val generator = ColorGenerator.MATERIAL
                        var color = generator.getRandomColor()

                        var builder = TextDrawable.builder()
                                .buildRoundRect(it.appname[0].toString(), color,200)

                        imgv_shared_app_header.setImageDrawable(builder)
                    } else {
                        Glide.with(this).load(Api.IPFSWebApi + it.appimg)
                                .transform(GlideCircleImage(this))
                                .into(imgv_shared_app_header)
                    }
                }, {
                    it.printStackTrace()
                })
    }

}
