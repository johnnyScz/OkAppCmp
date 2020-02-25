package com.xinyu.newdiggtest.ui.Digg.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.TodoUserBean;
import com.xinyu.newdiggtest.bigq.ToDoActivity;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.DisplayUtils;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 与我相关待办
 */
public class RelationTodoActivity extends BaseNoEventActivity {


    @BindView(R.id.title)
    public TextView title;

    @BindView(R.id.add)
    public View more;


    PopupWindow addPop;

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_order_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String name = getIntent().getStringExtra("userName");
        title.setText(name);
        initAddPicturePop();
        initFragment();
    }

    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frag, RelationTodoFragment.newInstance(getIntent().getStringExtra("userId")));
        transaction.commit();//一定要提交
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    private TodoUserBean getUser() {

        TodoUserBean user = new TodoUserBean();
        user.setUser_id(getIntent().getStringExtra("userId"));
        user.setNickname(getIntent().getStringExtra("userName"));
        user.setHead(getIntent().getStringExtra("head"));

        return user;
    }


    private void initAddPicturePop() {

        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.pop_more, null);
//点击事件
        setPopListner(view);
        addPop = new PopupWindow(view, DisplayUtils.dp2px(this, 120),
                DisplayUtils.dp2px(this, 75));

        addPop.setTouchable(true);
        addPop.setOutsideTouchable(true);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!addPop.isShowing()) {
                    addPop.showAsDropDown(more);

                } else {
                    addPop.dismiss();
                }

            }
        });


    }

    private void setPopListner(LinearLayout view) {

        view.findViewById(R.id.create_todo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPop.dismiss();

                Intent intent = getIntent();
                intent.setClass(mContext, ToDoActivity.class);
                intent.putExtra("date", DateUtil.getCurrentData());

                TodoUserBean user = getUser();
                intent.putExtra("signer", user);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.look_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPop.dismiss();

                Intent intent=getIntent();
                intent.setClass(mContext,MyselfTreeActivity.class);
                startActivity(intent);

            }
        });


    }


}




