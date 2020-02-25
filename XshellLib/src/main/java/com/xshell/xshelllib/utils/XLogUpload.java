//package com.xshell.xshelllib.utils;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.xshell.xshelllib.application.AppConstants;
//import com.xshell.xshelllib.greendao.GreenManager;
//import com.xshell.xshelllib.greendao.XLogUploadCallback;
//import com.xshell.xshelllib.greendao.bean.XLog;
//import com.xshell.xshelllib.greendao.dao.XLogDao;
//import com.zhy.http.okhttp.OkHttpUtils;
//
//import org.json.JSONArray;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.Call;
//
///**
// * Created by zzy on 2016/10/31.
// * XLog上传
// */
//public class XLogUpload {
//
//    private static XLogUpload xLogUpload;
//    private static final String TAG = "XLogUpload";
//    private Context context;
//    private int maxUpload = 20;
//
//    private XLogUpload(Context context) {
//        this.context = context;
//    }
//
//    public static XLogUpload getInstance(Context context) {
//        if (xLogUpload == null) {
//            synchronized (TAG) {
//                if (xLogUpload == null) {
//                    xLogUpload = new XLogUpload(context.getApplicationContext());
//                }
//            }
//        }
//        return xLogUpload;
//    }
//
//
//    /**
//     * XLog发送
//     */
//    public void uploadXLog() {
//        Log.i("zzy", "开始上传:");
//        //1.查询数据库所有的数据
//        final XLogDao xLogDao = GreenManager.getInstance(context).getXLogDao();
//        List<XLog> list = xLogDao.queryBuilder().list();
//        //2.上传
//        JSONArray array = new JSONArray();
//        final int size = list.size();
//        Log.i("zzy", "size:" + size);
//
//        if (size == 0) { //等于0代表没有数据，跳过上传
//            return;
//        } else {
//            if (size <= maxUpload && size > 0) {  //小于最大上传数直接上传
//                for (int i = 0; i < size; i++) {
//                    array.put(list.get(i).getData());
//                }
//                Log.i("zzy", "array:" + array);
//                XLogUploadCallback call = new XLogUploadCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int i) {
//                    }
//
//                    @Override
//                    public void onResponse(String s, int i) {
//                        String temp = s.replaceAll("\\r\\n", "");
//                        Log.i("zzy", "小于20条直接上传成功:" + temp);
//                        //3.删除
//                        for (int j = 0; j < getSize(); j++) {
//                            xLogDao.deleteByKey(getxLogList().get(j).getId());
//                        }
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
//                    }
//                };
//                call.setSize(size);
//                call.setxLogList(list);
//                OkHttpUtils.post().addParams("log", String.valueOf(array)).url(AppConstants.XLOG_URL).build().execute(call);
//            } else {  //大于20条需要分批上传
//                int count = size / maxUpload;
//                int remainder = size % maxUpload;
//                for (int i = 1; i <= count; i++) {
//                    JSONArray tempArray = new JSONArray();
//                    final List<XLog> tempList = new ArrayList<>();
//                    for (int j = (i - 1) * maxUpload; j < i * maxUpload; j++) {
//                        tempArray.put(list.get(j).getData());
//                        tempList.add(list.get(j));
//                    }
//
//                    Log.i("zzy", "连续的uer----:" + tempArray);
//                    XLogUploadCallback callback = new XLogUploadCallback() {
//                        @Override
//                        public void onError(Call call, Exception e, int i) {
//
//                        }
//
//                        @Override
//                        public void onResponse(String s, int i) {
//                            Log.i("zzy", "连续的---------------------------:" + s.replaceAll("\\r\\n", ""));
//                            List<XLog> xLogList = getxLogList();
//                            for (int j = 0; j < xLogList.size(); j++) {
//                                xLogDao.deleteByKey(xLogList.get(j).getId());
//                            }
//                        }
//                    };
//                    callback.setxLogList(tempList);
//                    OkHttpUtils.post().addParams("log", String.valueOf(tempArray)).url(AppConstants.XLOG_URL).build().execute(callback);
//                }
//                //上传余数的条数
//                if (remainder > 0) {
//                    JSONArray tempArray = new JSONArray();
//                    final List<XLog> tempList = new ArrayList<XLog>();
//                    for (int i = count * maxUpload; i < size; i++) {
//                        tempArray.put(list.get(i).getData());
//                        tempList.add(list.get(i));
//                    }
//
//                    Log.i("zzy", "剩余的----:" + tempArray);
//                    XLogUploadCallback callback = new XLogUploadCallback() {
//                        @Override
//                        public void onError(Call call, Exception e, int i) {
//
//                        }
//
//                        @Override
//                        public void onResponse(String s, int i) {
//                            Log.e("zzy", "最后的:" + s.replaceAll("\\r\\n", ""));
//                            List<XLog> xLogList = getxLogList();
//                            for (int j = 0; j < xLogList.size(); j++) {
//                                xLogDao.deleteByKey(xLogList.get(j).getId());
//                            }
//                        }
//                    };
//                    callback.setxLogList(tempList);
//                    OkHttpUtils.post().addParams("log", String.valueOf(tempArray)).url(AppConstants.XLOG_URL).build().execute(callback);
//                }
//            }
//        }
//    }
//
//
//
//    //App登入与登出统计
//    public void getAppLoginAndExit(int session, int turnover){
//        XLogUploadCallback callback = new XLogUploadCallback() {
//            @Override
//            public void onError(Call call, Exception e, int i) {
//                Log.e("huang", "App登入与登出统计onError=========:" + e.toString() + "====:" + i + "++++:" + call);
//            }
//
//            @Override
//            public void onResponse(String s, int i) {
//
//            }
//        };
//        OkHttpUtils.post()
//                .addParams("session_id", String.valueOf(session))
//                .addParams("turnover_flag", String.valueOf(turnover))//0是登入，1是退出
//                .url(AppConstants.APP_LOGIN_URL).build().execute(callback);
//    }
//}
