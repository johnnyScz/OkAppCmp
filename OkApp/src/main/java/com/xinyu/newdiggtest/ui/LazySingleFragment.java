package com.xinyu.newdiggtest.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.tu.loadingdialog.LoadingDailog;
import com.xinyu.newdiggtest.utils.DialogUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * ViewPager 配套的懒加载的Fragment
 */


public abstract class LazySingleFragment extends Fragment {
    protected View rootView;

    private Unbinder mUnbinder;
    //当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发
    private boolean isFragmentVisible;
    //是否是第一次开启网络加载
    public boolean isFirst;

    protected Activity mContext;

    protected LoadingDailog dialog;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(getLayoutResource(), container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        initView();
        dialog = new DialogUtil(mContext).buildDialog("加载中...");
        //可见，但是并没有加载过

        return rootView;
    }

    //获取布局文件
    protected abstract int getLayoutResource();


    //初始化view
    protected abstract void initView();


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisible = isVisibleToUser;

        if (rootView == null) {
            return;
        }
        //可见，并且没有加载过
        if (!isFirst && isFragmentVisible) {
            isFirst = true;
            onFragmentVisibleChange(true);

            return;
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }


    public abstract void onFragmentVisibleChange(boolean isVisible);


}
