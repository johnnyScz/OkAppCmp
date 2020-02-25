package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.MonitorChildBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

public class ChooseAdapter extends BaseQuickAdapter<MonitorChildBean, BaseViewHolder> {


    public ChooseAdapter(int layoutResId, @Nullable List<MonitorChildBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final MonitorChildBean bean) {
        ImageView img = helper.getView(R.id.head);

        String url = MyTextUtil.isEmpty(bean.getUser().getHead()) ? bean.getUser().getCustom_head() : bean.getUser().getHead();
        Picasso.with(mContext).load(url).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(img);

        helper.addOnClickListener(R.id.iv_delete);

    }


}
