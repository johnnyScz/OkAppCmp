//package com.xinyu.newdiggtest.bigq;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.xinyu.newdiggtest.R;
//import com.xinyu.newdiggtest.adapter.InfoListAdapter;
//import com.xinyu.newdiggtest.adapter.viewhelper.TotalMemberActivity;
//import com.xinyu.newdiggtest.bean.MemberBean;
//import com.xinyu.newdiggtest.bean.MemberRetBean;
//import com.xinyu.newdiggtest.bean.RetListBean;
//import com.xinyu.newdiggtest.bean.TodoRetBean;
//import com.xinyu.newdiggtest.net.AppUrl;
//import com.xinyu.newdiggtest.net.RetrofitServiceManager;
//import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
//import com.xinyu.newdiggtest.utils.AppContacts;
//import com.xinyu.newdiggtest.utils.PreferenceUtil;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
//
//public class TodoInfoListActivity1 extends BaseNoEventActivity {
//
//
//    @BindView(R.id.rycycleview)
//    RecyclerView recyclerView;
//
//
//    @BindView(R.id.edit)
//    ImageView edit;
//
//
//    InfoListAdapter adapter;
//
//
//    String todoId;
//
//    Handler mhandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            if (msg.what == 0x99) {
//                finish();
//            } else if (msg.what == 0x88) {
//                requestToDoInfo();
//            }
//
//        }
//    };
//
//
//    @Override
//    protected int getLayoutResouce() {
//        return R.layout.activity_todo_info;
//    }
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initView();
//        requestToDoInfo();
//    }
//
//    private void initView() {
//
//        todoId = getIntent().getStringExtra("todoId");
//
//        String uuId = getIntent().getStringExtra("userId");
//
//        if (uuId.equals(PreferenceUtil.getInstance(mContext).getUserId())) {
//            edit.setVisibility(View.VISIBLE);
//        } else {
//            edit.setVisibility(View.GONE);
//        }
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
//
//    }
//
//    public void requestToDoInfo() {
//        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
//        AppUrl url = manager.create(AppUrl.class);
//        HashMap<String, String> map = new HashMap<>();
//
//        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
//        map.put("f_id", todoId);
//        url.getDodoInById(map).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<TodoRetBean>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                        Log.e("amtf", "服务onError:" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(TodoRetBean msg) {
//
//                        if (msg.getOp().getCode().equals("Y")) {
//
//                            if (msg.getData() != null && msg.getData().size() > 0) {
//                                showData(msg.getData());
//                            }
//                        }
//                    }
//                });
//    }
//
//
//    private void showData(List<RetListBean> data) {
//        adapter = new InfoListAdapter(R.layout.item_todo_info, data);
//
//        adapter.setHandler(mhandler);
//
//        adapter.setFid(todoId);
//        recyclerView.setAdapter(adapter);
//
//        adapter.setRightClickListner(new InfoListAdapter.OnRightClickListner() {
//            @Override
//            public void onAtClick(String fid) {
//
//                AppContacts.SelectData.clear();
//
//                Intent intent = new Intent(mContext, TotalMemberActivity.class);
//                intent.putExtra("aready", getStrId(1));//传当前已经选好的UerId
//                startActivityForResult(intent, 0x11);
//            }
//
//            @Override
//            public void onCcClick(String fid) {
//                AppContacts.SelectData.clear();
//
//                Intent intent = new Intent(mContext, TotalMemberActivity.class);
//                intent.putExtra("aready", getStrId(2));
//                startActivityForResult(intent, 0x21);
//            }
//
//        });
//
//
//    }
//
//    private String getStrId(int tag) {
//
//        AppContacts.intentData.clear();
//
//        String id = "";
//        List<RetListBean.InvitesBean> datas = null;
//        if (tag == 1) {
//            datas = adapter.getAtAdapter().getData();
//        } else if (tag == 2) {
//            datas = adapter.getCcAdapter().getData();
//        }
//
//        AppContacts.intentData.addAll(convert(datas));
//
//        if (datas != null && datas.size() > 0) {
//            StringBuffer buffer = new StringBuffer();
//            for (RetListBean.InvitesBean item : datas) {
//                buffer.append(item.getOwnermap().getUser_id()).append(":");
//            }
//
//            String temp = buffer.toString();
//
//            return temp.substring(0, temp.length() - 1);
//        }
//
//        return id;
//    }
//
//    private List<MemberRetBean.MemberOutBean> convert(List<RetListBean.InvitesBean> datas) {
//
//        List<MemberRetBean.MemberOutBean> res = new ArrayList<>();
//        for (RetListBean.InvitesBean tt : datas) {
//            MemberRetBean.MemberOutBean bean = new MemberRetBean.MemberOutBean();
//            MemberBean cc = new MemberBean();
//            cc.setUser_id(tt.getOwnermap().getUser_id());
//            cc.setNickname(tt.getOwnermap().getNickname());
//            cc.setHead(tt.getOwnermap().getHead());
//            bean.setUserinfo(cc);
//            res.add(bean);
//        }
//
//        return res;
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//
//            switch (requestCode) {
//
//                case 0x11:
//
//                    if (AppContacts.SelectData.size() < 1)
//                        return;
//
//                    adapter.setAtData(AppContacts.SelectData);
//
//                    break;
//
//                case 0x21:
//
//                    if (AppContacts.SelectData.size() < 1)
//                        return;
//
//                    adapter.setCcData(AppContacts.SelectData);
//                    break;
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//
//    @OnClick(R.id.iv_back)
//    public void goCommit() {
//        finish();
//    }
//
//
//    @OnClick(R.id.edit)
//    public void goEdit() {
//        Intent intent = new Intent(mContext, ToDoEditActivity.class);
//        intent.putExtra("todoId", todoId);
//        startActivity(intent);
//        finish();
//
//    }
//
//
//}
