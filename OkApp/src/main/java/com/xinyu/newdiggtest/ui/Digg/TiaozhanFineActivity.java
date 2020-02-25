package com.xinyu.newdiggtest.ui.Digg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.TiaozhaFineAdapter;
import com.xinyu.newdiggtest.bean.FineListBean;
import com.xinyu.newdiggtest.bean.TiaozhanReturnBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TiaozhanFineActivity extends BaseNoEventActivity {


    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;


    @BindView(R.id.emputview)
    public ImageView emputview;

    @BindView(R.id.money_total)
    public TextView money_total;


    TiaozhaFineAdapter adapter;

    String targetId, moneyTx;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_tiaozhan_fine;
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
        requestTiaozhanDatas();
    }


    @OnClick(R.id.iv_back)
    public void goCommit() {
        finish();
    }

    private void requestTiaozhanDatas() {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("aim_id", targetId);
        requsMap.put("command", "ok-api.SelectChallengeGoldByTargetId");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        url.getTiaozhanInfo(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TiaozhanReturnBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(TiaozhanReturnBean msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getExcita_list() == null || msg.getExcita_list().size() < 1) {
                                showEmpty(true, null);

                            } else {
                                showEmpty(false, msg.getExcita_list());
                            }

                        } else {
//                            ToastUtils.getInstanc(mContext).showToast("服务异常！");
                        }

                    }
                });

    }

    /**
     * 显示数据
     *
     * @param list
     */
    private void showEmpty(boolean show, List<FineListBean> list) {

        if (show) {
            emputview.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emputview.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            adapter = new TiaozhaFineAdapter(R.layout.item_finelist, list);
            recyclerView.setAdapter(adapter);
        }

    }


}
