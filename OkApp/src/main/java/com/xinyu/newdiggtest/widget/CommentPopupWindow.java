package com.xinyu.newdiggtest.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;

import java.util.ArrayList;

/**
 * 朋友圈点赞评论的popupwindow
 *
 * @author wei.yi
 */
public class CommentPopupWindow extends PopupWindow implements OnClickListener {

    private TextView digBtn;
    private TextView commentBtn;
    private TextView dashang;

    // 实例化一个矩形
    private Rect mRect = new Rect();
    // 坐标的位置（x、y）
    private final int[] mLocation = new int[2];
    // 弹窗子类项选中时的监听
    private OnItemClickListener mItemClickListener;
    // 定义弹窗子类项列表
    private ArrayList<String> mActionItems = new ArrayList<String>();

    public void setmItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public ArrayList<String> getmActionItems() {
        return mActionItems;
    }

    public void setmActionItems(ArrayList<String> mActionItems) {
        this.mActionItems = mActionItems;
    }


    public CommentPopupWindow(Context context, String type, boolean isShow) {
        View view = LayoutInflater.from(context).inflate(R.layout.social_sns_popupwindow, null);
        View line = view.findViewById(R.id.line2);
        digBtn = (TextView) view.findViewById(R.id.digBtn);
        commentBtn = (TextView) view.findViewById(R.id.commentBtn);
        dashang = (TextView) view.findViewById(R.id.dashang);
        digBtn.setOnClickListener(this);
        commentBtn.setOnClickListener(this);
        dashang.setOnClickListener(this);
        this.setContentView(view);
        if (!isShow) {
            dashang.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            this.setWidth(DensityUtil.dip2px(context, 90));
            this.setHeight(DensityUtil.dip2px(context, 35));
        } else {
            dashang.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            this.setWidth(DensityUtil.dip2px(context, 130));
            this.setHeight(DensityUtil.dip2px(context, 35));
        }

        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.social_pop_anim);

        initItemData(type);
    }

    private void initItemData(String type) {
        if (type.equals("1")) {
            addAction("取消");
            addAction("打赏");
            addAction("评论");
        } else {
            addAction("赞");
            addAction("打赏");
            addAction("评论");
        }


    }

    public void showPopupWindow(View parent) {
        parent.getLocationOnScreen(mLocation);
        // 设置矩形的大小
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + parent.getWidth(), mLocation[1] + parent.getHeight());
        digBtn.setText(mActionItems.get(0));
        if (!this.isShowing()) {
            showAtLocation(parent, Gravity.NO_GRAVITY, mLocation[0] - this.getWidth()
                    , mLocation[1] - ((this.getHeight() - parent.getHeight()) / 2));
        } else {
            dismiss();
        }
    }

    public void setZanBtnText(String text) {
        digBtn.setText(text);
    }


    @Override
    public void onClick(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.digBtn:
                mItemClickListener.onItemClick(mActionItems.get(0));
                break;

            case R.id.dashang:
                mItemClickListener.onItemClick(mActionItems.get(1));
                break;

            case R.id.commentBtn:
                mItemClickListener.onItemClick(mActionItems.get(2));
                break;


            default:
                break;
        }
    }

    /**
     * 添加子类项
     */
    public void addAction(String action) {

        mActionItems.add(action);

    }

    /**
     * 功能描述：弹窗子类项按钮监听事件
     */
    public static interface OnItemClickListener {
        public void onItemClick(String item);
    }
}
