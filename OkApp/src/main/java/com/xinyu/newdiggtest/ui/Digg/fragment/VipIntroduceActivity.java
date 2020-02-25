package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.BaseActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;


public class VipIntroduceActivity extends BaseActivity {


    @BindView(R.id.vip1)
    public ImageView vipTag;


    @BindView(R.id.btn_commint)
    public View goVIp;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_vip_introduce;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        String tag = getIntent().getStringExtra(IntentParams.IsVIP);
        if (tag.equals("N")) {
            vipTag.setImageResource(R.mipmap.section1_no);
            goVIp.setVisibility(View.VISIBLE);
            setGoVipListner();

        } else if (tag.equals("Y")) {
            vipTag.setImageResource(R.mipmap.section1_yes);
            goVIp.setVisibility(View.GONE);
        }


    }

    private void setGoVipListner() {
        vipTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.getInstanc().showToast("vipTag去开通vip");
            }
        });

        goVIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PayVipActivity.class));

            }
        });
    }

    @OnClick(R.id.iv_back)
    public void goCommit() {
        finish();
    }


    @OnClick(R.id.vip_2)
    public void goVip2() {
        ToastUtils.getInstanc().showToast("暂未开通，敬请期待");
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == EventConts.MSG_Finish_VIP) {
            finish();
        }

    }


}
