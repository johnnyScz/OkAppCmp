package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.FilmBean;

import java.util.List;

/**
 * 电影列表
 */
public class FilmAdapter extends BaseQuickAdapter<FilmBean.FilmListBean, BaseViewHolder> {


    public FilmAdapter(int layoutResId, @Nullable List<FilmBean.FilmListBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, FilmBean.FilmListBean item) {

        helper.addOnClickListener(R.id.item);

        helper.setText(R.id.film_name, item.getF_movie_name());

        helper.setText(R.id.director, "导演: " + item.getF_director());

        ImageView postView = helper.getView(R.id.post_film);

        Picasso.with(mContext).load(item.getF_img()).error(R.drawable.icon_no_download).into(postView);


    }


}
