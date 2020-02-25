package com.xinyu.newdiggtest.adapter;


import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.FUrlBean;
import com.xinyu.newdiggtest.bean.LeaveInfoBean;
import com.xinyu.newdiggtest.h5.WebViewActivity;
import com.xinyu.newdiggtest.office.OfficeX5CoreActivity;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

/**
 * 电影列表
 */
public class ProcessAdapter extends BaseQuickAdapter<LeaveInfoBean.LeaveBean, BaseViewHolder> {


    public ProcessAdapter(int layoutResId, @Nullable List<LeaveInfoBean.LeaveBean> data) {
        super(layoutResId, data);


    }

    @Override
    protected void convert(BaseViewHolder helper, LeaveInfoBean.LeaveBean item) {

        String head = item.getExecutorinfo().getHead();

        ImageView headImg = helper.getView(R.id.head);

        Picasso.with(mContext).load(head).transform(new CircleCornerForm()).error(R.drawable.icon_no_download).into(headImg);
        helper.setText(R.id.name, item.getExecutorinfo().getNickname());
        helper.setText(R.id.step, "步骤" + item.getStep());

        helper.setText(R.id.creat_time, checkTime(item.getF_confirm_date()));

        String state = item.getF_state();//(0等待操作  1.拒绝 2.通过)


        TextView caozuo = helper.getView(R.id.caozuo);


        if (state.equals("0")) {
            TextView state_tag = helper.getView(R.id.state_tag);
            state_tag.setVisibility(View.GONE);

            caozuo.setText("等待操作");
            caozuo.setTextColor(mContext.getResources().getColor(R.color.wait_black));


        } else {
            TextView state_tag = helper.getView(R.id.state_tag);
            state_tag.setVisibility(View.VISIBLE);

            state_tag.setText(item.getF_comment());

            if (state.equals("1")) {
                caozuo.setText("拒绝");
                caozuo.setTextColor(Color.RED);

            } else if (state.equals("2")) {
                caozuo.setText("通过");
                caozuo.setTextColor(Color.GREEN);
            }
        }


        LeaveInfoBean.LeaveBean.FFileBean fileBean = item.getF_file();

        if (fileBean != null && fileBean.getF_url() != null && fileBean.getF_url().size() > 0) {

            RecyclerView recyclerView = helper.getView(R.id.file_list);
            recyclerView.setVisibility(View.VISIBLE);
            List<FUrlBean> list = fileBean.getF_url();
            setFileList(recyclerView, list);
        } else {
            RecyclerView recyclerView = helper.getView(R.id.file_list);
            recyclerView.setVisibility(View.GONE);
        }


    }

    private void setFileList(RecyclerView recyclerView, List<FUrlBean> list) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器

        FileProcessAdapter adapter = new FileProcessAdapter(R.layout.item_file, list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                FUrlBean item = (FUrlBean) adapter.getData().get(position);

                String path = item.getF_type();

                if (path.toLowerCase().equals("jpg") || path.toLowerCase().equals("jeg") || path.toLowerCase().equals("jpeg") || path.toLowerCase().equals("png")) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("path", item.getF_path());
                    mContext.startActivity(intent);

                } else {
                    Intent intent = new Intent(mContext, OfficeX5CoreActivity.class);
                    intent.putExtra("path", item.getF_path());
                    mContext.startActivity(intent);
                }
            }
        });


    }

    private String checkTime(String f_confirm_date) {

        if (MyTextUtil.isEmpty(f_confirm_date) || f_confirm_date.length() < 9) {
            return "";
        }

        String time = "";
        if (f_confirm_date.contains("T")) {
            time = f_confirm_date.replace("T", " ");
        } else {
            time = f_confirm_date;
        }
        return time.substring(0, time.length() - 3);
    }


}
