package com.xinyu.newdiggtest.ui.Digg.target;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.tu.loadingdialog.LoadingDailog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.CreateTargetBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.BaseSubscriber;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;


import com.xinyu.newdiggtest.seriable.SeletExcutor;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.Digg.ExcutorSelectActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.Public2PrivateActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.TargetSuccedActivity;


import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.DialogUtil;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyMiddleDialog;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateTargetActivity extends BaseNoEventActivity implements View.OnClickListener {
    @BindView(R.id.target_pager)
    ViewPager target_pager;
    @BindView(R.id.target_tab)
    TabLayout target_tab;
    @BindView(R.id.start_linear)
    RelativeLayout start_linear;
    @BindView(R.id.end_linear)
    RelativeLayout end_linear;
    @BindView(R.id.start_time_txt)
    TextView start_time_txt;
    @BindView(R.id.end_time_txt)
    TextView end_time_txt;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.txt_finish)
    TextView txt_finish;

    @BindView(R.id.tv_names)
    TextView monitorNames;

    @BindView(R.id.title)
    TextView title;


    @BindView(R.id.tv_public)
    TextView tv_public;


    @BindView(R.id.iv_target_icon)
    ImageView iv_target_icon;


    @BindView(R.id.alarm_new_time)
    TextView showTime;


    String f_isShare = "1";


    @BindView(R.id.tv_tarName)
    EditText targetName;

    String selectMonitor;

    Context mContext;

    LoadingDailog myDialog;


    int currentYear, month, day;

    boolean editFlay = false;//开始时间是否可以修改

    String curentTime;
    //    private DatePickerPopup pickerPopup;
//    private Date leftDate;
    private static final String TAG = "CreateTargetActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        start_linear.setOnClickListener(this);
        end_linear.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        txt_finish.setOnClickListener(this);

        AppContacts.SELECT_Monitor = null;
        pushIntentData();
        initFrag();
        curentTime = DateUtil.getCurrentDataTime();
        myDialog = new DialogUtil(this).buildDialog("加载中...");

    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_edit_target;
    }

    private void initFrag() {
        initView();
        fragmentList = new ArrayList<EveryMonthFixedFragment>();
//        fragmentList.add(new EveryMonthFixedFragment());
        week = new EveryMonthFixedFragment();
        week.setSelectWeeks(getIntent().getStringExtra(IntentParams.Repeat_Week));
        fragmentList.add(week);
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        target_pager.setAdapter(adapter);
        //绑定
        target_tab.setupWithViewPager(target_pager);

    }

    EveryMonthFixedFragment week;


    private void initView() {

        if (getIntent().hasExtra(IntentParams.Alarm_Time)) {
            showTime.setText(getIntent().getStringExtra(IntentParams.Alarm_Time));
        }

        if (getIntent().hasExtra(IntentParams.Intent_Eidt_target)) {//从编辑进来的
            pushIntentData();
            checkStartTime();

        } else {
            getCurrentDay();
        }


    }

    /**
     * 检查目标开始时间是否早于当天
     */
    private void checkStartTime() {

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


    private void pushIntentData() {


        String imgUrl = getIntent().getStringExtra(IntentParams.Target_create_postion);

        if (!MyTextUtil.isEmpty(imgUrl)) {
            Picasso.with(mContext).load(imgUrl).error(R.drawable.icon_no_download).into(iv_target_icon);
        }


        title.setText("新建目标");

        //------------此处为了让datePicker不从1900年开始 ---------------------
        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        if (getIntent().hasExtra(IntentParams.Target_create_Custom)) {
            targetName.setHint("请输入名称");
        } else {
            targetName.setText(getIntent().getStringExtra(IntentParams.Target_Name));
        }


        if (getIntent().hasExtra(IntentParams.Intent_Enter_Type)
                && getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("child")) {
            //TODO 可能要选择黄色图标
            iv_target_icon.setImageResource(R.drawable.dashang);
        }


        if (getIntent().hasExtra(IntentParams.SELECT_MONITOR)) {
            String names = getIntent().getStringExtra(IntentParams.SELECT_MONITOR);
            if (!MyTextUtil.isEmpty(names)) {
                if (names.length() > 13) {
                    String part1 = names.substring(0, 13);
                    String part2 = names.substring(13, names.length());
                    monitorNames.setText(part1 + "\n" + part2);
                } else {
                    monitorNames.setText(names);
                }

            }
        }


        start_time_txt.setText(getIntent().getStringExtra(IntentParams.STATE_DATE));
        end_time_txt.setText(getIntent().getStringExtra(IntentParams.END_DATE));


    }


    private void getCurrentDay() {
        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        start_time_txt.setText(DateUtil.getDayOCurrentMonth());
        end_time_txt.setText(DateUtil.getDayOfNextMonth());

    }

    //    private String[] titles = {"完成次数", "每周重复", "每月固定"};
    private String[] titles = {"重复"};

    private List<EveryMonthFixedFragment> fragmentList;

    public void createTarget(String weeks) {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        String name = targetName.getText().toString().trim();
        map.put("name", MyTextUtil.getUrl3Encoe(name));
        map.put("command", " ok-api.createTarget");
        map.put("end_date", end_time_txt.getText().toString());
        map.put("start_date", start_time_txt.getText().toString());
        map.put("type", "2"); //目标完成类型：1：按次数设定；2：按周设定；3：按月设定
        map.put("repeat_date", weeks);
        map.put("sid", PreferenceUtil.getInstance(this).getSessonId());
        map.put("user_id", PreferenceUtil.getInstance(this).getUserId());
        map.put("is_share", f_isShare);


        if (!MyTextUtil.isEmpty(monitorNames.getText().toString())) {
            map.put("supervisor", createJson());
        }

        if (!MyTextUtil.isEmpty(showTime.getText().toString())) {
            map.put("reminder_time", showTime.getText().toString());
        }


        if (getIntent().hasExtra(IntentParams.Target_create_postion)) {
            map.put("class_id", getIntent().getStringExtra(IntentParams.Target_create_postion));
        }

        if (getIntent().hasExtra(IntentParams.Target_Pid)) {
            map.put("pid", getIntent().getStringExtra(IntentParams.Target_Pid));
        }

        if (getIntent().hasExtra(IntentParams.Target_Root_id)) {
            map.put("rid", getIntent().getStringExtra(IntentParams.Target_Root_id));
        }
//        map.put("company_id", "xinyu2013");


        url.createTarget1(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<CreateTargetBean>(this) {
                    @Override
                    public void onNext(CreateTargetBean s) {
                        if (s.getOp().getCode().equals("Y")) {
                            if (s.getData() != null && !MyTextUtil.isEmpty(s.getData().getF_target_id())) {

                                if (MyTextUtil.isEmpty(monitorNames.getText().toString())) {
                                    finish();
                                } else {
                                    Intent intent = new Intent(CreateTargetActivity.this, TargetSuccedActivity.class);
                                    intent.putExtra(IntentParams.Target_Name, targetName.getText().toString());
                                    intent.putExtra(IntentParams.DAKA_Target_Id, s.getData().getF_target_id());
                                    intent.putExtra(IntentParams.SELECT_MONITOR, selectMonitor);
                                    intent.putExtra(IntentParams.STATE_DATE, start_time_txt.getText().toString());
                                    intent.putExtra(IntentParams.END_DATE, end_time_txt.getText().toString());
                                    startActivity(intent);
                                    finish();
                                }
                            }


                        } else {
                            ToastUtils.getInstanc(CreateTargetActivity.this).showToast("创建失败");
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e("amtf", e.getMessage());
                        ToastUtils.getInstanc(CreateTargetActivity.this).showToast("服务异常");
                    }
                });
    }

    private String createJson() {
//        Map 装
//        List 装Map
//        List装Json字符串
        try {
            List<Map<String, String>> data = new ArrayList<>();
            int len = datalist.size();
            for (int i = 0; i < len; i++) {
                SeletExcutor bean = datalist.get(i);
                HashMap<String, String> map = new HashMap<>();
                map.put("nickname", MyTextUtil.getUrl3Encoe(bean.getName()));
                map.put("user_id", bean.getUserId());
                data.add(map);
            }
            Gson gson = new Gson();
            return gson.toJson(data);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("amtf", "error:不支持 utf-8编码");
        }
        return "";
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_linear: //开始日期
                showStartPopup(true);
                break;

            case R.id.end_linear:  //结束日期
                showStartPopup(false);
                break;
            case R.id.iv_back:
                CreateTargetActivity.this.finish();
                break;
            case R.id.txt_finish:
                if (MyTextUtil.isEmpty(targetName.getText().toString().trim())) {
                    ToastUtils.getInstanc(this).showToast("请输入目标名称");
                    return;
                }

                String content = getWeekDayStr(AppContacts.SELECT_WEEKS);
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.getInstanc(this).showToast("请选择周时间！");
                    return;
                }

                if (MyTextUtil.isEmpty(monitorNames.getText().toString()) && f_isShare.equals("1")) {
                    showMyDialog();
                } else {
                    createTarget(content);
                }


                break;
            default:

                break;
        }

    }


    public String getWeekDayStr(List<String> seletWeeks) {
        int len = seletWeeks.size();
        if (len == 0) {
            return "";
        }

        List<String> numweek = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            if (seletWeeks.get(i).equals("周一")) {
                numweek.add("1");
            } else if (seletWeeks.get(i).equals("周二")) {
                numweek.add("2");
            } else if (seletWeeks.get(i).equals("周三")) {
                numweek.add("3");
            } else if (seletWeeks.get(i).equals("周四")) {
                numweek.add("4");
            } else if (seletWeeks.get(i).equals("周五")) {
                numweek.add("5");
            } else if (seletWeeks.get(i).equals("周六")) {
                numweek.add("6");
            } else if (seletWeeks.get(i).equals("周日")) {
                numweek.add("7");
            }
        }

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < len; i++) {
            buffer.append(numweek.get(i)).append(",");
        }
        String contais = buffer.toString();


        return contais.substring(0, contais.length() - 1);

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


    @OnClick(R.id.ll_gomonitor)
    public void goCommit() {

        if (tv_public.getText().toString().equals("私密")) {
            ToastUtils.getInstanc().showToast("私密目标不可以设置监督人");
            return;
        }


        Intent mIntent = new Intent(mContext, ExcutorSelectActivity.class);

        if (userIds != null && userIds.size() > 0)
            mIntent.putStringArrayListExtra("befor_select", userIds);

        mIntent.putExtra(IntentParams.Intent_Enter_Type, "target");
        startActivityForResult(mIntent, 0x15);


    }

    List<SeletExcutor> datalist;

    ArrayList<String> userIds;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 0x15) {


            datalist = (List<SeletExcutor>) data.getSerializableExtra("selectExcutor");

            if (datalist == null || datalist.size() < 1) {
                monitorNames.setText("");
                userIds.clear();
                return;
            }


            StringBuffer buffer = new StringBuffer();

            userIds = new ArrayList<>();

            int len = datalist.size();//选好的监督人

            for (int i = 0; i < len; i++) {
                buffer.append(datalist.get(i).getName()).append(",");
                userIds.add(datalist.get(i).getUserId());
            }
            String content = buffer.toString();
            selectMonitor = content.substring(0, content.length() - 1);

//            monitorNames.setText(selectMonitor);

            if (selectMonitor.length() > 13) {
                String part1 = selectMonitor.substring(0, 13);
                String part2 = selectMonitor.substring(13, selectMonitor.length());
                monitorNames.setText(part1 + "\n" + part2);
            } else {
                monitorNames.setText(selectMonitor);
            }


        }
        if (resultCode == 0x55) {
            f_isShare = data.getStringExtra("f_is_share");
            if (f_isShare.equals("1")) {
                tv_public.setText("公开");
            } else if (f_isShare.equals("2")) {
                tv_public.setText("私密");
            }
        }
    }


    TimePickerDialog timePicker;


    @OnClick(R.id.iv_add_alarm)
    public void addTime() {
        String[] hourMin = curentTime.split(":");
        if (timePicker == null) {
            timePicker = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    String hourStr = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
                    String minStr = minute < 10 ? "0" + minute : minute + "";
                    String timeDate = hourStr + ":" + minStr;

                    String curentTime = DateUtil.getCurrentDataTime();

                    if (mathCheck(curentTime, timeDate)) {
                        ToastUtils.getInstanc().showToast("提醒时间不能早于当前时间，请重新选择");
                    } else {
                        showTime.setText(timeDate);
                    }

                }
            }, Integer.parseInt(hourMin[0]), Integer.parseInt(hourMin[1]), true);
        }
        timePicker.show();
    }


    private boolean mathCheck(String curentTime, String timeDate) {
        String[] curent = curentTime.split(":");
        String[] select = timeDate.split(":");

        String curentMath = curent[0] + curent[1];
        String selectMath = select[0] + select[1];

        if (Integer.parseInt(curentMath) > Integer.parseInt(selectMath)) {
            return true;
        }

        return false;
    }


    @OnClick(R.id.tv_public)
    public void goPub() {

        if (!MyTextUtil.isEmpty(monitorNames.getText().toString())) {
            ToastUtils.getInstanc().showToast("已经指定监督人的目标不可以设置为私密!");
            return;
        }

        Intent intent = new Intent(mContext, Public2PrivateActivity.class);
        intent.putExtra("f_is_share", f_isShare);
        startActivityForResult(intent, 0x31);


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

                createTarget(getWeekDayStr(AppContacts.SELECT_WEEKS));

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
