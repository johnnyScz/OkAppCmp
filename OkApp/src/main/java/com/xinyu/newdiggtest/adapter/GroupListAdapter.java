package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;


import java.util.List;

public class GroupListAdapter extends BaseQuickAdapter<RoomMemberBean, BaseViewHolder> {


    public GroupListAdapter(int layoutResId, @Nullable List<RoomMemberBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, RoomMemberBean item) {
        helper.addOnClickListener(R.id.rl_item).setText(R.id.tv_groupname, item.getUser().getName());
        ImageView logoview = helper.getView(R.id.iv_icon);
//            Glide.with(mContext).load(item.getHeadUrl()).
//                    transform(new GlideCircleTransform(mContext)).placeholder(R.drawable.item_group).into(logoview);
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.icon_no_download);

        if (item.getMember_type().equals("N")) {
            Glide.with(mContext).load(item.getUser().getHead()).apply(options).into(logoview);
        } else {
            logoview.setImageResource(R.drawable.icon_no_download);
        }


    }
}
