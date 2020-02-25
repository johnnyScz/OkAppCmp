package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.MyTargetAdapter;
import com.xinyu.newdiggtest.bean.SpaceTargetRetunBean;
import com.xinyu.newdiggtest.bean.TargetBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.TargetNewInfoActivity;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 个人空间目标
 */
public class SpaceTargetFragment extends BaseFragment implements View.OnClickListener {

    EasyRecyclerView recyView;

    SmartRefreshLayout refreshLayout;

    MyTargetAdapter adapter;

    ImageView emptyView;

    String tagNo = "", param = "";

    @Override
    protected void initView() {
        tagNo = getArguments().getString("tag");
        param = getArguments().getString("param");
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        recyView = rootView.findViewById(R.id.recyclerView);
        emptyView = rootView.findViewById(R.id.emputview);
        initAdapter();

    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
    }

    public static SpaceTargetFragment newInstance(String info) {
        Bundle args = new Bundle();
        SpaceTargetFragment fragment = new SpaceTargetFragment();
        args.putString("tag", info);
        fragment.setArguments(args);
        return fragment;
    }

    public static SpaceTargetFragment newInstance(String info, String param) {
        Bundle args = new Bundle();
        SpaceTargetFragment fragment = new SpaceTargetFragment();
        args.putString("tag", info);
        args.putString("param", param);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        requestdatas();
    }

    private void requestdatas() {
        dialog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("user_id", param);
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("page_num", "1");
        requsMap.put("page_size", "40");

        url.getSpaceTargetList(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SpaceTargetRetunBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(SpaceTargetRetunBean msg) {
                        dialog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData().getTargetplan() == null || msg.getData().getTargetplan().size() == 0) {
                                showEmpyt(true);
                            } else {
                                showEmpyt(false);
                                fillData(msg.getData().getTargetplan());
                            }

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }


                    }
                });
    }

    private void fillData(final List<TargetBean> targetplan) {


        adapter = new MyTargetAdapter(R.layout.item_target, targetplan);

        if (targetplan.size() <= 3) {
            View footer = LayoutInflater.from(mContext).inflate(R.layout.adapter_footer, null);
            adapter.addFooterView(footer);
        }


        adapter.setTag("0");
        recyView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TargetBean tean = targetplan.get(position);
                Intent mitent = mContext.getIntent();
                mitent.setClass(mContext, TargetNewInfoActivity.class);
                mitent.putExtra(IntentParams.Target_FinishTag, tagNo);
                mitent.putExtra(IntentParams.Target_Name, tean.getName());
                mitent.putExtra(IntentParams.DAKA_Target_Id, tean.getTarget_uuid());
                startActivity(mitent);
            }
        });
    }

    private void showEmpyt(boolean show) {
        if (show) {
            refreshLayout.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            refreshLayout.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

    }


    @Override
    protected void loadData() {

    }


    @Override
    protected void reLoadData() {

    }

    @Override
    protected int getResId() {
        return R.layout.fragment_space;
    }


    @Override
    public void onClick(View v) {

    }
}
