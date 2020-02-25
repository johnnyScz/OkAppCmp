package com.xinyu.newdiggtest.adapter.viewhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.CatactSearchAdapter;
import com.xinyu.newdiggtest.bean.AskRoomIdBean;
import com.xinyu.newdiggtest.bean.MemberBean;
import com.xinyu.newdiggtest.bean.MemberRetBean;
import com.xinyu.newdiggtest.bean.WxUserBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.RelationTodoActivity;

import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.ui.chat.ChatCompanyActivity;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TreeActivity extends BaseNoEventActivity {


    @BindView(R.id.search_list)
    RecyclerView recyclerView;

    @BindView(R.id.recy_tree)
    RecyclerView recyTree;


    TreeHelperUtil1 util;

    @BindView(R.id.seach)
    TextView seachTx;

    @BindView(R.id.doserch)
    View doserch;

    @BindView(R.id.input)
    EditText input;


    List<WxUserBean> totalChild = new ArrayList<>();

    CatactSearchAdapter adapter;

    DemoAdapter baseAdapter;


    Map<String, List<MemberRetBean.MemberOutBean>> allDatas;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initView();

        EventBus.getDefault().register(this);
    }

    private void initData() {

        String cpnId = getIntent().getStringExtra("companyId");
        TreeHelperUtil1 util1 = TreeHelperUtil1.getInstance(mContext);
        util1.requestGroupDatas(mContext, cpnId);
    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_expandlistview;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void checkSearchKey(CharSequence key) {

        totalChild.clear();

//
        List<MemberRetBean.MemberOutBean> total = util.getTotolMembers();
        for (MemberRetBean.MemberOutBean user : total) {

            MemberBean item = user.getUserinfo();

            if (item == null)
                continue;

            String nameTx = item.getNickname();

            if (!MyTextUtil.isEmpty(nameTx) && nameTx.contains(key.toString())) {
                totalChild.add(convertBean(item));
            }
        }
        if (totalChild.size() > 0) {
            recyTree.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            List<WxUserBean> noCopy = getNocopy(totalChild);

            adapter.setNewData(noCopy);

        } else {
            recyTree.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private WxUserBean convertBean(MemberBean item) {

        WxUserBean data = new WxUserBean();
        data.setNickname(item.getNickname());
        data.setHead(item.getHead());
        data.setUser_id(item.getUser_id());

        return data;
    }


    /**
     * 数据去重
     *
     * @param totalChild
     * @return
     */
    private List<WxUserBean> getNocopy(List<WxUserBean> totalChild) {

        List<WxUserBean> newData = new ArrayList<>();

        for (WxUserBean origin : totalChild) {

            if (newData.size() < 1) {
                newData.add(origin);
                continue;
            }


            boolean isHave = false;

            for (WxUserBean hh : newData) {
                if (hh.getUser_id().equals(origin.getUser_id())) {
                    isHave = true;
                }
            }

            if (!isHave) {
                newData.add(origin);
            }
        }
        return newData;
    }


    protected void initView() {


        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(2000);
        animator.setRemoveDuration(2000);
        recyTree.setItemAnimator(animator);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyTree.setLayoutManager(linearLayoutManager);


        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
        adapter = new CatactSearchAdapter(R.layout.item_contact, totalChild);
        recyclerView.setAdapter(adapter);


        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence key, int start, int before, int count) {

                if (!MyTextUtil.isEmpty(key.toString().trim())) {
                    checkSearchKey(key);
                } else {
                    recyTree.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                WxUserBean data = (WxUserBean) adapter.getData().get(position);

                if (view.getId() == R.id.post_film) {


                    Intent mintent = new Intent(mContext, RelationTodoActivity.class);
                    mintent.putExtra("userId", data.getUser_id());
                    mintent.putExtra("userName", data.getNickname());
                    mintent.putExtra("head", data.getHead());

                    startActivity(mintent);


                } else if (view.getId() == R.id.gochat) {
                    getOrCreateRoodId(data);
                }


            }
        });


    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onXshellEvent(XshellEvent event) {

        if (event.what == EventConts.Contact_Finish) {
            util = TreeHelperUtil1.getInstance(mContext);
            allDatas = util.getMapList();

            List<TreeBean.TreeListBean> datas = util.getFirstLevelDatas();
            if (datas != null && datas.size() > 0) {
                handleData(datas);
            }


        }
    }


    @OnClick(R.id.seach)
    public void goSearch() {
        seachTx.setVisibility(View.GONE);
        doserch.setVisibility(View.VISIBLE);

    }


    public void getOrCreateRoodId(WxUserBean data) {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        final AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        map.put("room_name", data.getNickname());
        map.put("room_head", data.getHead());
        map.put("friend_id", data.getUser_id());
        map.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());
        url.getOrCreateChatRoom(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AskRoomIdBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(AskRoomIdBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            AskRoomIdBean.RoomRetBean data = msg.getData();

                            if (data == null || MyTextUtil.isEmpty(data.getRoom_id())) {
                                return;
                            }

                            Intent mIntent = new Intent(mContext, ChatCompanyActivity.class);
                            mIntent.putExtra("room_id", data.getRoom_id());
                            mIntent.putExtra("room_name", data.getRoom_name());
                            mIntent.putExtra("room_type", "S");
                            startActivity(mIntent);
                        }

                    }
                });
    }


    //处理数据
    private void handleData(List<TreeBean.TreeListBean> datas) {

        if (datas != null && datas.size() > 0) {

            List<DemoTreeData> kkkkk = new ArrayList<>();

            for (TreeBean.TreeListBean dt : datas) {

                DemoTreeData item = new DemoTreeData(1);
                item.setName(dt.getF_name());

                List<DemoTreeData> itemData = new ArrayList<>();

                List<MemberRetBean.MemberOutBean> self = getTheDatas(dt.getF_id());

                if (self != null && self.size() > 0) {

                    for (MemberRetBean.MemberOutBean lev2 : self) {

                        MemberBean user = lev2.getUserinfo();
                        if (user == null)
                            break;

                        DemoTreeData datet = new DemoTreeData(2);

                        datet.setName(user.getNickname());
                        datet.setUserId(user.getUser_id());
                        datet.setHead(user.getHead());

                        itemData.add(datet);

                    }
                }


                if (dt.getChild() != null && dt.getChild().size() > 0) {
                    List<TreeBean.TreeListBean.ChildBean> childList = dt.getChild();

                    for (TreeBean.TreeListBean.ChildBean cc : childList) {

                        itemData.add(getSecondLeve(cc));
                    }
                }

                item.setData(itemData);

                if (Integer.parseInt(dt.getEmpnum()) > 0) {
                    item.setHasChild(true);
                } else {
                    item.setHasChild(false);
                }

                kkkkk.add(item);
            }

            baseAdapter = new DemoAdapter(mContext, kkkkk);

            recyTree.setAdapter(baseAdapter);

        }

        baseAdapter.setMyListner(new DemoAdapter.OnChatClick() {
            @Override
            public void onChat(DemoTreeData data) {

                WxUserBean wd = convert(data);
                getOrCreateRoodId(wd);

            }
        });


    }

    private List<MemberRetBean.MemberOutBean> getTheDatas(String f_id) {

        for (Map.Entry entry : allDatas.entrySet()) {

            String key = (String) entry.getKey();
            if (key.equals(f_id)) {
                return (List<MemberRetBean.MemberOutBean>) entry.getValue();
            }

        }

        return null;
    }

    private WxUserBean convert(DemoTreeData data) {
        WxUserBean item = new WxUserBean();
        item.setHead(data.getHead());
        item.setNickname(data.getName());
        item.setUser_id(data.getUserId());
        return item;
    }

    private DemoTreeData getSecondLeve(TreeBean.TreeListBean.ChildBean cc) {

        DemoTreeData item = new DemoTreeData(2);
        item.setName(cc.getF_name());

        List<DemoTreeData> item3Data = new ArrayList<>();

        List<MemberRetBean.MemberOutBean> third = getTheDatas(cc.getF_id());

        if (third != null && third.size() > 0) {

            for (MemberRetBean.MemberOutBean it3 : third) {

                MemberBean user11 = it3.getUserinfo();

                if (user11 == null)
                    break;

                DemoTreeData datet = new DemoTreeData(3);
                datet.setName(user11.getNickname());
                datet.setUserId(user11.getUser_id());
                datet.setHead(user11.getHead());
                item3Data.add(datet);
            }

        }

        if (cc.getChild() != null && cc.getChild().size() > 0) {

            //TODO 暂时未处理
        }

        item.setData(item3Data);

        if (Integer.parseInt(cc.getEmpnum()) > 0) {
            item.setHasChild(true);
        } else {
            item.setHasChild(false);
        }

        return item;
    }


    @OnClick(R.id.back)
    public void goBack() {
        finish();
    }


}
