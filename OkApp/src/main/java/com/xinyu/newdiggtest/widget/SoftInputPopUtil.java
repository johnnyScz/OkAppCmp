package com.xinyu.newdiggtest.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.utils.CommonUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SoftInputPopUtil {

    public static View commentView = null;
    public static PopupWindow commentPopup = null;
    public static String result = "";
    public static liveCommentResult liveCommentResult = null;
    public static EditText popup_live_comment_edit;
    public static TextView popup_live_comment_send;

    public static void liveCommentEdit(final Activity context, View view, final liveCommentResult commentResult) {
        liveCommentResult = commentResult;
        if (commentView == null) {
            commentView = context.getLayoutInflater().inflate(R.layout.pop_comment_input, null);
        }
        if (commentPopup == null) {
            // 创建一个PopuWidow对象
            commentPopup = new PopupWindow(commentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        // 设置动画 commentPopup.setAnimationStyle(R.style.popWindow_animation_in2out);
        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        commentPopup.setFocusable(true);
        // 设置允许在外点击消失
        commentPopup.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        commentPopup.setBackgroundDrawable(new BitmapDrawable());
        //必须加这两行，不然不会显示在键盘上方
        commentPopup.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        commentPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // PopupWindow的显示及位置设置
        commentPopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        popup_live_comment_edit = (EditText) commentView.findViewById(R.id.popup_live_comment_edit);
        popup_live_comment_send = (TextView) commentView.findViewById(R.id.popup_live_comment_send);


        popup_live_comment_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    popup_live_comment_send.setBackgroundResource(R.drawable.shaper_kuang);
                } else {
                    popup_live_comment_send.setBackgroundResource(R.drawable.shaper_green);
                }

            }
        });


//这是布局中发送按钮的监听
        popup_live_comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = popup_live_comment_edit.getText().toString().trim();
                if (liveCommentResult != null && result.length() != 0) {
                    //把数据传出去
                    liveCommentResult.onResult(true, result);
                    //关闭popup
                    commentPopup.dismiss();
                }
            }
        });
        //设置popup关闭时要做的操作
        commentPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(popup_live_comment_edit.getWindowToken(), 0);
                popup_live_comment_edit.setText("");
            }
        });
        //显示软键盘
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                CommonUtils.showSoftInput(context, commentView);

            }
        }, 200);
    }

    /**
     * 发送评论回调
     */
    public interface liveCommentResult {
        void onResult(boolean confirmed, String comment);
    }


}
