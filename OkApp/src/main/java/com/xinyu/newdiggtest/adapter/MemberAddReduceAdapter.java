package com.xinyu.newdiggtest.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.ImgBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;

public class MemberAddReduceAdapter extends BaseMultiItemQuickAdapter<RoomMemberBean, BaseViewHolder> {

    Context context;

    public MemberAddReduceAdapter(List<RoomMemberBean> data, Context mctx) {
        super(data);
        this.context = mctx;
        addItemType(ImgBean.Item, R.layout.item_member);
        addItemType(ImgBean.Item_Add, R.layout.item_add);
        addItemType(ImgBean.Item_Reduce, R.layout.item_reduce);
    }

    @Override
    protected void convert(BaseViewHolder helper, RoomMemberBean item) {

        int type = helper.getItemViewType();

        switch (type) {
            case 0:
                helper.addOnClickListener(R.id.img);

                ImageView icon = helper.getView(R.id.img);

                String headUrl = MyTextUtil.isEmpty(item.getUser().getHead()) ? item.getUser().getCustom_head()
                        : item.getUser().getHead();

                if (!MyTextUtil.isEmpty(headUrl)) {
                    Picasso.with(mContext).load(headUrl).transform(new CircleCornerForm()).into(icon);
                } else {
                    icon.setImageResource(R.drawable.icon_no_download);
                }
                helper.setText(R.id.nick_name, item.getUser().getNickname());
                break;

            case 1:
                helper.addOnClickListener(R.id.add);

                break;

            case 2:

                helper.addOnClickListener(R.id.reduce);

                break;


        }
    }
}
