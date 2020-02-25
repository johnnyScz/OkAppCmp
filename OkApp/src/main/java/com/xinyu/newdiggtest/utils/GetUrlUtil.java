package com.xinyu.newdiggtest.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class GetUrlUtil {


    public static String getRqstUrl(String url, Map<String, String> params) {
        StringBuilder builder = new StringBuilder(url);
        boolean isFirst = true;
        for (String key : params.keySet()) {
            if (key != null && params.get(key) != null) {
                if (isFirst) {
                    isFirst = false;
                    builder.append("?");
                } else {
                    builder.append("&");
                }
                builder.append(key)
                        .append("=")
                        .append(params.get(key));
            }
        }
        return builder.toString();
    }


    public static String getJsonByInternet(String path) {
        try {
            URL url = new URL(path.trim());
            //打开连接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (HttpURLConnection.HTTP_OK== urlConnection.getResponseCode() || 400 == urlConnection.getResponseCode()) {
                //得到输入流
                InputStream is = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while (-1 != (len = is.read(buffer))) {
                    baos.write(buffer, 0, len);
                    baos.flush();
                }
                return baos.toString("utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "505";
    }


    public static String urlConn(String segId, String message) {
        String result = "";
        String url1 = "http://localhost:8080/wangdkMvc/spider/urlGetJson.aido?targetOffer={";
        try {
            StringBuffer urlBuff = new StringBuffer(url1);


            urlBuff.append("\"segId\":\"" + segId + "\"");
            urlBuff.append(",\"message\":\"" + message + "\"");
            urlBuff.append("}");
            url1 = urlBuff.toString();
            System.out.println("url=" + url1);
            URL url = null;
            url = new URL(url1);
            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
            urlcon.connect();         //获取连接
            InputStream is = urlcon.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(is, "GBK"));
            StringBuffer bs = new StringBuffer();
            String l = null;
            while ((l = buffer.readLine()) != null) {
                bs.append(l);
            }
            System.out.println(bs.toString());
            result = bs.toString();
        } catch (IOException e) {
            e.printStackTrace();
            result = String.valueOf(e.getMessage());
        }
        return result;

    }
}



