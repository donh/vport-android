package com.a21vianet.wallet.vport.library.constant;

import com.a21vianet.wallet.vport.library.BaseApplication;
import com.littlesparkle.growler.core.utility.PrefUtility;

/**
 * Created by wang.rongqiang on 2017/6/12.
 */

public class SysConstant {
    private static final String APP_HRAD_IMAGE_URL_HASH = "app_hardimage_url_hash";

    public static final String IS_INIT_TABLE = "isinittable";

    /**
     * 将头像Hash 存放到本地
     *
     * @param hash
     */
    public static void setHradImageUrlHash(String hash) {
        PrefUtility.setString(BaseApplication.getContext(), APP_HRAD_IMAGE_URL_HASH, hash);
    }

    /**
     * 获取本地 头像 Hash 没有则为 “”
     *
     * @return
     */
    public static String getHradImageUrlHash() {
        return PrefUtility.getString(BaseApplication.getContext(), APP_HRAD_IMAGE_URL_HASH, "");
    }

    /**
     * 清除本地头像 Hash
     */
    public static void clearHradImageUrlHash() {
        PrefUtility.delete(BaseApplication.getContext(), APP_HRAD_IMAGE_URL_HASH);
    }
}
