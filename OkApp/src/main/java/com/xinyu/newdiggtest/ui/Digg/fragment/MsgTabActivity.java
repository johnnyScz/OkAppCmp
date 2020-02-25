package com.xinyu.newdiggtest.ui.Digg.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.xinyu.newdiggtest.APPConstans;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.RedMsg;
import com.xinyu.newdiggtest.config.ActyFinishEvent;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.IntentParams;
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

public class MsgTabActivity extends BaseNoEventActivity implements MsgFragment.OnFragmentInteractionListener {

    private String[] titles = {"消息", "私信"};

    @BindView(R.id.target_pager)
    ViewPager target_pager;

    @BindView(R.id.target_tab)
    public SlidingTabLayout target_tab;

    int noticeCount = 0, chatCount = 0;

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_msg_tab;
    }


    @OnClick(R.id.back)
    public void goCommit() {
        ActyFinishEvent.HomeFromMsgTab = 1;
        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentList = new ArrayList<Fragment>();

        fragmentList.add(MsgFragment.newInstance("2"));
        fragmentList.add(new PersonChatFragment());

        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        target_pager.setAdapter(adapter);
        target_pager.setCurrentItem(0);
        target_tab.setViewPager(target_pager);

        initView();
        regester();
    }

    private void initView() {
        int curent = Integer.parseInt(APPConstans.MsgTab);

        target_pager.setCurrentItem(curent);

        if (getIntent().hasExtra(IntentParams.Intent_Person_chat)) {
            target_pager.setCurrentItem(1);
        } else {
            target_pager.setCurrentItem(0);
        }
        timerCountBegin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

    }

    private void regester() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("close_msglist");
        registerReceiver(receiver, filter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("close_msglist")) {
                finish();
            }

        }
    };


    private List<Fragment> fragmentList;

    @Override
    public void onFragmentInteraction(String data) {

        if (data.equals("notice")) {
            noticeCount--;
            if (noticeCount > 0) {
                target_tab.showMsg(1, noticeCount);
            } else {
                target_tab.hideMsg(1);
            }

        } else if (data.equals("chat")) {
            chatCount--;
            if (chatCount > 0) {
                target_tab.showMsg(2, chatCount);
            } else {
                target_tab.hideMsg(2);
            }

        }


    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
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


    private void queryTotalRedMsgCount() {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "ok-api.GetMsgCount");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
//        requsMap.put("topics", "");
        url.getXhintCount(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RedMsg>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(RedMsg msg) {
                        if (msg.getOp().getCode().equals("Y")) {

                            showTabRedot(msg);

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });

    }

    /**
     * 展示消息红点
     *
     * @param msg
     */
    private void showTabRedot(RedMsg msg) {

//        List<RedMsgItem> dateList = msg.getDetail();
//        if (dateList != null && dateList.size() > 0) {
//            for (RedMsgItem item : dateList) {
//
//                if (item.getTopic().equals("notice")) {
//                    String count = item.getCount();
//
//                    noticeCount = Integer.parseInt(count);
//
//                    if (Integer.parseInt(count) > 0) {
//                        target_tab.showMsg(1, noticeCount);
//                    } else {
//                        target_tab.hideMsg(1);
//                    }
//
//
//                } else if (item.getTopic().equals("chat")) {
//                    String count = item.getCount();
//                    chatCount = Integer.parseInt(count);
//                    if (Integer.parseInt(count) > 0) {
//                        target_tab.showMsg(2, chatCount);
//                    } else {
//                        target_tab.hideMsg(2);
//                    }
//                }
//            }
//        }
    }


    private CountDownTimer mTimer;

    private void timerCountBegin() {
        if (mTimer == null) {
            mTimer = new CountDownTimer(50000 * 1000, 5000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    queryTotalRedMsgCount();
                }

                @Override
                public void onFinish() {


                }
            }.start();

        }
    }
}






