package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.TargetBean;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

public class MyTargetAdapter extends BaseQuickAdapter<TargetBean, BaseViewHolder> {


    private String TAG_NO = "0";


    public void setTag(String tag) {
        this.TAG_NO = tag;
    }


    public MyTargetAdapter(int layoutResId, @Nullable List<TargetBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, TargetBean item) {

        ImageView imageView = helper.getView(R.id.iv_target_icon);

        helper.addOnClickListener(R.id.rl_item1);

        TextView nameContent = helper.getView(R.id.tv_type);
        String name = item.getName();

        String nameStr = MyTextUtil.getDecodeStr(name);
        if (nameStr.length() > 6) {
            nameContent.setText(nameStr + "\n" + getDotDay(item));
        } else {
            nameContent.setText(nameStr + " " + getDotDay(item));
        }


        helper.setText(R.id.tv_left_days, item.getCompletion_degree());
        helper.setText(R.id.dianzan, "点赞 : " + item.getLike_count() + "")
                .setText(R.id.comment, "评论 : " + item.getComment_count() + "");


        View view = helper.getView(R.id.target_rl);


        if (!MyTextUtil.isEmpty(item.getClass_id())) {

            String clazzId = "";

            if (item.getClass_id().contains("http://")) {
                clazzId = item.getClass_id();
//                Picasso.with(mContext).load(item.getClass_id()).error(R.mipmap.icon_zidingyi).into(imageView);
            } else {
                clazzId = item.getClass_id().equals("icon_zidingyi1") ? "icon_zidingyi" : item.getClass_id();
                int drawableId = mContext.getResources().getIdentifier(clazzId, "mipmap", mContext.getPackageName());
                imageView.setImageResource(drawableId);
            }

            if (clazzId.contains("yundong")) {
                view.setBackgroundResource(R.mipmap.yundong_bg);
            } else if (clazzId.contains("xuexi")) {
                view.setBackgroundResource(R.mipmap.study_bg);
            } else if (clazzId.contains("jiankang")) {
                view.setBackgroundResource(R.mipmap.health_bg);
            } else if (clazzId.contains("qinggan")) {
                view.setBackgroundResource(R.mipmap.qinggan_bg);
            } else {
                view.setBackgroundResource(R.mipmap.icon_zdy);
            }


        } else {
//            imageView.setImageResource(R.mipmap.icon_zidingyi);
            view.setBackgroundResource(R.mipmap.icon_zdy);
        }

        if (item.getState().equals("1") || TAG_NO.equals("1")) {
            view.setBackgroundResource(R.drawable.shaper_target_gray);
        }

        if (!MyTextUtil.isEmpty(item.getReward())) {
            helper.setText(R.id.dashang, "奖励 : " + item.getReward());
        } else {
            helper.setText(R.id.dashang, "奖励 : 0");
        }

        if (!MyTextUtil.isEmpty(item.getFollow_count())) {
            helper.setText(R.id.focus, "关注 : " + item.getFollow_count());
        } else {
            helper.setText(R.id.focus, "关注 : 0");
        }


    }

    private String getDotDay(TargetBean item) {

        if (MyTextUtil.isEmpty(item.getStart_date()) || MyTextUtil.isEmpty(item.getEnd_date())) {
            return "";
        }
        return DateUtil.timeOnlyDot(item.getStart_date()) + "-" + DateUtil.timeOnlyDot(item.getEnd_date());
    }


}
