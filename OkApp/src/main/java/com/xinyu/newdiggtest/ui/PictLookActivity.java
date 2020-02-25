package com.xinyu.newdiggtest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.xinyu.newdiggtest.R;

import java.util.ArrayList;
import java.util.List;

public class PictLookActivity extends FragmentActivity {

    ViewPager pager;

    FragAdapter madapter;

    List<String> urlList;

    String currentIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pict_fragment);
        pager = this.findViewById(R.id.imagePager);

        initFragment();


    }

    private void initFragment() {
        urlList = getIntent().getStringArrayListExtra("urlList");
        currentIndex = getIntent().getStringExtra("currentIndex");
        List<Fragment> mFragments = new ArrayList<>();
        if (urlList != null && urlList.size() > 0) {
            int len = urlList.size();
            for (int i = 0; i < len; i++) {
                PictFragment frage = new PictFragment().getInstance(urlList.get(i));
                frage.setUrl(urlList.get(i));
                frage.setTip((i + 1) + "/" + len);
                mFragments.add(frage);
            }
        }
        madapter = new FragAdapter(getSupportFragmentManager(), mFragments);
        pager.setAdapter(madapter);
        pager.setCurrentItem(Integer.parseInt(currentIndex));

    }

    public class FragAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        public FragAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            return mFragments.get(arg0);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mFragments.size();
        }

    }


}
