package com.xinyu.newdiggtest.bigq;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.WxUserBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 绑定手机号
 */
public class BindMobileActivity extends BaseNoEventActivity {


    @BindView(R.id.edt_content)
    EditText moible;

    WxUserBean mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

        mData = (WxUserBean) getIntent().getSerializableExtra("data");


    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.bind_mobile;
    }


    @OnClick(R.id.btn_commint)
    public void goCreate() {

        if (MyTextUtil.isEmpty(moible.getText().toString())) {
            ToastUtils.getInstanc().showToast("请输入手机号码!");
            return;

        }

        if (!AppUtils.isCellphone(moible.getText().toString())) {
            ToastUtils.getInstanc().showToast("手机号码格式错误,请重新输入!");
            return;
        }

        commitParams();

    }


    public void commitParams() {

        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = createMap();

        url.bindMobile(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        loadingDailog.dismiss();

                        if (msg.getOp().getCode().equals("Y")) {
                            PreferenceUtil.getInstance(mContext).setMobilePhone(moible.getText().toString());
                            goCompanyList();
                        }
                    }
                });
    }


    /**
     * TODO
     * 获取公司的列表
     */
    private void goCompanyList() {

    }

    private HashMap<String, String> createMap() {

        HashMap<String, String> map = new HashMap<>();

        map.put("data_from", "OK");
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());

        map.put("user", creatJson());

        return map;
    }

    private String creatJson() {

        JSONObject object = new JSONObject();

        try {
            object.put("custom_head", MyTextUtil.isEmpty(mData.getCustom_head()) ? mData.getHead() : mData.getCustom_head());
            object.put("name", MyTextUtil.getUrl1Encoe(mData.getNickname()));
            object.put("mobile", moible.getText().toString());
            object.put("province", mData.getProvince());
            object.put("city", mData.getCity());
            object.put("sex", mData.getSex());
            object.put("email", mData.getEmail());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return object.toString();
    }


    @OnClick(R.id.iv_back)
    public void goback() {
        finish();
    }


}
