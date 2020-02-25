package com.xinyu.newdiggtest.adapter.viewhelper;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.ChildExpandBean;
import com.xinyu.newdiggtest.bean.MemberRetBean;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.util.List;


public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private List<ChildExpandBean> groupList;
    private List<List<MemberRetBean.MemberOutBean>> childList;

    Context ctx;

    MyExpandableListAdapter(List<ChildExpandBean> groupList, List<List<MemberRetBean.MemberOutBean>> childList, Context context) {
        this.groupList = groupList;
        this.childList = childList;
        this.ctx = context;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    private int selectedGroupPosition = -1;
    private int selectedChildPosition = -1;

    public void setSelectedPosition(int selectedGroupPosition, int selectedChildPosition) {
        this.selectedGroupPosition = selectedGroupPosition;
        this.selectedChildPosition = selectedChildPosition;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_contact1, null);
        }
        final TextView textView = (TextView) convertView.findViewById(R.id.film_name);


        ImageView imageView = convertView.findViewById(R.id.post_film);

        final MemberRetBean.MemberOutBean data = (MemberRetBean.MemberOutBean) getChild(groupPosition, childPosition);

        textView.setText(data.getUserinfo().getName());


        if (data.getUserinfo() != null && !MyTextUtil.isEmpty(data.getUserinfo().getHead())) {

            String headUrl = data.getUserinfo().getHead();

            Picasso.with(App.mContext).load(headUrl).error(R.drawable.icon_no_download).
                    transform(new CircleCornerForm()).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.icon_no_download);
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("amtf", "去个人空间:" + data.getUserinfo().getName());
            }
        });


        convertView.findViewById(R.id.gochat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listner != null) {
                    listner.askRoom(data);
                }
            }
        });


        convertView.findViewById(R.id.post_film).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listner != null) {
                    listner.onHead(data);
                }
            }
        });


//        if (groupPosition == selectedGroupPosition) {
//            if (childPosition == selectedChildPosition) {
//                textView.setBackgroundColor(0xffb6ddee);
//            } else {
//                textView.setBackgroundColor(Color.TRANSPARENT);
//            }
//        }


//        textView.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                setSelectedPosition(groupPosition, childPosition);
//                notifyDataSetChanged();
//
//            }
//        });


        return convertView;
    }

    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition).getF_name();
    }

    public int getGroupCount() {
        return groupList.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_tree_zhang, null);
        }

        ImageView imgIndicator = convertView.findViewById(R.id.ivIcon);
        TextView textView = convertView.findViewById(R.id.tvName);

        textView.setText(getGroup(groupPosition).toString());


        if (isExpanded) {
            imgIndicator.setBackgroundResource(R.drawable.arrow_b);
        } else {
            imgIndicator.setBackgroundResource(R.drawable.ic_right);
        }


        return convertView;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    ChatRoomListner listner;

    public void setChildListner(ChatRoomListner ll) {

        this.listner = ll;
    }


    interface ChatRoomListner {

        void askRoom(MemberRetBean.MemberOutBean data);

        void onHead(MemberRetBean.MemberOutBean data);

    }
}




