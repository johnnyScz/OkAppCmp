package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class PaySuccedActivity extends BaseNoEventActivity {


    @BindView(R.id.pay_money)
    public TextView money;


    @BindView(R.id.title)
    public TextView title;

    @BindView(R.id.sub_title)
    public TextView sub_title;


    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        money.setText(getIntent().getStringExtra("payMoney"));


        if (getIntent().hasExtra(IntentParams.Intent_Enter_Type)
                && getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("ChargeConfirmActivity")) {

            title.setText("充值成功");
            sub_title.setText("充值成功");

        } else {
            title.setText("支付成功");
            sub_title.setText("支付成功");
        }


    }


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_pay_succed;
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        if (getIntent().hasExtra(IntentParams.FromVIP)) {
            PreferenceUtil.getInstance(mContext).setIsVip(true);
        }
        EventBus.getDefault().postSticky(new XshellEvent(EventConts.FiNISH_INPUT_MONEY));

        EventBus.getDefault().post(new XshellEvent(EventConts.HomeFresh));
        finish();
    }


    @OnClick(R.id.btn_return)
    public void goCommit() {
        if (getIntent().hasExtra(IntentParams.FromVIP)) {
            PreferenceUtil.getInstance(mContext).setIsVip(true);
            EventBus.getDefault().postSticky(new XshellEvent(EventConts.MSG_Finish_VIP));
        }

        EventBus.getDefault().postSticky(new XshellEvent(EventConts.FiNISH_INPUT_MONEY));
        EventBus.getDefault().post(new XshellEvent(EventConts.HomeFresh));
        finish();
    }


}




