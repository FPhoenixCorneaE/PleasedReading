package com.wkz.framework.utils;

import com.orhanobut.logger.Logger;
import com.wkz.framework.annotations.FRTimeUnit;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取友好型与当前时间的差
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 友好型与当前时间的差
     * <ul>
     * <li>如果在60秒内，显示刚刚</li>
     * <li>如果在60分钟内，显示XXX分钟前</li>
     * <li>如果超过1小时且24小时内，显示1小时前~23小时前</li>
     * <li>如果超过24小时且48小时内，显示昨天</li>
     * <li>如果超过48小时且30天内，显示2天前~30天前</li>
     * <li>如果超过1个月且1年内，显示1个月前~11个月前</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    public static String getFriendlyTimeSpanByNow(String time) {
        try {
            return getFriendlyTimeSpanByNow(time, new SimpleDateFormat(DEFAULT_FORMAT, Locale.getDefault()));
        } catch (Exception e) {
            Logger.e(e.toString());
            return time;
        }
    }

    /**
     * 获取友好型与当前时间的差
     * <p>time格式为format</p>
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return 友好型与当前时间的差
     * <ul>
     * <li>如果在60秒内，显示刚刚</li>
     * <li>如果在60分钟内，显示XXX分钟前</li>
     * <li>如果超过1小时且24小时内，显示1小时前~23小时前</li>
     * <li>如果超过24小时且48小时内，显示昨天</li>
     * <li>如果超过48小时且30天内，显示2天前~30天前</li>
     * <li>如果超过1个月且1年内，显示1个月前~11个月前</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    public static String getFriendlyTimeSpanByNow(String time, DateFormat format) {
        return getFriendlyTimeSpanByNow(string2Millis(time, format));
    }

    /**
     * 获取友好型与当前时间的差
     *
     * @param date Date类型时间
     * @return 友好型与当前时间的差
     * <ul>
     * <li>如果在60秒内，显示刚刚</li>
     * <li>如果在60分钟内，显示XXX分钟前</li>
     * <li>如果超过1小时且24小时内，显示1小时前~23小时前</li>
     * <li>如果超过24小时且48小时内，显示昨天</li>
     * <li>如果超过48小时且30天内，显示2天前~30天前</li>
     * <li>如果超过1个月且1年内，显示1个月前~11个月前</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    public static String getFriendlyTimeSpanByNow(Date date) {
        return getFriendlyTimeSpanByNow(date.getTime());
    }

    /**
     * 获取友好型与当前时间的差
     *
     * @param millis 毫秒时间戳
     * @return 友好型与当前时间的差
     * <ul>
     * <li>如果在60秒内，显示刚刚</li>
     * <li>如果在60分钟内，显示XXX分钟前</li>
     * <li>如果超过1小时且24小时内，显示1小时前~23小时前</li>
     * <li>如果超过24小时且48小时内，显示昨天</li>
     * <li>如果超过48小时且30天内，显示2天前~30天前</li>
     * <li>如果超过1个月且1年内，显示1个月前~11个月前</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    public static String getFriendlyTimeSpanByNow(long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        if (span < 0)
            return String.format(Locale.getDefault(), "%tc", millis);
        if (span < FRTimeUnit.MINUTE) {
            return "刚刚";
        } else if (span < FRTimeUnit.HOUR) {
            return String.format(Locale.getDefault(), "%d分钟前", span / FRTimeUnit.MINUTE);
        } else if (span < FRTimeUnit.DAY) {
            return String.format(Locale.getDefault(), "%d小时前", span / FRTimeUnit.HOUR);
        } else if (span < 2 * FRTimeUnit.DAY) {
            return String.format("昨天%tR", millis);
        } else if (span < FRTimeUnit.MONTH) {
            return String.format(Locale.getDefault(), "%d天前", span / FRTimeUnit.DAY);
        } else if (span < FRTimeUnit.YEAR) {
            return String.format(Locale.getDefault(), "%d个月前", span / FRTimeUnit.MONTH);
        } else {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(millis);
        }
    }

    /**
     * 将时间字符串转为时间戳
     * <p>time格式为format</p>
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return 毫秒时间戳
     */
    public static long string2Millis(String time, DateFormat format) {
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 将时间戳转为时间字符串
     * <p>格式为format</p>
     *
     * @param millis 毫秒时间戳
     * @param format 时间格式
     * @return 时间字符串
     */
    public static String millis2String(long millis, DateFormat format) {
        return format.format(new Date(millis));
    }

    /**
     * 获取当前时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @return 时间字符串
     */
    public static String getNowString() {
        return millis2String(System.currentTimeMillis(), new SimpleDateFormat(DEFAULT_FORMAT, Locale.getDefault()));
    }
}
