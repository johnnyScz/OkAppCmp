package com.xinyu.newdiggtest.adapter.viewhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xinyu.newdiggtest.R;
;

import com.xinyu.newdiggtest.adapter.RoomMemberBean;
import com.xinyu.newdiggtest.adapter.TotalMemberAdapter;
import com.xinyu.newdiggtest.bean.ChildExpandBean;

import com.xinyu.newdiggtest.bean.GroupMemberBean;
import com.xinyu.newdiggtest.bean.MemberBean;
import com.xinyu.newdiggtest.bean.MemberRetBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.BaseBean;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.chat.MessageInfo;
import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TotalMemberActivity extends BaseNoEventActivity {


    @BindView(R.id.seach)
    TextView seachTx;


    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.btn_commit)
    TextView btn_commit;

    @BindView(R.id.doserch)
    View doserch;

    @BindView(R.id.input)
    EditText input;


    @BindView(R.id.listview)
    public RecyclerView recyclerView;


    @BindView(R.id.searchlist)
    public RecyclerView searchListView;


    TotalMemberAdapter adapter, searchAdaper;


    List<MemberRetBean.MemberOutBean> selectDatas = new ArrayList<>();

    List<MemberRetBean.MemberOutBean> reduceList;//原始的待删减的成员

    String type = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppContacts.SelectData.clear();

        //TODO 已选数据
        AppContacts.SelectData.addAll(AppContacts.intentData);

        initView();

    }

    private void initView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器


        if (getIntent().hasExtra("add_type")) {
            btn_commit.setVisibility(View.VISIBLE);
        } else {
            btn_commit.setVisibility(View.GONE);
        }


        if (getIntent().hasExtra("add_type")) {
            type = getIntent().getStringExtra("add_type");
            queryData(type);
        }

        if (type.equals("0")) {
            request1Level();
        }

        if (type.equals("-1")) {
            title.setText("删除成员");
        } else if (type.equals("1")) {
            title.setText("邀请成员");
        }


        initSelectView();

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence key, int start, int before, int count) {

                if (!MyTextUtil.isEmpty(key.toString().trim())) {
                    checkSearchKey(key);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    searchListView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void initSelectView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchListView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
        searchAdaper = new TotalMemberAdapter(R.layout.item_totalmember, selectDatas);

        searchListView.setAdapter(searchAdaper);

        searchAdaper.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MemberRetBean.MemberOutBean tt = (MemberRetBean.MemberOutBean) adapter.getData().get(position);
                boolean isSelect = tt.isSelect();
                tt.setSelect(!isSelect);
                adapter.notifyItemChanged(position);

                notifDatas(tt);
            }
        });


    }

    private void notifDatas(MemberRetBean.MemberOutBean dt) {

        if (dt.isSelect()) {

            if (getIntent().hasExtra("creatBy"))
                dt.getUserinfo().setCreater(getIntent().getStringExtra("creatBy"));

            AppContacts.SelectData.add(dt);
        } else {

            int len = AppContacts.SelectData.size();
            if (AppContacts.SelectData.size() > 0) {

                for (int i = len - 1; i >= 0; i--) {

                    if (AppContacts.SelectData.get(i).getUserinfo().getUser_id().equals(dt.getUserinfo().getUser_id())) {
                        AppContacts.SelectData.remove(i);
                    }

                }

            }


        }


    }

    private void checkSearchKey(CharSequence key) {

        selectDatas.clear();

        List<MemberRetBean.MemberOutBean> total = null;

        if (type.equals("-1")) {
            total = reduceList;
            reomveMeself(total);
        } else {

            if (adapter != null)
                total = adapter.getData();
        }

        if (total == null && total.size() < 1)
            return;

        for (MemberRetBean.MemberOutBean item : total) {
            MemberBean data = item.getUserinfo();
            if (data == null)
                continue;
            String name = MyTextUtil.isEmpty(data.getNickname()) ? data.getName() : data.getNickname();

            if (MyTextUtil.isEmpty(name)) {
                continue;
            }

            if (name.contains(key.toString())) {
                selectDatas.add(item);
            }
        }

        if (selectDatas.size() > 0) {
            recyclerView.setVisibility(View.GONE);
            searchListView.setVisibility(View.VISIBLE);

            if (type.equals("1")) {
                searchAdaper.setTag(1);
            }

            List<MemberRetBean.MemberOutBean> noCopy = getNocopy(selectDatas);

            searchAdaper.getData().clear();
            searchAdaper.getData().addAll(noCopy);
            searchAdaper.notifyDataSetChanged();

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            searchListView.setVisibility(View.GONE);
        }


    }

    /**
     * 删除人不能包括自己
     *
     * @param total
     */
    private void reomveMeself(List<MemberRetBean.MemberOutBean> total) {

        for (MemberRetBean.MemberOutBean dd : total) {
            if (dd.getUserinfo().getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                total.remove(dd);
                break;
            }
        }


    }

    private List<MemberRetBean.MemberOutBean> getNocopy(List<MemberRetBean.MemberOutBean> selectDatas) {

        List<MemberRetBean.MemberOutBean> newData = new ArrayList<>();

        for (MemberRetBean.MemberOutBean origin : selectDatas) {

            if (newData.size() < 1) {
                newData.add(origin);
                continue;
            }

            boolean isHave = false;

            for (MemberRetBean.MemberOutBean hh : newData) {
                if (hh.getUserinfo().getUser_id().equals(origin.getUserinfo().getUser_id())) {
                    isHave = true;
                }
            }

            if (!isHave) {
                newData.add(origin);
            }


        }
        return newData;
    }


    public void requestMembers(String fid) {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());
        map.put("orgId", fid);

        url.queryLevel2Member(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MemberRetBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(MemberRetBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() != null && msg.getData().size() > 0) {
                                showDatas(msg.getData());

                            } else {
                                Log.e("amtf", "Total未请求到数据");
                            }


                        } else {
                            Log.e("amtf", "请求异常");
                        }


                    }
                });
    }

    /**
     * 显示数据
     *
     * @param data
     */
    private void showDatas(List<MemberRetBean.MemberOutBean> data) {


        List<MemberRetBean.MemberOutBean> res = checkDatas(data);


        if (type.equals("1")) {
            unAbleDatas(res);
        }

        adapter = new TotalMemberAdapter(R.layout.item_totalmember, res);
        if (type.equals("1")) {
            adapter.setTag(1);
        }


        recyclerView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MemberRetBean.MemberOutBean tt = (MemberRetBean.MemberOutBean) adapter.getData().get(position);

                boolean isSelect = tt.isSelect();
                tt.setSelect(!isSelect);
                adapter.notifyItemChanged(position);
                notifDatas(tt);
            }
        });

    }

    private void unAbleDatas(List<MemberRetBean.MemberOutBean> res) {

        if (areadyIn != null && areadyIn.size() > 0) {

            for (RoomMemberBean target : areadyIn) {

                for (MemberRetBean.MemberOutBean handData : res) {
                    if (handData.getUserinfo().getUser_id().equals(target.getUser().getUser_id())) {
                        handData.setIsUnable(1);
                        break;
                    }

                }
            }


        }

    }

    private List<MemberRetBean.MemberOutBean> checkDatas(List<MemberRetBean.MemberOutBean> data) {

        if (getIntent().hasExtra("aready")) {

            String ids = getIntent().getStringExtra("aready");

            String[] aready = ids.split(":");

            for (String id : aready) {

                for (MemberRetBean.MemberOutBean dt : data) {
                    if (dt.getUserinfo().getUser_id().equals(id)) {
                        dt.setSelect(true);
                        break;
                    }
                }
            }

        }


        return data;
    }


    private void request1Level() {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("companyId", PreferenceUtil.getInstance(mContext).getCompanyId());

        url.queryCompanyTotal(map).subscribeOn(Schedulers.io())
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

                        Gson gs = new Gson();
                        String strJson = gs.toJson(msg);
                        try {
                            JSONObject object = new JSONObject(strJson);

                            String code = object.getJSONObject("op").getString("code");
//
                            if (code.equals("Y")) {

                                Type type = new TypeToken<List<ChildExpandBean>>() {
                                }.getType();

                                List<ChildExpandBean> datalist = new Gson().fromJson(object.get("data").toString(), type);

                                if (datalist != null && datalist.size() > 0) {
                                    check(datalist);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("amtf", "error:" + e.getMessage());
                        }


                    }
                });
    }

    private void check(List<ChildExpandBean> datalist) {

        String orgId = "";
        for (ChildExpandBean item : datalist) {
            if (item.getF_name().equals("全部联系人")) {
                orgId = item.getF_id();
                break;
            }
        }
        requestMembers(orgId);
    }


    @OnClick(R.id.icon_back)
    public void goBack() {

        setIntentData();

        finish();
    }

    @Override
    public void onBackPressed() {
        setIntentData();
        super.onBackPressed();
    }

    private void setIntentData() {
        Intent intent = new Intent();

        setResult(RESULT_OK, intent);
    }


    @OnClick(R.id.seach)
    public void goSearch() {
        seachTx.setVisibility(View.GONE);
        doserch.setVisibility(View.VISIBLE);

    }


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_expand;
    }


    @OnClick(R.id.btn_commit)
    public void commit() {

        if (type.equals("-1")) {
            reduceMember();
        } else if (type.equals("1")) {
            addMember();
        }

    }

    List<RoomMemberBean> areadyIn;

    public void queryData(final String tagg) {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("room_id", getIntent().getStringExtra("room_id"));

        url.queryZuMembers(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GroupMemberBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(GroupMemberBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getMember_list() != null && msg.getMember_list().size() > 0) {

                                if (tagg.equals("-1")) {
                                    handleDatas(msg.getMember_list());
                                } else if (tagg.equals("1")) {
                                    areadyIn = msg.getMember_list();
                                    request1Level();
                                }

                            } else {
                                ToastUtils.getInstanc(mContext).showToast("没有数据！");
                            }

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });
    }


    private void handleDatas(List<RoomMemberBean> member_list) {

        reduceList = convertRoom(member_list);

        for (MemberRetBean.MemberOutBean item : reduceList) {
            if (item.getUserinfo().getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                reduceList.remove(item);
                break;
            }

        }


        adapter = new TotalMemberAdapter(R.layout.item_totalmember, reduceList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MemberRetBean.MemberOutBean tt = (MemberRetBean.MemberOutBean) adapter.getData().get(position);

                boolean isSelect = tt.isSelect();
                tt.setSelect(!isSelect);
                adapter.notifyItemChanged(position);
                notifDatas(tt);
            }
        });

    }


    private List<MemberRetBean.MemberOutBean> convertRoom(List<RoomMemberBean> member_list) {
        List<MemberRetBean.MemberOutBean> dats = new ArrayList<>();
        for (RoomMemberBean item : member_list) {
            MemberRetBean.MemberOutBean kk = creat(item);
            dats.add(kk);
        }

        return dats;
    }

    private MemberRetBean.MemberOutBean creat(RoomMemberBean item) {

        MemberRetBean.MemberOutBean bean = new MemberRetBean.MemberOutBean();

        bean.setSelect(false);

        String name = MyTextUtil.isEmpty(item.getUser().getName()) ? item.getUser().getNickname() : item.getUser().getName();
        MemberBean user = new MemberBean();
        user.setName(name);
        user.setHead(item.getUser().getHead());
        user.setUser_id(item.getUser().getUser_id());
        bean.setUserinfo(user);

        return bean;
    }


    /**
     * 删除好友
     */

    public void reduceMember() {

        String ids = getSelectUsers();

        if (MyTextUtil.isEmpty(ids)) {
            ToastUtils.getInstanc().showToast(" 请选择成员!");
            return;
        }


        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("room_id", getIntent().getStringExtra("room_id"));//roomId
        map.put("user_ids", ids);


        url.delectGroupMember(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean msg) {
                        loadingDailog.dismiss();

                        if (msg.getOp().getCode().equals("Y")) {
                            ToastUtils.getInstanc().showToast("删除成功!");


                            MessageInfo messageInfo = new MessageInfo();
                            messageInfo.setMsgType("999");//通知刷新
                            EventBus.getDefault().post(messageInfo);

                            finish();
                        }
                    }
                });
    }

    private String getSelectUsers() {

        List<MemberRetBean.MemberOutBean> selectDatas = new ArrayList<>();

        List<MemberRetBean.MemberOutBean> data = adapter.getData();

        if (data.size() > 0) {
            for (MemberRetBean.MemberOutBean item : data) {
                if (item.isSelect()) {
                    selectDatas.add(item);
                }
            }
        }

        List<MemberRetBean.MemberOutBean> search = searchAdaper.getData();

        if (search.size() > 0) {
            for (MemberRetBean.MemberOutBean kk : search) {
                if (kk.isSelect()) {
                    selectDatas.add(kk);
                }
            }
        }

        if (selectDatas.size() > 0) {

            List<MemberRetBean.MemberOutBean> noCopy = new ArrayList<>();


            for (MemberRetBean.MemberOutBean dt : selectDatas) {

                if (noCopy.size() < 1) {
                    noCopy.add(dt);
                    continue;
                }

                boolean isHave = false;

                for (MemberRetBean.MemberOutBean targe : noCopy) {
                    if (targe.getUserinfo().getUser_id().equals(dt.getUserinfo().getUser_id())) {
                        isHave = true;
                        break;
                    }
                }

                if (!isHave) {
                    noCopy.add(dt);
                }
            }

            StringBuffer buffer = new StringBuffer();

            for (MemberRetBean.MemberOutBean finDate : noCopy) {
                buffer.append(finDate.getUserinfo().getUser_id()).append(",");
            }
            return buffer.toString();
        }

        return "";
    }


    /**
     * 添加好友
     */

    public void addMember() {

        String ids = getSelectUsers();

        if (MyTextUtil.isEmpty(ids)) {
            ToastUtils.getInstanc().showToast(" 请选择成员!");
            return;
        }

        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("room_id", getIntent().getStringExtra("room_id"));//roomId
        map.put("user_ids", ids);
        map.put("member_type", "N");

        url.addGroupMember(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean msg) {
                        loadingDailog.dismiss();

                        if (msg.getOp().getCode().equals("Y")) {
                            ToastUtils.getInstanc().showToast("新增成员成功!");


                            MessageInfo messageInfo = new MessageInfo();
                            messageInfo.setMsgType("999");//通知刷新
                            EventBus.getDefault().post(messageInfo);


                            finish();
                        }
                    }
                });
    }


}
