package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.DakaSelectBean;
import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

public class MyDakaSelectAdapter extends BaseQuickAdapter<DakaSelectBean.ShowBean, BaseViewHolder> {


    public MyDakaSelectAdapter(int layoutResId, @Nullable List<DakaSelectBean.ShowBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, DakaSelectBean.ShowBean item) {
        helper.addOnClickListener(R.id.item);
        helper.setText(R.id.tv_target_name, MyTextUtil.getDecodeStr(item.getTarget_name()));

        if (item.getTarget_uuid().equals(AppContacts.SELECT_Tag)) {
            helper.setVisible(R.id.img_select, true);
        } else {
            helper.setVisible(R.id.img_select, false);
        }

    }
}
