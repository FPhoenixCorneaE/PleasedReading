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
public @interface TimeUnit {

    /**
     * 毫秒与毫秒的倍数
     */
    long MILLISECOND = 1L;
    /**
     * 秒与毫秒的倍数
     */
    long SECOND = 1000L;
    /**
     * 分与毫秒的倍数
     */
    long MINUTE = 60 * 1000L;
    /**
     * 时与毫秒的倍数
     */
    long HOUR = 60 * 60 * 1000L;
    /**
     * 天与毫秒的倍数
     */
    long DAY = 24 * 60 * 60 * 1000L;
    /**
     * 月与毫秒的倍数
     */
    long MONTH = 30 * 24 * 60 * 60 * 1000L;
    /**
     * 年与毫秒的倍数
     */
    long YEAR = 365 * 24 * 60 * 60 * 1000L;
}
