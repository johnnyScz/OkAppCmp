package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.net.bean.CardChildBean;

import java.util.List;

/**
 * 名片信息
 */
public class CardPersonAdapter extends BaseQuickAdapter<CardChildBean, BaseViewHolder> {


    public CardPersonAdapter(int layoutResId, @Nullable List<CardChildBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, CardChildBean item) {

        helper.addOnClickListener(R.id.item);

        helper.setText(R.id.name, item.getF_name());

        helper.setText(R.id.mail, item.getF_email());


    }


}
