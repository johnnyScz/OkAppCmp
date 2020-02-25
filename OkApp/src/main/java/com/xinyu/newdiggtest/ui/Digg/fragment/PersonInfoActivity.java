package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.UserInfoBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
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
 * 详细资料
 */
public class PersonInfoActivity extends BaseNoEventActivity {

    @BindView(R.id.head)
    public ImageView header;


    @BindView(R.id.name)
    public TextView name;

    @BindView(R.id.name1)
    public TextView name1;

    @BindView(R.id.phone)
    public TextView phone;

    @BindView(R.id.email)
    public TextView email;

    @BindView(R.id.sex)
    public TextView sex;


    @BindView(R.id.birthday)
    public TextView birthday;


    @BindView(R.id.zone)
    public TextView zone;


    @BindView(R.id.nickname)
    public TextView nickName;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_person_info;
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

        //TODO 生日跟邮箱


        name.setText(user.getNickname());
        name1.setText(user.getNickname());
        nickName.setText(user.getNickname());

        Picasso.with(mContext).load(user.getHead()).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(header);

        if (!MyTextUtil.isEmpty(user.getSex()) && user.getSex().equals("M")) {
            sex.setText("男");
        } else {
            sex.setText("女");
        }
        if (!MyTextUtil.isEmpty(user.getProvince())) {
            zone.setText(user.getProvince());
        }


    }


    @OnClick(R.id.edit_name)
    public void editName() {
        ToastUtils.getInstanc().showToast("编辑名字");
    }


}




