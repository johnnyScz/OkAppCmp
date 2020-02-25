package com.xinyu.newdiggtest.bigq;


import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;

import java.util.List;

/**
 * 电影列表
 */
public class FileAdapter extends BaseQuickAdapter<FileBean, BaseViewHolder> {


    int type;//0 展示 1新增或编辑


    public void setType(int tp) {

        this.type = tp;
    }


    public FileAdapter(int layoutResId, @Nullable List<FileBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, FileBean item) {

        ImageView icon = helper.getView(R.id.icon);
        helper.setText(R.id.filename, item.getFname() + "." + item.getDex());
        helper.addOnClickListener(R.id.item);

        if (type == 1) {
            helper.setVisible(R.id.delect_img, true);
            helper.addOnClickListener(R.id.delect_img);
        } else {
            helper.setVisible(R.id.delect_img, false);
        }

        int tt = item.getType();
        switch (tt) {
            case 1:
                icon.setImageResource(R.mipmap.pic);
                break;

            case 2:
                icon.setImageResource(R.mipmap.docu);
                break;

            case 3:
                icon.setImageResource(R.mipmap.qita);
                break;
        }


    }


}
