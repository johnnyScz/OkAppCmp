package com.xinyu.newdiggtest.ui.Digg.target;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.Digg.base.BaseFragment;
import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.MyTextUtil;

/**
 * 每周固定
 */
public class EveryMonthFixedFragment extends BaseFragment {

    LinearLayout ll;

    String selectWeeks = "";
    String[] splis;

    @Override
    public void initView(View view) {
        AppContacts.SELECT_WEEKS.clear();
        ll = view.findViewById(R.id.ll_checkboxs);
        int len = ll.getChildCount();

        if (MyTextUtil.isEmpty(selectWeeks)) {
            initList();
        }
        for (int i = 0; i < len; i++) {
            CheckBox checkBox = (CheckBox) ll.getChildAt(i);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String text = buttonView.getText().toString();
                    if (isChecked) {
                        if (!AppContacts.SELECT_WEEKS.contains(text))
                            AppContacts.SELECT_WEEKS.add(text);
                    } else {
                        if (AppContacts.SELECT_WEEKS.contains(text)) {
                            AppContacts.SELECT_WEEKS.remove(text);
                        }
                    }

                }
            });
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (splis == null || splis.length < 1)
            return;
        checkSeclect(splis);
    }


    public void setSelectWeeks(String weekStr) {
        selectWeeks = weekStr;
        if (MyTextUtil.isEmpty(selectWeeks))
            return;
        splis = selectWeeks.split(",");
    }

    private void checkSeclect(String[] splis) {
        int len = ll.getChildCount();
        for (int i = 0; i < len; i++) {
            CheckBox checkBox = (CheckBox) ll.getChildAt(i);
            checkBox.setChecked(false);
        }

        AppContacts.SELECT_WEEKS.clear();
        for (String week : splis) {
            AppContacts.SELECT_WEEKS.add(week);
            for (int i = 0; i < len; i++) {
                CheckBox checkBox = (CheckBox) ll.getChildAt(i);
                if (checkBox.getText().equals(convert(week))) {
                    checkBox.setChecked(true);
                }
            }
        }
    }

    private CharSequence convert(String week) {
        String weekStr = "";
        switch (week) {
            case "1":
                weekStr = "周一";
                break;

            case "2":
                weekStr = "周二";
                break;

            case "3":
                weekStr = "周三";
                break;
            case "4":
                weekStr = "周四";
                break;

            case "5":
                weekStr = "周五";
                break;

            case "6":
                weekStr = "周六";
                break;

            case "7":
                weekStr = "周日";
                break;
        }


        return weekStr;
    }


    private void initList() {
        AppContacts.SELECT_WEEKS.add("周一");
        AppContacts.SELECT_WEEKS.add("周二");
        AppContacts.SELECT_WEEKS.add("周三");
        AppContacts.SELECT_WEEKS.add("周四");
        AppContacts.SELECT_WEEKS.add("周五");
        AppContacts.SELECT_WEEKS.add("周六");
        AppContacts.SELECT_WEEKS.add("周日");

    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_month_fixed;
    }

    @Override
    public void initData() {

    }
}
