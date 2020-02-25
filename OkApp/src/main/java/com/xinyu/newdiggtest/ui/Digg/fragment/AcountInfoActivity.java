package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.OrderReturnBean;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.LeakActivity;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 收支详情
 */
public class AcountInfoActivity extends BaseNoEventActivity {

    @BindView(R.id.curent_state)
    TextView curent_state;

    @BindView(R.id.order_no)
    TextView order_no;

    @BindView(R.id.order_time)
    TextView order_time;

    @BindView(R.id.order_type)
    TextView order_type;

    @BindView(R.id.sub_title)
    TextView sub_title;

    @BindView(R.id.deal_type)
    TextView deal_type;

    @BindView(R.id.income_type)
    TextView income_type;

    @BindView(R.id.deal_state)
    TextView deal_state;

    @BindView(R.id.asotion_target)
    TextView asotion_target;

    @BindView(R.id.asotion_daka)
    TextView asotion_daka;

    @BindView(R.id.img_icon)
    ImageView img_icon;


    @BindView(R.id.pay_ll)
    LinearLayout pay_ll;


    @BindView(R.id.ll_target)
    LinearLayout ll_target;


    @BindView(R.id.ll_daka)
    LinearLayout ll_daka;


    OrderReturnBean.OrderlistBean mydata;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_acc_info;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        mydata = (OrderReturnBean.OrderlistBean) getIntent().getSerializableExtra("data");

        order_type.setText(getIntent().getStringExtra("title"));

        sub_title.setText(getIntent().getStringExtra("money"));

        showIcon();

        fillInfo();


    }

    private void showIcon() {

        String type = getIntent().getStringExtra("f_type");

        switch (type) {

            case "0":

                img_icon.setImageResource(R.mipmap.shouzhi_tiaozhan);

                break;

            case "1":

                img_icon.setImageResource(R.mipmap.shouzhi_dashang);
                break;

            case "2":
                img_icon.setImageResource(R.mipmap.shouzhi_jl);
                break;

            case "3":
                img_icon.setImageResource(R.mipmap.shouzhi_vip);
                break;

            case "4":
                img_icon.setImageResource(R.mipmap.sz_chongzhi);
                break;

            case "5":
                img_icon.setImageResource(R.mipmap.shouzhi_l1);
                break;

            case "6":
                img_icon.setImageResource(R.mipmap.shouzhi_l2);
                break;

            case "7":
                img_icon.setImageResource(R.mipmap.shouzhi_tixian);
                break;


        }


    }

    private void fillInfo() {
        if (mydata == null)
            return;

        order_no.setText(mydata.getF_order_id());

        if (!MyTextUtil.isEmpty(mydata.getF_createtime())) {
            order_time.setText(DateUtil.longToDateMMss(Long.parseLong(mydata.getF_createtime())));
        }


        String type = mydata.getF_inorout_type();
        if (type.equals("1")) {
            income_type.setText("收入");
        } else {
            income_type.setText("支出");
        }

        String payType = mydata.getF_pay_type();

        if (payType.equals("2")) {
            deal_type.setText("支付宝");
        } else if (payType.equals("1")) {
            deal_type.setText("微信");
        } else {
            deal_type.setText("钱包");
        }


        if (!MyTextUtil.isEmpty(mydata.getF_relevant_type())) {
            if (mydata.getF_relevant_type().equals("0")) {
                asotion_target.setText(MyTextUtil.getDecodeStr(getIntent().getStringExtra("tv_target")));
                ll_target.setVisibility(View.VISIBLE);
                ll_daka.setVisibility(View.GONE);
            } else if (mydata.getF_relevant_type().equals("1")) {
                asotion_daka.setText(MyTextUtil.getDecodeStr(getIntent().getStringExtra("tv_target")));
                ll_target.setVisibility(View.GONE);
                ll_daka.setVisibility(View.VISIBLE);
            }

        } else {
            ll_daka.setVisibility(View.GONE);
            ll_target.setVisibility(View.GONE);
        }


        curent_state.setText(getIntent().getStringExtra("curentState"));

        if (mydata.getF_type().equals("7")) {
            pay_ll.setVisibility(View.VISIBLE);
            if (mydata.getF_forum_state().equals("5")) {
                deal_state.setText("已拒绝");
            } else if (mydata.getF_forum_state().equals("4")) {
                deal_state.setText("打款成功");
            } else {
                deal_state.setText("审核中");
            }


        } else {
            pay_ll.setVisibility(View.GONE);

        }


    }

    @OnClick(R.id.icon_back)
    public void goBack() {

        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LeakActivity.class));
        finish();
    }


}




