//package com.xshell.xshelllib.plugin;
//
//import android.app.AlertDialog;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.widget.RemoteViews;
//import android.widget.Toast;
//
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest;
//import com.xshell.xshelllib.R;
//import com.xshell.xshelllib.application.AppConfig;
//import com.xshell.xshelllib.application.AppConstants;
//import com.xshell.xshelllib.application.AppContext;
//import com.xshell.xshelllib.utils.FileUtil;
//import com.xshell.xshelllib.utils.ParseConfig;
//import com.xshell.xshelllib.utils.PreferenceXshellUtil;
//import com.xshell.xshelllib.utils.Write2SDCard;
//
//import org.apache.cordova.CallbackContext;
//import org.apache.cordova.CordovaArgs;
//import org.apache.cordova.CordovaInterface;
//import org.apache.cordova.CordovaPlugin;
//import org.apache.cordova.CordovaWebView;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.lang.ref.WeakReference;
//import java.util.Map;
//
///**
// * Created by zzy on 2016/8/15.
// * 检查app是否有更新
// */
//public class CheckUpdateAppPlugin extends CordovaPlugin {
//    CallbackContext callback;
//    private Map<String, String> configInfo;
//    private HttpUtils httpUtils;
//    private MyHandler handler;
//
//    private static final int UPDATING = 1;
//    private static final int FINISH = 2;
//    protected Notification notification;
//    private NotificationManager notificationManager;
//
//    static class MyHandler extends Handler {
//        WeakReference<CheckUpdateAppPlugin> plugin;
//
//        public MyHandler(CheckUpdateAppPlugin appPlugin) {
//            plugin = new WeakReference<>(appPlugin);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            CheckUpdateAppPlugin checkUpdateAppPlugin = plugin.get();
//            switch (msg.what) {
//                case UPDATING:
//                    int len = (int) msg.obj;
//                    checkUpdateAppPlugin.notification.contentView.setTextViewText(R.id.content_view_text1, len + "%");
//                    checkUpdateAppPlugin.notification.contentView.setProgressBar(R.id.content_view_progress, 100, len, false);
//                    checkUpdateAppPlugin.notificationManager.notify(101, checkUpdateAppPlugin.notification);
//                    break;
//                case FINISH:
//                    checkUpdateAppPlugin.notification.contentView.setTextViewText(R.id.content_view_text1, "下载完成");
//
//                    Intent openIntent = new Intent();
//                    openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    openIntent.setAction(Intent.ACTION_VIEW);
//                    Uri uri = Uri.fromFile(new File(PreferenceXshellUtil.getInstance().getDownAppDir()));
//                    openIntent.setDataAndType(uri, "application/vnd.android.package-archive");
//
//                    PendingIntent pendingIntent = PendingIntent.getActivity(AppContext.CONTEXT, 0, openIntent, 0);
//
//                    checkUpdateAppPlugin.notification.contentIntent = pendingIntent;
//
//                    Toast.makeText(checkUpdateAppPlugin.cordova.getActivity(), "下载完成", Toast.LENGTH_SHORT).show();
//                    checkUpdateAppPlugin.notificationManager.notify(101, checkUpdateAppPlugin.notification);
//                    break;
//            }
//        }
//    }
//
//
//    @Override
//    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
//        super.initialize(cordova, webView);
//        handler = new MyHandler(this);
//    }
//
//    @Override
//    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
//        this.callback = callbackContext;
//        if ("checkUpdate".equals(action)) {
//            if (httpUtils == null) {
//                httpUtils = new HttpUtils();
//            }
//            if (configInfo == null) {
//                configInfo = ParseConfig.getInstance(cordova.getActivity()).getConfigInfo();
//            }
//            //获得本地app的更新时间，去请求。
//            String url = configInfo.get("xversion-update-url") + "&appname=" + configInfo.get("xversion-app-name") + "&time=" + PreferenceXshellUtil.getInstance().getAppUpdateTime();
//            Log.i("zzy", "url:" + url);
//            httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
//
//                @Override
//                public void onSuccess(ResponseInfo<String> responseInfo) {
//                    Log.i("zzy", "------0000000000000:");
//                    JSONObject json;
//                    try {
//                        json = new JSONObject(responseInfo.result);
//                        JSONObject op = json.getJSONObject("op");
//                        String code = op.getString("code");
//                        if (code.equals("Y")) {// 检查更新成功
//                            final String changezip = json.getString("changezip");
//                            int count = json.getInt("count");
//                            if (count != 0) {// 有更新
//                                showAppDialog(changezip);
//                            } else {
//                                Toast.makeText(cordova.getActivity(), "当前已是最新版本！", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            //Toast.makeText(LoadingActivity.this, "网络异常，请稍候再试！", Toast.LENGTH_SHORT).show();
//
//                        }
//                    } catch (Exception e) {
//                        Write2SDCard.getInstance().writeMsg("isUpdate error：" + e.toString());
//                        e.printStackTrace();
//                        callback.error(e.toString());
//                    }
//                }
//
//                @Override
//                public void onFailure(HttpException e, String s) {
//                    Log.i("zzy", "-------------:");
//                    callback.error(e.toString());
//                }
//            });
//            return true;
//        }
//
//        return false;
//    }
//
//
//    private void ShowDialog(final String zip) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(cordova.getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//        builder.setMessage("1111");
//        builder.setTitle("更新提示");
//        builder.setCancelable(false);
//        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                downloadApp(zip);
//            }
//        });
//        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//
//            }
//        });
//        builder.create().show();
//    }
//
//
//    /**
//     * 显示更新dialog, 获取更新内容现在是失效的。
//     */
//    protected void showAppDialog(final String zip) throws Exception {
//        // 这个是更新的内容提示
//        String url = configInfo.get("xversion-update-content-url") + "&appname=" + configInfo.get("xversion-app-name");
//        Log.i("zzy", "content:" + url);
//        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
//            @Override
//            public void onFailure(HttpException arg0, String arg1) {
//                Toast.makeText(cordova.getActivity(), "网络异常，请稍候再试！！", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onSuccess(ResponseInfo<String> res) {
//                JSONObject json;
//                try {
//                    json = new JSONObject(res.result);
//                    JSONObject op = json.getJSONObject("op");
//                    String code = op.getString("code");
//                    String content = json.getString("content");
//                    JSONObject contentJson = new JSONObject(content);
//                    content = contentJson.getString("info");
//                    content = content.replaceAll("///", "\n");
//                    if (code.equals("Y")) {// 检查更新成功
//                        AlertDialog.Builder builder = new AlertDialog.Builder(cordova.getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                        builder.setMessage(content);
//                        builder.setTitle("更新提示");
//                        builder.setCancelable(false);
//                        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                downloadApp(zip);
//                            }
//                        });
//                        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//
//                            }
//                        });
//                        builder.create().show();
//                    } else {
//                        Toast.makeText(cordova.getActivity(), "检查失败，请稍候再试！！", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//
//    /**
//     * 下载app
//     */
//    private void downloadApp(final String zipName) {
//
//        //新建一个通知栏，
//        notificationManager = (NotificationManager) cordova.getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//        notification = new Notification();
//        notification.tickerText = "下载进度";
//        notification.icon = R.drawable.ic_launcher;
//        notification.contentView = new RemoteViews(cordova.getActivity().getPackageName(), R.layout.xinyusoft_item_notification_view);
//        notificationManager.notify(101, notification);
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        PreferenceXshellUtil.getInstance().setDownAppDir(FileUtil.getInstance().getFilePathInSDCard(AppConstants.XINYUSOFT_CACHE, AppConstants.APP_APK_NAME));
//        final String file = FileUtil.getInstance().getFilePathInSDCard(AppConstants.XINYUSOFT_CACHE, AppConstants.APP_APK_NAME);
//        String url = configInfo.get("xversion-download-url") + configInfo.get("xversion-app-name") + "/" + configInfo.get("xversion-apk-name");
//        Log.i("zzy", "url +:" + url);
//        httpUtils.download(url, file, false, true, new RequestCallBack<File>() {
//
//            @Override
//            public void onSuccess(ResponseInfo<File> responseInfo) {
//                sendMessage(0, FINISH);
//                // 设置xversion的最新更新的时间
////                String time = zipName.split("-")[1].split("\\.")[0];
////                // 设置应当升级
////                PreferenceXshellUtil.getInstance().setNextToInstall(true);
////                PreferenceXshellUtil.getInstance().setAppUpdateTime(time);
//                openApp();
//                //cordova.getActivity().finish();
//
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                Toast.makeText(cordova.getActivity(), "下载失败，请重试！", Toast.LENGTH_SHORT).show();
//                if (AppConfig.WIRTE_SDCARD)
//                    Write2SDCard.getInstance().writeMsg("下载app失败 error:" + error.toString());
//
//            }
//
//            @Override
//            public void onLoading(long total, long current, boolean isUploading) {
//                float allFileSize = (float) (Math.round(((float) (total * 1.0 / 1000000)) * 10) / 10.0);
//                float currentFileSize = (float) (Math.round(((float) (current * 1.0 / 1000000)) * 10) / 10.0);
//                //showMessageUsehandler("APP下载进度：" + currentFileSize + "M /" + allFileSize + "M");
//                sendMessage((int) ((current * 100) / total), UPDATING);
//            }
//        });
//    }
//
//
//    private void sendMessage(int str, int what) {
//        Message msg = Message.obtain();
//        msg.what = what;
//        msg.obj = str;
//        handler.sendMessage(msg);
//
//    }
//
//    private void openApp() {
//        Intent openIntent = new Intent();
//        openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        openIntent.setAction(Intent.ACTION_VIEW);
//        Uri uri = Uri.fromFile(new File(PreferenceXshellUtil.getInstance().getDownAppDir()));
//        openIntent.setDataAndType(uri, "application/vnd.android.package-archive");
//        AppContext.CONTEXT.startActivity(openIntent);
//    }
//}
