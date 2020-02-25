package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.CheckFinshReturnBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;

import java.util.List;

/**
 * 检查目标完成进度
 */
public class CheckFinishAdapter extends BaseQuickAdapter<CheckFinshReturnBean.CheckFinshListBean, BaseViewHolder> {


    public CheckFinishAdapter(int layoutResId, @Nullable List<CheckFinshReturnBean.CheckFinshListBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, CheckFinshReturnBean.CheckFinshListBean item) {

        ImageView head = helper.getView(R.id.person_head);

        ImageView state = helper.getView(R.id.finish_statu);

        helper.setText(R.id.person_name, item.getNickname());
        helper.setText(R.id.reward_money, item.getF_money() + "元");


        Picasso.with(mContext).load(item.getHead()).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(head);

        switch (item.getF_score()) {

//            case "1":
//                state.setImageResource(R.mipmap.okno);
//                break;
//
//            case "2":
//                state.setImageResource(R.mipmap.ok60);
//                break;
//
//            case "3":
//                state.setImageResource(R.mipmap.ok100);
//                break;


        }


    }


}
