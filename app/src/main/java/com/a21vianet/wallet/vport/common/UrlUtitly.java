package com.a21vianet.wallet.vport.common;

import android.support.v4.util.Pair;

/**
 * Created by wang.rongqiang on 2017/7/6.
 */

public class UrlUtitly {

    public static Pair<String, String> substringUrl(String path) {
        String replace = path.replace("//", "--");
        int i = replace.lastIndexOf("/");
        if (i == -1) {
            if (path.charAt(path.length() - 1) != '/') {
                path += "/";
            }
            return new Pair(path, " ");
        }
        String prefix = path.substring(0, i + 1);
        String suffix = path.substring(i + 1) + " ";
        return new Pair(prefix, suffix);
    }
}
