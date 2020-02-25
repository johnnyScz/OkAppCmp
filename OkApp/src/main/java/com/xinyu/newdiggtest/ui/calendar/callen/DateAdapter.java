package com.xinyu.newdiggtest.ui.calendar.callen;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.widget.CalendarView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sung on 2017/7/19.
 * <p>
 * 日期适配器
 */

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> implements View.OnClickListener {
    private Context mContext;
    public List<DateObject> dates = new ArrayList<>();
    private onCalendarDayClick onCalendarDayClickListner;

    public DateAdapter(Context context, List<DateObject> dates) {
        this.mContext = context;
        if (dates != null)
            this.dates = dates;
    }

    @Override
    public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_calendar_item, parent, false);
        DateViewHolder viewHolder = new DateViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DateViewHolder holder, int position) {
        //此处设置不复用 防止选中状态以及月标状态错乱
        holder.setIsRecyclable(false);

        holder.position = position;
        DateObject date = dates.get(position);
        holder.day.setText(dates.get(position).day + "");
        holder.root.setOnClickListener(this);
        holder.root.setTag(holder);

        //设置月初
        if (date.day == 1) {
            if (date.currentMonth)
                holder.month.setText((date.month) + "月");
            else
                holder.month.setText((date.month + 1) + "月");

            //多重保险
            if (holder.day.getText().toString().trim().equals("1"))
                holder.month.setVisibility(View.VISIBLE);
        }

        //设置当天
        if (date.year == CalendarView.TODAY.year
                && date.month == CalendarView.TODAY.month
                && date.day == CalendarView.TODAY.day) {
            holder.flag.setVisibility(View.VISIBLE);
        }

        //设置当月高亮
        if (!date.currentMonth) {
            holder.root.setAlpha(0.3f);
        } else {
            holder.day.setTextColor(Color.BLACK);
        }

        //设置选中状态
        setSellectStatus(position, holder);

    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    /**
     * 获取日期详情
     */
    public DateObject getDateObjectWithPosition(int position) {
        return this.dates.get(position);
    }

    public void setDates(List dates, int pagerIndex, boolean reset) {
        if (dates == null)
            return;

        if (reset) {
            this.dates.clear();
        }
        this.dates.addAll(dates);
        this.notifyDataSetChanged();

        //查询当前pagerindex是否存入数据库
        List<DateObject> result = ProviderMannager.query(mContext, pagerIndex, true);
        if (result.size() == 0) {
            Log.d("amtf", "date query not exits !!");
            for (DateObject date : (List<DateObject>) dates) {
                ProviderMannager.insert(mContext, date);
            }
        } else {
            Log.d("amtf", "date query exits !!");
        }
    }

    /**
     * 根据前后的选中状态判断图片的状态
     *
     * @param holder   holder操作ui
     * @param position 游标
     */
    private void setSellectStatus(int position, DateViewHolder holder) {
        DateObject date = dates.get(position);
        //Log.e("position:"+position+"\tsellect status:"+date.sellectStatus);
        if (!date.currentMonth)
            return;

        ImageView img = holder.sellect;
        TextView day = holder.day;

        if (date.sellectStatus) {
            day.setTextColor(Color.WHITE);
        } else {
            day.setTextColor(Color.BLACK);
        }

        if (position == 0) {
            if (date.sellectStatus && dates.get(position + 1).sellectStatus)
                img.setImageResource(R.drawable.calendar_sellector_left);
            else if (date.sellectStatus && !dates.get(position + 1).sellectStatus)
                img.setImageResource(R.drawable.calendar_sellector_single);

            return;
        }

        if (position == 34) {
            if (date.sellectStatus && dates.get(position - 1).sellectStatus)
                img.setImageResource(R.drawable.calendar_sellector_right);
            else if (date.sellectStatus && !dates.get(position - 1).sellectStatus)
                img.setImageResource(R.drawable.calendar_sellector_single);

            return;
        }

        DateObject dateLeft = dates.get(position - 1);
        DateObject dateRight = dates.get(position + 1);

        if (dateLeft.sellectStatus && date.sellectStatus && dateRight.sellectStatus) {
            img.setImageResource(R.drawable.calendar_sellector_center);
        }

        if (!dateLeft.sellectStatus && date.sellectStatus && dateRight.sellectStatus) {
            img.setImageResource(R.drawable.calendar_sellector_left);
        }

        if (dateLeft.sellectStatus && !date.sellectStatus && dateRight.sellectStatus) {
            img.setImageResource(0);
        }

        if (dateLeft.sellectStatus && date.sellectStatus && !dateRight.sellectStatus) {
            img.setImageResource(R.drawable.calendar_sellector_right);
        }

        if (!dateLeft.sellectStatus && date.sellectStatus && !dateRight.sellectStatus) {
            img.setImageResource(R.drawable.calendar_sellector_single);
        }
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout root;
        public ImageView sellect;
        public TextView flag;
        public TextView day;
        public TextView month;
        public int position;

        public DateViewHolder(View view) {
            super(view);
            position = 0;
            root = (RelativeLayout) view.findViewById(R.id.calendar_item_root);
            sellect = (ImageView) view.findViewById(R.id.calendar_item_sellect);
            flag = (TextView) view.findViewById(R.id.calendar_item_today);
            day = (TextView) view.findViewById(R.id.calendar_item_text_day);
            month = (TextView) view.findViewById(R.id.calendar_item_text_month);
        }
    }

    /**
     * 状态变更
     */
    @Override
    public void onClick(View view) {
        if (view instanceof RelativeLayout) {
            DateViewHolder holder = (DateViewHolder) view.getTag();
            DateObject date = dates.get(holder.position);
            if (!date.currentMonth)
                return;

            date.sellectStatus = !date.sellectStatus;

            this.notifyDataSetChanged();
            onCalendarDayClickListner.onClick(holder.position);

            // 更新数据库当前日期选中状态
            // date._id == -1即代表当前点击的date对象
            // query的未查询到有效的数据表id此时弃置不做更新处理
            if (date._id != -1) {
                ProviderMannager.update(mContext, date._id, date.sellectStatus);
            }
        }
    }

    public void setOnCalendarDayClickListner(onCalendarDayClick onCalendarDayClickListner) {
        this.onCalendarDayClickListner = onCalendarDayClickListner;
    }

    public interface onCalendarDayClick {

        /**
         * 监听已内部响应选中状态的标示以及数据库相关的操作
         * 外部接口监听只需要实现点击之后的逻辑不必关心内部
         */
        void onClick(int position);
    }
}
