package com.xinyu.newdiggtest.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;


import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.WalletAdapter;
import com.xinyu.newdiggtest.bean.WalletDashangBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.Digg.fragment.BaseFragment;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 钱包相关打卡fragment
 */
public class Walletfragment extends BaseFragment {

    Context ctx;
    RecyclerView recyclerView;//


    ImageView emputview;//
    String param = "";
    WalletAdapter adapter;


    @Override
    protected void initView() {
        param = getArguments().getString("param");
        recyclerView = rootView.findViewById(R.id.chat_list);

        emputview = rootView.findViewById(R.id.emputview);
        initRecycle();
    }


    public static Walletfragment newInstance(String info) {
        Bundle args = new Bundle();
        Walletfragment fragment = new Walletfragment();
        args.putString("param", info);//网络请求的类型
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void loadData() {
        requestListData(param);
    }

    private void requestDashangdatas() {
        dialog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();

        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        url.getWalletDashang(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WalletDashangBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(WalletDashangBean msg) {
                        dialog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            setDatas(msg);

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常！");
                        }

                    }
                });


    }

    private void setDatas(WalletDashangBean msg) {

        if (msg.getData() == null || msg.getData().size() == 0) {
            showEmpty(true);

        } else {
            showEmpty(false);
            fillData(msg);
        }


    }

    private void showEmpty(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.GONE);
            emputview.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emputview.setVisibility(View.GONE);
        }
    }

    /**
     * 填充数据
     *
     * @param msg
     */
    private void fillData(WalletDashangBean msg) {
        adapter = new WalletAdapter(R.layout.item_wallet, msg.getData());
        adapter.setFragmentType(param);
        recyclerView.setAdapter(adapter);

    }


    /**
     * 请求数据
     *
     * @param param
     */
    private void requestListData(String param) {
        switch (param) {
            case "1":
                requestTiaozhanDatas();
                break;

            //打赏
            case "2":
                requestDashangdatas();
                break;

            //奖励金
            case "3":
                requestRewardDatas();
                break;


        }

    }

    private void requestTiaozhanDatas() {
        dialog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        url.getWalletTiaozhan(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WalletDashangBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(WalletDashangBean msg) {
                        dialog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            setDatas(msg);

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常！");
                        }

                    }
                });

    }

    /**
     * 奖励金
     */
    private void requestRewardDatas() {
        dialog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();

        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        url.getWalletReward(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WalletDashangBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(WalletDashangBean msg) {
                        dialog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            setDatas(msg);
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常！");
                        }

                    }
                });

    }


    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {

    }


    @Override
    protected void reLoadData() {

    }

    @Override
    protected int getResId() {
        return R.layout.fragment_wallet;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
