package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.RelationAdapter;
import com.xinyu.newdiggtest.bean.OrderReturnBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderListActivity extends BaseNoEventActivity {

    RelationAdapter adapter;

    @BindView(R.id.recyclerView)
    public RecyclerView recyView;


    @BindView(R.id.refreshLayout)
    public SmartRefreshLayout refreshLayout;


    @BindView(R.id.emputview)
    public ImageView emptyView;

    List<OrderReturnBean.OrderlistBean> datalist = new ArrayList<>();

    int pageCount = 1;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_order_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecycle();

        quetyList();
    }

    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                pageCount++;
                quetyList();
            }
        });

//        adapter = new RelationAdapter(R.layout.item_order, datalist);

        recyView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OrderReturnBean.OrderlistBean data = datalist.get(position);

                TextView tv = view.findViewById(R.id.tv_title);


                TextView money = view.findViewById(R.id.money);

                TextView pay_status = view.findViewById(R.id.pay_status);

                String titleState = "";
                if (MyTextUtil.isEmpty(pay_status.getText().toString())) {
                    titleState = "交易完成";
                } else {
                    if (pay_status.getText().toString().equals("交易关闭")) {
                        titleState = "交易失败";
                    } else {
                        titleState = pay_status.getText().toString();
                    }
                }


                Intent intent = new Intent(mContext, AcountInfoActivity.class);
                intent.putExtra("data", data);
                intent.putExtra("title", tv.getText().toString());
                intent.putExtra("money", money.getText().toString());
                intent.putExtra("tv_target", data.getF_content());
                intent.putExtra("curentState", titleState);

                intent.putExtra("f_type", data.getF_type());

                startActivity(intent);


            }
        });

    }


    /**
     * 请求订单(需要做分页)
     */

    public void quetyList() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "ok-api.SelectTargetOrderByUser");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());

        requsMap.put("curPageNo", pageCount + "");
        requsMap.put("pageRowCnt", "15");

        url.queryOrderList(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OrderReturnBean>() {
                    @Override
                    public void onCompleted() {
                        refreshLayout.finishLoadMore();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();

                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(OrderReturnBean msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getDatalist() == null || msg.getDatalist().size() < 1) {
                                ToastUtils.getInstanc().showToast("没有更多数据了！");

                            } else {
                                List<OrderReturnBean.OrderlistBean> datas = msg.getDatalist();
                                datalist.addAll(datas);
                                adapter.notifyDataSetChanged();
                            }

                            if (datalist.size() > 0) {
                                showEmpty(false);
                            } else {
                                showEmpty(true);
                            }


                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });
    }

    /**
     * 显示与隐藏
     *
     * @param show
     */
    private void showEmpty(boolean show) {
        if (show) {
            emptyView.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
        }


    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


}




