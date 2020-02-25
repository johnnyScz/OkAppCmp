package com.xinyu.newdiggtest.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.TargetKindItemBean;

import java.util.List;

/**
 * Created by zhangyb on 2017/7/3.
 */
public class MultiGridRecycleAdapter extends RecyclerView.Adapter<MultiGridRecycleAdapter.MyViewHolder> {


    public static final int TYPE_ITEM_TiTle = 1;
    public static final int TYPE_ITEM_Normal = 2;

    private List<TargetKindItemBean> mDataList;
    private LayoutInflater mInflater;

    Context mctx;

    public MultiGridRecycleAdapter(Context context, List<TargetKindItemBean> dataList) {
        mDataList = dataList;
        mInflater = LayoutInflater.from(context);
        mctx = context;
    }

    @Override
    public MultiGridRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        //根据 viewType 区分不同的 布局
        if (viewType == TYPE_ITEM_TiTle) {
            view = mInflater.inflate(R.layout.item_title, parent, false);
        } else {
            view = mInflater.inflate(R.layout.item_target__kind, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MultiGridRecycleAdapter.MyViewHolder holder, int position) {
        int type = holder.getItemViewType();

        final TargetKindItemBean item = mDataList.get(position);

        switch (type) {
            case TYPE_ITEM_TiTle:
                holder.mTextView.setText(item.getF_title());
                break;

            case TYPE_ITEM_Normal:
                holder.mTextView.setText(item.getF_title());
                Picasso.with(mctx).load(item.getF_img()).error(R.drawable.icon_no_download).into(holder.kindHead);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (listner != null) {
                            listner.onItemClick(item);
                        }
                    }
                });

                break;
        }


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    //此函数在调用 RecyclerView.setAdapter 时调用
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            GridLayoutManager gridManager = ((GridLayoutManager) manager);

            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type) {
                        case TYPE_ITEM_TiTle:
                            return 3;

                        case TYPE_ITEM_Normal:
                            return 1;

                        default:
                            return 3;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getItemType();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView kindHead;

        public MyViewHolder(View view) {
            super(view);

            int id = view.getId();

            switch (id) {
                case R.id.title:
                    mTextView = view.findViewById(R.id.title);
                    break;

                case R.id.item_normal:
                    mTextView = view.findViewById(R.id.kind_name);
                    kindHead = view.findViewById(R.id.kind_id);

                    break;

            }

        }
    }

    OnItemDataClickListner listner;


    public void setOnListner(OnItemDataClickListner mlistner) {

        this.listner = mlistner;
    }


    public interface OnItemDataClickListner {

        void onItemClick(TargetKindItemBean item);


    }

}