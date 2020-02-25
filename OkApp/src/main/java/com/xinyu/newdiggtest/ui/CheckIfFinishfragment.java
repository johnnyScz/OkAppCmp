package com.xinyu.newdiggtest.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.CheckFinishAdapter;
import com.xinyu.newdiggtest.bean.CheckFinshReturnBean;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 监督人对我的目标的评定结果
 */
public class CheckIfFinishfragment extends LazySingleFragment {

    Context ctx;


    @BindView(R.id.chat_list)
    RecyclerView recyclerView;//

    @BindView(R.id.emputview)
    ImageView emputview;//


    CheckFinishAdapter adapter;

    String armId = "";

    List<CheckFinshReturnBean.CheckFinshListBean> datelist;


    public void setTargetId(String targetid) {
        this.armId = targetid;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_common_everywhere;
    }

    @Override
    protected void initView() {
        initRecycle();
    }

    @Override
    public void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            if (datelist == null || datelist.size() < 1) {
                requestDatas();
            }
        }
    }


    private void setDatas(List<CheckFinshReturnBean.CheckFinshListBean> data) {

        if (data == null || data.size() == 0) {
            showEmpty(true);
        } else {
            showEmpty(false);
            fillData(data);
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
     */
    private void fillData(List<CheckFinshReturnBean.CheckFinshListBean> data) {
        adapter = new CheckFinishAdapter(R.layout.item_check_finish, data);
        recyclerView.setAdapter(adapter);
    }


    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
    }


//    @Override
//    protected void onFragmentVisibleChange(boolean isVisible) {

//    }

    private void requestDatas() {
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("aim_id", armId);
        map.put("command", "ok-api.SelectHeartenScore");
        url.checkMyTargetList(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CheckFinshReturnBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(CheckFinshReturnBean msg) {

                        String code = msg.getOp().getCode();
                        if (code.equals("Y")) {
                            if (msg.getData() == null || msg.getData().size() < 1) {
                                setDatas(null);
                                return;
                            }
                            datelist = msg.getData();
                            setDatas(datelist);

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
    }


}
