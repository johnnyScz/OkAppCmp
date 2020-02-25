package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.FineListBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

public class TiaozhaFineAdapter extends BaseQuickAdapter<FineListBean, BaseViewHolder> {


    public TiaozhaFineAdapter(int layoutResId, @Nullable List<FineListBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, FineListBean item) {

        showHead(helper, item);

        if (item.getF_rate_type().equals("n")) {
            helper.setText(R.id.tv_state, "评分：未评定");
            helper.setText(R.id.money_status, "待评定：" + item.getUnassessed_money() + "元");
        } else {
            int scroe = item.getF_score();
            switch (scroe) {
                case 1://未完成
                    helper.setText(R.id.tv_state, "评分：未完成");
                    helper.setText(R.id.money_status, "瓜分：" + item.getF_sup_money());
                    break;

                case 2://基本完成
                    helper.setText(R.id.tv_state, "评分：基本完成");
                    helper.setText(R.id.money_status, "退回 ：" + item.getF_refund() + " ,  瓜分 ：" + item.getF_sup_money());

                    break;


                case 3://完成
                    helper.setText(R.id.tv_state, "评分：已完成");

                    helper.setText(R.id.money_status, "退回 ： " + item.getF_refund());

                    break;

            }
        }


    }

    /**
     * 显示 头部数据
     *
     * @param helper
     * @param item
     */
    private void showHead(BaseViewHolder helper, FineListBean item) {


        if (MyTextUtil.isEmpty(item.getF_create_time() + "") || item.getF_create_time() == 0) {

        } else {
            helper.setText(R.id.tv_time, DateUtil.longToDateMMss(item.getF_create_time()));
        }

        ImageView icon = helper.getView(R.id.iv_icon);
        if (item.getUser() != null) {
            helper.setText(R.id.tv_groupname, item.getUser().getNickname());
            Picasso.with(mContext).load(item.getUser().getHead())
                    .error(R.drawable.icon_no_download).transform(new CircleCornerForm()).into(icon);
        }
        if (item.getF_rate_type().equals("sys")) {
            helper.setText(R.id.tv_groupname, "系统评定");
            icon.setImageResource(R.mipmap.ic_launcher);
        }


    }


}
