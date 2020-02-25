package com.xinyu.newdiggtest.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;


import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;

import io.reactivex.functions.Consumer;


public class TakePhotoUtil {

    final int REQUEST_TAKE_PHOTO_CODE = 1;
    Activity mctx;

    public TakePhotoUtil(Activity ctx) {
        mctx = ctx;
    }

    public void openCamera() {

        RxPermissions rxPermission = new RxPermissions(mctx);
        rxPermission.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            takePict();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 下次再次启动时，还会提示请求权限的对话框
                            Log.e("amtf", permission.name + " is denied. More info should be provided.用户拒绝了该权限，没有选中『不再询问』");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                        }
                    }
                });


    }

    private void takePict() {
        Uri mUri = null;


        String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "images" + File.separator;
        File file = new File(path, "test.jpg");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //步骤二：Android 7.0及以上获取文件 Uri
            mUri = FileProvider.getUriForFile(mctx, "com.xinyu.newdiggtest", file);
        } else {
            //步骤三：获取文件Uri
            mUri = Uri.fromFile(file);
        }
        //步骤四：调取系统拍照
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        mctx.startActivityForResult(intent, REQUEST_TAKE_PHOTO_CODE);
    }


}
