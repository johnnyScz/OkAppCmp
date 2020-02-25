package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.CommentReturnBean;
import com.xinyu.newdiggtest.utils.CircleTransform;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

/**
 * 评论列表
 */
public class CommentAdapter extends BaseQuickAdapter<CommentReturnBean.CommentBean, BaseViewHolder> {


    public CommentAdapter(int layoutResId, @Nullable List<CommentReturnBean.CommentBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, CommentReturnBean.CommentBean item) {

        ImageView imageView = helper.getView(R.id.iv_icon);


        Picasso.with(mContext).load(item.getUserinfo().getHead()).error(R.drawable.icon_no_download).
                transform(new CircleTransform()).into(imageView);

        helper.setText(R.id.tv_pname, item.getUserinfo().getNickname());

        helper.setText(R.id.msg_time, MyTextUtil.replaeT(item.getF_create_date()));

        helper.setText(R.id.content, item.getF_comment());


    }


}
