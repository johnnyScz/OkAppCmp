package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.ChanneltopicBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

/**
 * 帖子列表
 */
public class TopicAdapter extends BaseQuickAdapter<ChanneltopicBean, BaseViewHolder> {


    public TopicAdapter(int layoutResId, @Nullable List<ChanneltopicBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, ChanneltopicBean item) {

        helper.addOnClickListener(R.id.item);

        helper.setText(R.id.title, item.getTitle());

        String time = item.getCreatedtime();

        helper.setText(R.id.time_create, time.substring(0, time.length() - 3));


//        if (!MyTextUtil.isEmpty(item.getImg()) && item.getImg().contains("http")) {
//            ImageView hean = helper.getView(R.id.img);
//
//            Picasso.with(mContext).load(item.getImg()).error(R.drawable.icon_no_download).
//                    transform(new CircleCornerForm()).into(hean);
//
//        }

        ImageView head = helper.getView(R.id.head);

        Picasso.with(mContext).load(item.getCreateduserhead()).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(head);

        helper.setText(R.id.name, item.getCreateusername());

        helper.setText(R.id.comment_couont, checkIfNull(item.getCommentcount()));

        helper.setText(R.id.prise_couont, checkIfNull(item.getLikecount()));


    }


    public String checkIfNull(String come) {

        if (MyTextUtil.isEmpty(come)) {

            return "0";

        }
        return come;
    }


}
