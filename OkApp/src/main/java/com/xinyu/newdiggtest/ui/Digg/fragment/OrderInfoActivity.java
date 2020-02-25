//package com.xinyu.newdiggtest.ui.Digg.fragment;
//
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.widget.TextView;
//
//import com.xinyu.newdiggtest.R;
//
//import com.xinyu.newdiggtest.bean.OrderReturnBean;
//import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
//import com.xinyu.newdiggtest.utils.MyTextUtil;
//
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
//
///**
// * 订单详情
// */
//public class OrderInfoActivity extends BaseNoEventActivity {
//
//    OrderReturnBean.OrderlistBean mydata;
//
//
//    @BindView(R.id.content)
//    public TextView content;
//
//    @BindView(R.id.pay_money)
//    public TextView payMoney;
//
//    @BindView(R.id.state)
//    public TextView state;
//
//    @BindView(R.id.paydesc)
//    public TextView paydesc;//支付说明
//
//    @BindView(R.id.pay_time)
//    public TextView pay_time;//支付时间
//
//
//    @BindView(R.id.cush_time)
//    public TextView cush_time;//到账时间
//
//    @BindView(R.id.account)
//    public TextView account;//账户
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        mydata = (OrderReturnBean.OrderlistBean) getIntent().getSerializableExtra("data");
//
//        initData();
//
//    }
//
//    private void initData() {
//
//        if (MyTextUtil.isEmpty(mydata.getF_content())) {
//            if (mydata.getF_pay_type().equals("3"))
//                content.setText("提现");
//        } else {
//            content.setText(MyTextUtil.getDecodeStr(mydata.getF_content()));
//        }
//
//
//        payMoney.setText(mydata.getF_money());
//
//        if (MyTextUtil.isEmpty(mydata.getF_desc())) {
//            paydesc.setText("提现");
//        } else {
//            paydesc.setText(mydata.getF_desc());
//        }
//
//        account.setText(mydata.getF_order_id());
//
//        String inorout_type = mydata.getF_inorout_type();
//        String addHead = "";
//
//        if (!MyTextUtil.isEmpty(inorout_type) && inorout_type.equals("3")) {
//            String fState = MyTextUtil.isEmpty(mydata.getF_forum_state()) ? "2" : mydata.getF_forum_state();
//            if (fState.equals("2")) {
//                addHead = "审核中";
//            } else if (fState.equals("3")) {
//                addHead = "已审核";
//            } else if (fState.equals("4")) {
//                addHead = "已到账";
//            } else if (fState.equals("5")) {
//                addHead = "已拒绝";
//            } else {
//                addHead = "审核中";
//            }
//        } else {
//            String state = mydata.getF_pay_state();
//            if (state.equals("2")) {
//                addHead = "支付成功";
//            } else {
//                addHead = "支付失败";
//            }
//        }
//        state.setText(addHead);
//
//        String time = mydata.getF_createtime();
//
//
//        pay_time.setText(time.substring(0, time.length() - 2));
//        cush_time.setText(time.substring(0, time.length() - 2));
//
//
//    }
//
//    @Override
//    protected int getLayoutResouce() {
//        return R.layout.activity_order_info;
//    }
//
//
//    @OnClick(R.id.icon_back)
//    public void goBack() {
//        finish();
//    }
//
//
//}
//
//
//
//
