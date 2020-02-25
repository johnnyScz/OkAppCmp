package com.xinyu.newdiggtest.ui.Digg.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.AccountBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.CashActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.ChargeActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.OrderListActivity;
import com.xinyu.newdiggtest.ui.Walletfragment;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MyWalletActivity1 extends BaseActivity {


    @BindView(R.id.target_tab)
    public TabLayout tabLayout;

    @BindView(R.id.target_pager)
    public ViewPager viewPager;

    @BindView(R.id.my_account)
    public TextView my_account;


    private String[] titles = {"挑战金", "打赏", "奖励金", "红包"};
    private List<Fragment> fragmentList;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_wallet1;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //显示账户信息
        requestAccount();
    }

    private void initView() {
        initData();
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                AppUtils.setIndicator(tabLayout, 20, 20);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {

    }

    private void requestAccount() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        url.getAccountInfo(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AccountBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(AccountBean msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData() == null || MyTextUtil.isEmpty(msg.getData().getF_balance())) {
                                my_account.setText("0");
                            } else {
                                my_account.setText(msg.getData().getF_balance());
                            }
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });
    }


    public void initData() {

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(Walletfragment.newInstance("1"));
        fragmentList.add(Walletfragment.newInstance("2"));
        fragmentList.add(Walletfragment.newInstance("3"));
        fragmentList.add(Walletfragment.newInstance("4"));

        MyPagetAdapter adapter = new MyPagetAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        //绑定
        tabLayout.setupWithViewPager(viewPager);

    }


    class MyPagetAdapter extends FragmentPagerAdapter {

        public MyPagetAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        //重写这个方法，将设置每个Tab的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @OnClick(R.id.icon_back)
    public void goCommit() {
        finish();
    }


    @OnClick(R.id.tl_order)
    public void goOrder() {
        startActivity(new Intent(mContext, OrderListActivity.class));
    }


    @OnClick(R.id.cush_out)
    public void goCush() {
        startActivity(new Intent(mContext, CashActivity.class));
    }


    @OnClick(R.id.tl_recharge)
    public void goCharge() {
        startActivity(new Intent(mContext, ChargeActivity.class));
    }


}
