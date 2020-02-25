//package com.xshell.xshelllib.utils;
//
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.util.Log;
//
//import com.xshell.xshelllib.application.AppConstants;
//import com.xshell.xshelllib.greendao.XLogUploadCallback;
//
//import com.xshell.xshelllib.sqlite.DataLocalityManager;
//import com.zhy.http.okhttp.OkHttpUtils;
//
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//
//import java.net.URLDecoder;
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.Call;
//import okhttp3.MediaType;
//
///**
// * Created by huang on 2017/8/17.
// * XMessageUpload上传
// */
//public class XMessageUpload {
//
//    private static XMessageUpload xMessageUpload;
//    private static final String TAG = "XMessageUpload";
//    private Context context;
//    private int maxUpload = 10;
//    private DataLocalityManager dataLocalityManager;
//    private String appName;
//    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//
//    private XMessageUpload(Context context) {
//        this.context = context;
//        dataLocalityManager = DataLocalityManager.getInstance(context, "dataLocal");
//        dataLocalityManager.openDatabase();
//        dataLocalityManager.createTableMessageSql();
//        //获取当下项目名字
//        PackageManager pm = context.getPackageManager();
//        try {
//            appName = context.getApplicationInfo().loadLabel(pm).toString();
//            //    appName = new String(appName.getBytes("UTF-8"), "UTF-8");
////            appName = URLDecoder.decode(appName, "UTF-8");
////            appName =new String(appName.getBytes(),"UTF-8");
//            appName = "android";
//            Log.e("huang", "编码：" + appName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static XMessageUpload getInstance(Context context) {
//        if (xMessageUpload == null) {
//            synchronized (TAG) {
//                if (xMessageUpload == null) {
//                    xMessageUpload = new XMessageUpload(context.getApplicationContext());
//                }
//            }
//        }
//        return xMessageUpload;
//    }
//
//
//    /**
//     * XLog发送
//     */
//    public void uploadXMessage() throws JSONException {
//        Log.e("huang", "开始上传:");
//        //1.查询数据库所有的数据
//        //  dataLocalityManager.openSQLData();
//        dataLocalityManager.openDatabase();
//        final JSONArray array = dataLocalityManager.getMessageData();
//        dataLocalityManager.closeDatabase();
//        //2.上传
//        final int size = array.length();
//        Log.e("huang", "size:" + size + "：=appname:" + appName);
//
//        if (size == 0) { //等于0代表没有数据，跳过上传
//            return;
//        } else {
//
//            if (size <= maxUpload && size > 0) {  //小于最大上传数直接上传
//                Log.e("huang", "array:" + array.toString());
//                StringBuffer stringBuffer1 = new StringBuffer();
//                for (int i = 0; i < size; i++) {
//                    if (i == size - 1) {
//                        stringBuffer1.append(array.get(i).toString());
//                    } else {
//                        stringBuffer1.append(array.get(i).toString() + ";");
//                    }
//                }
//
//                XLogUploadCallback call = new XLogUploadCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int i) {
//                        //     Log.e("huang", "小于10条直接上传onError=========:" + e.toString() + "====:" + i + "++++:" + call);
//                    }
//
//                    @Override
//                    public void onResponse(final String s, int i) {
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                String temp = s.replaceAll("\\r\\n", "");
//                                //    Log.e("huang", "小于10条连续的=========:" + s);
//                                dataLocalityManager.openDatabase();
//                                try {
//                                    JSONObject jsonObject = new JSONObject(s);
//                                    String code = jsonObject.getJSONObject("op").getString("code");
//                                    if ("Y".equals(code)) {
//                                        //3.删除
//                                        for (int j = 0; j < size; j++) {
//                                            dataLocalityManager.deleteMessageById(array.get(j).toString());
//                                        }
//                                        dataLocalityManager.closeDatabase();
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }).start();
//                    }
//
////
////                        new Handler().postDelayed(new Runnable() {
////                            @Override
////                            public void run() {
////                                AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
////                                builder.setMessage("上传条数:" + size);
////                                builder.setTitle("提示");
////                                builder.setCancelable(false);
////                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
////                                    @Override
////                                    public void onClick(DialogInterface dialog, int which) {
////                                        dialog.dismiss();
////                                     }
////                                });
////                                builder.create().show();
////                            }
////                        }, 2000);
//                };
//                OkHttpUtils.post()
//                        .addParams("system", appName)
//                        .addParams("operate_content", stringBuffer1.toString())
//                        .addParams("timestamp", System.currentTimeMillis() + "")
//                        .addParams("channel", "androidapp")
//                        .url(AppConstants.XMessage_URL).build().execute(call);
//                //  Log.e("huang", "system:"+appName+" operate_content:"+stringBuffer1.toString()+"  timestamp:"+System.currentTimeMillis()+"  AppConstants.XMessage_URL:"+ AppConstants.XMessage_URL);
//            } else {  //大于10条需要分批上传
//                //  Log.e("huang", "大于10条需要分批上传array:" + array.toString());
//                int count = size / maxUpload;
//                int remainder = size % maxUpload;
//                for (int i = 1; i <= count; i++) {
//                    StringBuffer stringBuffer = new StringBuffer();
//                    final List<String> tempList = new ArrayList<>();
//                    for (int j = (i - 1) * maxUpload; j < i * maxUpload; j++) {
//                        //tempArray.put(array.get(j));
//                        if (j == i * maxUpload - 1) {
//                            stringBuffer.append(array.get(j).toString());
//                        } else {
//                            stringBuffer.append(array.get(j).toString() + ";");
//                        }
//                        tempList.add(array.get(j).toString());
//                    }
//                    //   Log.e("huang", "连续的uer----:" + stringBuffer);
//
//                    XLogUploadCallback callback = new XLogUploadCallback() {
//                        @Override
//                        public void onError(Call call, Exception e, int i) {
//                            //  Log.e("huang", "大于10条需要分批上传onError=========:" + e.toString() + "====:" + i + "++++:" + call);
//                        }
//
//                        @Override
//                        public void onResponse(final String s, int i) {
//                            Log.e("huang", "连续的---------------------------:" + s.replaceAll("\\r\\n", ""));
//                            //    Log.e("huang", "连续的=========:" + s);
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        dataLocalityManager.openDatabase();
//                                        JSONObject jsonObject = new JSONObject(s);
//                                        String code = jsonObject.getJSONObject("op").getString("code");
//                                        if ("Y".equals(code)) {
//                                            //3.删除
//                                            for (int j = 0; j < tempList.size(); j++) {
//                                                dataLocalityManager.deleteMessageById(tempList.get(j).toString());
//                                            }
//                                            dataLocalityManager.closeDatabase();
//                                        }
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }).start();
//                        }
//                    };
//                    OkHttpUtils.post()
//                            .addParams("system", appName)
//                            .addParams("operate_content", stringBuffer.toString())
//                            .addParams("timestamp", System.currentTimeMillis() + "")
//                            .addParams("channel", "androidapp")
//                            .url(AppConstants.XMessage_URL).build().execute(callback);
//                    //    Log.e("huang", "system:"+appName+" operate_content:"+stringBuffer.toString()+"  timestamp:"+System.currentTimeMillis()+"  AppConstants.XMessage_URL:"+ AppConstants.XMessage_URL);
//                }
//                //上传余数的条数
//                if (remainder > 0) {
//                    // JSONArray tempArray = new JSONArray();
//                    StringBuffer stringBuffer = new StringBuffer();
//                    final List<String> tempList = new ArrayList<String>();
//                    for (int i = count * maxUpload; i < size; i++) {
//                        //  tempArray.put(array.get(i));
//                        if (i == size - 1) {
//                            stringBuffer.append(array.get(i).toString());
//                        } else {
//                            stringBuffer.append(array.get(i).toString() + ";");
//                        }
//                        tempList.add(array.get(i).toString());
//                    }
//
//                    Log.e("huang", "剩余的----:" + stringBuffer);
//                    XLogUploadCallback callback = new XLogUploadCallback() {
//                        @Override
//                        public void onError(Call call, Exception e, int i) {
//                            //    Log.e("huang", "上传余数的条数onError=========:" + e.toString() + "====:" + i + "++++:" + call);
//                        }
//
//                        @Override
//                        public void onResponse(final String s, int i) {
//                            Log.e("huang", "最后的:" + s.replaceAll("\\r\\n", ""));
//                            //  Log.e("huang", "最后的:=============" + s);
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        dataLocalityManager.openDatabase();
//                                        JSONObject jsonObject = new JSONObject(s);
//                                        String code = jsonObject.getJSONObject("op").getString("code");
//                                        if ("Y".equals(code)) {
//                                            for (int j = 0; j < tempList.size(); j++) {
//                                                dataLocalityManager.deleteMessageById(tempList.get(j).toString());
//                                            }
//                                            dataLocalityManager.closeDatabase();
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }).start();
//                        }
//                    };
//                    OkHttpUtils.post()
//                            .addParams("system", appName)
//                            .addParams("operate_content", stringBuffer.toString())
//                            .addParams("timestamp", System.currentTimeMillis() + "")
//                            .addParams("channel", "androidapp")
//                            .url(AppConstants.XMessage_URL).build().execute(callback);
//                    // Log.e("huang", "system:"+appName+" operate_content:"+stringBuffer.toString()+"  timestamp:"+System.currentTimeMillis()+"  AppConstants.XMessage_URL:"+ AppConstants.XMessage_URL);
//                }
//            }
//        }
//    }
//}
