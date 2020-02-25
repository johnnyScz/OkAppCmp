package com.xinyu.newdiggtest.h5;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;


public class WebViewActivity extends BaseNoEventActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.img_layout;
    }

    private void initView() {

        ImageView img = this.findViewById(R.id.img);

        String imgUrl = getIntent().getStringExtra("path");

        Picasso.with(this).load(imgUrl).into(img);


        this.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
