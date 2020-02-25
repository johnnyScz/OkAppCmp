package com.xinyu.newdiggtest.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.MobileBean;
import com.xinyu.newdiggtest.bean.SectionRetBean;
import com.xinyu.newdiggtest.bean.WxUserBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SelfInfoFragment extends Fragment {

    View rootVeiw;

    @BindView(R.id.phone_tx)
    TextView textPhone;

    @BindView(R.id.email)
    TextView email;

    @BindView(R.id.adress)
    TextView adress;


    @BindView(R.id.apartment)
    TextView apartment;


    String userId;


    public void setUserId(String uid) {
        this.userId = uid;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootVeiw = inflater.inflate(R.layout.fragment_info, null);
        ButterKnife.bind(this, rootVeiw);
        initView();
        return rootVeiw;
    }


    /**
     * 查询我所在部门
     */

    public void requestSection() {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", PreferenceUtil.getInstance(App.mContext).getSessonId());
        map.put("userId", userId);
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


    private void initView() {
        requestSection();
    }

    public void setPersonData(WxUserBean personData) {

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


    @OnClick(R.id.ll_call)
    public void call() {

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + textPhone.getText().toString());
        callIntent.setData(data);
        startActivity(callIntent);
    }


    @Override
    public void onResume() {
        super.onResume();
        getInfo();
    }

    /**
     * 获取用户的mobile
     */
    public void getInfo() {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", PreferenceUtil.getInstance(App.mContext).getSessonId());
        map.put("user_id", userId);
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


}
