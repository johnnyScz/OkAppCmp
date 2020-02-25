package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.wxapi.PayResult;
import com.xinyu.newdiggtest.wxapi.WxPayUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 充值确认
 */
public class ChargeConfirmActivity extends BaseNoEventActivity {


    @BindView(R.id.money)
    public TextView money;

    @BindView(R.id.zfb_pay)
    public LinearLayout zfb_pay;

    @BindView(R.id.wxpay)
    public LinearLayout wxpay;

    int payTag = 0;//1 支付宝 2 微信(0未选择)


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_charge_confirm;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initPayView();
        money.setText("¥" + getIntent().getStringExtra(IntentParams.Money));
    }

    /**
     * 初始化支付视图
     */
    private void initPayView() {
        zfb_pay.setTag(false);
        wxpay.setTag(false);
        payTag = 0;
    }


    @OnClick(R.id.btn_commit)
    public void goCommit() {
        switch (payTag) {

            case 0:
                ToastUtils.getInstanc().showToast("请选择支付方式");
                return;

            case 1:
                zfbPay();
                break;

            case 2:
                payWx();
                break;


        }


    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }

    @OnClick(R.id.zfb_pay)
    public void zfbClick() {
        boolean isCheck = !(boolean) zfb_pay.getTag();
        zfb_pay.setTag(isCheck);

        if (isCheck) {
            zfb_pay.setBackgroundResource(R.drawable.shaper_wallet);
            payTag = 1;
            resetView(1);
        } else {
            zfb_pay.setBackgroundResource(R.drawable.shaper_bg_ed);
            payTag = 0;
        }

    }

    private void resetView(int type) {
        if (type == 1) {
            wxpay.setTag(false);
            wxpay.setBackgroundResource(R.drawable.shaper_bg_ed);
        } else if (type == 2) {
            zfb_pay.setTag(false);
            zfb_pay.setBackgroundResource(R.drawable.shaper_bg_ed);
        }

    }

    @OnClick(R.id.wxpay)
    public void wxClick() {
        boolean isCheck = !(boolean) wxpay.getTag();
        wxpay.setTag(isCheck);
        if (isCheck) {
            wxpay.setBackgroundResource(R.drawable.shaper_wallet);
            payTag = 2;
            resetView(2);
        } else {
            wxpay.setBackgroundResource(R.drawable.shaper_bg_ed);
            payTag = 0;
        }

    }


    private void zfbPay() {
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "ok-api.TargetOrderForSelfAction");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("money", getIntent().getStringExtra(IntentParams.Money));
        requsMap.put("prod_name", "支付宝充值");

        requsMap.put("payer", PreferenceUtil.getInstance(mContext).getUserId());//付款人
        requsMap.put("pay_type", "2");//1微信 2 支付宝
        requsMap.put("systemno", "ok");
        requsMap.put("user_client_ip", AppUtils.getIpStr());

        requsMap.put("type", "4");//0 挑战金 1 打赏 2.奖励金 3vip 4.充值

        requsMap.put("inorout_type", "1");//1 收入 2.支出
        requsMap.put("desc", "支付宝充值");

        WxPayUtil payUtil = WxPayUtil.getInstance(mContext);
        payUtil.setDialog(loadingDailog);
        payUtil.setHandler(mhandler);
        payUtil.getZfbReq(requsMap, this);
    }


    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == EventConts.MSG_WX_Succed) {
                Map<String, String> meess = (Map) msg.obj;

                PayResult payResult = new PayResult((meess));
                String resultInfo = payResult.getResultStatus();// 同步返回需要验证的信息
                if (resultInfo.equals("6001")) {
                    ToastUtils.getInstanc(App.mContext).showToast("支付取消");
                } else if (resultInfo.equals("9000")) {
                    String zfbMoney = "0";
                    String result = payResult.getResult();
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONObject child = object.getJSONObject("alipay_trade_app_pay_response");
                        zfbMoney = child.getString("total_amount");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent itent = new Intent(mContext, PaySuccedActivity.class);
                    itent.putExtra("payMoney", zfbMoney);
                    itent.putExtra(IntentParams.Intent_Enter_Type, "ChargeConfirmActivity");//充值

                    startActivity(itent);
                    finish();

                } else {
                    startActivity(new Intent(mContext, PayFailActivity.class));
                }
            }
        }
    };


    private void payWx() {
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "ok-api.TargetOrderForSelfAction");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("money", getIntent().getStringExtra(IntentParams.Money));
        requsMap.put("prod_name", "微信充值");
        requsMap.put("payer", PreferenceUtil.getInstance(mContext).getUserId());//付款人
        requsMap.put("pay_type", "1");//1微信 2 支付宝
        requsMap.put("systemno", "ok");
        requsMap.put("user_client_ip", AppUtils.getIpStr());

        requsMap.put("type", "4");//0 挑战金 1 打赏 2.奖励金 3.vip 4 充值
        requsMap.put("inorout_type", "1");//1 收入 2.支出
        requsMap.put("desc", "微信充值");

        WxPayUtil payUtil = WxPayUtil.getInstance(mContext);
        payUtil.setDialog(loadingDailog);
        payUtil.getReq(requsMap);

    }


}




