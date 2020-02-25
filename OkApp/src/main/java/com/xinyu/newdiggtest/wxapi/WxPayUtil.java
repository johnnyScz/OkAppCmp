package com.xinyu.newdiggtest.wxapi;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.android.tu.loadingdialog.LoadingDailog;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xinyu.newdiggtest.bean.PayBean;
import com.xinyu.newdiggtest.net.ApiConfig;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.ZfbInfo;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 微信/支付宝支付工具
 */
public class WxPayUtil {

    private static final Object lockObj = new Object();

    private static WxPayUtil instance;

    private static IWXAPI api;


    Handler handler;

    static Activity mCtx;

    public void setHandler(Handler mhander) {

        this.handler = mhander;

    }


    public static WxPayUtil getInstance(Activity ctx) {
        mCtx = ctx;
        if (instance == null) {
            synchronized (lockObj) {
                if (instance == null) {
                    instance = new WxPayUtil();
                    initWeixinApi(ctx);
                }
            }
        }
        return instance;
    }

    public static boolean initWeixinApi(Context context) {

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


    LoadingDailog mdialog;

    public void setDialog(LoadingDailog dialog) {
        this.mdialog = dialog;
    }


    /**
     * 生成订单
     */
    public void getReq(HashMap<String, String> map) {
        mdialog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        url.wxZfbPay(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PayBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mdialog.dismiss();
                        ToastUtils.getInstanc(mCtx).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(PayBean msg) {
                        mdialog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData() != null) {
                                wxPay(msg.getData());
                            }

                        } else {
                            ToastUtils.getInstanc(mCtx).showToast("服务异常");
                        }

                    }
                });
    }


    /**
     * 生成订单
     */
    public void getZfbReq(HashMap<String, String> map, final Activity ctx) {
        mdialog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        url.ZfbPay(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ZfbInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mdialog.dismiss();
                        ToastUtils.getInstanc(ctx).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(ZfbInfo msg) {
                        mdialog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData() != null) {
                                initZfbPay(msg.getData(), ctx);
                            }

                        } else {
                            ToastUtils.getInstanc(mCtx).showToast("服务异常");
                        }

                    }
                });
    }


    private void wxPay(PayBean.WxReqBean data) {
        final PayReq request = new PayReq();
        request.appId = ApiConfig.WXAPP_ID;
        request.partnerId = data.getMch_id();
        request.prepayId = data.getPrepay_id();
        request.packageValue = "Sign=WXPay";
        request.nonceStr = data.getNonce_str();
        request.timeStamp = data.getTimestamp();
        request.sign = data.getSign();

        if (api != null) {
            api.sendReq(request);
        }

    }


    /**
     * 支付宝支付的初始化
     */
    public void initZfbPay(final String orderInfo, final Activity ctx) {

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(ctx);
                Map<String, String> maps = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = EventConts.MSG_WX_Succed;
                msg.obj = maps;
                handler.sendMessage(msg);

            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }


}
