package com.xinyu.newdiggtest.wxapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xinyu.newdiggtest.net.ApiConfig;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeixinUtil {
    private static final Object lockObj = new Object();

    ExecutorService executorService = Executors.newCachedThreadPool();


    private static WeixinUtil instance;
    private static IWXAPI api;

    /**
     * 微信的WX_CODE
     */
    private String code;

    public static WeixinUtil getInstance() {
        if (instance == null) {
            synchronized (lockObj) {
                if (instance == null) {
                    instance = new WeixinUtil();
                }
            }
        }
        return instance;
    }


    private WeixinUtil() {

    }

    public boolean initWeixinApi(Context context) {

        if (api == null) {
            // 这种注册方式会向微信官方请求校验身份
            api = WXAPIFactory.createWXAPI(context.getApplicationContext(), ApiConfig.WXAPP_ID, true);
            // 将该app注册到微信
            api.registerApp(ApiConfig.WXAPP_ID);
            return true;
        }
        return false;
    }

    public IWXAPI getWeixinApi() {
        return api;
    }


    public void saveWXCode(String code) {
        this.code = code;
    }

    public String getWXCode() {
        return code;
    }

    /**
     * 发送一个微信登录的请求
     *
     * @return
     */
    public boolean weixinSendReq() {
        // 没有安装微信
        if (!api.isWXAppInstalled()) {
            return false;
        }

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
//        api.sendReq(req);

        if (api.sendReq(req)) {
            return true;
        }
        return false;
    }

    /**
     * 发送一个微信登录的请求
     *
     * @param callBackJavascriptName 当使用html5的时候，回调的函数
     */
    public boolean weixinSendReq(String callBackJavascriptName) {

        return weixinSendReq();
    }


    /**
     * * 获得微信用户信息
     *
     * @param secret 微信的secret
     * @param code   微信的
     * @return 用户的信息
     */
    public void loadWXUserInfo(final String secret, final String code) {

        if (code == null || ApiConfig.WXAPP_ID == null || secret == null || "".equals(code) || "".equals(ApiConfig.WXAPP_ID) || "".equals(secret)) {
            return;
        }

        final String urlPath = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + ApiConfig.WXAPP_ID + "&secret=" + secret + "&code=" + code
                + "&grant_type=authorization_code";


        executorService.execute(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                try {
                    URL url = new URL(urlPath);
                    //打开连接
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    if (200 == urlConnection.getResponseCode()) {
                        //得到输入流
                        is = urlConnection.getInputStream();
                        StringBuffer out = new StringBuffer();
                        byte[] b = new byte[1024];
                        for (int n; (n = is.read(b)) != -1; ) {
                            out.append(new String(b, 0, n));
                        }
                        JSONObject obj = new JSONObject(out.toString());
                        String token = obj.getString("access_token");
                        String openId = obj.getString("openid");
                        getWxNetInfo(token, openId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }
        });


    }

    /**
     * 分享一个文本
     *
     * @param context 上下文
     * @param text    内容
     * @param friends 是否分享到朋友圈
     */
    public void sendText(Context context, String text, boolean friends) {
        initAPI(context);
        // 初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // 发送文本类型的消息时，title字段不起作用
        // msg.title = "Will be ignored";
        msg.description = text;

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
        req.message = msg;
        req.scene = friends ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);

    }


    public void sendWebPage(Context context, String webpageUrl, boolean friends) {
        initAPI(context);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webpageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = friends ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    private void initAPI(Context context) {
        synchronized (lockObj) {
            if (api == null) {

                // 这种注册方式会向微信官方请求校验身份
                api = WXAPIFactory.createWXAPI(context.getApplicationContext(), ApiConfig.WXAPP_ID, true);
                // 将该app注册到微信
                api.registerApp(ApiConfig.WXAPP_ID);
            }
        }
    }

    private String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public interface GetUserInfoListener {
        void onResp(String userInfo);
    }

    /**
     * 微信分享
     *
     * @param jo      json的参数
     * @param context 上下文
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public void weixinShare(JSONObject jo, Context context) throws JSONException, UnsupportedEncodingException {
        String sign = jo.getString("flag");
        String msg = jo.getString("content");
        String friends = jo.getString("friend");
        boolean share_friends = true;// 默认分享到朋友圈
        int number = 1;
        try {
            number = Integer.parseInt(sign);
        } catch (Exception e) {
        }
        if (number == 1) {// 代表一个文字`
            try {// 进行捕捉 如果没有设置 那么分享到朋友圈
                share_friends = Integer.parseInt(friends) != 2;// 2是分享到会话
            } catch (Exception e) {
            }
            sendText(context, msg, share_friends);
        } else if (number == 2) {// 代表一个url
            String title = jo.getString("title");
            title = URLDecoder.decode(title, "UTF-8");
            String content = jo.getString("describe");
            content = URLDecoder.decode(content, "UTF-8");
            try {// 进行捕捉 如果没有设置 那么分享到朋友圈
                share_friends = Integer.parseInt(friends) != 2;// 2是分享到朋友圈
            } catch (Exception e) {
                e.printStackTrace();
            }
            int resId;
            resId = context.getResources().getIdentifier("logo_share", "drawable", context.getPackageName());
            if (resId == 0) {
                resId = context.getResources().getIdentifier("ic_launcher", "drawable", context.getPackageName());
            }
            sendWebPage(context, msg, title, content, resId, share_friends);
        }

    }

    public void sendWebPage(Context context, String webpageUrl, String title, String content, int resId, boolean friends) {
        initAPI(context);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webpageUrl;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = content;
        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), resId);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(thumb, 120, 120, true);
        thumb.recycle();

        msg.thumbData = WeixinModel2ByteUtil.bmpToByteArray(thumbBmp, true);
        thumbBmp.recycle();
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = friends ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        if (api.sendReq(req)) {

        }
    }


    public void diggWxShare(Context context, String webpageUrl, String title, String content, int resId, boolean friends) {
        initAPI(context);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webpageUrl;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = content;
        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), resId);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(thumb, 120, 120, true);
        thumb.recycle();

        msg.thumbData = WeixinModel2ByteUtil.bmpToByteArray(thumbBmp, true);
        thumbBmp.recycle();
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = friends ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        if (api.sendReq(req)) {

        }
    }


    public void diggWxShare(Context context, String webpageUrl, String title, String content, Bitmap resId, boolean friends) {
        initAPI(context);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webpageUrl;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = content;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(resId, 120, 120, true);
        msg.thumbData = WeixinModel2ByteUtil.bmpToByteArray(thumbBmp, true);
        thumbBmp.recycle();
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = friends ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        if (api.sendReq(req)) {

        }
    }

    private void getWxNetInfo(String token, String openid) {
        String urlPath = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + openid;
        InputStream is = null;
        try {
            URL url = new URL(urlPath);
            //打开连接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (200 == urlConnection.getResponseCode()) {
                //得到输入流
                is = urlConnection.getInputStream();
                StringBuffer out = new StringBuffer();
                byte[] b = new byte[1024];
                for (int n; (n = is.read(b)) != -1; ) {
                    out.append(new String(b, 0, n));
                }
                JSONObject obj = new JSONObject(out.toString());

                savePersonInfo(obj);
//                if (APPConstans.mcallBack != null) {
//                    APPConstans.mcallBack.success(obj);
//                    savePersonInfo(obj);
//                    Log.w("amtf", "拿到微信信息：" + obj.toString());
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void savePersonInfo(JSONObject obj) {

        try {
            String nikeName = obj.getString("nickname");
            String headUrl = obj.getString("headimgurl");

            PreferenceUtil.getInstance(App.mContext).setNickName(nikeName);
            PreferenceUtil.getInstance(App.mContext).setHeadUrl(headUrl);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
