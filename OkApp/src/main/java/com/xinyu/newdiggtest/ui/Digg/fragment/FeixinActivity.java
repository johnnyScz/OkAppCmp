package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.xinyu.newdiggtest.R;


import com.xinyu.newdiggtest.adapter.MsgFocusAdapter;
import com.xinyu.newdiggtest.bean.CommonGroupBean;
import com.xinyu.newdiggtest.bean.XhintMsgBean;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;

import com.xinyu.newdiggtest.ui.BaseActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.ui.chat.ChatCompanyActivity;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 飞信聊天列表
 */
public class FeixinActivity extends BaseActivity {

    MsgFocusAdapter adapter;

    @BindView(R.id.recyclerView)
    public RecyclerView recyView;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_feixin;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecycle();

        initView();


    }

    private void initView() {
        getXhintList();
    }

    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onXshellEvent(XshellEvent event) {
        //TODO 需要定义
        if (event.what == 0) {

        }

    }


    private void getXhintList() {
        loadingDailog.show();

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());

        url.getXhintList(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<XhintMsgBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(XhintMsgBean msg) {

                        String json = new Gson().toJson(msg);

                        Log.e("amtf", "json:" + json);

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() != null && msg.getData().size() > 0) {

                                showUi(msg.getData());

                            }
                        }

                    }
                });
    }


    /**
     * 显示
     *
     * @param data
     */
    private void showUi(List<XhintMsgBean.MsgDataBean> data) {

        adapter = new MsgFocusAdapter(R.layout.item_group_business, filtdatas(data));

        recyView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                XhintMsgBean.MsgDataBean data = (XhintMsgBean.MsgDataBean) adapter.getData().get(position);

                readMsg(data);

                CommonGroupBean group = data.getLatestmsg().getGroup();

                if (group == null || MyTextUtil.isEmpty(group.getRoom_id())) {
                    return;
                }

                Intent mIntent = new Intent(mContext, ChatCompanyActivity.class);
                mIntent.putExtra("room_id", group.getRoom_id());
                mIntent.putExtra("room_name", group.getRoom_name());
                mIntent.putExtra("room_type", group.getRoom_type());
                startActivity(mIntent);
            }
        });


    }

    private List<XhintMsgBean.MsgDataBean> filtdatas(List<XhintMsgBean.MsgDataBean> data) {

        List<XhintMsgBean.MsgDataBean> filtData = new ArrayList<>();

        for (XhintMsgBean.MsgDataBean tt : data) {

            CommonGroupBean gr = tt.getLatestmsg().getGroup();

            if (gr != null && !MyTextUtil.isEmpty(gr.getRoom_type()) && !gr.getRoom_type().equals("C")) {
                filtData.add(tt);
            }
        }
        return filtData;
    }


    /**
     * 阅读消息
     *
     * @param data
     */
    public void readMsg(XhintMsgBean.MsgDataBean data) {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("project", "OK2E");
        requsMap.put("deleteAll", "true");
        requsMap.put("target", data.getLatestmsg().getUser_id());
        requsMap.put("topic", data.getTopic());
        requsMap.put("chatId", data.getLatestmsg().getRoom_id());

        url.redChatMsg(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object msg) {


                    }
                });
    }


}




