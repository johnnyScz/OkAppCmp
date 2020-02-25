package com.xinyu.newdiggtest.utils;

import android.content.Context;

import com.android.tu.loadingdialog.LoadingDailog;
import com.xinyu.newdiggtest.R;

public class DialogUtil {

    Context context;

    public DialogUtil(Context ctx) {
        this.context = ctx;
    }


    private static DialogUtil instance;

    LoadingDailog mDialog;


    public synchronized LoadingDailog getInstance() {
        if (mDialog == null) {
            mDialog = buildDialog(context.getResources().getString(R.string.loading_dialog));
        }
        return mDialog;
    }


    public LoadingDailog buildDialog(String msg) {
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(context)
                .setMessage(msg)
                .setCancelable(true)
                .setCancelOutside(false);
        LoadingDailog dialog = loadBuilder.create();
        return dialog;
    }


}
