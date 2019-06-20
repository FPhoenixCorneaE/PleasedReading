package com.wkz.unit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wkz
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface MemoryUnit {

    /**
     * Byte与Byte的倍数
     */
    long BYTE = (long) Math.pow(1024, 0);
    /**
     * KB与Byte的倍数
     */
    long KB = (long) Math.pow(1024, 1);
    /**
     * MB与Byte的倍数
     */
    long MB = (long) Math.pow(1024, 2);
    /**
     * GB与Byte的倍数
     */
    long GB = (long) Math.pow(1024, 3);
    /**
     * TB与Byte的倍数
     */
    long TB = (long) Math.pow(1024, 4);
    /**
     * PB与Byte的倍数
     */
    long PB = (long) Math.pow(1024, 5);
    /**
     * EB与Byte的倍数
     */
    long EB = (long) Math.pow(1024, 6);
    /**
     * ZB与Byte的倍数
     */
    long ZB = (long) Math.pow(1024, 7);
    /**
     * YB与Byte的倍数
     */
    long YB = (long) Math.pow(1024, 8);
    /**
     * BB与Byte的倍数
     */
    long BB = (long) Math.pow(1024, 9);
    /**
     * NB与Byte的倍数
     */
    long NB = (long) Math.pow(1024, 10);
    /**
     * DB与Byte的倍数
     */
    long DB = (long) Math.pow(1024, 11);
    /**
     * CB与Byte的倍数
     */
    long CB = (long) Math.pow(1024, 12);
    /**
     * XB与Byte的倍数
     */
    long XB = (long) Math.pow(1024, 13);
}
