package com.xinyu.newdiggtest.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;


import com.ahao.basetreeview.model.TreeNode;
import com.ahao.basetreeview.util.TreeDataUtils;

import com.xinyu.newdiggtest.R;

import com.xinyu.newdiggtest.adapter.tree.DataSource;

import com.xinyu.newdiggtest.adapter.tree.MySingleLayoutTreeAdapter;

import com.xinyu.newdiggtest.adapter.tree.NoteItem;


import java.util.ArrayList;
import java.util.List;

public class SingleLayoutTreeActivity extends AppCompatActivity {

    private final static String TAG = "TreeActivity";
    RecyclerView recyclerView;

    MySingleLayoutTreeAdapter adapter;

    private List<TreeNode<NoteItem>> dataToBind = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expand1);
        initUI();
        initData();
        initEvent();
    }

    private void initEvent() {
        recyclerView.setAdapter(adapter);

        adapter.setOnTreeClickedListener(new MySingleLayoutTreeAdapter.OnTreeClickedListener<NoteItem>() {
            @Override
            public void onNodeClicked(View view, TreeNode<NoteItem> node, int position) {
                ImageView icon = view.findViewById(R.id.ivIcon);
                if (node.isExpand()) {
                    icon.setImageResource(R.drawable.ic_right);
                } else {
                    icon.setImageResource(R.drawable.arrow_b);
                }
            }

            @Override
            public void onLeafClicked(View view, TreeNode<NoteItem> node, int position) {

            }
        });


    }

    private void initData() {
        dataToBind.clear();
        dataToBind.addAll(TreeDataUtils.convertDataToTreeNode(DataSource.getFiles()));
        adapter = new MySingleLayoutTreeAdapter(R.layout.item_tree_zhang, dataToBind);
    }

    private void initUI() {
        recyclerView = findViewById(R.id.listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


}
