package com.xinyu.newdiggtest.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.cutecomm.smartsdk.RemoteAssistanceManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.xinyu.newdiggtest.APPConstans;
import com.xinyu.newdiggtest.R;

import com.xinyu.newdiggtest.app.HomeAppActivity;
import com.xinyu.newdiggtest.net.ApiConfig;
import com.xinyu.newdiggtest.net.bean.SSLSocket;
import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.CrashHandler;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import io.agora.AgoraAPIOnlySignal;
import io.socket.client.IO;
import io.socket.client.Socket;

public class App extends Application {

    public static Context mContext;

    private static Socket mSocket;

    {
        try {
            IO.Options opts = new IO.Options();
            //如果服务端使用的是https 加以下两句代码,文章尾部提供SSLSocket类
            opts.sslContext = SSLSocket.genSSLSocketFactory();
            opts.hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            mSocket = IO.socket(ApiConfig.SOCKET_IP, opts);
        } catch (Exception e) {
        }
    }


    public static Socket getSocket() {
        return mSocket;
    }

    public static void setSocket(Socket socket) {
        mSocket = socket;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        init();
        initPush();
        activityLifeCycle();

        /**
         * 相机问题
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        initX5();

        initAgraCall();


        initChelper();

    }

    private void initChelper() {
        RemoteAssistanceManager.getInstance().init(mContext, "8c6ebd0f8be58a09ffe5329ef20ab967");
    }


    private void initX5() {
        QbSdk.initX5Environment(getApplicationContext(), new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                APPConstans.X5Core = b;
                if (!APPConstans.X5Core) {
                    initX5();
                }
            }
        });
    }


    private void initPush() {
        UMConfigure.init(this, ApiConfig.Umeng_AppKey,
                "Umeng", UMConfigure.DEVICE_TYPE_PHONE, ApiConfig.Umeng_Message_Secret);

//        Constants.Umeng_Message_Secret


        final PushAgent mPushAgent = PushAgent.getInstance(this);

        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                PreferenceUtil.getInstance(mContext).setUmengToken(deviceToken);

                Log.e("amtf", "友盟注册成功：-------->:  " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e("amtf", "友盟注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {

                Log.e("amtf", "自定义消息UmengNotificationClickHandler");

                APPConstans.PUSH_INFO = msg.custom;


                Intent intent1 = new Intent(mContext, HomeAppActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("UMengPush", msg.custom);
                startActivity(intent1);


            }

        };


        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        PushAgent.getInstance(this).onAppStart();
    }


    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //设置下拉部分layout颜色
//                layout.setPrimaryColorsId(R.color.mall_blue, android.R.color.white);
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }


    private void activityLifeCycle() {

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (AppContacts.isAppAlive == 0) {
                    AppContacts.isBackrond = false;
                    Log.w("amtf", "后台进前台");
                }
                AppContacts.isAppAlive++;

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                AppContacts.isAppAlive--;
                if (AppContacts.isAppAlive == 0) {
                    AppContacts.isBackrond = true;
                    Log.w("amtf", "前台进后台");
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });


    }


    private void init() {

        AppContacts.WXTag = 1;
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

//        CrashReport.initCrashReport(getApplicationContext(), "00d8315c0a", true);//异常上报，发布时改成false
    }

    public static Context getContext() {
        return mContext;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        mSocket.disconnect();
        mSocket = null;
        m_agoraAPI = null;
        RemoteAssistanceManager.getInstance().release();
    }

    private void initAgraCall() {
        setupAgoraEngine();
    }

    private void setupAgoraEngine() {
        String appID = getString(R.string.agora_app_id);
        m_agoraAPI = AgoraAPIOnlySignal.getInstance(this, appID);
    }


    private static App mInstance;
    private AgoraAPIOnlySignal m_agoraAPI;

    public AgoraAPIOnlySignal getM_agoraAPI() {
        return m_agoraAPI;
    }

    public static App the() {
        return mInstance;
    }

    public App() {
        mInstance = this;
    }


}
