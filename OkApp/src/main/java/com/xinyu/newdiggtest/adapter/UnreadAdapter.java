package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.UnreadMsgListBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

public class UnreadAdapter extends BaseQuickAdapter<UnreadMsgListBean.MsgUnreadBean, BaseViewHolder> {

    public UnreadAdapter(int layoutResId, @Nullable List<UnreadMsgListBean.MsgUnreadBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UnreadMsgListBean.MsgUnreadBean item) {

        helper.addOnClickListener(R.id.item);


        String head = item.getF_create_by_info().getHead();

        ImageView headView = helper.getView(R.id.post_film);
        Picasso.with(mContext).load(head).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(headView);


        String name = item.getF_create_by_info().getNickname();

        String title = item.getF_title();

        String msg = item.getF_msg();
        helper.setText(R.id.title, title);
        helper.setText(R.id.msg_sub, name + " : " + msg);


        String time = item.getF_create_date();

        if (MyTextUtil.isEmpty(time) || time.length() < 9)
            return;

        helper.setText(R.id.time_stap, time.substring(time.length() - 8, time.length() - 3));


    }
}
