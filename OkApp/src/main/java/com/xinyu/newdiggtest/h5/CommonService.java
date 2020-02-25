package com.xinyu.newdiggtest.h5;

import android.app.Service;

import android.content.Context;
import android.content.Intent;

import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import com.xinyu.newdiggtest.net.ApiConfig;
import com.xinyu.newdiggtest.net.bean.SSLSocket;

import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * 开启公共事务的的service
 */

public class CommonService extends Service {


    private Socket mSocket;

    Context mctx;

    Timer timer;


    @Override
    public void onCreate() {
        super.onCreate();
        mctx = this;
        initSocket();
    }

    /**
     * socket相关
     */
    private void initSocket() {
        mSocket = App.getSocket();
        mSocket.connect();
        setListner();

    }


    private void setListner() {
        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                beginEmit();
            }
        });// 连接成功;

        mSocket.on("msg", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {

                JSONObject data = (JSONObject) args[0];

                try {
                    String json = data.getString("msg");

                    Log.e("amtf", "Service收到json数据:" + json);

                    JSONObject pushData = new JSONObject(json);
                    XshellEvent event = new XshellEvent(EventConts.SoCket_Push);
                    event.object = pushData;
                    EventBus.getDefault().postSticky(event);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        mSocket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("amtf", "链接错误:" + ApiConfig.SOCKET_IP);
                reconnectSocket();
            }
        });
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("amtf", "链接超时:" + ApiConfig.SOCKET_IP);
                reconnectSocket();

            }
        });

        mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("amtf", "链接断开:" + ApiConfig.SOCKET_IP);
                reconnectSocket();
            }
        });

        mSocket.on("subresp", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("amtf", "订阅成功:" + ApiConfig.SOCKET_IP);
            }
        });

    }


    private void reconnectSocket() {
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket = null;
        }

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
        mSocket.connect();
        setListner();

        App.setSocket(mSocket);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        timer = new Timer();

        timer.schedule(timerTask, 1000, 1000 * 20); //延时1000ms后执行，20m执行一次

        return super.onStartCommand(intent, flags, startId);


    }


    TimerTask timerTask = new TimerTask() {
        public void run() {
            EventBus.getDefault().postSticky(new XshellEvent(EventConts.Service_Timer));
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        unrigesterSocket();

        if (timer != null) {
            timer.cancel();
            timer = null;

        }

    }


    private void beginEmit() {
        try {
            JSONObject object = new JSONObject();
            JSONArray array = new JSONArray();
            JSONObject child = new JSONObject();
            child.put("userId", PreferenceUtil.getInstance(this).getUserId());

            child.put("topic", "ok");


            array.put(child);
            object.put("subs", array);
            mSocket.emit("sub", object);//向服务端发送数据


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void unrigesterSocket() {
        mSocket.off(Socket.EVENT_CONNECT);
        mSocket.off(Socket.EVENT_CONNECT_ERROR);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT);
        mSocket.off(Socket.EVENT_DISCONNECT);
        mSocket.off("msg");
        mSocket.off("subresp");
        mSocket.disconnect();
        mSocket = null;
    }


}
