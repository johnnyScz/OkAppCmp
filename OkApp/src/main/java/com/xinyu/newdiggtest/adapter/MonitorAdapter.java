package com.xinyu.newdiggtest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.MonitorSpanBean;
import com.xinyu.newdiggtest.bean.MonitorSpanChildBean;
import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;


import java.util.List;

/**
 * Created by duanlian on 2016/9/12.
 */
public class MonitorAdapter extends BaseExpandableListAdapter {


    List<MonitorSpanBean.DataParentBean> groupList;//外层的数据源

    List<List<MonitorSpanChildBean>> childList;//里层的数据源


    OnCheckBox checkBoxListner;


    private Context context;

    public MonitorAdapter(Context context, List<MonitorSpanBean.DataParentBean> groupList,
                          List<List<MonitorSpanChildBean>> childList) {
        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    /**
     * 这个返回的一定要是对应外层的item里面的List集合的size
     *
     * @param groupPosition
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_group, null);
        //分组名字
        TextView textView = (TextView) convertView.findViewById(R.id.group_name);
        //子元素的个数
        ImageView icon = (ImageView) convertView.findViewById(R.id.right_icon);
        textView.setText(groupList.get(groupPosition).getRoom_name() + "(" + childList.get(groupPosition).size() + ")");

        if (isExpanded) {
            icon.setBackgroundResource(R.drawable.arrow_b);
        } else {
            icon.setBackgroundResource(R.drawable.icon_rr);
        }


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {

        final MonitorSpanChildBean item = childList.get(groupPosition).get(childPosition);

        view = View.inflate(context, R.layout.item_item, null);
        TextView textView = (TextView) view.findViewById(R.id.item_name);
        textView.setText(item.getUser().getNickname());

        ImageView head = view.findViewById(R.id.inco_person);

        String headUrl = MyTextUtil.isEmpty(item.getUser().getHead()) ?
                item.getUser().getCustom_head() :
                item.getUser().getHead();


        Picasso.with(context).load(headUrl).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(head);

        final CheckBox cx = view.findViewById(R.id.check);
        checkBoxState(item, cx);

        cx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    if (checkBoxListner != null) {
                        if (!AppContacts.MonitorList1.contains(item)) {
                            AppContacts.MonitorList1.add(item);
                        }
                        checkBoxListner.onSelect(item, true);
                    }

                } else {
                    if (checkBoxListner != null) {
                        if (AppContacts.MonitorList1.contains(item)) {
                            AppContacts.MonitorList1.remove(item);
                        }
                        checkBoxListner.onSelect(item, false);
                    }
                }

            }
        });


        return view;
    }

    private void checkBoxState(MonitorSpanChildBean item, CheckBox cx) {
        if (!item.getUser_id().equals(PreferenceUtil.getInstance(context).getUserId())) {
            cx.setEnabled(true);
            if (item.getCheckTag() == 0) {
                cx.setChecked(false);

            } else if (item.getCheckTag() == 1 && AppContacts.MonitorList1.size() <= 5) {
                cx.setChecked(true);
            } else {
                cx.setChecked(false);
            }
        } else {
            cx.setEnabled(false);
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public void setCheckBoxListner(OnCheckBox listner) {
        this.checkBoxListner = listner;
    }


    public interface OnCheckBox {

        void onSelect(MonitorSpanChildBean bean, boolean isCheck);
    }


}
