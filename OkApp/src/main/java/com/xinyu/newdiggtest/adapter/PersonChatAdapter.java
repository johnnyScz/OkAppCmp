package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;

import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.RoomPersonListBean;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.widget.MarkedImageView;

import java.util.List;

public class PersonChatAdapter extends BaseQuickAdapter<RoomPersonListBean, BaseViewHolder> {


    public PersonChatAdapter(int layoutResId, @Nullable List<RoomPersonListBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, RoomPersonListBean item) {
        helper.addOnClickListener(R.id.rl_item).setText(R.id.tv_groupname, item.getRoom_name());
        MarkedImageView logoview = helper.getView(R.id.iv_icon);

        String msgCount = MyTextUtil.isEmpty(item.getDetail().getCount()) ? "0" : item.getDetail().getCount();

        logoview.setMessageNumber(Integer.parseInt(msgCount));

        if (!MyTextUtil.isEmpty(item.getLatstMsg())) {
            helper.setText(R.id.tv_latestmsg, MyTextUtil.getDecodeStr(AppUtils.convertMsg(item.getLatstMsg())));
        }
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.icon_no_download);
        Picasso.with(mContext).load(item.getRoom_head()).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(logoview);

    }


}
