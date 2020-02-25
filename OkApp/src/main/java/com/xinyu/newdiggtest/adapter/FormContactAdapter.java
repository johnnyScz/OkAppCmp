package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.FormItemBean;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

/**
 * 合同
 */
public class FormContactAdapter extends BaseQuickAdapter<FormItemBean, BaseViewHolder> {


    public FormContactAdapter(int layoutResId, @Nullable List<FormItemBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, FormItemBean item) {

        helper.addOnClickListener(R.id.item);
        helper.setText(R.id.stander, item.getOwnermap().getNickname());

        helper.setText(R.id.pro_name, item.getF_product_name());
        helper.setText(R.id.pre_money, item.getF_contract_amount());

        helper.setText(R.id.contact_no, item.getF_contract_no());

        helper.setText(R.id.sign_time, item.getF_create_date().substring(0, 10));

        helper.setText(R.id.create_date, item.getF_create_date().substring(0, 10));

        if (item.getDirectormap() == null || MyTextUtil.isEmpty(item.getDirectormap().getF_organization_name()))
            return;

        helper.setText(R.id.pro_cmp, item.getDirectormap().getF_organization_name());

        helper.setText(R.id.contact_person, item.getDirectormap().getF_name());


    }


}
