package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.viewhelper.interfa.MyLongClickner;
import com.xinyu.newdiggtest.bean.RetListBean;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

public class TodoInfoAdapter extends BaseQuickAdapter<RetListBean.InvitesBean, BaseViewHolder> {


    String byCreate;

    int type = 0;//0 展示打钩，问好 1 不显示


    List<RetListBean.InvitesBean> finshDatas;

    public void setFinishDatas(List<RetListBean.InvitesBean> data) {
        this.finshDatas = data;
    }


    public TodoInfoAdapter(int layoutResId, @Nullable List<RetListBean.InvitesBean> data) {
        super(layoutResId, data);

    }

    public void setCreater(String creater) {
        this.byCreate = creater;
    }


    public void setType(int tag) {
        this.type = tag;
    }


    @Override
    protected void convert(final BaseViewHolder helper, final RetListBean.InvitesBean bean) {

        String name = MyTextUtil.isEmpty(bean.getOwnermap().getNickname()) ? bean.getOwnermap().getName() : bean.getOwnermap().getNickname();


        final TextView textView = helper.getView(R.id.name_person);

        textView.setText(name);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llongLisntner != null) {
                    llongLisntner.onLongMyClick(bean, textView);
                }
            }
        });

        ImageView imageView = helper.getView(R.id.state_tag);

        String state = bean.getF_state();


        if (type == 1) {
            imageView.setVisibility(View.GONE);
        } else {

            if (state.equals("0") && !bean.getF_create_by().equals(bean.getOwnermap().getUser_id())) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.weiqueren);
            } else if (state.equals("2")) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.cancel);
            } else {

                if (finshDatas == null || finshDatas.size() < 1) {
                    imageView.setVisibility(View.GONE);
                } else {
                    RetListBean.InvitesBean finData = null;
                    for (RetListBean.InvitesBean tt : finshDatas) {
                        if (tt.getOwnermap().getUser_id().equals(bean.getOwnermap().getUser_id())) {
                            finData = tt;
                            break;
                        }
                    }

                    if (finData != null) {
                        if (finData.getF_state().equals("1")) {
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageResource(R.drawable.yiqueren);
                        } else {
                            imageView.setVisibility(View.GONE);
                        }
                    }

                }
            }
        }


    }


    MyLongClickner llongLisntner;


    public void setLongMyListner(MyLongClickner ll) {
        this.llongLisntner = ll;

    }


}
