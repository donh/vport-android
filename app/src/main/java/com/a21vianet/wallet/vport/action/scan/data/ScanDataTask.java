package com.a21vianet.wallet.vport.action.scan.data;


import com.a21vianet.wallet.vport.action.mian.MainActivity;
import com.a21vianet.wallet.vport.library.commom.crypto.CryptoManager;
import com.a21vianet.wallet.vport.library.commom.crypto.NoDecryptException;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.Contract;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.JWTBean;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.JWTPayload;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.LoginTokenContext;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.UserLoginTokenContext;

public class ScanDataTask {

    public static final String SUB_LOGIN_TOKEN_SIGNED = "login token signed";
    public static final String SUB_AUTHORIZATION_TOKEN_SIGNED = "authorization token signed";

    private String scope = "";

    public JWTBean<UserLoginTokenContext> getJWTBeanFromSeverJWTBean(JWTBean<LoginTokenContext> severJWTBean) {
        Contract contract = new Contract();
        contract.get();

        JWTBean<UserLoginTokenContext> userLoginTokenContextJWTBean = new JWTBean<>();
        userLoginTokenContextJWTBean.payload = new JWTPayload<>();
        userLoginTokenContextJWTBean.payload.iss = severJWTBean.payload.aud;
        userLoginTokenContextJWTBean.payload.aud = severJWTBean.payload.iss;
        userLoginTokenContextJWTBean.payload.iat = (int) (System.currentTimeMillis() / 1000);
        userLoginTokenContextJWTBean.payload.exp = severJWTBean.payload.exp;
        switch (severJWTBean.payload.sub) {
            case MainActivity.SUB_LOGIN_TOKEN:
                scope = severJWTBean.payload.context.scope;
                userLoginTokenContextJWTBean.payload.sub = SUB_LOGIN_TOKEN_SIGNED;
                break;
            case MainActivity.SUB_AUTHORIZATION_TOKEN:
                scope = "";
                userLoginTokenContextJWTBean.payload.sub = SUB_AUTHORIZATION_TOKEN_SIGNED;
                break;
        }
        try {
            userLoginTokenContextJWTBean.payload.context =
                    new UserLoginTokenContext(scope
                            , severJWTBean.payload.context.token
                            , contract.getProxy()
                            , CryptoManager.getInstance().getCoinKey().getPubKey());
        } catch (NoDecryptException e) {
            e.printStackTrace();
        }
        return userLoginTokenContextJWTBean;
    }

}
