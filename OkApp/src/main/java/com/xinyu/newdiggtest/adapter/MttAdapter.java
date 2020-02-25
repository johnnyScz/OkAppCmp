package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.viewhelper.MemberTestBean;

import java.util.List;

public class MttAdapter extends BaseMultiItemQuickAdapter<MemberTestBean, BaseViewHolder> {


    public MttAdapter(@Nullable List<MemberTestBean> data) {
        super(data);
        addItemType(1, R.layout.item_test1);//defaule 类型
        addItemType(2, R.layout.item_test2);

    }


    @Override
    protected void convert(BaseViewHolder helper, MemberTestBean item) {

        int type = helper.getItemViewType();

        switch (type) {

            case 1:
                helper.setText(R.id.text, item.getName());

                helper.addOnClickListener(R.id.rl_item1);

//                if (item.getChild() == null || item.getChild().size() < 1) {
//
//                }

                break;

            case 2:
//                final ExpandView expandView = helper.getView(R.id.expandView);
////
//                if (item.getChild() != null && item.getChild().size() > 0) {
//                    expandView.setListData(item.getChild());
//                }
//
//                View view = helper.getView(R.id.item2);
//                final ImageView mImageView = helper.getView(R.id.icon);
//                helper.setText(R.id.title, item.getName());
//
//                expandView.setmIsExpand(false);
//
//                view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if (expandView.isExpand()) {
//                            expandView.collapse();
//                            mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow_b));
//                        } else {
//                            expandView.expand();
//
//                            mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.arrow_u));
//                        }
//
//
//                    }
//                });


                break;
        }


    }


}
