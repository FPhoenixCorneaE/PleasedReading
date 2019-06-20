package com.wkz.utils;

import android.os.Build;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * MD5加密
 *
 * @author wkz
 */

public class MD5Utils {

    private MD5Utils() {
        throw new UnsupportedOperationException("U can't initialize me...");
    }

    /**
     * 获取MD5串
     */
    public static String getMD5(String origin) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                md5.update(origin.getBytes(StandardCharsets.UTF_8));
            } else {
                md5.update(origin.getBytes("UTF-8"));
            }
            byte[] encryption = md5.digest();

            StringBuilder strBuilder = new StringBuilder();
            for (byte anEncryption : encryption) {
                if (Integer.toHexString(0xff & anEncryption).length() == 1) {
                    strBuilder.append("0").append(Integer.toHexString(0xff & anEncryption));
                } else {
                    strBuilder.append(Integer.toHexString(0xff & anEncryption));
                }
            }

            return strBuilder.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 'A' 是随机生成大写的字母
     * 'a' 是随机生成小写的字母
     */
    public static String generateMD5Str() {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            strBuilder.append((char) (Math.random() * 26 + 'A'));
        }
        strBuilder.append(System.currentTimeMillis());
        return getMD5(strBuilder.toString());
    }
}
