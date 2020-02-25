package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerFriendAdapter extends FragmentPagerAdapter {
    List<Fragment> list;

    /**
     * @param fm
     * @deprecated
     */
    public ViewPagerFriendAdapter(FragmentManager fm) {
        super(fm);
        list = new ArrayList<>();
    }

    public void setdata(List<Fragment> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "OKR";
            case 1:
                return "事务群组";
        }
        return null;
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

