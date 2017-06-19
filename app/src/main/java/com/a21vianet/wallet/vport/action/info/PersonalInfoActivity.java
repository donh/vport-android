package com.a21vianet.wallet.vport.action.info;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a21vianet.wallet.vport.R;
import com.a21vianet.wallet.vport.library.commom.crypto.CryptoManager;
import com.a21vianet.wallet.vport.library.commom.crypto.NoDecryptException;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.Contract;
import com.a21vianet.wallet.vport.library.constant.SysConstant;
import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.littlesparkle.growler.core.am.ActivityUtility;
import com.littlesparkle.growler.core.ui.activity.BaseTitleBarActivity;
import com.littlesparkle.growler.core.ui.listener.OnPopwindowClickListener;
import com.littlesparkle.growler.core.ui.view.GlideCircleImage;
import com.littlesparkle.growler.core.ui.view.HeadPopWindow;
import com.littlesparkle.growler.core.utility.DensityUtility;

import butterknife.BindView;
import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;


public class PersonalInfoActivity extends BaseTitleBarActivity<PersonalInfoPresenter> implements
        PersonalInfoContract.View, TakePhoto.TakeResultListener, InvokeListener {
    @BindView(R.id.imgv_info_header)
    ImageView imgvInfoHeader;
    @BindView(R.id.user_info_pop_relative_layout)
    RelativeLayout userInfoPopRelativeLayout;
    @BindView(R.id.user_info_tv_for_pop)
    TextView userInfoTvForPop;
    @BindView(R.id.tv_info_name_content)
    TextView mTvInfoNameContent;
    @BindView(R.id.tv_info_vportid_content)
    TextView mTvInfovPortIDContent;
    @BindView(R.id.tv_info_pubkey_content)
    TextView mTvInfoPubkeyContent;


    private HeadPopWindow headPopWindow;
    private InvokeParam invokeParam;
    private TakePhoto takePhoto;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected int selfTitleResId() {
        return R.string.personal_info;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected PersonalInfoPresenter initPresenter() {
        return new PersonalInfoPresenter();
    }

    @OnClick(R.id.relative_info_header)
    public void onViewClicked() {
        initHeadPopWindow();
        headPopWindow.showAsDropDown(userInfoTvForPop);
    }

    @Override
    public void initHeadPopWindow() {
        headPopWindow = new HeadPopWindow(this, userInfoPopRelativeLayout);
        headPopWindow.setWidth(userInfoPopRelativeLayout.getWidth());
        headPopWindow.setHeight(DensityUtility.dp2px(this, 150));
        headPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ActivityUtility.backgroundAlpha(PersonalInfoActivity.this, 1.0f);
            }
        });
        ActivityUtility.backgroundAlpha(this, 0.7f);
        headPopWindow.setOnPopwindowClickListener(new OnPopwindowClickListener() {
            @Override
            public void getPhotoByCamera() {
                mPresenter.getPhotoByCamera();
            }

            @Override
            public void getPhotoByAlbums() {
                mPresenter.getPhotoByAlbums();
            }
        });
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap
                .of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl
                    (this, this));
        }
        return takePhoto;
    }

    @Override
    public void showHardImage(String image) {
        if (image != null && !image.equals("")) {
            Glide.with(this)
                    .load(SysConstant.getIPFSIP() + image)
                    .transform(new GlideCircleImage(this))
                    .placeholder(R.drawable.icon_header)
                    .error(R.drawable.icon_header)
                    .into(imgvInfoHeader);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult
                (requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public void takeSuccess(TResult result) {
        mPresenter.onTakeSuccess(result);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeCancel() {
        Toast.makeText(this, "操作被取消", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTakePhoto().onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getTakePhoto().onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (headPopWindow != null && headPopWindow.isShowing()) {
            headPopWindow.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setViewData() {
        Contract contract = new Contract();
        if (!contract.isEmpty()) {
            mTvInfovPortIDContent.setText(contract.getProxy());
            mTvInfoNameContent.setText(contract.getNickname());
        }

        try {
            mTvInfoPubkeyContent.setText(CryptoManager.getInstance().getCoinKey().getPubKey());
        } catch (NoDecryptException e) {
            e.printStackTrace();
        }

        String hradImageUrlHash = SysConstant.getHradImageUrlHash();
        if (hradImageUrlHash != null && !hradImageUrlHash.equals("")) {
            Glide.with(this)
                    .load(SysConstant.getIPFSIP() + hradImageUrlHash)
                    .transform(new GlideCircleImage(this))
                    .placeholder(R.drawable.icon_header)
                    .error(R.drawable.icon_header)
                    .into(imgvInfoHeader);
        }
    }
}
