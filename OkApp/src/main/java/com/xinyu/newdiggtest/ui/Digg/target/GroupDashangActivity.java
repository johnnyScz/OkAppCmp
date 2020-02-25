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
import com.xinyu.newdiggtest.ui.Digg.fragment.GroupTargetFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GroupDashangActivity extends BaseNoEventActivity {

    @BindView(R.id.target_tab)
    public TabLayout tabLayout;

    @BindView(R.id.target_pager)
    public ViewPager viewPager;

    private String[] titles = {"进行中", "已结束"};

    private List<Fragment> fragmentList = new ArrayList<>();


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_group_dashang;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

        GroupTargetFragment current = GroupTargetFragment.newInstance("0");
        fragmentList.add(current);

        GroupTargetFragment history = GroupTargetFragment.newInstance("1");
        fragmentList.add(history);

        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
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




