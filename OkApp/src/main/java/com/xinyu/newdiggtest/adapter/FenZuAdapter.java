package com.xinyu.newdiggtest.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.MonitorChildBean;
import com.xinyu.newdiggtest.bean.MonitorParentBean;
import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import java.util.List;

public class FenZuAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    //    private Context context;
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_PERSON = 1;

    OnCheckBox checkBoxListner;


    public FenZuAdapter(List<MultiItemEntity> data) {
        super(data);
        AppContacts.MonitorList.clear();
        addItemType(TYPE_LEVEL_0, R.layout.item_head);
        addItemType(TYPE_PERSON, R.layout.item_item);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                final MonitorParentBean lv0 = (MonitorParentBean) item;

                int len = lv0.getMember_list().size();

                helper.setText(R.id.head_name, lv0.getRoom_name() + "(" + len + ")").
                        setImageResource(R.id.head_iv, lv0.isExpanded() ? R.drawable.arrow_b : R.drawable.icon_rr);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getPosition();
                        if (lv0.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case TYPE_PERSON:
                final MonitorChildBean bean = (MonitorChildBean) item;
                helper.setText(R.id.item_name, bean.getUser().getNickname());
                ImageView img = helper.getView(R.id.inco_person);

                String url = MyTextUtil.isEmpty(bean.getUser().getHead()) ? bean.getUser().getCustom_head() : bean.getUser().getHead();
                Picasso.with(mContext).load(url).error(R.drawable.icon_no_download).
                        transform(new CircleCornerForm()).into(img);

                final CheckBox cx = helper.getView(R.id.check);

                if (!bean.getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                    cx.setEnabled(true);
                    if (bean.getCheckTag() == 0) {
                        cx.setChecked(false);

                    } else if (bean.getCheckTag() == 1) {
                        cx.setChecked(true);

                    }
                } else {
                    cx.setEnabled(false);
                }


                cx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//                        if (AppContacts.MonitorList.contains(bean)) {
//                            ToastUtils.getInstanc(mContext).showToast("不能重复选择");
//                            cx.setChecked(false);
//                            return;
//                        }

                        if (isChecked) {
                            if (checkBoxListner != null) {
                                checkBoxListner.addImg(bean);
                                if (!AppContacts.MonitorList.contains(bean)) {
                                    AppContacts.MonitorList.add(bean);
                                }
                                // checkBoxListner.onSelect(bean, true);
                            }

                        }
//                        else {
//                            if (checkBoxListner != null) {
//                                checkBoxListner.removeImg(bean);
//                                if (AppContacts.MonitorList.contains(bean)) {
//                                    AppContacts.MonitorList.remove(bean);
//                                }
//                                //checkBoxListner.onSelect(bean, false);
//                            }
//                        }

                    }
                });


//                helper.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        int cp = getParentPosition(person);
//                        ((Level0Item) getData().get(cp)).removeSubItem(person);
//                        getData().remove(helper.getClickPosition());
//                        notifyItemRemoved(helper.getClickPosition());
//                    }
//                });
                break;
        }
    }


    public void setCheckBoxListner(OnCheckBox listner) {
        this.checkBoxListner = listner;
    }


    public interface OnCheckBox {

        void addImg(MonitorChildBean bean);

        void removeImg(MonitorChildBean bean);

        void onSelect(MonitorChildBean bean, boolean isCheck);
    }


}
