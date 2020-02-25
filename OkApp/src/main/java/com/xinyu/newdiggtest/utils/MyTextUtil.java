package com.xinyu.newdiggtest.utils;

import android.text.TextUtils;

import com.xinyu.newdiggtest.bean.BaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

public class MyTextUtil {

    public static boolean isEmpty(String str) {
        if (null == str || str.equals("") || str.equals("") || TextUtils.isEmpty(str.trim()) || str.equals("\"null\"")) {
            return true;
        }

        return false;
    }


    public static String getUrl3Encoe(String str) {
        String retStr = "";
        try {
            String temp1 = URLEncoder.encode(str, "UTF-8");
            String temp12 = URLEncoder.encode(temp1, "UTF-8");
            retStr = URLEncoder.encode(temp12, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retStr;
    }


    public static String getUrl2Encoe(String str) {
        String retStr = "";
        try {
            String temp1 = URLEncoder.encode(str, "UTF-8");
            retStr = URLEncoder.encode(temp1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retStr;
    }


    public static String getUrl1Encoe(String str) {
        String retStr = "";
        try {
            retStr = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retStr;
    }


    public static String getDecodeStr(String data) {
        if (MyTextUtil.isEmpty(data)) {
            return "";
        }

        String ss = data.replaceAll("%(?![0-9a-fA-F]{2})", "%25");

        String feedStr = "";
        try {
            feedStr = URLDecoder.decode(ss, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return feedStr;
    }


    public static String getCommonDecodeStr(String data) {
        if (MyTextUtil.isEmpty(data)) {
            return "";
        }
        String feedStr = "";
        try {
            feedStr = URLDecoder.decode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return feedStr;
    }


    public static String getExcutorListNames(List<BaseUser> list) {

        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (BaseUser item : list) {
            buffer.append(item.getNickname()).append(",");
        }
        String str = buffer.toString();
        return str.substring(0, str.length() - 1);
    }

    public static String getExcutorListUserId(List<BaseUser> list) {

        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (BaseUser item : list) {
            buffer.append(item.getUser_id()).append(",");
        }
        String str = buffer.toString();
        return str.substring(0, str.length() - 1);
    }


    public static String getTargtStr(String str) {
        String retStr = "";
        try {
            JSONObject object = new JSONObject(str);
            retStr = object.getString("content");
        } catch (JSONException e) {
            retStr = str;
            e.printStackTrace();
        }

        return retStr;
    }


    public static String getBehandStr(String str1, String str2) {
        String retStr = str1;
        if (MyTextUtil.isEmpty(str1)) {
            retStr = str2;
        }
        return retStr;
    }


    public static String replaeT(String orig) {
        String retStr = "";

        if (orig.contains("T")) {

            retStr = orig.replace("T", " ");

        }


        return retStr.substring(0, retStr.length() - 3);
    }


    public static boolean checkIfImg(String fileName) {

        if (!TextUtils.isEmpty(fileName) && fileName.lastIndexOf(".") != -1) {

            int len = fileName.lastIndexOf(".");

            String path = fileName.substring(len, fileName.length());

            if (path.toLowerCase().equals("jpg") || path.toLowerCase().equals("jeg") || path.toLowerCase().equals("jpeg") || path.toLowerCase().equals("png")) {
                return true;
            }


        }


        return false;

    }


}



