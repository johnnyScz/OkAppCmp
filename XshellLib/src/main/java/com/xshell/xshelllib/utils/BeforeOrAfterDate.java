package com.xshell.xshelllib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by DELL on 2017/8/22.
 */

public class BeforeOrAfterDate {
    /**
     *
     * 查询当前日期前(后)x天的日期
     *
     * @param date 当前日期
     * @param day 天数（如果day数为负数,说明是此日期前的天数）
     * @return yyyy-MM-dd
     */
    public String beforNumDay(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, day);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    /**
     *
     * 查询当前日期前(后)x天的日期
     *
     * @param date 当前日期
     * @param day 天数（如果day数为负数,说明是此日期前的天数）
     * @return yyyyMMdd
     */
    public String beforNumberDay(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, day);
        return new SimpleDateFormat("yyyyMMdd").format(c.getTime());
    }

    /**
     * 查询当前日期前(后)x天的日期
     *
     * @param millis 当前日期毫秒数
     * @param day 天数（如果day数为负数,说明是此日期前的天数）
     * @return long 毫秒数只显示到天，时间全为0
     * @throws ParseException
     */
    public long beforDateNum(long millis, int day) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.add(Calendar.DAY_OF_YEAR, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(c.getTimeInMillis());
        Date newDate = sdf.parse(sdf.format(date));
        return newDate.getTime();
    }

    /**
     *  查询当前日期前(后)x天的日期
     *
     * @param millis 当前日期毫秒数
     * @param day 天数（如果day数为负数,说明是此日期前的天数）
     * @return yyyy-MM-dd
     */
    public String beforLongDate(long millis, int day) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.add(Calendar.DAY_OF_YEAR, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(c.getTimeInMillis());
        return sdf.format(date);
    }
}
