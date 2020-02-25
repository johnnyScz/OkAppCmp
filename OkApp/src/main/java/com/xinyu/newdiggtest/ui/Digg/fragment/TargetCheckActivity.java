package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.CheckRetBean;
import com.xinyu.newdiggtest.net.ApiManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * 检查目标完成情况
 */
public class TargetCheckActivity extends BaseNoEventActivity {

    @BindView(R.id.ll_check_parent)
    public LinearLayout checkPrent;

    @BindView(R.id.iv_head)
    public ImageView head;

    @BindView(R.id.tv_name)
    public TextView name;

    @BindView(R.id.tg_info)
    public TextView targetInfo;


    String headUrl, fromUser, targetName,aim_id, aim_money, nick_name, nameContent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent();
        initView();
    }

    private void initIntent() {
        headUrl = getIntent().getStringExtra("headUrl");
        fromUser = getIntent().getStringExtra("fromUser");
        targetName = getIntent().getStringExtra("targetName");
        aim_id = getIntent().getStringExtra("aim_id");
        aim_money = TextUtils.isEmpty(getIntent().getStringExtra("aim_money")) ?
                "0" : getIntent().getStringExtra("aim_money");
        nick_name = getIntent().getStringExtra("nick_name");
        nameContent = getIntent().getStringExtra("name_content");

        showHead();
    }







    private void showHead() {
        if (TextUtils.isEmpty(headUrl)) {
            head.setImageResource(R.drawable.icon_error);
        } else {

            Picasso.with(mContext).load(headUrl).error(R.drawable.icon_no_download).
                    transform(new CircleCornerForm()).into(head);

        }
        name.setText(nick_name);
        targetInfo.setText(getName());

    }

    private String getName() {
        if (MyTextUtil.isEmpty(nameContent))
            return "";

        if (nameContent.contains("#")) {

            int begin = nameContent.indexOf("#");
            int last = nameContent.lastIndexOf("#");
            return nameContent.substring(begin, last + 1);

        }

        return "";
    }

    String checkType = "0", finishState = "";

    private void initView() {
        int len = checkPrent.getChildCount();

        for (int i = 0; i < len; i++) {
            CheckBox box = (CheckBox) checkPrent.getChildAt(i);
            final int finalI = i;
            box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        resetOther(finalI);
                        int id = buttonView.getId();
                        switch (id) {
                            case R.id.check:
                                checkType = "1";
                                finishState = "未完成";
                                break;

                            case R.id.check60:
                                checkType = "2";
                                finishState = "基本完成";
                                break;

                            case R.id.check100:
                                checkType = "3";
                                finishState = "完成";
                                break;
                        }
                    } else {
                        checkType = "0";
                    }
                }
            });

        }

        CheckBox cx = (CheckBox) checkPrent.getChildAt(2);
        cx.setChecked(true);
        checkType = "3";

    }


    private void requestNet() {
        loadingDailog.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("aim_id", aim_id);
        map.put("money", aim_money);
        map.put("user_id", fromUser);//目标创建者
        map.put("rater_id", PreferenceUtil.getInstance(mContext).getUserId());//评分的人
        map.put("score", checkType);
        map.put("rate_type", getIntent().getStringExtra("rate_type"));//r 打赏 s 监督人
        map.put("part_count", "1");

        ApiManager.requestPost1(map, new Subscriber<CheckRetBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                loadingDailog.dismiss();
                ToastUtils.getInstanc(mContext).showToast(e.getMessage());
            }

            @Override
            public void onNext(CheckRetBean msgRetBean) {
                loadingDailog.dismiss();
                if (msgRetBean.getOp().getCode().equals("Y")) {
                    nofityForwardClose();
                } else {
                    ToastUtils.getInstanc().showToast("服务异常");
                }

            }
        });


    }

    private void nofityForwardClose() {
        Intent intent = new Intent("close_msglist");
        sendBroadcast(intent);
        finish();
    }


    private void resetOther(int finalI) {
        int len = checkPrent.getChildCount();
        for (int i = 0; i < len; i++) {
            if (i != finalI) {
                CheckBox box = (CheckBox) checkPrent.getChildAt(i);
                box.setChecked(false);
            }
        }
    }


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_progress_check;
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @OnClick(R.id.btn_commit)
    public void goCommit() {
        if (checkType.equals("0")) {
            ToastUtils.getInstanc(mContext).showToast("请评定完成情况");
            return;
        }
//        ToastUtils.getInstanc(mContext).showToast("当前选中：" + checkType);

        AlertDialog.Builder buid = new AlertDialog.Builder(mContext);
        buid.setMessage("您的评定结果为：" + finishState + ",提交后不可更改，确定提交？");
        buid.setPositiveButton(
                "确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {

                        requestNet();
                    }
                }
        );
        buid.setNegativeButton(
                "取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {

                    }
                }
        );

        AlertDialog dialog = buid.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                .getColor(R.color.bar_grey_90));


    }

    @OnClick(R.id.more_info)
    public void goRules() {
        startActivity(new Intent(mContext, RulesInfoActivity.class));
    }


}




