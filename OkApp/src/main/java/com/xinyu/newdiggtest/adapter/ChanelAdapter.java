package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.ChanelBean;
import com.xinyu.newdiggtest.bean.TodoUserBean;
import com.xinyu.newdiggtest.utils.CircleTransform;

import java.util.List;

/**
 * 电影列表
 */
public class ChanelAdapter extends BaseQuickAdapter<ChanelBean.ChanelChilcBean, BaseViewHolder> {


    public ChanelAdapter(int layoutResId, @Nullable List<ChanelBean.ChanelChilcBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final ChanelBean.ChanelChilcBean item) {

        helper.addOnClickListener(R.id.chanel);

        helper.setText(R.id.chanel_name, item.getChannelname());

        helper.setText(R.id.title, "「" + item.getDescription() + "」");

        helper.setText(R.id.descp, "来自" + "\"" + item.getGroupname() + "\"" + "的群");


        if (item.getCreate_user_info() != null) {

            TodoUserBean user = item.getCreate_user_info();

            helper.setText(R.id.name, user.getNickname());

            String headUrl = user.getHead();

            ImageView hean = helper.getView(R.id.icon);

            Picasso.with(mContext).load(headUrl).error(R.drawable.icon_no_download).
                    transform(new CircleTransform()).into(hean);


        }

        int count = item.getMsgCount();

        if (count < 1) {
            helper.getView(R.id.msg_count).setVisibility(View.GONE);

        } else {
            helper.getView(R.id.msg_count).setVisibility(View.VISIBLE);
            helper.setText(R.id.msg_count, count + "");
        }


    }


}
