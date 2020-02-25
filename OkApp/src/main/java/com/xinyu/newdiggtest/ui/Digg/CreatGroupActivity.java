package com.xinyu.newdiggtest.ui.Digg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.tu.loadingdialog.LoadingDailog;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.CreateGroupBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.utils.DialogUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreatGroupActivity extends Activity {

    @BindView(R.id.tv_title)
    public TextView title;

    @BindView(R.id.et_groupname)
    public EditText edGroupNam;

    Context ctx;
    LoadingDailog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ButterKnife.bind(this);
        ctx = this;
        initView();
    }

    private void initView() {
        title.setText("创建群组");

        dialog = new DialogUtil(this).buildDialog("加载中...");
    }

    @OnClick(R.id.iv_back)
    public void pageBack() {
        finish();
    }

    @OnClick(R.id.btn_gname)
    public void commitGName() {
        if (TextUtils.isEmpty(edGroupNam.getText().toString().trim())) {
            ToastUtils.getInstanc(this).showToast("群组名称不能为空！");
        } else {
            commitGroupName();
        }
    }

    private void commitGroupName() {

        dialog.show();

        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "user.p_CreateRoom");
        map.put("sid", PreferenceUtil.getInstance(ctx).getSessonId());

        map.put("room_name", MyTextUtil.getUrl3Encoe(edGroupNam.getText().toString().trim()));

        url.creatGroup(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateGroupBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        Log.e("amtf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(CreateGroupBean msg) {
                        dialog.dismiss();
                        if (msg.getOp() == null) {
                            ToastUtils.getInstanc(ctx).showToast("服务异常");
                            return;
                        }
                        if (msg.getOp().getCode().equals("Y")) {
                            //TODO 拿到roomid
                            String roomid = msg.getRoom_id();
                            Intent intent = new Intent(ctx, GroupSuccedActivity.class);
                            intent.putExtra("roomId", roomid);
                            intent.putExtra("roomName", edGroupNam.getText().toString().trim());
                            startActivity(intent);
                            finish();
                        } else {
                            ToastUtils.getInstanc(ctx).showToast("服务异常");
                        }

                    }
                });

    }


}




