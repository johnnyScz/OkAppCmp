package com.xinyu.newdiggtest.adapter.viewhelper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.withwings.wt.treelist.TreeAdapter;
import com.withwings.wt.treelist.bean.TreeData;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.Digg.fragment.MyselfTreeActivity;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;


import java.util.List;

/**
 * TODO
 * 创建：WithWings 时间 19/1/2
 * Email:wangtong1175@sina.com
 */
public class DemoAdapter extends TreeAdapter {

    private Context mActivity;

    public DemoAdapter(Context activity, List<DemoTreeData> data) {
        super(data);
        mActivity = activity;
    }

    @Override
    public int getItemViewType(int position, int level) {
        return level;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == 1) {
            return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_level_1, viewGroup, false));
        } else if (viewType == 2) {
            return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_level_2, viewGroup, false));
        } else {
            return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_level_3, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int pos, TreeData data) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;

            final DemoTreeData dat = (DemoTreeData) data;

            holder.name.setText(dat.getName());

            if (!dat.isHasChild()) {

                holder.mTvStatus.setVisibility(View.INVISIBLE);

                if (getItemViewType(pos) == 1) {
                    holder.head.setVisibility(View.GONE);
                    holder.msgChat.setVisibility(View.GONE);
                } else {
                    showImg(holder);
                    if (!MyTextUtil.isEmpty(dat.getHead()) && dat.getHead().contains("http")) {
                        Picasso.with(mActivity).load(dat.getHead()).error(R.drawable.icon_no_download).
                                transform(new CircleCornerForm()).into(holder.head);
                    } else {
                        holder.head.setImageResource(R.drawable.icon_no_download);
                    }

                    holder.head.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if (PreferenceUtil.getInstance(mActivity).getUserId().equals(dat.userId))
                                return;
                            //获取用户信息
                            Intent mintent = new Intent(mActivity, MyselfTreeActivity.class);


                            mintent.putExtra("userId", dat.userId);
                            mActivity.startActivity(mintent);

                        }
                    });


                    holder.msgChat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (clickListner != null) {
                                clickListner.onChat(dat);
                            }

                        }
                    });
                }

            } else {
                holder.mTvStatus.setVisibility(View.VISIBLE);

                if (data.isTreeOpen()) {
                    holder.mTvStatus.setImageResource(R.drawable.arrow_b);
                } else {
                    holder.mTvStatus.setImageResource(R.drawable.ic_right);
                }


                hidenImg(holder);
            }

            int type = getItemViewType(pos);


            if (!MyTextUtil.isEmpty(dat.getUserId()) && dat.getUserId().equals(PreferenceUtil.getInstance(mActivity).getUserId())) {
                holder.msgChat.setVisibility(View.GONE);
            }

            if (type == 1) {

            } else {

            }
//
        }
    }

    private void hidenImg(ViewHolder holder) {
        holder.head.setVisibility(View.GONE);
        holder.msgChat.setVisibility(View.GONE);

    }

    private void showImg(ViewHolder holder) {
        holder.head.setVisibility(View.VISIBLE);
        holder.msgChat.setVisibility(View.VISIBLE);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mTvStatus;

        ImageView head;
        ImageView msgChat;

        TextView name;

        View item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvStatus = itemView.findViewById(R.id.tv_status);
            name = itemView.findViewById(R.id.text);
            item = itemView.findViewById(R.id.item);

            head = itemView.findViewById(R.id.head);
            msgChat = itemView.findViewById(R.id.go_msg);


        }
    }


    public void setMyListner(OnChatClick listner) {

        this.clickListner = listner;
    }


    OnChatClick clickListner;

    public interface OnChatClick {

        void onChat(DemoTreeData data);

    }

}
