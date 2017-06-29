package com.a21vianet.wallet.vport.action.mian;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.a21vianet.wallet.vport.R;
import com.a21vianet.wallet.vport.action.historyoperation.HistoryOperationActivity;
import com.a21vianet.wallet.vport.action.identityinfo.IdentityInfoActivity;
import com.a21vianet.wallet.vport.action.info.PersonalInfoActivity;
import com.a21vianet.wallet.vport.action.password.PasswordManager;
import com.a21vianet.wallet.vport.action.scan.ScanActivity;
import com.a21vianet.wallet.vport.action.scan.data.ScanDataTask;
import com.a21vianet.wallet.vport.action.setting.SettingActivity;
import com.a21vianet.wallet.vport.http.Api;
import com.a21vianet.wallet.vport.library.commom.crypto.CryptoManager;
import com.a21vianet.wallet.vport.library.commom.crypto.NoDecryptException;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.JWTBean;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.LoginTokenContext;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.UserLoginTokenContext;
import com.a21vianet.wallet.vport.library.commom.crypto.callback.OnFinishedListener;
import com.a21vianet.wallet.vport.library.commom.crypto.callback.OnVerifiedListener;
import com.a21vianet.wallet.vport.library.commom.http.vport.LoginResponse;
import com.a21vianet.wallet.vport.library.commom.http.vport.VPortRequest;
import com.a21vianet.wallet.vport.library.constant.SysConstant;
import com.a21vianet.wallet.vport.library.event.ScanResultEvent;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.littlesparkle.growler.core.am.ActivityUtility;
import com.littlesparkle.growler.core.http.BaseHttpSubscriber;
import com.littlesparkle.growler.core.ui.activity.BaseMainActivity;
import com.littlesparkle.growler.core.ui.view.GlideCircleImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.GeneralSecurityException;

import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.github.orangegangsters.lollipin.lib.managers.AppLockActivity.EXT_DATA;

public class MainActivity extends BaseMainActivity {

    private static final int REQUEST_ENTER_PASSWORD = 0x1;
    public static final int REQUEST_CODE_SCAN = 8080;
    public static final String SUB_LOGIN_TOKEN = "login token";
    public static final String SUB_AUTHORIZATION_TOKEN = "authorization token";

    private AlertDialog mAlertDialogLogin;
    private ImageView imageViewCompany;
    private String userJWT = "";
    private String serverJWT = "";
    private String serverUrl = "";

    private JWTBean<LoginTokenContext> mLoginTokenContextJWTBean = null;
    private JWTBean<UserLoginTokenContext> mUserLoginTokenContextJWTBean = null;

    public void initLoginDialog() {
        RelativeLayout relativeLoginDialog = (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_login, null);
        mAlertDialogLogin = new AlertDialog.Builder(this).setView(relativeLoginDialog).create();
        imageViewCompany = (ImageView) relativeLoginDialog.findViewById(R.id.imgv_login_dialog_server);
        AppCompatButton btCancel = (AppCompatButton) relativeLoginDialog.findViewById(R.id.bt_login_dialog_cancel);
        AppCompatButton btOK = (AppCompatButton) relativeLoginDialog.findViewById(R.id.bt_login_dialog_ok);
        AppCompatImageButton btClose = (AppCompatImageButton) relativeLoginDialog.findViewById(R.id.imgv_login_dialog_close);
        ImageView imageViewUser = (ImageView) relativeLoginDialog.findViewById(R.id.imgv_login_dialog_user);

        Glide.with(this)
                .load(getHeaderUrl())
                .transform(new GlideCircleImage(this))
                .placeholder(R.drawable.icon_header)
                .into(imageViewUser);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(userJWT) && !TextUtils.isEmpty(serverUrl)) {
                    new VPortRequest(serverUrl + "/").login(new BaseHttpSubscriber<LoginResponse>() {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            dismissDialog();
                            Toast.makeText(MainActivity.this, "登录失败，请稍候重试", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        protected void onSuccess(LoginResponse loginResponse) {
                            super.onSuccess(loginResponse);
                        }
                    }, userJWT);
                } else Toast.makeText(MainActivity.this, "数据错误，请稍候重试", Toast.LENGTH_SHORT).show();
            }
        });
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
    }

    public void dismissDialog() {
        if (mAlertDialogLogin != null && mAlertDialogLogin.isShowing()) {
            mAlertDialogLogin.dismiss();
        }
    }

    public String getHeaderUrl() {
        return Api.IPFSWebApi + SysConstant.getHradImageUrlHash();
    }

    @Override
    protected void initData() {
        checkEnCode();
        initLoginDialog();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarCompat.translucentStatusBar(this);
    }

    @OnClick({R.id.relative_visiting, R.id.relative_identity, R.id.relative_operation, R.id
            .imgv_main_scan, R.id.imgv_main_setting})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.relative_visiting:
                intent = new Intent(this, PersonalInfoActivity.class);
                break;
            case R.id.relative_identity:
                intent = new Intent(this, IdentityInfoActivity.class);
                break;
            case R.id.relative_operation:
                intent = new Intent(this, HistoryOperationActivity.class);
                break;
            case R.id.imgv_main_scan:
                startActivityForResult(new Intent(this, ScanActivity.class), REQUEST_CODE_SCAN);
                break;
            case R.id.imgv_main_setting:
                intent = new Intent(this, SettingActivity.class);
                break;
        }
        if (intent != null) {
            ActivityUtility.startActivityWithAnim(this, intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String stringExtra = null;
        if (data != null) {
            stringExtra = data.getStringExtra(EXT_DATA);
        }
        switch (requestCode) {
            case REQUEST_ENTER_PASSWORD:
                if (stringExtra != null) {
                    try {
                        CryptoManager.getInstance().decrypt(stringExtra);
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }
                } else {
                    finish();
                }
                break;
        }
    }

    /**
     * 检查私钥是否已解密
     */
    private void checkEnCode() {
        if (CryptoManager.getInstance().isEncrypted()) {
            PasswordManager.startCheckPassword(this, REQUEST_ENTER_PASSWORD);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScanResultGet(final ScanResultEvent scanResultEvent) {
        showProgress();
        CryptoManager.getInstance().decodeJWTToken(this, scanResultEvent.result, new OnFinishedListener() {
            @Override
            public void onFinished(final String s) {
                final Gson gson = new Gson();
                JWTBean<Object> jwtBean = gson.fromJson(s, new TypeToken<JWTBean<Object>>() {
                }.getType());
                switch (jwtBean.payload.sub) {
                    case SUB_LOGIN_TOKEN:
                        final JWTBean<LoginTokenContext> loginTokenContextJWTBean = gson.fromJson(s, new TypeToken<JWTBean<LoginTokenContext>>() {
                        }.getType());
                        mLoginTokenContextJWTBean = loginTokenContextJWTBean;
                        Observable.create(new Observable.OnSubscribe<JWTBean<LoginTokenContext>>() {

                            @Override
                            public void call(Subscriber<? super JWTBean<LoginTokenContext>> subscriber) {
                                serverJWT = scanResultEvent.result;
                                serverUrl = loginTokenContextJWTBean.payload.context.serverURL;
                                subscriber.onNext(loginTokenContextJWTBean);
                                subscriber.onCompleted();
                            }
                        })
                                .observeOn(AndroidSchedulers.mainThread())
                                .flatMap(new Func1<JWTBean<LoginTokenContext>, Observable<JWTBean<UserLoginTokenContext>>>() {

                                    @Override
                                    public Observable<JWTBean<UserLoginTokenContext>> call(final JWTBean<LoginTokenContext> jwtBean) {
                                        return Observable.create(new Observable.OnSubscribe<JWTBean<UserLoginTokenContext>>() {
                                            @Override
                                            public void call(final Subscriber<? super JWTBean<UserLoginTokenContext>> subscriber) {
                                                try {
                                                    CryptoManager.getInstance().verifyJWTToken(MainActivity.this
                                                            , jwtBean.payload.context.serverPublicKey
                                                            , scanResultEvent.result
                                                            , new OnVerifiedListener() {
                                                                @Override
                                                                public void onVerified(boolean isValid) {
                                                                    if (isValid) {
                                                                        JWTBean<UserLoginTokenContext> userLoginTokenContextJWTBean =
                                                                                new ScanDataTask().getJWTBeanFromSeverJWTBean(loginTokenContextJWTBean);
                                                                        mUserLoginTokenContextJWTBean = userLoginTokenContextJWTBean;
                                                                        subscriber.onNext(userLoginTokenContextJWTBean);
                                                                        subscriber.onCompleted();
                                                                    } else
                                                                        subscriber.onError(new Throwable("验证签名失败，请稍候重试"));
                                                                }
                                                            });
                                                } catch (NoDecryptException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .flatMap(new Func1<JWTBean<UserLoginTokenContext>, Observable<String>>() {

                                    @Override
                                    public Observable<String> call(final JWTBean<UserLoginTokenContext> userLoginTokenContextJWTBean) {
                                        return Observable.create(new Observable.OnSubscribe<String>() {
                                            @Override
                                            public void call(final Subscriber<? super String> subscriber) {
                                                try {
                                                    CryptoManager.getInstance().signJWTToken(MainActivity.this
                                                            , gson.toJson(userLoginTokenContextJWTBean)
                                                            , new OnFinishedListener() {
                                                                @Override
                                                                public void onFinished(String s) {
                                                                    subscriber.onNext(s);
                                                                    subscriber.onCompleted();
                                                                }

                                                                @Override
                                                                public void onError(Exception e) {
                                                                    subscriber.onError(e);
                                                                }
                                                            });
                                                } catch (NoDecryptException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<String>() {
                                    @Override
                                    public void onCompleted() {
                                        mAlertDialogLogin.show();
                                        dismissProgress();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (!TextUtils.isEmpty(e.getMessage())) {
                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                        dismissProgress();
                                    }

                                    @Override
                                    public void onNext(String s) {
                                        userJWT = s;
                                    }
                                });
                        break;
                    case SUB_AUTHORIZATION_TOKEN:
                        break;
                }
            }

            @Override
            public void onError(Exception e) {
                dismissProgress();
            }
        });
    }
}
