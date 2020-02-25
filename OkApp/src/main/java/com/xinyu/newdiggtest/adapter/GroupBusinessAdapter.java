package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.AfairBean;
import com.xinyu.newdiggtest.bean.MsgNewBean;
import com.xinyu.newdiggtest.bean.PushBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.TimeUtil;
import com.xinyu.newdiggtest.widget.MarkedImageView;

import java.util.List;

public class GroupBusinessAdapter extends BaseQuickAdapter<AfairBean.AfairChildBean, BaseViewHolder> {


    public GroupBusinessAdapter(int layoutResId, @Nullable List<AfairBean.AfairChildBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, AfairBean.AfairChildBean item) {

        helper.addOnClickListener(R.id.rl_item);

        AfairBean.AfairChildBean.ChatInfoBean info = item.getChat_info();

        MarkedImageView room_head = helper.getView(R.id.iv_icon);

        Picasso.with(mContext).load(info.getRoom_head()).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(room_head);


        MsgNewBean msg = item.getMsg();

        if (msg != null) {
            String count = MyTextUtil.isEmpty(msg.getCount()) ? "0" : msg.getCount();

            room_head.setMessageNumber(Integer.parseInt(count));
        }

        helper.setText(R.id.tv_groupname, info.getRoom_name());

        if (item.getGroup_operation_record() != null) {

            PushBean push = item.getGroup_operation_record();

            if (!MyTextUtil.isEmpty(push.getMsg())) {
                helper.setText(R.id.tv_latestmsg, push.getMsg());
            } else {
                helper.setText(R.id.tv_latestmsg, "");
            }

            String time = TimeUtil.getChatTimeStr(DateUtil.convertSecond(push.getMsg_time()));
            helper.setText(R.id.msg_time, time);

        } else {
            helper.setText(R.id.tv_latestmsg, "");
        }


    }


}
