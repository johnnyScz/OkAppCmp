package com.xshell.xshelllib.plugin;

import android.app.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.xshell.xshelllib.R;
import com.xshell.xshelllib.application.AppConstants;
import com.xshell.xshelllib.application.AppContext;

import com.xshell.xshelllib.utils.Log2FileUtil;
import com.xshell.xshelllib.utils.PhoneInfo;
import com.xshell.xshelllib.utils.PlaySoundsUtil;
import com.xshell.xshelllib.utils.SharedPreferencesUtils;
import com.xshell.xshelllib.utils.VersionUtil;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;

/**
 * 原生插件，比如读写文件
 *
 * @author zzy
 */
public class NativePlugin extends CordovaPlugin {
    private static final String TAG = "NativePlugin";
    private Activity context;
    private static HashMap<String, Integer> sounds = new HashMap<String, Integer>();// 音乐播放
    private File ROOT_FILE;
    private Handler handler = new Handler();
    private boolean isFlay = true;
    /**
     * 当打开一个页面时，需要调用回调函数名字
     */
    private String newBroserCallBcak;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        context = cordova.getActivity();
        ROOT_FILE = new File(context.getFilesDir().getAbsolutePath());
    }

    @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) throws JSONException {
        String token = SharedPreferencesUtils.getDevieToken(context, "-1");
        if ("getDeviceId".equals(action)) { // 获取设备id
            PhoneInfo phoneInfo = PhoneInfo.getInstance(context);
            JSONObject json = new JSONObject();
            json.put("version", VersionUtil.getVersionName(context));
            json.put("deviceid", phoneInfo.getDeviceID());
            json.put("model", phoneInfo.getModel());
            json.put("release", "Android" + phoneInfo.getSystemVersion());
            json.put("pixels", phoneInfo.getPixels());
            json.put("result", 1);
            json.put("devicetoken", token);
            if (callbackContext != null) {
                Log.e("amtf", "callbackContext上传shprf 中的deviveToken:" + token);
                //Toast.makeText(context, "传给会h5的DeviceToken：" + json.toString(), Toast.LENGTH_LONG).show();
                callbackContext.success(json.toString());
            }
            return true;
        }  /*else if ("startToChangeOrientation".equals(action)) { // 横竖屏切换
                JSONObject jos = new JSONObject(result);
				final String callBackName = jos.getString("callbackName");
				String string = jos.getString("type");
				if("1".equals(jos.getString("type"))) {
					cordova.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
				} else {
					cordova.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
				}
				cordova.getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						webView.loadUrl("javascript:" + callBackName.trim() + "('{\"result\":1}')");
					}
				});
				return true;
			}*/ else if ("share".equals(action)) { // 分享功能
//				JSONObject jo = args.getJSONObject(0);
//				WeixinUtil.getInstance().weixinShare(jo, cordova.getActivity());
//				JSONObject json = new JSONObject();
//				json.put("result", 1);
//				callbackContext.success(json);
//				return true;
        } else if ("sound".equals(action)) { // 播放声音
            try {
                JSONObject json = new JSONObject();
                String name = json.getString("soundfilename");
                name = URLDecoder.decode(name, "UTF-8");
                if (sounds.containsKey(name)) {
                    PlaySoundsUtil.getInstance().play(sounds.get(name), 0);
                } else {

                    int id = -1;
                    synchronized (TAG) {
                        File file = new File(ROOT_FILE, name);
                        if (file.exists() && file.isFile())
                            id = PlaySoundsUtil.getInstance().loadSound(file.getAbsolutePath());
                    }
                    if (id != -1) {
                        sounds.put(name, id);
                        Intent intent = new Intent(AppConstants.ACTION_WEBVIEW_PLAY_SOUND);
                        intent.putExtra("soundId", id);
                        cordova.getActivity().sendBroadcast(intent);
                    }

                }
                json.put("result", 1);
                callbackContext.success(json);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else if ("xinyuNewBrowser".endsWith(action)) { // 开启一个新的Activity
            if (isFlay) {
                isFlay = false;
                Log.e("amtf", "NativePlugin:开启一个新的Activity");
                JSONObject jos = args.getJSONObject(0);
                if (jos.has("callbackName")) {
                    newBroserCallBcak = jos.getString("callbackName");
                }
                Log.e("huanghu", "newBroserCallBcak:" + newBroserCallBcak);
                String url = jos.getString("url");
                Log2FileUtil.getInstance().saveCrashInfo2File("开启了一个新的Activity");


                Intent intent1 = new Intent();
                intent1.setAction(AppConstants.ACTION_NEW_BROSER);
                intent1.putExtra("url", url);
                LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent1);
//                MessageEvent event = new MessageEvent(XshellConsts.OPEN_NEW_BROWSER);
//                event.msg = url;
//                EventBus.getDefault().post(event);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isFlay = true;
                    }
                }, 500);
            }
            return true;
        } else if ("closeNewBrowser".equals(action)) {
            // 关闭Activity
            try {
                if (!TextUtils.isEmpty(args.toString())) {
                    Log.e("amtf", "closeNewBrowser:" + args.toString());
                    boolean aBoolean = args.getBoolean(0);
                    Object aQuict = args.get(1);
                    if (aQuict != null && !aQuict.toString().equals("null")) {
                        aQuict = args.getString(1);
                        SharedPreferencesUtils.setParam(cordova.getActivity(), "quict", aQuict + "");
                    } else {
                        SharedPreferencesUtils.setParam(cordova.getActivity(), "quict", aQuict + "");
                    }
                    Log2FileUtil.getInstance().saveCrashInfo2File("关闭了一个新的Activity");
                    Intent intent1 = new Intent();
                    intent1.setAction(AppConstants.ACTION_CLOSE_BROSER);
                    LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent1);
//                    EventBus.getDefault().post(new MessageEvent(XshellConsts.CLOSE_NEW_BROWSER));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return true;
        } else if ("clipboard".equals(action)) { //粘贴板
            JSONObject jos = args.getJSONObject(0);
            ClipboardManager clip = (ClipboardManager) AppContext.CONTEXT.getSystemService(Context.CLIPBOARD_SERVICE);
            clip.setPrimaryClip(ClipData.newPlainText(null, jos.getString("paste")));
            callbackContext.success();
            return true;
        } else if ("reminder".equals(action)) {// 提示
            // Intent intent = new Intent(AppConstants.ACTION_REMINDER);
            // LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent);
            AudioManager audio = (AudioManager) cordova.getActivity().getSystemService(Context.AUDIO_SERVICE);
            int RingerMode = audio.getRingerMode();
            vibrator = (Vibrator) cordova.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            // 注册音频通道
            cordova.getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = MediaPlayer.create(cordova.getActivity(), cordova.getActivity().getResources().getIdentifier("test", "raw", cordova.getActivity().getPackageName()));
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 注册事件。当播放完毕一次后，重新指向流文件的开头，以准备下次播放。
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.seekTo(0);
                }
            });
            switch (RingerMode) {
                case AudioManager.RINGER_MODE_NORMAL:// 铃声震动模式
                    vibrator.vibrate(new long[]{300, 500}, -1);
                    mediaPlayer.start();
                    break;
                case AudioManager.RINGER_MODE_SILENT:// 静音模式
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:// 震动模式
                    vibrator.vibrate(new long[]{300, 500}, -1);
                    break;

                default:
                    break;
            }
            callbackContext.success();
            return true;

        } else if ("openLock".equals(action)) {

            return true;
        } else if ("createWindow".equals(action) || "createNotFullWindow".equals(action)) {  //开启一个带title的activity
//            Log.i("zzy", "------createWindow--------:");
//            Intent in;
//            in = new Intent(cordova.getActivity(), NewBrowserActivity.class);
//            JSONObject jos = args.getJSONObject(0);
//            String url = jos.getString("url");
//            String title = jos.getString("title");
//            in.putExtra("newBrowserUrl", url);
//            in.putExtra("title", title);
//            cordova.getActivity().startActivity(in);
//            cordova.getActivity().overridePendingTransition(R.anim.xinyusoft_activity_right_in, R.anim.xinyusoft_activity_left_out);
            return true;
        }

        return false;
    }


    MediaPlayer mediaPlayer;
    Vibrator vibrator;

    @Override
    public void onPause(boolean multitasking) {
        super.onPause(multitasking);
        if (mediaPlayer != null) {
            mediaPlayer.release();

        }
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        Log.e("huanghu", "=======================onResume");
        if (newBroserCallBcak != null) {
            cordova.getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    String quict = (String) SharedPreferencesUtils.getParam(cordova.getActivity(), "quict", "");
                    Log.e("huanghu", "=======================+++++++++++++++++++++++" + quict + "===newBroserCallBcak:" + newBroserCallBcak);
                    if ("null".equals(quict) || "".equals(quict)) {
                        webView.loadUrl("javascript:" + newBroserCallBcak + "()");
                        newBroserCallBcak = null;
                        SharedPreferencesUtils.setParam(cordova.getActivity(), "quict", "");
                    } else {
                        webView.loadUrl("javascript:" + newBroserCallBcak + "('" + quict + "')");
                        newBroserCallBcak = null;
                        SharedPreferencesUtils.setParam(cordova.getActivity(), "quict", "");
                    }
                }
            });
        }

    }


}
