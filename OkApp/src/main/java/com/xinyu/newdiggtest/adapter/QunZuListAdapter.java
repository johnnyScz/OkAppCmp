package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.RoomListBean;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.widget.MarkedImageView;

import java.util.List;

public class QunZuListAdapter extends BaseQuickAdapter<RoomListBean, BaseViewHolder> {


    public QunZuListAdapter(int layoutResId, @Nullable List<RoomListBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, RoomListBean item) {
        helper.addOnClickListener(R.id.rl_item).setText(R.id.tv_groupname, MyTextUtil.getDecodeStr(item.getRoom_name()) +
                "(" + item.getRoom_member_number() + ")");
        MarkedImageView logoview = helper.getView(R.id.iv_icon);

        if (item.getDetail() != null) {
            String msgCount = item.getDetail().getCount();
            logoview.setMessageNumber(Integer.parseInt(msgCount));

            Object obj = item.getDetail().getLatestmsg();
            String msgStr = "";
            try {
                msgStr = new Gson().toJson(obj);
            } catch (Exception e) {
                msgStr = (String) obj;
                e.printStackTrace();
            }


            if (!MyTextUtil.isEmpty(msgStr) && Integer.parseInt(msgCount) > 0) {

                String decStr = MyTextUtil.getDecodeStr(msgStr);

                helper.setText(R.id.tv_latestmsg, MyTextUtil.getTargtStr((AppUtils.getTypeStr(decStr))));

            }
        }
        Picasso.with(mContext).load(item.getRoom_head()).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(logoview);

    }


}
