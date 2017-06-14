package com.a21vianet.wallet.vport.library.commom.crypto.bean;

import com.a21vianet.wallet.app.WalletApplication;
import com.google.gson.Gson;
import com.littlesparkle.growler.core.utility.PrefUtility;

/**
 * Created by wang.rongqiang on 2017/6/9.
 * 保存公开的智能合约信息
 */

public class Contract {
    private String proxy;
    private String controller;
    private String recover;
    private String ipfsHex;

    private static final String SAVE_TAG = "contract_info_save_tag";

    public Contract() {
    }

    public Contract(String proxy, String controller, String recover) {
        this.proxy = proxy;
        this.controller = controller;
        this.recover = recover;
    }

    public Contract(String proxy, String controller, String recover, String ipfsHex) {
        this.proxy = proxy;
        this.controller = controller;
        this.recover = recover;
        this.ipfsHex = ipfsHex;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getRecover() {
        return recover;
    }

    public void setRecover(String recover) {
        this.recover = recover;
    }

    public String getIpfsHex() {
        return ipfsHex;
    }

    public void setIpfsHex(String ipfsHex) {
        this.ipfsHex = ipfsHex;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "proxy='" + proxy + '\'' +
                ", controller='" + controller + '\'' +
                ", recover='" + recover + '\'' +
                ", ipfsHex='" + ipfsHex + '\'' +
                '}';
    }

    /**
     * 保存智能合约地址
     */
    public void save() {
        PrefUtility.setString(WalletApplication.getContext(), SAVE_TAG, new Gson().toJson(this));
    }

    /**
     * 获取智能合约地址
     */
    public void get() {
        Gson gson = new Gson();
        String string = PrefUtility.getString(WalletApplication.getContext(), SAVE_TAG, "");
        if (string.equals("")) {
            return;
        }
        Contract contract = gson.fromJson(string, this.getClass());
        this.setController(contract.getController());
        this.setRecover(contract.getRecover());
        this.setProxy(contract.getProxy());
        this.setIpfsHex(contract.getIpfsHex());
    }

    /**
     * 清除智能合约地址
     */
    public void clear() {
        Gson gson = new Gson();
        Contract contract = new Contract();
        PrefUtility.setString(WalletApplication.getContext(), SAVE_TAG, gson.toJson(contract));
        this.setController(contract.getController());
        this.setRecover(contract.getRecover());
        this.setProxy(contract.getProxy());
        this.setIpfsHex(contract.getIpfsHex());
    }

    public boolean isEmpty() {
        get();
        if (proxy == null || controller == null || recover == null) {
            return true;
        } else {
            return false;
        }
    }

}
