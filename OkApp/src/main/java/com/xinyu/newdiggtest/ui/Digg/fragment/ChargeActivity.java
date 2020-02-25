package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.BaseActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class ChargeActivity extends BaseActivity {


    @BindView(R.id.edt_money)
    public EditText money;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_charge;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

    }

    private void initView() {
        money.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
    }


    @OnClick(R.id.btn_commit)
    public void goCommit() {
        if (MyTextUtil.isEmpty(money.getText().toString())) {
            ToastUtils.getInstanc().showToast("请输入充值金额");
            return;
        }

        float moneyCount = Float.parseFloat(money.getText().toString());

        if (moneyCount < 1) {
            ToastUtils.getInstanc().showToast("最小充值金额为1元");
            return;
        }

        Intent mintent = new Intent(mContext, ChargeConfirmActivity.class);
        mintent.putExtra(IntentParams.Money, money.getText().toString());
        startActivity(mintent);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == EventConts.FiNISH_INPUT_MONEY) {
            finish();
        }

    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }

}




