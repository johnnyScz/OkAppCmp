package com.xinyu.newdiggtest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import android.widget.EditText;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;

import com.xinyu.newdiggtest.bean.MsgBean;

import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;

import com.xinyu.newdiggtest.ui.Digg.punchcard.DakaSelectTargetActivity;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreatDakaActivity extends BaseActivity {


    @BindView(R.id.tv_daka_target)
    public TextView dataContent;

    @BindView(R.id.et_input_content)
    public EditText inputContent;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_create_daka;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        String name = getIntent().getStringExtra(IntentParams.SELECT_Target);
        targetId = getIntent().getStringExtra(IntentParams.SELECT_TargetId);
        dataContent.setText(name);
    }

    @OnClick(R.id.btn_publish)
    public void goCommit() {

        if (TextUtils.isEmpty(inputContent.getText().toString().trim())) {
            ToastUtils.getInstanc(this).showToast("请输入打卡内容！");
            return;
        }
        requestdatas();
    }

    private void requestdatas() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        String content = "";
        try {
            content = URLEncoder.encode(inputContent.getText().toString().trim(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("target_id", targetId);
        requsMap.put("content", content);
        requsMap.put("plan_date", DateUtil.getTodayStr());
        requsMap.put("user_id", PreferenceUtil.getInstance(this).getUserId());
        requsMap.put("sid", PreferenceUtil.getInstance(this).getSessonId());

        url.createDaka(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MsgBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(MsgBean msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            ToastUtils.getInstanc(mContext).showToast("创建成功！");
                            finish();
                            EventBus.getDefault().post(new XshellEvent(EventConts.DAKA_Publishi));

                        } else {
                            ToastUtils.getInstanc(mContext).showToast(mContext.getResources().getString(R.string.network_error));
                        }
                    }
                });
        ;
    }


    @OnClick(R.id.iv_back)
    public void goback() {
        finish();
    }


    @OnClick(R.id.ll_go_select)
    public void goSelect() {

        Intent intent = new Intent(mContext, DakaSelectTargetActivity.class);
        intent.putExtra(IntentParams.Intent_Enter_Type, "CreatDakaActivity");
        startActivityForResult(intent, 0x12);
    }


    String targetId;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == 0x14) {
            String name = data.getStringExtra(IntentParams.SELECT_Target);
            targetId = data.getStringExtra(IntentParams.SELECT_TargetId);
            dataContent.setText(name);
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {

    }
}



