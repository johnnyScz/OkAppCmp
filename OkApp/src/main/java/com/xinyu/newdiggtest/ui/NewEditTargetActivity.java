package com.xinyu.newdiggtest.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.ChildTargetAdapter;

import com.xinyu.newdiggtest.bean.ChildTargetBean;

import com.xinyu.newdiggtest.bean.TargetDataBean;
import com.xinyu.newdiggtest.bean.TargetInfo;
import com.xinyu.newdiggtest.config.ActyFinishEvent;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.seriable.SeletExcutor;
import com.xinyu.newdiggtest.ui.Digg.ExcutorSelectActivity;

import com.xinyu.newdiggtest.ui.Digg.fragment.Public2PrivateActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.TargetSuccedActivity;
import com.xinyu.newdiggtest.ui.Digg.target.EditMonthFixedFragment;
import com.xinyu.newdiggtest.ui.Digg.target.CreateTargetActivity;

import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyMiddleDialog;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewEditTargetActivity extends BaseNoEventActivity {


    @BindView(R.id.target_pager)
    ViewPager target_pager;
    @BindView(R.id.target_tab)
    TabLayout target_tab;

    @BindView(R.id.start_linear)
    RelativeLayout start_linear;

    @BindView(R.id.iv_target_icon)
    ImageView iv_target_icon;//图标


    private String[] titles = {"重复"};

    private List<EditMonthFixedFragment> fragmentList;


    @BindView(R.id.tv_tarName)
    TextView tv_tarName;

    @BindView(R.id.tv_books)
    TextView tv_books;


    @BindView(R.id.start_time_txt)
    TextView start_time_txt;

    @BindView(R.id.end_time_txt)
    TextView end_time_txt;

    @BindView(R.id.alarm_new_time)
    TextView alarm_new_time;

    @BindView(R.id.monitor_names)
    TextView monitor_names;

    @BindView(R.id.ll_child)
    LinearLayout ll_child;

    @BindView(R.id.child_count)
    TextView child_count;

    @BindView(R.id.child_recycle)
    RecyclerView child_recycle;

    boolean editFlay = false;//开始时间是否可以修改

    String targetId = "", parentId = "", rootId = "";

    EditMonthFixedFragment week;

    List<SeletExcutor> selectMOnitorList;

    String finish_count = "", fine = "", classid = "", describe = "";

    Object supervior;

    String f_is_share = "1";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        requestdatas();
    }

    private void initView() {
        targetId = getIntent().getStringExtra(IntentParams.DAKA_Target_Id);

        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        checkStartTime();

    }


    private void requestdatas() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("target_uuid", targetId);
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
                            filldata(msg.getData());

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });

    }


    private void filldata(TargetDataBean data) {

        String urlImg = data.getTargetplan().getF_class_id();

        if (MyTextUtil.isEmpty(urlImg)) {
            iv_target_icon.setImageResource(R.drawable.dashang);
        } else {
            Picasso.with(mContext).load(urlImg).error(R.drawable.icon_no_download).into(iv_target_icon);
        }


        setRequestMonitor(data);

        finish_count = data.getTargetplan().getCount();
        classid = data.getTargetplan().getF_class_id();
        fine = MyTextUtil.isEmpty(data.getTargetplan().getF_fine()) ? "0" : data.getTargetplan().getF_fine();

        rootId = data.getTargetplan().getF_rid();
        parentId = data.getTargetplan().getF_pid();

        tv_tarName.setText(MyTextUtil.getDecodeStr(data.getTargetplan().getF_name()));
        start_time_txt.setText(data.getTargetplan().getF_start_date());
        end_time_txt.setText(data.getTargetplan().getF_end_date());
        alarm_new_time.setText(data.getTargetplan().getF_reminder_time());


        f_is_share = data.getTargetplan().getF_is_share();

        if (!MyTextUtil.isEmpty(f_is_share)) {
            if (f_is_share.equals("1")) {
                tv_books.setText("公开");
            } else if (f_is_share.equals("2")) {
                tv_books.setText("私密");
            }
        }

        checkRepeatWeek(data.getTargetplan().getF_repeat_date());
        parseData(data.getTargetplan().getChild());
    }


    private void setRequestMonitor(TargetDataBean data) {
        supervior = data.getTargetplan().getF_supervisor();

        selectMOnitorList = json2Person(supervior);

        monitor_names.setText(handleJson(selectMOnitorList));
    }

    private String handleJson(List<SeletExcutor> beforMonitor) {


        if (beforMonitor == null || beforMonitor.size() < 1)
            return "";

        StringBuffer buffer = new StringBuffer();

        for (SeletExcutor item : beforMonitor) {
            buffer.append(MyTextUtil.getDecodeStr(item.getName())).append(",");
        }
        String json = buffer.toString();
        return json.substring(0, json.length() - 1);
    }

    private List<SeletExcutor> json2Person(Object supervior) {

        selectMOnitorList = new ArrayList<>();

        if (supervior != null) {
            String jsonStr = new Gson().toJson(supervior);
            try {
                JSONArray myJsonArray = new JSONArray(jsonStr);
                if (myJsonArray != null && myJsonArray.length() > 0) {

                    int len = myJsonArray.length();
                    for (int i = 0; i < len; i++) {

                        JSONObject object = myJsonArray.getJSONObject(i);
                        SeletExcutor data = new SeletExcutor(object.getString("nickname"), object.getString("user_id"));
                        selectMOnitorList.add(data);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return selectMOnitorList;
    }


    private void parseData(List<ChildTargetBean> child) {

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
                mitent.putExtra(IntentParams.Target_Pid, data.getF_uuid());
                mitent.putExtra(IntentParams.Target_Root_id, data.getF_rid());
                mitent.putExtra(IntentParams.DAKA_Target_Id, data.getF_uuid());
                startActivity(mitent);
            }
        });


    }


    private void checkRepeatWeek(String f_repeat_date) {
        fragmentList = new ArrayList<EditMonthFixedFragment>();
        week = new EditMonthFixedFragment();
        week.setSelectWeeks(f_repeat_date);
        fragmentList.add(week);
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        target_pager.setAdapter(adapter);
        //绑定
        target_tab.setupWithViewPager(target_pager);
    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_edit_new_target;
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        //重写这个方法，将设置每个Tab的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @OnClick(R.id.add_child_item)
    public void goAdd() {
        Intent intent = new Intent(mContext, CreateTargetActivity.class);
        intent.putExtra(IntentParams.Intent_Enter_Type, "TargetGroupActivity");
        intent.putExtra(IntentParams.Target_Pid, parentId);
        intent.putExtra(IntentParams.Target_Root_id, rootId);
        startActivity(intent);
    }


    @OnClick(R.id.iv_back)
    public void back() {
        ActyFinishEvent.FromSelectQun = 1;
        finish();
    }


    @OnClick(R.id.ll_gomonitor)
    public void goMonitor() {

        if (f_is_share.equals("2")) {
            ToastUtils.getInstanc().showToast("私密目标不可以设置监督人");
            return;
        }


        ArrayList<String> userIds = new ArrayList<>();

        if (selectMOnitorList != null && selectMOnitorList.size() > 0) {
            for (SeletExcutor item : selectMOnitorList) {
                userIds.add(item.getUserId());
            }

        }


        Intent mIntent = new Intent(mContext, ExcutorSelectActivity.class);
        mIntent.putExtra(IntentParams.Intent_Enter_Type, "target");

        if (!MyTextUtil.isEmpty(fine) && !fine.equals("0")) {
            mIntent.putExtra(IntentParams.Intent_Have_FINE, "fine");//如果有挑战金，监督人不能删空
        }

        mIntent.putStringArrayListExtra("befor_select", userIds);
        startActivityForResult(mIntent, 0x15);
    }


    String selectMonitor;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 0x15) {

            selectMOnitorList = (List<SeletExcutor>) data.getSerializableExtra("selectExcutor");

            if (selectMOnitorList == null || selectMOnitorList.size() < 1) {
                monitor_names.setText("");
                return;
            }


            StringBuffer buffer = new StringBuffer();


            if (selectMOnitorList == null || selectMOnitorList.size() == 0) {
                monitor_names.setText("");
                return;
            }

            int len = selectMOnitorList.size();//选好的监督人

            for (int i = 0; i < len; i++) {
                buffer.append(selectMOnitorList.get(i).getName()).append(",");
            }
            String content = buffer.toString();
            selectMonitor = content.substring(0, content.length() - 1);


            if (selectMonitor.length() > 13) {
                String part1 = selectMonitor.substring(0, 13);
                String part2 = selectMonitor.substring(13, selectMonitor.length());
                monitor_names.setText(part1 + "\n" + part2);
            } else {
                monitor_names.setText(selectMonitor);
            }
        }
        if (resultCode == 0x55) {
            f_is_share = data.getStringExtra("f_is_share");
            if (f_is_share.equals("1")) {
                tv_books.setText("公开");
            } else if (f_is_share.equals("2")) {
                tv_books.setText("私密");
            }
        }

    }

    @OnClick(R.id.cancel_target)
    public void goCancelTarget() {
        AlertDialog.Builder buid = new AlertDialog.Builder(mContext);
        buid.setMessage("您确定要取消目标吗？");
        buid.setPositiveButton(
                "确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        cancelTarget();
                    }
                }
        );
        buid.setNegativeButton(
                "取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {

                    }
                }
        );

        AlertDialog dialog = buid.create();
        dialog.show();

        dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                .getColor(R.color.bar_grey_90));


    }

    private void cancelTarget() {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> maps = new HashMap<>();

        maps.put("command", "ok-api.targetCancel");
        maps.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        maps.put("target_id", getIntent().getStringExtra(IntentParams.DAKA_Target_Id));

        url.cancelTarget(maps).subscribeOn(Schedulers.io())
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

                            ToastUtils.getInstanc().showToast("目标取消成功！");
                            Intent intent = new Intent();
                            intent.setAction(EventConts.MSG_Finish_TargetDetail + "");
                            EventBus.getDefault().post(new XshellEvent(EventConts.Target_End));
                            sendBroadcast(intent);
                            finish();
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });

    }


    TimePickerDialog timePicker;

    @OnClick(R.id.start_linear)
    public void startDate() {
        showStartPopup(true);
    }

    @OnClick(R.id.end_linear)
    public void endDate() {
        showStartPopup(false);
    }


    @OnClick(R.id.txt_finish)
    public void updateTarget() {

        if (MyTextUtil.isEmpty(tv_tarName.getText().toString().trim())) {
            ToastUtils.getInstanc(this).showToast("请输入目标名称");
            return;
        }

        String content = getWeekDayStr();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.getInstanc(this).showToast("请选择周时间！");
            return;
        }

        if (MyTextUtil.isEmpty(monitor_names.getText().toString()) && f_is_share.equals("1")) {
            showMyDialog();
        } else {
            targetEdit(content);
        }
    }

    @OnClick(R.id.ll_shoucang)
    public void goPublic() {

        if (!MyTextUtil.isEmpty(monitor_names.getText().toString())) {
            ToastUtils.getInstanc().showToast("已经指定监督人的目标不可以设置为私密");
            return;
        }

        Intent intent = new Intent(mContext, Public2PrivateActivity.class);
        if (tv_books.getText().toString().equals("公开")) {
            f_is_share = "1";
        } else if (tv_books.getText().toString().equals("私密")) {
            f_is_share = "2";
        }
        intent.putExtra("f_is_share", f_is_share);
        startActivityForResult(intent, 0x31);
    }


    @OnClick(R.id.iv_add_alarm)
    public void alarmTime() {
        String[] hourMin = new String[2];
        if (!MyTextUtil.isEmpty(alarm_new_time.getText().toString()) && alarm_new_time.getText().toString().contains(":")) {
            hourMin = alarm_new_time.getText().toString().split(":");
        } else {
            hourMin = new String[]{"10", "30"};
        }

        if (timePicker == null) {
            timePicker = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String hourStr = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
                    String minStr = minute < 10 ? "0" + minute : minute + "";
                    String timeDate = hourStr + ":" + minStr;
                    alarm_new_time.setText(timeDate);

                }
            }, Integer.parseInt(hourMin[0]), Integer.parseInt(hourMin[1]), true);
        }
        timePicker.show();

    }


    //显示日期dialog
    private void showStartPopup(final boolean isBegin) {

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {

                String monthStr = getShowMoth(month);
                String dayStr = getShowDay(day);
                String selectDate = year + "-" + monthStr + "-" + dayStr;

                if (isBegin) {
                    if (end1beginTime(DateUtil.getCurrentData(), selectDate)) {
                        ToastUtils.getInstanc(mContext).showToast(mContext.getResources().getString(R.string.target_date_tip));
                        return;
                    }
                    start_time_txt.setText(selectDate);
                } else {
                    if (end1beginTime(start_time_txt.getText().toString(), selectDate)) {
                        ToastUtils.getInstanc(mContext).showToast(getResources().getString(R.string.target_info));
                        return;
                    }

                    String btime = DateUtil.getCurrentData();
                    if (end1beginTime(btime, selectDate)) {
                        if (end1beginTime(DateUtil.getCurrentData(), selectDate)) {//目标已经开始
                            ToastUtils.getInstanc(mContext).showToast(getResources().getString(R.string.target_info));
                            return;
                        }
                    }
                    end_time_txt.setText(selectDate);
                }

                //选择时间
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(mContext,
                0, listener, currentYear, month, day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        dialog.show();

    }

    private String getShowDay(int day) {
        if (day < 10) {
            return "0" + day;
        } else {
            return "" + day;
        }
    }

    private String getShowMoth(int month) {

        if (month + 1 < 10) {
            return "0" + (month + 1);
        } else {
            return "" + (month + 1);
        }

    }

    int currentYear, month, day;

    private boolean end1beginTime(String beginTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long end1 = sdf.parse(endTime).getTime();
            long begin1 = sdf.parse(beginTime).getTime();
            if (end1 < begin1) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }


    private void checkStartTime() {
        if (!getIntent().hasExtra(IntentParams.STATE_DATE) || MyTextUtil.isEmpty(getIntent().getStringExtra(IntentParams.STATE_DATE)))
            return;

        String start = getIntent().getStringExtra(IntentParams.STATE_DATE);

        try {
            long timgLong = DateUtil.stringToDate(start, "yyyy-MM-dd");

            String today = DateUtil.getSelectDay(0);

            long todayLong = DateUtil.stringToDate(today, "yyyy-MM-dd");

            if (timgLong <= todayLong) {
                start_linear.setEnabled(false);
                editFlay = false;

            } else {
                start_linear.setEnabled(true);
                editFlay = true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    private void targetEdit(String content) {

        String remidTime = "", supvStr = "";
        if (!MyTextUtil.isEmpty(alarm_new_time.getText().toString())) {
            remidTime = alarm_new_time.getText().toString();
        }
        if (selectMOnitorList != null && selectMOnitorList.size() > 0) {
            supvStr = createJson();

        } else {
            supvStr = "";
        }
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        Map<String, String> maps = new HashMap<>();

        maps.put("command", "ok-api.UpdateTarget");
        maps.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        maps.put("name", MyTextUtil.getUrl3Encoe(tv_tarName.getText().toString()));


        if (editFlay) {
            maps.put("start_date", start_time_txt.getText().toString());//目标已经 开始不让修改
        }

        maps.put("end_date", end_time_txt.getText().toString());

        maps.put("supervisor", supvStr);

        maps.put("describe", "");

        maps.put("repeat_date", content);
        maps.put("target_uuid", getIntent().getStringExtra(IntentParams.DAKA_Target_Id));
        maps.put("is_share", f_is_share);
        if (!MyTextUtil.isEmpty(remidTime)) {
            maps.put("reminder_time", remidTime);
        } else {
            maps.put("reminder_time", "");
        }
        if (!MyTextUtil.isEmpty(parentId)) {
            maps.put("pid", parentId);
        } else {
            maps.put("pid", "");
        }
//        maps.put("finish_count", finish_count);
//        maps.put("describe", describe);

        maps.put("fine", fine);
        maps.put("classid", classid);

        url.editTarget(maps).subscribeOn(Schedulers.io())
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

                            if (MyTextUtil.isEmpty(monitor_names.getText().toString())) {

                            } else {
                                if (MyTextUtil.isEmpty(fine) || fine.equals("0")) {
                                    Intent intent = new Intent(mContext, TargetSuccedActivity.class);
                                    intent.putExtra(IntentParams.Target_Name, tv_tarName.getText().toString());
                                    intent.putExtra(IntentParams.DAKA_Target_Id, targetId);
                                    intent.putExtra(IntentParams.STATE_DATE, start_time_txt.getText().toString());
                                    intent.putExtra(IntentParams.END_DATE, end_time_txt.getText().toString());
                                    intent.putExtra(IntentParams.SELECT_MONITOR, monitor_names.getText().toString());
                                    startActivity(intent);
                                }
                            }

                            EventBus.getDefault().post(new XshellEvent(EventConts.Target_Update));//重新加载目标详情页

                            finish();

                        } else {
                            ToastUtils.getInstanc().showToast("服务异常");
                        }
                    }
                });

    }


    private String createJson() {
//        Map 装
//        List 装Map
//        List装Json字符串
        try {
            List<Map<String, String>> data = new ArrayList<>();
            int len = selectMOnitorList.size();
            for (int i = 0; i < len; i++) {
                SeletExcutor bean = selectMOnitorList.get(i);
                HashMap<String, String> map = new HashMap<>();
                map.put("nickname", URLEncoder.encode(bean.getName(), "UTF-8"));
                map.put("user_id", bean.getUserId());
                data.add(map);
            }
            Gson gson = new Gson();
            return gson.toJson(data);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("amtf", "error:不支持 utf-8编码");
        }
        return "";
    }

    public String getWeekDayStr() {

        List<String> seletWeeks = week.getSelectWeeks();

        if (seletWeeks == null || seletWeeks.size() == 0) {
            return "7";
        }

        int len = seletWeeks.size();

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < len; i++) {
            if (seletWeeks.get(i).equals("周一")) {
                buffer.append("1").append(",");
            } else if (seletWeeks.get(i).equals("周二")) {
                buffer.append("2").append(",");
            } else if (seletWeeks.get(i).equals("周三")) {
                buffer.append("3").append(",");
            } else if (seletWeeks.get(i).equals("周四")) {
                buffer.append("4").append(",");
            } else if (seletWeeks.get(i).equals("周五")) {
                buffer.append("5").append(",");
            } else if (seletWeeks.get(i).equals("周六")) {
                buffer.append("6").append(",");
            } else if (seletWeeks.get(i).equals("周日")) {
                buffer.append("7").append(",");
            }
        }

        String contais = buffer.toString();

        if (contais.length() > 1) {
            return contais.substring(0, contais.length() - 1);
        } else {
            return contais;
        }

    }


    MyMiddleDialog myMiddleDialog;

    private void showMyDialog() {
        if (myMiddleDialog == null) {
            myMiddleDialog = new MyMiddleDialog(this, R.style.MyMiddleDialogStyle) {
                @Override
                protected View getView() {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    View view = inflater.inflate(R.layout.dialog_no_monitor, null);
                    initDialogView(view);
                    return view;
                }
            };
        }
        myMiddleDialog.show();
    }

    private void initDialogView(View view) {

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMiddleDialog.dismiss();
                targetEdit(getWeekDayStr());

            }
        });

        view.findViewById(R.id.conform).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMiddleDialog.dismiss();

                Intent mIntent = new Intent(mContext, ExcutorSelectActivity.class);
                mIntent.putExtra(IntentParams.Intent_Enter_Type, "target");
                startActivityForResult(mIntent, 0x15);

            }
        });

    }


}
