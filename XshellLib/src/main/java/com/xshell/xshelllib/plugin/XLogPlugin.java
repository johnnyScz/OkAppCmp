package com.xshell.xshelllib.plugin;

import android.content.Context;
import android.os.Handler;
import android.util.Log;


import com.xshell.xshelllib.sqlite.DataLocalityManager;
import com.xshell.xshelllib.utils.IpUtil;
import com.xshell.xshelllib.utils.LogUtils;
import com.xshell.xshelllib.utils.NetUtil;
import com.xshell.xshelllib.utils.SharedPreferencesUtils;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;


/**
 * Created by zzy on 2016/10/12.
 * XLog的插件
 */
public class XLogPlugin extends CordovaPlugin {

    private Context context;
    private String model;
    private String systemVersion;
    private String deviceId;
    private int pixels_w;
    private int pixels_h;
    private String versionName;
    private String packageName;

    //private Map<String, Long> loadTimeMap;
    private long startTime;
    private DataLocalityManager dataLocalityManager;
    private Thread userrecordThread;
    // private DataLocalityManager dataLocalityManager;
    private Handler mHandler = new Handler();
    private boolean isLogin = true;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        Log.i("zzy", "initialize:XLogPlugin");
        // loadTimeMap = new HashMap<>();
        context = cordova.getActivity();
        dataLocalityManager = DataLocalityManager.getInstance(context, "dataLocal");

//        PhoneInfo phoneInfo = PhoneInfo.getInstance(context);
//        //获取设备信息
//        //手机型号
//        model = phoneInfo.getModel();
//        //版本
//        systemVersion = "Android " + phoneInfo.getSystemVersion();
//        //手机id
//        deviceId = phoneInfo.getDeviceID();
//
//        //分辨率
//        pixels_w = phoneInfo.getPixels_w();
//        pixels_h = phoneInfo.getPixels_h();
//        //版本号
//        versionName = PreferenceXshellUtil.getInstance().getFileUpdateTime();
//        //Log.i("zzy","verionName-----------------:"+versionName);
//        packageName = phoneInfo.getPackageName();
    }


    @Override
    public Object onMessage(String id, Object data) {
        Log.i("zzy", "onMessage--------" + id + "-------------:" + data);
        if (id.equals("onPageStarted")) {
            //loadTimeMap.put((String)data, System.currentTimeMillis());
            startTime = System.currentTimeMillis();

        } else if (id.equals("onPageFinished")) {
//
            Log.e("amtf", "进入onPageFinished");
        }

        return super.onMessage(id, data);
    }

    @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("recordLog".equals(action)) {
            //拼接数据
            JSONObject mainObj = new JSONObject();
            JSONObject sysObj = new JSONObject();
            JSONObject pageObj = new JSONObject();
            JSONObject deviceObj = new JSONObject();
            try {
                sysObj.put("id", packageName);
                sysObj.put("version", versionName);
                sysObj.put("time", System.currentTimeMillis());
                sysObj.put("network", NetUtil.getTypeName(context));
                sysObj.put("ip", IpUtil.getIp());
                mainObj.put("sys", sysObj);
                pageObj.put("url", args.getString(0));
                pageObj.put("loadtime", 0);
                mainObj.put("page", pageObj);

                deviceObj.put("id", deviceId);
                deviceObj.put("os", systemVersion);
                deviceObj.put("model", model);
                deviceObj.put("width", pixels_w);
                deviceObj.put("height", pixels_h);
                mainObj.put("device", deviceObj);

                //保存到数据库
//                XLogDao xLogDao = GreenManager.getInstance(context).getXLogDao();
//                XLog xLog = new XLog(null, mainObj.toString());
//                xLogDao.insert(xLog);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        } else if ("userrecord".equals(action)) {
            final String f_Message = args.getString(0);
            //LogUtils.e("huang", "获取数据:" +  args.getString(0));
            userrecordThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        dataLocalityManager.openDatabase();
                        dataLocalityManager.createTableMessageSql();
                        String[] splitSub = f_Message.split(";");
                        ArrayList<String> dataList = new ArrayList<>();
                        StringBuilder stringBuilder = new StringBuilder(128);
                        for (int j = 0; j < splitSub.length; j++) {
                            String jsonObject = splitSub[j];
                            String[] split = jsonObject.split(",");
                            //用户id,对象id,对象类型,锚点2,操作时间(按这个顺序)
//                            stringBuilder = split[0] + "," + split[2] + "," + split[1] + "," + split[3];
                            stringBuilder.append(split[0] + "," + split[2] + "," + split[1] + "," + split[3]);
                            long time = System.currentTimeMillis();
                            String date = stringToDate(time);
                            //    LogUtils.e("huang","===="+date);
//                            String jsonObject1=stringBuilder.toString()+","+date;
                            stringBuilder.append("," + date);
                            dataList.add(stringBuilder.toString());
                            LogUtils.e("huang", "获取数据=======:" + stringBuilder.toString());
                        }
                        dataLocalityManager.insertMessageData(dataList);
                        dataLocalityManager.closeDatabase();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            userrecordThread.start();
            return true;
        } else if ("getUserInfo".equals(action)) {
            LogUtils.e("huang", "getUserInfo:" + args.getJSONObject(0).toString());
            if (isLogin) {
                isLogin = false;
                JSONObject jsonObject = args.getJSONObject(0);
                int statusCode = jsonObject.getInt("statusCode");//1是登入，-1是退出
                String userId = jsonObject.getString("userId");
                String sessionid;
                if (statusCode == 1) {
                    sessionid = jsonObject.getString("sessionid");
                    SharedPreferencesUtils.setParam(context, "SESSIONID", sessionid);

                    SharedPreferencesUtils.setParam(context, "LOGIN_APP", "App");
                    //  AppH5Config.APP_STATUE = "App";//登入标识
                } else {
                    sessionid = (String) SharedPreferencesUtils.getParam(context, "SESSIONID", 0 + "");
                    SharedPreferencesUtils.setParam(context, "SESSIONID", 0);
                    SharedPreferencesUtils.setParam(context, "LOGIN_APP", "");
                    // AppH5Config.APP_STATUE = "";
                }
                SharedPreferencesUtils.setParam(context, "userId", userId);
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isLogin = true;
                }
            }, 1000);
            return true;
        }
        return false;
    }


    public static String getHostIp() {
        try {
            Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
            InetAddress inetAddress = null;

            while (enumeration.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) enumeration.nextElement();
                Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if (inetAddress instanceof Inet6Address) {
                        continue;
                    }
                    String hostAddress = inetAddress.getHostAddress();
                    if (!"127.0.0.1".equals(hostAddress)) {
                        return hostAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String stringToDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        Date date = c.getTime();
        String format = formatter.format(date);
        return format;
    }
}
