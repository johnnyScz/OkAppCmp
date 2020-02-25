package com.xinyu.newdiggtest.ui.Digg.fragment;


import android.content.Intent;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.ChildTargetAdapter;
import com.xinyu.newdiggtest.bean.ChildTargetBean;
import com.xinyu.newdiggtest.bean.DakaBottowItem;
import com.xinyu.newdiggtest.bean.TargetDataBean;
import com.xinyu.newdiggtest.bean.TargetInfo;
import com.xinyu.newdiggtest.bean.TargetplanBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;


import com.xinyu.newdiggtest.ui.Digg.target.CreateTargetActivity;
import com.xinyu.newdiggtest.ui.LazySingleFragment;

import com.xinyu.newdiggtest.ui.TargetNewInfoActivity;
import com.xinyu.newdiggtest.ui.circle.FavortItem;

import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.view.CommentItem;
import com.xinyu.newdiggtest.view.CommentListView;
import com.xinyu.newdiggtest.widget.CommentPopupWindow;
import com.xinyu.newdiggtest.widget.PraiseListView;
import com.xinyu.newdiggtest.widget.SoftInputPopUtil;

import org.json.JSONArray;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 目标详情
 */
public class TargetInfoFragment extends LazySingleFragment {

    String tarEndtime, tarWeeks, classId, targetId,
            targName, creater, userId, fFine = "0", fstate = "0", isShare = "";//目标是否结束状态

    public String moniTorNames = "", startTime;

    @BindView(R.id.creater)
    TextView tarCreater;//目标创建者

    @BindView(R.id.begin_time)
    TextView begin;

    @BindView(R.id.end_time)
    TextView endTime;


    @BindView(R.id.from)
    TextView from;

    @BindView(R.id.my_qcode)
    ImageView my_qcode;


    @BindView(R.id.ll_child)
    LinearLayout ll_child;

    @BindView(R.id.qcode_ll)
    RelativeLayout qcode_ll;

    @BindView(R.id.tv_private)
    TextView tv_private;


    @BindView(R.id.add_child_item)
    LinearLayout add_child_item;

    @BindView(R.id.ll_from)
    LinearLayout ll_from;


    @BindView(R.id.child_count)
    TextView child_count;

    @BindView(R.id.child_recycle)
    RecyclerView child_recycle;

    @BindView(R.id.monitor_list)
    TextView monitor_list;//监督人列表


    @BindView(R.id.f_fine)
    public TextView f_fine;//挑战金

//    @BindView(R.id.iv_setfine)
//    public ImageView iv_setfine;//设置挑战金图标


    @BindView(R.id.weeks)
    public TextView weeks;


    @BindView(R.id.alartime)
    public TextView alarmTime;


    //----------------目标点赞和评论------------------

    @BindView(R.id.iv_common_more)
    public ImageView iv_common_more;//评论点赞

    @BindView(R.id.praiseListView)
    public PraiseListView praiseView;//点赞

    @BindView(R.id.commentList)
    public CommentListView commentListView;//评论

    @BindView(R.id.digCommentBody)
    public LinearLayout comonBody;//评论点赞父控件

    TargetDataBean netData;


    String reward = "0";


    public void setTargetId(String tarId) {
        targetId = tarId;
    }


    @Override
    public void onResume() {
        super.onResume();
        requestdatas();
    }


    private void requestdatas() {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("target_uuid", targetId);
        map.put("command", "ok-api.SelectTargetDetail");
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        url.getTargetInfo(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TargetInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(TargetInfo msg) {
                        String code = msg.getOp().getCode();
                        if (code.equals("Y")) {
                            if (msg.getData() == null) {
                                return;
                            }
                            netData = msg.getData();
                            updateView(netData.getTargetplan());
                            showCommonInfo(netData);

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });

    }


    /**
     * 显示点赞评论详情
     *
     * @param data
     */
    private void showCommonInfo(TargetDataBean data) {

        boolean hasPrise = data.getTargetplanlikes() != null && data.getTargetplanlikes().size() > 0;
        boolean hasCommon = data.getTargetplancomment() != null && data.getTargetplancomment().size() > 0;

        if (!hasPrise && !hasCommon) {
            comonBody.setVisibility(View.GONE);
            return;
        } else {
            comonBody.setVisibility(View.VISIBLE);
        }

        if (data.getTargetplanlikes() != null && data.getTargetplanlikes().size() > 0) {
            praiseView.setVisibility(View.VISIBLE);
            praiseView.setDatas(convertPaise(data.getTargetplanlikes()));
        } else {
            praiseView.setVisibility(View.GONE);
        }

        if (data.getTargetplancomment() != null && data.getTargetplancomment().size() > 0) {
            commentListView.setVisibility(View.VISIBLE);
            commentListView.setDatas(conVertData(data.getTargetplancomment(), "1"));
        } else {
            commentListView.setVisibility(View.GONE);
        }


    }


    private void updateView(TargetplanBean teargetplan) {

        fstate = teargetplan.getF_state();

        reward = teargetplan.getF_reward();


        targetId = teargetplan.getF_uuid();
        targName = teargetplan.getF_name();

        startTime = teargetplan.getF_start_date();
        begin.setText(startTime);

        userId = teargetplan.getF_createuser();


        classId = teargetplan.getF_class_id();

        tarEndtime = teargetplan.getF_end_date();
        endTime.setText(tarEndtime);

        tarWeeks = conVert(teargetplan.getF_repeat_date());
        weeks.setText(tarWeeks);

        creater = teargetplan.getF_createname();
        tarCreater.setText(creater);//创建者名字


        fFine = teargetplan.getF_fine();

        isShare = teargetplan.getF_is_share();

        if (isShare.equals("2")) {
            qcode_ll.setVisibility(View.GONE);
            tv_private.setVisibility(View.VISIBLE);

            setPriList(tv_private);
        } else {
            qcode_ll.setVisibility(View.VISIBLE);
            tv_private.setVisibility(View.GONE);
        }


        if (MyTextUtil.isEmpty(fFine) || Float.parseFloat(fFine) == 0) {
            f_fine.setText("0");
        } else {
            f_fine.setText(fFine);
        }

        if (!MyTextUtil.isEmpty(teargetplan.getF_reminder_time())) {
            alarmTime.setText(teargetplan.getF_reminder_time());
        } else {
            alarmTime.setText("--");

        }

        parchJsonStr(teargetplan.getF_supervisor());
        parchChildTarget(teargetplan.getChild());

        showIfChildTarget();

        if (teargetplan.getFrom() == null) {
            ll_from.setVisibility(View.GONE);
        } else {
            ll_from.setVisibility(View.VISIBLE);
            String name = teargetplan.getFrom().getF_name();
            from.setText("#" + MyTextUtil.getDecodeStr(name) + "#");
        }


    }

    private void setPriList(TextView tv) {

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.getInstanc().showToast("无法为私密目标生成二维码!");
            }
        });

    }

    /**
     * TODO
     * 是否是自己,目标是否已结束
     * ①不显示字目标
     * ②挑战金是否可显示
     */
    private void showIfChildTarget() {
        if (MyTextUtil.isEmpty(userId))
            return;

        if (!userId.equals(PreferenceUtil.getInstance(mContext).getUserId()) || fstate.equals("1")) {
            add_child_item.setVisibility(View.GONE);
        } else {
            add_child_item.setVisibility(View.VISIBLE);
        }


    }

    /**
     * 解析字目标
     *
     * @param child
     */
    private void parchChildTarget(List<ChildTargetBean> child) {
        if (child != null && child.size() > 0) {
            ll_child.setVisibility(View.VISIBLE);
            child_count.setText(child.size() + "个子目标");
            fillRecycle(child);
        } else {
            ll_child.setVisibility(View.GONE);
        }
    }


    private void fillRecycle(final List<ChildTargetBean> child) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        child_recycle.setLayoutManager(layoutManager);//给RecyclerView设置适配器
        ChildTargetAdapter adapter = new ChildTargetAdapter(R.layout.item_child_target, child);
        child_recycle.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                ChildTargetBean data = child.get(position);

                Intent mitent = mContext.getIntent();
                mitent.setClass(mContext, TargetNewInfoActivity.class);
                mitent.putExtra(IntentParams.Target_Pid, data.getF_pid());
                mitent.putExtra(IntentParams.Target_Root_id, data.getF_rid());
                mitent.putExtra(IntentParams.DAKA_Target_Id, data.getF_uuid());
                startActivity(mitent);
            }
        });


    }

    private void parchJsonStr(Object f_supervisor) {

        try {
            String jsonObj = (String) f_supervisor;
            JSONArray jsonarray = new JSONArray(jsonObj);
            showMonitor(jsonarray);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void showMonitor(JSONArray arr) {
        StringBuffer buffer = new StringBuffer();
        int len = arr.length();
        for (int i = 0; i < len; i++) {
            try {
                JSONObject object = arr.getJSONObject(i);
                String name = object.getString("nickname");
                buffer.append(URLDecoder.decode(name, "UTF-8")).append(",");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String bufStr = buffer.toString();
        if (!MyTextUtil.isEmpty(bufStr)) {
            moniTorNames = bufStr.substring(0, buffer.length() - 1);
            monitor_list.setText(moniTorNames);
        }
    }


    private String conVert(String f_repeat_date) {

        String[] list = f_repeat_date.split(",");

        int len = list.length;
        String[] reList = new String[len];

        for (int i = 0; i < len; i++) {
            String ch = list[i];
            switch (ch) {
                case "1":
                    reList[i] = "周一";
                    break;
                case "2":
                    reList[i] = "周二";
                    break;
                case "3":
                    reList[i] = "周三";
                    break;

                case "4":
                    reList[i] = "周四";
                    break;

                case "5":
                    reList[i] = "周五";
                    break;

                case "6":
                    reList[i] = "周六";
                    break;

                case "7":
                    reList[i] = "周日";
                    break;

            }
        }

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < len; i++) {
            buffer.append(reList[i]).append(",");
        }
        String content = buffer.toString();
        return content.substring(0, content.length() - 1);
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_target_info;
    }

    @Override
    protected void initView() {


    }

    @Override
    public void onFragmentVisibleChange(boolean isVisible) {

    }


    @OnClick(R.id.iv_common_more)
    public void goCommen() {
        List<DakaBottowItem> dakaitem = netData.getTargetplanlikes();
        String type = "";
        if (dakaitem != null && dakaitem.size() > 0) {
            for (DakaBottowItem kk : dakaitem) {
                if (kk.getF_like_user().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                    type = "1";
                    break;
                } else {
                    type = "0";
                }
            }

        } else {
            type = "0";
        }

        final CommentPopupWindow pop = new CommentPopupWindow(mContext, type, false);
        pop.update();
        pop.setmItemClickListener(new CommentPopupWindow.OnItemClickListener() {
            @Override
            public void onItemClick(String content) {
                if (content.equals("赞") || content.equals("取消")) {
                    if (content.equals("赞")) {
                        dianZan();
                    } else if (content.equals("取消")) {
                        canCelZan();
                    }
                } else if (content.equals("评论")) {
                    SoftInputPopUtil.liveCommentEdit(mContext, iv_common_more, new SoftInputPopUtil.liveCommentResult() {
                        @Override
                        public void onResult(boolean confirmed, String comment) {
                            doRequestComment(comment);
                        }
                    });

                }
            }
        });
//
        pop.showPopupWindow(iv_common_more);

    }


    /**
     * 取消点赞
     */
    private void canCelZan() {
        List<DakaBottowItem> fidList = netData.getTargetplanlikes();
        String fid = "";
        for (DakaBottowItem ta : fidList) {
            if (ta.getF_like_user().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                fid = ta.getF_uuid();
                break;
            }

        }

        if (!MyTextUtil.isEmpty(fid)) {
            RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
            AppUrl api = manager.create(AppUrl.class);
            HashMap<String, String> requsMap = new HashMap<>();
            requsMap.put("likes_uuid", fid);
            requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
            api.cancelZan(requsMap).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Info>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.getInstanc(mContext).showToast(e.getMessage());

                        }

                        @Override
                        public void onNext(Info msg) {
                            if (msg.getOp().getCode().equals("Y")) {
                                ToastUtils.getInstanc(mContext).showToast("取消成功");
                                requestdatas();
                            } else {
                                ToastUtils.getInstanc(mContext).showToast("服务异常");
                            }
                        }
                    });


        }


    }


    /**
     * 目标点赞
     */
    private void dianZan() {
        if (netData != null) {
            RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
            AppUrl api = manager.create(AppUrl.class);

            TargetplanBean item = netData.getTargetplan();

            HashMap<String, String> requsMap = new HashMap<>();
            requsMap.put("object_id", item.getF_uuid());
            requsMap.put("like_user_id", PreferenceUtil.getInstance(mContext).getUserId());
            requsMap.put("type", "0");
            requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
            requsMap.put("target_name", MyTextUtil.getUrl3Encoe(MyTextUtil.getDecodeStr(item.getF_name())));
            requsMap.put("start_date", item.getF_start_date());
            requsMap.put("end_date", item.getF_end_date());
            requsMap.put("like_user_name", PreferenceUtil.getInstance(mContext).getNickName());
            requsMap.put("user_id", item.getF_createuser());
            requsMap.put("command", "ok-api.InsertTargetLikes");

            api.addZan(requsMap).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Info>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.getInstanc(mContext).showToast(e.getMessage());

                        }

                        @Override
                        public void onNext(Info msg) {
                            if (msg.getOp().getCode().equals("Y")) {
                                ToastUtils.getInstanc(mContext).showToast("点赞成功");
                                requestdatas();
                            } else {
                                ToastUtils.getInstanc(mContext).showToast("点赞失败");
                            }
                        }
                    });
        }
    }


    /**
     * 目标评论
     */
    private void doRequestComment(String comments) {
        if (netData != null) {
            String content = "";
            try {
                content = URLEncoder.encode(comments, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
            AppUrl api = manager.create(AppUrl.class);
            TargetplanBean commentBean = netData.getTargetplan();


            HashMap<String, String> requsMap = new HashMap<>();
            requsMap.put("object_id", commentBean.getF_uuid());
            requsMap.put("comment", content);
            requsMap.put("comment_user_id", PreferenceUtil.getInstance(mContext).getUserId());
            requsMap.put("type", "0");
            requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
            requsMap.put("target_name", MyTextUtil.getUrl3Encoe(MyTextUtil.getDecodeStr(commentBean.getF_name())));
            requsMap.put("start_date", commentBean.getF_start_date());
            requsMap.put("end_date", commentBean.getF_end_date());
            requsMap.put("nick_name", PreferenceUtil.getInstance(mContext).getNickName());
//            requsMap.put("user_id", commentBean.getF_toUser());
            requsMap.put("user_id", commentBean.getF_createuser());
            requsMap.put("command", "ok-api.InsertTargetComment");
            api.addComments(requsMap).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Info>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.getInstanc(mContext).showToast(e.getMessage());

                        }

                        @Override
                        public void onNext(Info msg) {
                            if (msg.getOp().getCode().equals("Y")) {
                                requestdatas();
                            } else {
                                ToastUtils.getInstanc(mContext).showToast("评论失败");
                            }
                        }
                    });

        }
    }


    private List<FavortItem> convertPaise(List<DakaBottowItem> likes) {
        List<FavortItem> mdate = new ArrayList<>();
        FavortItem tt;
        for (DakaBottowItem item : likes) {
            tt = new FavortItem();
            tt.setName(item.getF_nick_name());
            tt.setContent(item.getF_comment());
            mdate.add(tt);
        }
        return mdate;
    }


    private List<CommentItem> conVertData(List<DakaBottowItem> targetcomment, String type) {
        int len = targetcomment.size();
        List<CommentItem> list = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            DakaBottowItem daka = targetcomment.get(i);
            CommentItem item = new CommentItem();
            item.setCommentUser(daka.getF_nick_name());
            item.setType(type);
            try {
                String content = URLDecoder.decode(daka.getF_comment(), "UTF-8");
                item.setContent(content);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            list.add(item);
        }
        return list;

    }


    @OnClick(R.id.qcode_ll)
    public void goQcodeImg() {
//        Intent intent = new Intent(mContext, CommonQcodeActivity.class);
//        intent.putExtra(IntentParams.Intent_Enter_Type, "target");
//        intent.putExtra("roomName", targName);
//        //TODO 后期不用-1(目标图片)
//        intent.putExtra("headUrl", "-1");
//        intent.putExtra("targetId", targetId);
//        startActivity(intent);
    }


    @OnClick(R.id.add_child_item)
    public void addChildItem() {

        Intent intent = new Intent(mContext, CreateTargetActivity.class);
        intent.putExtra(IntentParams.Intent_Enter_Type, "child");
        intent.putExtra(IntentParams.Target_Pid, targetId);
        intent.putExtra(IntentParams.Target_Root_id, targetId);

        startActivity(intent);


    }


}
