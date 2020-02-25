package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.RetListBean;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

public class ChaosongAdapter extends BaseQuickAdapter<RetListBean.InvitesBean, BaseViewHolder> {


    public ChaosongAdapter(int layoutResId, @Nullable List<RetListBean.InvitesBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final RetListBean.InvitesBean bean) {

        String name = MyTextUtil.isEmpty(bean.getOwnermap().getNickname()) ? bean.getOwnermap().getName() : bean.getOwnermap().getNickname();

        helper.setText(R.id.name_person, name);

    }


}
