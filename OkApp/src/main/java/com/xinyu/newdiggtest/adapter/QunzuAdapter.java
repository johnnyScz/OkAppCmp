package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.FocusTodoBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class QunzuAdapter extends BaseMultiItemQuickAdapter<FocusTodoBean, BaseViewHolder> {


    public QunzuAdapter(@Nullable List<FocusTodoBean> data) {
        super(data);
        addItemType(0, R.layout.item_cmp_card);//默认公司名片
        addItemType(1, R.layout.item_company);//公司类型
//        addItemType(2, R.layout.item_go_msg);//消息
    }


    @Override
    protected void convert(BaseViewHolder helper, FocusTodoBean item) {

        helper.addOnClickListener(R.id.rl_item);


        switch (helper.getItemViewType()) {

            case 0:

                helper.setText(R.id.comp_name, item.getCardBean().getName());

                helper.setText(R.id.count, item.getCardBean().getCount());


                break;


            case 1:

                JSONObject object = item.getCmpBean();

                if (object == null)
                    return;

                try {
                    String name = object.getString("name");

                    String cmType = object.getString("company_type");

                    ImageView icon = helper.getView(R.id.comp_icon);

                    if (cmType.equals("1")) {
                        icon.setImageResource(R.drawable.company_icon);
                        helper.setText(R.id.comp_name, name);
                    } else if (cmType.equals("0")) {
                        icon.setImageResource(R.drawable.person_icon);
                        helper.setText(R.id.comp_name, "个人通讯录");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;
        }


    }


}
