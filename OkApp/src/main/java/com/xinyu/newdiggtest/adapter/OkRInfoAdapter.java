package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.GroupInfoBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;

import java.util.List;

public class OkRInfoAdapter extends BaseQuickAdapter<GroupInfoBean, BaseViewHolder> {


    public OkRInfoAdapter(int layoutResId, @Nullable List<GroupInfoBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, GroupInfoBean item) {

        ImageView head = helper.getView(R.id.img);


        Picasso.with(mContext).load(item.getHead()).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(head);


        helper.setText(R.id.nick_name, item.getOwner_name());


    }


}
