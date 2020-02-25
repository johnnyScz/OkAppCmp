package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.WxUserBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import java.util.List;

/**
 * 电影列表
 */
public class CatactSearchAdapter extends BaseQuickAdapter<WxUserBean, BaseViewHolder> {


    public CatactSearchAdapter(int layoutResId, @Nullable List<WxUserBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, WxUserBean item) {

        helper.addOnClickListener(R.id.post_film);

        if (item.getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
            helper.setVisible(R.id.gochat, false);

        } else {
            helper.setVisible(R.id.gochat, true);
        }

        helper.addOnClickListener(R.id.gochat);

        helper.setText(R.id.film_name, item.getNickname());

        ImageView icon = helper.getView(R.id.post_film);

        if (MyTextUtil.isEmpty(item.getHead())) {
            icon.setImageResource(R.drawable.icon_no_download);
        } else {
            Picasso.with(mContext).load(item.getHead()).
                    transform(new CircleCornerForm()).into(icon);
        }


    }


}
