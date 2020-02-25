package com.xinyu.newdiggtest.ui;


import android.os.Bundle;

import android.support.annotation.Nullable;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;

import com.xinyu.newdiggtest.bean.MobileBean;
import com.xinyu.newdiggtest.bean.SectionRetBean;
import com.xinyu.newdiggtest.bean.WxUserBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;


import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的
 */
public class SelfActivity extends BaseNoEventActivity {

    @BindView(R.id.tv_adress_me)
    public TextView tv_adress_me;

    @BindView(R.id.icon_me)
    public ImageView headIcon;

    @BindView(R.id.tv_name_me)
    public TextView name_me;

    @BindView(R.id.phone_tx)
    TextView textPhone;

    @BindView(R.id.email)
    TextView email;

    @BindView(R.id.adress)
    TextView adress;


    @BindView(R.id.apartment)
    TextView apartment;


    private void initView() {

        if (!MyTextUtil.isEmpty(PreferenceUtil.getInstance(mContext).getHeadUrl())) {
            Picasso.with(mContext).load(PreferenceUtil.getInstance(mContext).getHeadUrl()).error(R.drawable.icon_no_download).
                    transform(new CircleCornerForm()).into(headIcon);
        }

        name_me.setText(PreferenceUtil.getInstance(mContext).getNickName());

        tv_adress_me.setText(PreferenceUtil.getInstance(mContext).getAdress());

    }


    private void getInfo() {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", PreferenceUtil.getInstance(App.mContext).getSessonId());
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("flag", "Y");

        url.getWxMobile(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MobileBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(MobileBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getUser() != null) {
                                setPersonData(msg.getUser());
                            }

                        } else {
                            ToastUtils.getInstanc().showToast("服务异常");
                        }

                    }
                });
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        getInfo();
        requestSection();

    }

    private void setPersonData(WxUserBean personData) {

        String mobie = personData.getMobile();

        if (!MyTextUtil.isEmpty(mobie)) {
            textPhone.setText(mobie);
        }

        if (!MyTextUtil.isEmpty(personData.getEmail())) {
            email.setText(personData.getEmail());
        }

        if (!MyTextUtil.isEmpty(personData.getProvince())) {

            adress.setText(personData.getProvince() + " " + personData.getCity());
        }

    }

    public void requestSection() {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", PreferenceUtil.getInstance(App.mContext).getSessonId());
        map.put("userId", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("f_company_id", PreferenceUtil.getInstance(App.mContext).getCompanyId());

        url.getSection(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SectionRetBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(SectionRetBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            List<SectionRetBean.OrgSection> data = msg.getData();
                            if (data != null && data.size() > 0) {

                                showUi(data);
                            } else {
                                apartment.setText("--");
                            }
                        }
                    }
                });
    }


    private void showUi(List<SectionRetBean.OrgSection> data) {

        StringBuffer buffer = new StringBuffer();

        for (SectionRetBean.OrgSection dd : data) {
            buffer.append(dd.getF_name()).append("，");
        }
        String str = buffer.toString();
        apartment.setText(str.substring(0, str.length() - 1));
    }


    @Override
    protected int getLayoutResouce() {
        return R.layout.myslef_layout;
    }


    @OnClick(R.id.back)
    public void back() {
        finish();
    }


}
