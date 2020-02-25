package com.xinyu.newdiggtest.ui.Digg.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import android.util.Log;

import android.view.View;

import android.widget.Button;

import android.widget.TextView;


import com.xinyu.newdiggtest.R;

import com.xinyu.newdiggtest.adapter.OkRInfoAdapter;
import com.xinyu.newdiggtest.bean.AfairBean;

import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.BaseBean;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.BaseActivity;

import com.xinyu.newdiggtest.ui.XshellEvent;

import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.IntentParams;

import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OKR 拆分二级页面
 */
public class OkSecondActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.btn_delet)
    public Button btn_delet;


    @BindView(R.id.title)
    public TextView title;


    OkRInfoAdapter adapter;


    AfairBean.AfairChildBean data;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_second;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fid = getIntent().getStringExtra("f_id");
        queryAffairInfo(getIntent().getStringExtra("f_id"));

    }

    String fid = "";


    public void queryAffairInfo(String f_id) {

        loadingDailog.show();

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("user_id", PreferenceUtil.getInstance(App.mContext).getUserId());
        requsMap.put("sid", PreferenceUtil.getInstance(App.mContext).getSessonId());
        requsMap.put("f_id", f_id);


        url.checkAffair(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AfairBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());

                    }

                    @Override
                    public void onNext(AfairBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() != null && msg.getData().size() > 0) {

                                List<AfairBean.AfairChildBean> total = msg.getData();

                                if (total != null && total.size() > 0) {

                                    showInfo(total);

                                }
                            }
                        }
                    }
                });
    }


    private void showInfo(List<AfairBean.AfairChildBean> total) {
        data = total.get(0);

        title.setText(data.getF_title());

        String creater = data.getF_create_by();

        if (creater.equals(PreferenceUtil.getInstance(mContext).getUserId())) {
            btn_delet.setText("删除");
        } else {
            btn_delet.setText("退出");
        }


    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @OnClick(R.id.btn_delet)
    public void delet() {

        String tip = btn_delet.getText().toString().equals("删除") ? "删除" : "退出";

        AlertDialog dialog = new AlertDialog.Builder(mContext)

                .setMessage("确定" + tip + "该群吗")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        if (btn_delet.getText().toString().equals("删除")) {
                            deletAffair();

                        } else {
                            logOutAffair();
                        }
                    }
                })
                .show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.button_vip));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);


    }


    @Override
    public void onClick(View v) {
        ToastUtils.getInstanc().showToast("请下载PC版体验");
    }


    public void deletAffair() {

        loadingDailog.show();

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("sid", PreferenceUtil.getInstance(App.mContext).getSessonId());
        requsMap.put("f_id", fid);


        url.deletAffair(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());

                    }

                    @Override
                    public void onNext(BaseBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            ToastUtils.getInstanc().showToast("删除成功");

                            //TODO 通知界面更新
                            EventBus.getDefault().post(new XshellEvent(0x66));
                            finish();

                        }

                    }
                });
    }


    public void logOutAffair() {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("sid", PreferenceUtil.getInstance(App.mContext).getSessonId());
        requsMap.put("f_id", fid);
        requsMap.put("f_chat_id", data.getF_chat_id());
        requsMap.put("f_group_id", fid);


        url.logOutAffair(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {
                            //TODO 通知界面更新
                            ToastUtils.getInstanc().showToast("退出成功!");
                            EventBus.getDefault().post(new XshellEvent(0x66));
                            finish();
                        }
                    }
                });
    }


    @OnClick(R.id.g_name)
    public void goEditGName() {

        Intent intent = getIntent();
        intent.putExtra(IntentParams.GROUP_NAME, title.getText().toString());
        intent.setClass(this, EditGroupNameActity.class);
        startActivity(intent);


    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == EventConts.MSG_Refresh_GName) {
            title.setText(event.msg);
        }

    }


    @OnClick(R.id.biao3)
    public void goRight() {
        ToastUtils.getInstanc().showToast("请下载PC版体验");
    }


}




