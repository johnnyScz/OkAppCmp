package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.utils.CircleCornerForm;

import java.util.List;

/**
 * 电影列表
 */
public class DeleteAdapter extends BaseQuickAdapter<RoomMemberBean, BaseViewHolder> {


    public DeleteAdapter(int layoutResId, @Nullable List<RoomMemberBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final RoomMemberBean item) {

        helper.setText(R.id.item_name, item.getUser().getNickname());
        ImageView person = helper.getView(R.id.inco_person);


        Picasso.with(mContext).load(item.getUser().getHead()).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(person);

        CheckBox ctx = helper.getView(R.id.check);

        ctx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (checkBoxListner != null) {
                    if (isChecked) {
                        checkBoxListner.addSelcet(item);
                    } else {
                        checkBoxListner.removeSelcet(item);
                    }
                }
            }
        });


    }


    public void setCheckBoxListner(OnCheckBoxListner listner) {
        this.checkBoxListner = listner;
    }


    OnCheckBoxListner checkBoxListner;

    public interface OnCheckBoxListner {

        void addSelcet(RoomMemberBean bean);

        void removeSelcet(RoomMemberBean bean);


    }


}
