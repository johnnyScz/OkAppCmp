package com.xinyu.newdiggtest.bigq;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;


import com.xinyu.newdiggtest.adapter.ChaosongAdapter;
import com.xinyu.newdiggtest.adapter.HomeReplyAdapter;

import com.xinyu.newdiggtest.adapter.TodoInfoAdapter;
import com.xinyu.newdiggtest.adapter.viewhelper.TotalMemberActivity;

import com.xinyu.newdiggtest.bean.MemberBean;
import com.xinyu.newdiggtest.bean.MemberRetBean;
import com.xinyu.newdiggtest.bean.RetListBean;
import com.xinyu.newdiggtest.bean.TodoRetBean;
import com.xinyu.newdiggtest.bean.TodoUserBean;
import com.xinyu.newdiggtest.h5.WebViewActivity;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.CommBean;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TodoInfoListActivity extends BaseNoEventActivity {


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


    @BindView(R.id.eidt_view)
    LinearLayout eidt_view;

    @BindView(R.id.jiaoban)
    View mLl;


    @BindView(R.id.add_todo)
    ImageView icon1;

    @BindView(R.id.cc)
    ImageView icon2;


    TodoInfoAdapter joinAdapter;

    ChaosongAdapter chaoSongAdapter;

    FileAdapter adapter;

    HomeReplyAdapter replyAdapter;


    @BindView(R.id.edit)
    ImageView edit;

    @BindView(R.id.doculist)
    RecyclerView documentRecycleView;

    @BindView(R.id.recyclerView_todo)
    RecyclerView recyclerViewTodo;

    @BindView(R.id.cc_recycle)
    RecyclerView cc_recycle;


    @BindView(R.id.reply_cylce)
    RecyclerView reply_cylce;


    String todoId;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_todo_info;
    }


    private void initRecycle() {

        LinearLayoutManager hoziLayountManager = new LinearLayoutManager(mContext);
        hoziLayountManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewTodo.setLayoutManager(hoziLayountManager);//给RecyclerView设置适配器

        List<RetListBean.InvitesBean> todoDatas = new ArrayList<>();
        joinAdapter = new TodoInfoAdapter(R.layout.item_info_member, todoDatas);
        recyclerViewTodo.setAdapter(joinAdapter);


        LinearLayoutManager hoziLayountManager11 = new LinearLayoutManager(mContext);
        hoziLayountManager11.setOrientation(LinearLayoutManager.HORIZONTAL);
        cc_recycle.setLayoutManager(hoziLayountManager11);//给RecyclerView设置适配器

        List<RetListBean.InvitesBean> chsongDats = new ArrayList<>();
        chaoSongAdapter = new ChaosongAdapter(R.layout.item_chaosong, chsongDats);
        cc_recycle.setAdapter(chaoSongAdapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        documentRecycleView.setLayoutManager(layoutManager);//给RecyclerView设置适配器

        List<FileBean> fileList = new ArrayList<>();

        adapter = new FileAdapter(R.layout.item_file, fileList);
        documentRecycleView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                FileBean ddd = (FileBean) adapter.getData().get(position);

                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("path", ddd.getFileUrl());
                mContext.startActivity(intent);

            }
        });


        final List<RetListBean.InvitesBean> replyList = new ArrayList<>();

        reply_cylce.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        replyAdapter = new HomeReplyAdapter(R.layout.item_reply, replyList);
        reply_cylce.setAdapter(replyAdapter);

        //没有编辑

//        replyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//
//                RetListBean.InvitesBean mdt = replyList.get(position);
//
//                if (mdt.getOwnermap().getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
//                    Intent mIntent = new Intent(mContext, AddDoneActivity.class);
//                    String ftodoId = replyList.get(position).getF_id();
//                    mIntent.putExtra("todoId", ftodoId);
//                    mIntent.putExtra("enter_type", "1");//编辑
//                    mIntent.putExtra("oldContent", replyList.get(position).getF_title());
//                    mContext.startActivity(mIntent);
//                }
//            }
//        });


    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        requestToDoInfo();
    }

    private void initView() {

        todoId = getIntent().getStringExtra("todoId");

        String uuId = getIntent().getStringExtra("userId");

        String isInvit = getIntent().getStringExtra("haveInvit");

        //TODO 如果不是自己
        if (uuId.equals(PreferenceUtil.getInstance(mContext).getUserId())) {
            edit.setVisibility(View.VISIBLE);
        } else {
            edit.setVisibility(View.GONE);
        }

        if (isInvit.equals("-1")) {//只有是被待办人，但是没有接受邀办的，才不能at 人
            icon1.setVisibility(View.GONE);
            icon2.setVisibility(View.GONE);
        } else {
            icon1.setVisibility(View.VISIBLE);
            icon2.setVisibility(View.VISIBLE);
        }

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

        String endTime = MyTextUtil.isEmpty(tt.getF_finish_date()) ? tt.getF_end_date() : tt.getF_finish_date();

        if (!MyTextUtil.isEmpty(endTime) && endTime.length() > 10) {
            end_date.setText(endTime.substring(0, 10));
        }

        tod_title.setText(tt.getF_title());


        // 1.文件列表
        List<RetListBean.DocumentBean> documentList = tt.getAttachment();

        if (documentList != null && documentList.size() > 0) {
            adapter.setNewData(convertData(documentList));
        }

        //2 At 的人

        List<RetListBean.InvitesBean> invitList = tt.getInvites();

        firstInvitList = new ArrayList<>();


        if (invitList == null || invitList.size() < 1) {

        } else {
            for (RetListBean.InvitesBean kk : invitList) {
                firstInvitList.add(kk);
            }

            joinAdapter.setNewData(invitList);

        }


        List<RetListBean.InvitesBean> chognsongList = tt.getCcs();

        firstCcData = new ArrayList<>();


        if (chognsongList == null || chognsongList.size() < 1) {
        } else {

            for (RetListBean.InvitesBean rr : chognsongList) {
                firstCcData.add(rr);
            }

            chaoSongAdapter.setNewData(chognsongList);

        }

        if (tt.getNotes() != null && tt.getNotes().size() > 0) {
            mLl.setVisibility(View.VISIBLE);

            replyAdapter.setNewData(tt.getNotes());

        } else {
            mLl.setVisibility(View.GONE);
        }


    }


    private List<FileBean> convertData(List<RetListBean.DocumentBean> documentList) {

        List<FileBean> data = new ArrayList<>();

        for (RetListBean.DocumentBean tt : documentList) {
            FileBean bean = new FileBean();
            String dcc = tt.getF_type();

            bean.setDex(dcc);

            if (dcc.equals("png") || dcc.equals("jpg") || dcc.equals("jpeg")) {
                bean.setType(1);
            } else if (dcc.equals("pdf") || dcc.equals("pptx") || dcc.equals("ppt") || dcc.equals("txt") || dcc.equals("doc") || dcc.equals("docx") || dcc.equals("xls") || dcc.equals("xlsx")) {
                bean.setType(2);
            } else {
                bean.setType(3);
            }

            bean.setFileUrl(tt.getF_path());
            bean.setFname(tt.getF_title());


            data.add(bean);
        }


        return data;
    }


    private String nosecond(String f_start_date) {
        if (MyTextUtil.isEmpty(f_start_date) || f_start_date.length() < 4) {
            return "";
        }
        int len = f_start_date.length();
        return f_start_date.substring(0, len - 3);
    }


    private String getStrId(int tag) {

        AppContacts.intentData.clear();

        String id = "";
        List<RetListBean.InvitesBean> datas = null;
        if (tag == 1) {
            datas = joinAdapter.getData();
        } else if (tag == 2) {
            datas = chaoSongAdapter.getData();
        }

        AppContacts.intentData.addAll(convert(datas));

        if (datas != null && datas.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (RetListBean.InvitesBean item : datas) {
                buffer.append(item.getOwnermap().getUser_id()).append(":");
            }

            String temp = buffer.toString();

            return temp.substring(0, temp.length() - 1);
        }

        return id;
    }

    private List<MemberRetBean.MemberOutBean> convert(List<RetListBean.InvitesBean> datas) {

        List<MemberRetBean.MemberOutBean> res = new ArrayList<>();
        for (RetListBean.InvitesBean tt : datas) {
            MemberRetBean.MemberOutBean bean = new MemberRetBean.MemberOutBean();
            MemberBean cc = new MemberBean();
            cc.setUser_id(tt.getOwnermap().getUser_id());
            String name = MyTextUtil.isEmpty(tt.getOwnermap().getNickname()) ? tt.getOwnermap().getName() : tt.getOwnermap().getNickname();
            cc.setNickname(name);
            cc.setName(name);
            cc.setCreater(tt.getF_create_by());
            cc.setHead(tt.getOwnermap().getHead());
            bean.setUserinfo(cc);
            res.add(bean);
        }

        return res;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case 0x11:

                    eidt_view.setVisibility(View.VISIBLE);

                    List<RetListBean.InvitesBean> resut = checkDatas();

                    joinAdapter.getData().clear();
                    joinAdapter.getData().addAll(resut);
                    joinAdapter.notifyDataSetChanged();

                    break;

                case 0x21:

                    //TODO 后面需要去除已经At的人

                    eidt_view.setVisibility(View.VISIBLE);

                    List<RetListBean.InvitesBean> datt = convertDatas(AppContacts.SelectData);

                    chaoSongAdapter.getData().clear();
                    chaoSongAdapter.getData().addAll(datt);
                    chaoSongAdapter.notifyDataSetChanged();


                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private List<RetListBean.InvitesBean> checkDatas() {

        List<RetListBean.InvitesBean> resule = new ArrayList<>();

        for (MemberRetBean.MemberOutBean dt : AppContacts.SelectData) {

            boolean isAdd = false;
            for (RetListBean.InvitesBean hh : firstInvitList) {
                if (dt.getUserinfo().getUser_id().equals(hh.getOwnermap().getUser_id())) {
                    resule.add(hh);
                    isAdd = true;
                    break;
                }

            }

            if (!isAdd) {
                RetListBean.InvitesBean dttt = creatBb(dt);
                resule.add(dttt);
            }
        }


        return resule;
    }


    private List<RetListBean.InvitesBean> convertDatas(List<MemberRetBean.MemberOutBean> datas) {

        List<RetListBean.InvitesBean> result = new ArrayList<>();

        for (MemberRetBean.MemberOutBean item : datas) {

            if (hasJoin(item)) {
                continue;
            }

            RetListBean.InvitesBean data = creatBb(item);
            result.add(data);

        }
        return result;
    }

    private boolean hasJoin(MemberRetBean.MemberOutBean item) {

        List<RetListBean.InvitesBean> join = joinAdapter.getData();
        if (join.size() < 1)
            return false;

        for (RetListBean.InvitesBean kk : join) {
            if (kk.getOwnermap().getUser_id().equals(item.getUserinfo().getUser_id())) {
                ToastUtils.getInstanc().showToast(item.getUserinfo().getNickname() + "已经被@,不用重复抄送");
                return true;
            }

        }
        return false;
    }

    private RetListBean.InvitesBean creatBb(MemberRetBean.MemberOutBean child) {

        RetListBean.InvitesBean data = new RetListBean.InvitesBean();

        TodoUserBean secend = new TodoUserBean();

        secend.setUser_id(child.getUserinfo().getUser_id());
        secend.setName(MyTextUtil.isEmpty(child.getUserinfo().getNickname()) ? child.getUserinfo().getName() : child.getUserinfo().getNickname());
        secend.setHead(child.getUserinfo().getHead());
        data.setOwnermap(secend);
        data.setF_create_by(child.getUserinfo().getCreater());

        return data;
    }


    @OnClick(R.id.iv_back)
    public void goCommit() {
        finish();
    }


    @OnClick(R.id.edit)
    public void goEdit() {
        Intent intent = new Intent(mContext, ToDoEditActivity.class);
        intent.putExtra("todoId", todoId);
        startActivity(intent);
        finish();

    }


    @OnClick(R.id.add_todo)
    public void goJoin() {

        Intent mintent = getIntent();

        mintent.setClass(mContext, TotalMemberActivity.class);
        mintent.putExtra("aready", getStrId(1));//传当前已经选好的UerId


        startActivityForResult(mintent, 0x11);

    }


    @OnClick(R.id.cc)
    public void goCc() {

        Intent intent = new Intent(mContext, TotalMemberActivity.class);
        intent.putExtra("aready", getStrId(2));

        startActivityForResult(intent, 0x21);

    }


    @OnClick(R.id.delet_edit)
    public void goCancel() {

        if (joinAdapter != null) {
            joinAdapter.getData().clear();
            if (firstInvitList != null) {
                joinAdapter.getData().addAll(firstInvitList);
            }
            joinAdapter.notifyDataSetChanged();
        }

        if (chaoSongAdapter != null) {
            chaoSongAdapter.getData().clear();
            if (firstCcData != null) {
                chaoSongAdapter.getData().addAll(firstCcData);

            }
            chaoSongAdapter.notifyDataSetChanged();
        }

        eidt_view.setVisibility(View.GONE);

    }

    @OnClick(R.id.btn_commint)
    public void goComit() {
        if (AppContacts.ToDOInfo == null)
            return;


        if (joinAdapter.getData().size() < 1) {
            ToastUtils.getInstanc().showToast("请添加待办负责人");
            return;
        }

        commitUpdate(AppContacts.ToDOInfo);
    }


    public void commitUpdate(RetListBean mData) {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("f_id", mData.getF_id());
        map.put("f_source_id", "");
        map.put("f_title", mData.getF_title());
        map.put("org_id", "");
        map.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());

        String date = MyTextUtil.isEmpty(mData.getF_todo_date()) ? mData.getF_create_date() : mData.getF_todo_date();
        map.put("f_start_date", date + " " + DateUtil.getCurrentHms());

        map.put("f_end_date", getEndDate(mData) + " " + DateUtil.getCurrentHms());

        map.put("f_cc", getChaosongParams());
        map.put("f_assign", getTodoParams());
        map.put("attachments", getFujianParams(mData));


        url.upDateTodo(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(CommBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            ToastUtils.getInstanc().showToast("待办编辑成功!");
                            finish();

                        } else {
                            ToastUtils.getInstanc().showToast("编辑服务异常,请稍后再试");
                        }

                    }
                });
    }

    private String getEndDate(RetListBean mData) {

        String date = MyTextUtil.isEmpty(mData.getF_finish_date()) ? mData.getF_end_date() : mData.getF_finish_date();

        if (!MyTextUtil.isEmpty(date) && date.length() > 10) {
            return date.substring(0, 10);
        }

        return "";
    }


    private String getChaosongParams() {

        List<RetListBean.InvitesBean> chaosong = chaoSongAdapter.getData();

        if (chaosong == null || chaosong.size() == 0) {
            return "";
        } else if (chaosong.size() == 1) {
            return chaosong.get(0).getOwnermap().getUser_id();
        } else {

            StringBuffer buffer = new StringBuffer();

            for (RetListBean.InvitesBean itme : chaosong) {
                buffer.append(itme.getOwnermap().getUser_id()).append(",");
            }

            return buffer.toString();
        }


    }


    private String getTodoParams() {
        List<RetListBean.InvitesBean> todo = joinAdapter.getData();

        if (todo == null || todo.size() == 0) {
            return "";
        } else if (todo.size() == 1) {
            return todo.get(0).getOwnermap().getUser_id();
        } else {
            StringBuffer buffer = new StringBuffer();

            for (RetListBean.InvitesBean itme : todo) {
                buffer.append(itme.getOwnermap().getUser_id()).append(",");
            }
            return buffer.toString();
        }

    }


    private String getFujianParams(RetListBean mData) {

        List<RetListBean.DocumentBean> datas = mData.getAttachment();

        if (datas == null || datas.size() < 1) {
            JSONArray obj = new JSONArray();
            return obj.toString();
        } else {

            JSONArray array = new JSONArray();

            for (RetListBean.DocumentBean tt : datas) {
                JSONObject object = creatJson(tt);
                array.put(object);
            }
            return array.toString();
        }

    }

    private JSONObject creatJson(RetListBean.DocumentBean res) {

        JSONObject object = new JSONObject();

        try {
            object.put("f_title", res.getF_title());
            object.put("f_path", res.getF_path());
            object.put("f_type", res.getF_type());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return object;
    }


}
