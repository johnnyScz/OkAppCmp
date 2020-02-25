package com.xinyu.newdiggtest.ui.Digg.fragment;


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
 * 懒加载Fragment，其子类都具有懒加载的能力
 */
public abstract class BaseFragment extends Fragment {


    protected boolean viewCreated = false; //视图是否已经加载过了
    protected boolean isVisble = false;
    protected View rootView;
    protected Activity mContext;

    protected LoadingDailog dialog;

    private Unbinder mUnbinder;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getResId(), null);
        }
        mUnbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }


    protected abstract void initView();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisble = isVisibleToUser;

        if (isVisble && rootView != null) {
            loadData();
        }
        check2LoadData();
    }


    /**
     * 供子类使用，子类fragment初始化操作，此函数内部真正开始进行页面的一些列操作
     */
    protected abstract void loadData();//加载数据

    protected abstract void reLoadData();//重新装载数据

    protected abstract int getResId();//获取布局Id

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = new DialogUtil(mContext).buildDialog("加载中...");
        viewCreated = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewCreated = false;
        mUnbinder.unbind();
    }


    /**
     * 空实现
     */
    private void check2LoadData() {


    }

}
