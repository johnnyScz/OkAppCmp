package com.xshell.xshelllib.plugin;

import android.content.Context;
import android.util.Log;


import com.xshell.xshelllib.sqlite.DataLocalityHuManager;
import com.xshell.xshelllib.tools.http.MyRequestCallBack;
import com.xshell.xshelllib.utils.EncodeUtil;
import com.xshell.xshelllib.utils.FileUtil;
import com.xshell.xshelllib.utils.LogUtils;
import com.xshell.xshelllib.utils.TimeUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;

import static android.R.attr.value;

/**
 * Created by hushen on 2017/8/13.
 * 本地缓存测试
 */

public class DataLocalityPlugin extends CordovaPlugin {

    private Context context;
    private DataLocalityHuManager dataLocalityManager;
    private ConcurrentHashMap<CallbackContext, String> imgMap = new ConcurrentHashMap<CallbackContext, String>();
    private int index;
    private String mFileName = "";
    private String mFileNameArr = "";
    private boolean isFist = true;//第二次打开数据库
    private Thread getArrDataByIDThread;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        context = cordova.getActivity();
        dataLocalityManager = DataLocalityHuManager.getInstance(context, "mc.db");
        LogUtils.e("huanghu", "初始化几次。。。。。。");
    }

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Log.i("huang", "--------------------------:" + action);
        if ("getArr".equals(action)) {//从数据库取出数组
            String type = "";
            int pageNum = 0;
            int dataNum = 0;
            String descAndAsc = "asc";
            // Log.i("huang", "--------------------------length:" + args.length());
            for (int i = 0; i < args.length(); i++) {
                LogUtils.e("huang", "======" + args.get(i));
                Object obj = args.get(i);
                if (obj != null && !obj.toString().equals("null")) {
                    if (i == 0) {
                        type = args.getString(i);
                    } else if (i == 1) {
                        if ("asc".equals(args.getString(i)) || "desc".equals(args.getString(i))) {
                            pageNum = 0;
                        } else {
                            pageNum = args.getInt(i);
                        }
                    } else if (i == 2) {
                        dataNum = args.getInt(i);
                    } else if (i == 3) {
                        descAndAsc = args.getString(i);
                    }
                } else {
                    if (i == 1) {
                        pageNum = 0;
                    } else if (i == 2) {
                        dataNum = 0;
                    } else if (i == 3) {
                        descAndAsc = "asc";
                    }
                }
            }
            LogUtils.e("huang", type + "==" + pageNum + "==" + dataNum + "==" + descAndAsc);
            final String finalType = type;
            final int finalPageNum = pageNum;
            final int finalDataNum = dataNum;
            final String finalDescAndAsc = descAndAsc;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        dataLocalityManager.openSQLData();
                        JSONArray arrayData = dataLocalityManager.getArrayData(finalType, finalPageNum, finalDataNum, finalDescAndAsc);
                        LogUtils.e("huang", "取出数据：" + arrayData.toString());
                        if (0 < arrayData.length()) {
                            callbackContext.success(arrayData);
                        } else {
                            callbackContext.error("error");
                        }
                    } catch (JSONException e) {
                        LogUtils.e("huang", e.toString());
                    }
                }
            }).start();
            return true;
        } else if ("setArr".equals(action)) {//存入数组到数据库中
            final String type = args.getString(0);
            final JSONArray arr = args.getJSONArray(1);
            LogUtils.e("huang", "表明：" + type + "===数组为：" + arr.toString());
            dataLocalityManager.openSQLData();
            dataLocalityManager.createArrayTableSql(type);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < arr.length(); i++) {
                            String jsonObject = arr.getString(i);
                            JSONObject json = new JSONObject(jsonObject);
                            if (json.has("imgarr")) {
                                //数组中存在图片
                                // existenceImages(callbackContext, json, i+"", type);
                            } else {
                                dataLocalityManager.insertArrayData(type, i + 1 + "", jsonObject);
                                dataLocalityManager.openSQLData();
                            }
                        }
                    } catch (JSONException e) {
                        LogUtils.e("huang", e.toString());
                    }
                }
            }).start();
            return true;
        } else if ("getMap".equals(action)) {//从数据库中取map集合
            final String key = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createTableSql();
                        String data = dataLocalityManager.getData(key);
                        JSONObject json = new JSONObject();
                        // JSONObject jsonObject = new JSONObject(data);
                        json.put("f_key", key);
                        //json.put("f_value", jsonObject);
                        json.put("f_value", data);
                        LogUtils.e("huang", "+++++++key:" + key);
                        LogUtils.e("huang", "+++++++data:" + data);
                        LogUtils.e("huang", "+++++++data===key:" + data);
                        if (data != null) {
                            callbackContext.success(json);
                        } else {
                            callbackContext.error("error");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        } else if ("setMap".equals(action)) {//将map集合存入数据库中
            final String key = args.getString(0);
            final String jsonObject = args.getString(1);
            LogUtils.e("huang", "=========key:" + key + "=====value:" + value);

            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createTableSql();
                        dataLocalityManager.insertData(key, jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;

        } else if ("getImages".equals(action)) {//缓存图片
            index++;
            LogUtils.e("huang", index + "");
            String imageUrl = args.getString(0);
            String type;
            if (args.isNull(1)) {
                type = "other";
            } else {
                type = args.getString(1);
            }
            if (imageUrl == null || imageUrl.equals("")) {
                callbackContext.error("url is null");
                return true;
            }

            if (imageUrl.length() < 4 || !imageUrl.substring(0, 4).equals("http")) {
                callbackContext.error("unexpected url:" + imageUrl);
                return true;
            }
            String fileName = EncodeUtil.MD5Encode(imageUrl);//将图片转换为MD5，原因可以判断是否是同一张图片
            File file = new File(cordova.getActivity().getFilesDir() + "/cacheimage/" + type + File.separator + fileName + ".jpg");
            LogUtils.e("huanghu", file.toString());

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists() && file.length() > 0) {  //存在就直接返回
                // Log.i("huang", "本地获取！！！！！:" + file.length() + "=--------------" + file.getPath());
                callbackContext.success(file.getPath());
            } else {
                fileName = fileName + ".jpg";
                LogUtils.e("huanghu", "mFileName" + mFileName + "===" + "fileName" + fileName);
                if (mFileName.equals(fileName) && file.exists()) {//判断两次相同的图片，则返回
                    return true;
                } else {//不同图片开始下载
                    LogUtils.e("huanghu", "index==================:" + index);
                    mFileName = fileName;
                    for (Map.Entry<CallbackContext, String> entry : imgMap.entrySet()) {//防止正在下载的图片又下载一次，所以存入map集合中避免再次下载
                        if (entry.getValue().equals(fileName)) {
                            //  LogUtils.e("huang", "存放在载集合......" + fileName);
                            imgMap.put(callbackContext, fileName);
                            //  return true;
                        }
                    }
//                    imgMap.put(callbackContext, fileName);
                    MyRequestCallBack requestCallBack = new MyRequestCallBack(file.getParent(), fileName) {

                        @Override
                        public void inProgress(float progress, long total, int id) {
//                       LogUtils.e("huang","长在下载集合......");
                        }

                        @Override
                        public void onError(Call call, Exception e, int i) {
                            LogUtils.e("huang", "00000:" + e.toString());
                            for (Map.Entry<CallbackContext, String> entry : imgMap.entrySet()) {
                                if (entry.getValue().equals(getCallbackName())) {
                                    entry.getKey().error(e.toString());
                                }
                            }
                        }

                        @Override
                        public void onResponse(File file, int i) {
                            callbackContext.success(file.getPath());
//                            for (Map.Entry<CallbackContext, String> entry : imgMap.entrySet()) {
//                                if (entry.getValue().equals(file.getName())) {
//                                    entry.getKey().success(file.getPath());
//                                    LogUtils.e("huang", "下载成功：" + file.getPath());
//                                }
//                            }
                        }
                    };
                    requestCallBack.setCallbackContext(callbackContext);
                    requestCallBack.setCallbackName(fileName);
                    OkHttpUtils.get().url(imageUrl).build().execute(requestCallBack);//下载自动存放在本地//fileName
                }
            }
            return true;
        } else if ("setArrData".equals(action)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String type = args.getString(0);
                        final JSONArray arr = args.getJSONArray(1);
                        LogUtils.e("huang", "新需求表明：" + type + "===新需求数组为：" + arr.toString() + "数组的长长度： " + arr.length());
                        LogUtils.e("huang", "数组的长长度： " + arr.length());
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createArrayKeyTableSql(type);
                        ArrayList<String> value = new ArrayList<String>();
                        ArrayList<String> f_id = new ArrayList<String>();

                        for (int i = 0; i < arr.length(); i++) {
                            String jsonObject = arr.getString(i);
                            JSONObject json = new JSONObject(jsonObject);
                            String id = json.getString("recommendid");
//                            if (json.has("imgarr")) {
//                                //数组中存在图片
//                                existenceImages(callbackContext, json, id, type, value, f_id);
//                            } else {
                            //  dataLocalityManager.insertIdArrayData(type, id, jsonObject);
                            value.add(jsonObject);
                            f_id.add(id);
                            //     }
                            if (i == arr.length() - 1) {
                                dataLocalityManager.insertIdArrayData(type, value, f_id);
                            }
                        }
                    } catch (Exception e) {
                        LogUtils.e("huang", e.toString());
                    }
                }
            }).start();
            return true;
        } else if ("getArrDataByID".equals(action)) {
            //String[] split = arr.split(",");
            //JSONArray idData = dataLocalityManager.getIdData(type, split);
//            getArrDataByIDThread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
//            getArrDataByIDThread.start();
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String type = args.getString(0);
                        String arr = args.getString(1);
                        //String[] split = arr.split(",");
                        LogUtils.e("huang", "获取新需求表明：" + type + "获取===新需求数组为：" + arr.toString());
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createArrayKeyTableSql(type);
                        //JSONArray idData = dataLocalityManager.getIdData(type, split);
                        JSONArray idData = dataLocalityManager.getIdData(type, arr);
                        if (idData != null) {
                            callbackContext.success(idData);
                            LogUtils.e("huang", "getArrDataByID查询结果为：" + idData.toString());
                        } else {
                            callbackContext.error("error");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.e("huang", e.toString());
                    }
                }
            });
            return true;
        } else if ("getArrData".equals(action)) {
            final String type = args.getString(0);
            final String id = args.getString(1);
            final String num = args.getString(2);
            final String flay = args.getString(3);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        dataLocalityManager.openSQLData();
                        JSONArray idData = dataLocalityManager.getZDiDData(type, id, flay, num);
                        LogUtils.e("huang", idData.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        } else if ("getCacheSize".equals(action)) {
            //获取数据库的文件大小
            String pathDatabase = context.getDatabasePath("mc.db").getPath();
            LogUtils.e("huang", "数据库的路径=======" + pathDatabase);
            //获取图片文件大小
            File imageFile = new File(cordova.getActivity().getFilesDir() + "/cacheimage/" + "other");
            File file = new File(pathDatabase);
            double databaseSize = FileUtil.getDirSize(file);
            double imageFileSize = FileUtil.getDirSize(imageFile);
            double databaseSizes = Double.valueOf(FileUtil.stringFormat(databaseSize));
            double imageFileSizes = Double.valueOf(FileUtil.stringFormat(imageFileSize));
            double zong = databaseSizes + imageFileSizes;
            LogUtils.e("huang", "图片大小：" + imageFileSize + "====数据库大小" + databaseSize + "==总：" + zong);
            callbackContext.success(zong + "");
            return true;
        } else if ("deleteData".equals(action)) {
            String deleteAll = "";
            String day = args.getString(0);
            LogUtils.e("huang", "删除");
            long time = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            // long time = System.currentTimeMillis();
            String timeStr = TimeUtil.stringToDate(time);//当下时间
            if ("-1".equals(day)) {
                //删除所有的
                deleteAll = "all";
                dataLocalityManager.openSQLData();
                dataLocalityManager.deleteData("2016-01-10 23:59:59", deleteAll, timeStr);
                final String finalDeleteAll = deleteAll;
                cordova.getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        TimeUtil.getImagePathFromSD(cordova, "2016-01-10", finalDeleteAll);
                    }
                });
            } else {
                deleteAll = "part";
                final String oldDate = TimeUtil.getOldDate(-Integer.valueOf(day));//获取几天前的时间日期
                dataLocalityManager.openSQLData();
                dataLocalityManager.deleteData(oldDate, deleteAll, timeStr);
                final String finalDeleteAll = deleteAll;
                cordova.getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        TimeUtil.getImagePathFromSD(cordova, oldDate, finalDeleteAll);
                    }
                });
            }
            return true;
        } else if ("getArrAllData".equals(action)) {
            LogUtils.e("huang", "根据key查询数据:" + args.getString(0) + "==:" + args.getString(1));
            final String key = args.getString(0);
            final String sort = args.getString(1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createArrayKeyTableSql(key);
                        JSONArray keyAllData = dataLocalityManager.getKeyAllData(key, sort);
//                        for(int i = 0; i < keyAllData.length(); i++) {
//                            JSONObject jsonObject = keyAllData.getJSONObject(i);
//                            if("1151_gtb_recommend_list".equals(key)) {
//                                LogUtils.e("huanghu",jsonObject.getString("recommendid"));
//                            }
//                        }
                        LogUtils.e("huang", "getArrAllData获取的数组长度：：" + keyAllData.length());
                        LogUtils.e("huang", "getArrAllData获取的数组：" + keyAllData);
                        if (keyAllData != null) {
                            callbackContext.success(keyAllData);
                        } else {
                            callbackContext.error("error");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.e("huang", "错误信息：" + e.toString());
                    }
                }
            }).start();
            return true;
        } else if ("deleteDataByKey".equals(action)) {

            final String key = args.getString(0);
            LogUtils.e("huang", "根据key删除数据====" + key);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        dataLocalityManager.openSQLData();
                        dataLocalityManager.createArrayKeyTableSql(key);
                        dataLocalityManager.deleteKeydata(key);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.e("huang", e.toString());
                    }
                }
            }).start();

            return true;
        }
        return false;
    }

    //数据库中存放数组中有图片的处理
    private String existenceImages(CallbackContext callbackContext, JSONObject jsonStr, String id, String type, ArrayList<String> value, ArrayList<String> f_id) throws JSONException {
        //    String image = jsonStr.getString("imgarr");
        //  String[] imageArray = image.split(",");
        //   if ("[".equals(image.substring(0, 1))) {
        JSONArray imageArray = jsonStr.getJSONArray("imgarr");
        // String jsonString = "";
        JSONArray jsonArray = new JSONArray();
        for (int j = 0; j < imageArray.length(); j++) {

            String imageArrPath = imageArray.getString(j);
            String imagePath = setImagesMessageData(callbackContext, imageArrPath, "other");
            jsonArray.put(imagePath);

//            if (j == imageArray.length - 1) {
//                jsonString += imagePath;
//            } else {
//                jsonString += imagePath + ",";
//            }
        }
        //jsonStr.put("imgarr", jsonString);
        jsonStr.put("imgarr", jsonArray);
        value.add(jsonStr.toString());
        f_id.add(id);
        //  dataLocalityManager.openSQLData();
        //  dataLocalityManager.insertIdArrayData(type, id + "", jsonStr.toString());
        //    }

//        else {
//            String imagePath = setImagesMessageData(callbackContext, image, "other");
//            jsonStr.put("image", imagePath);
//            dataLocalityManager.insertArrayData(type, i + 1 + "", jsonStr.toString());
//            dataLocalityManager.openSQLData();
//        }
        return null;
    }

    //存放图片过程
    private String setImagesMessageData(CallbackContext callbackContext, String imageUrl, String type) {
        if (imageUrl.length() < 4 || !imageUrl.substring(0, 4).equals("http")) {
            return "unexpected url:" + imageUrl;
        }
        String fileName = EncodeUtil.MD5Encode(imageUrl);//将图片转换为MD5，原因可以判断是否是同一张图片
        File file = new File(cordova.getActivity().getFilesDir() + "/cacheimage/" + type + File.separator + fileName + ".jpg");
        LogUtils.e("huanghu", file.toString());

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists() && file.length() > 0) {  //存在就直接返回
            // Log.i("huang", "本地获取！！！！！:" + file.length() + "=--------------" + file.getPath());
            Date date = new Date(file.lastModified());//文件最后修改时间
            LogUtils.e("huang", "获取图片的属性：" + date);
            return file.getPath();
        } else {
            fileName = fileName + ".jpg";
            LogUtils.e("huanghu", "mFileName" + mFileNameArr + "===" + "fileName" + fileName);
            if (mFileNameArr.equals(fileName) && file.exists()) {//判断两次相同的图片，则返回
                return file.getPath();
            } else {//不同图片开始下载
                LogUtils.e("huanghu", "index==================:" + index);
                mFileNameArr = fileName;
                imgMap.put(callbackContext, fileName);
                MyRequestCallBack requestCallBack = new MyRequestCallBack(file.getParent(), fileName) {

                    @Override
                    public void inProgress(float progress, long total, int id) {
//                       LogUtils.e("huang","长在下载集合......");
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {
                        LogUtils.e("huang", "00000:" + e.toString());
                        for (Map.Entry<CallbackContext, String> entry : imgMap.entrySet()) {
                            if (entry.getValue().equals(getCallbackName())) {

                            }
                        }
                    }

                    @Override
                    public void onResponse(File file, int i) {
                        for (Map.Entry<CallbackContext, String> entry : imgMap.entrySet()) {
                            if (entry.getValue().equals(file.getName())) {
                                LogUtils.e("huang", "下载成功：" + file.getPath());
                            }
                        }
                    }
                };
                requestCallBack.setCallbackContext(callbackContext);
                requestCallBack.setCallbackName(fileName);
                OkHttpUtils.get().url(imageUrl).build().execute(requestCallBack);//下载自动存放在本地//fileName
            }
            return file.getPath();
        }
    }
}
