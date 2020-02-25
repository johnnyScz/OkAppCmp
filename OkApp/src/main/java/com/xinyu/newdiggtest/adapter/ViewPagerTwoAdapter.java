package com.xinyu.newdiggtest.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerTwoAdapter extends FragmentPagerAdapter {
    List<Fragment> list;

    /**
     * @param fm
     * @deprecated
     */
    public ViewPagerTwoAdapter(FragmentManager fm) {
        super(fm);
        list = new ArrayList<>();
    }

    public void setdata(List<Fragment> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    /**
     * @param i
     * @deprecated
     */
    @Override
    public Fragment getItem(int i) {
        Fragment fragment = list.get(i);

        return fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}

