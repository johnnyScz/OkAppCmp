package com.xinyu.newdiggtest.ui.Digg.fragment;


import android.text.TextUtils;
import android.util.Log;

import android.widget.EditText;


import com.xinyu.newdiggtest.R;

import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.CardInsertBean;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
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
 * 插入名片
 */
public class InsertCardActivity extends BaseNoEventActivity {


    @BindView(R.id.input_name)
    EditText input_name;

    @BindView(R.id.input_pos)
    EditText input_pos;

    @BindView(R.id.input_phone)
    EditText input_phone;

    @BindView(R.id.input_mail)
    EditText input_mail;

    @BindView(R.id.input_company)
    EditText input_company;

    @BindView(R.id.input_host)
    EditText input_host;

    @BindView(R.id.input_adress)
    EditText input_adress;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_card1;
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @OnClick(R.id.conform)
    public void conform() {
        check2CommitParams();


    }


    private void check2CommitParams() {

        if (MyTextUtil.isEmpty(input_name.getText().toString())) {

            ToastUtils.getInstanc().showToast("请输入姓名");
            return;
        }

        if (MyTextUtil.isEmpty(input_pos.getText().toString())) {

            ToastUtils.getInstanc().showToast("请输入职位");
            return;
        }

        if (TextUtils.isEmpty(input_phone.getText()) && TextUtils.isEmpty(input_mail.getText())) {
            ToastUtils.getInstanc().showToast("请输入联系方式!");
            return;
        }

        if (MyTextUtil.isEmpty(input_company.getText().toString())) {

            ToastUtils.getInstanc().showToast("请输入公司名称");
            return;
        }

        doCommitParams();

    }


    /**
     * 提交参数
     * <p>
     * TODO
     */
    private void doCommitParams() {

        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi netApi = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());
        requsMap.put("f_name", input_name.getText().toString());
        requsMap.put("f_organization_name", input_company.getText().toString());
        requsMap.put("f_mobile", input_phone.getText().toString());
        requsMap.put("f_position", input_pos.getText().toString());

        requsMap.put("f_email", input_mail.getText().toString());
        requsMap.put("f_addredss", input_adress.getText().toString());
        requsMap.put("f_homepage", input_host.getText().toString());

        requsMap.put("f_plugin_type_id", "103");


        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        netApi.insertCard(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CardInsertBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());

                    }

                    @Override
                    public void onNext(CardInsertBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {
                            ToastUtils.getInstanc().showToast("名片插入成功");
                            finish();
                        } else {
                            ToastUtils.getInstanc().showToast("名片插入失败!");
                        }


                    }
                });
    }


}




