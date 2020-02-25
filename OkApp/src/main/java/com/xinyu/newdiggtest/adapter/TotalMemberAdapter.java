package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.MemberRetBean;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

public class TotalMemberAdapter extends BaseQuickAdapter<MemberRetBean.MemberOutBean, BaseViewHolder> {


    int tag = 0;


    public void setTag(int mTag) {

        this.tag = mTag;
    }


    public TotalMemberAdapter(int layoutResId, @Nullable List<MemberRetBean.MemberOutBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final MemberRetBean.MemberOutBean bean) {

        View item = helper.getView(R.id.item);


        TextView textView = helper.getView(R.id.person_name);

        if (tag == 1 && bean.getIsUnable() == 1) {
            item.setEnabled(false);
            textView.setTextColor(App.mContext.getResources().getColor(R.color.bar_grey));

        } else {
            textView.setTextColor(App.mContext.getResources().getColor(R.color.color_4d));
            item.setEnabled(true);
            helper.addOnClickListener(R.id.item);
        }

        ImageView img = helper.getView(R.id.head);
        String url = bean.getUserinfo().getHead();

        if (!MyTextUtil.isEmpty(url) && url.contains("http")) {

            Picasso.with(mContext).load(url).error(R.drawable.icon_no_download).
                    transform(new CircleCornerForm()).into(img);
        } else {
            img.setImageResource(R.drawable.icon_no_download);
        }


        String name = MyTextUtil.isEmpty(bean.getUserinfo().getNickname()) ? bean.getUserinfo().getName() : bean.getUserinfo().getNickname();


        textView.setText(name);

        ImageView seletIcon = helper.getView(R.id.select);

        if (bean.isSelect()) {
            seletIcon.setImageResource(R.drawable.cbox_check);
        } else {
            seletIcon.setImageResource(R.drawable.cbox_nocheck);
        }

    }


}
