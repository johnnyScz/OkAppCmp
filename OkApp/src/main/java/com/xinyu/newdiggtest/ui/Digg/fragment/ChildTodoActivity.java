package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.MonitorChildBean;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.Digg.ExcutorSelectActivity;
import com.xinyu.newdiggtest.ui.Digg.punchcard.DakaSelectTargetActivity;
import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 新建子待办
 */
public class ChildTodoActivity extends BaseNoEventActivity {


    @BindView(R.id.target_name)
    public TextView target_name;//关联目标


    @BindView(R.id.excuter)
    public TextView excuter;//执行人

    @BindView(R.id.asin_date)
    public TextView asin_date;//制定日期


    @BindView(R.id.alarm_date)
    public TextView alarm_date;//提醒时间


    int currentYear, month, day;

    String curentTime;

    DatePickerDialog dialog;

    TimePickerDialog timePicker;

    String watch_img = "";

    String targetName = "", targetUid = "";


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_todo_child;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

        initDateTime();


    }

    /**
     * 当前日期和时间
     */
    private void initDateTime() {
        String today = DateUtil.getCurrentData();
        curentTime = DateUtil.getCurrentDataTime();

        asin_date.setText(today);
        alarm_date.setText(curentTime);

        excuter.setText(PreferenceUtil.getInstance(mContext).getNickName());

        //-------------开始年份不从1900年开始 -----------------------
        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

    }

    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @OnClick(R.id.commit)
    public void commit() {

        commitFinal();

    }

    @OnClick(R.id.ll_excutor)
    public void excutor() {
        startActivityForResult(new Intent(mContext, ExcutorSelectActivity.class), 0x33);
    }


    @OnClick(R.id.ll_target)
    public void guanlian() {
        String selectData = DateUtil.getCurrentData();
        Intent intent = new Intent(mContext, DakaSelectTargetActivity.class);
        intent.putExtra(IntentParams.SELECT_DATA, selectData);
        intent.putExtra(IntentParams.Intent_Enter_Type, "TodoAddActivity");

        startActivityForResult(intent, 0x14);
    }


    @OnClick(R.id.ll_asign_date)
    public void asign() {
        if (dialog == null) {
            dialog = new DatePickerDialog(mContext,
                    0, listener, currentYear, month, day);
            //后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        }
        dialog.show();
    }

    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {

            String endTime = "";
            if (month < 9) {
                endTime = year + "-0" + (month + 1) + "-" + day;
            } else {
                endTime = year + "-" + (month + 1) + "-" + day;
            }

            String btime = DateUtil.getCurrentData();
            if (DateUtil.end2beginTime(btime, endTime)) {
                ToastUtils.getInstanc().showToast("开始时间不能早于今天");
                return;
            }
            asin_date.setText(endTime);
        }

    };


    @OnClick(R.id.ll_alarm_date)
    public void alarm() {
        String[] hourMin = curentTime.split(":");
        if (timePicker == null) {
            timePicker = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    String hourStr = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
                    String minStr = minute < 10 ? "0" + minute : minute + "";
                    String timeDate = hourStr + ":" + minStr;
                    if (checkCurentTime(timeDate)) {
                        ToastUtils.getInstanc().showToast("提醒时间不能早于当前时间，请重新选择");
                    } else {
                        alarm_date.setText(timeDate);
                    }


                }
            }, Integer.parseInt(hourMin[0]), Integer.parseInt(hourMin[1]), true);
        }
        timePicker.show();
    }

    private boolean checkCurentTime(String timeDate) {
        String date = asin_date.getText().toString();
        if (date.equals(DateUtil.getCurrentData())) {
            //如果时间是今天
            String curentTime = DateUtil.getCurrentDataTime();
            if (mathCheck(curentTime, timeDate)) {
                return true;
            }
        }

        return false;
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == 0x33) {
                if (data.hasExtra("monitor")) {
                    showDatas(AppContacts.SELECT_Monitor);
                }

            } else if (requestCode == 0x14) {
                targetName = data.getStringExtra(IntentParams.SELECT_Target);
                targetUid = data.getStringExtra(IntentParams.SELECT_TargetId);
                target_name.setText("#" + targetName + "#");
            }
        }


    }

    private void showDatas(List<MonitorChildBean> selectExcutor) {
        StringBuffer buffer = new StringBuffer();
        for (MonitorChildBean item : selectExcutor) {
            buffer.append(item.getUser().getNickname()).append(",");
        }
        String finaStr = buffer.substring(0, buffer.length() - 1);
        excuter.setText(finaStr);

    }

    private void commitFinal() {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.InsertTargetExecute");
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("title", "待传的名字");
        map.put("to_user", uionUserId());
        map.put("plan_date", asin_date.getText().toString());
        map.put("reminder_time", alarm_date.getText().toString());

        if (!MyTextUtil.isEmpty(watch_img)) {
            map.put("watch_img", watch_img);
        }

        if (!MyTextUtil.isEmpty(targetUid)) {
            map.put("target_id", targetUid);
        }

        url.commitTodo(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        if (msg.getOp().getCode().equals("Y")) {
                            ToastUtils.getInstanc().showToast("提交成功了");
                            finish();
                        }
                    }
                });


    }

    private String uionUserId() {
        String userIdss = "";
        if (excuter.getText().toString().equals(PreferenceUtil.getInstance(mContext).getNickName())) {
            userIdss = PreferenceUtil.getInstance(mContext).getUserId();
        } else {
            if (AppContacts.SELECT_Monitor != null && AppContacts.SELECT_Monitor.size() > 0) {
                StringBuffer buffer = new StringBuffer();
                for (MonitorChildBean item : AppContacts.SELECT_Monitor) {
                    buffer.append(item.getUser().getUser_id()).append(",");
                }
                userIdss = buffer.substring(0, buffer.length() - 1);
            }
        }

        return userIdss;
    }


}




