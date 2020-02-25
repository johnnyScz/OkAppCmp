package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.MileStoneBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.DateUtil;

import java.util.List;

/**
 * 电影列表
 */
public class LichBeiAdapter extends BaseQuickAdapter<MileStoneBean.StoneBean, BaseViewHolder> {


    public LichBeiAdapter(int layoutResId, @Nullable List<MileStoneBean.StoneBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final MileStoneBean.StoneBean item) {


        helper.addOnClickListener(R.id.item_stone);
        ImageView icon = helper.getView(R.id.iv_target_icon);

        if (item.getUserinfo() != null) {

            Picasso.with(mContext).load(item.getUserinfo().getHead()).error(R.drawable.icon_no_download).
                    transform(new CircleCornerForm()).into(icon);

            helper.setText(R.id.tv_name, item.getUserinfo().getNickname());
        }


        helper.setText(R.id.descp, item.getF_title());


        String date = DateUtil.getDotOnlyDay(item.getF_create_date()) + "-"
                + DateUtil.getDotOnlyDay(item.getF_finish_date());


        helper.setText(R.id.tv_date, date);


    }


}
