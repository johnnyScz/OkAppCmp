//package com.xinyu.newdiggtest.ui.calendar;
//
//import android.Manifest;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.codbking.calendar.CaledarAdapter;
//import com.codbking.calendar.CalendarBean;
//import com.codbking.calendar.CalendarDateView;
//import com.codbking.calendar.CalendarLayout;
//import com.codbking.calendar.CalendarMyUtil;
//import com.codbking.calendar.CalendarView;
//import com.google.zxing.activity.CaptureActivity;
//import com.lzy.ninegrid.NineGridView;
//import com.squareup.picasso.Picasso;
//import com.tbruyelle.rxpermissions2.RxPermissions;
//import com.xinyu.newdiggtest.APPConstans;
//import com.xinyu.newdiggtest.R;
//import com.xinyu.newdiggtest.adapter.MyExpandableListAdapter;
//import com.xinyu.newdiggtest.adapter.MyDakaAdapter;
//import com.xinyu.newdiggtest.bean.AnswerBean;
//import com.xinyu.newdiggtest.bean.AskTopBean;
//import com.xinyu.newdiggtest.bean.BaseUser;
//import com.xinyu.newdiggtest.bean.CompanyBean;
//import com.xinyu.newdiggtest.bean.DingRetBean;
//import com.xinyu.newdiggtest.bean.DingReturnBean;
//import com.xinyu.newdiggtest.bean.FollowListBean;
//import com.xinyu.newdiggtest.bean.FollowTestBean;
//import com.xinyu.newdiggtest.bean.MobieCompanyBean;
//import com.xinyu.newdiggtest.bean.MsgNewHomeBean;
//import com.xinyu.newdiggtest.bean.MsgRedBean;
//import com.xinyu.newdiggtest.bean.RoomPersonListBean;
//import com.xinyu.newdiggtest.bean.TodoDingBean;
//
//import com.xinyu.newdiggtest.config.ActyFinishEvent;
//import com.xinyu.newdiggtest.h5.LoadingActivity;
//import com.xinyu.newdiggtest.net.AppUrl;
//import com.xinyu.newdiggtest.net.NetApi;
//import com.xinyu.newdiggtest.net.RetrofitServiceManager;
//import com.xinyu.newdiggtest.net.bean.InfoStr;
//import com.xinyu.newdiggtest.ui.App;
//import com.xinyu.newdiggtest.ui.Digg.fragment.BaseFragment;
//import com.xinyu.newdiggtest.ui.Digg.fragment.MsgNewTabActivity;
//import com.xinyu.newdiggtest.ui.Digg.fragment.RewardActivity;
//import com.xinyu.newdiggtest.ui.Digg.fragment.TargetCheckActivity;
//import com.xinyu.newdiggtest.ui.Digg.fragment.TodoAddActivity;
//import com.xinyu.newdiggtest.ui.Digg.fragment.VipIntroduceActivity;
//import com.xinyu.newdiggtest.ui.Digg.punchcard.DakaUploadActivity;
//import com.xinyu.newdiggtest.ui.MySpaceActivity;
//import com.xinyu.newdiggtest.ui.TargetNewInfoActivity;
//import com.xinyu.newdiggtest.ui.XshellEvent;
//import com.xinyu.newdiggtest.ui.chat.PersonChatActivity;
//import com.xinyu.newdiggtest.ui.chat.SuiShouNoteActivity;
//import com.xinyu.newdiggtest.utils.AppContacts;
//import com.xinyu.newdiggtest.utils.DateUtil;
//import com.xinyu.newdiggtest.utils.DisplayUtils;
//import com.xinyu.newdiggtest.utils.EventConts;
//import com.xinyu.newdiggtest.utils.IntentParams;
//import com.xinyu.newdiggtest.utils.MyTextUtil;
//import com.xinyu.newdiggtest.utils.NetApiUtil;
//import com.xinyu.newdiggtest.utils.PreferenceUtil;
//import com.xinyu.newdiggtest.utils.ToastUtils;
//import com.xinyu.newdiggtest.widget.SoftInputPopUtil;
//import com.xinyu.newdiggtest.widget.WrapContentLinearLayoutManager;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import io.reactivex.functions.Consumer;
//import rx.Observable;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
//import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
//import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
//import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;
//import static com.xinyu.newdiggtest.ui.calendar.HomeAdapter.SELECTION_Title;
//
//
//public class DingdingFragment extends BaseFragment implements View.OnClickListener {
//
//    CalendarDateView mCalendarDateView;
//    RecyclerView mList;
//
//    TextView mTitle;
//    ImageView img, emptyView;
//    ImageView share;
//    CalendarLayout clipLayout;
//
//    HomeCalendarNetUtil dateListner;
//
//    List<FollowTestBean> tempDatas = new ArrayList<>();
//
//    List<FollowTestBean> SXOnlyOld = new ArrayList<>();//只有私聊的老数据
//
//    List<FollowTestBean> MsgOnly = new ArrayList<>();//只有消息
//
//    List<FollowTestBean> toduOnly = new ArrayList<>();//只有待办
//
//    List<FollowTestBean> focusOnly = new ArrayList<>();//只有关注
//
//
//    HomeAdapter adapter;
//
//    String selectDataStr = "";
//
//    int ScrollState = 0;//滑动状态
//
//    boolean isLunxun = false;
//
//    int insertIdex = 0;
//
//    int toduCount = 0;//待办的条数
//
//
//    int isfocus = 0;//0 待办  1 关注
//
//
//    Map<String, String> mySelfDaka = new HashMap<>();
//
//    private void createInit() {
//        initBindView();
//        mCalendarDateView.setAdapter(new CaledarAdapter() {
//            @Override
//            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
//                TextView view, chinaDay;
//                if (convertView == null) {
//                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar, null);
//                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(px(48), px(48));
//                    convertView.setLayoutParams(params);
//                }
//
//                view = (TextView) convertView.findViewById(R.id.text);
//                chinaDay = (TextView) convertView.findViewById(R.id.chinaday);
//
//                if (bean.mothFlag != 0) {
//                    view.setTextColor(0xff9299a1);
//                } else {
//                    view.setTextColor(Color.parseColor("#cc000000"));
//                }
//
//                view.setTag(bean.mothFlag);
//                view.setText("" + bean.day);
//                chinaDay.setText(bean.chinaDay);
//
//                checkIfToday(bean, view, chinaDay);
//
//                return convertView;
//            }
//        });
//
//        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int postion, CalendarBean bean) {
//                mTitle.setText(getDisPlayNumber(bean.moth) + "月" + getDisPlayNumber(bean.day) + "日" + "  " + getWeekStr(bean.week));
//
//                selectDataStr = getDisPlayNumber(bean.year) + "-" + getDisPlayNumber(bean.moth) + "-" + getDisPlayNumber(bean.day);
//
//                /**
//                 * 我的数据
//                 */
//                reload();
//
//            }
//
//            @Override
//            public void onItemColorSelect(View view, CalendarBean bean) {
//                TextView text = view.findViewById(R.id.text);
//                TextView chinaday = view.findViewById(R.id.chinaday);
//                text.setTextColor(getResources().getColor(R.color.white));
//                chinaday.setTextColor(getResources().getColor(R.color.white));
//
//            }
//
//            @Override
//            public void onItemColorReset(View view, CalendarBean bean) {
//                TextView text = view.findViewById(R.id.text);
//                TextView chinaday = view.findViewById(R.id.chinaday);
//                chinaday.setTextColor(getResources().getColor(R.color.gray_color99));
//
//                int tag = (int) text.getTag();
//                if (tag != 0) {
//                    text.setTextColor(0xff9299a1);
//                } else {
//                    text.setTextColor(Color.parseColor("#cc000000"));
//                }
//            }
//
//
//        });
//
//
//        int[] data = CalendarMyUtil.getYMD(new Date());
//        mTitle.setText(data[1] + "月" + data[2] + "日" + "  " + getWeekStr(data[3]));
//
//        selectDataStr = DateUtil.getTodayStr();
//
//        clipLayout.setSwitchListner(new CalendarLayout.SlidListner() {
//            @Override
//            public void onOpen() {
//                img.setImageResource(R.drawable.arrow_u);
//            }
//
//            @Override
//            public void onFold() {
//                img.setImageResource(R.drawable.arrow_b);
//            }
//        });
//
//        initRecycleView();
//
////        initFoldType();
//
//        initDataListner();
//
//        initPop();
//
//
//        initCompanyView();
//    }
//
//
//    private void initBindView() {
//        mCalendarDateView = rootView.findViewById(R.id.calendarDateView);
//        mList = rootView.findViewById(R.id.list);
//        mTitle = rootView.findViewById(R.id.tv_title);
//        emptyView = rootView.findViewById(R.id.empty);
//
//        img = rootView.findViewById(R.id.arrow);
//        share = rootView.findViewById(R.id.add);
//
//        clipLayout = rootView.findViewById(R.id.clipse_ll);
//
//        rootView.findViewById(R.id.ll_slide).setOnClickListener(this);
//
//        rootView.findViewById(R.id.swtich_qy).setOnClickListener(this);
//
//
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (AppContacts.BackFromH5 == 1) {//阅后即焚
//            AppContacts.BackFromH5 = 0;
//            getCompanyInfoByMobile(PreferenceUtil.getInstance(mContext).getPhone());
//        }
//    }
//
//    @Override
//    protected void initView() {
//        createInit();
//    }
//
//    @Override
//    protected void loadData() {
//        requestAllData(true);
//        getCompanyInfoByMobile(PreferenceUtil.getInstance(mContext).getPhone());
//
//    }
//
//    @Override
//    protected void reLoadData() {
//
//    }
//
//    @Override
//    protected int getResId() {
//        return R.layout.activity_dingding1;
//    }
//
//
//    private void initDataListner() {
//
//        dateListner = new HomeCalendarNetUtil().getInstance(mContext);
//
//        dateListner.setDataListner(new HomeCalendarNetUtil.OnPuchDataListner() {
//            @Override
//            public void onSxData(List<RoomPersonListBean> dateList) {
//
//                if (SXOnlyOld != null) {
//                    SXOnlyOld.clear();
//                } else {
//                    SXOnlyOld = new ArrayList<>();
//                }
//
//                if (dateList != null && dateList.size() > 0) {
//                    List<RoomPersonListBean> list = filter(dateList);
//                    if (list != null && list.size() > 0) {
//                        FollowTestBean item = new FollowTestBean();
//                        item.setViewType(5);
//                        item.setTitleName("私信");
//                        item.setReqestDataType(2);
//                        SXOnlyOld.add(item);
//                        for (RoomPersonListBean kk : list) {
//                            FollowTestBean tyy = new FollowTestBean();
//                            tyy.setViewType(2);
//                            item.setReqestDataType(2);
//                            tyy.setSxBean(kk);
//                            SXOnlyOld.add(tyy);
//                        }
//                    }
//
//                }
//
//                dateListner.getLatestMsg(selectDataStr);
//            }
//
//            @Override
//            public void onMsgFeed(List<MsgNewHomeBean> dataList) {
//                if (MsgOnly != null) {
//                    MsgOnly.clear();
//                } else {
//                    MsgOnly = new ArrayList<>();
//                }
//                if (dataList != null && dataList.size() > 0) {
//                    FollowTestBean item = new FollowTestBean();
//                    item.setViewType(5);
//                    item.setTitleName("消息");
//                    item.setReqestDataType(4);
//                    MsgOnly.add(item);
//
//                    for (MsgNewHomeBean tt : dataList) {
//                        FollowTestBean msg = new FollowTestBean();
//                        msg.setViewType(4);
//                        item.setReqestDataType(4);
//                        msg.setMsgBean(tt);
//                        MsgOnly.add(msg);
//                    }
//                }
//                dateListner.getTodoList(selectDataStr);
//
//            }
//
//            @Override
//            public void onTodoData(List<TodoDingBean> list) {
//
//                putData(list);
//                //TODO 干掉和打开
////                goUIload();
//                dateListner.getFocusList(selectDataStr);
//            }
//
//            @Override
//            public void onFocusData(List<FollowListBean> list) {
//                dialog.dismiss();
//
//                if (focusOnly != null) {
//                    focusOnly.clear();
//                } else {
//                    focusOnly = new ArrayList<>();
//                }
//
//
//                if (list != null && list.size() > 0) {
//                    FollowTestBean item = new FollowTestBean();
//                    item.setViewType(5);
//                    item.setReqestDataType(3);
//                    item.setTitleName("关注动态");
//                    focusOnly.add(item);
//                    for (FollowListBean kk : list) {
//                        FollowTestBean ff = new FollowTestBean();
//                        ff.setViewType(3);
//                        item.setReqestDataType(3);
//                        ff.setFocusBean(kk);
//                        focusOnly.add(ff);
//                    }
//                }
//
//                goUIload();
//
//            }
//
//
//        });
//    }
//
//
//    /**
//     * 接口刷新数据
//     */
//    private void goUIload() {
//        dialog.dismiss();
//        if (ScrollState != 0)
//            return;
//
//        if (SXOnlyOld.size() > 0) {
//            tempDatas.addAll(SXOnlyOld);
//        }
//        if (MsgOnly.size() > 0) {
//            tempDatas.addAll(MsgOnly);
//        }
//        if (toduOnly.size() > 0) {
//            tempDatas.addAll(toduOnly);
//        }
//        if (focusOnly.size() > 0) {
//            tempDatas.addAll(focusOnly);
//        }
//        if (tempDatas.size() > 0) {
//            List<FollowTestBean> allTypeData = getNewDatas();
//            adapter = new HomeAdapter(allTypeData, mContext);
//            mList.setAdapter(adapter);
//
//            emptyView.setVisibility(View.GONE);
//
//            final CommonLikesNetUtil api = new CommonLikesNetUtil().getInstance(mContext);
//
//            initAdapterItemListner(adapter);
//
//
//            //待办 点赞评论
//            adapter.setDakaPopListner(new MyDakaAdapter.onPopClick() {
//                @Override
//                public void onZanClick(FollowListBean item, String content) {
//
//                    String answerid = item.getAnswerId();
//
//                    if (MyTextUtil.isEmpty(answerid)) {
//
//                        isfocus = 1;
//                    } else {
//                        isfocus = 0;
//
//                    }
//
//
//                    if (item == null)
//                        return;
//
//                    if (content.equals("赞")) {
//                        api.dianZan(item, isfocus);
//
//                    } else if (content.equals("取消")) {
//                        api.canCelZan(item, isfocus);
//                    }
//
//
//                }
//
//                @Override
//                public void onDaShangClick(FollowListBean item) {
//
//                    if (item == null)
//                        return;
//
//                    if (MyTextUtil.isEmpty(item.getUserBean().getBecome_vip_date())) {
//                        NetApiUtil.sendAppVipMissNotice(mContext, "13", item.getF_executor());
//                        ToastUtils.getInstanc().showToast("您的好友不是vip用户，不能被打赏哦~");
//                        return;
//                    }
//                    Intent intent = new Intent(mContext, RewardActivity.class);
//
//                    String names = MyTextUtil.isEmpty(item.getF_target_name()) ? item.getF_title() : item.getF_target_name();
//                    intent.putExtra(IntentParams.Target_Name, MyTextUtil.getDecodeStr(names));
//                    intent.putExtra(IntentParams.DAKA_Target_Id, item.getF_plan_id());
//                    intent.putExtra(IntentParams.STATE_DATE, item.getF_target_start_date());
//                    intent.putExtra(IntentParams.END_DATE, item.getF_target_end_date());
//                    intent.putExtra(IntentParams.Intent_Enter_Type, "daka_dashang");//打卡打赏
//                    intent.putExtra(IntentParams.TO_USER, item.getF_executor());
//                    mContext.startActivity(intent);
//                }
//
//                @Override
//                public void onCommentClick(final FollowListBean item, View view) {
//
//                    if (item == null)
//                        return;
//
//
//                    String answerid = item.getAnswerId();
//
//                    if (MyTextUtil.isEmpty(answerid)) {
//                        isfocus = 1;
//                    } else {
//                        isfocus = 0;
//                    }
//
//
//                    SoftInputPopUtil.liveCommentEdit(mContext, view, new SoftInputPopUtil.liveCommentResult() {
//                        @Override
//                        public void onResult(boolean confirmed, String comment) {
//
//                            api.addComment(item, comment, isfocus);
//                        }
//                    });
//
//
//                }
//            });
//
//
//        } else {
//            emptyView.setVisibility(View.VISIBLE);
//        }
//    }
//
//
//    private List<FollowTestBean> getNewDatas() {
//        List<FollowTestBean> datas = new ArrayList<>();
//        for (FollowTestBean item : tempDatas) {
//            datas.add(item);
//        }
//        return datas;
//    }
//
//
//    /**
//     * count =0 或者不是今天的消息不展示
//     *
//     * @param dateList
//     * @return
//     */
//    private List<RoomPersonListBean> filter(List<RoomPersonListBean> dateList) {
//        List<RoomPersonListBean> newList = new ArrayList<>();
//        for (RoomPersonListBean orignal : dateList) {
//            if (check(orignal)) {
//                newList.add(orignal);
//            }
//        }
//        return newList;
//    }
//
//    private boolean check(RoomPersonListBean orignal) {
//        if (orignal.getDetail() != null) {
//            if (Integer.parseInt(orignal.getDetail().getCount()) != 0 && checkTime(orignal)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    private boolean checkTime(RoomPersonListBean orignal) {
//
//        String timeStr = DateUtil.longToDay(Long.parseLong(orignal.getMsg_time()));
//
//        if (!MyTextUtil.isEmpty(timeStr) && timeStr.length() > 9) {
//            if (timeStr.equals(selectDataStr)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    private void initAdapterItemListner(HomeAdapter adapter) {
//
//        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//
//                FollowTestBean item = (FollowTestBean) adapter.getData().get(position);
//
//                int id = view.getId();
//                switch (id) {
//                    case R.id.rl_item1://待办编辑
//                        AskTopBean daiban = item.getAskTopBean();
//                        if (daiban == null)
//                            return;
//
//                        if (daiban.getFrom_user() != null && !daiban.getFrom_user().getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
//                            return;
//                        }
//
//
//                        goEditDaiban(daiban);
//
//                        break;
//
//                    case R.id.iv_icon:
//                        AskTopBean askBean = item.getAskTopBean();
//
//                        if (askBean == null)
//                            return;
//
//                        ImageView img = view.findViewById(R.id.iv_icon);
//
//                        String tag = (String) img.getTag();
//
//                        if (tag.equals("0")) {
//
//                            goUpdakaInfo(askBean);
//
//                        } else if (tag.equals("1")) {
//                            goCancelDaka(item);
//                        }
//
//
//                        break;
//
//
//                    case R.id.rl_sx:
//
//                        RoomPersonListBean person = item.getSxBean();
//
//                        //设置消息已读
//                        NetApiUtil.readMsg(mContext, person.getRoom_id(), "chat");
//
//                        Intent intent = new Intent(mContext, PersonChatActivity.class);
//                        intent.putExtra("roomName", person.getRoom_name());
//                        APPConstans.PersonChat = true;
//                        APPConstans.User_Id = person.getUser().getUser_id();
//                        APPConstans.Room_id = person.getRoom_id();
//                        startActivity(intent);
//
//                        break;
//
//                    case R.id.rl_msg:
//                        //TODO 处理消息类型
//                        MsgNewHomeBean msg = item.getMsgBean();
//
//                        if (msg != null && !MyTextUtil.isEmpty(msg.getType())) {
//
//                            checkHandleMsg(msg);
//                        }
//
//                        break;
//
//                    case R.id.btn_delete:
//
//                        int type = item.getAskType();
//                        if (type == 0) {
//                            AskTopBean ban = item.getAskTopBean();
//                            if (ban == null)
//                                return;
//
//                            int count = ban.getChildCount();
//                            int index = position + count;
//                            //TODO 删除接口
//                            deletTodo(item, position, index);
//                        } else if (type == 1) {
//
//                            updateDaka(position, item);
//                        }
//
//
//                        break;
//
//                    case R.id.iv_target_icon://关注的图标：跳个人空间
//
//                        FollowListBean followListBean = item.getFocusBean();
//                        if (followListBean == null || followListBean.getUserBean() == null)
//                            return;
//
//                        BaseUser user = followListBean.getUserBean();
//
//                        Intent mIntent = new Intent(mContext, MySpaceActivity.class);
//                        mIntent.putExtra(IntentParams.USER_ID, user.getUser_id());
//                        mIntent.putExtra(IntentParams.USER_head, user.getHead());
//                        mIntent.putExtra(IntentParams.UserName, user.getNickname());
//                        mIntent.putExtra(IntentParams.STATE, "0");
//                        startActivity(mIntent);
//
//                        break;
//
//                    case R.id.id_text:
//                        startActivity(new Intent(mContext, MsgNewTabActivity.class));
//                        break;
//                }
//            }
//        });
//
//
//    }
//
//    private void checkHandleMsg(MsgNewHomeBean msg) {
//        readTheMsg(msg);
//        if (msg.getType().equals("5") || msg.getType().equals("3") || msg.getType().equals("4") || msg.getType().equals("8")) {
//            Intent intent = new Intent(App.mContext, MySpaceActivity.class);
//            intent.putExtra(IntentParams.USER_ID, msg.getFrom_id());
//            App.mContext.startActivity(intent);
//
//
//        } else if (msg.getType().equals("1") || msg.getType().equals("2") || msg.getType().equals("6") || msg.getType().equals("7")) {
//            goTargetInfo(msg);
//
//
//        } else if (msg.getType().equals("12") || msg.getType().equals("11")) {
//
//            Intent intent = new Intent(mContext, TargetCheckActivity.class);
//            intent.putExtra("headUrl", msg.getFrom_head());
//            intent.putExtra("fromUser", msg.getFrom_id());
//            intent.putExtra("targetName", msg.getAim_name());
//            intent.putExtra("aim_id", msg.getAim_id());
//
//
//            intent.putExtra("rate_type", msg.getRate_type());
//            intent.putExtra("aim_money", msg.getMoney());
//            intent.putExtra("nick_name", msg.getFrom_nickname());
//            intent.putExtra("name_content", msg.getName());
//            mContext.startActivity(intent);
//
//        } else if (msg.getType().equals("13") || msg.getType().equals("14")) {
//            Intent intent = new Intent(mContext, VipIntroduceActivity.class);
//            intent.putExtra(IntentParams.IsVIP, "N");
//            startActivity(intent);
//
//        }
//        requestAllData(true);
//    }
//
//    /**
//     * 跳到目标详情
//     *
//     * @param msg
//     */
//    private void goTargetInfo(MsgNewHomeBean msg) {
//        Intent intent = new Intent(App.mContext, TargetNewInfoActivity.class);
//        intent.putExtra(IntentParams.DAKA_Target_Id, msg.getAim_id());
//        startActivity(intent);
//
//    }
//
//
//    private void readTheMsg(final MsgNewHomeBean data) {
////        RetrofitServiceManager manager = RetrofitServiceManager.getInstance(true);
//        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
//        AppUrl url = manager.create(AppUrl.class);
//        HashMap<String, String> map = new HashMap<>();
//        map.put("id", data.getId());
//        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
//
//        Observable<MsgRedBean> obsever;
//
//        if (true) {
//            obsever = url.readMsg(map);
//        } else {
//            obsever = url.readMsg1(map);
//        }
//
//        obsever.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<MsgRedBean>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(MsgRedBean msg) {
//                        if (msg.getOp().getCode().equals("Y")) {
//
//                        } else {
//                            ToastUtils.getInstanc(mContext).showToast("消息已读服务异常");
//                        }
//                    }
//                });
//    }
//
//
//    private void goCancelDaka(final FollowTestBean item) {
//        AlertDialog.Builder buid = new AlertDialog.Builder(mContext);
//        buid.setMessage("确定要取消打卡吗？");
//        buid.setPositiveButton(
//                "确定", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialoginterface, int i) {
//                        updateDaka(item);
//                    }
//                }
//        );
//        buid.setNegativeButton(
//                "取消", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialoginterface, int i) {
//
//                    }
//                }
//        );
//
//        AlertDialog dialog = buid.create();
//        dialog.show();
//
//        dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
//                .getColor(R.color.bar_grey_90));
//
//
//    }
//
//    /**
//     * 删除我的打卡待办
//     *
//     * @param position
//     * @param item
//     */
//
//    private void updateDaka(final int position, FollowTestBean item) {
//        dialog.show();
//        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
//        NetApi url = manager.create(NetApi.class);
//        HashMap<String, String> map = new HashMap<>();
//        map.put("command", "ok-api.DeleteExecuteCheck");
//        map.put("plan_id", item.getAnswerBean().getF_answer_plan_id());
//
//        map.put("user_id", item.getAnswerBean().getUser().getUser_id());
//        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
//
//
//        url.deletTodo(map).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<InfoStr>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        dialog.dismiss();
//                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(InfoStr msg) {
//                        dialog.dismiss();
//                        String code = msg.getOp().getCode();
//                        if (code.equals("Y")) {
////                            adapter.getData().remove(position);
////                            adapter.notifyItemRemoved(position);
////                            adapter.notifyItemChanged(position);
////                            //TODO 通知顶上发起的item更新
//                            notifyParentupdate();
//                        } else {
//                            ToastUtils.getInstanc(mContext).showToast("服务异常");
//                        }
//                    }
//                });
//
//    }
//
//
//    /**
//     * 删除我的打卡待办
//     *
//     * @param item
//     */
//
//    private void updateDaka(FollowTestBean item) {
//        dialog.show();
//        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
//        NetApi url = manager.create(NetApi.class);
//        HashMap<String, String> map = new HashMap<>();
//        map.put("command", "ok-api.DeleteExecuteCheck");
//
//        map.put("plan_id", getMyDakaId(item));
//
//        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
//        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
//
//
//        url.deletTodo(map).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<InfoStr>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        dialog.dismiss();
//                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(InfoStr msg) {
//                        dialog.dismiss();
//                        String code = msg.getOp().getCode();
//                        if (code.equals("Y")) {
////                            adapter.getData().remove(position);
////                            adapter.notifyItemRemoved(position);
////                            adapter.notifyItemChanged(position);
////                            //TODO 通知顶上发起的item更新
//                            notifyParentupdate();
//                        } else {
//                            ToastUtils.getInstanc(mContext).showToast("服务异常");
//                        }
//                    }
//                });
//
//    }
//
//
//    //通知主条目更新
//    private void notifyParentupdate() {
//        reload();
//    }
//
//
//    /**
//     * 编辑待办
//     *
//     * @param daiban
//     */
//    private void goEditDaiban(AskTopBean daiban) {
//
//        Intent mitent = new Intent(mContext, TodoAddActivity.class);
//        mitent.putExtra(IntentParams.Intent_Enter_Type, "edit");
//        AppContacts.EditTODO = daiban;
//        startActivity(mitent);
//    }
//
//
//    /**
//     * 待办打卡
//     *
//     * @param topBean
//     */
//    private void goUpdakaInfo(AskTopBean topBean) {
//
//        Intent intent = new Intent(mContext, DakaUploadActivity.class);
//        String targName = MyTextUtil.isEmpty(topBean.getF_target_name()) ? "" : topBean.getF_target_name();
//        intent.putExtra(IntentParams.SELECT_Target_Name, targName);
//        if (!MyTextUtil.isEmpty(topBean.getF_target_start_date())) {
//            intent.putExtra(IntentParams.SELECT_Target_TIME, DateUtil.timeOnlyDot(topBean.getF_target_start_date()) + "-"
//                    + DateUtil.timeOnlyDot(topBean.getF_target_end_date()));
//        }
//
//        intent.putExtra(IntentParams.DAKA_Target_Id, topBean.getF_plan_id());
//        intent.putExtra(IntentParams.DAKA_State, "1");
//        mContext.startActivity(intent);
//
//    }
//
//
//    private void deletTodo(FollowTestBean ban, final int position, final int index) {
//        dialog.show();
//        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
//        NetApi url = manager.create(NetApi.class);
//        HashMap<String, String> map = new HashMap<>();
//        map.put("command", "ok-api.DeleteExecuteByUserId");
//        map.put("plan_id", ban.getAskTopBean().getF_plan_id());
////        map.put("exe_id", ban.getAskTopBean().get);
//        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
//
//
//        url.deletTodo(map).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<InfoStr>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        dialog.dismiss();
//                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(InfoStr msg) {
//                        dialog.dismiss();
//                        String code = msg.getOp().getCode();
//                        if (code.equals("Y")) {
//
//                            //通知目标列表更新
//
//                            for (int i = index; i >= position; i--) {
//                                adapter.getData().remove(i);
//                                adapter.notifyItemRemoved(i);
//                            }
////                            adapter.notifyItemRangeRemoved(position, index);
//                            adapter.notifyDataSetChanged();
//
//                            checkIfNoTodu();
//
//                        } else {
//                            ToastUtils.getInstanc(mContext).showToast("服务异常");
//                        }
//                    }
//                });
//
//    }
//
//
//    private String getMyDakaId(FollowTestBean item) {
//
//        if (item == null || item.getAskTopBean() == null || MyTextUtil.isEmpty(item.getAskTopBean().getF_plan_id()) || mySelfDaka.size() < 1) {
//            return "";
//        }
//
//        String planId = item.getAskTopBean().getF_plan_id();
//
//        Set<String> mapSet = mySelfDaka.keySet();    //获取所有的key值 为set的集合
//        Iterator<String> itor = mapSet.iterator();//获取key的Iterator便利
//
//        while (itor.hasNext()) {
//            String key = itor.next();
//            if (key.equals(planId)) {
//                return mySelfDaka.get(key);
//            }
//        }
//        return "";
//    }
//
//
//    private void checkIfNoTodu() {
//        toduCount--;
//        if (toduCount < 1) {
//            List<FollowTestBean> datas = adapter.getData();
//            int len = datas.size();
//            for (int i = 0; i < len; i++) {
//                FollowTestBean dtt = datas.get(i);
//
//                if (dtt.getItemType() == SELECTION_Title) {
//                    if (dtt.getTitleName().equals("待办")) {
//                        adapter.getData().remove(dtt);
//                        adapter.notifyItemRemoved(i);
//                        adapter.notifyItemChanged(i);
//                        break;
//                    }
//                }
//            }
//
//
//        }
//    }
//
//
//    private void putData(List<TodoDingBean> list) {
//        if (toduOnly != null) {
//            toduOnly.clear();
//        } else {
//            toduOnly = new ArrayList<>();
//        }
//
//        if (list == null || list.size() < 1) {
//            return;
//        }
//        toduCount = list.size();
//        FollowTestBean bean = new FollowTestBean();
//        bean.setViewType(SELECTION_Title);//标题
//        bean.setTitleName("待办");
//        bean.setViewType(5);
//        bean.setReqestDataType(1);
//        toduOnly.add(bean);
//
//        //解体
//
//        for (TodoDingBean data : list) {
//            AskTopBean bean1 = new AskTopBean();
//            bean1.setF_is_need(data.getF_is_need());
//            bean1.setF_plan_date(data.getF_plan_date());
//
//            if (!MyTextUtil.isEmpty(data.getF_target_uuid()))
//                bean1.setF_target_uuid(data.getF_target_uuid());
//
//            bean1.setF_reminder_time(data.getF_reminder_time());
//            bean1.setF_watch_img(data.getF_watch_img());
//            bean1.setF_title(data.getF_title());
//            bean1.setF_plan_id(data.getF_plan_id());
//            bean1.setF_target_name(data.getF_target_name());
//            bean1.setF_state(data.getF_state());
//            bean1.setExecutorlist(data.getExecutorlist());
//            bean1.setF_pid(data.getF_pid());
//            bean1.setF_executor(data.getF_executor());
//            bean1.setFrom_user(data.getFrom_user());
//            bean1.setF_target_end_date(data.getF_target_end_date());
//            bean1.setF_target_start_date(data.getF_target_start_date());
//
//            List<AnswerBean> answers = data.getAnswer();
//            if (answers != null && answers.size() > 0) {
//                bean1.setChildCount(answers.size());
//            } else {
//                bean1.setChildCount(0);
//            }
//
//            FollowTestBean tt = new FollowTestBean();
//            tt.setAskType(0);
//            tt.setViewType(HomeAdapter.ITEM_TYPR);
//            tt.setAskTopBean(bean1);
//
//            toduOnly.add(tt);
//
//            if (answers != null && answers.size() > 0) {
//                for (AnswerBean anss : answers) {
//                    anss.setF_target_name(data.getF_target_name());
//                    FollowTestBean dd = new FollowTestBean();
//                    dd.setAskType(1);
//                    dd.setViewType(HomeAdapter.ITEM_TYPR);
//                    dd.setAnswerBean(anss);
//
//                    if (anss.getUser().getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
//                        mySelfDaka.put(data.getF_plan_id(), anss.getF_answer_plan_id());
//                    }
//
//                    toduOnly.add(dd);
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 在发起的时候clear
//     */
//    private void requestAllData(boolean isShow) {
//
//
//        isLunxun = !isShow;
//        if (ScrollState != 0)
//            return;
//
//        if (adapter != null) {
//            adapter.getData().clear();
//            adapter.notifyDataSetChanged();
//            mList.removeAllViews();
//            clearAlldatas();
//        }
//
//
//        if (isShow)
//            dialog.show();
//
//        tempDatas.clear();
//
//        dateListner.queryWxList("1", "40");
//    }
//
//    private void clearAlldatas() {
//        mySelfDaka.clear();
//        tempDatas.clear();
//        SXOnlyOld.clear();
//        MsgOnly.clear();
//        toduOnly.clear();
//        focusOnly.clear();
//    }
//
//
//    private void reload() {
//
//        mySelfDaka.clear();
//
//        if (adapter != null) {
//            adapter.getData().clear();
//            adapter.notifyDataSetChanged();
//        }
//
//
//        dialog.show();
//        tempDatas.clear();
//        dateListner.queryWxList("1", "40");
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ActyFinishEvent.HomeFromMsgTab = 0;
//        NineGridView.setImageLoader(new NineGridView.ImageLoader() {
//            @Override
//            public void onDisplayImage(Context context, ImageView imageView, String url) {
//                Glide.with(context).load(url)
//                        .into(imageView);
//            }
//
//            @Override
//            public Bitmap getCacheImage(String url) {
//                return null;
//            }
//        });
//
//        EventBus.getDefault().register(this);
//    }
//
//
//    private String getWeekStr(int datum) {
//
//        String str = "";
//        switch (datum) {
//            case 1:
//
//                return "周日";
//
//            case 2:
//                return "周一";
//
//            case 3:
//                return "周二";
//
//            case 4:
//                return "周三";
//
//            case 5:
//                return "周四";
//
//            case 6:
//                return "周五";
//
//            case 7:
//                return "周六";
//
//
//        }
//        return str;
//    }
//
//    /**
//     * 检查是否是今天
//     *
//     * @param bean
//     * @param view
//     */
//    private void checkIfToday(CalendarBean bean, TextView view, TextView chinaDay) {
//
//        String month = bean.moth > 9 ? bean.moth + "" : "0" + bean.moth;
//        String dayStr = bean.day > 9 ? bean.day + "" : "0" + bean.day;
//        String dateStr = bean.year + "-" + month + "-" + dayStr;
//
//        String today = DateUtil.getTodayStr();
//        if (dateStr.equals(today)) {
//            view.setText("今");
//            view.setTextColor(getResources().getColor(R.color.white));
//            chinaDay.setTextColor(getResources().getColor(R.color.white));
//        }
//
//    }
//
//
//    private void initRecycleView() {
//
//
//        mList.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
//        mList.setItemAnimator(null);
//
//        mList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//
//                ScrollState = newState;
//
//                switch (ScrollState) {
//                    case SCROLL_STATE_IDLE:
//
//
//                        break;
//
//                    case SCROLL_STATE_DRAGGING:
//
//                        break;
//
//                    case SCROLL_STATE_SETTLING:
//
//                        break;
//                }
//
//            }
//        });
//
//
//    }
//
//
//    private String getDisPlayNumber(int num) {
//        return num < 10 ? "0" + num : "" + num;
//    }
//
//
//    public int px(float dipValue) {
//        Resources r = Resources.getSystem();
//        final float scale = r.getDisplayMetrics().density;
//        return (int) (dipValue * scale + 0.5f);
//    }
//
//
//    PopupWindow popupWindow;
//
//    private void initPop() {
//        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.pop_dingding, null);
//
//        setListner(view);
//
//        popupWindow = new PopupWindow(view, DisplayUtils.dp2px(mContext, 135),
//                DisplayUtils.dp2px(mContext, 250));
//
//        popupWindow.setTouchable(true);
//        popupWindow.setOutsideTouchable(true);
//
//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                if (!popupWindow.isShowing()) {
//
//                    popupWindow.showAsDropDown(share, (DisplayUtils.dp2px(mContext, 30)),
//                            DisplayUtils.dp2px(mContext, 30));
//
//                } else {
//                    popupWindow.dismiss();
//                }
//
//            }
//        });
//    }
//
//
//    private void setListner(LinearLayout view) {
//
//        int childCount = view.getChildCount();
//
//        for (int i = 0; i < childCount; i++) {
//            view.getChildAt(i).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int viewId = v.getId();
//                    switch (viewId) {
//
//                        case R.id.ll_creat_todo:
//                            startActivity(new Intent(mContext, TodoAddActivity.class));
//                            popupWindow.dismiss();
//                            break;
//
//                        case R.id.ll_suishou:
//                            popupWindow.dismiss();
//
//                            startActivity(new Intent(mContext, SuiShouNoteActivity.class));
//
//                            break;
//
//                        case R.id.ll_vote:
//                            ToastUtils.getInstanc().showToast("投票");
//                            popupWindow.dismiss();
//                            break;
//
//                        case R.id.ll_join:
//                            ToastUtils.getInstanc().showToast("报名");
//                            popupWindow.dismiss();
//                            break;
//
//                        case R.id.ll_pk:
//                            ToastUtils.getInstanc().showToast("创建PK");
//                            popupWindow.dismiss();
//                            break;
//
//                        case R.id.ll_swipt:
//
//                            popupWindow.dismiss();
//
//                            RxPermissions rxPermissions = new RxPermissions(mContext);
//                            rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
//                                @Override
//                                public void accept(Boolean aBoolean) throws Exception {
//                                    if (aBoolean) {
//                                        startActivity(new Intent(mContext, CaptureActivity.class));
//                                    } else {
//                                        ToastUtils.getInstanc().showToast("你禁止了相机权限!");
//                                    }
//                                }
//                            });
//
//
//                            break;
//
//
//                    }
//
//                }
//            });
//        }
//
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onXshellEvent(XshellEvent event) {
//        if (event.what == EventConts.HomeFresh) {
//
//            Log.e("amtf", "收到刷新");
//
//            if (event.type == 1) {
//                //请求关注列表
//                requestFocusList2UpUI(event.msg);
//
//            } else if (event.type == 0) {
//                requestToduList2UpUI(event.msg);
//            } else {
//                requestAllData(true);
//            }
//        }
//
//    }
//
//    /**
//     * 待办更新（点赞评论）
//     *
//     * @param planId
//     */
//    private void requestToduList2UpUI(final String planId) {
//
//        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
//        AppUrl url = manager.create(AppUrl.class);
//        HashMap<String, String> map = new HashMap<>();
//        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
//        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
//        map.put("plan_date", selectDataStr);
//        map.put("command", "ok-api.SelectTargetExecuteList");
//
//        url.getTodoList(map).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<DingReturnBean>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("amtf", e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(DingReturnBean msg) {
//
//                        if (msg.getOp() == null) {
//
//                            return;
//                        }
//                        if (msg.getOp().getCode().equals("Y")) {
//                            List<TodoDingBean> list = msg.getData();
//
//                            if (list != null && list.size() > 0) {
//
//                                notifyTodu(list, planId);
//
//                            }
//                        }
//                    }
//                });
//
//
//    }
//
//    private void notifyTodu(List<TodoDingBean> list, String planId) {
//
//        int len = list.size();
//        for (int i = 0; i < len; i++) {
//
//            TodoDingBean item = list.get(i);
//
//            if (item.getAnswer() != null && item.getAnswer().size() > 0) {
//
//                for (AnswerBean data : item.getAnswer()) {
//                    if (data.getF_answer_plan_id().equals(planId)) {
//
//                        chedk2notifyDatas(data, planId);
//                        break;
//                    }
//                }
//            }
//
//
//        }
//
//
//    }
//
//    private void chedk2notifyDatas(AnswerBean data, String planId) {
//        List<FollowTestBean> list = adapter.getData();
//
//        if (list.size() > 0) {
//            int len = list.size();
//
//            for (int i = 0; i < len; i++) {
//                FollowTestBean bean = list.get(i);
//                if (bean.getAnswerBean() != null && bean.getAnswerBean().getF_answer_plan_id().equals(planId)) {
//                    adapter.getData().get(i).setAnswerBean(data);
//                    adapter.notifyItemChanged(i);
//                    adapter.notifyDataSetChanged();
//                    break;
//                }
//
//
//            }
//
//
//        }
//
//
//    }
//
//    /**
//     * 关注更新(点赞，评论)
//     *
//     * @param planId
//     */
//    private void requestFocusList2UpUI(final String planId) {
//
//        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
//        AppUrl url = manager.create(AppUrl.class);
//        HashMap<String, String> map = new HashMap<>();
//        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
//        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
//        map.put("plan_date", selectDataStr);
//        map.put("page_num", "1");
//        map.put("page_size", "30");
//
//        url.getFoucsPerson2Target(map).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<DingRetBean>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("amtf", e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(DingRetBean msg) {
//
//                        if (msg.getOp() == null) {
//
//                            return;
//                        }
//                        if (msg.getOp().getCode().equals("Y")) {
//
//                            List<FollowListBean> list = msg.getData();
//
//                            if (list != null && list.size() > 0) {
//
//                                for (FollowListBean item : list) {
//
//                                    if (item.getF_plan_id().equals(planId)) {
//                                        check2notify(item, planId);
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                });
//
//    }
//
//    /**
//     * 更新当前
//     *
//     * @param item
//     */
//    private void check2notify(FollowListBean item, String planId) {
//
//        List<FollowTestBean> datas = adapter.getData();
//
//        int len = datas.size();
//
//        for (int i = 0; i < len; i++) {
//            FollowTestBean bean = datas.get(i);
//
//            if (bean.getFocusBean() != null &&
//                    !MyTextUtil.isEmpty(bean.getFocusBean().getF_plan_id()) && bean.getFocusBean().getF_plan_id().equals(planId)) {
//
//                adapter.getData().get(i).setFocusBean(item);
//                adapter.notifyItemChanged(i);
//                adapter.notifyDataSetChanged();
//            }
//
//
//        }
//
//
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        if (R.id.ll_slide == id) {
//            if (clipLayout.getType() == 0) {
//                clipLayout.flod();
//                clipLayout.setType(1);
//                img.setImageResource(R.drawable.arrow_b);
//
//            } else if (clipLayout.getType() == 1) {
//                clipLayout.open();
//                clipLayout.setType(0);
//                img.setImageResource(R.drawable.arrow_u);
//            }
//        } else if (R.id.swtich_qy == id) {
//
//            if (AppContacts.BackFromH5 == 0 && MyTextUtil.isEmpty(PreferenceUtil.getInstance(mContext).getCompanyId())) {
//
//                //TODO 给Tina手机弄的,后面干掉
//
//                Intent mintent = mContext.getIntent();
//                mintent.setClass(mContext, LoadingActivity.class);
//                startActivity(mintent);
//
////                if (companyLen < 1) {
////                    ToastUtils.getInstanc().showToast("未查询到你的公司信息!");
////                    return;
////                } else if (companyLen > 1) {
////                    arr.performClick();
////                } else if (companyLen == 1) {
////                    Intent mintent = mContext.getIntent();
////                    mintent.setClass(mContext, LoadingActivity.class);
////                    startActivity(mintent);
////                }
//
//            } else {
//                Intent mintent = mContext.getIntent();
//                mintent.setClass(mContext, LoadingActivity.class);
//                startActivity(mintent);
//
//            }
//
//
//        }
//    }
//
//
//    ImageView logo, arr;
//    TextView comName;
//
//    PopupWindow companePop;
//
//
//
//    private void initCompanyData(List<CompanyBean> data) {
//        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.pop_company, null);
//
//        initComListView(view, data);
//
//        companePop = new PopupWindow(view, DisplayUtils.dp2px(mContext, LinearLayout.LayoutParams.MATCH_PARENT),
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        companePop.setTouchable(true);
//        companePop.setOutsideTouchable(true);
//    }
//
//
//    MyExpandableListAdapter companyAdapter;
//
//    private void initComListView(LinearLayout popView, final List<CompanyBean> data) {
//
//        RecyclerView recy = popView.findViewById(R.id.company_list);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recy.setLayoutManager(layoutManager);//给RecyclerView设置适配器
//
//        companyAdapter = new MyExpandableListAdapter(R.layout.item_pop_company, data);
//        companyAdapter.setItemTag(1);
//
//        recy.setAdapter(companyAdapter);
//
//        companyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                companePop.dismiss();
//                arr.setTag(false);
//                arr.setImageResource(R.mipmap.switch_down);
//
//                CompanyBean datt = data.get(position);
//                PreferenceUtil.getInstance(mContext).setCompanyId(datt.getCompany_id());
//
//                Intent intent = mContext.getIntent().putExtra("company", datt);
//                intent.setClass(mContext, LoadingActivity.class);
//                startActivity(intent);
//
//            }
//        });
//
//
//    }
//
//
//    private void getCompanyInfoByMobile(String mobile) {
//        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
//        NetApi url = manager.create(NetApi.class);
//        HashMap<String, String> requsMap = new HashMap<>();
//        requsMap.put("command", "api2e.GetCompanyList");
//        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
//        requsMap.put("mobile", mobile);
//
//        url.getCompanyInfoByMobile(requsMap).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<MobieCompanyBean>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("amtf", e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(MobieCompanyBean msg) {
//
//                        if (msg.getOp().getCode().equals("Y")) {
//
//                            if (msg.getData() != null && msg.getData().size() > 0) {
//
//                                showCompanyList(msg.getData());
//
//                            } else {
//                                arr.setVisibility(View.GONE);
//                            }
//
//                        } else {
//                            arr.setVisibility(View.GONE);
//                            Log.e("amtf", "获取公司列表服务异常");
//                        }
//
//
//                    }
//                });
//
//
//    }
//
//    int companyLen = 0;//公司的个数
//
//
//    private void showCompanyList(List<CompanyBean> data) {
//
//
//        companyLen = data.size();
//
//        if (data.size() == 1) {
//            arr.setVisibility(View.GONE);
//            showCompanyHeadInfo(data.get(0));
//            mContext.getIntent().putExtra("company", data.get(0));
//
//
//        } else if (data.size() > 1) {
//            arr.setVisibility(View.VISIBLE);
//
//            filtSelectCompany(data);
//
//            initCompanyData(data);
//        }
//
//    }
//
//    private void filtSelectCompany(List<CompanyBean> data) {
//
//        if (!MyTextUtil.isEmpty(PreferenceUtil.getInstance(mContext).getCompanyId())) {
//
//            for (CompanyBean item : data) {
//                if (item.getCompany_id().equals(PreferenceUtil.getInstance(mContext).getCompanyId())) {
//                    showCompanyHeadInfo(item);
//                    break;
//                }
//
//            }
//
//
//        }
//
//
//    }
//
//    private void showCompanyHeadInfo(final CompanyBean companyBean) {
//
//        comName.setText(companyBean.getName());
//
//        Picasso.with(mContext).load(companyBean.getLogo())/*.transform(new CircleTransform())*/.error(R.drawable.icon_no_download).into(logo);
//
//
//    }
//
//
//}
//
//
//
