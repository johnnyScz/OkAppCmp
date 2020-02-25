package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;

import com.xinyu.newdiggtest.bean.MomBean;


import java.util.List;

/**
 * 合同
 */
public class MomAdapter extends BaseQuickAdapter<MomBean.MomChildBean, BaseViewHolder> {


    public MomAdapter(int layoutResId, @Nullable List<MomBean.MomChildBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, MomBean.MomChildBean item) {

        helper.addOnClickListener(R.id.item);


        helper.setText(R.id.name, item.getF_field1());

        helper.setText(R.id.custom_type, item.getF_field2());

        helper.setText(R.id.rigest_date, item.getF_field3());

        helper.setText(R.id.contact_phone, item.getF_field4());

        helper.setText(R.id.e_money, item.getF_field5());

        helper.setText(R.id.remark, item.getF_field6());

    }


}
