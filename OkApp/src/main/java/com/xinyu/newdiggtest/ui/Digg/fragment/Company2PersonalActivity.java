//package com.xinyu.newdiggtest.ui.Digg.fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.xinyu.newdiggtest.R;
//import com.xinyu.newdiggtest.adapter.MyAdapter;
//import com.xinyu.newdiggtest.bean.CompanyBean;
//import com.xinyu.newdiggtest.bean.MobieCompanyBean;
//import com.xinyu.newdiggtest.bean.UserInfoBean;
//
//import com.xinyu.newdiggtest.net.AppUrl;
//import com.xinyu.newdiggtest.net.NetApi;
//import com.xinyu.newdiggtest.net.RetrofitServiceManager;
//import com.xinyu.newdiggtest.net.bean.Info;
//import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
//
//
//import com.xinyu.newdiggtest.ui.Digg.GuidActivity;
//import com.xinyu.newdiggtest.ui.Digg.HomeDiggActivity;
//import com.xinyu.newdiggtest.utils.AppUtils;
//import com.xinyu.newdiggtest.utils.MyTextUtil;
//import com.xinyu.newdiggtest.utils.PreferenceUtil;
//import com.xinyu.newdiggtest.utils.ToastUtils;
//
//import java.util.HashMap;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
//public class Company2PersonalActivity extends BaseNoEventActivity {
//
//
//    int hasMobile = 0;
//
//    @BindView(R.id.recleview)
//    RecyclerView recyclerView;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        startService(new Intent(this, H5Service.class));
//        checkDevicToken();
//        getUserIfhasPhone();
//    }
//
//
//    public void getUserIfhasPhone() {
//        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
//        AppUrl url = manager.create(AppUrl.class);
//        HashMap<String, String> requsMap = new HashMap<>();
//        requsMap.put("command", "user.getUserInfo");
//        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
//        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
//        requsMap.put("flag", "Y");
//        url.getUserInfo(requsMap).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<UserInfoBean>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                        Log.e("amtf", e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(UserInfoBean msg) {
//                        if (msg.getOp().getCode().equals("Y")) {
//                            if (msg.getUser() != null) {
////                                check(msg.getUser());
//                            }
//                        } else {
//                            ToastUtils.getInstanc(mContext).showToast("获取用户信息服务异常");
//                        }
//                    }
//                });
//    }
//
//    private void check(UserInfoBean.UserBean user) {
//
//        if (!MyTextUtil.isEmpty(user.getMobile()) && user.getMobile().toString().length() >= 7) {
//            hasMobile = 1;
//            PreferenceUtil.getInstance(mContext).setMobilePhone(user.getMobile().toString());
//            getCompanyInfoByMobile(user.getMobile().toString());
//
//        } else {
//            showClickInfo("你还没有绑定手机号!");
//        }
//    }
//
//    private void showClickInfo(final String info) {
//        this.findViewById(R.id.company_fun).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtils.getInstanc().showToast(info);
//            }
//        });
//
//
//    }
//
//    private void getCompanyInfoByMobile(String mobile) {
//        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
//        NetApi url = manager.create(NetApi.class);
//        HashMap<String, String> requsMap = new HashMap<>();
//        requsMap.put("command", "api2e.GetCompanyList");
//        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
//        requsMap.put("mobile", mobile);
//
//        url.getCompanyInfoByMobile(requsMap).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<MobieCompanyBean>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("amtf", e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(MobieCompanyBean msg) {
//
//                        if (msg.getOp().getCode().equals("Y")) {
//
//                            if (msg.getData() != null && msg.getData().size() > 0) {
//
//                                showCompanyList(msg.getData());
//
//                            } else {
//                                showClickInfo("您的手机号码没有注册到任何公司");
//
//                            }
//
//                        } else {
//                            ToastUtils.getInstanc().showToast("获取公司列表服务异常");
//                        }
//
//
//                    }
//                });
//
//
//    }
//
//    /**
//     * 展示公司列表
//     *
//     * @param data
//     */
//    private void showCompanyList(final List<CompanyBean> data) {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
//
//        MyAdapter adapter = new MyAdapter(R.layout.item_pop_company, data);
//        recyclerView.setAdapter(adapter);
//
//        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//
//                CompanyBean datt = data.get(position);
//                PreferenceUtil.getInstance(mContext).setCompanyId(datt.getCompany_id());
//                PreferenceUtil.getInstance(mContext).setAppFun("1");
//
//                Intent itent = new Intent(mContext, LoadingActivity.class);
//                itent.putExtra("company", datt);
//                startActivity(itent);
//
//
//            }
//        });
//
//
//    }
//
//
//    @Override
//    protected int getLayoutResouce() {
//        return R.layout.activity_company_person;
//    }
//
//
//    @OnClick(R.id.company_fun)
//    public void company() {
//        ToastUtils.getInstanc().showToast("该功能暂未开通");
//    }
//
//
//    @OnClick(R.id.person_fun)
//    public void goPerson() {
//        PreferenceUtil.getInstance(mContext).setAppFun("0");
//
//        if (PreferenceUtil.getInstance(mContext).isAppFirstEnter()) {
//            PreferenceUtil.getInstance(mContext).setAppFirstEnter(false);
//            startActivity(new Intent(mContext, GuidActivity.class));
//        } else {
//            startActivity(new Intent(mContext, HomeDiggActivity.class));
//        }
//    }
//
//
//    private void upLoadDevToken() {
//
//        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
//        AppUrl url = manager.create(AppUrl.class);
//
//        HashMap<String, String> requsMap = new HashMap<>();
//        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
//        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
//        requsMap.put("model", AppUtils.getPhoneModel());
//        requsMap.put("release", AppUtils.getBuildVersion());
//        requsMap.put("deviceid", AppUtils.getDeviceId(mContext));
//        requsMap.put("version", AppUtils.getVersionName(mContext));
//        requsMap.put("devicetoken", PreferenceUtil.getInstance(mContext).getUmengToken());
//
//        url.upDeviceToken(requsMap).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Info>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(Info msg) {
//                        if (msg.getOp().getCode().equals("Y")) {
//                            PreferenceUtil.getInstance(mContext).setLoadDevToken(true);
//                        } else {
//                            ToastUtils.getInstanc(mContext).showToast("服务异常");
//                        }
//                    }
//                });
//
//    }
//
//
//    private void checkDevicToken() {
//        upLoadDevToken();
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        stopService(new Intent(mContext, H5Service.class));
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        return;
//    }
//}
//
//
//
//
