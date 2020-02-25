package com.xshell.xshelllib.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by DELL on 2018/5/7.
 */

public class CalendarUtil {

    private static String CALANDER_URL = "content://com.android.calendar/calendars";
    private static String CALANDER_EVENT_URL = "content://com.android.calendar/events";
    private static String CALANDER_REMIDER_URL = "content://com.android.calendar/reminders";
    public final static int REQUESTCODE = 0x11;

    private static volatile CalendarUtil instance = null;

    private CalendarUtil() {


    }


    public static CalendarUtil getInstance() {
        if (instance == null) {
            synchronized (CalendarUtil.class) {
                if (instance == null) {
                    instance = new CalendarUtil();
                }
            }
        }
        return instance;
    }


    private static int checkCalendarAccount(Context context) {
        Cursor userCursor = context.getContentResolver().query(Uri.parse(CALANDER_URL), null, null, null, null);
        try {
            if (userCursor == null)//查询返回空值
                return -1;
            int count = userCursor.getCount();
            if (count > 0) {//存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }


    private static String CALENDARS_NAME = "Johnny_test";
    private static String CALENDARS_ACCOUNT_NAME = "15618210727@163.com";
    private static String CALENDARS_ACCOUNT_TYPE = "com.android.exchange";
    private static String CALENDARS_DISPLAY_NAME = "测试账户";

    private static long addCalendarAccount(Context context) {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);

        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(CALANDER_URL);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }


    private static int checkAndAddCalendarAccount(Context context) {
        int oldId = checkCalendarAccount(context);
        if (oldId >= 0) {
            return oldId;
        } else {
            long addId = addCalendarAccount(context);
            if (addId >= 0) {
                return checkCalendarAccount(context);
            } else {
                return -1;
            }
        }
    }


    public static long addCalendarEvent(Context context, String title, String description, long beginTime, long endTime) {
        // 获取日历账户的id
        long calId = 0;
        try {
            calId = checkAndAddCalendarAccount(context);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (calId < 0) {
            // 获取账户id失败直接返回，添加日历事件失败
            return -1;
        }

        ContentValues event = new ContentValues();
        event.put("title", title);
        event.put("description", description);
        // 插入账户的id
        event.put("calendar_id", calId);

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(beginTime);//设置开始时间
        long start = mCalendar.getTime().getTime();
        //TODO 终止时间
        mCalendar.setTimeInMillis(endTime);//设置终止时间
        long end = mCalendar.getTime().getTime();

        event.put(CalendarContract.Events.DTSTART, start);
        event.put(CalendarContract.Events.DTEND, end);
        event.put(CalendarContract.Events.HAS_ALARM, 1);//设置有闹钟提醒
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");  //这个是时区，必须有，
        //添加事件
        Uri newEvent = context.getContentResolver().insert(Uri.parse(CALANDER_EVENT_URL), event);
        if (newEvent == null) {
            // 添加日历事件失败直接返回
            return -1;
        }
        //事件提醒的设定
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
        calId = ContentUris.parseId(newEvent);
        // 提前10分钟有提醒
        values.put(CalendarContract.Reminders.MINUTES, 1);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri = context.getContentResolver().insert(Uri.parse(CALANDER_REMIDER_URL), values);

        if (uri == null) {
            // 添加闹钟提醒失败直接返回
            return -1;
        }

        return calId;
    }

    public static JSONArray queryCalendarList(Context context) {
        JSONArray array = new JSONArray();
        Cursor cursor = context.getContentResolver().query(Uri.parse(CALANDER_EVENT_URL), null, null, null, null);
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                String eventId = cursor.getString(cursor.getColumnIndex("_id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                JSONObject obj = new JSONObject();
                try {
                    obj.put("eventId", eventId);
                    obj.put("title", title);
                    array.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return array;
    }

    public static JSONArray deleteCalendarByGroup(Context context, JSONArray array) {
        JSONArray resultArray = new JSONArray();
        if (array == null || array.length() <= 0) {
            return null;
        }
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                String eventid = obj.getString("eventId");
                int resultCode = deleteCalendarEvent(context, eventid);
                JSONObject resut = new JSONObject();
                if (resultCode != -1) {
                    resut.put("resultCode", 9000);
                } else {
                    resut.put("resultCode", -1);
                }
                resut.put("resultStr", eventid);
                resultArray.put(resut);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        return resultArray;
    }


    public static int deleteCalendarEvent(Context context, String eventId) {
        Uri deleteUri = ContentUris.withAppendedId(Uri.parse(CALANDER_EVENT_URL), Long.parseLong(eventId));
        if (deleteUri == null) {
            return -1;
        }
        int result = context.getContentResolver().delete(deleteUri, null, null);
        return result;

    }


}
