package com.a21vianet.wallet.vport.library.commom.utility;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wang.rongqiang on 2017/6/7.
 */

public class CryptoUtility {
    /**
     * 把比特币助记词由List转为空格隔开的字符串
     *
     * @param list 助记词
     * @return
     */
    public static String wordsList2String(List<String> list) {
        StringBuffer builder = new StringBuffer();
        for (String str : list) {
            builder.append(str.trim() + " ");
        }
        return builder.toString().trim();
    }

    /**
     * 把比特币助记词由空格隔开的字符串转为List
     *
     * @param words 空格隔开的助记词字符串
     * @return
     */
    public static List<String> wordsString2List(String words) {
        String[] keys = words.split(" ");
        ArrayList<String> wordslist = new ArrayList<>(Arrays.asList(keys));
        List<Integer> pepsiList = new ArrayList<>();
        for (int i = 0; i < wordslist.size(); i++) {
            String word = wordslist.get(i);
            if (word.trim().equals("")) {
                pepsiList.add(i);
            }
        }
        for (int i = pepsiList.size() - 1; i >= 0; i--) {
            wordslist.remove((int) pepsiList.get(i));
        }
        return wordslist;
    }

    /**
     * 判断助记词字符串是否合法
     *
     * @param words
     * @return
     */
    public static boolean wordsIsSuccess(String words) {
        if (TextUtils.isEmpty(words.trim())) {
            return false;
        }
        ArrayList<String> wordslist = (ArrayList<String>) wordsString2List(words);
        if (wordslist.size() == 12) {
            return true;
        } else {
            return false;
        }
    }
}
