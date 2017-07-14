package com.a21vianet.wallet.vport.http;

/**
 * Created by wang.rongqiang on 2017/6/16.
 */

public class Api {
    /**
     * VChain 区块链 Api 地址
     */
    public static final String vChainApi = "http://58.83.219.133:9487/";

    /**
     * 用户代理注册 服务器地址
     */
    public static final String vChainServiceApi = "http://58.83.219.147:8000/";

    /**
     * 注册用户代理打钱 服务器地址
     */
    public static final String vChainFarmApi = "http://54.250.155.8:8091/";
    public static final String vChainGetColor1Api = "http://58.83.219.136:2222/";

    /**
     * IPFS 服务器地址
     */
    public static final String IPFSApi = "http://58.83.219.152:5001/";

    /**
     * IPFS Web 服务器地址
     */
    public static final String IPFSWebApi = "http://58.83.219.152:8080/ipfs/";


    /**
     * 声明 服务器地址
     */
    public static final String ClaimApi = " http://58.83.219.147:8000/api/v1/";

    public static final String LoginServerUrl = "http://58.83.219.147:8000/api/v1/login/jwt";

    public static final String ClaimServerUrl = "http://58.83.219.147:8000/api/v1/claims/add";

    public static final String AuthServerUrl = "http://58.83.219.147:8000/api/v1/authorizations/jwt";
}
