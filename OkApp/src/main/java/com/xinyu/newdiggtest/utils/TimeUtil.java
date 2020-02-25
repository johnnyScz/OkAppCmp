package com.xinyu.newdiggtest.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {


    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5F);
    }

    /**
     * 文本中的emojb字符处理为表情图片
     *
     * @param context
     * @param tv
     * @param source
     * @return
     */
//    public static SpannableString getEmotionContent(final Context context, final TextView tv, String source) {
//        SpannableString spannableString = new SpannableString(source);
//        Resources res = context.getResources();
//
//        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";
//        Pattern patternEmotion = Pattern.compile(regexEmotion);
//        Matcher matcherEmotion = patternEmotion.matcher(spannableString);
//
//        while (matcherEmotion.find()) {
//            // 获取匹配到的具体字符
//            String key = matcherEmotion.group();
//            // 匹配字符串的开始位置
//            int start = matcherEmotion.start();
//            // 利用表情名字获取到对应的图片
//            Integer imgRes = EmotionUtils.EMOTION_STATIC_MAP.get(key);
//            if (imgRes != null) {
//                // 压缩表情图片
//                int size = (int) tv.getTextSize() * 13 / 8;
//                Bitmap bitmap = BitmapFactory.decodeResource(res, imgRes);
//                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
//
//                ImageSpan span = new ImageSpan(context, scaleBitmap);
//                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//        }
//        return spannableString;
//    }

    /**
     * 返回当前时间的格式为 yyyyMMddHHmmss
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(System.currentTimeMillis());
    }

    //毫秒转秒
    public static String long2String(long time) {
        //毫秒转秒
        int sec = (int) time / 1000;
        int min = sec / 60;    //分钟
        sec = sec % 60;        //秒
        if (min < 10) {    //分钟补0
            if (sec < 10) {    //秒补0
                return "0" + min + ":0" + sec;
            } else {
                return "0" + min + ":" + sec;
            }
        } else {
            if (sec < 10) {    //秒补0
                return min + ":0" + sec;
            } else {
                return min + ":" + sec;
            }
        }
    }

    /**
     * 毫秒转化时分秒毫秒
     *
     * @param ms
     * @return
     */
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "d");
        }
        if (hour > 0) {
            sb.append(hour + "h");
        }
        if (minute > 0) {
            sb.append(minute + "′");
        }
        if (second > 0) {
            sb.append(second + "″");
        }
        return sb.toString();
    }


    /**
     * 时间转化为显示字符串
     *
     * @param timeStamp 单位为秒
     */
//    public static String getTimeStr(long timeStamp) {
//        if (timeStamp == 0) return "";
//        Calendar inputTime = Calendar.getInstance();
//        inputTime.setTimeInMillis(timeStamp * 1000);
//        Date currenTimeZone = inputTime.getTime();
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 23);
//        calendar.set(Calendar.MINUTE, 59);
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        if (calendar.before(inputTime)) {
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//            return sdf.format(currenTimeZone);
//        }
//        calendar.add(Calendar.DAY_OF_MONTH, -1);
//        if (calendar.before(inputTime)) {
//            return "昨天";
//        } else {
//            calendar.add(Calendar.DAY_OF_MONTH, -5);
//            if (calendar.before(inputTime)) {
//                return getWeekDayStr(inputTime.get(Calendar.DAY_OF_WEEK));
//            } else {
//                calendar.set(Calendar.DAY_OF_MONTH, 1);
//                calendar.set(Calendar.MONTH, Calendar.JANUARY);
//                int year = inputTime.get(Calendar.YEAR);
//                int month = inputTime.get(Calendar.MONTH);
//                int day = inputTime.get(Calendar.DAY_OF_MONTH);
//                return year + "/" + month + "/" + day;
//            }
//        }
//
//    }


    /**
     * 时间转化为星期
     *
     * @param indexOfWeek 星期的第几天
     */
    public static String getWeekDayStr(int indexOfWeek) {
        String weekDayStr = "";
        switch (indexOfWeek) {
            case 1:
                weekDayStr = "星期日";
                break;
            case 2:
                weekDayStr = "星期一";
                break;
            case 3:
                weekDayStr = "星期二";
                break;
            case 4:
                weekDayStr = "星期三";
                break;
            case 5:
                weekDayStr = "星期四";
                break;
            case 6:
                weekDayStr = "星期五";
                break;
            case 7:
                weekDayStr = "星期六";
                break;
        }
        return weekDayStr;
    }


    /**
     * 时间转化为聊天界面显示字符串
     *
     * @param timeStamp 单位为秒
     */
    public static String getChatTimeStr(long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        String timeStr = timeStamp + "";
        if (timeStr.length() == 10) {
            timeStamp = timeStamp * 1000;
        }
        inputTime.setTimeInMillis(timeStamp);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
            return timeFormatStr(inputTime, sdf.format(currenTimeZone));
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);


        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
            return "昨天" + " " + timeFormatStr(inputTime, sdf.format(currenTimeZone));
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, -5);
            if (calendar.before(inputTime)) {
                return getWeekDayStr(inputTime.get(Calendar.DAY_OF_WEEK));
            } else {
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.MONTH, Calendar.JANUARY);
                if (calendar.before(inputTime)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("M" + "月" + "d" + "日");
                    String temp1 = sdf.format(currenTimeZone);
                    SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm");
                    String temp2 = timeFormatStr(inputTime, sdf1.format(currenTimeZone));
                    return temp1 + temp2;
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "年" + "M" + "月" + "d" + "日");
                    String temp1 = sdf.format(currenTimeZone);
                    SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm");
                    String temp2 = timeFormatStr(inputTime, sdf1.format(currenTimeZone));
                    return temp1 + temp2;
                }
            }


        }

    }

    /**
     * 24小时制转化成12小时制
     *
     * @param strDay
     */
    public static String timeFormatStr(Calendar calendar, String strDay) {
        String tempStr = "";
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 11) {
            tempStr = "下午" + " " + strDay;
        } else {
            tempStr = "上午" + " " + strDay;
        }
        return tempStr;
    }

    /**
     * 时间转化为聊天界面显示字符串
     */
    public static String getLast4Month(int count) {

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, -count);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");


        String temp1 = sdf.format(calendar.getTime());


        String[] month = temp1.split("-");

        int after = Integer.parseInt(month[1]) + 1;

        if (after < 10) {
            return month[0] + "-0" + after;
        } else {
            return month[0] + "-" + after;
        }
    }

}




