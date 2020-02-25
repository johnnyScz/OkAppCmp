package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.net.bean.IntentBean;
import com.xinyu.newdiggtest.net.bean.paramsForIntent;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置挑战金
 */
public class SetFineActivity extends BaseNoEventActivity {


    @BindView(R.id.edt_money)
    public EditText money;

    @BindView(R.id.targe_name)
    public TextView targe_name;


    @BindView(R.id.btn_return)
    public Button commitBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        targe_name.setText(MyTextUtil.getDecodeStr(getIntent().getStringExtra(IntentParams.Target_Name)) +
                DateUtil.timeOnlyDot(getIntent().getStringExtra(IntentParams.STATE_DATE)) + "-" + DateUtil.timeOnlyDot(getIntent().getStringExtra(IntentParams.END_DATE)));

        commitBtn.setEnabled(false);
        money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String money = s.toString();
                if (MyTextUtil.isEmpty(money)) {
                    commitBtn.setEnabled(false);
                } else {
                    commitBtn.setEnabled(true);
                }

            }
        });


    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_set_fine;
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @OnClick(R.id.tv_gowhere)
    public void goInfo() {
        startActivity(new Intent(mContext, FineGoWhereeActivity.class));
    }


    @OnClick(R.id.btn_return)
    public void goCommit() {
        goTargetBangFine();
    }

    @OnClick(R.id.tv)
    public void goreback() {
        finish();
    }


    /**
     * 目标绑定挑战金
     */
    private void goTargetBangFine() {

        if (Integer.parseInt(money.getText().toString()) == 0) {
            ToastUtils.getInstanc().showToast("挑战金最小额度为1元");
            return;
        }


        if (Integer.parseInt(money.getText().toString().trim()) > 10000) {
            ToastUtils.getInstanc().showToast("挑战金最大额度为10000元");
            return;
        }


        Intent intent = getIntent();
        intent.setClass(mContext, PayRismActivity.class);
        intent.putExtra("money", money.getText().toString().trim());
        paramsForIntent.TargetTiaozhan = new IntentBean();
        paramsForIntent.TargetTiaozhan.object_name = MyTextUtil.getUrl3Encoe(MyTextUtil.getDecodeStr(getIntent().getStringExtra(IntentParams.Target_Name)));
        paramsForIntent.TargetTiaozhan.target_id = getIntent().getStringExtra(IntentParams.DAKA_Target_Id);
        paramsForIntent.TargetTiaozhan.start_time = getIntent().getStringExtra(IntentParams.STATE_DATE);
        paramsForIntent.TargetTiaozhan.end_time = getIntent().getStringExtra(IntentParams.END_DATE);
        paramsForIntent.TargetTiaozhan.money = money.getText().toString().trim();
        paramsForIntent.TargetTiaozhan.type = "0";
        paramsForIntent.TargetTiaozhan.relevant_type = "0";
        paramsForIntent.TargetTiaozhan.ballanceType = "1";
        paramsForIntent.TargetTiaozhan.inorout_type = "2";


        intent.putExtra(IntentParams.Intent_Enter_Type, "tiaozhan_fine");
        startActivity(intent);
        finish();
    }


}




