package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.FocusPersonAdapter;
import com.xinyu.newdiggtest.bean.FocusPersonBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;

import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;


import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class FocusPersonFragment extends BaseFragment {


    RecyclerView recyclerView;

    ImageView emptyView;

    FocusPersonAdapter adapter;


    @Override
    protected void initView() {

        recyclerView = rootView.findViewById(R.id.recyclerView);
        emptyView = rootView.findViewById(R.id.emputview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器

    }

    @Override
    protected void loadData() {

    }


    @Override
    public void onResume() {
        super.onResume();
        requestSelfInfo();
    }

    public void requestSelfInfo() {

        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        url.getFocusPersonList(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FocusPersonBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(FocusPersonBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData() == null || msg.getData().size() < 1) {
                                showEmpty(true, msg.getData());

                            } else {
                                showEmpty(false, msg.getData());
                            }

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });

    }

    private void showEmpty(boolean show, List<FocusPersonBean.PersonBean> data) {
        if (show) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            showData(data);
        }

    }

    private void showData(final List<FocusPersonBean.PersonBean> data) {
        adapter = new FocusPersonAdapter(R.layout.item_focus_person, data);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                FocusPersonBean.PersonBean item = data.get(position);

                goSpackAty(item);

            }
        });

    }

    /**
     * 跳转到个人空间
     *
     * @param item
     */
    private void goSpackAty(FocusPersonBean.PersonBean item) {

//        Intent mIntent = new Intent(mContext, MySpaceActivity.class);
//        mIntent.putExtra(IntentParams.USER_ID, item.getUser_id());
//        mIntent.putExtra(IntentParams.USER_head, item.getHead());
//        mIntent.putExtra(IntentParams.UserName, item.getNick_name());
//        mIntent.putExtra(IntentParams.STATE, "0");
//        startActivity(mIntent);
    }


    @Override
    protected void reLoadData() {

    }

    @Override
    protected int getResId() {
        return R.layout.fragment_foucus_person;
    }
}
