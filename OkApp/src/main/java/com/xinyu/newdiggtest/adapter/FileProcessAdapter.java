package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.FUrlBean;

import java.util.List;

/**
 * 电影列表
 */
public class FileProcessAdapter extends BaseQuickAdapter<FUrlBean, BaseViewHolder> {


    public FileProcessAdapter(int layoutResId, @Nullable List<FUrlBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, FUrlBean item) {


        ImageView icon = helper.getView(R.id.icon);
        helper.setText(R.id.filename, item.getF_title() + "." + item.getF_type());
        helper.addOnClickListener(R.id.item);

        helper.setVisible(R.id.delect_img, false);

        String dcc = item.getF_type();

        if (dcc.toLowerCase().equals("jpg") || dcc.toLowerCase().equals("jeg") || dcc.toLowerCase().equals("jpeg") || dcc.toLowerCase().equals("png")) {
            icon.setImageResource(R.mipmap.pic);
        } else if (dcc.equals("pdf") || dcc.equals("pptx") || dcc.equals("ppt") || dcc.equals("txt") || dcc.equals("doc") || dcc.equals("docx") || dcc.equals("xls") || dcc.equals("xlsx")) {
            icon.setImageResource(R.mipmap.docu);
        } else {
            icon.setImageResource(R.mipmap.qita);
        }


    }


}
