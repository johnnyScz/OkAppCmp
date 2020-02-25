//package com.xshell.xshelllib.plugin;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.pm.PackageManager;
//import android.media.MediaPlayer;
//import android.support.v4.content.ContextCompat;
//import android.util.Log;
//import android.widget.Toast;
//
//
//
//import com.xshell.xshelllib.utils.XshellConsts;
//import com.xshell.xshelllib.utils.RecorderNewUtil;
//import com.xshell.xshelllib.utils.SpeechUtil;
//
//import org.apache.cordova.CallbackContext;
//import org.apache.cordova.CordovaArgs;
//import org.apache.cordova.CordovaInterface;
//import org.apache.cordova.CordovaPlugin;
//import org.apache.cordova.CordovaWebView;
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//import org.json.JSONException;
//
//import java.io.IOException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//
//public class NativeSetPlugin extends CordovaPlugin {
//    private Activity context;
//
//    CallbackContext mCallbackContext;
//    //
//    final String[] Permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
//
//
//    @Override
//    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
//        super.initialize(cordova, webView);
//        context = cordova.getActivity();
//        XshellConsts.mySpeechCallback = null;
//        EventBus.getDefault().register(this);
//    }
//
//
//    /**
//     * 2.拍照权限 3.读写读写外部存储设备权限
//     *
//     * @return
//     * @throws JSONException
//     */
//
//
//    @Override
//    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
//        String flag = args.getString(0);
//        mCallbackContext = callbackContext;
//        if ("authorizeIsOpen".equals(action)) {
//            if ("1".equals(flag)) {
//                if (CommonUtil.isNotificationEnable(context)) {
//                    callbackContext.success(1);
//                    Toast.makeText(context, "权限开通了", Toast.LENGTH_LONG).show();
//                } else {
//                    callbackContext.success(0);
//                    Log.e("amtf", "没有通知权限");
//                }
//            } else {
//                if ("2".equals(flag)) {
//                    if (ContextCompat.checkSelfPermission(context, Permissions[1])
//                            != PackageManager.PERMISSION_GRANTED) {
//                        callbackContext.success(1);
//                        Log.e("amtf", "有相机权限");
//
//                    } else {
//                        callbackContext.success(0);
//                        Log.e("amtf", "权限没有开通");
//
//                    }
//                }
//                if ("3".equals(flag)) {
//                    if (ContextCompat.checkSelfPermission(context, Permissions[2])
//                            != PackageManager.PERMISSION_GRANTED) {
//                        callbackContext.success(1);
//
//                    } else {
//                        callbackContext.success(0);
//                    }
//                }
//            }
//        } else if ("toAuthorizeSet".equals(action)) {
//            //goSetting(flag);
////        } else if ("openaccount".equals(action)) {
////            EventBus.getDefault().post(new XshellEvent(XshellConsts.OPEN_ACCT));
////        } else if ("cchelper".equals(action)) {
////            EventBus.getDefault().post(new XshellEvent(XshellConsts.OPEN_CcHelper));
////        } else
//            if ("startRecord".equals(action)) {
//            eService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    //播放前释放资源
//                    RecorderNewUtil.getInstance(context).releaseRecorder();
//                    //执行录音操作
//                    RecorderNewUtil.getInstance(context).recordOperation();
//                }
//            });
//
//
//        } else if ("endRecord".equals(action)) {
//            RecorderNewUtil.getInstance(context).stopRecording();
//            callbackContext.success(RecorderNewUtil.getInstance(context).getRecordFile());
//            //TODO 测试录音的音频，可以删掉
//            playMusic();
//            return true;
//        } else if ("speechStart".equals(action)) {
////            EventBus.getDefault().post(new XshellEvent(XshellConsts.STAETSPEECH));
////            XshellConsts.mySpeechCallback = callbackContext;
//            Log.e("amtf", "进入到百度语音");
//            beginBaiduSpeech();
//            return true;
//        } else if ("getWebInfo".equals(action)) {
//            Log.e("amtf", "进入到getWebInfo");
//            Toast.makeText(context, "我来了", Toast.LENGTH_LONG).show();
//            return true;
//        } else if ("beginCall".equals(action)) {
//            Log.e("amtf", "beginCall");
//            return true;
//        }
//        return true;
//    }
//
//    ExecutorService eService = Executors.newSingleThreadExecutor();
//
//
//    MediaPlayer mediaPlayer = new MediaPlayer();
//
//    private void playMusic() {
//        mediaPlayer = new MediaPlayer();
//        mediaPlayer.reset();
//        try {
//            mediaPlayer.setDataSource(RecorderNewUtil.getInstance(context).getRecordFile());
//            mediaPlayer.setVolume(30, 50);
//            //是否循环播放
//            mediaPlayer.setLooping(false);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        eService.submit(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    mediaPlayer.prepare();
//                    mediaPlayer.start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//    }
//
//    SpeechUtil instance;
//
//    private void beginBaiduSpeech() {
//        if (instance == null) {
//            instance = new SpeechUtil(context);
//        }
//        instance.start();
//
//    }
//
//
//
//    @Override
//    public void onDestroy() {
//        mediaPlayer.stop();
//        mediaPlayer.release();
//        if (instance != null) {
//            instance.releaseSpeech();
//        }
//        EventBus.getDefault().unregister(this);
//        super.onDestroy();
//    }
//
//
//
//    {
//
//
//
//
//    }
//}
