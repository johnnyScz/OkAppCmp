package com.xinyu.newdiggtest.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.lzy.ninegrid.NineGridView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.MyDakaAdapter;
import com.xinyu.newdiggtest.bean.DaKaItemBean;
import com.xinyu.newdiggtest.bean.DakaBottowItem;
import com.xinyu.newdiggtest.bean.FollowListBean;
import com.xinyu.newdiggtest.bean.HomeDakaBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.Digg.fragment.BaseFragment;
import com.xinyu.newdiggtest.ui.Digg.fragment.MsgTabActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.RewardActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.TargetDetailActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.VipIntroduceActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.VipListActivity;
import com.xinyu.newdiggtest.ui.Digg.punchcard.DakaUploadActivity;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.NetApiUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.widget.SoftInputPopUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    String[] day = new String[7];

    String[] weeks = new String[7];

    LinearLayout linearLayout;

    EasyRecyclerView recyclerView;

    SmartRefreshLayout freshLayout;
    ImageView emputview;

    List<DaKaItemBean> datalist = new ArrayList<>();


    String selectData = "";

    int indexDay = 0;//当前哪一天
    int currentDay = 1;//今天永远是第二项

    MyDakaAdapter adapter;
    List<FollowListBean> mOwnList = new ArrayList<>();
    List<FollowListBean> mfocusList = new ArrayList<>();//我的关注列表
    List<FollowListBean> totalList = new ArrayList<>();
    boolean hasCreateTitle = false;

    String pageMax = "20";


    TextView tv_msg_access;

    ImageView banner;


    @Override
    protected void initView() {
        rootView.findViewById(R.id.iv_add).setOnClickListener(this);
        linearLayout = rootView.findViewById(R.id.ll_parent);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        freshLayout = rootView.findViewById(R.id.refreshLayout);
        emputview = rootView.findViewById(R.id.emputview);

        tv_msg_access = rootView.findViewById(R.id.tv_msg_access);
        banner = rootView.findViewById(R.id.banner);

        banner.setOnClickListener(this);


        rootView.findViewById(R.id.ll_msg_access).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MsgTabActivity.class);
                mContext.startActivity(intent);
            }
        });
        rootView.setOnClickListener(this);


        initRecycle();

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NineGridView.setImageLoader(new NineGridView.ImageLoader() {
            @Override
            public void onDisplayImage(Context context, ImageView imageView, String url) {
                Glide.with(context).load(url)
                        .into(imageView);
            }

            @Override
            public Bitmap getCacheImage(String url) {
                return null;
            }
        });

        EventBus.getDefault().register(this);
        mContext.registerReceiver(mReceiver, new IntentFilter("HomeNotice_MSG"));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mContext.unregisterReceiver(mReceiver);
    }

    @Override
    protected void loadData() {

    }


    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("HomeNotice_MSG")) {
                String content = intent.getStringExtra("HomeNotice_MSG");
                if (tv_msg_access != null)
                    tv_msg_access.setText(content);

            }

        }
    };


    private void loadMore(int page, final String day) {
        dialog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("plan_date", day);
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("page_num", page + "");
        requsMap.put("page_size", pageMax);

        url.getDaka2tList(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeDakaBean>() {
                    @Override
                    public void onCompleted() {
                        freshLayout.finishLoadMore();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(HomeDakaBean msg) {
                        dialog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData() == null || msg.getData().size() == 0) {
                                ToastUtils.getInstanc(mContext).showToast("没有更多数据了...");
                            } else {
                                fillMore(msg.getData(), day);
                            }
                        } else {
                            ToastUtils.getInstanc(mContext).showToast(mContext.getResources().getString(R.string.network_error));

                        }

                    }
                });
    }

    /**
     * 加载更多数据
     *
     * @param data
     * @param day
     */
    private void fillMore(List<DaKaItemBean> data, String day) {
        int len = data.size();
        for (int i = 0; i < len; i++) {
            DaKaItemBean daKaItemBean = data.get(i);
            String headUrl = daKaItemBean.getHead();
            String childUid = daKaItemBean.getUser_id();
            if (daKaItemBean.getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                if (daKaItemBean.getFollow_list() != null && daKaItemBean.getFollow_list().size() > 0) {
                    setDataType(daKaItemBean.getFollow_list(), childUid, headUrl);
                    mOwnList.addAll(daKaItemBean.getFollow_list());
                }
            } else {
                String name = MyTextUtil.isEmpty(daKaItemBean.getUser_name())
                        ? daKaItemBean.getNick_name() : daKaItemBean.getUser_name();
                String url = daKaItemBean.getHead();

                int isVip = MyTextUtil.isEmpty(daKaItemBean.getBecome_vip_date()) || daKaItemBean.getBecome_vip_date().equals("0") ? 0 : 1;


                List<FollowListBean> mlist = daKaItemBean.getFollow_list();
                if (mlist == null || mlist.size() == 0)
                    return;

                int mlen = mlist.size();
                for (int k = 0; k < mlen; k++) {
                    FollowListBean bean = mlist.get(k);
                    bean.setTyep(3);
                    bean.setName(name);
                    bean.setIsVip(isVip);
                    bean.setF_toUser(childUid);
                    bean.setHead(daKaItemBean.getHead());
                    bean.setImgUrl(url);
                }
                mfocusList.addAll(mlist);
            }
        }

        totalList.clear();
        totalList.addAll(mOwnList);
        totalList.addAll(mfocusList);
        adapter.setNewData(totalList);

//        adapter.addData();

    }

    private void setDataType(List<FollowListBean> follow_list, String uid, String head) {
        for (FollowListBean ii : follow_list) {
            ii.setTyep(1);
            ii.setF_toUser(uid);
            ii.setHead(head);
        }
    }


    private void getPageDatas(int page, final String day) {
        //TODO getPage
        dialog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);


        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("plan_date", day);
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("page_num", page + "");
        requsMap.put("page_size", pageMax);

        url.getDaka2tList(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeDakaBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(HomeDakaBean msg) {
                        dialog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData() == null || msg.getData().size() == 0) {
                                freshLayout.setVisibility(View.GONE);
                                emputview.setVisibility(View.VISIBLE);
                            } else {
                                freshLayout.setVisibility(View.VISIBLE);
                                emputview.setVisibility(View.GONE);
                                setDatas(msg.getData(), day);
                            }
                        } else {
                            ToastUtils.getInstanc(mContext).showToast(mContext.getResources().getString(R.string.network_error));

                        }

                    }
                });
    }


    private void setDatas(List<DaKaItemBean> data, String day) {
        totalList.clear();
        mfocusList.clear();
        mOwnList.clear();
        hasCreateTitle = false;

        int len = data.size();
        for (int i = 0; i < len; i++) {
            if (data.get(i).getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                mOwnList = addUserId(data.get(i).getFollow_list(), data.get(i));
            } else {
                convertItemData(data.get(i));
            }
        }

        totalList.addAll(mOwnList);
        totalList.addAll(mfocusList);

        adapter = new MyDakaAdapter(totalList, mContext);
        adapter.setDakaDate(day);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                switch (id) {
                    case R.id.rl_item1:
                        goDakaInfo(totalList.get(position));
                        break;

                    case R.id.img_check:
                        Intent intent = new Intent(mContext, DakaUploadActivity.class);
                        intent.putExtra(IntentParams.SELECT_Target_Name, totalList.get(position).getF_target_name());
                        intent.putExtra(IntentParams.SELECT_Target_TIME, totalList.get(position).getF_plan_date());
                        intent.putExtra(IntentParams.DAKA_Target_Id, totalList.get(position).getF_plan_id());
                        //TODO 勾选状态后面会改
                        intent.putExtra(IntentParams.DAKA_State, "1");
                        mContext.startActivity(intent);
                        break;

                    case R.id.iv_target_icon:
                        goMySpace(totalList.get(position));
                        break;


                    case R.id.tv_descrep:
                    case R.id.tv_daka_title:
                        goTargetDetail(totalList.get(position));
                        break;

                }

            }
        });


        adapter.setPopListner(new MyDakaAdapter.onPopClick() {
            @Override
            public void onZanClick(FollowListBean item, String content) {
                if (content.equals("赞")) {
                    dianZan(item);
                } else if (content.equals("取消")) {
                    canCelZan(item);
                }

            }

            @Override
            public void onDaShangClick(FollowListBean item) {
                if (item.getIsVip() != 1) {

                    NetApiUtil.sendAppVipMissNotice(mContext, "13", item.getF_toUser());

                    ToastUtils.getInstanc().showToast("您的好友不是vip用户，不能被打赏哦~");
                    return;
                }
                Intent intent = new Intent(mContext, RewardActivity.class);
                intent.putExtra(IntentParams.Target_Name, item.getF_target_name());
                intent.putExtra(IntentParams.DAKA_Target_Id, item.getF_plan_id());
                intent.putExtra(IntentParams.STATE_DATE, item.getF_target_start_date());
                intent.putExtra(IntentParams.END_DATE, item.getF_target_end_date());
                intent.putExtra(IntentParams.Intent_Enter_Type, "daka_dashang");//打卡打赏
                intent.putExtra(IntentParams.TO_USER, item.getF_toUser());
                mContext.startActivity(intent);
            }

            @Override
            public void onCommentClick(final FollowListBean item, View view) {

                SoftInputPopUtil.liveCommentEdit(mContext, view, new SoftInputPopUtil.liveCommentResult() {
                    @Override
                    public void onResult(boolean confirmed, String comment) {
                        doRequestComment(item, comment);
                    }
                });


            }
        });


        recyclerView.setAdapter(adapter);
    }


    /**
     * 跳转到目标详情
     *
     * @param followListBean
     */
    private void goTargetDetail(FollowListBean followListBean) {
        Intent intent = new Intent(mContext, TargetDetailActivity.class);
        intent.putExtra(IntentParams.DAKA_Target_Id, followListBean.getF_target_uuid());
        intent.putExtra(IntentParams.USER_ID, followListBean.getF_toUser());

        intent.putExtra(IntentParams.Target_Name, followListBean.getF_target_name());
        startActivity(intent);
    }

    /**
     * 前往打卡详情
     *
     * @param followListBean
     */
    private void goDakaInfo(FollowListBean followListBean) {


        Intent intent = new Intent(mContext, DakaInfoActivity.class);

//        intent.putExtra(IntentParams.IsVIP, followListBean.getIsVip() + "");//是否是Vip
//        intent.putExtra(IntentParams.CreatUserName, followListBean.getCratename());
//        intent.putExtra(IntentParams.HAVE_daka, followListBean.getF_state());
//        intent.putExtra(IntentParams.Target_Name, followListBean.getF_target_name());
        intent.putExtra(IntentParams.DAKA_Target_Id, followListBean.getF_plan_id());
//        intent.putExtra(IntentParams.STATE_DATE, followListBean.getF_target_start_date());
//        intent.putExtra(IntentParams.END_DATE, followListBean.getF_target_end_date());
//        intent.putExtra(IntentParams.Intent_Enter_Type, "daka_dashang");//打卡打赏
        intent.putExtra(IntentParams.TO_USER, followListBean.getF_toUser());
//        intent.putExtra(IntentParams.class_id, followListBean.getF_class_id());
//        intent.putExtra("creater", followListBean.getNick_name());
//        AppContacts.DAKA_User_Head = followListBean.getHead();

        mContext.startActivity(intent);

    }

    /**
     * 去个人空间
     *
     * @param followListBean
     */
    private void goMySpace(FollowListBean followListBean) {

//        Intent mIntent = new Intent(ctx, MySpaceActivity.class);
//        mIntent.putExtra(IntentParams.USER_ID, followListBean.getF_toUser());
//        mIntent.putExtra(IntentParams.USER_head, followListBean.getImgUrl());
//        mIntent.putExtra(IntentParams.UserName, followListBean.getName());
//        mIntent.putExtra(IntentParams.STATE, "0");
//        startActivity(mIntent);
    }

    private List<FollowListBean> addUserId(List<FollowListBean> follow_list, DaKaItemBean daka) {
        for (FollowListBean item : follow_list) {
            item.setF_toUser(daka.getUser_id());
            String name = MyTextUtil.isEmpty(daka.getNick_name()) ? daka.getUser_name() : daka.getNick_name();
            item.setCratename(name);
        }

        return follow_list;
    }


    /**
     * 点赞
     *
     * @param item
     */
    private void dianZan(FollowListBean item) {
        if (item != null) {

            RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
            AppUrl api = manager.create(AppUrl.class);

            HashMap<String, String> requsMap = new HashMap<>();
            requsMap.put("object_id", item.getF_plan_id());
            requsMap.put("like_user_id", PreferenceUtil.getInstance(mContext).getUserId());
            requsMap.put("type", "1");
            requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
            requsMap.put("target_name", item.getF_target_name());
            requsMap.put("start_date", item.getF_target_start_date());
            requsMap.put("end_date", item.getF_target_end_date());
            requsMap.put("like_user_name", PreferenceUtil.getInstance(mContext).getNickName());
            requsMap.put("user_id", item.getF_executor());

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
                                getPageDatas(1, selectData);

                            } else {
                                ToastUtils.getInstanc(mContext).showToast("点赞失败");
                            }
                        }
                    });


        }


    }


    /**
     * 取消点赞
     *
     * @param item
     */
    private void canCelZan(FollowListBean item) {

        List<DakaBottowItem> fidList = item.getTargetlikes();
        String fid = "";
        for (DakaBottowItem ta : fidList) {
            if (ta.getF_like_user().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                fid = ta.getF_uuid();
                break;
            }

        }

        if (item != null) {

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
//                            ToastUtils.getInstanc(mContext).showToast(e.getMessage());

                        }

                        @Override
                        public void onNext(Info msg) {
                            if (msg.getOp().getCode().equals("Y")) {
                                ToastUtils.getInstanc(mContext).showToast("取消成功");
                                getPageDatas(1, selectData);
                            } else {
                                ToastUtils.getInstanc(mContext).showToast("服务异常");
                            }
                        }
                    });


        }


    }


    private void convertItemData(DaKaItemBean daKaItemBean) {
        if (!hasCreateTitle) {
            hasCreateTitle = true;
            FollowListBean bean = new FollowListBean();
            bean.setTyep(2);
            mfocusList.add(bean);

        }
        String name = MyTextUtil.isEmpty(daKaItemBean.getNick_name()) ? daKaItemBean.getUser_name() : daKaItemBean.getNick_name();
        String url = daKaItemBean.getHead();

        String isVip = daKaItemBean.getBecome_vip_date();

        int vip = (MyTextUtil.isEmpty(isVip) || isVip.equals("0")) ? 0 : 1;

        List<FollowListBean> mlist = daKaItemBean.getFollow_list();
        int len = mlist.size();
        for (int i = 0; i < len; i++) {
            FollowListBean bean = mlist.get(i);
            bean.setTyep(3);
            bean.setIsVip(vip);
            bean.setName(name);
            bean.setCratename(name);
            bean.setF_toUser(daKaItemBean.getUser_id());
            bean.setHead(daKaItemBean.getHead());
            bean.setImgUrl(url);
        }
        mfocusList.addAll(mlist);
    }

    int pageCount = 1;


    @Override
    public void onStart() {
        super.onStart();
        hasCreateTitle = false;
        datalist.clear();
        pageCount = 1;
        selectData = DateUtil.getCurrentData();
        initCalandar();
        getPageDatas(1, selectData);

    }


    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器


        freshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageCount++;
                loadMore(pageCount, selectData);
            }
        });


    }


    private void initCalandar() {
        String yestaday = DateUtil.getYestaday();
        String currentDay = DateUtil.getCurrentDay();
        String tomorro = DateUtil.getAfter5(1);
        String tomorroAdd1 = DateUtil.getAfter5(2);
        String tomorroAdd2 = DateUtil.getAfter5(3);
        String tomorroAdd3 = DateUtil.getAfter5(4);
        String tomorroAdd4 = DateUtil.getAfter5(5);

        day[0] = yestaday;
        day[1] = currentDay;
        day[2] = tomorro;
        day[3] = tomorroAdd1;
        day[4] = tomorroAdd2;
        day[5] = tomorroAdd3;
        day[6] = tomorroAdd4;

        setWeeks();

        initClick();

        setCalendarDatas();
    }


    private void initClick() {
        final int len = linearLayout.getChildCount();
        for (int i = 0; i < len; i++) {
            LinearLayout child = (LinearLayout) linearLayout.getChildAt(i);
            final int finalI = i;
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    indexDay = finalI;
                    resetView(len);
                    v.setBackgroundResource(R.drawable.inco_bg);
                    setCurrentDay(indexDay);
                }
            });

        }

    }


    private void setCurrentDay(int index) {
        selectData = DateUtil.getSelectDay(index - currentDay);
        datalist.clear();
        hasCreateTitle = false;
        getPageDatas(1, selectData);
        pageCount = 1;
    }

    private void resetView(int len) {
        for (int i = 0; i < len; i++) {
            LinearLayout child = (LinearLayout) linearLayout.getChildAt(i);
            child.setBackgroundResource(R.color.windowBg);
        }
    }

    private void setCalendarDatas() {
        int len = linearLayout.getChildCount();
        for (int i = 0; i < len; i++) {
            LinearLayout child = (LinearLayout) linearLayout.getChildAt(i);
            if (i == 1) {

            } else {

                child.setVisibility(View.VISIBLE);
                TextView weekday = (TextView) child.getChildAt(0);
                TextView dayCont = (TextView) child.getChildAt(1);
                weekday.setText(weeks[i]);
                dayCont.setText(day[i]);

            }


        }
    }

    private void setWeeks() {
        String todayWeek = DateUtil.getWeekNO();
        switch (todayWeek) {
            case "日":
                weeks = new String[]{"六", "日", "一", "二", "三", "四", "五"};
                break;

            case "一":
                weeks = new String[]{"日", "一", "二", "三", "四", "五", "六"};
                break;

            case "二":
                weeks = new String[]{"一", "二", "三", "四", "五", "六", "日"};
                break;

            case "三":
                weeks = new String[]{"二", "三", "四", "五", "六", "日", "一"};
                break;

            case "四":
                weeks = new String[]{"三", "四", "五", "六", "日", "一", "二"};
                break;

            case "五":
                weeks = new String[]{"四", "五", "六", "日", "一", "二", "三"};
                break;

            case "六":
                weeks = new String[]{"五", "六", "日", "一", "二", "三", "四"};
                break;


        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == EventConts.DAKA_Publishi) {
            datalist.clear();
            pageCount = 1;
            getPageDatas(1, selectData);
        } else if (event.what == EventConts.MSG_Home_ResetDate) {//时间重置为当天
            pageCount = 1;
            selectData = DateUtil.getCurrentData();

            int len = linearLayout.getChildCount();
            for (int i = 0; i < len; i++) {
                LinearLayout child = (LinearLayout) linearLayout.getChildAt(i);
                child.setBackgroundResource(R.color.windowBg);
            }

            linearLayout.getChildAt(1).setBackgroundResource(R.drawable.inco_bg);
        }
    }


    @Override
    protected void reLoadData() {

    }

    @Override
    protected int getResId() {
        return R.layout.fragment_daka_layout;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_add:

//                selectData = DateUtil.getCurrentData();
//                Intent intent = new Intent(mContext, DakaSelectTargetActivity.class);
//                intent.putExtra(IntentParams.Intent_Enter_Type, "HomeFragment");
//                intent.putExtra(IntentParams.SELECT_DATA, selectData);
//                startActivity(intent);

//                startActivity(new Intent(mContext, DingdingActivity.class));

                break;

            case R.id.ll_root:

                break;

            case R.id.banner:
                if (!PreferenceUtil.getInstance(mContext).getIsVip()) {
                    Intent intent1 = new Intent(mContext, VipIntroduceActivity.class);
                    intent1.putExtra(IntentParams.IsVIP, "N");
                    startActivity(intent1);
                } else {
                    startActivity(new Intent(mContext, VipListActivity.class));
                }


                break;

        }
    }


    /**
     * 添加用户评论
     */
    private void doRequestComment(FollowListBean commentBean, String comments) {
        if (commentBean != null) {
            String content = "";
            try {
                content = URLEncoder.encode(comments, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
            AppUrl api = manager.create(AppUrl.class);


            HashMap<String, String> requsMap = new HashMap<>();
            requsMap.put("object_id", commentBean.getF_plan_id());
            requsMap.put("comment", content);
            requsMap.put("comment_user_id", PreferenceUtil.getInstance(mContext).getUserId());
            requsMap.put("type", "1");
            requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
            requsMap.put("target_name", commentBean.getF_target_name());
            requsMap.put("start_date", commentBean.getF_target_start_date());
            requsMap.put("end_date", commentBean.getF_target_end_date());
            requsMap.put("nick_name", PreferenceUtil.getInstance(mContext).getNickName());
//            requsMap.put("user_id", commentBean.getF_toUser());
            requsMap.put("user_id", commentBean.getF_executor());

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
                                ToastUtils.getInstanc(mContext).showToast("评论成功");

                                getPageDatas(1, selectData);

                            } else {
                                ToastUtils.getInstanc(mContext).showToast("评论失败");
                            }
                        }
                    });

        }
    }


    Context ctx;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
    }


}
