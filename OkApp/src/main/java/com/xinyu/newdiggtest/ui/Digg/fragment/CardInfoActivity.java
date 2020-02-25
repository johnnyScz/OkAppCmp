package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.CardChildBean;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * card详情页面
 */
public class CardInfoActivity extends BaseNoEventActivity {

    @BindView(R.id.title)
    public TextView title;


    @BindView(R.id.name_tx)
    public TextView name_tx;


    @BindView(R.id.mobile)
    public TextView mobile;


    @BindView(R.id.email)
    public TextView email;


    @BindView(R.id.company_name)
    public TextView company_name;

    @BindView(R.id.home_host)
    public TextView home_host;

    String phoneNum = "";


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_card_info;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(getIntent().getStringExtra("title"));
        name_tx.setText(getIntent().getStringExtra("title"));


        queryCmpCard();
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    public void queryCmpCard() {

        loadingDailog.show();

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();

        requsMap.put("sid", PreferenceUtil.getInstance(App.mContext).getSessonId());
        requsMap.put("f_plugin_ids", getIntent().getStringExtra("plugin_id"));

        url.queryCardMsg(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object msg) {

                        String json = new Gson().toJson(msg);

                        Map<String, Object> data = json2map(json);

                        Map<String, Object> child = (Map<String, Object>) data.get("data");

                        JSONObject firstObj = null;

                        if (child != null) {

                            for (Map.Entry<String, Object> entry : child.entrySet()) {

                                Object myData = entry.getValue();
                                String ttChild = new Gson().toJson(myData);
                                try {
                                    firstObj = new JSONObject(ttChild);
                                    if (firstObj != null)
                                        break;

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        CardChildBean bean = JSON.parseObject(firstObj.toString(), CardChildBean.class);

                        freshUi(bean);


                    }
                });
    }

    /**
     * @param bean
     */
    private void freshUi(CardChildBean bean) {

        phoneNum = bean.getF_mobile();

        mobile.setText("手机 : " + bean.getF_mobile());

        email.setText("工作邮箱 : " + bean.getF_email());

        company_name.setText("公司名称 : " + bean.getF_organization_name());

        home_host.setText("首页 : " + bean.getF_homepage());

    }


    public static Map<String, Object> json2map(String str_json) {
        Map<String, Object> res = null;
        try {
            Gson gson = new Gson();
            res = gson.fromJson(str_json, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (JsonSyntaxException e) {

            Log.e("amtf", "格式异常：" + e.getMessage());
        }
        return res;
    }


    @OnClick(R.id.cread_todo)
    public void goTodo() {

        //TODO 待办功能

//        bb.setHead(getIntent().getStringExtra("head"));
//        bb.setNickname(getIntent().getStringExtra("name"));
//        bb.setUser_id(getIntent().getStringExtra("signer"));

    }

    @OnClick(R.id.send_img)
    public void sendImg() {
        ToastUtils.getInstanc().showToast("该功能暂未开通");

    }

    @OnClick(R.id.call)
    public void call() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
        startActivity(intent);
    }

}




