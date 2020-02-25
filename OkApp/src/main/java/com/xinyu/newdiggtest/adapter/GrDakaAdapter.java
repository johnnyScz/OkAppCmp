package com.xinyu.newdiggtest.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.TargetBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;


public class GrDakaAdapter extends BaseMultiItemQuickAdapter<TargetBean, BaseViewHolder> {

    Activity context;

    static final int Normal_ITEM = 1;

    static final int Head_ITEM = 2;

    private String TAG_NO = "0";//进行中/结束

    public void setTag(String tag) {
        this.TAG_NO = tag;
    }


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public GrDakaAdapter(List<TargetBean> data, Activity mctx) {
        super(data);
        this.context = mctx;
        addItemType(Normal_ITEM, R.layout.item_target);
        addItemType(Head_ITEM, R.layout.item_daka_selection1);

    }

    @Override
    protected void convert(BaseViewHolder helper, final TargetBean item) {
        switch (helper.getItemViewType()) {
            case Normal_ITEM:

                ImageView imageView = helper.getView(R.id.iv_target_icon);
                helper.addOnClickListener(R.id.rl_item1);
                TextView nameContent = helper.getView(R.id.tv_type);

                String name = MyTextUtil.getDecodeStr(item.getName());
                if (name.length() > 6) {
                    nameContent.setText(name + "\n" + getDotDay(item));
                } else {
                    nameContent.setText(name + " " + getDotDay(item));
                }

                helper.setText(R.id.tv_left_days, item.getCompletion_degree());
                helper.setText(R.id.dianzan, "点赞 : " + item.getLike_count() + "")
                        .setText(R.id.comment, "评论 : " + item.getComment_count() + "");


                String myMoney = TextUtils.isEmpty(item.getReward()) ? "奖励：0" : "奖励：" + item.getReward();
                helper.setText(R.id.dashang, myMoney);


                View view = helper.getView(R.id.target_rl);

                if (TAG_NO.equals("1")) {
                    view.setBackgroundResource(R.drawable.shaper_target_gray);
                } else {
                    showSwichBg(view, item);

                }

                if (!MyTextUtil.isEmpty(item.getClass_id())) {
                    Picasso.with(mContext).load(item.getClass_id()).error(R.drawable.icon_no_download).into(imageView);
                }

                if (!MyTextUtil.isEmpty(item.getFollow_count())) {
                    helper.setText(R.id.focus, "关注 : " + item.getFollow_count());
                } else {
                    helper.setText(R.id.focus, "关注 : 0");
                }


                break;

            case Head_ITEM:
                if (item.getUser() == null)
                    return;

                helper.addOnClickListener(R.id.iv_head);
                helper.setText(R.id.tv_name, item.getUser().getNickname());
                ImageView img = helper.getView(R.id.iv_head);
                String headUrl = MyTextUtil.isEmpty(item.getUser().getHead()) ? item.getUser().getCustom_head() : item.getUser().getHead();

                Picasso.with(mContext).load(headUrl).error(R.drawable.icon_no_download).
                        transform(new CircleCornerForm()).into(img);

                break;
        }

    }

    private void showSwichBg(View view, TargetBean item) {
        String clazz = item.getClass_id();

        if (item.getState().equals("1")) {
            view.setBackgroundResource(R.drawable.shaper_target_gray);
        } else {
            if (MyTextUtil.isEmpty(clazz)) {
                view.setBackgroundResource(R.mipmap.icon_zdy);
            } else if (clazz.contains("yundong")) {
                view.setBackgroundResource(R.mipmap.yundong_bg);
            } else if (clazz.contains("xuexi")) {
                view.setBackgroundResource(R.mipmap.study_bg);
            } else if (clazz.contains("jiankang")) {
                view.setBackgroundResource(R.mipmap.health_bg);
            } else if (clazz.contains("qinggan")) {
                view.setBackgroundResource(R.mipmap.qinggan_bg);
            }
        }


    }


    private String getDotDay(TargetBean item) {

        if (MyTextUtil.isEmpty(item.getStart_date()) || MyTextUtil.isEmpty(item.getEnd_date())) {
            return "";
        }
        return DateUtil.timeOnlyDot(item.getStart_date()) + "-" + DateUtil.timeOnlyDot(item.getEnd_date());
    }

}
