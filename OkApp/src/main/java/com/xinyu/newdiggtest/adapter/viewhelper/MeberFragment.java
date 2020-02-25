//package com.xinyu.newdiggtest.adapter.viewhelper;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ExpandableListView;
//import android.widget.TextView;
//
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.xinyu.newdiggtest.R;
//import com.xinyu.newdiggtest.adapter.CatactSearchAdapter;
//import com.xinyu.newdiggtest.bean.AskRoomIdBean;
//import com.xinyu.newdiggtest.bean.ChildExpandBean;
//import com.xinyu.newdiggtest.bean.MapBean;
//
//import com.xinyu.newdiggtest.bean.MemberRetBean;
//import com.xinyu.newdiggtest.net.AppUrl;
//import com.xinyu.newdiggtest.net.RetrofitServiceManager;
//import com.xinyu.newdiggtest.ui.MySpaceActivity;
//import com.xinyu.newdiggtest.ui.XshellEvent;
//import com.xinyu.newdiggtest.ui.chat.ChatCompanyActivity;
//import com.xinyu.newdiggtest.utils.EventConts;
//import com.xinyu.newdiggtest.utils.MyTextUtil;
//import com.xinyu.newdiggtest.utils.PreferenceUtil;
//
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
//public class MeberFragment extends Fragment {
//
//
//    @BindView(R.id.search_list)
//    RecyclerView recyclerView;
//
//
//    @BindView(R.id.expanlist)
//    ExpandableListView listView;
//
//
//    ViewHelperUtil util;
//
//    @BindView(R.id.seach)
//    TextView seachTx;
//
//    @BindView(R.id.doserch)
//    View doserch;
//
//    @BindView(R.id.input)
//    EditText input;
//
//
//    List<MapBean> total;
//
//
//    List<MemberRetBean.MemberOutBean> totalChild = new ArrayList<>();
//
//    CatactSearchAdapter adapter;
//
//    Context mContext;
//
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        mContext = context;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        util = ViewHelperUtil.getInstance(mContext);
//        util.requestGroupDatas(mContext);
//        initView();
//    }
//
//    private void initView() {
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
//        adapter = new CatactSearchAdapter(R.layout.item_contact, totalChild);
//        recyclerView.setAdapter(adapter);
//
//
//        input.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence key, int start, int before, int count) {
//
//                if (!MyTextUtil.isEmpty(key.toString().trim())) {
//                    checkSearchKey(key);
//                } else {
//                    listView.setVisibility(View.VISIBLE);
//                    recyclerView.setVisibility(View.GONE);
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//
//                MemberRetBean.MemberOutBean data = (MemberRetBean.MemberOutBean) adapter.getData().get(position);
//
//                if (view.getId() == R.id.post_film) {
//
//                    Intent intent = new Intent(mContext, MySpaceActivity.class);
//                    intent.putExtra("userId", data.getUserinfo().getUser_id());
//                    startActivity(intent);
//
//
//                } else if (view.getId() == R.id.gochat) {
//                    getOrCreateRoodId(data);
//                }
//
//
//            }
//        });
//
//
//    }
//
//    private void checkSearchKey(CharSequence key) {
//
//        totalChild.clear();
//
//        for (MapBean item : total) {
//
//            List<MemberRetBean.MemberOutBean> data = item.getData();
//
//            if (data != null && data.size() > 0) {
//
//                for (MemberRetBean.MemberOutBean tt : data) {
//
//                    if (tt.getUserinfo() == null)
//                        continue;
//
//                    String nameTx = MyTextUtil.isEmpty(tt.getUserinfo().getName()) ? tt.getUserinfo().getNickname() : tt.getUserinfo().getName();
//
//                    if (!MyTextUtil.isEmpty(nameTx) && nameTx.contains(key.toString())) {
//
//                        totalChild.add(tt);
//                    }
//
//
//                }
//            }
//        }
//
//        if (totalChild.size() > 0) {
//            listView.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.VISIBLE);
//
//            List<MemberRetBean.MemberOutBean> noCopy = getNocopy(totalChild);
//
//            adapter.setNewData(noCopy);
//
//        } else {
//            listView.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.GONE);
//        }
//
//
//    }
//
//
//    /**
//     * 数据去重
//     *
//     * @param totalChild
//     * @return
//     */
//    private List<MemberRetBean.MemberOutBean> getNocopy(List<MemberRetBean.MemberOutBean> totalChild) {
//
//        List<MemberRetBean.MemberOutBean> newData = new ArrayList<>();
//
//        for (MemberRetBean.MemberOutBean origin : totalChild) {
//
//            if (newData.size() < 1) {
//                newData.add(origin);
//                continue;
//            }
//
//
//            boolean isHave = false;
//
//            for (MemberRetBean.MemberOutBean hh : newData) {
//                if (hh.getUserinfo().getUser_id().equals(origin.getUserinfo().getUser_id())) {
//                    isHave = true;
//                }
//            }
//
//            if (!isHave) {
//                newData.add(origin);
//            }
//
//
//        }
//        return newData;
//    }
//
//    View rootVeiw;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        rootVeiw = inflater.inflate(R.layout.activity_expandlistview, null);
//        ButterKnife.bind(this, rootVeiw);
//
//        initView();
//
//        return rootVeiw;
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onXshellEvent(XshellEvent event) {
//
//
//        if (event.what == EventConts.Contact_Finish) {
//
//            total = util.getChildDatas();
//
//            List<ChildExpandBean> patent = util.getParentDatas();
//
////            Log.e("amtf", "收到eventBus事件:数据长度-->" + patent.size());
//
//            if (total != null && total.size() > 0) {
//
//                handleDatas(patent, total);
//            }
//
//        }
//    }
//
//    private void handleDatas(List<ChildExpandBean> patent, List<MapBean> total) {
//
//        List<List<MemberRetBean.MemberOutBean>> childList = new ArrayList<>();
//
//        for (ChildExpandBean pp : patent) {
//
//            for (MapBean child : total) {
//                if (child.getOrgId().equals(pp.getF_id())) {
//                    childList.add(child.getData());
//                }
//
//            }
//
//        }
//
//
//        MyExpandableListAdapter mAdapter = new MyExpandableListAdapter(patent, childList, mContext);
//
//        mAdapter.setChildListner(new MyExpandableListAdapter.ChatRoomListner() {
//            @Override
//            public void askRoom(MemberRetBean.MemberOutBean data) {
//
//                getOrCreateRoodId(data);
//            }
//
//            @Override
//            public void onHead(MemberRetBean.MemberOutBean data) {
//
//                Intent intent = new Intent(mContext, MySpaceActivity.class);
//                intent.putExtra("userId", data.getUserinfo().getUser_id());
//                startActivity(intent);
//            }
//        });
//
//        listView.setAdapter(mAdapter);
//
//        listView.setCacheColorHint(0x00000000);
//        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//        listView.setGroupIndicator(null);
//    }
//
//    @OnClick(R.id.seach)
//    public void goSearch() {
//        seachTx.setVisibility(View.GONE);
//        doserch.setVisibility(View.VISIBLE);
//
//    }
//
//
//    public void getOrCreateRoodId(MemberRetBean.MemberOutBean data) {
//
//        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
//        final AppUrl url = manager.create(AppUrl.class);
//        HashMap<String, String> map = new HashMap<>();
//        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
//
//        map.put("room_name", data.getUserinfo().getNickname());
//        map.put("room_head", data.getUserinfo().getHead());
//        map.put("friend_id", data.getUserinfo().getUser_id());
//
//        url.getOrCreateChatRoom(map).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<AskRoomIdBean>() {
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
//                    public void onNext(AskRoomIdBean msg) {
//
//                        if (msg.getOp().getCode().equals("Y")) {
//
//                            AskRoomIdBean.RoomRetBean data = msg.getData();
//
//                            if (data == null || MyTextUtil.isEmpty(data.getRoom_id())) {
//                                return;
//                            }
//
//                            Intent mIntent = new Intent(mContext, ChatCompanyActivity.class);
//                            mIntent.putExtra("room_id", data.getRoom_id());
//                            mIntent.putExtra("room_name", data.getRoom_name());
//                            mIntent.putExtra("room_type", "S");
//                            startActivity(mIntent);
//                        }
//
//                    }
//                });
//    }
//
//
//}
