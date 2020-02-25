package com.xinyu.newdiggtest.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.DashangDataBean;
import com.xinyu.newdiggtest.ui.App;

import java.util.List;

/**
 * Created by yiwei on 16/7/9.
 */
public class DakaDashangListView extends android.support.v7.widget.AppCompatTextView {


    private List<DashangDataBean> datas;

    public DakaDashangListView(Context context) {
        super(context);
    }

    public DakaDashangListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public DakaDashangListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {

    }

    public List<DashangDataBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DashangDataBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    public void notifyDataSetChanged() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (datas != null && datas.size() > 0) {
            //添加打赏图标
            builder.append(setImageSpan());
            DashangDataBean item = null;
            for (int i = 0; i < datas.size(); i++) {
                item = datas.get(i);
                if (item != null) {
                    builder.append(setClickableSpan(item.getNickname() + "打赏了" + item.getF_money() + "元", i));
                    if (i != datas.size() - 1) {
                        builder.append(", ");
                    }
                }
            }
        }

        setText(builder);
    }


    private SpannableString setImageSpan() {
        String text = "  ";
        SpannableString imgSpanText = new SpannableString(text);
        imgSpanText.setSpan(new ImageSpan(App.getContext(), R.mipmap.dashang, DynamicDrawableSpan.ALIGN_BASELINE),
                0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return imgSpanText;
    }

    @NonNull
    private SpannableString setClickableSpan(String textStr, final int position) {
        SpannableString subjectSpanText = new SpannableString(textStr);
//        subjectSpanText.setSpan(new SpannableClickable(itemColor) {
//                                    @Override
//                                    public void onClick(View widget) {
//                                        if (onItemClickListener != null) {
//                                            onItemClickListener.onClick(position);
//                                        }
//                                    }
//                                }, 0, subjectSpanText.length(),
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }
}
