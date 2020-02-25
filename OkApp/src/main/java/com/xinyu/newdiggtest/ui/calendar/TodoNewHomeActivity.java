package com.xinyu.newdiggtest.ui.calendar;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.codbking.calendar.CaledarAdapter;
import com.codbking.calendar.CalendarBean;
import com.codbking.calendar.CalendarDateView;
import com.codbking.calendar.CalendarUtil;
import com.codbking.calendar.CalendarView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.HomeAppAdapter;
import com.xinyu.newdiggtest.adapter.viewhelper.interfa.MyLongClickner;
import com.xinyu.newdiggtest.bean.AskRoomIdBean;
import com.xinyu.newdiggtest.bean.MsgDotBean;
import com.xinyu.newdiggtest.bean.MsgTodoBean;
import com.xinyu.newdiggtest.bean.RetListBean;
import com.xinyu.newdiggtest.bean.TodoRetBean;
import com.xinyu.newdiggtest.bean.TodoUserBean;
import com.xinyu.newdiggtest.bigq.AddDoneActivity;
import com.xinyu.newdiggtest.bigq.ProjectManagerActivity;
import com.xinyu.newdiggtest.bigq.ToDoActivity;
import com.xinyu.newdiggtest.bigq.TodoInfoListActivity;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.BaseBean;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.net.bean.InfoStr;
import com.xinyu.newdiggtest.ui.BaseActivity;
import com.xinyu.newdiggtest.ui.ProcessActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.ui.chat.ChatCompanyActivity;
import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.DisplayUtils;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.TimeUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.widget.MarkedImageView;
import com.xinyu.newdiggtest.widget.WrapContentLinearLayoutManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TodoNewHomeActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.list)
    RecyclerView mList;

    @BindView(R.id.calendarDateView)
    CalendarDateView mCalendarDateView;


    ExecutorService excutor = Executors.newCachedThreadPool();

    @BindView(R.id.tv_title)
    TextView mTitle;


    ImageView emptyView;
    ImageView share;
    //    CalendarLayout clipLayout;
    String selectDataStr = "", currentMonth = "2020-02";


    View week_layout;


    HomeAppAdapter appAdapter;


    Map<String, List<MsgDotBean>> date4Month = new HashMap<>();

    List<MarkedImageView> dateView = new ArrayList<>();


    Map<View, String> dates = new HashMap<>();


    /**
     * pop 相关
     */

    PopupWindow addPop;
    LinearLayout view;
    RetListBean.InvitesBean signUser = null;

    private void checkJson(JSONObject child1) {

        try {

            String op = child1.getString("op");

            String type = child1.getString("type");

            if (type.equals("todo") && op.equals("create")) {

                getTodoListByDay(selectDataStr);

            } else if (op.equals("delete")) {

                getTodoListByDay(selectDataStr);
            }


            if (type.equals("todo") && op.equals("update")) {

                JSONObject object = child1.getJSONObject("data");
                RetListBean rest = JSON.parseObject(object.toString(), RetListBean.class);

                String endDate = DateUtil.getOnlyDate(rest.getF_end_date());

                if (!rest.getF_create_date().equals(selectDataStr) && !endDate.equals(selectDataStr)) {
                    return;
                }

                int len = appAdapter.getData().size();

                if (len < 0)
                    return;

                for (int i = 0; i < len; i++) {

                    if (appAdapter.getData().get(i).getF_id().equals(rest.getF_id())) {

                        appAdapter.getData().set(i, rest);
                        appAdapter.notifyItemChanged(i);

                        break;
                    }
                }

            } else if (type.equals("todonote") && (op.equals("update") || op.equals("create"))) {
                getTodoListByDay(selectDataStr);
            }

        } catch (JSONException e) {
            Log.e("amtf", "json异常：" + e.getMessage());

            e.printStackTrace();
        }


    }


    private void initCanlendar() {

        mCalendarDateView.setAdapter(new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
                TextView view;
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar, null);
//                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(px(40), px(40));

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(px(45), px(45));

                    convertView.setLayoutParams(params);
                }

                view = (TextView) convertView.findViewById(R.id.text);


                if (bean.mothFlag != 0) {
                    view.setTextColor(0xff9299a1);
                } else {
                    view.setTextColor(Color.parseColor("#cc000000"));
                }

                view.setTag(bean.mothFlag);
                view.setText("" + bean.day);
                checkIfToday(bean, view);

                return convertView;
            }
        });

        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {

                mTitle.setText(getDisPlayNumber(bean.moth) + "月" + getDisPlayNumber(bean.day) + "日" + "  " + getWeekStr(bean.week));

                selectDataStr = getDisPlayNumber(bean.year) + "-" + getDisPlayNumber(bean.moth) + "-" + getDisPlayNumber(bean.day);
                currentMonth = selectDataStr.substring(0, 7);

                /**
                 * 我的数据
                 */
                reload();

            }

            @Override
            public void onItemColorSelect(View view, CalendarBean bean) {

                Log.e("amtf", "onItemColorSelect--");


            }

            @Override
            public void onItemColorReset(View view, CalendarBean bean) {

                Log.e("amtf", "onItemColorReset--");

            }

            @Override
            public void onItemCheckDot(View view, CalendarBean bean) {

                MarkedImageView count = view.findViewById(R.id.msg_count);
                count.setMessageNumber(0);//重置


                String day = bean.getCalendarDay().substring(0, 7);

                String current = selectDataStr.substring(0, 7);

                if (day.equals(current)) {
                    dates.put(view, bean.getCalendarDay());
                }


                count.setTag(bean.getCalendarDay());

                dateView.add(count);//TODO 更新View


                if (date4Month.size() > 0) {

                    for (Map.Entry<String, List<MsgDotBean>> entry : date4Month.entrySet()) {

                        if (day.equals(entry.getKey())) {

                            List<MsgDotBean> datas = entry.getValue();

                            checkMsgDot(count, bean, datas);

                            break;
                        }
                    }
                }
            }

            @Override
            public void onPageChange() {
                dates.clear();
            }
        });
    }


    private void checkMsgDot(MarkedImageView count, CalendarBean bean, List<MsgDotBean> times) {
        if (times == null || times.size() < 1)
            return;

        String date = bean.getCalendarDay();
        for (MsgDotBean item : times) {
            if (date.equals(item.getDate())) {
                count.setMessageNumber(Integer.parseInt(item.getUnfinish()));
                break;
            }
        }

    }

    private void initTitleDate() {
        int[] data = CalendarUtil.getYMD(new Date());

        String dayStr = "";
        if (data[2] < 10) {
            dayStr = "0" + data[2];
        } else {
            dayStr = data[2] + "";
        }

        mTitle.setText(data[1] + "月" + dayStr + "日" + "  " + getWeekStr(data[3]));
    }


    /**
     * 重新加载数据
     */
    private void reload() {
        refresDayDatas();
        currentMonth = selectDataStr.substring(0, 7);
    }


    private void initBindView() {

        findViewById(R.id.creat_todo).setOnClickListener(this);
        emptyView = findViewById(R.id.empty);
        share = findViewById(R.id.add);
        week_layout = findViewById(R.id.week_layout);
        mTitle.setOnClickListener(this);
        mTitle.setTag(false);
        canlenderHiden();

    }

    private void canlenderHiden() {
        mCalendarDateView.setVisibility(View.GONE);
        week_layout.setVisibility(View.GONE);


    }


    private void initView() {

        if (appAdapter == null) {
            initBindView();
            initRecycleView();
            initTitleDate();
            initCanlendar();
            initAddPicturePop();
        }


        getCalendMsgDot(currentMonth);

    }


    @Override
    public void onResume() {
        super.onResume();

        selectDataStr = DateUtil.getTodayStr();
        currentMonth = selectDataStr.substring(0, 7);
        check5Month();
        refresDayDatas();


    }


    /**
     * 请求本月前4个月的红点
     */
    private void check5Month() {

        date4Month.clear();

        String day1 = TimeUtil.getLast4Month(1);
        String day2 = TimeUtil.getLast4Month(2);
        String day3 = TimeUtil.getLast4Month(3);
        String day4 = TimeUtil.getLast4Month(4);

        List<String> dates = new ArrayList<>();

        dates.add(day1);
        dates.add(day2);
        dates.add(day3);
        dates.add(day4);


        for (final String item : dates) {
            excutor.execute(new Runnable() {
                @Override
                public void run() {
                    getCalendMsgDot(item);
                }
            });
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_dingding1;
    }


    public void getTodoListByDay(String day) {
//        dialog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("day", day);
        map.put("withDelay", "false");
        map.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());

        map.put("asc", "false");

        url.getTodoListByDay(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TodoRetBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
//                        dialog.dismiss();
                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(TodoRetBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            List<RetListBean> yaoban = msg.getInvitedata();

                            List<RetListBean> mySelf = msg.getData();

                            List<RetListBean> total = new ArrayList<>();

                            if (yaoban != null && yaoban.size() > 0) {

                                for (RetListBean item : yaoban) {
                                    item.setIsYaoban(1);
                                }
                                total.addAll(yaoban);
                            }


                            if (mySelf != null && mySelf.size() > 0) {

                                for (RetListBean item : mySelf) {
                                    item.setIsYaoban(0);
                                }

                                total.addAll(mySelf);
                            }

                            for (RetListBean ev : total) {
                                if (ev.getF_type().equals("vote")) {
                                    ev.setItemType(1);
                                    ev.setVoteType(1);
                                } else if (ev.getF_type().equals("enroll")) {
                                    ev.setItemType(1);
                                    ev.setVoteType(2);
                                } else if (ev.getF_type().equals("conference")) {
                                    ev.setItemType(1);
                                    ev.setVoteType(3);
                                } else if (isProcess(ev)) {
                                    ev.setItemType(2);
                                } else {
                                    ev.setItemType(0);
                                }
                            }

                            if (total.size() > 0) {
                                showUIdata(total);
                                emptyView.setVisibility(View.GONE);
                            } else if (appAdapter.getData().size() < 1) {
                                appAdapter.getData().clear();
                                appAdapter.notifyDataSetChanged();
                                emptyView.setVisibility(View.VISIBLE);
                            }

                        } else {
                            ToastUtils.getInstanc().showToast("服务异常!");
                        }

                    }
                });
    }

    private boolean isProcess(RetListBean ev) {//流程待办

        if (!MyTextUtil.isEmpty(ev.getF_process_id())) {
            return true;
        }

        return false;
    }


    /**
     * 刷新数据
     *
     * @param data
     */
    private void showUIdata(List<RetListBean> data) {

        appAdapter.getData().addAll(data);
        appAdapter.notifyDataSetChanged();

    }


    private String getWeekStr(int datum) {

        String str = "";
        switch (datum) {
            case 1:

                return "周日";

            case 2:
                return "周一";

            case 3:
                return "周二";

            case 4:
                return "周三";

            case 5:
                return "周四";

            case 6:
                return "周五";

            case 7:
                return "周六";


        }
        return str;
    }

    /**
     * 检查是否是今天
     *
     * @param bean
     * @param view
     */
    private void checkIfToday(CalendarBean bean, TextView view) {

        String month = bean.moth > 9 ? bean.moth + "" : "0" + bean.moth;
        String dayStr = bean.day > 9 ? bean.day + "" : "0" + bean.day;
        String dateStr = bean.year + "-" + month + "-" + dayStr;

        String today = DateUtil.getTodayStr();
        if (dateStr.equals(today)) {

            view.setTextColor(getResources().getColor(R.color.white));
            view.setBackgroundResource(R.drawable.background_item);
        }

    }


    private void initRecycleView() {
        mList.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mList.setItemAnimator(null);

        List<RetListBean> data = new ArrayList<>();
        appAdapter = new HomeAppAdapter(data);
        appAdapter.setHasStableIds(true);

        mList.setAdapter(appAdapter);

        appAdapter.transListner(new MyLongClickner() {
            @Override
            public void onLongMyClick(RetListBean.InvitesBean bean, TextView textView) {

                signUser = bean;

                String user = bean.getOwnermap().getUser_id();

                if (user.equals(PreferenceUtil.getInstance(mContext).getUserId()))
                    return;

                if (!addPop.isShowing()) {
                    addPop.showAsDropDown(textView);

                } else {
                    addPop.dismiss();
                }

            }
        });


        appAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                int id = view.getId();

                switch (id) {


                    case R.id.rl_vote:

                        int type = appAdapter.getData().get(position).getVoteType();

                        if (type == 3) {
                            ToastUtils.getInstanc().showToast("请前往PC端体验完整版");
                        } else {
                            RetListBean mt = appAdapter.getData().get(position);
                            goProjectInfo(mt);
                            ToastUtils.getInstanc().showToast("请前往PC端体验完整版");
                        }
                        break;


                    case R.id.btn_confirm:

                        RetListBean jb = appAdapter.getData().get(position);
                        goComfrim(jb, 1);

                        break;

                    case R.id.cancel:
                        RetListBean bbb = appAdapter.getData().get(position);
                        goComfrim(bbb, 2);

                        break;

                    case R.id.add_done:

                        RetListBean tt = appAdapter.getData().get(position);
                        goCreateHaveDone(tt);

                        break;

                    case R.id.rl_item1:

                        RetListBean data = appAdapter.getData().get(position);
                        Intent intent = new Intent(mContext, ProcessActivity.class);
                        intent.putExtra("process_id", data.getF_process_id());
                        intent.putExtra("f_type", data.getF_type());
                        intent.putExtra("f_title", data.getF_title());
                        intent.putExtra("f_finish_id", checkFinishId(data));
                        if (AppUtils.isCountReback(data)) {
                            intent.putExtra("count_back", "1");
                        }

                        int eventTy = data.getProcessType();

                        if (eventTy == 1) {
                            intent.putExtra("event_type", "1");//请假
                            startActivity(intent);

                        } else if (eventTy == 2) {
                            intent.putExtra("event_type", "2");//设备申请
                            startActivity(intent);

                        } else if (eventTy == 3) {//付款申请表
                            intent.putExtra("event_type", "3");
                            startActivity(intent);

                        } else if (eventTy == 4) {//推广费

                            intent.putExtra("event_type", "4");
                            startActivity(intent);

                        } else if (eventTy == 5) {//月度结算
                            intent.putExtra("event_type", "5");
                            startActivity(intent);
                        } else if (eventTy == 6) {//项目管理
                            goProjectInfo(data);
                        } else {
                            goInfo(data);
                        }

                        break;

                    case R.id.icon:

                        final RetListBean rrtt = appAdapter.getData().get(position);

                        String state = getMyState(rrtt);

                        if (state.equals("0")) {

                            goChcek(rrtt, 1);

                        } else if (state.equals("1")) {

                            AlertDialog dialog = new AlertDialog.Builder(mContext)
                                    .setTitle("温馨提示")
                                    .setMessage("确定取消完成吗？")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            goChcek(rrtt, 0);
                                        }
                                    })
                                    .show();

                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.button_vip));
                            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

                        }

                        break;


                }
            }
        });


    }

    private String checkFinishId(RetListBean data) {
        String finishId = "";

        if (data.getFinishes() != null && data.getFinishes().size() > 0) {

            List<RetListBean.InvitesBean> flishList = data.getFinishes();

            for (RetListBean.InvitesBean item : flishList) {
                if (item.getOwnermap().getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                    return item.getF_id();
                }
            }
        }

        return finishId;
    }

    private String getMyState(RetListBean dt) {

        List<RetListBean.InvitesBean> data = dt.getFinishes();

        if (data != null && data.size() > 0) {

            for (RetListBean.InvitesBean tt : data) {

                if (tt.getOwnermap().getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {

                    return tt.getF_state();

                }

            }
        }

        return "0";
    }


    /**
     * 确认别人的邀请
     *
     * @param data
     */

    public void goComfrim(RetListBean data, int type) {

        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("f_id", checkMyFid(data));

        map.put("f_todo_id", data.getF_id());

        if (type == 1) {
            map.put("confirm", "true");
        } else {
            map.put("confirm", "false");
        }

        url.updateInviteState(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        loadingDailog.dismiss();

                        if (msg.getOp().getCode().equals("Y")) {
                            Log.e("amtf", "邀办更新成功");
                        } else {
                            Log.e("amtf", "邀办更新失败");
                        }
                    }
                });
    }

    private String checkMyFid(RetListBean data) {

        String fid = "";

        List<RetListBean.InvitesBean> mdata = data.getInvites();

        for (RetListBean.InvitesBean dd : mdata) {

            if (dd.getOwnermap().getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                return dd.getF_id();
            }
        }

        return fid;
    }


    /**
     * 获取交办id
     *
     * @param data
     * @return
     */
    private String getCheckFid(RetListBean data) {

        String fid = "";

        List<RetListBean.InvitesBean> mdata = data.getFinishes();

        for (RetListBean.InvitesBean dd : mdata) {

            if (dd.getOwnermap().getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                return dd.getF_id();
            }
        }

        return fid;
    }


    /**
     * 新增交办
     *
     * @param tt
     */
    private void goCreateHaveDone(RetListBean tt) {
        Intent mIntent = new Intent(mContext, AddDoneActivity.class);
        mIntent.putExtra("todoId", tt.getF_id());
        mIntent.putExtra("enter_type", "0");//新建
        startActivity(mIntent);
    }

    /**
     * 调到待办详情
     *
     * @param data
     */
    private void goInfo(RetListBean data) {

        AppContacts.ToDOInfo = data;

        Intent mIntent = new Intent(mContext, TodoInfoListActivity.class);
        if (AppUtils.ifInviteNoAccept(data) == -1) {
            mIntent.putExtra("haveInvit", "-1");
        } else {
            mIntent.putExtra("haveInvit", "0");
        }

        mIntent.putExtra("creatBy", data.getF_create_by());

        mIntent.putExtra("todoId", data.getF_id());
        mIntent.putExtra("userId", data.getCreate_name().getUser_id());
        startActivity(mIntent);
    }


    /**
     * 调到待办详情
     *
     * @param data
     */
    private void goProjectInfo(RetListBean data) {

        AppContacts.ToDOInfo = data;

        Intent mIntent = new Intent(mContext, ProjectManagerActivity.class);
        mIntent.putExtra("creatBy", data.getF_create_by());
        mIntent.putExtra("todoId", data.getF_id());
        mIntent.putExtra("userId", data.getCreate_name().getUser_id());
        startActivity(mIntent);
    }


    private String getDisPlayNumber(int num) {
        return num < 10 ? "0" + num : "" + num;
    }


    public int px(float dipValue) {
        Resources r = Resources.getSystem();
        final float scale = r.getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {

        if (event.what == EventConts.SoCket_Push) {
            Log.e("amtf", "TodoActivity推送数据");
            JSONObject data = event.object;
            checkJson(data);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (appAdapter != null) {
            appAdapter.releaseTimer();
        }

    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.tv_title) {

            boolean ischeck = (boolean) mTitle.getTag();

            mTitle.setTag(!ischeck);

            if (!ischeck) {
                mCalendarDateView.setVisibility(View.VISIBLE);
                week_layout.setVisibility(View.VISIBLE);

            } else {
                canlenderHiden();
            }
        } else if (id == R.id.creat_todo) {

            Intent intent = new Intent(mContext, ToDoActivity.class);

            intent.putExtra("date", selectDataStr);

            startActivity(intent);


        }


    }


    /**
     * 交办状态
     *
     * @param data
     */

    public void goChcek(RetListBean data, int type) {

        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("f_id", getCheckFid(data));

        map.put("f_todo_id", data.getF_id());

        if (type == 1) {
            map.put("check", "true");
        } else {
            map.put("check", "false");
        }

        url.updateFinishState(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InfoStr>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(InfoStr msg) {
                        loadingDailog.dismiss();

                        if (msg.getOp().getCode().equals("Y")) {
                        } else {
                            ToastUtils.getInstanc().showToast("交办更新失败,请稍后再试!");
                        }
                    }
                });
    }


    private void initAddPicturePop() {

        view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.pop_sign, null);
//点击事件
        setPopListner(view);
        addPop = new PopupWindow(view, DisplayUtils.dp2px(mContext, 85),
                DisplayUtils.dp2px(mContext, 100));

        addPop.setTouchable(true);
        addPop.setOutsideTouchable(true);


    }


    private void setPopListner(LinearLayout view) {

        view.findViewById(R.id.sixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrCreateRoodId();
                addPop.dismiss();

            }
        });

        view.findViewById(R.id.sign_work).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addPop.dismiss();

                TodoUserBean user = signUser.getOwnermap();
                Intent intent = new Intent(mContext, ToDoActivity.class);
                intent.putExtra("date", selectDataStr);
                intent.putExtra("signer", user);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobie = signUser.getOwnermap().getMobile();

                if (!MyTextUtil.isEmpty(mobie) && AppUtils.isCellphone(mobie)) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + mobie);
                    callIntent.setData(data);
                    startActivity(callIntent);

                } else {
                    ToastUtils.getInstanc().showToast("用户暂未收录手机号码！");
                }

                addPop.dismiss();
            }
        });


    }


    public void getOrCreateRoodId() {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        final AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        String name = MyTextUtil.isEmpty(signUser.getOwnermap().getName()) ? signUser.getOwnermap().getNickname() : signUser.getOwnermap().getName();

        map.put("room_name", name);
        map.put("room_head", signUser.getOwnermap().getHead());
        map.put("friend_id", signUser.getOwnermap().getUser_id());
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


    public void getCalendMsgDot(final String monthStr) {
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        final AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("month", monthStr);

        url.queryMsgNote(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            List<MsgDotBean> msgList = new ArrayList<>();

                            Object object = msg.getData();
                            String jsonStr = new Gson().toJson(object);

                            Map<String, Object> data = json2map(jsonStr);

                            if (data != null && data.size() > 0) {

                                for (Map.Entry<String, Object> entry : data.entrySet()) {
                                    Object myData = entry.getValue();
                                    String ttChild = new Gson().toJson(myData);
                                    try {
                                        JSONObject mapChild = new JSONObject(ttChild);

                                        MsgDotBean item = getItem(mapChild);

                                        msgList.add(item);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                date4Month.put(monthStr, msgList);
                            }

                        }
                    }
                });
    }


    /**
     * JSONObject 转化成对象
     *
     * @return
     */
    private MsgDotBean getItem(JSONObject mapChild) {

        MsgDotBean data = new MsgDotBean();
        try {
            data.setDate(mapChild.getString("date"));
            data.setUnfinish(mapChild.getString("unfinish"));
            data.setFinish(mapChild.getString("finish"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return data;
    }


    public static Map<String, Object> json2map(String str_json) {
        Map<String, Object> res = null;
        try {
            Gson gson = new Gson();
            res = gson.fromJson(str_json, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (JsonSyntaxException e) {

            Log.e("amtf", "格式异常：" + e.getMessage());
        }
        return res;
    }


//    public void getDayMsgList() {
//        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
//        final NetApi url = manager.create(NetApi.class);
//        HashMap<String, String> map = new HashMap<>();
//
//        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
//        map.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());
//        map.put("f_day", selectDataStr);
//        map.put("f_state", "0");
//
//        url.queryHomeMsgList(map).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<TodoMsgRetBean>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                        Log.e("amtf", "服务onError:" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(TodoMsgRetBean msg) {
//
//                        //TODO 如果要调试,可以用下面方式打印处理
////                        String json = new Gson().toJson(msg);
////                        Log.e("amtf", "数据:" + json);
//
//
//                        if (msg.getOp().getCode().equals("Y")) {
//
//                            if (msg.getData() != null && msg.getData().size() > 0) {
//                                insertMsg(msg.getData());
//                            }
//                        }
//                    }
//                });
//    }

    /**
     * 顶部插入数据
     *
     * @param data
     */
//    private void insertMsg(List<MsgTodoBean> data) {
//
//        List<RetListBean> dtList = new ArrayList<>();
//
//        List<MsgTodoBean> checList = filter(data);
//
//        for (MsgTodoBean bean : checList) {
//            RetListBean child = new RetListBean();
//            child.setItemType(3);
//            child.setMsg(bean);
//            dtList.add(child);
//        }
//        appAdapter.getData().addAll(0, dtList);
//        appAdapter.notifyDataSetChanged();
//
//    }

    /**
     * 同一个待办过滤掉
     *
     * @param data
     * @return
     */
    private List<MsgTodoBean> filter(List<MsgTodoBean> data) {

        List<MsgTodoBean> ret = new ArrayList<>();

        if (ret.size() < 1) {
            ret.add(data.get(0));
        }

        int len = data.size();
        for (int i = 1; i < len; i++) {

            MsgTodoBean item = data.get(i);

            boolean isHave = false;
            for (MsgTodoBean target : ret) {
                if (target.getF_object_id().equals(item.getF_object_id())) {
                    isHave = true;
                    break;
                }
            }

            if (!isHave) {
                ret.add(item);
            }

        }


        return ret;
    }


    /**
     * 更新所有数据
     */
    private void refresDayDatas() {
        appAdapter.getData().clear();
        getTodoListByDay(selectDataStr);

    }


    @OnClick(R.id.back)
    public void goCommit() {

        finish();


    }

}



