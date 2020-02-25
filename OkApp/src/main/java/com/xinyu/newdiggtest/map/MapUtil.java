package com.xinyu.newdiggtest.map;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * 调用高德相关API
 */
public class MapUtil {
    //个人key
    private static final String KEY = "a1f64fe17c760aceb46ebc4f3dc1f37c";
    //API前缀
    private static final String BASE_PATH = "http://restapi.amap.com/v3";

    public static void main(String[] args) {
        //1.计算两个经纬度之间的距离
        String origin = "104.043390" + "," + "30.641982";  // 格式:经度,纬度;注意：高德最多取小数点后六位
        String target = "106.655347" + "," + "31.786691";
        String distance = distance(origin, target);
        System.out.println("原坐标:{" + origin + "}，目标坐标:{" + target + "}--------->计算后距离：" + distance);

        //2.地址转换高德坐标
        String address = "成都市武侯区";
        String coordinate = coordinate(address);
        System.out.println("转换前地址:" + address + "--------->转变后坐标：" + coordinate);


        //3.gps坐标转化为高德坐标
        String coordsys = "121.43687,31.18826";
        String moordsys = convert(coordsys);
        System.out.println("转换前的经纬度：" + coordsys + "-------->转变后的经纬度:" + moordsys);
    }

    /**
     * 高德地图WebAPI : 驾车路径规划 计算两地之间行驶的距离(米)
     * String origins:起始坐标
     * String destination:终点坐标
     */
    public static String distance(String origins, String destination) {
        int strategy = 0;
        /**
         * 0:速度优先（时间）; 1:费用优先（不走收费路段的最快道路）;2:距离优先; 3:不走快速路 4躲避拥堵;
         * 5:多策略（同时使用速度优先、费用优先、距离优先三个策略计算路径）;6:不走高速; 7:不走高速且避免收费;
         * 8:躲避收费和拥堵; 9:不走高速且躲避收费和拥堵
         */
        String url = BASE_PATH + "/direction/driving?" + "origin=" + origins + "&destination=" + destination
                + "&strategy=" + strategy + "&extensions=base&key=" + KEY;
//        JSONObject jsonobject = JSONObject.fromObject(getHttpResponse(url));

        String distanceString = null;
        try {
            JSONObject jsonobject = new JSONObject(getHttpResponse(url));
            JSONArray pathArray = jsonobject.getJSONObject("route").getJSONArray("paths");
            distanceString = pathArray.getJSONObject(0).getString("distance");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return distanceString;
    }

    /**
     * 高德地图WebAPI : 地址转化为高德坐标
     * String address：高德地图地址
     */
    public static String coordinate(String address) {
        try {
            address = URLEncoder.encode(address, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String url = BASE_PATH + "/geocode/geo?address=" + address + "&output=json&key=" + KEY;

        Log.e("amtf", "地址转经纬度：" + url);

        String coordinateString = null;

        getHttpResponse(url);

//        try {
//            JSONObject jsonobject = new JSONObject(getHttpResponse(url));
//            JSONArray pathArray = jsonobject.getJSONArray("geocodes");
//            coordinateString = pathArray.getJSONObject(0).getString("location");
//        } catch (JSONException e) {
//            e.printStackTrace();
//
//        }
//        Log.e("amtf", "转换结果：" + coordinateString);
        return coordinateString;
    }

    /**
     * 高德地图WebAPI : gps坐标转化为高德坐标
     * String coordsys：高德地图坐标
     */
    public static String convert(String coordsys) {
        try {
            coordsys = URLEncoder.encode(coordsys, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = BASE_PATH + "/assistant/coordinate/convert?locations=" + coordsys + "&coordsys=gps&output=json&key=" + KEY;

        String coordinateString = null;
        try {
            JSONObject jsonobject = new JSONObject(getHttpResponse(url));
            System.out.println(jsonobject.toString());
            coordinateString = jsonobject.getString("locations");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return coordinateString;
    }

    public static String getHttpResponse(String allConfigUrl) {
        BufferedReader in = null;
        StringBuffer result = null;
        try {
            // url请求中如果有中文，要在接收方用相应字符转码
            URI uri = new URI(allConfigUrl);
            URL url = uri.toURL();
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Content-type", "text/html");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("contentType", "utf-8");
            connection.connect();
            result = new StringBuffer();
            // 读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }

            Log.e("amtf", "result:" + result.toString());

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("amtf", "error:" + e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }
}
