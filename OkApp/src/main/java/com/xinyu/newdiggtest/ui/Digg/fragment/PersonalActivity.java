package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.UserInfoBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.Digg.PreLoginActivity;
import com.xinyu.newdiggtest.utils.CircleTransform;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 个人资料
 */
public class PersonalActivity extends BaseNoEventActivity {

    @BindView(R.id.head)
    public ImageView header;


    @BindView(R.id.phone)
    public TextView phone;


    @BindView(R.id.nickname)
    public TextView nickName;

    String name = "", headUrl = "";

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_person_info1;
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        requestUserInfo();
    }

    public void requestUserInfo() {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "user.getUserInfo");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("flag", "Y");
        url.getUserInfo(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserInfoBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(UserInfoBean msg) {
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getUser() != null) {
                                upHeadView(msg.getUser());
                            }
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });
    }


    /**
     * 展示数据
     *
     * @param user
     */
    private void upHeadView(UserInfoBean.UserBean user) {

        name = user.getNickname();
        headUrl = user.getHead();

        nickName.setText(name);
        Picasso.with(mContext).load(headUrl).error(R.drawable.icon_no_download).
                transform(new CircleTransform()).into(header);

        if (MyTextUtil.isEmpty(user.getMobile())) {
            phone.setText("未设置");
        } else {
            phone.setText(user.getMobile());
        }


    }


    @OnClick(R.id.eidt_info)
    public void editName() {
        startActivity(new Intent(mContext, PersonEditActivity.class));
    }


    @OnClick(R.id.qcode_ll)
    public void qcode() {

        Intent intent = getIntent();
        intent.putExtra(IntentParams.Intent_Enter_Type, "personnal");
        intent.putExtra("roomName", name);
        intent.putExtra("headUrl", headUrl);

//        intent.setClass(mContext, CommonQcodeActivity.class);
        startActivity(intent);

    }


    private long firstPressedTime;


    @OnClick(R.id.exit)
    public void exit() {
        if (System.currentTimeMillis() - firstPressedTime < 1000) {
            PreferenceUtil.getInstance(mContext).setUserId("");
            mContext.startActivity(new Intent(mContext, PreLoginActivity.class));

        } else {
            ToastUtils.getInstanc(mContext).showToast("再点一次退出登录");
            firstPressedTime = System.currentTimeMillis();
        }
    }


}




