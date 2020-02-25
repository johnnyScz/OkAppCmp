//package com.xinyu.newdiggtest.ui.Digg.fragment;
//
//import android.content.Intent;
//
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.xinyu.newdiggtest.R;
//import com.xinyu.newdiggtest.adapter.QunzuAdapter;
//
//import com.xinyu.newdiggtest.bean.FocusRetuBean;
//import com.xinyu.newdiggtest.bean.FocusTodoBean;
//import com.xinyu.newdiggtest.net.AppUrl;
//import com.xinyu.newdiggtest.net.RetrofitServiceManager;
//import com.xinyu.newdiggtest.ui.App;
//
//import com.xinyu.newdiggtest.utils.PreferenceUtil;
//
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import butterknife.BindView;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
//
///**
// *
// */
//public class InnerQunFragment extends BaseFragment {
//
//    @BindView(R.id.recyclerView)
//    RecyclerView recyView;
//
//
//    List<FocusTodoBean> totalList = new ArrayList<>();
//
//    QunzuAdapter adapter;
//
//
//    String userId = "";
//
//
//    public void setUserId(String uid) {
//
//        this.userId = uid;
//    }
//
//
//    @Override
//    protected void initView() {
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(App.mContext);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
//
//    }
//
//
//    @Override
//    protected void loadData() {
//        if (totalList.size() < 1) {
//            queryTotalFocus(0);
//        }
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        queryTotalFocus(1);
//    }
//
//    @Override
//    protected void reLoadData() {
//
//    }
//
//    @Override
//    protected int getResId() {
//        return R.layout.fragment_inner;
//    }
//
//
//    public void queryTotalFocus(final int tag) {
//
//        totalList.clear();
//
//        if (tag < 1)
//            dialog.show();
//
//        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
//        AppUrl url = manager.create(AppUrl.class);
//        HashMap<String, String> requsMap = new HashMap<>();
//
//        requsMap.put("sid", PreferenceUtil.getInstance(App.mContext).getSessonId());
//
//
//        requsMap.put("user_id", userId);
//
//        requsMap.put("f_company_id", PreferenceUtil.getInstance(App.mContext).getCompanyId());
//
//        url.queryRelationFocus(requsMap).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<FocusRetuBean>() {
//                    @Override
//                    public void onCompleted() {
//
//                        if (dialog.isShowing())
//                            dialog.dismiss();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//
//                    }
//
//                    @Override
//                    public void onNext(FocusRetuBean msg) {
//
//
//                        if (msg.getOp().getCode().equals("Y")) {
//
//                            if (msg.getData() != null && msg.getData().size() > 0) {
//                                List<FocusTodoBean> datas = msg.getData();
//
//                                totalList.addAll(datas);
//                                freshDatas(totalList);
//                            }
//                        }
//                    }
//                });
//    }
//
//    private void freshDatas(List<FocusTodoBean> totalList) {
//
//        recyView.removeAllViews();
//        if (totalList.size() < 1)
//            return;
//
//        adapter = new QunzuAdapter(totalList);
//        recyView.setAdapter(adapter);
//
//
//    }
//
//
//}
