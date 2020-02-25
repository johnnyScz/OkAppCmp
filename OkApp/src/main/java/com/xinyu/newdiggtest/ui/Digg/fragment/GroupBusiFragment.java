package com.xinyu.newdiggtest.ui.Digg.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.GroupBusinessAdapter;
import com.xinyu.newdiggtest.bean.AfairBean;
import com.xinyu.newdiggtest.bean.PushBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 *
 */
public class GroupBusiFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyView;

    @BindView(R.id.emputview)
    ImageView emputview;

    GroupBusinessAdapter adapter;
    List<AfairBean.AfairChildBean> listData = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(App.mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器

    }

    @Override
    protected void loadData() {
        if (listData.size() < 1) {
            queryTotalAffair("", 0);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        queryTotalAffair("", 1);
    }

    @Override
    protected void reLoadData() {

    }

    @Override
    protected int getResId() {
        return R.layout.fragment_person_chat;
    }


    private void showEmptyView(boolean show) {
        if (show) {
            recyView.setVisibility(View.GONE);
            emputview.setVisibility(View.VISIBLE);

        } else {
            recyView.setVisibility(View.VISIBLE);
            emputview.setVisibility(View.GONE);
        }

    }

    public void queryTotalAffair(String f_id, final int tag) {

        if (tag < 1)
            dialog.show();

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();

        requsMap.put("user_id", PreferenceUtil.getInstance(App.mContext).getUserId());
        requsMap.put("sid", PreferenceUtil.getInstance(App.mContext).getSessonId());
        requsMap.put("f_id", f_id);

        url.checkAffair(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AfairBean>() {
                    @Override
                    public void onCompleted() {
                        if (dialog.isShowing())
                            dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {


                    }

                    @Override
                    public void onNext(AfairBean msg) {

                        if (tag < 1)
                            dialog.dismiss();

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() != null && msg.getData().size() > 0) {

                                listData = msg.getData();

                                if (listData != null && listData.size() > 0) {
                                    showEmptyView(false);
                                    fillDatas();
                                } else {
                                    showEmptyView(true);
                                }

                            }
                        }
                    }
                });
    }

    private void fillDatas() {

        adapter = new GroupBusinessAdapter(R.layout.item_group_business, listData);
        recyView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                AfairBean.AfairChildBean item = (AfairBean.AfairChildBean) adapter.getData().get(position);
                Intent intent = new Intent(mContext, OkRInfoActivity.class);

                intent.putExtra("checkId", item.getCheck_id());
                intent.putExtra("f_id", item.getF_id());
                intent.putExtra("f_desc", item.getF_desc());

                startActivity(intent);

            }
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == EventConts.SoCket_Push) {


            JSONObject json = event.object;

            try {

                String op = json.getString("op");
                String type = json.getString("type");
                JSONObject data = json.getJSONObject("data");

                if (type.equals("group_operation_record")) {

                    refreshDatas(data);

                } else if (type.equals("check") && op.equals("update")) {

                    String fid = data.getString("f_id");
                    String name = data.getString("f_title");
                    refreshName(fid, name);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * @param fid
     * @param name
     */
    private void refreshName(String fid, String name) {

        List<AfairBean.AfairChildBean> list = adapter.getData();

        int len = list.size();
        for (int i = 0; i < len; i++) {
            AfairBean.AfairChildBean dt = list.get(i);
            if (dt.getF_id().equals(fid)) {

                AfairBean.AfairChildBean.ChatInfoBean info = dt.getChat_info();

                info.setRoom_name(name);

                adapter.notifyItemChanged(i);

                break;
            }

        }
    }

    /**
     * 刷新数据
     *
     * @param data
     */
    private void refreshDatas(JSONObject data) {

        List<AfairBean.AfairChildBean> list = adapter.getData();


        String fid = "", content = "", cretime = "";

        try {

            fid = data.getString("group_id");
            cretime = data.getString("create_time");
            content = data.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int len = list.size();
        for (int i = 0; i < len; i++) {
            AfairBean.AfairChildBean dt = list.get(i);
            if (dt.getF_id().equals(fid)) {

                PushBean bean = new PushBean();
                bean.setMsg(content);
                bean.setMsg_time(cretime);
                dt.setGroup_operation_record(bean);

                adapter.notifyItemChanged(i);

                break;
            }


        }
    }


}
