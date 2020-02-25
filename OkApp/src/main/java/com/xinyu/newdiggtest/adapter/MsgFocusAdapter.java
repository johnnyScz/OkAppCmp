package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.CommonGroupBean;
import com.xinyu.newdiggtest.bean.CommonMutiCotentBean;
import com.xinyu.newdiggtest.bean.CommonUserBean;
import com.xinyu.newdiggtest.bean.XhintMsgBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.TimeUtil;
import com.xinyu.newdiggtest.widget.MarkedImageView;

import java.util.List;

/**
 * 与我相关的聊天
 */
public class MsgFocusAdapter extends BaseQuickAdapter<XhintMsgBean.MsgDataBean, BaseViewHolder> {


    public MsgFocusAdapter(int layoutResId, @Nullable List<XhintMsgBean.MsgDataBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, XhintMsgBean.MsgDataBean item) {

        helper.addOnClickListener(R.id.rl_item);

        MarkedImageView headView = helper.getView(R.id.iv_icon);

        CommonGroupBean group = item.getLatestmsg().getGroup();

        CommonUserBean user = item.getLatestmsg().getUser();


        if (group != null && !MyTextUtil.isEmpty(group.getRoom_name())) {

            helper.setText(R.id.tv_groupname, group.getRoom_name());

            String head = group.getRoom_head();

            Picasso.with(mContext).load(head).error(R.drawable.icon_no_download).
                    transform(new CircleCornerForm()).into(headView);

        } else {

            helper.setText(R.id.tv_groupname, user.getNickname());
            String head = user.getHead();
            Picasso.with(mContext).load(head).error(R.drawable.icon_no_download).
                    transform(new CircleCornerForm()).into(headView);

        }

        headView.setMessageNumber(Integer.parseInt(item.getCount()));

        Object obj = item.getLatestmsg().getContent();


        String content = "";
        if (obj instanceof String) {
            content = (String) obj;
        } else {
            String json = new Gson().toJson(obj);
            CommonMutiCotentBean bean = JSON.parseObject(json, CommonMutiCotentBean.class);
            if (bean != null) {
                content = bean.getFilename();
                if (MyTextUtil.isEmpty(content)) {
                    content = bean.getOriginal();
                }
            }
        }


        if (group == null || MyTextUtil.isEmpty(group.getRoom_name())) {
            helper.setText(R.id.tv_latestmsg, content);
        } else {
            helper.setText(R.id.tv_latestmsg, user.getNickname() + " : " + content);
        }


        String time = item.getLatesttime();

        helper.setText(R.id.msg_time, TimeUtil.getChatTimeStr(Long.parseLong(time)));

    }


}
