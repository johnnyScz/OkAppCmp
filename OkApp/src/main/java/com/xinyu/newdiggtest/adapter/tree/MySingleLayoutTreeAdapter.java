package com.xinyu.newdiggtest.adapter.tree;

import android.support.annotation.Nullable;

import com.ahao.basetreeview.adapter.SingleLayoutTreeAdapter;
import com.ahao.basetreeview.model.TreeNode;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.utils.Utils;

import java.util.List;

public class MySingleLayoutTreeAdapter extends SingleLayoutTreeAdapter<NoteItem> {

    public MySingleLayoutTreeAdapter(int layoutResId, @Nullable List<TreeNode<NoteItem>> dataToBind) {
        super(layoutResId, dataToBind);
    }

    @Override
    protected void convert(BaseViewHolder helper, TreeNode<NoteItem> item) {
        super.convert(helper, item);
        helper.setText(R.id.tvName, "Id:" + item.getId() + ":" + item.getData().getName() + " 父Id=" + item.getPId());
        if (item.isLeaf()) {
            helper.setImageResource(R.id.ivIcon, R.drawable.info);
        } else {
            if (item.isExpand()) {
                helper.setImageResource(R.id.ivIcon, R.drawable.ic_right);
            } else {
                helper.setImageResource(R.id.ivIcon, R.drawable.arrow_b);
            }
        }


    }

    @Override
    protected int getTreeNodeMargin() {
        return Utils.dp2px(this.mContext, 10);
    }


}