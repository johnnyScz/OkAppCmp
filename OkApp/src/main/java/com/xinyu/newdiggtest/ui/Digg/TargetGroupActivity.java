package com.xinyu.newdiggtest.ui.Digg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.MultiGridRecycleAdapter;
import com.xinyu.newdiggtest.bean.TargetKindItemBean;
import com.xinyu.newdiggtest.bean.TargetKindRetBean;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.Digg.target.CreateTargetActivity;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 新建目标
 */
public class TargetGroupActivity extends BaseNoEventActivity {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

//    TargetKindAdapter adapter;

    MultiGridRecycleAdapter adapter;

    List<TargetKindItemBean> datalist = new ArrayList<>();


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_add_target;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        requestTargetKind();
    }

    private void initView() {
        GridLayoutManager mgr = new GridLayoutManager(mContext, 3);
        recyclerView.setLayoutManager(mgr);
        recyclerView.setNestedScrollingEnabled(false);
    }


    private void requestTargetKind() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.SelectTargetClass");
        url.targetKind(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TargetKindRetBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(TargetKindRetBean msg) {
                        loadingDailog.dismiss();
                        String code = msg.getOp().getCode();
                        if (code.equals("Y")) {
                            if (msg.getData() == null || msg.getData().getStandard() == null
                                /* || msg.getData().getTemplate() == null*/) {

                                return;
                            }

                            loadData(msg.getData());

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });

    }

    /**
     * 填充数据
     *
     * @param data
     */
    private void loadData(TargetKindRetBean.KindTargetData data) {
        datalist.clear();

//        List<TargetKindItemBean> tushu = data.getTemplate();
//        for (TargetKindItemBean ts : tushu) {
//            ts.setViewType(2);
//        }
//
//        datalist.addAll(tushu);

        List<TargetKindRetBean.KindTargetData.StandardBean> standlist = data.getStandard();

        for (TargetKindRetBean.KindTargetData.StandardBean itme : standlist) {

            TargetKindItemBean itemBean = new TargetKindItemBean();
            itemBean.setViewType(1);
            itemBean.setF_title(itme.getF_title());
            datalist.add(itemBean);

            List<TargetKindItemBean> child = itme.getChild();
            for (TargetKindItemBean kk : child) {
                kk.setViewType(2);
            }
            datalist.addAll(child);
        }

        if (datalist.size() > 0) {
            adapter = new MultiGridRecycleAdapter(mContext, datalist);
            recyclerView.setAdapter(adapter);


            adapter.setOnListner(new MultiGridRecycleAdapter.OnItemDataClickListner() {
                @Override
                public void onItemClick(TargetKindItemBean item) {
                    Intent mItent = new Intent(mContext, CreateTargetActivity.class);
                    mItent.putExtra(IntentParams.Target_create_postion, item.getF_img());//选择的图标
                    mItent.putExtra(IntentParams.SELECT_Target_Name, item.getF_title());
                    mItent.putExtra(IntentParams.Intent_Enter_Type, "TargetGroupActivity");
                    startActivity(mItent);
                    finish();

                }
            });


        }

    }

    @OnClick(R.id.iv_back)
    public void goback() {
        finish();
    }


}
