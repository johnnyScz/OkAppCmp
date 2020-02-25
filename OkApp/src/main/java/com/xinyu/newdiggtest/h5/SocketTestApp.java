//package com.xinyu.newdigg.h5;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.util.Log;
//
//import com.xinyu.newdigg.R;
//import com.xinyu.newdigg.ui.App;
//import com.xinyu.newdigg.utils.ToastUtils;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import io.socket.client.Socket;
//import io.socket.emitter.Emitter;
//
//public class SocketTestApp extends Activity {
//
//    private Socket mSocket;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//        mSocket = App.getSocket();
//        initView();
//        setListner();
//        mSocket.connect();
////
//    }
//
//
//    private void setListner() {
//        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                beginEmit();
//
//            }
//        });// 连接成功;
//
//
//        mSocket.on("msg", new Emitter.Listener() {
//            @Override
//            public void call(final Object... args) {
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        JSONObject data = (JSONObject) args[0];
//                        Log.e("amtf", "socket监听数据返回：" + data.toString());
//                    }
//                });
//
//
//            }
//        });
//
//
//        mSocket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                ToastUtils.getInstanc().showToast("socket链接错误");
//            }
//        });
//        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.e("amtf", "socket链接超时");
//
//            }
//        });
//
//        mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                mSocket.connect();
//            }
//        });
//
//        mSocket.on("subresp", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.e("amtf", "订阅成功");
//            }
//        });
//
//
//    }
//
//    private void beginEmit() {
//
//
//        try {
//            JSONObject object = new JSONObject();
//            JSONArray array = new JSONArray();
//            JSONObject child = new JSONObject();
//            child.put("userId", "427");
//            child.put("topic", "ok:chat");
//            array.put(child);
//            object.put("subs", array);
//            mSocket.emit("sub", object);//向服务端发送数据
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    private void initView() {
//
//
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mSocket.disconnect();
//        mSocket.off(Socket.EVENT_CONNECT);
//        mSocket.off(Socket.EVENT_CONNECT_ERROR);
//        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT);
//        mSocket.off(Socket.EVENT_DISCONNECT);
//        mSocket.off("msg");
//        mSocket.off("subresp");
//    }
//}
