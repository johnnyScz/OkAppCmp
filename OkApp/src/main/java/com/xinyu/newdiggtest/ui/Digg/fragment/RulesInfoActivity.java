package com.xinyu.newdiggtest.ui.Digg.fragment;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;

import butterknife.OnClick;

/**
 * 规则说明
 */
public class RulesInfoActivity extends BaseNoEventActivity {


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_rules;
    }


    @OnClick(R.id.icon_back)
    public void goBack() {

        finish();
    }


}




