package com.xinyu.newdiggtest.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.ContactBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;

import java.util.List;

public class SearchContactAdapter extends BaseMultiItemQuickAdapter<ContactBean, BaseViewHolder> {

    public static final int TYPE_NoMal = 0;
    public static final int TYPE_Title = 1;


    public SearchContactAdapter(List<ContactBean> data) {
        super(data);
        addItemType(TYPE_NoMal, R.layout.item_img_tx);
        addItemType(TYPE_Title, R.layout.item_text);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final ContactBean item) {



        switch (helper.getItemViewType()) {

            case TYPE_NoMal:

                helper.addOnClickListener(R.id.item);

                helper.setText(R.id.item_name, item.getNickname());

                ImageView imageView = helper.getView(R.id.inco_person);


                if (item.getF_type().equals("1")) {

                    Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.head_card);
                    imageView.setImageDrawable(drawable);

                } else {

                    Picasso.with(mContext).load(item.getHead()).error(R.drawable.icon_no_download).
                            transform(new CircleCornerForm()).into(imageView);
                }


                break;

            case TYPE_Title:

                helper.setText(R.id.id_text, item.getName());

                break;
        }
    }


}
