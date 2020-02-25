package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.FormItemBean;

import java.util.List;

/**
 * 电影列表
 */
public class FormInfoAdapter extends BaseQuickAdapter<FormItemBean, BaseViewHolder> {


    int formType = 0;

    public void setFormType(int type) {

        this.formType = type;
    }


    public FormInfoAdapter(int layoutResId, @Nullable List<FormItemBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, FormItemBean item) {

        helper.addOnClickListener(R.id.item);
        helper.setText(R.id.stander, item.getOwnermap().getNickname());

        helper.setText(R.id.pro_name, item.getF_product_name());

        FormItemBean.DirectormapBean data = null;

        if (formType == 1) {
            data = item.getCustomermap();
        } else {
            data = item.getDirectormap();
        }

        if (data != null) {

            helper.setText(R.id.pro_cmp, data.getF_organization_name());

            helper.setText(R.id.contact_person, data.getF_name());
        }

        if (formType == 1) {
            helper.setText(R.id.pre_money, item.getF_budget());
        } else {
            helper.setText(R.id.pre_money, item.getF_intent_amount());
        }

        helper.setText(R.id.sign_state, checkState(item.getF_state()));

        helper.setText(R.id.create_date, item.getF_create_date().substring(0, 10));


    }

    private String checkState(String f_state) {

        switch (f_state) {
            case "1":

                return "销售机会";

            case "2":
                return "意向签约";

            case "3":
                return "已签约";

            case "4":
                return "丢单";
        }


        return "";
    }


}
