//package com.xshell.xshelllib.tools.weixin;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//
//import com.alibaba.fastjson.JSON;
//import com.tencent.mm.sdk.modelmsg.SendAuth;
//import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
//import com.tencent.mm.sdk.modelmsg.WXImageObject;
//import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
//import com.tencent.mm.sdk.modelmsg.WXMusicObject;
//import com.tencent.mm.sdk.modelmsg.WXTextObject;
//import com.tencent.mm.sdk.modelmsg.WXVideoObject;
//import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.xshell.xshelllib.application.AppConstants;
//import com.xshell.xshelllib.utils.ParseConfig;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URL;
//import java.net.URLDecoder;
//
//public class WeixinUtil {
//    private static final Object lockObj = new Object();
//    private String APP_ID;
//    /**
//     * 是否登录或者分享，进入后台如果是微信登录或者分享的话，应用会默认进入后台，要过滤
//     */
//    private boolean isLoginOrShare = true;
//
//    private static WeixinUtil instance;
//    private static IWXAPI api;
//    /**
//     * 是否微信登录
//     */
//    private boolean isWXLogin;
//    /**
//     * 当在html5使用时候，回调给html5的回调函数名字
//     */
//    private String callBackJavascriptName;
//    /**
//     * 微信的WX_CODE
//     */
//    private String code;
//
//    public static WeixinUtil getInstance() {
//        if (instance == null) {
//            synchronized (lockObj) {
//                if (instance == null) {
//                    instance = new WeixinUtil();
//                }
//            }
//        }
//        return instance;
//    }
//
//    public boolean getIsLoginOrShare() {
//        return isLoginOrShare;
//    }
//
//    public void setIsLoginOrShare(boolean isLoginOrShare) {
//        this.isLoginOrShare = isLoginOrShare;
//    }
//
//    private WeixinUtil() {
//
//    }
//
//    public boolean initWeixinApi(Context context, String appid) {
//        this.APP_ID = appid;
//        if (api == null) {
//            // 这种注册方式会向微信官方请求校验身份
//            api = WXAPIFactory.createWXAPI(context.getApplicationContext(), appid, true);
//            // 将该app注册到微信
//            api.registerApp(APP_ID);
//            return true;
//        }
//        return false;
//    }
//
//    public IWXAPI getWeixinApi() {
//        return api;
//    }
//
//    public String getCallBackJavascriptName() {
//        return callBackJavascriptName;
//    }
//
//    public void saveWXCode(String code) {
//        this.code = code;
//    }
//
//    public String getWXCode() {
//        return code;
//    }
//
//    /**
//     * 发送一个微信登录的请求
//     *
//     * @return
//     */
//    public boolean weixinSendReq() {
//        isLoginOrShare = true;
//        // 没有安装微信
//        if (!api.isWXAppInstalled()) {
//            return false;
//        }
//        // XinyuHomeActivity.isWXLogin = true;
//        SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        req.state = "wechat_sdk_demo";
//
//
//        if (api.sendReq(req)) {
//            isWXLogin = true;
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 发送一个微信登录的请求
//     *
//     * @param callBackJavascriptName 当使用html5的时候，回调的函数
//     */
//    public boolean weixinSendReq(String callBackJavascriptName) {
//        this.callBackJavascriptName = callBackJavascriptName;
//        return weixinSendReq();
//    }
//
//    public void setIsWXLogin(boolean isWXLogin) {
//        this.isWXLogin = isWXLogin;
//    }
//
//    public boolean isWXLogin() {
//        return isWXLogin;
//    }
//
//    /**
//     * * 获得微信用户信息
//     *
//     * @param secret 微信的secret
//     * @param code   微信的
//     * @return 用户的信息
//     */
//    public void loadWXUserInfo(final String secret, final String code, final GetUserInfoListener listener) {
//        isWXLogin = false;
//        if (code == null || APP_ID == null || secret == null || "".equals(code) || "".equals(APP_ID) || "".equals(secret)) {
//            return;
//        }
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APP_ID + "&secret=" + secret + "&code=" + code
//                        + "&grant_type=authorization_code";
//                String tokenResult = HttpUtil.httpsGet(accessTokenUrl);
//                if (null != tokenResult) {
//                    com.alibaba.fastjson.JSONObject tokenObj = JSON.parseObject(tokenResult);
//                    String accessToken = tokenObj.getString("access_token");
//                    String openId = tokenObj.getString("openid");
//                    String userUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
//                    String wxUserInfo = HttpUtil.httpsGet(userUrl);
//                    listener.onResp(wxUserInfo);
//                }
//            }
//        }).start();
//
//    }
//
//    /**
//     * 分享一个文本
//     *
//     * @param context 上下文
//     * @param text    内容
//     * @param friends 是否分享到朋友圈
//     */
//    public void sendText(Context context, String text, boolean friends) {
//        initAPI(context);
//        // 初始化一个WXTextObject对象
//        WXTextObject textObj = new WXTextObject();
//        textObj.text = text;
//
//        // 用WXTextObject对象初始化一个WXMediaMessage对象
//        WXMediaMessage msg = new WXMediaMessage();
//        msg.mediaObject = textObj;
//        // 发送文本类型的消息时，title字段不起作用
//        // msg.title = "Will be ignored";
//        msg.description = text;
//
//        // 构造一个Req
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
//        req.message = msg;
//        req.scene = friends ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//        api.sendReq(req);
//
//    }
//
//    public void sendImg(Context context, String title, String content, int resId, int THUMB_SIZE, boolean friends) {
//        initAPI(context);
//        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resId);
//        WXImageObject imgObj = new WXImageObject(bmp);
//        WXMediaMessage msg = new WXMediaMessage();
//        msg.title = title; // 设置标题
//        msg.description = content;// 设置内容
//        msg.mediaObject = imgObj;
//
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//        bmp.recycle();
//        msg.thumbData = WeixinModel2ByteUtil.bmpToByteArray(thumbBmp, true); // 设置缩略图
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("img");
//        req.message = msg;
//        req.scene = friends ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//        api.sendReq(req);
//    }
//
//    public void sendImg(Context context, String title, String content, String imgUrl, int THUMB_SIZE, boolean friends) {
//        initAPI(context);
//        try {
//            WXImageObject imgObj = new WXImageObject();
//            imgObj.imageUrl = imgUrl;
//
//            WXMediaMessage msg = new WXMediaMessage();
//            msg.mediaObject = imgObj;
//
//            Bitmap bmp = BitmapFactory.decodeStream(new URL(imgUrl).openStream());
//            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//            bmp.recycle();
//            msg.thumbData = WeixinModel2ByteUtil.bmpToByteArray(thumbBmp, true);
//
//            SendMessageToWX.Req req = new SendMessageToWX.Req();
//            req.transaction = buildTransaction("img");
//            req.message = msg;
//            req.scene = friends ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//            api.sendReq(req);
//        } catch (Exception e) {
//        }
//    }
//
//    public void sendMusic(Context context, String url, String title, String content, int resId, boolean friends) {
//        initAPI(context);
//        WXMusicObject music = new WXMusicObject();
//        music.musicUrl = url;
//        WXMediaMessage msg = new WXMediaMessage();
//        msg.mediaObject = music;
//        msg.title = title;
//        msg.description = content;
//        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), resId);
//        msg.thumbData = WeixinModel2ByteUtil.bmpToByteArray(thumb, true);
//
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("music");
//        req.message = msg;
//        req.scene = friends ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//        api.sendReq(req);
//    }
//
//    public void sendVedio(Context context, String videoUrl, String title, String content, int resId, boolean friends) {
//        initAPI(context);
//        WXVideoObject video = new WXVideoObject();
//        video.videoUrl = videoUrl;
//
//        WXMediaMessage msg = new WXMediaMessage(video);
//        msg.title = title;
//        msg.description = content;
//        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), resId);
//        msg.thumbData = WeixinModel2ByteUtil.bmpToByteArray(thumb, true);
//
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("video");
//        req.message = msg;
//        req.scene = friends ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//        api.sendReq(req);
//    }
//
//    public void sendWebPage(Context context, String webpageUrl, String title, String content, int resId, boolean friends) {
//        initAPI(context);
//        WXWebpageObject webpage = new WXWebpageObject();
//        webpage.webpageUrl = webpageUrl;
//
//        WXMediaMessage msg = new WXMediaMessage(webpage);
//        msg.title = title;
//        msg.description = content;
//        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), resId);
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(thumb, 120, 120, true);
//        thumb.recycle();
//
//        msg.thumbData = WeixinModel2ByteUtil.bmpToByteArray(thumbBmp, true);
//        thumbBmp.recycle();
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("webpage");
//        req.message = msg;
//        req.scene = friends ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//        if (api.sendReq(req)) {
//
//        }
//    }
//
//    public void sendWebPage(Context context, String webpageUrl, boolean friends) {
//        initAPI(context);
//        WXWebpageObject webpage = new WXWebpageObject();
//        webpage.webpageUrl = webpageUrl;
//        WXMediaMessage msg = new WXMediaMessage(webpage);
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("webpage");
//        req.message = msg;
//        req.scene = friends ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//        api.sendReq(req);
//    }
//
//    private void initAPI(Context context) {
//        synchronized (lockObj) {
//            if (api == null) {
//                if (APP_ID == null) {
//                    APP_ID = ParseConfig.getInstance(context).getConfigInfo().get("wxapp-id");
//                }
//                // 这种注册方式会向微信官方请求校验身份
//                api = WXAPIFactory.createWXAPI(context.getApplicationContext(), APP_ID, true);
//                // 将该app注册到微信
//                api.registerApp(APP_ID);
//            }
//        }
//    }
//
//    private String buildTransaction(String type) {
//        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
//    }
//
//    public interface GetUserInfoListener {
//        void onResp(String userInfo);
//    }
//
//    /**
//     * 微信分享
//     *
//     * @param jo      json的参数
//     * @param context 上下文
//     * @throws JSONException
//     * @throws UnsupportedEncodingException
//     */
//    public void weixinShare(JSONObject jo, Context context) throws JSONException, UnsupportedEncodingException {
//        String sign = jo.getString("flag");
//        String msg = jo.getString("content");
//        String friends = jo.getString("friend");
//        boolean share_friends = AppConstants.WEIXIN_SHARE_FRIEND;// 默认分享到朋友圈
//        int number = 1;
//        try {
//            number = Integer.parseInt(sign);
//        } catch (Exception e) {
//        }
//        if (number == 1) {// 代表一个文字
//            try {// 进行捕捉 如果没有设置 那么分享到朋友圈
//                share_friends = Integer.parseInt(friends) != 2;// 2是分享到会话
//            } catch (Exception e) {
//            }
//            sendText(context, msg, share_friends);
//        } else if (number == 2) {// 代表一个url
//            String title = jo.getString("title");
//            title = URLDecoder.decode(title, "UTF-8");
//            String content = jo.getString("describe");
//            content = URLDecoder.decode(content, "UTF-8");
//            try {// 进行捕捉 如果没有设置 那么分享到朋友圈
//                share_friends = Integer.parseInt(friends) != 2;// 2是分享到朋友圈
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            int resId;
//            resId = context.getResources().getIdentifier("logo_share", "drawable", context.getPackageName());
//            if (resId == 0) {
//                resId = context.getResources().getIdentifier("ic_launcher", "drawable", context.getPackageName());
//            }
//            sendWebPage(context, msg, title, content, resId, share_friends);
//        }
//        isLoginOrShare = true;
//    }
//}
