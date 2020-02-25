package com.xinyu.newdiggtest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.ViewPagerTargetAdapter;
import com.xinyu.newdiggtest.bean.ShareUrlBean;
import com.xinyu.newdiggtest.bean.TargetDataBean;
import com.xinyu.newdiggtest.bean.TargetInfo;
import com.xinyu.newdiggtest.bean.TargetplanBean;
import com.xinyu.newdiggtest.config.ActyFinishEvent;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.net.bean.InfoStr;
import com.xinyu.newdiggtest.ui.Digg.TiaozhanFineActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.MyDialog;
import com.xinyu.newdiggtest.ui.Digg.fragment.RewardActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.SelectGroupActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.TargetInfoFragment;
import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.DisplayUtils;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyMiddleDialog;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.NetApiUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.wxapi.WeixinUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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

public class TargetNewInfoActivity extends BaseNoEventActivity {


    Fragment checkResult;
    TargetInfoFragment info;

    TargetInfoDakafragment daka;

    List<Fragment> list;


    @BindView(R.id.tab)
    TabLayout tab;

    @BindView(R.id.title_name)
    TextView title_name;


    @BindView(R.id.dashang)
    TextView dashang;


    @BindView(R.id.viewpager)
    ViewPager viewpager;


    @BindView(R.id.daka_days)
    TextView daka_days;

    @BindView(R.id.tv_focus)
    TextView tv_focus;


    @BindView(R.id.tiaozhan)
    TextView tiaozhan;


    @BindView(R.id.inco_right)
    ImageView share;


    @BindView(R.id.bottonview)
    LinearLayout bottonview;


    ViewPagerTargetAdapter adapter;

    TargetplanBean targetInfo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActyFinishEvent.FromSelectQun = 0;
        EventBus.getDefault().register(this);

        requestdatas();

    }


    private void requestdatas() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("target_uuid", getIntent().getStringExtra(IntentParams.DAKA_Target_Id));
        map.put("command", "ok-api.SelectTargetDetail");
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
                            uiLoad(msg.getData());

                        } else if (code.equals("notfound")) {
                            showHasNoDialog();
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });

    }


    String become_vip_date, f_state = "0", f_isShare = "1", fine = "0";

    /**
     * 显示数据
     *
     * @param data
     */
    private void uiLoad(TargetDataBean data) {
        if (data.getTargetplan() == null)
            return;

        targetInfo = data.getTargetplan();
        become_vip_date = data.getTargetplan().getBecome_vip_date();
        f_state = data.getTargetplan().getF_state();
        targName = data.getTargetplan().getF_name();
        startTime = data.getTargetplan().getF_start_date();
        tarEndtime = data.getTargetplan().getF_end_date();
        creater = data.getTargetplan().getF_createuser();
        targetId = data.getTargetplan().getF_uuid();
        class_id = data.getTargetplan().getF_class_id();

        f_isShare = data.getTargetplan().getF_is_share();

        daka_days.setText(data.getTargetplan().getCount() + "次");
        fine = MyTextUtil.isEmpty(data.getTargetplan().getF_fine()) ? "0" : data.getTargetplan().getF_fine();
        tiaozhan.setText(fine);
        title_name.setText(MyTextUtil.getDecodeStr(targName));

        dashang.setText(data.getTargetplan().getF_reward());

        checkIfIsMyself();

        initView();

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
                leakFinish();

            }
        });

    }


    private void leakFinish() {
        startActivity(new Intent(mContext, LeakActivity.class));
        finish();
    }


    private void initView() {

        tab.post(new Runnable() {
            @Override
            public void run() {
                AppUtils.setIndicator(tab, 20, 20);
            }
        });
        list = new ArrayList<>();

        info = new TargetInfoFragment();
        info.setTargetId(getIntent().getStringExtra(IntentParams.DAKA_Target_Id));

        daka = new TargetInfoDakafragment();
        daka.setTargetId(getIntent().getStringExtra(IntentParams.DAKA_Target_Id));
        daka.setTargetInfo(targetInfo);


        checkResult = new CheckIfFinishfragment();
        ((CheckIfFinishfragment) checkResult).setTargetId(getIntent().getStringExtra(IntentParams.DAKA_Target_Id));

        list.add(info);
        list.add(daka);
        list.add(checkResult);
        adapter = new ViewPagerTargetAdapter(getSupportFragmentManager());
        adapter.setdata(list);
        tab.setupWithViewPager(viewpager);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(2);
    }


    private void checkIfIsMyself() {
        if (MyTextUtil.isEmpty(creater))
            return;

        if (creater.equals(PreferenceUtil.getInstance(mContext).getUserId())) {

            bottonview.setVisibility(View.GONE);
            initPop(0);
        } else {
            bottonview.setVisibility(View.VISIBLE);
            initPop(1);
            checkIfHaveFocus();
        }


    }


    /**
     * 查询是否是关注
     */
    private void checkIfHaveFocus() {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("target_uuid", getIntent().getStringExtra(IntentParams.DAKA_Target_Id));
        map.put("follow_user_id", creater);
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
                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        if (msg.getOp().getCode().equals("Y")) {
                            isfocusView(msg.getData());
                        }

                    }
                });
    }


    int foucsCount = 0;

    private void isfocusView(int count) {
        foucsCount = count;
        if (count <= 0) {
            tv_focus.setText("关注目标");
        } else {
            tv_focus.setText("取消关注");
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

    @OnClick(R.id.tv_reward)
    public void goCommit() {

        if (MyTextUtil.isEmpty(become_vip_date) || become_vip_date.equals("0")) {
            NetApiUtil.sendAppVipMissNotice(mContext, "14", creater);
            ToastUtils.getInstanc().showToast("你的好友不是Vip用户，不能被奖励哦~");
            return;
        }

        Intent intent = getIntent();
        intent.setClass(mContext, RewardActivity.class);
        intent.putExtra(IntentParams.Intent_Enter_Type, "target_reward");//目标打赏
        intent.putExtra(IntentParams.IsTarget_Finish, f_state);//目标是否已结束
        intent.putExtra(IntentParams.STATE_DATE, startTime);//开始时间
        intent.putExtra(IntentParams.END_DATE, tarEndtime);//结束时间
        intent.putExtra(IntentParams.Target_State, f_state);//目标状态
        intent.putExtra(IntentParams.TO_USER, creater);
        intent.putExtra(IntentParams.Target_Name, title_name.getText().toString());

        mContext.startActivity(intent);
        leakFinish();

    }


    /**
     * 取消关注
     */

    private void cancelFocus() {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("target_uuid", getIntent().getStringExtra(IntentParams.DAKA_Target_Id));
        map.put("follow_user_id", creater);
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
                            ToastUtils.getInstanc(mContext).showToast("取消成功");
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
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.AddMyfollowTargetPlan");
        map.put("target_uuid", getIntent().getStringExtra(IntentParams.DAKA_Target_Id));
        map.put("follow_user_id", creater);
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());


        url.targetFocus(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InfoStr>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(InfoStr msg) {
                        loadingDailog.dismiss();
                        String code = msg.getOp().getCode();
                        if (code.equals("Y")) {
                            tv_focus.setText("取消关注");
                            foucsCount = 1;
                            ToastUtils.getInstanc(mContext).showToast("关注成功");
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });

    }


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_target_new_info;
    }

    @OnClick(R.id.iv_back)
    public void back() {
        leakFinish();
    }


    @OnClick(R.id.ll_tiaozhan)
    public void goTiaozhan() {
        Intent intent = new Intent(mContext, TiaozhanFineActivity.class);
        intent.putExtra(IntentParams.DAKA_Target_Id, targetId);
        intent.putExtra("money", fine);
        startActivity(intent);

    }


    @OnClick(R.id.inco_right)
    public void more() {
        showDialog();
    }

    private void showDialog() {
        if (!popupWindow.isShowing()) {
            popupWindow.showAsDropDown(share, (DisplayUtils.dp2px(mContext, 30)),
                    DisplayUtils.dp2px(mContext, 30));

        } else {
            popupWindow.dismiss();
        }
    }


    PopupWindow popupWindow;

    private void initPop(int type) {

        if (f_isShare.equals("2")) {
            LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.pop_share, null);
            setListner(view, 2);
            popupWindow = new PopupWindow(view, DisplayUtils.dp2px(mContext, 120),
                    DisplayUtils.dp2px(mContext, 50));
        } else {

            if (type == 0 && f_state.equals("0")) {
                LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.pop_share, null);
                setListner(view, 0);
                popupWindow = new PopupWindow(view, DisplayUtils.dp2px(mContext, 120),
                        DisplayUtils.dp2px(mContext, 100));

            } else {
                LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.pop_share, null);
                setListner(view, 1);
                popupWindow = new PopupWindow(view, DisplayUtils.dp2px(mContext, 120),
                        DisplayUtils.dp2px(mContext, 50));
            }

        }

        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
    }


    private void setListner(LinearLayout view, int type) {
        if (type == 1) {
            view.getChildAt(0).setVisibility(View.GONE);
        } else if (type == 0) {
            view.getChildAt(0).setVisibility(View.VISIBLE);
        } else if (type == 2) {
            view.getChildAt(1).setVisibility(View.GONE);
        }

        int childCount = view.getChildCount();
        for (int i = 0; i < childCount; i++) {
            view.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int viewId = v.getId();
                    switch (viewId) {
                        case R.id.ll_target_edit:

                            Intent intent = getIntent();
                            intent.setClass(mContext, NewEditTargetActivity.class);
                            intent.putExtra(IntentParams.SELECT_MONITOR, info.moniTorNames);
                            intent.putExtra(IntentParams.STATE_DATE, info.startTime);
                            startActivity(intent);

                            popupWindow.dismiss();
                            break;


                        case R.id.ll_target_share:
                            popupWindow.dismiss();
                            showPopDialog();
                            break;


                    }

                }
            });
        }

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
                goShareQun();
                dialog.dismiss();


            }

            @Override
            public void onShareFriend() {
                dialog.dismiss();
            }

            @Override
            public void onShareWeixin() {
                dialog.dismiss();

                getShareUrl(0);
            }

            @Override
            public void onShareCircle() {
                getShareUrl(1);
                dialog.dismiss();

            }
        });

    }

    private void getShareUrl(final int isCircle) {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_type", "customer");
        map.put("use_to", "3");//3 目标查看 4 打卡查看
        map.put("target_uuid", getIntent().getStringExtra(IntentParams.DAKA_Target_Id));//3 目标查看 4 打卡查看

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
                        AppContacts.WXTag = 2;
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
                                title_name.getText().toString(), R.mipmap.ic_launcher, false);


    }

    /**
     * 分享到朋友圈
     */
    private void goShareCircle(String pageUrl) {
        WeixinUtil.getInstance().sendWebPage(mContext, pageUrl, "一起OK！",
                "您的好友" + PreferenceUtil.getInstance(this).getNickName() + "分享目标：" +
                        title_name.getText().toString(), R.mipmap.ic_launcher, true);


    }


    String targName, startTime, tarEndtime, creater, targetId, class_id;

    /**
     * 分享到群
     */
    private void goShareQun() {

        JSONObject object = new JSONObject();
        String content = "", pushStr = "";

        targName = title_name.getText().toString();

        try {
            content = targName + " " + DateUtil.timeOnlyDot(startTime) + "-" + DateUtil.timeOnlyDot(tarEndtime);
            pushStr = MyTextUtil.getUrl3Encoe(targName) + DateUtil.timeOnlyDot(startTime) + "-" + DateUtil.timeOnlyDot(tarEndtime);
            object.put("content", pushStr);
            object.put("f_createname", creater);//目标创建者
            object.put("type", "goal");
            object.put("user_id", targetId);
            object.put("uuid", targetId);//创建者的userid
            object.put("name", MyTextUtil.getUrl3Encoe(targName));
            object.put("state", "1");//0 立即结算 1 等待结算
            object.put("class_id", class_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent mIntent = new Intent(mContext, SelectGroupActivity.class);
        mIntent.putExtra(IntentParams.Target_Share_Content, object.toString());//分享内容
        mIntent.putExtra("show_Info", content);//分享内容

        startActivity(mIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == EventConts.Target_Update) {
            requestdatas();
        }
        if (event.what == EventConts.Target_End) {
            leakFinish();
        }

    }


}
