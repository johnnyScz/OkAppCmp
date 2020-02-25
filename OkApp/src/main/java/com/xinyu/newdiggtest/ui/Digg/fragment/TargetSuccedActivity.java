package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
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

public class TargetSuccedActivity extends BaseNoEventActivity {

    @BindView(R.id.tv_monitor)
    public TextView name;

    @BindView(R.id.edt_money)
    public EditText editText;

    @BindView(R.id.btn_commit)
    public Button commit;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_target__succed;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra(IntentParams.SELECT_MONITOR)) {
            String names = getIntent().getStringExtra(IntentParams.SELECT_MONITOR);
            if (!MyTextUtil.isEmpty(names)) {
                name.setText("监督人:" + names);
            } else {
                name.setText("");
            }

        } else {
            name.setText("");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {

                } else {
                    int money = Integer.parseInt(s.toString());
                    if (money > 10000) {
                        ToastUtils.getInstanc(mContext).showToast("挑战金最大额度为10000元");
                        commit.setEnabled(false);
                    } else {
                        commit.setEnabled(true);
                    }
                }


            }
        });
    }


    @OnClick(R.id.icon_back)
    public void goCommit() {
        finish();
    }

    @OnClick(R.id.tv_goback)
    public void goBack() {

        finish();
    }


    @OnClick(R.id.tv_gowhere)
    public void goWhere() {
        startActivity(new Intent(mContext, FineGoWhereeActivity.class));
    }


    @OnClick(R.id.btn_commit)
    public void goTz() {
        if (TextUtils.isEmpty(editText.getText())) {
            ToastUtils.getInstanc(mContext).showToast("请输入挑战金额");
            return;
        }

        String moneyStr = editText.getText().toString();
        if (Integer.parseInt(moneyStr) == 0) {
            ToastUtils.getInstanc(mContext).showToast("挑战金额不能为0");
            return;
        }

        paramsForIntent.TargetTiaozhan = new IntentBean();
        paramsForIntent.TargetTiaozhan.object_name = MyTextUtil.getUrl3Encoe(getIntent().getStringExtra(IntentParams.Target_Name));
        paramsForIntent.TargetTiaozhan.target_id = getIntent().getStringExtra(IntentParams.DAKA_Target_Id);
        paramsForIntent.TargetTiaozhan.start_time = getIntent().getStringExtra(IntentParams.STATE_DATE);
        paramsForIntent.TargetTiaozhan.end_time = getIntent().getStringExtra(IntentParams.END_DATE);
        paramsForIntent.TargetTiaozhan.money = editText.getText().toString().trim();
        paramsForIntent.TargetTiaozhan.type = "0";
        paramsForIntent.TargetTiaozhan.relevant_type = "0";
        paramsForIntent.TargetTiaozhan.ballanceType = "1";
        paramsForIntent.TargetTiaozhan.inorout_type = "2";

        Intent intent = getIntent();
        intent.setClass(mContext, PayRismActivity.class);
        intent.putExtra("money", editText.getText().toString().trim());
//        intent.putExtra(IntentParams.Pay_m_type, "0");
//        intent.putExtra(IntentParams.Pay_relevant_type, "0");
//        intent.putExtra(IntentParams.Now_cash, "1");
        intent.putExtra(IntentParams.Intent_Enter_Type, "tiaozhan_fine");
        startActivity(intent);
        finish();

    }


}




