package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.UnreadAdapter;
import com.xinyu.newdiggtest.bean.RemarkBean;
import com.xinyu.newdiggtest.bean.UnreadMsgListBean;
import com.xinyu.newdiggtest.bigq.TodoInfoListActivity;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 未读消息列表
 */
public class NoreadMsgActivity extends BaseNoEventActivity {


    @BindView(R.id.recyclerView)
    public RecyclerView recyView;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_noread_msg;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecycle();
    }

    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
    }


    @Override
    protected void onResume() {
        super.onResume();

        quetyList();
    }

    public void quetyList() {
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        final NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("f_state", "0");
        map.put("size", "100");
        map.put("current", "1");

        url.queryMsgUnread(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UnreadMsgListBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(UnreadMsgListBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() == null || msg.getData().size() < 1) {
                                return;
                            }

                            showUi(msg.getData());

                        } else {
                            Log.e("amtf", "enterprise.msg.queryByOwner服务异常");
                        }

                    }
                });
    }


    /**
     * 显示未读消息
     *
     * @param data
     */
    private void showUi(final List<UnreadMsgListBean.MsgUnreadBean> data) {

        UnreadAdapter adapter = new UnreadAdapter(R.layout.img_common, data);

        recyView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                UnreadMsgListBean.MsgUnreadBean item = data.get(position);

                read(item.getF_id());

                goMsgInfo(item);
            }
        });


    }


    /**
     * 进入待办或者表单详情
     *
     * @param item
     */
    private void goMsgInfo(UnreadMsgListBean.MsgUnreadBean item) {

        String type = item.getF_type();

        if (type.equals("todo")) {

            Intent mIntent = new Intent(mContext, TodoInfoListActivity.class);
            mIntent.putExtra("haveInvit", "0");
            mIntent.putExtra("creatBy", item.getF_create_by());
            mIntent.putExtra("todoId", item.getF_type_id());
            mIntent.putExtra("userId", item.getF_create_by());
            startActivity(mIntent);


        } else if (type.equals("form")) {


            if (item.getF_remark() instanceof String) {
                return;
            }

            String object = item.getF_remark().toString();
            RemarkBean ffc = new Gson().fromJson(object, RemarkBean.class);
//
//
            Intent intent = new Intent(mContext, BiaoInfoActivity.class);
            intent.putExtra("f_plugin_type_id", ffc.getF_plugin_type_id());

            //TODO 看groupId怎么 取值
//            intent.putExtra("f_group_id", item.getF_group_id());
            intent.putExtra("f_title", item.getF_title());
            intent.putExtra("f_scope", ffc.getF_scope());

            startActivity(intent);


        }
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    public void read(String fid) {
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        final NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("f_id", fid);


        url.readMsg(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Object msg) {

                        //可以不处理


                    }
                });
    }

}




