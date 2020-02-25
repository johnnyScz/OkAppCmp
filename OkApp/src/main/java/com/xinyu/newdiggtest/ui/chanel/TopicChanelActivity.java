package com.xinyu.newdiggtest.ui.chanel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.TopicAdapter;
import com.xinyu.newdiggtest.bean.ChannelRetunBean;
import com.xinyu.newdiggtest.bean.ChanneltopicBean;
import com.xinyu.newdiggtest.bean.MobileBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.ChannelCommentActivity;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TopicChanelActivity extends BaseNoEventActivity {


    @BindView(R.id.title)
    TextView textTitle;

    @BindView(R.id.desc)
    TextView desc;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.gruop)
    TextView gruop;


    @BindView(R.id.time_create)
    TextView time_create;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_topic;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getPerosnInfo();

        initView();

    }


    @Override
    protected void onResume() {
        super.onResume();
        queryInfo();
    }

    private void initView() {


        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器


        textTitle.setText(getIntent().getStringExtra("channelName"));
        desc.setText(getIntent().getStringExtra("description"));
        time_create.setText(getIntent().getStringExtra("createdtime").substring(0, 10));
        gruop.setText("来自" + "\"" + getIntent().getStringExtra("groupname") + "\"" + "的群");

    }


    public void queryInfo() {

        loadingDailog.show();

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("channel_id", getIntent().getStringExtra("chanel_id"));
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());

        url.topicChanel(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChannelRetunBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());

                    }

                    @Override
                    public void onNext(ChannelRetunBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            showInfo(msg.getData());

                        }
                    }
                });
    }


    /**
     * 获取用户的信息
     */
    public void getPerosnInfo() {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("user_id", getIntent().getStringExtra("createduserid"));
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
                                name.setText(msg.getUser().getNickname());
                            }

                        } else {
                            ToastUtils.getInstanc().showToast("服务异常");
                        }

                    }
                });
    }


    private void showInfo(ChannelRetunBean.Channel data) {

        List<ChanneltopicBean> dateList = data.getGetchanneltopicdtolist();

        if (dateList != null && dateList.size() > 0) {

            TopicAdapter adapter = new TopicAdapter(R.layout.item_topic, dateList);

            recyclerView.setAdapter(adapter);

            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                    ChanneltopicBean data = (ChanneltopicBean) adapter.getData().get(position);

                    Intent intent = getIntent();
                    intent.setClass(mContext, ChannelCommentActivity.class);
                    intent.putExtra("url", data.getDescription());


                    intent.putExtra("contentId", data.getFormcontentid());

                    intent.putExtra("chanel_plugin_type_id", data.getFormid());
                    intent.putExtra("comment", data.getCommentcount());
                    intent.putExtra("praise", data.getLikecount());

                    //带过去的头部
                    intent.putExtra("title", data.getTitle());
                    intent.putExtra("name", data.getCreateusername());
                    intent.putExtra("head", data.getCreateduserhead());
                    intent.putExtra("time", data.getCreatedtime());
                    intent.putExtra("userId", data.getCreateduserid());


                    startActivity(intent);


                }
            });
        }


    }


    @OnClick(R.id.icon_back)
    public void goCommit() {
        finish();
    }


}
