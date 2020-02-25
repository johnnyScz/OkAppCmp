package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.MonitorChildBean;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.ArrayList;
import java.util.List;

public class ExcutorSelectAdapter extends BaseQuickAdapter<MonitorChildBean, BaseViewHolder> {


    List<String> beforSelct = new ArrayList<>();

    public ExcutorSelectAdapter(int layoutResId, @Nullable List<MonitorChildBean> data) {
        super(layoutResId, data);

    }

    public void setBeforSelct(List<String> datas) {
        this.beforSelct = datas;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MonitorChildBean bean) {
        helper.setText(R.id.item_name, bean.getUser().getNickname());
        ImageView img = helper.getView(R.id.inco_person);

        String url = MyTextUtil.isEmpty(bean.getUser().getHead()) ? bean.getUser().getCustom_head() : bean.getUser().getHead();
        Picasso.with(mContext).load(url).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(img);

        final CheckBox cx = helper.getView(R.id.check);


        int tag = bean.getCheckTag();

        if (beforSelct.size() > 0) {
            for (String item : beforSelct) {
                if (item.equals(bean.getUser().getUser_id())) {
                    tag = 1;
                    break;
                }
            }
        }


        if (tag == 0) {
            cx.setChecked(false);
        } else {
            cx.setChecked(true);
        }

        cx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isChecked = !isChecked;
                if (isChecked) {
                    if (checkBoxListner != null) {
                        checkBoxListner.onSelect(bean);
                        bean.setCheckTag(1);
                    }

                } else {
                    if (checkBoxListner != null) {
                        checkBoxListner.onNoSelect(bean);
                        bean.setCheckTag(0);
                    }
                }
            }
        });

    }


    OnCheckBoxListner checkBoxListner;


    public void setCheckBoxListner(OnCheckBoxListner listner) {
        this.checkBoxListner = listner;
    }


    public interface OnCheckBoxListner {

        void onNoSelect(MonitorChildBean bean);

        void onSelect(MonitorChildBean bean);
    }


}
