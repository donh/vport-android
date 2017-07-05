package com.a21vianet.wallet.vport.action.scan.data;


import com.a21vianet.wallet.vport.library.commom.crypto.CryptoManager;
import com.a21vianet.wallet.vport.library.commom.crypto.NoDecryptException;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.ClaimTokenContext;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.Contract;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.JWTBean;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.JWTPayload;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.LoginTokenContext;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.UserClaimTokenContext;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.UserLoginTokenContext;

public class ScanDataTask {

    public static final String SUB_LOGIN_TOKEN_SIGNED = "login token signed";
    public static final String SUB_AUTHORIZATION_TOKEN_SIGNED = "authorization token signed";
    public static final String SUB_CLAIM_TOKEN_SIGNED = "claim for ID";

    private String scope = "";

    public JWTBean<UserLoginTokenContext> getLoginJWTBeanFromSeverJWTBean(JWTBean<LoginTokenContext> severJWTBean) {
        Contract contract = new Contract();
        contract.get();

        JWTBean<UserLoginTokenContext> userLoginTokenContextJWTBean = new JWTBean<>();
        userLoginTokenContextJWTBean.payload = new JWTPayload<>();
        userLoginTokenContextJWTBean.payload.iss = severJWTBean.payload.aud;
        userLoginTokenContextJWTBean.payload.aud = severJWTBean.payload.iss;
        userLoginTokenContextJWTBean.payload.iat = (int) (System.currentTimeMillis() / 1000);
        userLoginTokenContextJWTBean.payload.exp = severJWTBean.payload.exp;
        userLoginTokenContextJWTBean.payload.sub = SUB_LOGIN_TOKEN_SIGNED;

        scope = severJWTBean.payload.context.scope;

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

    public JWTBean<UserClaimTokenContext> getClaimJWTBeanFromSeverJWTBean(JWTBean<ClaimTokenContext> claimTokenContextJWTBean) {
        Contract contract = new Contract();
        contract.get();

        JWTBean<UserClaimTokenContext> userClaimTokenContextJWTBean = new JWTBean<>();
        userClaimTokenContextJWTBean.payload = new JWTPayload<>();
        userClaimTokenContextJWTBean.payload.iss = claimTokenContextJWTBean.payload.aud;
        userClaimTokenContextJWTBean.payload.aud = claimTokenContextJWTBean.payload.iss;
        userClaimTokenContextJWTBean.payload.iat = (int) (System.currentTimeMillis() / 1000);
        userClaimTokenContextJWTBean.payload.exp = claimTokenContextJWTBean.payload.exp;
        userClaimTokenContextJWTBean.payload.sub = SUB_CLAIM_TOKEN_SIGNED;

//        userClaimTokenContextJWTBean.payload.context = new UserClaimTokenContext();

        return userClaimTokenContextJWTBean;
    }

}
