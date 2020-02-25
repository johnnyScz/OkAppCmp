package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.GrDakaAdapter;
import com.xinyu.newdiggtest.adapter.MyTargetAdapter;
import com.xinyu.newdiggtest.bean.CommonUserBean;
import com.xinyu.newdiggtest.bean.GroupDashangBean;
import com.xinyu.newdiggtest.bean.GroupItemDakaBean;
import com.xinyu.newdiggtest.bean.TargetBean;
import com.xinyu.newdiggtest.bean.TargetListBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.NetApi;
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

public class TargetFragment extends BaseFragment implements View.OnClickListener {

    RecyclerView recyView;
    RecyclerView focusRecycleView;
    TextView focus_count;

    MyTargetAdapter adapter;

    ImageView emptyView;

    String tagNo = "", param = "", enterType = "";

    List<TargetBean> mySelfDatas;//我自己的目标

    List<GroupItemDakaBean> focusDatas;//关注的目标

    View content_ll;


    @Override
    protected void initView() {

        tagNo = getArguments().getString("tag");
        param = getArguments().getString("param");
        if (getArguments().getString("enterType") != null) {
            enterType = getArguments().getString("enterType");
        }
        recyView = rootView.findViewById(R.id.recyclerView);
        focusRecycleView = rootView.findViewById(R.id.foucs_recycle);
        content_ll = rootView.findViewById(R.id.content_ll);
        focus_count = rootView.findViewById(R.id.focus_count);

        initAdapter();
        emptyView = rootView.findViewById(R.id.emputview);
    }

    private void initAdapter() {

        recyView.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                return false;
            }
        });
        recyView.setNestedScrollingEnabled(false);
        recyView.setHasFixedSize(true);
        recyView.setFocusable(false);


        focusRecycleView.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                return false;
            }
        });
        focusRecycleView.setNestedScrollingEnabled(false);
        focusRecycleView.setHasFixedSize(true);

    }

    public static TargetFragment newInstance(String info) {
        Bundle args = new Bundle();
        TargetFragment fragment = new TargetFragment();
        args.putString("tag", info);
        fragment.setArguments(args);
        return fragment;
    }

    public static TargetFragment newInstance(String info, String param, String enterType) {
        Bundle args = new Bundle();
        TargetFragment fragment = new TargetFragment();
        args.putString("tag", info);
        args.putString("param", param);
        args.putString("enterType", enterType);
        fragment.setArguments(args);
        return fragment;
    }


    private void requestdatas() {
        dialog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("user_id", param);
        requsMap.put("state", tagNo);
        requsMap.put("command", "ok-api.SelectTargetList");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        //TargetListBean
        url.getTargetListPost(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TargetListBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        requestSelfFocusTargetData();
                    }

                    @Override
                    public void onNext(TargetListBean msg) {
                        dialog.dismiss();

                        if (msg.getOp().getCode().equals("Y")) {
                            mySelfDatas = msg.getData();
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                        requestSelfFocusTargetData();

                    }
                });

    }


    private void requestSelfFocusTargetData() {

        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("state", tagNo);
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("command", "ok-api.SelectTargetPlanByFollow");

        url.getSelfFocusTargetList1(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GroupDashangBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        checkAllDatas();
                    }

                    @Override
                    public void onNext(GroupDashangBean msg) {
                        if (msg.getOp().getCode().equals("Y")) {
                            focusDatas = msg.getData();
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常！");
                        }

                        checkAllDatas();
                    }
                });

    }

    //TODO 数据一起处理
    private void checkAllDatas() {
        if ((mySelfDatas == null || mySelfDatas.size() < 1) &&
                (focusDatas == null || focusDatas.size() < 1)) {
            emptyView.setVisibility(View.VISIBLE);
            content_ll.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            content_ll.setVisibility(View.VISIBLE);
            showDatas();
        }
    }

    /**
     * 展示所有数据
     */
    private void showDatas() {

        if (mySelfDatas != null && mySelfDatas.size() > 0) {
            setDatas(mySelfDatas);
        }
        if (focusDatas != null && focusDatas.size() > 0) {
            focusRecycleView.setVisibility(View.VISIBLE);
            focus_count.setVisibility(View.VISIBLE);
            setFocusDatas();
        } else {
            focusRecycleView.setVisibility(View.GONE);
            focus_count.setVisibility(View.GONE);
        }


    }

    /**
     * 关注目标列表
     */
    private void setFocusDatas() {

        int len = 0;

        final List<TargetBean> datalist = new ArrayList<>();
        for (GroupItemDakaBean item : focusDatas) {
            TargetBean headBean = new TargetBean();
            CommonUserBean user = new CommonUserBean(item.getHead(), item.getNick_name());

            List<TargetBean> itemBean = item.getFollow_list();

            len += itemBean.size();

            if (itemBean != null && itemBean.size() > 0) {
                user.setUser_id(itemBean.get(0).getCreateuser());
                headBean.setUser(user);
                headBean.setShowType(2);
                datalist.add(headBean);
            } else {
                return;
            }

            for (TargetBean it : itemBean) {
                it.setShowType(1);
                it.setToUser(it.getCreateuser());
                datalist.add(it);
            }
        }

        focus_count.setText("关注的目标(" + len + ")个");

        GrDakaAdapter focusAdapter = new GrDakaAdapter(datalist, mContext);
        focusRecycleView.setAdapter(focusAdapter);

        focusAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                TargetBean tean = datalist.get(position);
                if (id == R.id.rl_item1) {
                    String name = tean.getName();
                    String targetId = tean.getTarget_uuid();

//                Intent intent = new Intent(mContext, TargetDetailActivity.class);

                    Intent intent = new Intent(mContext, TargetNewInfoActivity.class);
                    intent.putExtra(IntentParams.DAKA_Target_Id, targetId);

                    intent.putExtra(IntentParams.Target_FinishTag, tagNo);
                    intent.putExtra(IntentParams.Target_Name, name);
                    mContext.startActivity(intent);
                } else if (id == R.id.iv_head) {

//                    Intent mIntent = new Intent(mContext, MySpaceActivity.class);
//                    mIntent.putExtra(IntentParams.USER_ID, tean.getUser().getUser_id());
//                    mIntent.putExtra(IntentParams.USER_head, tean.getUser().getHead());
//                    mIntent.putExtra(IntentParams.UserName, tean.getUser().getNickname());
//                    mIntent.putExtra(IntentParams.STATE, "0");
//                    startActivity(mIntent);


                }


            }
        });


    }


    public void setDatas(final List<TargetBean> datas) {
        adapter = new MyTargetAdapter(R.layout.item_target, datas);
        adapter.setTag(tagNo);

        if (enterType.equals("1")) {

        } else {
            View view = setView(datas.size());
            adapter.addHeaderView(view, 0);
        }

        recyView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TargetBean tean = datas.get(position);
                String name = tean.getName();
                String targetId = tean.getTarget_uuid();

//                Intent intent = new Intent(mContext, TargetDetailActivity.class);

                Intent intent = new Intent(mContext, TargetNewInfoActivity.class);
                intent.putExtra(IntentParams.DAKA_Target_Id, targetId);

                intent.putExtra(IntentParams.Target_FinishTag, tagNo);
                intent.putExtra(IntentParams.Target_Name, name);
                mContext.startActivity(intent);
            }
        });

    }

    private View setView(int size) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycle_head, null);
        TextView tv = view.findViewById(R.id.count_text);
        tv.setText("我的目标(" + size + ")个");
        return view;
    }


    @Override
    protected void loadData() {

    }


    @Override
    public void onResume() {
        super.onResume();
        requestdatas();
    }

    @Override
    protected void reLoadData() {

    }

    @Override
    protected int getResId() {
        return R.layout.fragment_target;
    }


    @Override
    public void onClick(View v) {

    }


}
