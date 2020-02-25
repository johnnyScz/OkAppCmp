package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.CardPersonAdapter;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.CardChildBean;
import com.xinyu.newdiggtest.net.bean.CmpBeanCard;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * card列表
 */
public class CardListActivity extends BaseNoEventActivity {

    CardPersonAdapter adapter;

    @BindView(R.id.recyclerView)
    public RecyclerView recyView;

    @BindView(R.id.title)
    public TextView title;


    @BindView(R.id.emputview)
    public ImageView emptyView;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_person_card;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        title.setText(getIntent().getStringExtra("title"));

        initRecycle();

        queryCmpCard();
    }

    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
    }


    /**
     * 显示与隐藏
     *
     * @param datalist
     * @param show
     */
    private void showEmpty(List<CardChildBean> datalist, boolean show) {
        if (show) {
            emptyView.setVisibility(View.VISIBLE);
            recyView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyView.setVisibility(View.VISIBLE);

            adapter = new CardPersonAdapter(R.layout.item_card_person, datalist);
            recyView.setAdapter(adapter);

            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                    CardChildBean item = (CardChildBean) adapter.getData().get(position);

                    Intent intent = new Intent(mContext, CardInfoActivity.class);

                    intent.putExtra("title", item.getF_name());
                    intent.putExtra("plugin_id", item.getF_plugin_id());

//                    intent.putExtra("head", item.get);

                    intent.putExtra("signer", item.getF_create_by());


                    startActivity(intent);

                }
            });


        }
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
        requsMap.put("f_organization_id", getIntent().getStringExtra("f_id"));

        url.queryCardDetail(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CmpBeanCard>() {
                    @Override
                    public void onCompleted() {

                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CmpBeanCard msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() != null && msg.getData().size() > 0) {

                                showEmpty(msg.getData(), false);

                            } else {
                                showEmpty(null, true);
                            }

                        }


                    }
                });
    }


}




