package com.xinyu.newdiggtest.ui.Digg.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.DakaBottowItem;
import com.xinyu.newdiggtest.bean.ShareUrlBean;
import com.xinyu.newdiggtest.bean.TargetDataBean;
import com.xinyu.newdiggtest.bean.TargetInfo;
import com.xinyu.newdiggtest.bean.TargetplanBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.BaseActivity;
import com.xinyu.newdiggtest.ui.CommonDakaFragment;
import com.xinyu.newdiggtest.ui.Digg.RewardFineActivity;
import com.xinyu.newdiggtest.ui.Digg.TiaozhanFineActivity;
import com.xinyu.newdiggtest.ui.Digg.target.CreateTargetActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.ui.circle.FavortItem;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.DisplayUtils;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyMiddleDialog;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.NetApiUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.view.CommentItem;
import com.xinyu.newdiggtest.view.CommentListView;
import com.xinyu.newdiggtest.widget.CommentPopupWindow;
import com.xinyu.newdiggtest.widget.PraiseListView;
import com.xinyu.newdiggtest.widget.SoftInputPopUtil;
import com.xinyu.newdiggtest.wxapi.WeixinUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
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
public class TargetDetailActivity extends BaseActivity {

    @BindView(R.id.title_name)
    public TextView title;

    @BindView(R.id.creater)
    public TextView tarCreater;//目标创建者

    @BindView(R.id.begin_time)
    public TextView begin;

    @BindView(R.id.end_time)
    public TextView endTime;

    @BindView(R.id.monitor_list)
    public EditText monitor_list;//监督人列表


    @BindView(R.id.f_fine)
    public TextView f_fine;//挑战金

    @BindView(R.id.iv_setfine)
    public ImageView iv_setfine;//设置挑战金图标


    @BindView(R.id.weeks)
    public TextView weeks;

    @BindView(R.id.dashang)
    public TextView dashang;

    @BindView(R.id.inco_right)
    public ImageView share;

    @BindView(R.id.tiaozhan)
    public TextView tiaozhan;


    @BindView(R.id.bottonview)
    public LinearLayout bottonView;


    @BindView(R.id.daka_days)
    public TextView dakaDays;

    @BindView(R.id.alartime)
    public TextView alarmTime;

    String targetId;


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

    @BindView(R.id.tv_focus)
    public TextView tv_focus;//关注/取消关注

    int foucsCount = 0;


    String reward = "0";

    @Override
    protected void onStart() {
        super.onStart();
        requestdatas();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initFragment();
        IntentFilter filter = new IntentFilter();
        filter.addAction(EventConts.MSG_Finish_TargetDetail + "");
        registerReceiver(mReceiver, filter);

    }

    /**
     * 是否是关注的目标
     */
    private void checkIfFocus() {
        //如果可见
        if (bottonView.getVisibility() == View.VISIBLE) {
            checkFocus();
        }

    }


    /**
     * 添加Fragment
     */
    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CommonDakaFragment fragment = CommonDakaFragment.newInstance(targetId, "group_target");
        transaction.add(R.id.fragment, fragment);
        transaction.commit();
    }


    private void requestdatas() {


        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("target_uuid", targetId);
        map.put("sid", PreferenceUtil.getInstance(this).getSessonId());
        url.getTargetInfo(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TargetInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(TargetInfo msg) {
                        loadingDailog.dismiss();
                        String code = msg.getOp().getCode();
                        if (code.equals("Y")) {
                            if (msg.getData() == null) {
                                return;
                            }
                            netData = msg.getData();
                            updateView(netData.getTargetplan());
                            showCommonInfo(netData);

                        } else if (code.equals("notfound")) {
                            showHasNoDialog();
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });

    }


    MyMiddleDialog myMiddleDialog;


    /**
     * 展示目标已结束的dialog
     */
    private void showHasNoDialog() {
        if (myMiddleDialog == null) {
            myMiddleDialog = new MyMiddleDialog(this, R.style.MyMiddleDialogStyle) {
                @Override
                protected View getView() {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    View view = inflater.inflate(R.layout.dialog_target_delet, null);
                    initDialogView(view);
                    return view;
                }
            };
        }
        myMiddleDialog.show();
    }

    private void initDialogView(View view) {
        view.findViewById(R.id.conform).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMiddleDialog.dismiss();
                finish();

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


    String startTime, tarEndtime, tarWeeks, moniTorNames, classId,
            targName, creater, userId, fFine = "0", superVior = "", fstate = "0";//目标是否结束状态


    private void updateView(TargetplanBean teargetplan) {

        fstate = teargetplan.getF_state();

        reward = teargetplan.getF_reward();


        targetId = teargetplan.getF_uuid();
        targName = teargetplan.getF_name();
        title.setText(targName);
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


        initPop();

        checkCreatUser(teargetplan.getF_createuser());

        checkIfFocus();

        String reward = TextUtils.isEmpty(teargetplan.getF_reward()) ? "0" : teargetplan.getF_reward();
        fFine = teargetplan.getF_fine();
        if (MyTextUtil.isEmpty(fFine) || Float.parseFloat(fFine) == 0) {
            tiaozhan.setText("￥" + 0);
            if (getIntent().hasExtra(IntentParams.TO_USER)) {
                if (getIntent().getStringExtra(IntentParams.TO_USER).equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                    f_fine.setVisibility(View.GONE);
                    iv_setfine.setVisibility(View.VISIBLE);
                    setClick(iv_setfine);
                } else {
                    iv_setfine.setVisibility(View.GONE);
                    f_fine.setVisibility(View.VISIBLE);
                    f_fine.setText("--");
                }
            } else {
                f_fine.setVisibility(View.GONE);
                iv_setfine.setVisibility(View.VISIBLE);
                setClick(iv_setfine);
            }


        } else {
            iv_setfine.setVisibility(View.GONE);
            f_fine.setVisibility(View.VISIBLE);
            tiaozhan.setText("￥" + fFine);
            f_fine.setText(fFine);
        }
        dashang.setText("￥" + reward);
        String dayy = MyTextUtil.isEmpty(teargetplan.getCount()) ? "0" : teargetplan.getCount();
        dakaDays.setText(dayy + "天");

        if (!MyTextUtil.isEmpty(teargetplan.getF_reminder_time())) {
            alarmTime.setText(teargetplan.getF_reminder_time());
        } else {
            alarmTime.setText("--");
        }


//        if (!MyTextUtil.isEmpty(teargetplan.getF_supervisor())) {
//            superVior = teargetplan.getF_supervisor();
//            try {
//                JSONArray arr = new JSONArray(teargetplan.getF_supervisor());
//                if (arr != null && arr.length() > 0) {
//                    showMonitor(arr);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            monitor_list.setText("--");
//        }


    }

    private void checkCreatUser(String f_createuser) {
        if (!MyTextUtil.isEmpty(f_createuser)) {
            if (f_createuser.equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                bottonView.setVisibility(View.GONE);
            } else {
                bottonView.setVisibility(View.VISIBLE);
            }
        } else {
            bottonView.setVisibility(View.GONE);
        }

    }

    private void setClick(ImageView iv_setfine) {
        iv_setfine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fstate.equals("1")) {
                    ToastUtils.getInstanc().showToast("目标已结束");
                    return;
                }
                Intent intent = getIntent();
                intent.setClass(mContext, SetFineActivity.class);
                intent.putExtra(IntentParams.Target_Name, title.getText().toString());
                intent.putExtra(IntentParams.STATE_DATE, startTime);
                intent.putExtra(IntentParams.END_DATE, tarEndtime);
                intent.putExtra(IntentParams.DAKA_Target_Id, targetId);
                startActivity(intent);
            }
        });

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
        moniTorNames = buffer.substring(0, buffer.length() - 1);
        monitor_list.setText(moniTorNames);
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


    private void initView() {
        targetId = getIntent().getStringExtra(IntentParams.DAKA_Target_Id);

    }

    PopupWindow popupWindow;

    LinearLayout targetEdit;

    private void initPop() {
        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.pop_share, null);
        targetEdit = view.findViewById(R.id.ll_target_edit);
        setListner(view);

        if (fstate.equals("1") || !userId.equals(PreferenceUtil.getInstance(mContext).getUserId())) {
            targetEdit.setVisibility(View.GONE);
            popupWindow = new PopupWindow(view, DisplayUtils.dp2px(this, 120),
                    DisplayUtils.dp2px(this, 50));
        } else {
            targetEdit.setVisibility(View.VISIBLE);
            popupWindow = new PopupWindow(view, DisplayUtils.dp2px(this, 120),
                    DisplayUtils.dp2px(this, 100));
        }


        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!popupWindow.isShowing()) {

                    popupWindow.showAsDropDown(share, -(DisplayUtils.dp2px(mContext, 100)),
                            DisplayUtils.dp2px(mContext, 5));

                } else {
                    popupWindow.dismiss();
                }

            }
        });
    }

    private void setListner(LinearLayout view) {

        targetEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                goEditActy();
            }
        });

        LinearLayout share = view.findViewById(R.id.ll_target_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                showPopDialog();
            }
        });


    }

    /**
     * 跳转到目标编辑/新建
     */
    private void goEditActy() {
        //挑战金不能修改，监督人可以修改
        Intent intent = getIntent();
        intent.setClass(mContext, CreateTargetActivity.class);
        intent.putExtra(IntentParams.Intent_Eidt_target, "edit");
        intent.putExtra(IntentParams.DAKA_Target_Id, targetId);
        intent.putExtra(IntentParams.Intent_Enter_Type, "TargetDetailActivity");
        intent.putExtra(IntentParams.STATE_DATE, startTime);
        intent.putExtra(IntentParams.END_DATE, tarEndtime);
        intent.putExtra(IntentParams.Target_Name, title.getText().toString());
        intent.putExtra(IntentParams.Repeat_Week, tarWeeks);

        String fine = MyTextUtil.isEmpty(fFine) ? "0" : fFine;
        intent.putExtra(IntentParams.Tareget_Fine, fine);//挑战金
        intent.putExtra(IntentParams.class_id, classId);
        intent.putExtra(IntentParams.SELECT_MONITOR, moniTorNames);
        intent.putExtra(IntentParams.SELECT_MONITORStr, superVior);

        if (!alarmTime.getText().toString().equals("--")) {
            intent.putExtra(IntentParams.Alarm_Time, alarmTime.getText().toString());
        }
        startActivity(intent);
    }

    private void showPopDialog() {
        final MyDialog dialog = new MyDialog();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, "tag");

        dialog.setOnPopListner(new MyDialog.OnPopClickListner() {
            @Override
            public void onCancle() {
                dialog.dismiss();
            }

            @Override
            public void onShareQun() {
                dialog.dismiss();
                goShareQun();

            }

            @Override
            public void onShareFriend() {
                dialog.dismiss();
                ToastUtils.getInstanc(mContext).showToast("我是分享朋友");
            }

            @Override
            public void onShareWeixin() {
                dialog.dismiss();
                getShareUrl(0);

            }

            @Override
            public void onShareCircle() {
                dialog.dismiss();
                getShareUrl(1);

            }
        });

    }

    /**
     * 分享到朋友圈
     */
    private void goShareCircle(String pageUrl) {
        WeixinUtil.getInstance().sendWebPage(mContext, pageUrl, "一起OK！",
                "您的好友" + PreferenceUtil.getInstance(this).getNickName() + "分享目标：" +
                        targName, R.mipmap.ic_launcher, true);


    }

    /**
     * 分享到群
     */
    private void goShareQun() {

        JSONObject object = new JSONObject();

        try {
            String content = targName + " " + DateUtil.timeOnlyDot(startTime) + "-" + DateUtil.timeOnlyDot(tarEndtime);
            object.put("content", content);
            object.put("f_createname", creater);//目标创建者
            object.put("type", "goal");
            object.put("user_id", userId);
            object.put("uuid", targetId);//创建者的userid
            object.put("name", targName);
            object.put("state", "1");//0 立即结算 1 等待结算
            object.put("class_id", classId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent mIntent = new Intent(mContext, SelectGroupActivity.class);
        mIntent.putExtra(IntentParams.Target_Share_Content, object.toString());//分享内容
        startActivity(mIntent);


    }


    //分享到微信

    private void getShareUrl(final int isCircle) {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_type", "customer");
        map.put("use_to", "3");//3 目标查看 4 打卡查看
        map.put("target_uuid", targetId);//3 目标查看 4 打卡查看

        url.getShareUrl(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShareUrlBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(ShareUrlBean msg) {
                        PreferenceUtil.getInstance(mContext).setWxToken(msg.getToken());
                        if (isCircle == 0) {
                            goShareWechat(msg.getUrl());
                        } else if (isCircle == 1) {
                            goShareCircle(msg.getUrl());
                        }

                    }
                });

    }


    private void goShareWechat(String url) {
        WeixinUtil.getInstance().diggWxShare
                (this, url, "一起OK！",
                        "您的好友" + PreferenceUtil.getInstance(this).getNickName() + "分享目标：" +
                                targName, R.mipmap.ic_launcher, false);
    }


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_target_detail1;
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == 100) {
            ToastUtils.getInstanc(mContext).showToast("目标分享成功！");
        }

    }


    @OnClick(R.id.tv_reward)
    public void goCommit() {
        String vip = netData.getTargetplan().getBecome_vip_date();
        if (MyTextUtil.isEmpty(vip) || vip.equals("0")) {
            NetApiUtil.sendAppVipMissNotice(mContext, "14", userId);
            ToastUtils.getInstanc().showToast("你的好友不是Vip用户，不能被奖励哦~");
            return;
        }

        Intent intent = getIntent();
        intent.setClass(mContext, RewardActivity.class);
        intent.putExtra(IntentParams.Intent_Enter_Type, "target_reward");//目标打赏
        intent.putExtra(IntentParams.IsTarget_Finish, fstate);//目标是否已结束
        intent.putExtra(IntentParams.STATE_DATE, startTime);//开始时间
        intent.putExtra(IntentParams.END_DATE, tarEndtime);//结束时间
        intent.putExtra(IntentParams.Target_State, fstate);//目标状态
        intent.putExtra(IntentParams.TO_USER, userId);

        mContext.startActivity(intent);
        finish();
    }


    @OnClick(R.id.iv_back)
    public void goback() {
        finish();
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
            requsMap.put("target_name", item.getF_name());
            requsMap.put("start_date", item.getF_start_date());
            requsMap.put("end_date", item.getF_end_date());
            requsMap.put("like_user_name", PreferenceUtil.getInstance(mContext).getNickName());
            requsMap.put("user_id", item.getF_createuser());


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
            requsMap.put("target_name", commentBean.getF_name());
            requsMap.put("start_date", commentBean.getF_start_date());
            requsMap.put("end_date", commentBean.getF_end_date());
            requsMap.put("nick_name", PreferenceUtil.getInstance(mContext).getNickName());
//            requsMap.put("user_id", commentBean.getF_toUser());
            requsMap.put("user_id", commentBean.getF_createuser());

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

    @OnClick(R.id.tv_focus)
    public void goFocus() {
        if (foucsCount <= 0) {
            focusTarget();
        } else {
            cancelFocus();
        }
    }

    /**
     * 取消关注
     */

    private void cancelFocus() {

        if (netData == null || netData.getTargetplan() == null) {
            return;
        }

        TargetplanBean commentBean = netData.getTargetplan();

        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("target_uuid", commentBean.getF_uuid());
        map.put("follow_user_id", commentBean.getF_createuser());
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("sid", PreferenceUtil.getInstance(this).getSessonId());

        url.cancelFocusTarget(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            tv_focus.setText("关注目标");
                            foucsCount = 0;

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常！");
                        }

                    }
                });

    }


    /**
     * 关注目标
     */
    private void focusTarget() {
        if (netData == null || netData.getTargetplan() == null) {
            return;
        }
        TargetplanBean commentBean = netData.getTargetplan();

        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("target_uuid", commentBean.getF_uuid());
        map.put("follow_user_id", commentBean.getF_createuser());
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("sid", PreferenceUtil.getInstance(this).getSessonId());
        map.put("start_date", commentBean.getF_start_date());
        map.put("end_date", commentBean.getF_end_date());
        map.put("target_name", commentBean.getF_name());
        map.put("nick_name", PreferenceUtil.getInstance(mContext).getNickName());

        url.focusTarget(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            tv_focus.setText("取消关注");
                            foucsCount = 1;
                        } else {
                            ToastUtils.getInstanc(App.mContext).showToast("关注失败");
                        }

                    }
                });
    }


    /**
     * 查询是否是关注
     */
    private void checkFocus() {
        if (netData == null || netData.getTargetplan() == null) {
            return;
        }
        TargetplanBean commentBean = netData.getTargetplan();

        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("target_uuid", commentBean.getF_uuid());
        map.put("follow_user_id", commentBean.getF_createuser());
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("sid", PreferenceUtil.getInstance(this).getSessonId());

        url.checkisfocusTarget(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            isfocusView(msg.getData());
                        }

                    }
                });
    }

    private void isfocusView(int count) {
        foucsCount = count;
        if (count <= 0) {
            tv_focus.setText("关注目标");
        } else {
            tv_focus.setText("取消关注");
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

    @OnClick(R.id.ll_tiaozhan)
    public void fineDetail() {

//        targetId

        Intent intent = new Intent(mContext, TiaozhanFineActivity.class);
        intent.putExtra(IntentParams.DAKA_Target_Id, targetId);
        intent.putExtra("money", fFine);


        startActivity(intent);


    }


    @OnClick(R.id.ll_reward)
    public void rewardDetail() {
        Intent intent = new Intent(mContext, RewardFineActivity.class);
        intent.putExtra(IntentParams.DAKA_Target_Id, targetId);
        intent.putExtra("money", reward);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }


    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(EventConts.MSG_Finish_TargetDetail + "")) {
                finish();
            }


        }
    };

}
