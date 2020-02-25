package com.xinyu.newdiggtest.ui.Digg.fragment;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;

import butterknife.OnClick;

public class FineGoWhereeActivity extends BaseNoEventActivity {


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_gowhere;
    }


    @OnClick(R.id.icon_back)
    public void goFinish() {
        finish();
    }


    @OnClick(R.id.i_know)
    public void goBaxk() {
        finish();
    }


}




