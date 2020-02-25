package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.MemberRetBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

public class TodoMemberDeletAdapter extends BaseQuickAdapter<MemberRetBean.MemberOutBean, BaseViewHolder> {


    public TodoMemberDeletAdapter(int layoutResId, @Nullable List<MemberRetBean.MemberOutBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final MemberRetBean.MemberOutBean bean) {

        helper.addOnClickListener(R.id.delect);

        ImageView img = helper.getView(R.id.inco_person);

        String url = bean.getUserinfo().getHead();


        if (!MyTextUtil.isEmpty(url)) {
            Picasso.with(mContext).load(url).error(R.drawable.icon_no_download).
                    transform(new CircleCornerForm()).into(img);

        } else {
            img.setImageResource(R.drawable.icon_no_download);
        }


    }


}
