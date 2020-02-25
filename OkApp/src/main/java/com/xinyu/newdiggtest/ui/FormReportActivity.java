package com.xinyu.newdiggtest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.FormAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FormReportActivity extends BaseNoEventActivity {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器


        List<Object> datas = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            datas.add(new Object());
        }

        FormAdapter adapter = new FormAdapter(R.layout.item_form, datas);

        recyclerView.setAdapter(adapter);

    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_form;
    }
}
