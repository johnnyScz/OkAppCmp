package com.xinyu.newdiggtest.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;

public class CallDialog {


    private Activity mActivity;
    private AlertDialog mAlertDialog;
    private TextView mTvCancel;
    private TextView audioTx;//音频
    private TextView mTvVedio;


    public CallDialog(Activity activity) {
        mActivity = activity;
        initDialog();
    }

    private void initDialog() {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(mActivity).create();
        }
        mAlertDialog.setCancelable(true);
        //必须先show
        mAlertDialog.show();
        //添加对话框自定义布局
        mAlertDialog.setContentView(R.layout.dialog_call);
        //获取对话框窗口
        Window window = mAlertDialog.getWindow();
        //设置显示窗口的宽高
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置窗口显示位置
        window.setGravity(Gravity.BOTTOM);
        //设置透明(解决5.0以上dialog不能全屏问题)
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //控件
        initView(window);
        //事件
        initEvent();
    }

    private void initView(Window window) {
        mTvCancel = window.findViewById(R.id.tv_dialog_photo_cancel);
        audioTx = window.findViewById(R.id.tv_dialog_audio);
        mTvVedio = window.findViewById(R.id.tv_vedio);
    }

    private void initEvent() {
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
        audioTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //相册
                if (mAlertDialog != null) {
                    mAlertDialog.dismiss();
                }

                if (listner != null) {
                    listner.onAudioCall();
                }

            }
        });
        mTvVedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照
                if (mAlertDialog != null) {
                    mAlertDialog.dismiss();
                }

                if (listner != null) {
                    listner.onVedioCall();
                }

            }
        });
    }


    public void setCallListner(OnCallListner mlistner) {
        this.listner = mlistner;
    }


    OnCallListner listner;

    public interface OnCallListner {

        void onVedioCall();

        void onAudioCall();


    }


}
