package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

public class VipRecomendAdapter extends BaseQuickAdapter<VipRecomendBean, BaseViewHolder> {


    public VipRecomendAdapter(int layoutResId, @Nullable List<VipRecomendBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, VipRecomendBean item) {

        ImageView inco = helper.getView(R.id.iv_img);

        if (MyTextUtil.isEmpty(item.getF_head())) {
            inco.setImageResource(R.drawable.icon_no_download);
        } else {
            Picasso.with(mContext)
                    .load(item.getF_head())
                    .transform(new CircleCornerForm())
                    .error(R.drawable.icon_no_download)
                    .into(inco);
        }

        helper.setText(R.id.tv_name, item.getNickname());

        helper.setText(R.id.tv_members, item.getF_first_rec_users() + "人");


        ImageView chapion = helper.getView(R.id.vip_time);

        if (MyTextUtil.isEmpty(item.getF_recharge_vip_date())) {
            chapion.setImageResource(R.mipmap.viplist_hat_no);
        } else {
            chapion.setImageResource(R.mipmap.viplist_hat_yes);
        }


        String area = "";

        if (!MyTextUtil.isEmpty(item.getF_province()) && !MyTextUtil.isEmpty(item.getF_city())) {
            area = item.getF_province() + " " + item.getF_city();
        }
        helper.setText(R.id.tv_state, area);

//        TextView state = helper.getView(R.id.tv_state);
//        ImageView chipion = helper.getView(R.id.iv_bottn);
//
//        showSwithState(state, chipion, item);

    }


}
