//package com.xinyu.newdigg.ui;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.BottomNavigationView;
//import android.support.design.widget.NavigationView;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//
//
//import com.xinyu.newdigg.R;
//import com.xinyu.newdigg.helper.BottomNavigationViewHelper;
//import com.xinyu.newdigg.utils.AppUtils;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
//
//    @BindView(R.id.nav_view)
//    NavigationView nvView;
//    ImageView iv;
//
//    @BindView(R.id.bottom_navigation_view)
//    BottomNavigationView mBottomNavigationView;
//
//    private MenuItem mItemWelfare;
//    private MenuItem mItemVideo;
//    private MenuItem mItemAboutUs;
//    private MenuItem mItemLogout;
//
//
//    //------------------以下是fragment------------------------------
//    private List<Fragment> mFragmentList;
//    private Fragment mCurrentFragment;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.mall_activity_home);
//        AppUtils.setStatusBarColor(this, R.color.mall_blue);
//        ButterKnife.bind(this);
//        EventBus.getDefault().register(this);
//        initView();
//    }
//
//    private void initView() {
//        nvView.setNavigationItemSelectedListener(this);
//        TextView tv = nvView.getHeaderView(0).findViewById(R.id.nav_header_login_tv);
//        iv = nvView.getHeaderView(0).findViewById(R.id.img_logo);
//        tv.setText("世界杯");
//        iv.setOnClickListener(this);
//
//        mItemWelfare = nvView.getMenu().findItem(R.id.nav_item_welfare);
//        //视频
//        mItemVideo = nvView.getMenu().findItem(R.id.nav_item_video);
//        //关于我们
//        mItemAboutUs = nvView.getMenu().findItem(R.id.nav_item_about_us);
//        //退出登录
//        mItemLogout = nvView.getMenu().findItem(R.id.nav_item_logout);
//        initBanner();
//        initFragment();
//        initBottomNavigationView();
//    }
//
//    private void initBanner() {
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        BannerFragment banner = new BannerFragment();
//        transaction.add(R.id.bannerView, banner);
//        transaction.commit();
//    }
//
//    private void initFragment() {
//        mFragmentList = new ArrayList<Fragment>();
//        mFragmentList.add(new HomeFragment());
//        mFragmentList.add(new ParentTargetFragment());
//        mFragmentList.add(new BahaverFragment());
//        mFragmentList.add(new ParentTargetFragment());
//
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        if (item == mItemWelfare) {
//            startActivity(new Intent(this, H5MainActivity.class));
//            closeDrawer();
//        } else if (item == mItemVideo) {
//            Log.e("amtf", "我是视频");
//            closeDrawer();
//        } else if (item == mItemAboutUs) {
//            Log.e("amtf", "关于我们");
//            closeDrawer();
//        } else if (item == mItemLogout) {
//            Log.e("amtf", "退出登录");
//            closeDrawer();
//        }
//
//        return false;
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        switch (id) {
//            case R.id.img_logo:
//                iv.setImageResource(R.drawable.girl);
//                break;
//        }
//
//    }
//
//    /**
//     * 关闭侧滑
//     */
//    private void closeDrawer() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//    }
//
//    private void initBottomNavigationView() {
//        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
//        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
//        // 预设定进来后,默认显示fragment
//        addFragment(R.id.layout_pager, mFragmentList.get(0));
//        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                if (item.getItemId() == R.id.tab_home) {
//                    addFragment(R.id.layout_pager, mFragmentList.get(0));
//                } else if (item.getItemId() == R.id.tab_goods) {
//                    addFragment(R.id.layout_pager, mFragmentList.get(1));
//                } else if (item.getItemId() == R.id.tab_cart) {
//                    addFragment(R.id.layout_pager, mFragmentList.get(2));
//                } else if (item.getItemId() == R.id.tab_self) {
//                    addFragment(R.id.layout_pager, mFragmentList.get(3));
//                }
//                return true;
//            }
//        });
//
//    }
//
//
//    /**
//     * 显示fragment
//     *
//     * @param frameLayoutId
//     * @param fragment
//     */
//    private void addFragment(int frameLayoutId, Fragment fragment) {
//        if (fragment != null) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            if (fragment.isAdded()) {
//                if (mCurrentFragment != null) {
//                    transaction.hide(mCurrentFragment).show(fragment);
//                } else {
//                    transaction.show(fragment);
//                }
//            } else {
//                if (mCurrentFragment != null) {
//                    transaction.hide(mCurrentFragment).add(frameLayoutId, fragment);
//                } else {
//                    transaction.add(frameLayoutId, fragment);
//                }
//            }
//            mCurrentFragment = fragment;
//            transaction.commit();
//        }
//    }
//
//
////    @Override
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onXshellEvent(XshellEvent event) {
//        if (event.what == 0x11) {
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            if (drawer.isDrawerOpen(GravityCompat.START)) {
//                drawer.closeDrawer(GravityCompat.START);
//            } else {
//                drawer.openDrawer(GravityCompat.START);
//            }
//        }
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//}
