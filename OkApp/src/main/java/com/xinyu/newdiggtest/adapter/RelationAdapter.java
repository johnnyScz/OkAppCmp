package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import java.util.List;

/**
 * 订单列表
 */
public class RelationAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {


    public RelationAdapter(int layoutResId, @Nullable List<Object> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {


    }


}
