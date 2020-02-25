package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.IntentParams;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonSexEditActity extends BaseNoEventActivity {

    String sex;

    @BindView(R.id.girl_selet)
    ImageView girl_selet;

    @BindView(R.id.man_selet)
    ImageView man_selet;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_edit_sex;
    }

    private void initView() {

        sex = getIntent().getStringExtra(IntentParams.GROUP_NAME);

        if (sex.equals("男")) {
            resetSexqoto(1);
        } else {
            resetSexqoto(0);
        }


    }


    private void resetSexqoto(int type) {
        if (type == 1) {
            man_selet.setVisibility(View.VISIBLE);
            girl_selet.setVisibility(View.INVISIBLE);
        } else {
            girl_selet.setVisibility(View.VISIBLE);
            man_selet.setVisibility(View.INVISIBLE);

        }


    }


    @OnClick(R.id.ll_man)
    public void man() {
        sex = "男";
        resetSexqoto(1);
    }


    @OnClick(R.id.ll_girl)
    public void girl() {
        sex = "女";
        resetSexqoto(0);
    }


    @OnClick(R.id.tv_save)
    public void save() {
        Intent intent = new Intent();
        intent.putExtra("IntentData", sex);
        setResult(4, intent);
        finish();

    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }


}
