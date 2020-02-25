package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class FormAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {


    public FormAdapter(int layoutResId, @Nullable List<Object> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final Object bean) {


    }


}
