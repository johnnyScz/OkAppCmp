package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.ChildTargetBean;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

/**
 * 电影列表
 */
public class ChildTargetAdapter extends BaseQuickAdapter<ChildTargetBean, BaseViewHolder> {


    public ChildTargetAdapter(int layoutResId, @Nullable List<ChildTargetBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, ChildTargetBean item) {
        helper.setText(R.id.child_name, MyTextUtil.getDecodeStr(item.getF_name()));
        helper.setText(R.id.finish_rate, item.getCompletion_degree());
        helper.addOnClickListener(R.id.item_child);

    }


}
