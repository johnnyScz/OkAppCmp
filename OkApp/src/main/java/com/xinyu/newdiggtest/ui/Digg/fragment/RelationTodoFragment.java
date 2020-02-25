package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.HomeAppAdapter;
import com.xinyu.newdiggtest.bean.RetListBean;
import com.xinyu.newdiggtest.bean.TodoRetBean;
import com.xinyu.newdiggtest.bigq.AddDoneActivity;
import com.xinyu.newdiggtest.bigq.ProjectManagerActivity;
import com.xinyu.newdiggtest.bigq.TodoInfoListActivity;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.ProcessActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 与我相关待办
 */
public class RelationTodoFragment extends BaseFragment {


    String userId;//通过参数传递

    HomeAppAdapter appAdapter;

    @BindView(R.id.recyclerView)
    public RecyclerView recyView;


    @BindView(R.id.refreshLayout)
    public SmartRefreshLayout refreshLayout;


    @BindView(R.id.emputview)
    public View emptyView;


    List<RetListBean> datalist = new ArrayList<>();

    int pageCount = 1;


    String total = "0";

    final int EverySize = 15;


    public static RelationTodoFragment newInstance(String userId) {
        Bundle args = new Bundle();
        RelationTodoFragment fragment = new RelationTodoFragment();
        args.putString("userInfo", userId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

                if (pageCount * EverySize > Integer.parseInt(total)) {
                    ToastUtils.getInstanc().showToast("没有更多数据了！");
                    refreshLayout.finishLoadMore();
                    return;
                }

                pageCount++;

                isPush = false;

                quetyList();
            }
        });

        appAdapter = new HomeAppAdapter(datalist);
        appAdapter.setDisAbleCheck(1);

        recyView.setAdapter(appAdapter);


        appAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                int id = view.getId();

                switch (id) {

                    case R.id.rl_vote:

                        int type = appAdapter.getData().get(position).getVoteType();

                        if (type == 3) {
                            ToastUtils.getInstanc().showToast("请前往PC端体验完整版");
                        } else {
                            RetListBean mt = appAdapter.getData().get(position);
                            goProjectInfo(mt);
                            ToastUtils.getInstanc().showToast("请前往PC端体验完整版");
                        }
                        break;


                    case R.id.add_done:

                        RetListBean tt = appAdapter.getData().get(position);
                        goCreateHaveDone(tt);

                        break;

                    case R.id.rl_item1:

                        RetListBean data = appAdapter.getData().get(position);
                        Intent intent = new Intent(mContext, ProcessActivity.class);
                        intent.putExtra("process_id", data.getF_process_id());
                        intent.putExtra("f_type", data.getF_type());
                        intent.putExtra("f_title", data.getF_title());
                        intent.putExtra("f_finish_id", checkFinishId(data));
                        if (AppUtils.isCountReback(data)) {
                            intent.putExtra("count_back", "1");
                        }

                        int eventTy = data.getProcessType();

                        if (eventTy == 1) {
                            intent.putExtra("event_type", "1");//请假
                            startActivity(intent);

                        } else if (eventTy == 2) {
                            intent.putExtra("event_type", "2");//设备申请
                            startActivity(intent);

                        } else if (eventTy == 3) {//付款申请表
                            intent.putExtra("event_type", "3");
                            startActivity(intent);

                        } else if (eventTy == 4) {//推广费

                            intent.putExtra("event_type", "4");
                            startActivity(intent);

                        } else if (eventTy == 5) {//月度结算
                            intent.putExtra("event_type", "5");
                            startActivity(intent);
                        } else if (eventTy == 6) {//项目管理
                            goProjectInfo(data);
                        } else {
                            goInfo(data);
                        }

                        break;


                }
            }
        });


    }


    /**
     * 请求数据(需要做分页)
     */

    public void quetyList() {

        dialog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();

        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", userId);
        requsMap.put("current", pageCount + "");
        requsMap.put("size", EverySize + "");

        url.getSelfTodo(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TodoRetBean>() {
                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                        refreshLayout.finishLoadMore();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(TodoRetBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() == null || msg.getData().size() < 1) {

                            } else {

                                total = msg.getTotal();
                                List<RetListBean> datas = msg.getData();

                                for (RetListBean ev : datas) {
                                    if (ev.getF_type().equals("vote")) {
                                        ev.setItemType(1);
                                        ev.setVoteType(1);
                                    } else if (ev.getF_type().equals("enroll")) {
                                        ev.setItemType(1);
                                        ev.setVoteType(2);
                                    } else if (ev.getF_type().equals("conference")) {
                                        ev.setItemType(1);
                                        ev.setVoteType(3);
                                    } else if (isProcess(ev)) {
                                        ev.setItemType(2);
                                    } else {
                                        ev.setItemType(0);
                                    }
                                }

                                if (isPush) {
                                    datalist.clear();
                                }

                                datalist.addAll(datas);
                                appAdapter.notifyDataSetChanged();
                            }

                            if (datalist.size() > 0) {
                                showEmpty(false);
                            } else {
                                showEmpty(true);
                            }


                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });
    }


    /**
     * 调到待办详情
     *
     * @param data
     */
    private void goInfo(RetListBean data) {

        AppContacts.ToDOInfo = data;

        Intent mIntent = new Intent(mContext, TodoInfoListActivity.class);
        if (AppUtils.ifInviteNoAccept(data) == -1) {
            mIntent.putExtra("haveInvit", "-1");
        } else {
            mIntent.putExtra("haveInvit", "0");
        }

        mIntent.putExtra("creatBy", data.getF_create_by());

        mIntent.putExtra("todoId", data.getF_id());
        mIntent.putExtra("userId", data.getCreate_name().getUser_id());
        startActivity(mIntent);
    }


    private String checkFinishId(RetListBean data) {
        String finishId = "";

        if (data.getFinishes() != null && data.getFinishes().size() > 0) {

            List<RetListBean.InvitesBean> flishList = data.getFinishes();

            for (RetListBean.InvitesBean item : flishList) {
                if (item.getOwnermap().getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                    return item.getF_id();
                }
            }
        }

        return finishId;
    }


    private boolean isProcess(RetListBean ev) {//流程待办

        if (!MyTextUtil.isEmpty(ev.getF_process_id())) {
            return true;
        }

        return false;
    }

    /**
     * 显示与隐藏
     *
     * @param show
     */
    private void showEmpty(boolean show) {
        if (show) {
            emptyView.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == EventConts.SoCket_Push) {

            JSONObject json = event.object;

            checkJson(json);
        }
    }


    /**
     * 调到待办详情
     *
     * @param data
     */
    private void goProjectInfo(RetListBean data) {

        AppContacts.ToDOInfo = data;

        Intent mIntent = new Intent(mContext, ProjectManagerActivity.class);
        mIntent.putExtra("creatBy", data.getF_create_by());
        mIntent.putExtra("todoId", data.getF_id());
        mIntent.putExtra("userId", data.getCreate_name().getUser_id());
        startActivity(mIntent);
    }


    /**
     * 新增交办
     *
     * @param tt
     */
    private void goCreateHaveDone(RetListBean tt) {
        Intent mIntent = new Intent(mContext, AddDoneActivity.class);
        mIntent.putExtra("todoId", tt.getF_id());
        mIntent.putExtra("enter_type", "0");//新建
        startActivity(mIntent);
    }


    boolean isPush = false;

    private void checkJson(JSONObject child1) {

        try {

            String op = child1.getString("op");

            String type = child1.getString("type");

            if (type.equals("todo") && op.equals("create")) {
                pageCount = 1;
                isPush = true;
                quetyList();

            } else if (op.equals("delete")) {
                pageCount = 1;
                isPush = true;
                quetyList();
            }


            if (type.equals("todo") && op.equals("update")) {

                JSONObject object = child1.getJSONObject("data");
                RetListBean rest = JSON.parseObject(object.toString(), RetListBean.class);


                int len = appAdapter.getData().size();

                if (len < 0)
                    return;

                for (int i = 0; i < len; i++) {

                    if (appAdapter.getData().get(i).getF_id().equals(rest.getF_id())) {

                        appAdapter.getData().set(i, rest);
                        appAdapter.notifyItemChanged(i);

                        break;
                    }
                }
            }

            if (type.equals("todonote") && (op.equals("update") || op.equals("create"))) {
                pageCount = 1;
                isPush = true;
                quetyList();
            }

        } catch (JSONException e) {
            Log.e("amtf", "json异常：" + e.getMessage());

            e.printStackTrace();
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        quetyList();
    }

    @Override
    protected void initView() {

        userId = getArguments().getString("userInfo");

        initRecycle();

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void reLoadData() {

    }

    @Override
    protected int getResId() {
        return R.layout.fragment_relation_list;
    }
}




