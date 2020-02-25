package com.xinyu.newdiggtest.bigq;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.HomeReplyAdapter;
import com.xinyu.newdiggtest.adapter.TodoInfoAdapter;
import com.xinyu.newdiggtest.bean.RetListBean;
import com.xinyu.newdiggtest.bean.TodoRetBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 项目管理
 */
public class ProjectManagerActivity extends BaseNoEventActivity {


    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.create_date)
    TextView create_date;

    @BindView(R.id.end_date)
    TextView end_date;

    @BindView(R.id.tod_title)
    TextView tod_title;


    @BindView(R.id.jiaoban)
    View mLl;


    TodoInfoAdapter joinAdapter;


    HomeReplyAdapter replyAdapter;


    @BindView(R.id.recyclerView_todo)
    RecyclerView recyclerViewTodo;


    @BindView(R.id.reply_cylce)
    RecyclerView reply_cylce;


    String todoId;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_projct_info;
    }


    private void initRecycle() {

        LinearLayoutManager hoziLayountManager = new LinearLayoutManager(mContext);
        hoziLayountManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewTodo.setLayoutManager(hoziLayountManager);//给RecyclerView设置适配器

        List<RetListBean.InvitesBean> todoDatas = new ArrayList<>();
        joinAdapter = new TodoInfoAdapter(R.layout.item_info_member, todoDatas);
        recyclerViewTodo.setAdapter(joinAdapter);


        final List<RetListBean.InvitesBean> replyList = new ArrayList<>();

        reply_cylce.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        replyAdapter = new HomeReplyAdapter(R.layout.item_reply, replyList);
        reply_cylce.setAdapter(replyAdapter);

        replyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                RetListBean.InvitesBean mdt = replyList.get(position);

                if (mdt.getOwnermap().getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                    Intent mIntent = new Intent(mContext, AddDoneActivity.class);
                    String ftodoId = replyList.get(position).getF_id();
                    mIntent.putExtra("todoId", ftodoId);
                    mIntent.putExtra("enter_type", "1");//编辑
                    mIntent.putExtra("oldContent", replyList.get(position).getF_title());
                    mContext.startActivity(mIntent);
                }
            }
        });


    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        requestToDoInfo();
    }

    private void initView() {

        todoId = getIntent().getStringExtra("todoId");

        initRecycle();
    }

    public void requestToDoInfo() {
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("f_id", todoId);
        url.getDodoInById(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TodoRetBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(TodoRetBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() != null && msg.getData().size() > 0) {
                                showData(msg.getData());
                            }
                        }
                    }
                });
    }


    List<RetListBean.InvitesBean> firstInvitList, firstCcData;

    private void showData(List<RetListBean> mData) {

        RetListBean tt = mData.get(0);

        name.setText("创建 ： " + tt.getCreate_name().getNickname());
        time.setText(nosecond(tt.getF_start_date()));


        String dateStr = MyTextUtil.isEmpty(tt.getF_todo_date()) ? tt.getF_create_date() : tt.getF_todo_date();
        create_date.setText(dateStr);


        end_date.setText(DateUtil.getDotDayNoHour(tt.getF_end_date()));

        tod_title.setText(tt.getF_title());

        List<RetListBean.InvitesBean> invitList = tt.getInvites();

        firstInvitList = new ArrayList<>();


        if (invitList == null || invitList.size() < 1) {

        } else {
            for (RetListBean.InvitesBean kk : invitList) {
                firstInvitList.add(kk);
            }

            joinAdapter.setNewData(invitList);

        }


        if (tt.getNotes() != null && tt.getNotes().size() > 0) {
            mLl.setVisibility(View.VISIBLE);

            replyAdapter.setNewData(tt.getNotes());

        } else {
            mLl.setVisibility(View.GONE);
        }


    }


    private String nosecond(String f_start_date) {
        if (MyTextUtil.isEmpty(f_start_date) || f_start_date.length() < 4) {
            return "";
        }
        int len = f_start_date.length();
        return f_start_date.substring(0, len - 3);
    }


    @OnClick(R.id.iv_back)
    public void goCommit() {
        finish();
    }


}
