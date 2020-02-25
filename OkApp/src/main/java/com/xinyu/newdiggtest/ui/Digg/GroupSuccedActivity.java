package com.xinyu.newdiggtest.ui.Digg;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.ShareUrlBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.wxapi.WeixinUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GroupSuccedActivity extends Activity {

    @BindView(R.id.tv_title)
    public TextView title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_succed);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        title.setText("群组");
    }

    @OnClick(R.id.iv_back)
    public void pageBack() {
        finish();
    }

    @OnClick(R.id.btn_invitewx)
    public void commitGName() {
        commitInviteWx();
    }

    private void commitInviteWx() {
        //btn_invitewx
        getInviteUrl();
    }


    private void doShareWxUrl(String url) {
        WeixinUtil.getInstance().diggWxShare
                (this, url, "一起OK！",
                        "您的好友" + PreferenceUtil.getInstance(this).getNickName() + "邀请你加入" +
                                getIntent().getStringExtra("roomName"), R.mipmap.ic_launcher, false);
    }

    private void getInviteUrl() {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_type", "customer");
        map.put("use_to", "2");
        map.put("relation_id", getIntent().getStringExtra("roomId"));
        map.put("share_user_id", PreferenceUtil.getInstance(GroupSuccedActivity.this).getUserId());

        url.getShareUrl(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShareUrlBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        ToastUtils.getInstanc(GroupSuccedActivity.this).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(ShareUrlBean msg) {
                        PreferenceUtil.getInstance(GroupSuccedActivity.this).setWxToken(msg.getToken());
                        doShareWxUrl(msg.getUrl());
                    }
                });


    }


}




