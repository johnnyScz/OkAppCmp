package com.xinyu.newdiggtest.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import anet.channel.util.StringUtils;

public class DateUtil {

    public static String getCurrentDay() {
        int y, m, d;
        Calendar cal = Calendar.getInstance();
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DATE);
        return d + "";
    }

    public static String getWeekNO() {
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar calendar = Calendar.getInstance();
        return weekDays[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public static String getYestaday() {

        String strDateFormat = "dd";

        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        Calendar before6 = Calendar.getInstance();
        before6.add(Calendar.DAY_OF_MONTH, -1);
        return sdf.format(before6.getTime());
    }


    public static String getAfter5(int count) {
        String strDateFormat = "dd";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        Calendar after6 = Calendar.getInstance();
        after6.add(Calendar.DAY_OF_MONTH, +count);
        return sdf.format(after6.getTime());
    }

    public static String getSelectDay(int count) {
        String strDateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        Calendar calend = Calendar.getInstance();
        calend.add(Calendar.DAY_OF_MONTH, count);
        return sdf.format(calend.getTime());
    }

    public static String longToDate(long lo) {

        Date date = new Date(lo);

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

        return sd.format(date);
    }


    public static String longToDateMMss(long lo) {

        Date date = new Date(lo);

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return sd.format(date);
    }


    public static String getEndDayOfCurrentMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String lastday;
        // 获取前月的第一天
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        lastday = format.format(cale.getTime());
        return lastday;
    }


    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrentData() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        return format.format(cale.getTime());
    }

    public static String getCurrentDataTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar cale = Calendar.getInstance();

        String time = format.format(cale.getTime());

        return time.substring(time.length() - 5, time.length());
    }


    /**
     * 获得当前时间的日期时分
     *
     * @return
     */
    public static String getCurrentMs() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar cale = Calendar.getInstance();

        String time = format.format(cale.getTime());

        return time;
    }


    public static String getEndDayOfLastMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String lastday;
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.DAY_OF_MONTH, -1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        lastday = format.format(cale.getTime());
        return lastday;
    }

    public static String getDayOfNextMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String nextDay;
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, +1);
        nextDay = format.format(cale.getTime());
        return nextDay;
    }

    public static String getDayOCurrentMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String nextDay;
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        nextDay = format.format(cale.getTime());
        return nextDay;
    }


    public static long stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date.getTime();
    }

    public static long getTodayLong() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time = format.format(Calendar.getInstance().getTime());
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

    }


    /**
     * 获取当天时间
     *
     * @return
     */
    public static String getTodayStr() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(Calendar.getInstance().getTime());
    }


    /**
     * 获取当天时间
     *
     * @return
     */
    public static String getTodayHHmm() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(Calendar.getInstance().getTime());
    }


    /**
     * 获取当天月份
     *
     * @return
     */
    public static String getCurentMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String month = format.format(Calendar.getInstance().getTime());
        return month.substring(5, 7);
    }


    /**
     * long转时分秒
     *
     * @param lo
     * @return
     */
    public static String longToHm(long lo) {

        Date date = new Date(lo);

        SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd  HH:mm");

        return sd.format(date);
    }

    public static String longToDay(long lo) {

        Date date = new Date(lo);

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

        return sd.format(date);
    }


    /**
     * 2018-11-24 转 11.24
     *
     * @return
     */
    public static String timeOnlyDot(String start) {
        String nStart = start.replaceAll("-", ".");
        return nStart.substring(5, start.length());
    }


    public static boolean end2beginTime(String beginTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long end1 = sdf.parse(endTime).getTime();
            long begin1 = sdf.parse(beginTime).getTime();
            if (end1 < begin1) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 时间戳只取 时分
     * <p>
     * 2018-11-24 11:56:23  转 11.56
     *
     * @return
     */
    public static String timeOnlyHourMin(String time) {
        if (MyTextUtil.isEmpty(time) || time.length() < 9) {
            return "";
        }
        return time.substring(time.length() - 8, time.length() - 3);
    }


    public static String cu(String time) {
        if (MyTextUtil.isEmpty(time) || time.length() < 9) {
            return "";
        }
        return time.substring(time.length() - 8, time.length() - 3);
    }

    /**
     * 获取当前时分秒
     *
     * @return
     */
    public static String getCurrentHms() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }


    /**
     * 获取当前时分秒
     *
     * @return
     */
    public static String getOnlyDate(String dateStr) {

        if (dateStr.length() < 17) {
            return "";

        }

        return dateStr.substring(0, dateStr.length() - 8).trim();


    }


    /**
     * 把横线 替换成"."
     *
     * @param time
     * @return
     */
    public static String getDotDay(String time) {
        if (MyTextUtil.isEmpty(time) || time.length() < 9) {
            return "";
        }
        ;
        return time.replaceAll("-", ".");
    }


    /**
     * 把横线 替换成"."
     *
     * @param time
     * @return
     */
    public static String getDotDayNoYear(String time) {
        if (MyTextUtil.isEmpty(time) || time.length() < 9) {
            return "";
        }

        String day = time.replaceAll("-", ".");
        ;
        return day.substring(5, day.length());
    }

    /**
     * 时间戳转化为long
     *
     * @param date
     * @return
     */
    public static long convert2long(String date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            if (StringUtils.isNotBlank(date)) {
                return format.parse(date).getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }



    /**
     * 时间戳转化为long
     *
     * @param date
     * @return
     */
    public static long convertSecond(String date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            if (StringUtils.isNotBlank(date)) {
                return format.parse(date).getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }



    /**
     * 只要年月日不要时分
     *
     * @param time
     * @return
     */
    public static String getDotDayNoHour(String time) {
        if (MyTextUtil.isEmpty(time) || time.length() < 11) {
            return "";
        }
        ;
        return time.substring(0, 10);
    }


    /**
     * 把横线 替换成"."
     * <p>
     * 2019-11-28 12:40:19 替换成"2019.11.28"
     *
     * @param time
     * @return
     */
    public static String getDotOnlyDay(String time) {
        if (MyTextUtil.isEmpty(time) || time.length() < 11) {
            return "";
        }
        ;
        String day = time.substring(0, 10);

        return day.replaceAll("-", ".");
    }


}
