package com.xinyu.newdiggtest.h5;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.dommy.qrcode.util.Constant;
import com.google.zxing.activity.CaptureActivity;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xinyu.newdiggtest.APPConstans;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.net.ApiConfig;
import com.xinyu.newdiggtest.office.OfficeX5CoreActivity;
import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.wxapi.WeixinUtil;
import com.xshell.xshelllib.application.AppContext;
import com.xshell.xshelllib.utils.XshellEvent;
import com.xshell.xshelllib.utils.PhoneInfo;
import com.xshell.xshelllib.utils.VersionUtil;
import com.xshell.xshelllib.utils.XshellConsts;

import org.apache.cordova.CallbackContext;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;


public class H5Service extends Service {

    CallbackContext callBack;


    Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        EventBus.getDefault().register(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction("SCAN_SQcode");
        registerReceiver(receiver, filter);


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(receiver);

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {

        callBack = event.object;

        switch (event.what) {
            case XshellConsts.UserInfo:


                requestUserInfo();
                break;


            case XshellConsts.DISTANCE:

                jisuanDistance(event.msg, event.extra);
                break;


            case XshellConsts.SCan:


                startActivity(new Intent(mContext, CaptureActivity.class));

                break;


            case XshellConsts.Call:

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + event.msg);
                callIntent.setData(data);
                startActivity(callIntent);

                break;

            case XshellConsts.IM_Chat:

                break;

            case XshellConsts.New_Browser:


                break;

            case XshellConsts.WXLittle:

//                shareToWxLittle(event.params);

                shareToWxLittle(event.extra);

                break;


            case XshellConsts.APP_Status:

                int status;
                if (AppContacts.isBackrond) {
                    status = 1;
                } else {
                    status = 2;
                }
                event.object.success(status);
                break;


            case XshellConsts.PUSH_INFO:

                if (!MyTextUtil.isEmpty(APPConstans.PUSH_INFO)) {
                    try {

                        JSONObject jsonObject = new JSONObject(APPConstans.PUSH_INFO);
                        callBack.success(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case 0x11:
                rebackReq();
                break;


            case XshellConsts.Office_Open_file:
                String path = event.msg;
                if (path.toLowerCase().contains(".jeg") || path.toLowerCase().contains(".jpeg") || path.toLowerCase().contains(".png")) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("path", event.msg);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(mContext, OfficeX5CoreActivity.class);
                    intent.putExtra("path", event.msg);
                    startActivity(intent);
                }
                break;

            case XshellConsts.DeviceInfo:
                PhoneInfo phoneInfo = PhoneInfo.getInstance(AppContext.CONTEXT);
                String token = PreferenceUtil.getInstance(mContext).getUmengToken();
                JSONObject json = null;
                try {
                    json = new JSONObject();
                    json.put("version", VersionUtil.getVersionName(mContext));
                    json.put("deviceid", phoneInfo.getDeviceID());
                    json.put("devicetoken", token);
                    json.put("model", phoneInfo.getModel());
                    json.put("release", "Android" + phoneInfo.getSystemVersion());
                    json.put("pixels", phoneInfo.getPixels());
                    json.put("result", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callBack.success(json);
                break;
        }

    }


    LatLonPoint begin, end;

    private void jisuanDistance(final String msg, final String extra) {

        GeocodeSearch geocoderSearch = new GeocodeSearch(mContext);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {

                if (rCode == 1000) {

                    if (geocodeResult.getGeocodeAddressList() == null || geocodeResult.getGeocodeAddressList().size() < 1) {
                        callFail();
                    } else {

                        if (geocodeResult.getGeocodeQuery().getLocationName().equals(msg)) {
                            begin = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();

                        } else if (geocodeResult.getGeocodeQuery().getLocationName().equals(extra)) {
                            end = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
                        }

                        if (begin != null && end != null) {

                            LatLng llbegin = new LatLng(begin.getLatitude(), begin.getLongitude());
                            LatLng llend = new LatLng(end.getLatitude(), end.getLongitude());

                            float distance = AMapUtils.calculateLineDistance(llbegin, llend);

                            JSONObject object = new JSONObject();
                            try {
                                object.put("resultCode", 9000);
                                object.put("resultStr", "succed");
                                object.put("distance", distance);
                                callBack.success(object);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                } else {
                    callFail();
                }

            }
        });
        GeocodeQuery query = new GeocodeQuery(msg, "上海市");
        geocoderSearch.getFromLocationNameAsyn(query);

        GeocodeQuery query2 = new GeocodeQuery(extra, "上海市");
        geocoderSearch.getFromLocationNameAsyn(query2);
    }

    private void callFail() {
        JSONObject object = new JSONObject();
        try {
            object.put("resultCode", -1);
            object.put("resultStr", "fail");
            callBack.success(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void shareToWxLittle(String params) {

        try {
//            JSONObject object = params.getJSONObject(0);
//            String webpageurl = object.getString("webpageurl");
//            String username = object.getString("username");
//            String path = object.getString("path");
//            String image = object.getString("image");
//            String title = object.getString("title");
//            String describe = object.getString("describe");


            WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();

//            miniProgramObj.webpageUrl = webpageurl + "?" + "fid=1602&roomid=1458&companyid=1";

            miniProgramObj.webpageUrl = "http://www.qq.com"; // 兼容低版本的网页链接

            miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
            miniProgramObj.userName = ApiConfig.WxLittle;     // 小程序原始id

            Log.e("amtf", "小程序分享：" + ApiConfig.WxLittle);

            miniProgramObj.path = "pages/index/index";            //小程序页面路径
            WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
            msg.title = "OK!讨论组";                    // 小程序消息title
            msg.description = "小程序的RoomName";               // 小程序消息desc


            msg.thumbData = getThumb();

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;  // 目前只支持会话
            req.transaction = "miniProgram";

            IWXAPI api = WXAPIFactory.createWXAPI(this, ApiConfig.WXAPP_ID);
            api.sendReq(req);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] getThumb() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.company_logo);
        int bytes = bitmap.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
        byte[] data = buffer.array(); //Get the bytes array of the bitmap

        return data;

    }

    //分享微信对话框
    private void goWxShareD(XshellEvent event) {
        try {
            JSONObject obj = event.params.getJSONObject(0);
            int shareType = obj.getInt("friend");
            String pageUrl = obj.getString("content");
            String title = obj.getString("title");
            String descrip = obj.getString("describe");


            if (shareType == 1) {//朋友圈

                WeixinUtil.getInstance().sendWebPage(mContext, pageUrl, title,
                        descrip, R.mipmap.ic_launcher, true);


            } else if (shareType == 2) {//分享好友

                WeixinUtil.getInstance().diggWxShare(mContext, pageUrl, title,
                        descrip, R.mipmap.ic_launcher, false);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void rebackReq() {

        WeixinUtil.getInstance().loadWXUserInfo(ApiConfig.WXAPP_APPSecret, WeixinUtil.getInstance().getWXCode());

    }


    public void requestUserInfo() {


        JSONObject argObj = new JSONObject();
        try {

            argObj.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
            argObj.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
            argObj.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());
            argObj.put("name", PreferenceUtil.getInstance(mContext).getNickName());


            callBack.success(argObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    /**
     * 微信登录
     */
    private void goWxLogin() {


        WeixinUtil weixinInstance = WeixinUtil.getInstance();

        if (weixinInstance.getWeixinApi() == null) {
            weixinInstance.initWeixinApi(mContext);
        }
        boolean isSuccess = WeixinUtil.getInstance().weixinSendReq();
        if (isSuccess) {
        } else {
            Log.e("amtf", "你还未安装微信");
        }
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("SCAN_SQcode")) {

                String url = intent.getStringExtra(Constant.INTENT_EXTRA_KEY_QR_SCAN);
                callBack.success(url);
            }


        }
    };


}
