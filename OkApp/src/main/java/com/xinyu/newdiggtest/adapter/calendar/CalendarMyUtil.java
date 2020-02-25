package com.xinyu.newdiggtest.adapter.calendar;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarMyUtil {


    Calendar cale;


    private CalendarMyUtil() {
        cale = Calendar.getInstance();
    }

    private static volatile CalendarMyUtil INSTANCE = null;

    //同步锁保证多线程安全
    public static CalendarMyUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (CalendarMyUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CalendarMyUtil();
                }
            }
        }
        return INSTANCE;
    }


    public String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfMonth = sdf.format(cal.getTime());
        return firstDayOfMonth;
    }


    /**
     * 获得该月最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public int getDay0fMont(int year, int month) {

        cale.set(year, month, 0); //输入类型为int类型
        int dayOfMonth = cale.get(Calendar.DAY_OF_MONTH);
        return dayOfMonth;

    }


    /**
     * 某年的某月所有天数
     *
     * @param year
     * @param month
     * @return
     */
    public List<String> getEveryDay0fMont(int year, int month) {

        List<String> dayOfMonth = new ArrayList<>();
        String firstDay = getFirstDayOfMonth(year, month);
        int len = getDay0fMont(year, month);

        String fontStr = firstDay.substring(0, 7);

        for (int k = 1; k <= len; k++) {

            String day = "";
            if (k < 10) {
                day = "0" + k;
            } else {
                day = "" + k;
            }
            dayOfMonth.add(fontStr + "-" + day);

        }

        return dayOfMonth;
    }


    /**
     * 获得该月最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }


}




