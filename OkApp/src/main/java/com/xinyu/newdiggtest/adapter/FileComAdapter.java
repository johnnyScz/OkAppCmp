package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.FileChildRetBean;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

/**
 * 电影列表
 */
public class FileComAdapter extends BaseQuickAdapter<FileChildRetBean.FileChildBean, BaseViewHolder> {


    public FileComAdapter(int layoutResId, @Nullable List<FileChildRetBean.FileChildBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, FileChildRetBean.FileChildBean item) {

        helper.addOnClickListener(R.id.item);

        helper.setText(R.id.title, item.getF_title());
        helper.setText(R.id.name, item.getF_create_by_info().getNickname());

        helper.setText(R.id.time, item.getF_create_date());

        helper.setText(R.id.comment_couont, "浏览 ：" + check(item.getF_count()));

        helper.setText(R.id.prise_couont, "点赞 ：" + check(item.getLikecount()));


    }

    private String check(String f_count) {

        if (MyTextUtil.isEmpty(f_count)) {
            return "0";
        }

        return f_count;
    }


}
