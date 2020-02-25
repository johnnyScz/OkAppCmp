package com.xinyu.newdiggtest.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.ViewPagerFriendAdapter;
import com.xinyu.newdiggtest.ui.Digg.CreatGroupActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.GroupNewFragment;
import com.xinyu.newdiggtest.ui.Digg.fragment.PersonChatFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class FriendFragment extends LazySingleFragment {


    Fragment frag1, frag2;
    Context mctx;

    @BindView(R.id.tab)
    TabLayout tab;

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.iv_add)
    ImageView more;


    List<Fragment> list;
    ViewPagerFriendAdapter adapter;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_friend;
    }


    public void setTagSwict(int tag) {
        viewpager.setCurrentItem(tag);
    }


    @Override
    protected void initView() {
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mctx.startActivity(new Intent(mctx, CreatGroupActivity.class));
            }
        });

    }

    @Override
    public void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            if (list == null || list.size() < 1) {
                list = new ArrayList<>();

                frag1 = new GroupNewFragment();

                frag2 = new PersonChatFragment();

                list.add(frag1);
                list.add(frag2);
                adapter = new ViewPagerFriendAdapter(getChildFragmentManager());
                adapter.setdata(list);
                tab.setupWithViewPager(viewpager);
                viewpager.setAdapter(adapter);
            }
        } else {
            //从可见变成不可见

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mctx = context;
    }


}
