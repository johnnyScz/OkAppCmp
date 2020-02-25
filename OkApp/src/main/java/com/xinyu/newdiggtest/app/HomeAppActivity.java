package com.xinyu.newdiggtest.app;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.chaychan.library.BottomBarItem;
import com.chaychan.library.BottomBarLayout;
import com.dommy.qrcode.util.Constant;

import com.xinyu.newdiggtest.R;

import com.xinyu.newdiggtest.h5.ChannelService;
import com.xinyu.newdiggtest.h5.CommonService;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.BaseBean;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;

import com.xinyu.newdiggtest.ui.ChannelFragment;
import com.xinyu.newdiggtest.ui.Digg.fragment.ContactFragment;

import com.xinyu.newdiggtest.ui.SelfFragment;
import com.xinyu.newdiggtest.ui.XshellEvent;

import com.xinyu.newdiggtest.ui.calendar.FirstHomePageFragment;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.IntentParams;

import com.xinyu.newdiggtest.utils.LogUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HomeAppActivity extends BaseNoEventActivity {


    @BindView(R.id.vp_home)
    public ViewPager vpager;

    @BindView(R.id.bblayout)
    public BottomBarLayout bbLayout;

    //------------------以下是fragment------------------------------
    private List<Fragment> mFragmentList;


    public FragmentManager manager = getSupportFragmentManager();

    Intent serviceIntent;

    Intent channelService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

//        startActivity(new Intent(mContext, BdWebViewActivity.class));

        serviceIntent = new Intent(this, CommonService.class);
        startService(serviceIntent);

        channelService = new Intent(this, ChannelService.class);
        startService(channelService);

        upLoadDeviceToken();//上传设备信息

    }


    private void initReceiver() {

        //TODO 二维码扫描

        IntentFilter filter = new IntentFilter();
        filter.addAction("SCAN_SQcode");
        filter.addAction("msgCount");
//        registerReceiver(mReceiver, filter);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (getIntent().hasExtra("UMengPush")) {
            String pushMsg = getIntent().getStringExtra("UMengPush");
            Log.e("amtf", "pushMsg:" + pushMsg);
        }

        if (Constant.Push_Switch == 1) {
            vpager.setCurrentItem(1);
        } else if (Constant.Push_Switch == 2) {
            vpager.setCurrentItem(1);
        }


    }


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_home_tab;
    }


    private void initView() {


        initFragment();

        if (getIntent().hasExtra(IntentParams.Intent_Enter_Type)
                && getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("pushMsg")) {
            vpager.setCurrentItem(1);

        }

    }

    private void initFragment() {
        mFragmentList = new ArrayList<Fragment>();

        mFragmentList.add(new FirstHomePageFragment());

        mFragmentList.add(new ContactFragment());

        mFragmentList.add(new ChannelFragment());

        mFragmentList.add(new SelfFragment());

        vpager.setAdapter(new MyAdapter(getSupportFragmentManager()));

        vpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        //TODO 缓存fragment
//        vpager.setOffscreenPageLimit(2);

        bbLayout.setViewPager(vpager);

        bbLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(BottomBarItem bottomBarItem, int pos, int page) {
                if (page == 0) {
                    EventBus.getDefault().post(new XshellEvent(EventConts.MSG_Home_ResetDate));
                }
            }
        });

    }


    /**
     * 查询消息红点数
     */


    private long firstPressedTime;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime < 500) {
            finish();
            Process.killProcess(Process.myPid());

        } else {
            ToastUtils.getInstanc(this).showToast("再点一次退出");
            firstPressedTime = System.currentTimeMillis();
        }
    }


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        vpager.removeAllViews();
        vpager = null;
        stopService(serviceIntent);
        stopService(channelService);

    }


    /**
     * 上传设备信息
     */
    public void upLoadDeviceToken() {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();

        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("model", AppUtils.getPhoneModel());
        requsMap.put("release", AppUtils.getBuildVersion());
        requsMap.put("deviceid", AppUtils.getDeviceId(mContext));
        requsMap.put("version", AppUtils.getVersionName(mContext));
        requsMap.put("devicetoken", PreferenceUtil.getInstance(mContext).getUmengToken());


        url.insertDeviceLog(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "设备信息上传失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {
                            LogUtil.e("设备信息上传成功");
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        getSupportFragmentManager().getFragments();
        if (getSupportFragmentManager().getFragments().size() > 0) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment mFragment : fragments) {
                mFragment.onActivityResult(requestCode, resultCode, data);
            }
        }

    }
}
