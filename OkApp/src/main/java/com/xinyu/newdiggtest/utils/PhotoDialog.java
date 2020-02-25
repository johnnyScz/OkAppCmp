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

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.xinyu.newdiggtest.R;

public class PhotoDialog {


    int REQUEST_CODE_PHOTO_ALBUM = 1;

    private Activity mActivity;
    private AlertDialog mAlertDialog;
    private TextView mTvCancel;
    private TextView mTvAlbum;
    private TextView mTvCamera;


    public PhotoDialog(Activity activity) {
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
        mAlertDialog.setContentView(R.layout.dialog_photo);
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
        mTvAlbum = window.findViewById(R.id.tv_dialog_photo_album);
        mTvCamera = window.findViewById(R.id.tv_dialog_photo_camera);
    }

    private void initEvent() {
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
        mTvAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //相册
                PictureSelector.create(mActivity).openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.MULTIPLE)//单选模式
                        .maxSelectNum(9)// 最大图片选择数量 int
                        .compress(true)
                        .forResult(REQUEST_CODE_PHOTO_ALBUM);
                if (mAlertDialog != null) {
                    mAlertDialog.dismiss();
                }
            }
        });
        mTvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照
                PictureSelector.create(mActivity)
                        .openCamera(PictureMimeType.ofImage())
                        .compress(true)
                        .forResult(0);


                if (mAlertDialog != null) {
                    mAlertDialog.dismiss();
                }
            }
        });
    }


}
