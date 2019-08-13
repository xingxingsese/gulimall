package com.atguigu.gulimall.commons.utils;

import org.junit.Test;

/**
 * @author heyijieyou
 * @date 2019-08-05 22:46
 */
public class AppUtils {
    public static String arrarToStringWitSeperator(String[] arr, String sep) {

        StringBuffer sb = new StringBuffer();
        String ss = "";
        if (arr != null && arr.length > 0) {
            for (String s : arr) {
                sb.append(s).append(sep);
            }
            ss = sb.toString().substring(0, sb.length() - 1);
        }
        return ss;
    }


}


