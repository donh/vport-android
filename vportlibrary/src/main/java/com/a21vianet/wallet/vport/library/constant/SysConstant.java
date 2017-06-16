package com.a21vianet.wallet.vport.library.constant;

import com.a21vianet.wallet.vport.library.BaseApplication;
import com.littlesparkle.growler.core.utility.PrefUtility;

/**
 * Created by wang.rongqiang on 2017/6/12.
 */

public class SysConstant {
    private static final String APP_HRAD_IMAGE_URL_HASH = "app_hardimage_url_hash";

    public static void setHradImageUrlHash(String hash) {
        PrefUtility.setString(BaseApplication.getContext(), APP_HRAD_IMAGE_URL_HASH, hash);
    }

    public static String getHradImageUrlHash() {
        return PrefUtility.getString(BaseApplication.getContext(), APP_HRAD_IMAGE_URL_HASH, "");
    }

    public static void clearHradImageUrlHash() {
        PrefUtility.delete(BaseApplication.getContext(), APP_HRAD_IMAGE_URL_HASH);
    }

    public static String getIPFSIP() {
        return "http://58.83.219.152:8080/ipfs/";
    }
}
