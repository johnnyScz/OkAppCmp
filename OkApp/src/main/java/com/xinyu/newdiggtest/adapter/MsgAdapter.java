package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.HomeMsgBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

public class MsgAdapter extends BaseMultiItemQuickAdapter<HomeMsgBean, BaseViewHolder> {


    public MsgAdapter(@Nullable List<HomeMsgBean> data) {
        super(data);
        addItemType(12, R.layout.item_finish_system);
        addItemType(1, R.layout.item_muit_msg);//defaule 类型
    }


    @Override
    protected void convert(BaseViewHolder helper, HomeMsgBean item) {
        if (item.getIs_read().equals("0")) {//未读
            helper.setVisible(R.id.red_dot, true);
        } else {
            helper.setVisible(R.id.red_dot, false);
        }

        switch (helper.getItemViewType()) {
            case 12:
                helper.setText(R.id.title_name, item.getName());
                String time = item.getCreate_time();
                String subTime = time.substring(0, time.length() - 3);
                helper.setText(R.id.finish_time, subTime);
                helper.addOnClickListener(R.id.rl_msg_item).addOnClickListener(R.id.look_info);
                helper.addOnClickListener(R.id.rl_item1);
                break;


            case 1:
                fillItemView(helper, item);
                break;
        }


    }

    /**
     * 类型为1
     * <p>
     * 点赞
     *
     * @param helper
     * @param item
     */
    private void fillItemView(BaseViewHolder helper, HomeMsgBean item) {
        ImageView iv = helper.getView(R.id.iv_target_icon);
        Picasso.with(mContext)
                .load(item.getFrom_head())
                .transform(new CircleCornerForm())
                .error(R.mipmap.icon_cat)
                .into(iv);
        helper.setText(R.id.tv_name, item.getFrom_nickname());
        helper.setText(R.id.mycontent, item.getName());
        String time = "";
        if (MyTextUtil.isEmpty(item.getCreate_time())) {
            time = "";
        } else {
            time = item.getCreate_time().substring(0, item.getCreate_time().length() - 3);
        }

        helper.setText(R.id.tv_date, time);

        if (!MyTextUtil.isEmpty(item.getWish())) {
            helper.setVisible(R.id.commonts_wish, true);
            helper.setText(R.id.commonts_wish, MyTextUtil.getDecodeStr(item.getWish()));

        } else {
            helper.setVisible(R.id.commonts_wish, false);
        }


        helper.addOnClickListener(R.id.rl_item1);

    }


}
