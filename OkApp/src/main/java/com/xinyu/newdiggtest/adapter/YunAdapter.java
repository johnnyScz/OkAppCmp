package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.YunBean;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

/**
 * 阿里云资源
 */
public class YunAdapter extends BaseQuickAdapter<YunBean, BaseViewHolder> {


    public YunAdapter(int layoutResId, @Nullable List<YunBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, YunBean item) {

        helper.addOnClickListener(R.id.item);

        if (item.getUserinfo() != null && !MyTextUtil.isEmpty(item.getUserinfo().getNickname())) {
            helper.setText(R.id.stander, item.getUserinfo().getNickname());
        }

        helper.setText(R.id.form_no, item.getF_id());

        helper.setText(R.id.creat_time, item.getF_create_time().substring(0, 16));


    }


}
