package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class Public2PrivateActivity extends BaseNoEventActivity {


    @BindView(R.id.inco_person)
    ImageView imgPub;

    @BindView(R.id.inco_pirate)
    ImageView imgPri;

    String share = "0";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }


    public void setImgBg() {
        if (share.equals("1")) {
            imgPub.setVisibility(View.VISIBLE);
            imgPri.setVisibility(View.INVISIBLE);
            imgPub.setImageResource(R.mipmap.icon_succed);

        } else if (share.equals("2")) {
            imgPri.setVisibility(View.VISIBLE);
            imgPri.setImageResource(R.mipmap.icon_succed);
            imgPub.setVisibility(View.INVISIBLE);

        }


    }


    private void initView() {

        share = getIntent().getStringExtra("f_is_share");

        setImgBg();


    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_pub_private;
    }


    @OnClick(R.id.pub)
    public void goPu() {
        share = "1";
        setImgBg();
    }


    @OnClick(R.id.pri_ll)
    public void goPri() {
        share = "2";
        setImgBg();
    }

    @OnClick(R.id.commit)
    public void goback() {

        Intent mitent = new Intent();
        mitent.putExtra("f_is_share", share);
        setResult(0x55, mitent);
        finish();

    }


}




