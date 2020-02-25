//package com.xshell.xshelllib.plugin;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.xshell.xshelllib.application.AppConstants;
//import com.xshell.xshelllib.greendao.GreenManager;
//import com.xshell.xshelllib.greendao.XLogUploadCallback;
//import com.xshell.xshelllib.greendao.bean.XLog;
//import com.xshell.xshelllib.greendao.dao.XLogDao;
//import com.xshell.xshelllib.logutil.LogUtils;
//import com.xshell.xshelllib.utils.TimeUtil;
//import com.zhy.http.okhttp.OkHttpUtils;
//
//import org.json.JSONArray;
//
//import java.io.File;
//import java.text.DateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
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
//                            LogUtils.e("zzy", "最后的:" + s.replaceAll("\\r\\n", ""));
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
//    //App登入与登出统计
//    public void getAppLoginAndExit(String session, int turnover) {
//        XLogUploadCallback callback = new XLogUploadCallback() {
//            @Override
//            public void onError(Call call, Exception e, int i) {
//                LogUtils.e("zzy", "App登入与登出统计onError=========:" + e.toString() + "====:" + i + "++++:" + call);
//            }
//
//            @Override
//            public void onResponse(String s, int i) {
//                LogUtils.e("zzy", "App登入与登出统计onResponse=========:" + s + "====:" + i);
//
//            }
//        };
//        OkHttpUtils.post()
//                .addParams("session_id", session)
//                .addParams("turnover_flag", String.valueOf(turnover))//0是登入，1是退出
//                .url(AppConstants.APP_LOGIN_URL).build().execute(callback);
//
//
//    }
//
//
//    /**
//     * 根据时间图片删除
//     *
//     * @param day             //天数
//     * @param finalDeleteType //删除的方式
//     */
//    public void DeletingPicturesTime(String day, String finalDeleteType) {
//        final String oldDate = TimeUtil.getOldDate(-Integer.valueOf(day));//获取几天前的时间日期
//        getImagePathFromSD(context, oldDate, finalDeleteType);
//    }
//
//    /**
//     * 图片删除处理
//     *
//     * @param context
//     * @param oldDate        //根据时间删除图片
//     * @param finalDeleteAll //删除的方式(all--所有数据)
//     */
//    public void getImagePathFromSD(Context context, String oldDate, String finalDeleteAll) {
//        // 图片列表
////        List<String> imagePathList = new ArrayList<String>();
//        //image文件夹的路径   File.separator(/)
//        String filePath = context.getFilesDir() + "/cacheimage/" + "other";
//        // 得到该路径文件夹下所有的文件
//        File fileAll = new File(filePath);
//        File[] files = fileAll.listFiles();
//        if (files == null) {
//            return;
//        }
//        if (files.length <= 0) {
//            return;
//        }
//        Log.e("huang", "图片的个数：" + files.length);
//        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
//        for (int i = 0; i < files.length; i++) {
//            File file = files[i];
//            Log.e("huang", "图片的路径：" + file.getPath());
//            if ("all".equals(finalDeleteAll)) {
//                //删除文件
//                deleteFile(file);
//            } else {
//                //   if (checkIsImageFile(file.getPath())) {
//                Date dateFile1 = new Date(file.lastModified());//文件最后修改时间
//                DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
//                String str_time = format.format(dateFile1);
//                Log.e("huang", "dateFile：" + dateFile1);
//                Log.e("huang", "str_time：" + str_time);
//                String[] split2 = str_time.split(" ");
//                String s1 = split2[0];
//                String year = s1.substring(0, 4);
//                String[] str1 = s1.split("年");
//                String str2 = str1[1];
//                String month = str2.split("月")[0];
//                String day = str2.split("月")[1].split("日")[0];
//
//                Log.e("huang", "year：" + year + "==month:" + month + "==" + day);
//                // imagePathList.add(file.getPath());
//                Calendar a = Calendar.getInstance();
//                a.set(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));
//                //参数为年 月 日 时 分 秒
//                a.set(Calendar.MILLISECOND, 0);
//                //设置毫秒
//                String[] split1 = oldDate.split("-");
//                Calendar c = Calendar.getInstance();
//                c.set(Integer.valueOf(split1[0]), Integer.valueOf(split1[1]), Integer.valueOf(split1[2]));
//                c.set(Calendar.MILLISECOND, 0);
//                //a比c早,返回-1,
//                //a与c相同,返回0
//                //a比c晚,返回1
//                int retrue = a.compareTo(c);
//                if ("-1".equals(retrue + "")) {
//                    Log.e("huang", "时间的比较：-1");
//                } else if ("0".equals(retrue + "")) {
//                    Log.e("huang", "时间的比较：0");
//                    //删除文件
//                    //deleteFile(file);
//                } else if ("1".equals(retrue + "")) {
//                    Log.e("huang", "时间的比较：1");
//                    //删除文件
//                    deleteFile(file);
//                }
//                //    }
//            }
//        }
//    }
//
//    //删除指定的图片
//    public void deleteFile(File file) {
//        if (file.exists()) {
//            if (file.isFile()) {  //如果是一个文件
//                boolean delete = file.delete();
//                Log.e("huang", "删除文件：" + delete);
//            } else {  //是一个目录
//                File[] files = file.listFiles();
//                for (int i = 0; i < files.length; i++) {
//                    deleteFile(files[i]);
//                }
//                file.delete();
//            }
//        }
//    }
//
//}
