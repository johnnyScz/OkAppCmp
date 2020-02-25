package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.PersonContactBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;

import java.util.List;

/**
 * 电影列表
 */
public class FriendAdapter extends BaseQuickAdapter<PersonContactBean.FriendBean, BaseViewHolder> {


    public FriendAdapter(int layoutResId, @Nullable List<PersonContactBean.FriendBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, PersonContactBean.FriendBean item) {

        helper.addOnClickListener(R.id.item);

        ImageView imageView = helper.getView(R.id.inco_person);

        Picasso.with(mContext).load(item.getHead()).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(imageView);

        helper.setText(R.id.item_name, item.getName());
    }


}
