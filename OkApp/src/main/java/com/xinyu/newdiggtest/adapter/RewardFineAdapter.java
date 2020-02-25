package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.RewardListBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * 奖金金
 */
public class RewardFineAdapter extends BaseQuickAdapter<RewardListBean, BaseViewHolder> {


    public RewardFineAdapter(int layoutResId, @Nullable List<RewardListBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, RewardListBean item) {

        ImageView icon = helper.getView(R.id.iv_icon);

        helper.setText(R.id.tv_groupname, item.getNickname());

        if (!MyTextUtil.isEmpty(item.getHead())) {
            Picasso.with(mContext).load(item.getHead()).
                    transform(new CircleCornerForm())
                    .error(R.drawable.icon_no_download).into(icon);
        }

        helper.setText(R.id.tv_money_count, "¥" + item.getF_money());

        if (!MyTextUtil.isEmpty(item.getF_wish())) {
            try {
                String wish = URLDecoder.decode(item.getF_wish(), "UTF-8");
                helper.setText(R.id.tv_state, wish);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        helper.setText(R.id.tv_time, DateUtil.longToDateMMss(item.getF_create_time()));

    }


}
