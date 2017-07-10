package com.a21vianet.wallet.vport.action.mian;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a21vianet.wallet.vport.R;
import com.a21vianet.wallet.vport.action.historyoperation.HistoryOperationActivity;
import com.a21vianet.wallet.vport.action.identityinfo.IdentityInfoActivity;
import com.a21vianet.wallet.vport.action.info.PersonalInfoActivity;
import com.a21vianet.wallet.vport.action.password.PasswordManager;
import com.a21vianet.wallet.vport.action.scan.ScanActivity;
import com.a21vianet.wallet.vport.action.scan.data.ScanDataTask;
import com.a21vianet.wallet.vport.action.setting.SettingActivity;
import com.a21vianet.wallet.vport.common.ActivityStartUtility;
import com.a21vianet.wallet.vport.dao.IdentityCardManager;
import com.a21vianet.wallet.vport.dao.OperatingDataManager;
import com.a21vianet.wallet.vport.dao.bean.IdentityCardState;
import com.a21vianet.wallet.vport.dao.bean.OperationStateEnum;
import com.a21vianet.wallet.vport.dao.bean.OperationTypeEnum;
import com.a21vianet.wallet.vport.dao.entity.IdentityCard;
import com.a21vianet.wallet.vport.dao.entity.OperatingData;
import com.a21vianet.wallet.vport.http.Api;
import com.a21vianet.wallet.vport.library.commom.crypto.CryptoManager;
import com.a21vianet.wallet.vport.library.commom.crypto.NoDecryptException;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.ClaimTokenContext;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.Contract;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.JWTBean;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.LoginTokenContext;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.UserClaimTokenContext;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.UserLoginTokenContext;
import com.a21vianet.wallet.vport.library.commom.crypto.callback.OnFinishedListener;
import com.a21vianet.wallet.vport.library.commom.crypto.callback.OnVerifiedListener;
import com.a21vianet.wallet.vport.library.commom.http.vport.ClaimResponse;
import com.a21vianet.wallet.vport.library.commom.http.vport.LoginResponse;
import com.a21vianet.wallet.vport.library.commom.http.vport.VPortRequest;
import com.a21vianet.wallet.vport.library.constant.SysConstant;
import com.a21vianet.wallet.vport.library.event.ScanResultEvent;
import com.amulyakhare.textdrawable.TextDrawable;
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
    public static final String SUB_CLAIM_TOKEN = "claim token";

    private AlertDialog mAlertDialogLogin;
    private ImageView imageViewCompany;
    private TextView textViewHintDialog;

    private AlertDialog mAlertDialogClaim;
    private ImageView imageViewClaimCompany;
    private TextView textViewClaimHintDialog;

    private String userJWT = "";
    private String userClaimJWT = "";
    private String serverUrl = "";

    private JWTBean<LoginTokenContext> mLoginTokenContextJWTBean = null;
    private JWTBean<ClaimTokenContext> mClaimTokenContextJWTBean = null;

    public void initClaimDialog() {
        RelativeLayout relativeClaimDialog = (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_claim, null);
        mAlertDialogClaim = new AlertDialog.Builder(this).setView(relativeClaimDialog).create();
        imageViewClaimCompany = (ImageView) relativeClaimDialog.findViewById(R.id.imgv_claim_dialog_server);
        textViewClaimHintDialog = (TextView) relativeClaimDialog.findViewById(R.id.tv_claim_dialog_hint);
        AppCompatButton btCancel = (AppCompatButton) relativeClaimDialog.findViewById(R.id.bt_claim_dialog_cancel);
        AppCompatButton btOK = (AppCompatButton) relativeClaimDialog.findViewById(R.id.bt_claim_dialog_ok);
        AppCompatImageButton btClose = (AppCompatImageButton) relativeClaimDialog.findViewById(R.id.imgv_claim_dialog_close);
        ImageView imageViewUser = (ImageView) relativeClaimDialog.findViewById(R.id.imgv_claim_dialog_user);

        Glide.with(this)
                .load(getHeaderUrl())
                .transform(new GlideCircleImage(this))
                .placeholder(R.drawable.icon_header)
                .into(imageViewUser);

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(userClaimJWT)) {
                    new VPortRequest(Api.ClaimApi).claim(new BaseHttpSubscriber<ClaimResponse>() {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            dismissDialog(mAlertDialogClaim);
                        }

                        @Override
                        protected void onSuccess(ClaimResponse claimResponse) {
                            super.onSuccess(claimResponse);
                            dismissDialog(mAlertDialogClaim);

                            Contract contract = new Contract();
                            contract.get();
                            OperatingData operatingData = new OperatingData(contract.getNickname()
                                    , SysConstant.getHradImageUrlHash()
                                    , mClaimTokenContextJWTBean.payload.context.clientName
                                    , ""
                                    , ""
                                    , OperationStateEnum.Success
                                    , "声明成功"
                                    , OperationTypeEnum.Statement
                                    , "");

                            if (claimResponse.result.valid) {
                                Toast.makeText(MainActivity.this, "声明信息提交成功", Toast.LENGTH_SHORT).show();
                                IdentityCard identityCard = IdentityCardManager.get(0);
                                identityCard.setState(IdentityCardState.PENDING.state);
                                IdentityCardManager.update(identityCard);
                            } else {
                                Toast.makeText(MainActivity.this, "声明信息提交失败", Toast.LENGTH_SHORT).show();
                                operatingData.setOperationState(OperationStateEnum.Error.state);
                                operatingData.setOperationmsg("声明失败");
                            }
                            OperatingDataManager.insert(operatingData);
                        }
                    }, userClaimJWT);
                } else Toast.makeText(MainActivity.this, "数据错误，请稍候重试", Toast.LENGTH_SHORT).show();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog(mAlertDialogClaim);
                insertCancelClaimToDb();
            }
        });

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog(mAlertDialogClaim);
                insertCancelClaimToDb();
            }
        });

    }

    public void initLoginDialog() {
        final RelativeLayout relativeLoginDialog = (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_login, null);
        mAlertDialogLogin = new AlertDialog.Builder(this).setView(relativeLoginDialog).create();
        imageViewCompany = (ImageView) relativeLoginDialog.findViewById(R.id.imgv_login_dialog_server);
        textViewHintDialog = (TextView) relativeLoginDialog.findViewById(R.id.tv_login_dialog_hint);
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
                insertCancelLoginToDb();
                dismissDialog(mAlertDialogLogin);
            }
        });
        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(userJWT) && !TextUtils.isEmpty(serverUrl)) {
                    new VPortRequest(Api.ClaimApi).login(new BaseHttpSubscriber<LoginResponse>() {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            dismissDialog(mAlertDialogLogin);
                            Toast.makeText(MainActivity.this, "登录失败，请稍候重试", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        protected void onSuccess(LoginResponse loginResponse) {
                            super.onSuccess(loginResponse);
                            dismissDialog(mAlertDialogLogin);

                            Contract contract = new Contract();
                            contract.get();
                            OperatingData operatingData = new OperatingData(contract.getNickname()
                                    , SysConstant.getHradImageUrlHash()
                                    , mLoginTokenContextJWTBean.payload.context.clientName
                                    , ""
                                    , mLoginTokenContextJWTBean.payload.context.serverURL
                                    , OperationStateEnum.Success
                                    , "登录成功"
                                    , OperationTypeEnum.Login
                                    , "");
                            if (loginResponse.result.valid) {
                                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            } else {
                                operatingData.setOperationState(OperationStateEnum.Error.state);
                                operatingData.setOperationmsg("登录失败");
                                Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                            }
                            OperatingDataManager.insert(operatingData);

                        }
                    }, userJWT);
                } else Toast.makeText(MainActivity.this, "数据错误，请稍候重试", Toast.LENGTH_SHORT).show();
            }
        });
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertCancelLoginToDb();
                dismissDialog(mAlertDialogLogin);
            }
        });
    }

    public void insertCancelClaimToDb() {
        Contract contract = new Contract();
        contract.get();
        OperatingData operatingData = new OperatingData(contract.getNickname()
                , SysConstant.getHradImageUrlHash()
                , mClaimTokenContextJWTBean.payload.context.clientName
                , ""
                , ""
                , OperationStateEnum.Cancel
                , "取消声明"
                , OperationTypeEnum.Statement
                , "");
        OperatingDataManager.insert(operatingData);
    }

    public void insertCancelLoginToDb() {
        Contract contract = new Contract();
        contract.get();
        OperatingData operatingData = new OperatingData(contract.getNickname()
                , SysConstant.getHradImageUrlHash()
                , mLoginTokenContextJWTBean.payload.context.clientName
                , ""
                , mLoginTokenContextJWTBean.payload.context.serverURL
                , OperationStateEnum.Cancel
                , "取消登录"
                , OperationTypeEnum.Login
                , "");
        OperatingDataManager.insert(operatingData);
    }

    public TextDrawable getTextDrawable(String clientName) {
        if (!TextUtils.isEmpty(clientName)) {
            return TextDrawable.builder().buildRound(clientName.substring(0, 1), Color.parseColor("#06bebd"));
        } else
            return TextDrawable.builder().buildRound("V", Color.parseColor("#06bebd"));
    }

    private void dismissDialog(AlertDialog alertDialog) {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public String getHeaderUrl() {
        return Api.IPFSWebApi + SysConstant.getHradImageUrlHash();
    }

    private boolean claimable() {
        IdentityCard identityCard = IdentityCardManager.get(0);
        return identityCard.getState() == IdentityCardState.NONE.state;
    }

    @Override

    protected void initData() {
        checkEnCode();
        initLoginDialog();
        initClaimDialog();
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
                ActivityStartUtility.startScanActivity(this, new Intent(this, ScanActivity.class), REQUEST_CODE_SCAN);
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
                final JWTBean<Object> jwtBean = gson.fromJson(s, new TypeToken<JWTBean<Object>>() {
                }.getType());
                Log.d("MainActivity", "jwtBean:" + jwtBean);
                switch (jwtBean.payload.sub) {
                    case SUB_LOGIN_TOKEN:
                        final JWTBean<LoginTokenContext> loginTokenContextJWTBean = gson.fromJson(s, new TypeToken<JWTBean<LoginTokenContext>>() {
                        }.getType());
                        mLoginTokenContextJWTBean = loginTokenContextJWTBean;
                        Observable.create(new Observable.OnSubscribe<JWTBean<LoginTokenContext>>() {

                            @Override
                            public void call(Subscriber<? super JWTBean<LoginTokenContext>> subscriber) {
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
                                                                                new ScanDataTask().getLoginJWTBeanFromSeverJWTBean(loginTokenContextJWTBean);
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
                                                            , gson.toJson(userLoginTokenContextJWTBean.payload)
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
                                        imageViewCompany.setImageDrawable(getTextDrawable(mLoginTokenContextJWTBean.payload.context.clientName));
                                        textViewHintDialog.setText("请确认是否登录" + mLoginTokenContextJWTBean.payload.context.clientName);
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
                    case SUB_CLAIM_TOKEN:
                        final JWTBean<ClaimTokenContext> claimTokenContextJWTBean = gson.fromJson(s, new TypeToken<JWTBean<ClaimTokenContext>>() {
                        }.getType());
                        mClaimTokenContextJWTBean = claimTokenContextJWTBean;
                        Log.d("MainActivity", "claimTokenContextJWTBean:" + claimTokenContextJWTBean);
                        Observable.create(new Observable.OnSubscribe<JWTBean<ClaimTokenContext>>() {
                            @Override
                            public void call(Subscriber<? super JWTBean<ClaimTokenContext>> subscriber) {
                                subscriber.onNext(claimTokenContextJWTBean);
                                subscriber.onCompleted();
                            }
                        })
                                .observeOn(AndroidSchedulers.mainThread())
                                .flatMap(new Func1<JWTBean<ClaimTokenContext>, Observable<JWTBean<UserClaimTokenContext>>>() {
                                    @Override
                                    public Observable<JWTBean<UserClaimTokenContext>> call(final JWTBean<ClaimTokenContext> claimTokenContextJWTBean) {
                                        return Observable.create(new Observable.OnSubscribe<JWTBean<UserClaimTokenContext>>() {
                                            @Override
                                            public void call(final Subscriber<? super JWTBean<UserClaimTokenContext>> subscriber) {
                                                try {
                                                    CryptoManager.getInstance().verifyJWTToken(MainActivity.this
                                                            , claimTokenContextJWTBean.payload.context.serverPublicKey
                                                            , scanResultEvent.result
                                                            , new OnVerifiedListener() {
                                                                @Override
                                                                public void onVerified(boolean isValid) {
                                                                    if (isValid) {
                                                                        if (IdentityCardManager.exists()) {
                                                                            JWTBean<UserClaimTokenContext> userClaimTokenContextJWTBean =
                                                                                    new ScanDataTask().getClaimJWTBeanFromSeverJWTBean(claimTokenContextJWTBean);
                                                                            subscriber.onNext(userClaimTokenContextJWTBean);
                                                                            subscriber.onCompleted();
                                                                        } else {
                                                                            subscriber.onError(new Throwable("暂无声明信息"));
                                                                        }

                                                                    } else {
                                                                        subscriber.onError(new Throwable("验证签名失败，请稍候重试"));
                                                                    }
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
                                .flatMap(new Func1<JWTBean<UserClaimTokenContext>, Observable<String>>() {
                                    @Override
                                    public Observable<String> call(final JWTBean<UserClaimTokenContext> userClaimTokenContextJWTBean) {
                                        return Observable.create(new Observable.OnSubscribe<String>() {
                                            @Override
                                            public void call(final Subscriber<? super String> subscriber) {
                                                try {
                                                    CryptoManager.getInstance().signJWTToken(MainActivity.this
                                                            , gson.toJson(userClaimTokenContextJWTBean.payload)
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
                                        imageViewClaimCompany.setImageDrawable(getTextDrawable(mClaimTokenContextJWTBean.payload.context.clientName));
                                        if (claimable()) {
                                            mAlertDialogClaim.show();
                                        }
                                        dismissProgress();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        dismissProgress();
                                        if (!TextUtils.isEmpty(e.getMessage())) {
                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onNext(String s) {
                                        userClaimJWT = s;
                                    }
                                });
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
