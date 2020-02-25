package com.xinyu.newdiggtest.adapter;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;

import com.xinyu.newdiggtest.bean.MemberRetBean;
import com.xinyu.newdiggtest.bean.RetListBean;
import com.xinyu.newdiggtest.bean.TodoUserBean;
import com.xinyu.newdiggtest.bigq.AddDoneActivity;
import com.xinyu.newdiggtest.bigq.FileAdapter;
import com.xinyu.newdiggtest.bigq.FileBean;
import com.xinyu.newdiggtest.h5.WebViewActivity;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.CommBean;
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


import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InfoListAdapter extends BaseQuickAdapter<RetListBean, BaseViewHolder> {


    String fid = "";

    RecyclerView documentRecycleView, recyclerViewTodo, cc_recycle, reply_cylce;

    LinearLayout eidt_view;

    TodoInfoAdapter joinAdapter;

    ChaosongAdapter chaoSongAdapter;

    BaseViewHolder mHelper;
    RetListBean data;


    Handler mhandler;

    public void setHandler(Handler handler) {
        this.mhandler = handler;
    }


    public void setFid(String mid) {
        this.fid = mid;
    }


    public void setAtData(List<MemberRetBean.MemberOutBean> datas) {

        eidt_view.setVisibility(View.VISIBLE);

        if (!data.getF_id().equals(fid))
            return;

        List<RetListBean.InvitesBean> myData = convertDatas(datas);

        joinAdapter.setNewData(myData);


    }

    private List<RetListBean.InvitesBean> convertDatas(List<MemberRetBean.MemberOutBean> datas) {

        List<RetListBean.InvitesBean> result = new ArrayList<>();


        for (MemberRetBean.MemberOutBean item : datas) {


            RetListBean.InvitesBean data = creatBb(item);
            result.add(data);

        }
        return result;
    }

    private RetListBean.InvitesBean creatBb(MemberRetBean.MemberOutBean child) {

        RetListBean.InvitesBean data = new RetListBean.InvitesBean();

        TodoUserBean secend = new TodoUserBean();

        secend.setUser_id(child.getUserinfo().getUser_id());
        secend.setName(MyTextUtil.isEmpty(child.getUserinfo().getNickname()) ? child.getUserinfo().getName() : child.getUserinfo().getNickname());
        secend.setHead(child.getUserinfo().getHead());

        data.setOwnermap(secend);


        return data;
    }


    public void setCcData(List<MemberRetBean.MemberOutBean> datas) {

        eidt_view.setVisibility(View.VISIBLE);

        if (!data.getF_id().equals(fid))
            return;

        List<RetListBean.InvitesBean> myData = convertDatas(datas);
        chaoSongAdapter.setNewData(myData);

//        chaoSongAdapter.getData().addAll(myData);
//        chaoSongAdapter.notifyDataSetChanged();

    }


    public InfoListAdapter(int layoutResId, @Nullable List<RetListBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final RetListBean mData) {

        mHelper = helper;
        data = mData;

        TextView name = helper.getView(R.id.name);
        TextView time = helper.getView(R.id.time);
        TextView create_date = helper.getView(R.id.create_date);
        TextView tod_title = helper.getView(R.id.tod_title);

        documentRecycleView = helper.getView(R.id.doculist);

        recyclerViewTodo = helper.getView(R.id.recyclerView_todo);

        cc_recycle = helper.getView(R.id.cc_recycle);

        reply_cylce = helper.getView(R.id.reply_cylce);


        initRecycle();

        name.setText("创建 ： " + mData.getCreate_name().getNickname());
        time.setText(nosecond(mData.getF_start_date()));
        create_date.setText(mData.getF_create_date());
        tod_title.setText(mData.getF_title());

        List<RetListBean.DocumentBean> documentList = mData.getAttachment();

        if (documentList != null && documentList.size() > 0) {
            showDocuments(documentList);
        }

        List<RetListBean.InvitesBean> invitList = mData.getInvites();

        if (invitList == null || invitList.size() < 1) {
        } else {
            showTodoData(invitList);
        }

        List<RetListBean.InvitesBean> chognsongList = mData.getCcs();

        if (chognsongList == null || chognsongList.size() < 1) {
        } else {
            showDaibanData(chognsongList);
        }

        //TODO 是否显示或者隐藏

        ImageView icon1 = helper.getView(R.id.add_todo);
        ImageView icon2 = helper.getView(R.id.cc);
        eidt_view = helper.getView(R.id.eidt_view);


//        View creat_child = helper.getView(R.id.creat_child);

//        creat_child.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                creatChildTodo(mData);
//            }
//        });


        String id = MyTextUtil.isEmpty(mData.getF_rid()) ? mData.getF_id() : mData.getF_rid();

        if (fid.equals(id)) {

            setListner(icon1, icon2, eidt_view, mData);
            icon1.setVisibility(View.VISIBLE);
            icon2.setVisibility(View.VISIBLE);

        } else {
            icon1.setVisibility(View.GONE);
            icon2.setVisibility(View.GONE);
            eidt_view.setVisibility(View.GONE);

        }


        View mLl = helper.getView(R.id.jiaoban);

        if (mData.getNotes() != null && mData.getNotes().size() > 0) {
            mLl.setVisibility(View.VISIBLE);
            showReply(mData.getNotes());

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

    private void showReply(final List<RetListBean.InvitesBean> replyList) {

        reply_cylce.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        HomeReplyAdapter adapter = new HomeReplyAdapter(R.layout.item_reply, replyList);
        reply_cylce.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
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


    private void setListner(ImageView icon1, ImageView icon2, final LinearLayout eidt_view, final RetListBean mData) {

        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clickListner != null) {
                    clickListner.onAtClick(mData.getF_id());
                }
            }
        });

        icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (clickListner != null) {
                    clickListner.onCcClick(mData.getF_id());
                }


            }
        });

        eidt_view.findViewById(R.id.delet_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });

        eidt_view.findViewById(R.id.btn_commint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commitUpdate(mData);
            }
        });

    }


    List<RetListBean.InvitesBean> firstInvitList, firstCcData;

    private void showDaibanData(List<RetListBean.InvitesBean> chognsongList) {

        chaoSongAdapter.getData().addAll(chognsongList);

        firstCcData = new ArrayList<RetListBean.InvitesBean>();

        for (RetListBean.InvitesBean data : chognsongList) {
            firstCcData.add(data);
        }

        chaoSongAdapter.notifyDataSetChanged();


    }


    private void showTodoData(List<RetListBean.InvitesBean> invitList) {

        joinAdapter.getData().addAll(invitList);

        firstInvitList = new ArrayList<>();

        for (RetListBean.InvitesBean data : invitList) {
            firstInvitList.add(data);
        }


        joinAdapter.notifyDataSetChanged();

    }

    private void showDocuments(List<RetListBean.DocumentBean> documentList) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        documentRecycleView.setLayoutManager(layoutManager);//给RecyclerView设置适配器

        FileAdapter adapter = new FileAdapter(R.layout.item_file, convertData(documentList));
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


    }

    private List<FileBean> convertData(List<RetListBean.DocumentBean> documentList) {

        List<FileBean> data = new ArrayList<>();

        for (RetListBean.DocumentBean tt : documentList) {
            FileBean bean = new FileBean();
            String dcc = tt.getF_type();

            if (dcc.equals("png") || dcc.equals("jpg") || dcc.equals("jpeg")) {
                bean.setType(1);
            } else if (dcc.equals("pdf") || dcc.equals("pptx") || dcc.equals("ppt") || dcc.equals("txt") || dcc.equals("doc") || dcc.equals("docx") || dcc.equals("xls") || dcc.equals("xlsx")) {
                bean.setType(2);
            } else {
                bean.setType(3);
            }

            bean.setFileUrl(tt.getF_path());
            bean.setFname(tt.getF_title() + "." + dcc);


            data.add(bean);
        }


        return data;
    }


    OnRightClickListner clickListner;


    public void setRightClickListner(OnRightClickListner listner) {

        this.clickListner = listner;
    }


    public interface OnRightClickListner {

        void onAtClick(String fid);

        void onCcClick(String fid);
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
        map.put("company_id", "1");

        map.put("f_start_date", mData.getF_create_date() + " " + DateUtil.getCurrentHms());


        map.put("f_cc", getChaosongParams());
        map.put("f_assign", getTodoParams());
        map.put("attachments", getFujianParams(mData));
        map.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());

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

                            mhandler.sendEmptyMessage(0x99);

                        } else {
                            ToastUtils.getInstanc().showToast("编辑服务异常,请稍后再试");
                        }

                    }
                });
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


    public TodoInfoAdapter getAtAdapter() {

        return joinAdapter;
    }


    public ChaosongAdapter getCcAdapter() {

        return chaoSongAdapter;
    }

}
