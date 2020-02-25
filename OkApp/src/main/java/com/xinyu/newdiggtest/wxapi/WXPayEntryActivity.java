package com.xinyu.newdiggtest.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xinyu.newdiggtest.net.ApiConfig;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.EventConts;

import org.greenrobot.eventbus.EventBus;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//          setContentView(R.layout.activity_pay_succed);
        IWXAPI api = WXAPIFactory.createWXAPI(this, ApiConfig.WXAPP_ID);
        api.handleIntent(getIntent(), this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        WeixinUtil.getInstance().initWeixinApi(this);
        WeixinUtil.getInstance().getWeixinApi().handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WeixinUtil.getInstance().getWeixinApi().handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq req) {
    }


    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {


            if (resp.errCode == 0) {
                EventBus.getDefault().post(new XshellEvent(EventConts.MSG_WX_Succed));

            } else if (resp.errCode == -2) {
                EventBus.getDefault().postSticky(new XshellEvent(EventConts.MSG_WX_Failure));
            } else if (resp.errCode == -3) {
                EventBus.getDefault().postSticky(new XshellEvent(EventConts.MSG_WX_Failure));
            }
            finish();
        }
    }


}
