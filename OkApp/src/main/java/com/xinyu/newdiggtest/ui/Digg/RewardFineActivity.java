package com.xinyu.newdiggtest.ui.Digg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.RewardFineAdapter;
import com.xinyu.newdiggtest.bean.RewardListBean;
import com.xinyu.newdiggtest.bean.RewardReturnBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 奖励金详情
 */
public class RewardFineActivity extends BaseNoEventActivity {


    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;


    @BindView(R.id.emputview)
    public ImageView emputview;

    @BindView(R.id.money_total)
    public TextView money_total;


    RewardFineAdapter adapter;

    String targetId, moneyTx;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_reward_fine;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
        targetId = getIntent().getStringExtra(IntentParams.DAKA_Target_Id);
        moneyTx = getIntent().getStringExtra("money");
        money_total.setText("¥" + moneyTx);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestRewardDatas();
    }


    @OnClick(R.id.iv_back)
    public void goCommit() {
        finish();
    }

    private void requestRewardDatas() {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("aim_id", targetId);
        url.rewardFineList(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RewardReturnBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(RewardReturnBean msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() == null || msg.getData().size() < 1) {
                                showEmpty(true, null);

                            } else {
                                showEmpty(false, msg.getData());
                            }

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常！");
                        }

                    }
                });

    }

    /**
     * 显示数据
     *
     * @param list
     */
    private void showEmpty(boolean show, List<RewardListBean> list) {

        if (show) {
            emputview.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emputview.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            adapter = new RewardFineAdapter(R.layout.item_rewardlist, list);
            recyclerView.setAdapter(adapter);
        }

    }


}
