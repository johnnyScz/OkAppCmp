package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.net.bean.IntentBean;
import com.xinyu.newdiggtest.net.bean.paramsForIntent;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class RewardActivity extends BaseNoEventActivity {

    @BindView(R.id.title)
    public TextView name;

    @BindView(R.id.toptitle)
    public TextView topTitle;

    @BindView(R.id.sub_title)
    public TextView sub_title;


    @BindView(R.id.edt_money)
    public EditText money;

    @BindView(R.id.commont)
    public EditText words;

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_reward;
    }

    boolean is200Limit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("daka_dashang")) {
            topTitle.setText("打赏");
            sub_title.setText("打赏金额");
            is200Limit = true;

        } else if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("target_reward")) {
            topTitle.setText("奖励");
            sub_title.setText("奖励金额");
            is200Limit = false;

        }


        name.setText(getIntent().getStringExtra(IntentParams.Target_Name));
    }


    @OnClick(R.id.btn_commit)
    public void goCommit() {
        if (TextUtils.isEmpty(money.getText())) {
            ToastUtils.getInstanc(mContext).showToast("请输入金额");
            return;
        }

        if (Integer.parseInt(money.getText().toString().trim()) == 0) {
            ToastUtils.getInstanc(mContext).showToast("金额不能为0");
            return;
        }

        if (is200Limit) {
            String moneyTx = money.getText().toString();
            if (Integer.parseInt(moneyTx) > 200) {
                ToastUtils.getInstanc(mContext).showToast("打赏金额不能超过200元");
                return;
            }
        } else {
            String moneyTx = money.getText().toString();
            if (Integer.parseInt(moneyTx) > 10000) {
                ToastUtils.getInstanc(mContext).showToast("奖励金额不能超过10000元");
                return;
            }
        }

        Intent intent = getIntent();
        intent.putExtra("money", money.getText().toString());
        intent.putExtra(IntentParams.Wish_Word, words.getText().toString());
        intent.putExtra(IntentParams.DAKA_Target_Id, getIntent().getStringExtra(IntentParams.DAKA_Target_Id));

        if (intent.getStringExtra(IntentParams.Intent_Enter_Type).equals("daka_dashang")) {
            paramsForIntent.TargetDakaDashang = new IntentBean();
            paramsForIntent.TargetDakaDashang.object_name = MyTextUtil.getUrl3Encoe(getIntent().getStringExtra(IntentParams.Target_Name));
            paramsForIntent.TargetDakaDashang.target_id = getIntent().getStringExtra(IntentParams.DAKA_Target_Id);
            paramsForIntent.TargetDakaDashang.start_time = getIntent().getStringExtra(IntentParams.STATE_DATE);
            paramsForIntent.TargetDakaDashang.end_time = getIntent().getStringExtra(IntentParams.END_DATE);
        } else if (intent.getStringExtra(IntentParams.Intent_Enter_Type).equals("target_reward")) {
            paramsForIntent.TargetReward = new IntentBean();
            paramsForIntent.TargetReward.object_name = MyTextUtil.getUrl3Encoe(getIntent().getStringExtra(IntentParams.Target_Name));
            paramsForIntent.TargetReward.target_id = getIntent().getStringExtra(IntentParams.DAKA_Target_Id);
            paramsForIntent.TargetReward.start_time = getIntent().getStringExtra(IntentParams.STATE_DATE);
            paramsForIntent.TargetReward.end_time = getIntent().getStringExtra(IntentParams.END_DATE);
            paramsForIntent.TargetReward.toUser = getIntent().getStringExtra(IntentParams.TO_USER);
        }
        intent.setClass(mContext, PayRismActivity.class);
        startActivity(intent);
        finish();
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }

}




