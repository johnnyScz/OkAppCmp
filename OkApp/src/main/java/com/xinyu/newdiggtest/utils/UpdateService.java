package com.xinyu.newdiggtest.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.App;


public class UpdateService extends Service {


    private NotificationManager notificationManager;


    private NotificationCompat.Builder builder;


    //    private PendingIntent pendingIntent;
    File updateFile;

    ExecutorService excutor = Executors.newCachedThreadPool();


    /***
     * 更新UI
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 101) {
                if (!updateFile.exists()) {
                    Log.e("amtf", "你白忙活了");
                    return;
                }
                Intent installApkIntent = getFileIntent(updateFile);
                startActivity(installApkIntent);
                notificationManager.cancel(1);

//                    pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, installApkIntent, 0);
//                    Notification notification = builder.build();
//                    notification.contentIntent = pendingIntent;
//                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
//                    notificationManager.notify(1, notification);

                stopSelf();

            } else if (msg.what != 0) {
                builder.setProgress(100, msg.what, false);
                builder.setContentText("下载进度:" + msg.what + "%");

                notificationManager.notify(1, builder.build());

            }


        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            try {

                final String down_url = intent.getStringExtra("downurl");
                // 创建文件
                String sdpath = Environment.getExternalStorageDirectory() + File.separator + "sdacard"/* + File.separator*/;


                File filePath = new File(sdpath);
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }

                String fileName = "test123.apk";

                updateFile = new File(sdpath, fileName);
                if (!updateFile.exists()) {
                    updateFile.createNewFile();
                }

                // 创建通知
                createNotification();
                // 开始下载

                excutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        downloadUrl(down_url, updateFile);
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    boolean isFinish = false;

    private void downloadUrl(String down_url, File file) {
        isFinish = false;
        try {
            URL url = new URL(down_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //打开连接
            conn.setConnectTimeout(5000);  // 连接超时时间 毫秒级 10S
            conn.setReadTimeout(5000); //读取超时时间
            conn.setRequestMethod("GET"); //请求方式
            conn.connect(); //获取连接
            BufferedInputStream bin = new BufferedInputStream(conn.getInputStream());
            int count = conn.getContentLength(); //文件总大小 字节
            int size = 0;
            int len = 0;
            byte[] buf = new byte[1024];

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {//响应成功 200
                FileOutputStream fos = new FileOutputStream(file);
                long beforeTime = System.currentTimeMillis();
                while ((size = bin.read(buf)) != -1) {
                    fos.write(buf, 0, size);
                    len += size; //累加每次读取的字节大小
                    if (System.currentTimeMillis() - beforeTime > 500) {
                        Message message = new Message();
                        message.what = (int) (((double) len / (double) count) * 100);
                        handler.sendMessage(message);
                        beforeTime = System.currentTimeMillis();
                    }
                }
                fos.flush();
                fos.close(); //关闭文件输出流
                bin.close();//关闭输入流
                conn.disconnect();//断开连接

                Log.e("amtf", "下载完成");

                Message message = new Message();
                message.what = 101;
                handler.sendMessage(message);


                isFinish = true;

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @SuppressWarnings("deprecation")
    public void createNotification() {

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("123", "NotificationChannel123", NotificationManager.IMPORTANCE_LOW);
            Log.i("amtf", mChannel.toString());
            notificationManager.createNotificationChannel(mChannel);
            builder = new NotificationCompat.Builder(this);
            builder
                    .setChannelId("123").setContentTitle("正在更新...") //设置通知标题
                    .setSmallIcon(R.mipmap.ic_launcher) //设置通知的小图标
                    .setAutoCancel(false)//设置通知被点击一次是否自动取消
                    .setContentText("下载进度:" + "0%")
                    .setProgress(100, 0, false);
        } else {
            builder = new NotificationCompat.Builder(this);
            builder.setContentTitle("正在更新...") //设置通知标题
                    .setSmallIcon(R.mipmap.ic_launcher) //设置通知的小图标
                    .setAutoCancel(false)//设置通知被点击一次是否自动取消
                    .setContentText("下载进度:" + "0%")
                    .setProgress(100, 0, false);
        }

    }


    public static Intent getFileIntent(File file) {

        Uri uri = FileProvider.getUriForFile(App.mContext, "com.xinyu.newdiggtest", file);

        String type = getMIMEType(file);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, type);
        return intent;
    }

    public static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        // 取得扩展名
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length());
        if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else {
            // /*如果无法直接打开，就跳出软件列表给用户选择 */
            type = "*/*";
        }
        return type;
    }
}