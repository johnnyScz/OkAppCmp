package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;
import android.widget.ImageView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.CompanyBean;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.utils.CircleTransform;
import com.xinyu.newdiggtest.utils.PreferenceUtil;


import java.util.List;


/**
 * 电影列表
 */
public class MyAdapter extends BaseQuickAdapter<CompanyBean, BaseViewHolder> {


    List<CompanyBean> datas;

    int itemTag = 0;

    public MyAdapter(int layoutResId, @Nullable List<CompanyBean> data) {
        super(layoutResId, data);
        datas = data;
    }

    public void setItemTag(int tag) {
        this.itemTag = tag;
    }


    @Override
    protected void convert(BaseViewHolder helper, CompanyBean bean) {

        helper.setText(R.id.item_name, bean.getName());

        helper.addOnClickListener(R.id.item_company);

        if (itemTag == 1) {
            helper.setVisible(R.id.wei_view, false);
        } else {
            helper.setVisible(R.id.wei_view, true);
        }

        if (helper.getAdapterPosition() == datas.size() - 1) {
            helper.setVisible(R.id.line, false);

        } else {
            helper.setVisible(R.id.line, true);
        }

        if (bean.getCompany_id().equals(PreferenceUtil.getInstance(App.mContext).getCompanyId())) {
            helper.setVisible(R.id.icon_check, true);
        } else {
            helper.setVisible(R.id.icon_check, false);
        }


        ImageView icon = helper.getView(R.id.inco_person);

        Picasso.with(App.mContext).load(bean.getLogo()).error(R.drawable.icon_no_download).
                transform(new CircleTransform()).into(icon);


    }
}
