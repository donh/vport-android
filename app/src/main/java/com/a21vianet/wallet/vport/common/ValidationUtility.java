package com.a21vianet.wallet.vport.common;

/**
 * Created by wang.rongqiang on 2017/7/5.
 */

public class ValidationUtility {
    static int[] weight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};    //十七位数字本体码权重
    static char[] validate = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};    //mod11,对应校验码字符值

    /**
     * 简单验算身份证是否合法需要配合正则验证
     * @param number
     * @return
     */
    public static boolean reagulerIdEntityNumber(String number) {
        String substring = number.substring(0, 17);
        int sum = 0;
        int mode;
        for (int i = 0; i < substring.length(); i++) {
            sum = sum + Integer.parseInt(String.valueOf(substring.charAt(i))) * weight[i];
        }
        mode = sum % 11;
        char c = validate[mode];
        if (number.charAt(17) == c) {
            return true;
        } else {
            return false;
        }
    }
}
