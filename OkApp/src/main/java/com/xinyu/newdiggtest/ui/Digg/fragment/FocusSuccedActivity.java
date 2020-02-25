package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.widget.Button;


import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.InfoStr;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.TargetNewInfoActivity;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;


import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FocusSuccedActivity extends BaseNoEventActivity {

    @BindView(R.id.btn_return)
    Button btnCommit;

    String targetId = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnCommit.setEnabled(false);
        initView();
    }

    private void initView() {
        targetId = getIntent().getStringExtra("targetId");
        String formUserId = getIntent().getStringExtra("formUserId");


        requestFocusTarget(targetId, formUserId);

    }


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_focus_succed;
    }


    @OnClick(R.id.btn_return)
    public void goCommit() {
        Intent mitent = new Intent(mContext, TargetNewInfoActivity.class);
        mitent.putExtra(IntentParams.Target_Pid, "");
        mitent.putExtra(IntentParams.Target_Root_id, targetId);
        mitent.putExtra(IntentParams.DAKA_Target_Id, targetId);
        startActivity(mitent);
        finish();
    }


    private void requestFocusTarget(String targetId, String formUserId) {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.AddMyfollowTargetPlan");
        map.put("target_uuid", targetId);

        map.put("follow_user_id", formUserId);
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());


        url.targetFocus(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InfoStr>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(InfoStr msg) {
                        loadingDailog.dismiss();
                        String code = msg.getOp().getCode();
                        if (code.equals("Y")) {
                            btnCommit.setEnabled(true);
                            ToastUtils.getInstanc(mContext).showToast("关注成功");
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });

    }


}




