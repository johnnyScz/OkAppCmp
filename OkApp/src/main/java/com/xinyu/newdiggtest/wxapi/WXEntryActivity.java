package com.xinyu.newdiggtest.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xinyu.newdiggtest.h5.H5XshellEvent;
import com.xinyu.newdiggtest.net.ApiConfig;
import com.xinyu.newdiggtest.utils.PreferenceUtil;


import org.greenrobot.eventbus.EventBus;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册API
        api = WXAPIFactory.createWXAPI(this, ApiConfig.WXAPP_ID);
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    boolean isHasSend = false;

    //  发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {

        if (resp instanceof SendAuth.Resp) {
            SendAuth.Resp newResp = (SendAuth.Resp) resp;
            //获取微信传回的code
            String code = newResp.code;
            PreferenceUtil.getInstance(this).setWxCode(code);
            WeixinUtil.getInstance().saveWXCode(code);

            if (!isHasSend) {
                isHasSend = true;
                EventBus.getDefault().postSticky(new H5XshellEvent(0x11));

                finish();
            }
        }


        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
//                if (AppContacts.WXTag == 2)
//                    ToastUtils.getInstanc().showToast("分享成功！");
                //发送成功
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                ToastUtils.getInstanc().showToast("分享取消！");
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.w("amtf", "发送取消ERR_AUTH_DENIEDERR_AUTH_DENIEDERR_AUTH_DENIED");
                //发送被拒绝
                break;
            default:
                Log.w("amtf", "发送返回breakbreakbreak");
                //发送返回
                break;
        }
        finish();

    }

}
