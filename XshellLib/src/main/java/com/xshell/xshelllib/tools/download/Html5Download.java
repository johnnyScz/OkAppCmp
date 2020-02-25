//package com.xshell.xshelllib.tools.download;
//
//import android.app.Activity;
//import android.app.ActivityManager;
//import android.app.ActivityManager.RunningAppProcessInfo;
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.content.Intent;
//import android.util.Log;
//
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest;
//import com.lidroid.xutils.util.LogUtils;
//import com.xshell.xshelllib.application.AppConfig;
//import com.xshell.xshelllib.application.AppConstants;
//import com.xshell.xshelllib.application.AppContext;
//import com.xshell.xshelllib.utils.FileUtil;
//import com.xshell.xshelllib.utils.ParseConfig;
//import com.xshell.xshelllib.utils.PreferenceXshellUtil;
//import com.xshell.xshelllib.utils.TimeUtil;
//import com.xshell.xshelllib.utils.VersionUtil;
//import com.xshell.xshelllib.utils.Write2SDCard;
//import com.xshell.xshelllib.utils.ZIPUtils;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//public class Html5Download {
//
//    private HttpUtils http = new HttpUtils();
//    /**
//     * 解析的配置文件集合
//     */
//    private Map<String, String> configInfo;
//
//    private Context context;
//    private Activity activity;
//
//    /**
//     * 检查html5更新
//     */
//    public void checkH5(Context context, Activity activity) {
//        this.context = context;
//        this.activity = activity;
//        configInfo = ParseConfig.getInstance(context).getConfigInfo();
//        String url = configInfo.get("html_url_list") + PreferenceXshellUtil.getInstance().getFileUpdateTime()
//                + "&platform=Android" + "&xshllversion=" + VersionUtil.getVersionCode(context);
//        //Log.i("zzy","前台更新url:"+url);
//        http.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
//
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                try {
//                    if (AppConfig.DEBUG) {
//                        Log.i("zzy", "前台开始检查html5:" + responseInfo);
//                    }
//
//                    if (AppConfig.WIRTE_SDCARD)
//                        Write2SDCard.getInstance().writeMsg("前台开始检查html5  qiantai start update");
//                    toJsonFile(responseInfo.result);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(HttpException arg0, String arg1) {
//                if (AppConfig.DEBUG)
//                    Log.i("zzy", "前台检查html5失败:");
//            }
//        });
//    }
//
//    /**
//     * 下载html5
//     *
//     * @param res json的字符串
//     * @throws JSONException
//     */
//    private void toJsonFile(String res) throws JSONException {
//        JSONObject json = new JSONObject(res);
//        JSONObject op = json.getJSONObject("op");
//        String code = op.getString("code");
//        int count = json.getInt("count");
//        final String changezip = json.getString("changezip");
//        Log.i("zzy", "changezip:" + changezip);
//
//        JSONArray array = json.getJSONArray("changelist");
//        for (int i = 0; i < array.length(); i++) {
//            JSONObject html = array.getJSONObject(i);
//            String path = html.getString("path");
//            String status = html.getString("status");
//            if ("DELETE".equals(status)) {  //找到带有删除状态的并且删除
//                File file = new File(activity.getFilesDir() + path.replaceAll("\\\\","/"));
//                if (file.exists()) {
//                    file.delete();
//                }
//            }
//        }
//
//
//        if (code.equals("Y")) {// 检查更新成功
//            if (count != 0) {// 有更新
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        LogUtils.d("changeFileDao.addItem" + TimeUtil.now());
//                        zipUpdateH5(changezip);
//                    }
//                }).start();
//            } else {// 没有更新
//                if (AppConfig.DEBUG)
//                    Log.i("zzy", "前台检测没有更新");
//                // 没有更新上次检查时间也改变
//                PreferenceXshellUtil.getInstance().setDownloadingFile(false);
//                if (AppConfig.WIRTE_SDCARD)
//                    Write2SDCard.getInstance().writeMsg("前台检测没有更新  qiantai no update");
//            }
//        } else {
//            if (AppConfig.DEBUG)
//                Log.i("zzy", "数据异常！");
//        }
//    }
//
//    /**
//     * 用zip的方式更新html5
//     *
//     * @param changezip zip的名字
//     */
//    private void zipUpdateH5(String changezip) {
//        final String time;
//        if (null == changezip || "".equals(changezip)) {  //代表只删除了文件，没有其他文件下载
//            time = PreferenceXshellUtil.getInstance().getFileUpdateTime(); //就保存原先的
//        } else {
//            time = changezip.split("-")[1].split("\\.")[0];
//            changezip = changezip.replaceAll("\\\\", "/");
//            if (changezip.startsWith("/")) {
//                changezip = changezip.substring(1);
//            }
//        }
//
//
//        HttpUtils http = new HttpUtils();
//        PreferenceXshellUtil.getInstance().setDownAppDir(FileUtil.getInstance().getFilePathInSDCard(AppConstants.XINYUSOFT_CACHE, AppConstants.APP_APK_NAME));
//        final String file = FileUtil.getInstance().getFilePathInSDCard(AppConstants.XINYUSOFT_CACHE, "backgroundupdatehtml5.zip");
//        final File mfile = new File(file);
//        // 存在的话，就直接用下载好的。
//        if (mfile.exists()) {
//            showDialog(time, file, mfile);
//        } else {
//            String url = configInfo.get("html_url_download") + changezip;
//            http.download(url, file, false, true, new RequestCallBack<File>() {
//
//                @Override
//                public void onSuccess(ResponseInfo<File> responseInfo) {
//                    showDialog(time, file, mfile);
//                }
//
//                @Override
//                public void onFailure(HttpException arg0, String arg1) {
//                    if (mfile.exists())
//                        mfile.delete();
//                }
//
//            });
//        }
//    }
//
//    private void showDialog(final String time, final String file, final File mfile) {
//
//
//        activity.runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                Builder builder = new Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                builder.setMessage("新版本已经发布！");
//                builder.setTitle("更新提示");
//                builder.setCancelable(false);
//                builder.setPositiveButton("立即更新", new OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        unZip(time, file, mfile);
//                        if (mfile.exists())
//                            mfile.delete();
//                        dialog.dismiss();
//                    }
//
//                });
//                builder.setNegativeButton("下次再说", new OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.create().show();
//                PreferenceXshellUtil.getInstance().setDownloadingFile(false);
//
//            }
//        });
//    }
//
//    private void unZip(final String time, final String file, final File mfile) {
//        try {
//            ZIPUtils.unzip(file, AppContext.CONTEXT.getFilesDir().getAbsolutePath());
//            PreferenceXshellUtil.getInstance().setFileUpdateTime(time);// 设置上次更新时间（设置最新版本号）
//            PreferenceXshellUtil.getInstance().setDownloadingFile(false);// 文件下载中状态
//            if (AppConfig.DEBUG)
//                Log.e("zzy", "!!!!!!!!! 前台HTML5DOWNLOAD解压成功了！！！:");
//            if (AppConfig.WIRTE_SDCARD)
//                Write2SDCard.getInstance().writeMsg("!!!!!!!!! qiantai HTML5DOWNLOAD解压成功了！！！:");
//            PreferenceXshellUtil.getInstance().setBackgroundUpdateFile(true);
//            Intent intent = new Intent(AppConstants.RELOAD_HOME_PAGE);
//            activity.sendBroadcast(intent);
//            if (isAppOnForeground()) {
//                Intent intent1 = new Intent(AppConstants.RELOAD_NEW_BROWSER_PAGE);
//                activity.sendBroadcast(intent1);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            //出现异常也要删除
//            if (mfile.exists())
//                mfile.delete();
//
//            if (AppConfig.WIRTE_SDCARD)
//                Write2SDCard.getInstance().writeMsg("unzip error:" + e.getMessage());
//        }
//    }
//
//
//    /**
//     * 用来判断是否有我们开的第2个进程
//     *
//     * @return
//     */
//    public boolean isAppOnForeground() {
//
//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        String packageName = context.getPackageName();
//
//        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
//        if (appProcesses == null)
//            return false;
//
//        for (RunningAppProcessInfo appProcess : appProcesses) {
//            if (appProcess.processName.equals(packageName + ":xinyu_remote")) {
//                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                    return true;
//                }
//
//            }
//        }
//
//        return false;
//    }
//}
