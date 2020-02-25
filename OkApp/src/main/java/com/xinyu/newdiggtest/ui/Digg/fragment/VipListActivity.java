package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.VipListReturnBean;
import com.xinyu.newdiggtest.adapter.VipRecomendAdapter;
import com.xinyu.newdiggtest.adapter.VipRecomendBean;

import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.InfoData;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;

import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.wxapi.WeixinUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class VipListActivity extends BaseNoEventActivity {

    VipRecomendAdapter adpter;

    @BindView(R.id.recyclerView)
    public RecyclerView recyView;

    @BindView(R.id.empty)
    public View empty;

    List<VipRecomendBean> datas = new ArrayList<>();

    //--------显示钱数和下线人数--------------

    @BindView(R.id.total_ct)
    public TextView total_ct;

    @BindView(R.id.first_cc)
    public TextView first_cc;

    @BindView(R.id.second_ct)
    public TextView second_ct;

    @BindView(R.id.tv_account)
    public TextView tv_account;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_vip_list;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
        adpter = new VipRecomendAdapter(R.layout.item_recomentlist, datas);
        recyView.setAdapter(adpter);

        requestNet();
    }


    public void requestNet() {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "ok-api.getMyRelationship");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        url.getRecomendList(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VipListReturnBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(VipListReturnBean msg) {
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData() != null) {
                                showTop3Data(msg.getData());
                            }
                            if (msg.getDatalist() == null || msg.getDatalist().size() < 1) {
                                showEmpty(true, null);

                            } else {
                                showEmpty(false, msg.getDatalist());
                            }

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });
    }

    private void showEmpty(boolean show, List<VipRecomendBean> datalist) {
        if (show) {
            empty.setVisibility(View.VISIBLE);
            recyView.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            recyView.setVisibility(View.VISIBLE);
//            adpter.replaceData(datalist);

            adpter.addData(datalist);

        }

    }


    /**
     * 显示 推荐人数
     *
     * @param data
     */
    private void showTop3Data(VipListReturnBean.DataBean data) {

        String firstCount = data.getFirst_rec_users();
        String secondCount = data.getSecond_rec_users();
        String account = data.getReco_bonus();

        tv_account.setText(account);
        first_cc.setText(firstCount + "人");
        second_ct.setText(secondCount + "人");
        int count = Integer.parseInt(firstCount) + Integer.parseInt(secondCount);
        total_ct.setText(count + "人");

    }


    @OnClick(R.id.iv_back)
    public void goCommit() {
        finish();
    }


    @OnClick(R.id.btn_commint)
    public void goShare() {
        go2Share();
    }


    private void go2Share() {
        final VipShareFragmentDialog dialog = new VipShareFragmentDialog();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, "tag");

        dialog.setOnPopListner(new VipShareFragmentDialog.OnPopClickListner() {
            @Override
            public void onCancle() {
                dialog.dismiss();
            }


            @Override
            public void onShareWeixin() {
                dialog.dismiss();
                getLinkUrl(0);
            }

            @Override
            public void onShareCircle() {
                dialog.dismiss();
                getLinkUrl(1);
            }
        });

    }


    /**
     * 分享到微信或者朋友圈
     *
     * @param isCircle
     */
    private void getLinkUrl(final int isCircle) {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_type", "customer");
        map.put("use_to", "5");//vip分享
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("session_id", PreferenceUtil.getInstance(mContext).getSessonId());

        url.getLinkUrl(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InfoData>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(InfoData msg) {
                        if (!MyTextUtil.isEmpty(msg.getUrl())) {
                            if (isCircle == 0) {
                                goShareWechat(msg.getUrl());
                            } else if (isCircle == 1) {
                                goCircle(msg.getUrl());
                            }

                        }
                    }
                });
    }


    /**
     * 分享朋友圈
     *
     * @param pageUrl
     */
    private void goCircle(String pageUrl) {

        WeixinUtil.getInstance().sendWebPage(mContext, pageUrl, "您的好友" + PreferenceUtil.getInstance(mContext).getNickName() + "邀请您成为OK！VIP会员",
                "开通VIP可享有被打赏,被奖励及有奖推荐特权", R.mipmap.ic_launcher, true);


    }

    /**
     * 分享微信好友
     *
     * @param url
     */
    private void goShareWechat(String url) {
        WeixinUtil.getInstance().diggWxShare
                (this, url, "您的好友" + PreferenceUtil.getInstance(mContext).getNickName() + "邀请您成为OK！VIP会员",
                        "开通VIP可享有被打赏,被奖励及有奖推荐特权", R.mipmap.ic_launcher, false);
    }


    @OnClick(R.id.vip_info)
    public void goInfo() {
        Intent intent = new Intent(mContext, VipIntroduceActivity.class);
        intent.putExtra(IntentParams.IsVIP, "Y");
        startActivity(intent);

    }


}
