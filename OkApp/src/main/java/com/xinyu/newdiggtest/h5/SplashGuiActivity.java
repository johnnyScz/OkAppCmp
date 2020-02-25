package com.xinyu.newdiggtest.h5;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.app.HomeAppActivity;
import com.xinyu.newdiggtest.ui.Digg.login.AppLoginActivity;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

/**
 * 新版首页
 *
 * @author zzy
 */
public class SplashGuiActivity extends AppCompatActivity {

    Handler mhandler = new Handler();

    Context mctx;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xinyusoft_activity_splash2);
        mctx = this;

        initView();
    }

    private void initView() {
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MyTextUtil.isEmpty(PreferenceUtil.getInstance(mctx).getUserId())) {
                    startActivity(new Intent(mctx, AppLoginActivity.class));
                } else {
                    startActivity(new Intent(mctx, HomeAppActivity.class));
                }
                finish();
            }
        }, 2000);


    }
}
