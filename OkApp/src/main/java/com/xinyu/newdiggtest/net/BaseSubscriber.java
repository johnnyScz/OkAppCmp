package com.xinyu.newdiggtest.net;

import android.content.Context;
import android.util.Log;

import com.android.tu.loadingdialog.LoadingDailog;
import com.xinyu.newdiggtest.utils.DialogUtil;

import rx.Subscriber;

/**
 * 默认弹出关闭dialog
 * @param <T>
 */
public abstract class BaseSubscriber<T> extends Subscriber<T>{
    LoadingDailog dialog;
    public BaseSubscriber(Context context){
        dialog = new DialogUtil(context).buildDialog("加载中...");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (null != dialog) {
            dialog.show();
        }
    }

    @Override
    public void onCompleted() {
        if (null != dialog) {
            dialog.dismiss();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (null != dialog) {
            dialog.dismiss();
        }
        Log.e("amtf", "onError" + e.getMessage());
    }
}
