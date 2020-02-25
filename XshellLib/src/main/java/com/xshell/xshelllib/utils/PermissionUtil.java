package com.xshell.xshelllib.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2018/5/7.
 */

public class PermissionUtil {

    private static volatile PermissionUtil instance = null;

    public static final int MY_PERMISSIONS_CODE = 0x11;


    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
    };

    List<String> mPermissionList = new ArrayList<>();


    private PermissionUtil(Activity context) {


    }

    public static PermissionUtil getInstance(Activity mCtx) {
        if (instance == null) {
            synchronized (PermissionUtil.class) {
                if (instance == null) {
                    instance = new PermissionUtil(mCtx);
                }
            }
        }
        return instance;
    }

    public void checkPermission(Activity mCtx) {
        mPermissionList.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(mCtx, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                }
            }

            if (!mPermissionList.isEmpty()) {
                String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
                ActivityCompat.requestPermissions(mCtx, permissions, MY_PERMISSIONS_CODE);
            }
        }
    }


}
