package com.xinyu.newdiggtest.ui.Digg.target;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.FocusPersonFragment;
import com.xinyu.newdiggtest.ui.Digg.fragment.GroupTargetFragment;
import com.xinyu.newdiggtest.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人空间--我的关注
 */
public class MySelfFocusTargetActivity extends BaseNoEventActivity {

    @BindView(R.id.target_tab)
    public TabLayout tabLayout;

    @BindView(R.id.target_pager)
    public ViewPager viewPager;

    private String[] titles = {"关注的人", "关注的目标"};

    private List<Fragment> fragmentList = new ArrayList<>();

    int curent = 0;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_self_focus_target;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String tag = getIntent().getStringExtra("current");
        curent = Integer.parseInt(tag);

        initView();
    }

    private void initView() {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                AppUtils.setIndicator(tabLayout, 20, 20);
            }
        });
        FocusPersonFragment person = new FocusPersonFragment();
        fragmentList.add(person);

        GroupTargetFragment selfTarget = GroupTargetFragment.newInstance("3");
        fragmentList.add(selfTarget);

        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(curent);
        //绑定
        tabLayout.setupWithViewPager(viewPager);

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


    @OnClick(R.id.iv_back)
    public void goCommit() {
        finish();
    }


}




