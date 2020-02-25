package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.viewhelper.MemberTestBean;

import java.util.List;


/**
 * 电影列表
 */
public class MtestChildAdapter extends BaseQuickAdapter<MemberTestBean, BaseViewHolder> {


    public MtestChildAdapter(int layoutResId, @Nullable List<MemberTestBean> data) {
        super(layoutResId, data);

    }


    @Override
    protected void convert(BaseViewHolder helper, MemberTestBean bean) {

        helper.setText(R.id.text, bean.getName());


    }
}
