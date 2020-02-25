package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import android.view.View;
import android.widget.ImageView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.GrDakaAdapter;
import com.xinyu.newdiggtest.bean.CommonUserBean;
import com.xinyu.newdiggtest.bean.GroupDashangBean;
import com.xinyu.newdiggtest.bean.GroupItemDakaBean;

import com.xinyu.newdiggtest.bean.TargetBean;

import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;


import com.xinyu.newdiggtest.ui.TargetNewInfoActivity;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GroupTargetFragment extends BaseFragment implements View.OnClickListener {

    EasyRecyclerView recyView;

    GrDakaAdapter adapter;

    ImageView emptyView;
    SmartRefreshLayout refreshLayout;

    List<TargetBean> datalist = new ArrayList<>();

    String tagNo = "-1";


    @Override
    protected void initView() {
        tagNo = getArguments().getString("tag");

        recyView = rootView.findViewById(R.id.recyclerView);
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        emptyView = rootView.findViewById(R.id.emputview);
        initRecycle();


    }

    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
        datalist.clear();
    }

    public static GroupTargetFragment newInstance(String info) {
        Bundle args = new Bundle();
        GroupTargetFragment fragment = new GroupTargetFragment();
        args.putString("tag", info);
        fragment.setArguments(args);
        return fragment;
    }


    private void requestdatas() {
        dialog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("group_id", mContext.getIntent().getStringExtra("roomId"));
        requsMap.put("state", tagNo);
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        url.getGroupTarget(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GroupDashangBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(GroupDashangBean msg) {
                        dialog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData() == null || msg.getData().size() == 0) {
                                showEmpty(true);
                                return;
                            } else {
                                showEmpty(false);
                                setDatas(msg.getData());
                            }

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常！");
                        }

                    }
                });


    }

    /**
     * 显示空数据
     *
     * @param show
     */
    private void showEmpty(boolean show) {
        if (show) {
            refreshLayout.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            refreshLayout.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

    }

    /**
     * 显示数据
     *
     * @param data
     */
    private void setDatas(final List<GroupItemDakaBean> data) {
        datalist.clear();
        int len = data.size();
        for (int i = 0; i < len; i++) {
            GroupItemDakaBean gb = data.get(i);
            inputBean(gb);
        }
        adapter = new GrDakaAdapter(datalist, mContext);
        adapter.setTag(tagNo);
        recyView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                int id = view.getId();
                if (id == R.id.rl_item1) {
                    TargetBean data = datalist.get(position);

                    Intent mitent = mContext.getIntent();
                    mitent.setClass(mContext, TargetNewInfoActivity.class);

                    mitent.putExtra(IntentParams.Target_FinishTag, tagNo);
                    mitent.putExtra(IntentParams.Target_Name, data.getName());
                    mitent.putExtra(IntentParams.DAKA_Target_Id, data.getTarget_uuid());
                    startActivity(mitent);


                } else if (id == R.id.iv_head) {
//                    TargetBean datt = datalist.get(position);
////                    Intent mIntent = new Intent(mContext, MySpaceActivity.class);
////                    if (tagNo.equals("3")) {
////                        mIntent.putExtra(IntentParams.USER_ID, datt.getCreateuser());
////                    } else {
////                        mIntent.putExtra(IntentParams.USER_ID, datt.getUser().getUser_id());
////                    }
////                    mIntent.putExtra(IntentParams.USER_head, datt.getUser().getHead());
////                    mIntent.putExtra(IntentParams.UserName, datt.getUser().getNickname());
////                    mIntent.putExtra(IntentParams.STATE, "0");
////                    startActivity(mIntent);
                }


            }
        });
    }

    private void inputBean(GroupItemDakaBean gb) {

        if (tagNo.equals("3")) {
            //用户关注的目标
            TargetBean headBean = new TargetBean();
            headBean.setUser(new CommonUserBean(gb.getHead(), gb.getNick_name()));
            headBean.setCreateuser(getChildUserId(gb));

            headBean.setShowType(2);
            datalist.add(headBean);
            List<TargetBean> itemBean = gb.getFollow_list();
            for (TargetBean it : itemBean) {
                it.setShowType(1);
                it.setToUser(it.getCreateuser());
                datalist.add(it);
            }

        } else {
            //--------群目标---------
            TargetBean headBean = new TargetBean();
            headBean.setUser(gb.getUser());
            headBean.setShowType(2);
            datalist.add(headBean);

            List<TargetBean> itemBean = gb.getTargetplan();
            for (TargetBean it : itemBean) {
                it.setShowType(1);
                it.setToUser(gb.getUser().getUser_id());
                datalist.add(it);
            }
        }


    }

    private String getChildUserId(GroupItemDakaBean gb) {
        List<TargetBean> itemBean = gb.getFollow_list();
        String uid = itemBean.get(0).getCreateuser();
        return uid;
    }


    @Override
    protected void loadData() {

    }


    @Override
    public void onResume() {
        super.onResume();
        if (tagNo.equals("3")) {
            requestSelfFocusTargetData();

        } else {
            requestdatas();
        }

    }

    private void requestSelfFocusTargetData() {
        dialog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("state", "0");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("pageNum", "1");
        requsMap.put("pageSize", "15");
        url.getSelfFocusTargetList(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GroupDashangBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(GroupDashangBean msg) {
                        dialog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData() == null || msg.getData().size() == 0) {
                                showEmpty(true);
                                return;
                            } else {
                                showEmpty(false);
                                setDatas(msg.getData());
                            }

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常！");
                        }

                    }
                });

    }

    @Override
    protected void reLoadData() {

    }

    @Override
    protected int getResId() {
        return R.layout.fragment_test;
    }


    @Override
    public void onClick(View v) {

    }
}
