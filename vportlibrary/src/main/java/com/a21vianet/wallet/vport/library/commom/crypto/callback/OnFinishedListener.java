package com.a21vianet.wallet.vport.library.commom.crypto.callback;

import android.util.Log;

/**
 * Created by ben on 2017/6/1.
 */
public abstract class OnFinishedListener {
    public void onFinish(String s) {
        String ErroeTag = "JSERROR.";
        Log.e("=====JSMSG===== ", s);

        if (s.indexOf(ErroeTag) != -1) {
            onError(new Exception(s));
            return;
        }
        String replace = s;
        char c = replace.charAt(0);

        if (String.valueOf(c).equals("\"")) {
            replace = replace.substring(1);
        }

        char c1 = replace.charAt(replace.length() - 1);

        if (String.valueOf(c1).equals("\"")) {
            replace = replace.substring(0, replace.length() - 1);
        }

        onFinished(replace.trim());
    }

    public abstract void onFinished(String s);

    public abstract void onError(Exception e);
}
