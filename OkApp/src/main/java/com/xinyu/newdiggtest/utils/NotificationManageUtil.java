package com.xinyu.newdiggtest.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.xinyu.newdiggtest.R;

import static android.content.Context.NOTIFICATION_SERVICE;


public class NotificationManageUtil {

    private static NotificationManageUtil instance;

    static Context context;


    private static NotificationManager notificationManager;


    private static NotificationCompat.Builder builder;


    private NotificationManageUtil() {
    }


    public static NotificationManageUtil getInstance(Context mctx) {

        context = mctx;

        if (instance == null) {
            instance = new NotificationManageUtil();
        }

        return instance;
    }


    /***
     * 更新UI
     */
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what <= 99) {
                builder.setProgress(100, msg.what, false);
                builder.setContentText("下载进度:" + msg.what + "%");
                notificationManager.notify(1, builder.build());
            } else {
                builder.setProgress(100, 100, false);
                builder.setContentText("下载完成");
                notificationManager.cancel(1);
            }
        }
    };


    @SuppressWarnings("deprecation")
    public static void createNotification(Context ctx) {

        notificationManager = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("123", "Notif123", NotificationManager.IMPORTANCE_LOW);
            Log.i("amtf", mChannel.toString());
            notificationManager.createNotificationChannel(mChannel);
            builder = new NotificationCompat.Builder(ctx);
            builder.setChannelId("123").setContentTitle("正在更新...") //设置通知标题
                    .setSmallIcon(R.mipmap.ic_launcher) //设置通知的小图标
                    .setAutoCancel(false)//设置通知被点击一次是否自动取消
                    .setContentText("下载进度:" + "0%")
                    .setProgress(100, 0, false);
        } else {
            builder = new NotificationCompat.Builder(ctx);
            builder.setContentTitle("正在更新...") //设置通知标题
                    .setSmallIcon(R.mipmap.ic_launcher) //设置通知的小图标
                    .setAutoCancel(false)//设置通知被点击一次是否自动取消
                    .setContentText("下载进度:" + "0%")
                    .setProgress(100, 0, false);
        }

    }


}