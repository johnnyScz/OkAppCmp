package com.xshell.xshelllib.application;

import android.content.Context;
import android.os.Looper;
import android.util.Log;


/**
 * Created by Administrator on 2017/11/29.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler instance; // 单例模式
    private Thread.UncaughtExceptionHandler defalutHandler; // 系统默认的UncaughtException处理类

    private CrashHandler() {

    }

    /**
     * 获取CrashHandler实例
     *
     * @return CrashHandler
     */
    public static CrashHandler getInstance() {
        if (instance == null) {
            synchronized (CrashHandler.class) {
                if (instance == null) {
                    instance = new CrashHandler();
                }
            }
        }

        return instance;
    }

    /**
     * 异常处理初始化
     *
     * @param context
     */
    public void init(Context context) {
        // 获取系统默认的UncaughtException处理器
        defalutHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //异常发生时，先给蓝牙服务端发送OK命令
//        if (mListener != null) {
//            mListener.onBeforeHandleException();
//        }

        // 自定义错误处理
        boolean res = handleException(ex);
        if (!res && defalutHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            defalutHandler.uncaughtException(thread, ex);

        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e("amtf", "error : " + e.getMessage());
            }
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }

        new Thread() {

            @Override
            public void run() {
                Looper.prepare();
                ex.printStackTrace();
                String err = "[" + ex.getMessage() + "]";
                Log.e("amtf", "handle error:" + err);
                Looper.loop();
            }

        }.start();

        // 收集设备参数信息 \日志信息
//        String errInfo = collectDeviceInfo(context, ex);
//        // 保存日志文件
//        saveCrashInfo2File(errInfo);
        return true;
    }
}

