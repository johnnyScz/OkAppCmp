package com.xinyu.newdiggtest.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.ViewPagerTwoAdapter;
import com.xinyu.newdiggtest.ui.Digg.TargetGroupActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.TargetFragment;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;


public class ParentTargetFragment1 extends Fragment {


    TargetFragment frag1, frag2;
    Context mctx;

    TabLayout tab;
    ViewPager viewpager;
    List<Fragment> list;
    ViewPagerTwoAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_page, null);
        initView(view);
        initChebox(view);
        return view;
    }

    private void initChebox(View view) {

    }


    private void initView(View view) {
        tab = view.findViewById(R.id.tab);
        viewpager = view.findViewById(R.id.viewpager);

        tab.post(new Runnable() {
            @Override
            public void run() {
                AppUtils.setIndicator(tab, 20, 20);
            }
        });

        view.findViewById(R.id.iv_addtarget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAddTargetActy();
            }
        });


    }

    private void goAddTargetActy() {
        mctx.startActivity(new Intent(mctx, TargetGroupActivity.class));
    }


    @Override
    public void onResume() {
        super.onResume();

        list = new ArrayList<>();

        frag1 = TargetFragment.newInstance("0", PreferenceUtil.getInstance(mctx).getUserId(), "0");
        frag2 = TargetFragment.newInstance("1", PreferenceUtil.getInstance(mctx).getUserId(), "0");


        list.add(frag1);
        list.add(frag2);
        adapter = new ViewPagerTwoAdapter(getChildFragmentManager());
        adapter.setdata(list);
        tab.setupWithViewPager(viewpager);
        viewpager.setAdapter(adapter);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mctx = context;
    }


}
