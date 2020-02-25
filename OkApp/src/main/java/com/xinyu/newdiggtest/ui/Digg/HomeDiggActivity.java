package com.xinyu.newdiggtest.ui.Digg;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xinyu.newdiggtest.R;

import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.FriendFragment;
import com.xinyu.newdiggtest.ui.NewSelfFragment;
import com.xinyu.newdiggtest.ui.ParentTargetFragment;
import com.xinyu.newdiggtest.ui.XshellEvent;

import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.HandleQcodeUtil;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import butterknife.BindView;
import io.reactivex.functions.Consumer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeDiggActivity extends BaseNoEventActivity {


    @BindView(R.id.vp_home)
    public ViewPager vpager;

    @BindView(R.id.bblayout)
    public BottomBarLayout bbLayout;

    //------------------以下是fragment------------------------------
    private List<Fragment> mFragmentList;


    public FragmentManager manager = getSupportFragmentManager();

    FriendFragment friendFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        check2StartService();
        initView();
        initReceiver();
        askPermissions();
        checkDevicToken();


    }

    private void check2StartService() {

//        if (!AppUtils.isServiceRunning(mContext, "H5Service")) {
//            startService(new Intent(this, H5Service.class));
//
//        } else {
//            Log.w("amtf1", "H5Service已经运行");
//        }


    }


    private void checkDevicToken() {
        upLoadDevToken();
    }


    private void upLoadDevToken() {

        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("model", AppUtils.getPhoneModel());
        requsMap.put("release", AppUtils.getBuildVersion());
        requsMap.put("deviceid", AppUtils.getDeviceId(mContext));
        requsMap.put("version", AppUtils.getVersionName(mContext));
        requsMap.put("devicetoken", PreferenceUtil.getInstance(mContext).getUmengToken());

        url.upDeviceToken(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        if (msg.getOp().getCode().equals("Y")) {
                            PreferenceUtil.getInstance(mContext).setLoadDevToken(true);
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("deviceToken服务异常");
                        }
                    }
                });

    }


    private void askPermissions() {
        RxPermissions rxPermission = new RxPermissions(mContext);
        rxPermission.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {

                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 下次再次启动时，还会提示请求权限的对话框
                            Log.e("amtf", permission.name + " is denied. More info should be provided.用户拒绝了该权限，没有选中『不再询问』");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                        }
                    }
                });

    }


    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("SCAN_SQcode");
        filter.addAction("msgCount");
        registerReceiver(mReceiver, filter);

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
            friendFragment.setTagSwict(0);
        } else if (Constant.Push_Switch == 2) {
            vpager.setCurrentItem(1);
            friendFragment.setTagSwict(1);

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

//        mFragmentList.add(new DingdingFragment());
        friendFragment = new FriendFragment();

        mFragmentList.add(friendFragment);
        mFragmentList.add(new ParentTargetFragment());
        mFragmentList.add(new NewSelfFragment());

//        mFragmentList.add(new BannerFragment());
//        mFragmentList.add(new BannerFragment());
//        mFragmentList.add(new BannerFragment());
//        mFragmentList.add(new NewSelfFragment());


        vpager.setAdapter(new MyAdapter(getSupportFragmentManager()));


        vpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        vpager.setOffscreenPageLimit(1);

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


    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("SCAN_SQcode")) {
                String qcode = intent.getStringExtra(Constant.INTENT_EXTRA_KEY_QR_SCAN);
                new HandleQcodeUtil(mContext).parseData(qcode);
            } else if (intent.getAction().equals("msgCount")) {
                int count = intent.getIntExtra("msgCount", 0);
                bbLayout.setUnread(1, count);
            }

        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
//        stopService(new Intent(mContext, H5Service.class));
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        PreferenceUtil.getInstance(this).setAppFun("0");
    }
}
