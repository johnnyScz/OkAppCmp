package com.xinyu.newdiggtest.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public abstract class MyMiddleDialog extends Dialog {
    private Context context;

    public MyMiddleDialog(Context context) {
        super(context);
    }

    public MyMiddleDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(getView());
    }


    protected abstract View getView();
}