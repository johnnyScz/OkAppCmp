package com.xinyu.newdiggtest.adapter.viewhelper;

import android.animation.ObjectAnimator;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xinyu.newdiggtest.R;

import java.util.List;

public class QuickExpandableAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {


    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_LEVEL_2 = 2;


    Handler sendHandler;


    public void setHandler(Handler mHandler) {
        this.sendHandler = mHandler;
    }


    public QuickExpandableAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_tree_zhang);
        addItemType(TYPE_LEVEL_1, R.layout.item_tree_zhang1);
        addItemType(TYPE_LEVEL_2, R.layout.item_tree_zhang2);

    }


    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {

        switch (helper.getItemViewType()) {

            case TYPE_LEVEL_0:

                final ExpandItem item0 = (ExpandItem) item;

                helper.setText(R.id.tvName, item0.getTitle());

                final ImageView icon = helper.getView(R.id.ivIcon);

                icon.setImageResource(R.drawable.arrow_b);


                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int pos = helper.getAdapterPosition();

                        if (item0.isExpanded()) {
                            collapse(pos);

                            Log.e("amtf", "展开到收缩");

//                            Animation circle_anim = AnimationUtils.loadAnimation(mContext, R.anim.collapse);
//                            LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
//                            circle_anim.setInterpolator(interpolator);
//                            circle_anim.setFillAfter(true);
//                            if (circle_anim != null) {
//                                icon.startAnimation(circle_anim);  //开始动画
//                            }

//                            icon.setBackgroundResource(0);

                            ObjectAnimator animator = ObjectAnimator.ofFloat(icon, "rotation", -90f, 0f);
                            animator.setDuration(100);
                            animator.start();


                        } else {
                            Log.e("amtf", "收缩到展开");


//                            Animation circle_anim = AnimationUtils.loadAnimation(mContext, R.anim.expand);
//                            LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
//                            circle_anim.setInterpolator(interpolator);
//                            circle_anim.setFillAfter(true);
//                            if (circle_anim != null) {
//                                icon.startAnimation(circle_anim);  //开始动画
//                            }

                            ObjectAnimator animator = ObjectAnimator.ofFloat(icon, "rotation", 0f, -90f);
                            animator.setDuration(100);
                            animator.start();


                            expand(pos);
                            if (listner != null) {
                                listner.requestData(item0, item0.getRequestChildCount());
                            }
                        }
                    }
                });


                break;

            case TYPE_LEVEL_1:

                final Expand1Item item1 = (Expand1Item) item;
                helper.setText(R.id.tvName, item1.getTitle());

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (item1.isExpanded()) {
                            collapse(pos, false);
                        } else {
                            expand(pos, false);
                        }
                    }
                });

                break;

            case TYPE_LEVEL_2:

//                final Expand2Item item2 = (Expand2Item) item;
//                helper.setText(R.id.tvName, item2.getTitle());
//
//                helper.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });

                break;
        }

    }

    RequestDataListner listner;

    public void setListner(RequestDataListner mListner) {
        this.listner = mListner;
    }


    public interface RequestDataListner {

        void requestData(MultiItemEntity item, int count);
    }

}
