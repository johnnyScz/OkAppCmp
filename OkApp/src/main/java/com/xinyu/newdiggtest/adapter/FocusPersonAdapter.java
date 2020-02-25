package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.FocusPersonBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;

import java.util.List;

/**
 * 订单列表
 */
public class FocusPersonAdapter extends BaseQuickAdapter<FocusPersonBean.PersonBean, BaseViewHolder> {


    public FocusPersonAdapter(int layoutResId, @Nullable List<FocusPersonBean.PersonBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, FocusPersonBean.PersonBean item) {

        helper.setText(R.id.name, item.getNick_name());

        helper.addOnClickListener(R.id.iv_img);
        ImageView iv = helper.getView(R.id.iv_img);
        Picasso.with(mContext).load(item.getHead()).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(iv);


    }


}
