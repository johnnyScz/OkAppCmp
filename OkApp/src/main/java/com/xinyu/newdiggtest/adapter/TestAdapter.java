package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 测试
 */
public class TestAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public TestAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {


    }


}
