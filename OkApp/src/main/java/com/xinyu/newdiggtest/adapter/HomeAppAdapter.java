package com.xinyu.newdiggtest.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.viewhelper.interfa.MyLongClickner;
import com.xinyu.newdiggtest.bean.LeaverBean;
import com.xinyu.newdiggtest.bean.RetListBean;
import com.xinyu.newdiggtest.bean.TodoUserBean;
import com.xinyu.newdiggtest.bigq.FileAdapter;
import com.xinyu.newdiggtest.bigq.FileBean;
import com.xinyu.newdiggtest.h5.WebViewActivity;
import com.xinyu.newdiggtest.office.OfficeX5CoreActivity;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 待办首页的多类型列表
 */
public class HomeAppAdapter extends BaseMultiItemQuickAdapter<RetListBean, BaseViewHolder> {


    int disAbleCheck = 0;


    public void setDisAbleCheck(int check) {

        this.disAbleCheck = check;
    }


    MyLongClickner listner;


    ScheduledExecutorService executor;


    public void releaseTimer() {
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
        mhandler.removeCallbacksAndMessages(null);
        mhandler = null;

    }


    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            TextView textView = (TextView) msg.obj;
            Bundle bundle = msg.getData();
            String endDate = bundle.getString("endDate");

            String finEndDate = endDate.substring(0, endDate.length() - 3);

            String now = DateUtil.getCurrentMs();


            long left = DateUtil.convert2long(finEndDate) - DateUtil.convert2long(now);


            if (left <= 0) {
                textView.setText("已超时");
                textView.setBackgroundColor(App.mContext.getResources().getColor(R.color.red_color));
                return;
            }

            int total = (int) (left / 1000) / 60;


            int hour = total / 60;


            int min = total - hour * 60;

            String hourStr = "", minStr = "";
            if (hour < 10) {
                hourStr = "0" + hour;
            } else {
                hourStr = hour + "";
            }

            if (min < 10) {
                minStr = "0" + min;
            } else {
                minStr = min + "";
            }
            textView.setText(hourStr + ":" + minStr + "分");
            textView.setBackgroundColor(App.mContext.getResources().getColor(R.color.mall_colorAccent));
        }
    };


    public void transListner(MyLongClickner myListner) {
        this.listner = myListner;
    }


    public HomeAppAdapter(@Nullable List<RetListBean> data) {
        super(data);

        addItemType(0, R.layout.item_home_todo);//普通待办
        addItemType(1, R.layout.item_vote);//投票,报名以及其他
        addItemType(2, R.layout.item_process_todo);//流程待办
//        addItemType(3, R.layout.item_group_business);//待办的消息类型

        executor = Executors.newScheduledThreadPool(1);
    }


    @Override
    protected void convert(BaseViewHolder helper, final RetListBean item) {


        int type = item.getItemType();


        switch (type) {


            case 1:

                helper.setText(R.id.title_content, item.getF_title());

                AppUtils.handleVote(helper, item);

                if (item.getCreate_name().getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                    helper.setText(R.id.from, "From 自己");
                } else {
                    helper.setText(R.id.from, "From " + item.getCreate_name().getNickname());
                }

                if (MyTextUtil.isEmpty(item.getF_end_date())) {
                    helper.getView(R.id.end_line).setVisibility(View.GONE);
                } else {
                    helper.setVisible(R.id.end_line, true);
                    helper.setText(R.id.end_line, "截止时间 ：" + getOnlyDay(item.getF_end_date()));
                }

                List<RetListBean.InvitesBean> ivsit = item.getInvites();
                if (ivsit != null && ivsit.size() > 0) {
                    helper.setVisible(R.id.ll_at, true);
                    showJoin(helper, ivsit, item.getF_create_by(), item.getFinishes());

                } else {
                    helper.getView(R.id.ll_at).setVisibility(View.GONE);
                }

                if (item.getNotes() == null || item.getNotes().size() < 1) {
                    helper.getView(R.id.rply_rl).setVisibility(View.GONE);

                } else {
                    helper.setVisible(R.id.rply_rl, true);
                    showReply(helper, item.getNotes());
                }

                helper.addOnClickListener(R.id.add_done);
                helper.addOnClickListener(R.id.rl_vote);
                break;


            case 2://流程待办

                String tt = item.getF_start_date();

                if (AppUtils.isToday(item)) {
                    String text = tt.substring(10, tt.length());
                    helper.setText(R.id.top_time, text.substring(0, text.length() - 3));

                } else {
                    helper.setText(R.id.top_time, tt.substring(0, 10));
                }

                helper.setText(R.id.title_content, item.getF_title());


                if (MyTextUtil.isEmpty(item.getF_end_date())) {
                    helper.getView(R.id.end_line).setVisibility(View.GONE);
                } else {
                    helper.setVisible(R.id.end_line, true);
                    helper.setText(R.id.end_line, "截止时间 ：" + getOnlyDay(item.getF_end_date()));
                }


                if (item.getNotes() == null || item.getNotes().size() < 1) {
                    helper.getView(R.id.rply_rl).setVisibility(View.GONE);

                } else {
                    helper.setVisible(R.id.rply_rl, true);
                    showReply(helper, item.getNotes());
                }


                List<RetListBean.InvitesBean> ccSong = item.getCcs();

                if (ccSong != null && ccSong.size() > 0) {
                    helper.setVisible(R.id.ll_Cc, true);
                    showCC(helper, ccSong);
                } else {
                    helper.getView(R.id.ll_Cc).setVisibility(View.GONE);
                }


                LeaverBean laver = item.getLeaveuser();
                if (laver != null) {
                    if (laver.getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                        helper.setText(R.id.from, "From 自己");
                    } else {
                        helper.setText(R.id.from, "From " + laver.getNickname());
                    }
                }


                helper.addOnClickListener(R.id.rl_item1);

                String eventType = item.getF_type();

                ImageView imageView = helper.getView(R.id.money_icon);


                if (eventType.equals("leave")) {
                    item.setProcessType(1);
                    imageView.setVisibility(View.GONE);
                } else if (eventType.equals("t_bgsbsqb")) {
                    item.setProcessType(2);
                    imageView.setVisibility(View.GONE);
                } else if (eventType.equals("t_fksqb")) {
                    item.setProcessType(3);
                    imageView.setVisibility(View.GONE);
                } else if (eventType.equals("investor")) {
                    item.setProcessType(5);
                    imageView.setVisibility(View.VISIBLE);

                } else if (eventType.equals("popularize")) {
                    item.setProcessType(4);
                    imageView.setVisibility(View.VISIBLE);
                }


                if (AppUtils.isCountReback(item)) {

                    helper.setVisible(R.id.time_tx, true);

                    final TextView countText = helper.getView(R.id.time_tx);


                    final String endDate = item.getF_end_date();

                    String finEndDate = endDate.substring(0, endDate.length() - 3);

                    String now = DateUtil.getCurrentMs();

                    long left = DateUtil.convert2long(finEndDate) - DateUtil.convert2long(now);

                    if (left <= 0) {
                        countText.setText("已超时");
                        countText.setBackgroundColor(App.mContext.getResources().getColor(R.color.red_color));

                    } else {
                        executor.scheduleWithFixedDelay(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Message msg = mhandler.obtainMessage();

                                        Bundle data = new Bundle();
                                        data.putString("endDate", endDate);
                                        msg.setData(data);

                                        msg.obj = countText;
                                        mhandler.sendMessage(msg);
                                    }
                                },
                                1,
                                30,
                                TimeUnit.SECONDS);//30秒需要更新一次
                    }
                } else {
                    helper.setVisible(R.id.time_tx, false);
                }

                if (eventType.equals("leave")) {
                    View rr_jb = helper.getView(R.id.rr_jb);
                    rr_jb.setVisibility(View.GONE);

                    View rply_rl = helper.getView(R.id.rply_rl);
                    rply_rl.setVisibility(View.GONE);

                } else {
                    if (AppUtils.ifInviteNoAccept(item) == -1) {
                        helper.setVisible(R.id.add_done, false);
                    } else {
                        helper.setVisible(R.id.rr_jb, true);
                        helper.setVisible(R.id.add_done, true);
                        helper.addOnClickListener(R.id.add_done);
                    }
                }

                List<RetListBean.InvitesBean> datas = item.getFinishes();

                RetListBean.InvitesBean me = datas.get(0);//只有一个人

                ImageView icon = helper.getView(R.id.icon);

                ImageView state_tag = helper.getView(R.id.state_tag);


                if (me != null) {
                    if (me.getF_state().equals("1")) {
                        icon.setImageResource(R.mipmap.check_new);
                        state_tag.setVisibility(View.VISIBLE);
                        state_tag.setImageResource(R.drawable.yiqueren);

                    } else {
                        icon.setImageResource(R.mipmap.check_new_no);
                        state_tag.setVisibility(View.GONE);
                    }
                } else {

                    RetListBean.InvitesBean cc = getMyslf(ccSong);

                    if (cc != null) {
                        icon.setImageResource(R.drawable.chaosong);
                    } else {
                        icon.setImageResource(R.mipmap.check_new_no);
                    }


                }

                List<RetListBean.InvitesBean> invit = item.getInvites();

                if (invit != null && invit.size() > 0) {
                    String name = invit.get(0).getOwnermap().getNickname();
                    helper.setText(R.id.name_person, name);
                }


                break;

            case 0:

                helper.addOnClickListener(R.id.rl_item1);

                List<RetListBean.InvitesBean> invitesList = item.getInvites();

                if (invitesList != null && invitesList.size() > 0) {
                    helper.setVisible(R.id.ll_at, true);
                    showJoin(helper, invitesList, item.getF_create_by(), item.getFinishes());

                } else {
                    helper.getView(R.id.ll_at).setVisibility(View.GONE);
                }


                String time = item.getF_start_date();

                if (AppUtils.isToday(item)) {
                    String text = time.substring(10, time.length());
                    helper.setText(R.id.top_time, text.substring(0, text.length() - 3));

                } else {
                    helper.setText(R.id.top_time, time.substring(0, 10));
                }

                helper.setText(R.id.title_content, item.getF_title());


                checkIconType(helper, item);


                if (MyTextUtil.isEmpty(item.getF_end_date())) {
                    helper.getView(R.id.end_line).setVisibility(View.GONE);
                } else {
                    helper.setVisible(R.id.end_line, true);
                    helper.setText(R.id.end_line, "截止时间 ：" + getOnlyDay(item.getF_end_date()));
                }


                if (item.getCreate_name().getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                    helper.setText(R.id.from, "From 自己");
                } else {
                    helper.setText(R.id.from, "From " + item.getCreate_name().getNickname());
                }


                if (item.getNotes() == null || item.getNotes().size() < 1) {
                    helper.getView(R.id.rply_rl).setVisibility(View.GONE);

                } else {
                    helper.setVisible(R.id.rply_rl, true);
                    showReply(helper, item.getNotes());
                }


                TextView textView = helper.getView(R.id.title_content);


                textView.setCompoundDrawables(null, null, null, null);


                helper.setVisible(R.id.time_tx, false);


                if (item.getF_type().equals("board")) {

                    item.setProcessType(6);//项目管理

                    ImageView magIcon = helper.getView(R.id.icon);
                    magIcon.setVisibility(View.GONE);

                    View rr_jb = helper.getView(R.id.rr_jb);
                    rr_jb.setVisibility(View.GONE);

                    View end_line = helper.getView(R.id.end_line);
                    end_line.setVisibility(View.GONE);
                    View ll_Cc = helper.getView(R.id.ll_Cc);
                    ll_Cc.setVisibility(View.GONE);

                } else {

                    if (AppUtils.ifInviteNoAccept(item) == -1) {

                        helper.getView(R.id.rr_jb).setVisibility(View.GONE);

                    } else {
                        helper.setVisible(R.id.rr_jb, true);
                        helper.addOnClickListener(R.id.add_done);
                    }

                    List<RetListBean.DocumentBean> doculist = item.getAttachment();

                    if (doculist != null && doculist.size() > 0) {
                        helper.setVisible(R.id.recyclerView, true);
                        showDocuments(helper, convertData(doculist));

                    } else {
                        helper.getView(R.id.recyclerView).setVisibility(View.GONE);

                    }


                    List<RetListBean.InvitesBean> chaosong = item.getCcs();

                    if (chaosong != null && chaosong.size() > 0) {
                        helper.setVisible(R.id.ll_Cc, true);
                        showCC(helper, chaosong);
                    } else {
                        helper.getView(R.id.ll_Cc).setVisibility(View.GONE);
                    }


                    String invitTag = haveHaveInvite(item);

                    if (invitTag.equals("0")) {
                        View view = helper.getView(R.id.comit_cance_ll);
                        view.setVisibility(View.VISIBLE);
                        helper.addOnClickListener(R.id.btn_confirm);
                        helper.addOnClickListener(R.id.cancel);
                    } else {
                        View view = helper.getView(R.id.comit_cance_ll);
                        view.setVisibility(View.GONE);
                    }
                }


                break;

        }

    }


    /**
     * 处理消息类型
     */
//    private void handleMsg(BaseViewHolder helper, RetListBean item) {
//
//        ImageView imageView = helper.getView(R.id.iv_icon);
//
//        if (item.getMsg() == null || item.getMsg().getUserinfo() == null) {
//            return;
//        }
//
//        String head = item.getMsg().getUserinfo().getHead();
//
//        Picasso.with(mContext).load(head).error(R.drawable.icon_no_download).
//                transform(new CircleCornerForm()).into(imageView);
//
//        helper.setText(R.id.tv_groupname, item.getMsg().getF_title());
//
//        helper.setText(R.id.tv_latestmsg, item.getMsg().getUserinfo().getNickname() + ":" + item.getMsg().getF_content());
//
//        helper.setText(R.id.msg_time, TimeUtil.getChatTimeStr(Long.parseLong(item.getMsg().getF_last_time())));
//
//
//    }
    private RetListBean.InvitesBean getMyslf(List<RetListBean.InvitesBean> datas) {

        if (datas == null || datas.size() < 1) {

            return null;

        }

        for (RetListBean.InvitesBean item : datas) {
            if (item.getOwnermap().getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                return item;
            }
        }

        return null;
    }

    private String haveHaveInvite(RetListBean item) {

        String vivit = "-1";//表 我不在visit列表中

        List<RetListBean.InvitesBean> data = item.getInvites();

        if (data != null && data.size() > 0) {
            for (RetListBean.InvitesBean tt : data) {
                if (tt.getOwnermap().getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                    return tt.getF_state();//0 在未接受 1在已接受 2在已拒绝
                }
            }

        }


        return vivit;
    }

    private String getOnlyDay(String f_end_date) {

        if (MyTextUtil.isEmpty(f_end_date) || f_end_date.length() < 8) {
            return "";
        }

        int len = f_end_date.length();

        return f_end_date.substring(0, len - 8);
    }

    /**
     * 顶部Icon的check显示逻辑
     *
     * @param helper
     * @param item
     */
    private void checkIconType(BaseViewHolder helper, RetListBean item) {

        TodoUserBean data = item.getCreate_name();

        ImageView imageView = helper.getView(R.id.icon);

        if (atHasMe(item, 1)) {

            if (atHasMe(item, 3)) {

                imageView.setVisibility(View.VISIBLE);

                List<RetListBean.InvitesBean> dd = item.getFinishes();

                if (dd != null && dd.size() > 0) {
                    String state = "";
                    for (RetListBean.InvitesBean kk : dd) {
                        if (kk.getOwnermap().getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                            state = kk.getF_state();
                            break;
                        }
                    }

                    if (state.equals("1")) {
                        imageView.setImageResource(R.mipmap.check_new);
                    } else {
                        imageView.setImageResource(R.mipmap.check_new_no);
                    }

                    if (disAbleCheck != 1) {
                        helper.addOnClickListener(R.id.icon);//个人空间跟与我相关的，都不能check
                    }
                }
            } else {
                imageView.setVisibility(View.GONE);//没有接受邀请，不显示方框
            }


        } else {
            if (atHasMe(item, 2)) {
                if (!data.getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                    helper.setVisible(R.id.icon, true);
                    imageView.setImageResource(R.drawable.chaosong);

                    View view = helper.getView(R.id.comit_cance_ll);
                    view.setVisibility(View.GONE);

                } else {
                    imageView.setVisibility(View.GONE);
                }


            } else {
                imageView.setVisibility(View.GONE);
            }
        }

    }

    private boolean atHasMe(RetListBean item, int type) {
        List<RetListBean.InvitesBean> dats = null;
        if (type == 1) {
            dats = item.getInvites();
        } else if (type == 2) {
            dats = item.getCcs();
        } else if (type == 3) {
            dats = item.getFinishes();
        }


        boolean isHas = false;
        for (RetListBean.InvitesBean itme : dats) {
            if (itme.getOwnermap().getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                return true;
            }
        }

        return isHas;
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


    private void showJoin(BaseViewHolder helper, List<RetListBean.InvitesBean> datas, String
            by, List<RetListBean.InvitesBean> finishes) {

        RecyclerView recyclerViewTodo = helper.getView(R.id.join_list);

        LinearLayoutManager hoziLayountManager = new LinearLayoutManager(mContext);
        hoziLayountManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewTodo.setLayoutManager(hoziLayountManager);//给RecyclerView设置适配器
        TodoInfoAdapter joinAdapter = new TodoInfoAdapter(R.layout.item_info_member, datas);
        joinAdapter.setFinishDatas(finishes);
        joinAdapter.setCreater(by);
        joinAdapter.setType(0);

        joinAdapter.setLongMyListner(listner);


        recyclerViewTodo.setAdapter(joinAdapter);

    }


    private void showCC(BaseViewHolder helper, List<RetListBean.InvitesBean> chaosong) {

        RecyclerView recyclerViewCc = helper.getView(R.id.cc_list);

        LinearLayoutManager hoziLayountManager = new LinearLayoutManager(mContext);
        hoziLayountManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewCc.setLayoutManager(hoziLayountManager);//给RecyclerView设置适配器
        TodoInfoAdapter CcAdapter = new TodoInfoAdapter(R.layout.item_info_member, chaosong);
        CcAdapter.setType(1);
        recyclerViewCc.setAdapter(CcAdapter);
        CcAdapter.setLongMyListner(listner);


    }

    private void showDocuments(BaseViewHolder helper, List<FileBean> documentList) {

        RecyclerView documentRecycleView = helper.getView(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        documentRecycleView.setLayoutManager(layoutManager);//给RecyclerView设置适配器

        FileAdapter adapter = new FileAdapter(R.layout.item_file, documentList);
        documentRecycleView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                FileBean ddd = (FileBean) adapter.getData().get(position);


                String path = ddd.getDex();


                if (path.toLowerCase().equals("jpg") || path.toLowerCase().equals("jeg") || path.toLowerCase().equals("jpeg") || path.toLowerCase().equals("png")) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("path", ddd.getFileUrl());
                    mContext.startActivity(intent);

                } else {
                    Intent intent = new Intent(mContext, OfficeX5CoreActivity.class);
                    intent.putExtra("path", ddd.getFileUrl());
                    mContext.startActivity(intent);
                }


            }
        });

    }


    private void showReply(BaseViewHolder helper, final List<RetListBean.InvitesBean> replyList) {

        RecyclerView recyclerView = helper.getView(R.id.reply_recycle);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        HomeReplyAdapter adapter = new HomeReplyAdapter(R.layout.item_reply, replyList);
        recyclerView.setAdapter(adapter);

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


//        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//
//                RetListBean.InvitesBean mdt = replyList.get(position);
//
//                //只有自己才能编辑自己的交办
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
    public long getItemId(int position) {
        return position;
    }

}
