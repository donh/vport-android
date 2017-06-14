package com.a21vianet.wallet.vport.library.constant;

import com.a21vianet.wallet.app.WalletApplication;
import com.littlesparkle.growler.core.utility.PrefUtility;

/**
 * Created by wang.rongqiang on 2017/6/12.
 */

public class SysConstant {
    private static final String APP_HRAD_IMAGE_URL_HASH = "app_hardimage_url_hash";

    public static void setHradImageUrlHash(String hash) {
        PrefUtility.setString(WalletApplication.getContext(), APP_HRAD_IMAGE_URL_HASH, hash);
    }

    public static String getHradImageUrlHash() {
        return PrefUtility.getString(WalletApplication.getContext(), APP_HRAD_IMAGE_URL_HASH, "");
    }

    public static void clearHradImageUrlHash() {
        PrefUtility.delete(WalletApplication.getContext(), APP_HRAD_IMAGE_URL_HASH);
    }

    public static String getIPFSIP() {
        return "http://58.83.219.152:8080/ipfs/";
    }
}
