package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.nanchen.compresshelper.CompressHelper;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.AskTopBean;
import com.xinyu.newdiggtest.bean.BaseUser;
import com.xinyu.newdiggtest.bean.ImImgBean;
import com.xinyu.newdiggtest.bean.UploadUrlBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.seriable.SeletExcutor;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.Digg.ExcutorSelectActivity;
import com.xinyu.newdiggtest.ui.Digg.punchcard.DakaSelectTargetActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.FileImgUtil;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PhotoDialog;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;
import me.zhouzhuo.zzimagebox.ZzImageBox;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 待办列表
 */
public class TodoAddActivity extends BaseNoEventActivity {

    @BindView(R.id.todo_name)
    public EditText toDoName;//待办名称

    @BindView(R.id.target_name)
    public TextView target_name;//关联目标


    @BindView(R.id.excuter)
    public TextView excuter;//执行人

    @BindView(R.id.asin_date)
    public TextView asin_date;//制定日期


    @BindView(R.id.alarm_date)
    public TextView alarm_date;//提醒时间

    @BindView(R.id.zz_image_box)
    public ZzImageBox imagebox;


    int currentYear, month, day;

    String curentTime;

    DatePickerDialog dialog;

    TimePickerDialog timePicker;

    String watch_img = "";

    String targetName = "", targetUid = "", excutorList = "", aSignDate = "", alarmDate = "";


    int enterType = 0;//(0 新建 1 编辑)

    @BindView(R.id.tilte)
    public TextView tilte;//目标名称

    ExecutorService excutor = Executors.newCachedThreadPool();

    List<SeletExcutor> selectExcutor;

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_todo;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        imagebox.setOnImageClickListener(new ZzImageBox.OnImageClickListener() {
            @Override
            public void onImageClick(int position, String filePath, ImageView iv) {
                Log.e("amtf", "onImageClick 点击了");
            }

            @Override
            public void onDeleteClick(int position, String filePath) {
                imagebox.removeImage(position);

                if (imagebox.getAllImages().size() < 1) {
                    imagebox.setVisibility(View.GONE);
                }


            }

            @Override
            public void onAddClick() {

            }
        });

        if (getIntent().hasExtra(IntentParams.Intent_Enter_Type)
                && getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("edit")) {
            enterType = 1;
            tilte.setText("编辑待办");
        } else {
            enterType = 0;
            tilte.setText("新建待办");
        }

        if (enterType == 0) {
            initDateTime();
        } else {
            initIntentData();
            EidtOriginalData();
        }
    }

    private void initIntentData() {
        if (AppContacts.EditTODO == null)
            return;
        targetName = MyTextUtil.getDecodeStr(AppContacts.EditTODO.getF_title());
        excutorList = MyTextUtil.getExcutorListUserId(AppContacts.EditTODO.getExecutorlist());
        targetUid = AppContacts.EditTODO.getF_target_uuid();

        aSignDate = AppContacts.EditTODO.getF_plan_date();
        alarmDate = AppContacts.EditTODO.getF_reminder_time();
    }

    /**
     * 编辑之前的
     */
    private void EidtOriginalData() {
        if (AppContacts.EditTODO == null) return;


//        String watch_img = "";
//
//        String targetName = "", targetUid = "", excutorList = "";


        toDoName.setText(targetName);

        String namess = MyTextUtil.getExcutorListNames(AppContacts.EditTODO.getExecutorlist());

        if (!MyTextUtil.isEmpty(namess)) {
            excuter.setText(namess);
        }

        String plan_date = AppContacts.EditTODO.getF_plan_date();
        asin_date.setText(plan_date);

        if (!MyTextUtil.isEmpty(AppContacts.EditTODO.getF_reminder_time())) {
            alarm_date.setText(AppContacts.EditTODO.getF_reminder_time());
        }

        if (!MyTextUtil.isEmpty(AppContacts.EditTODO.getF_target_name())) {
            target_name.setText("#" + MyTextUtil.getDecodeStr(AppContacts.EditTODO.getF_target_name()) + "#");
        }

        if (AppContacts.EditTODO.getF_watch_img() != null && AppContacts.EditTODO.getF_watch_img().size() > 0) {
            showImgs();
        }


    }


//    List<String> filePaths = new ArrayList<>();

    /**
     * 显示图片
     */

    List<LocalMedia> beforDate = new ArrayList<>();

    private void showImgs() {

        if (AppContacts.EditTODO == null || AppContacts.EditTODO.getF_watch_img() == null)
            return;

        final int len = AppContacts.EditTODO.getF_watch_img().size();


        excutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < len; i++) {
                        Bitmap bitmap = FileImgUtil.returnBitMap(AppContacts.EditTODO.getF_watch_img().get(i).getOriginal());
                        String imageName = System.currentTimeMillis() + "" + i + ".png";
                        File file = FileImgUtil.saveFile(bitmap, imageName);

                        LocalMedia imgBean = new LocalMedia();
                        imgBean.setCompressPath(file.getPath());
                        beforDate.add(imgBean);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (beforDate.size() > 0) {
                        mhandler.sendEmptyMessage(0x22);
                    }
                }

            }
        });
    }


    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x22) {
                imagebox.setVisibility(View.VISIBLE);
                int lent = beforDate.size();
                for (int i = 0; i < lent; i++) {
                    imagebox.addImage(beforDate.get(i).getCompressPath());
                }
            }
        }
    };


    /**
     * 当前日期和时间
     */
    private void initDateTime() {
        String today = DateUtil.getCurrentData();
        curentTime = DateUtil.getCurrentDataTime();

        asin_date.setText(today);
        alarm_date.setText(curentTime);

        excuter.setText(PreferenceUtil.getInstance(mContext).getNickName());

    }

    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @OnClick(R.id.commit)
    public void commit() {
        if (MyTextUtil.isEmpty(toDoName.getText().toString())) {
            ToastUtils.getInstanc().showToast("请输入待办名称");
            return;
        }
        if (imagebox.getAllImages() == null || imagebox.getAllImages().size() == 0) {

            if (enterType == 0) {
                commitFinal();
            } else {
                editFinal();
            }


        } else {
            upLoadImg();
        }


    }

    /**
     * 编辑待办
     */
    private void editFinal() {

        loadingDailog.show();

        AskTopBean oriData = AppContacts.EditTODO;

        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.UpdateTargetExecuteByUuid");
        map.put("plan_id", oriData.getF_plan_id());
        if (!MyTextUtil.isEmpty(targetUid)) {
            map.put("target_id", targetUid);
        }

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("reminder_time", alarm_date.getText().toString());

        map.put("pid", oriData.getF_pid());

        map.put("title", MyTextUtil.getUrl3Encoe(toDoName.getText().toString()));

        if (!MyTextUtil.isEmpty(watch_img)) {
            map.put("watch_img", watch_img);
        }
        map.put("plan_date", asin_date.getText().toString());
        map.put("exe_id", excutorList);


        url.editTodu(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", e.getMessage());

                    }

                    @Override
                    public void onNext(Info msg) {
                        loadingDailog.dismiss();

                        if (msg.getOp().getCode().equals("Y")) {
                            ToastUtils.getInstanc().showToast("待办编辑成功！");
                            EventBus.getDefault().post(new XshellEvent(EventConts.HomeFresh));
                            finish();
                        } else {
                            ToastUtils.getInstanc().showToast("编辑待办服务异常");
                        }
                    }
                });

    }


    @OnClick(R.id.ll_excutor)
    public void excutor() {

        Intent mitent = new Intent(mContext, ExcutorSelectActivity.class);

        mitent.putStringArrayListExtra("befor_select", getLastSelectMonitor());

        startActivityForResult(mitent, 0x33);
    }

    ArrayList<String> userIds;

    private ArrayList<String> getLastSelectMonitor() {

        userIds = new ArrayList<>();
        userIds.clear();
        if (selectExcutor != null && selectExcutor.size() > 0) {
            for (SeletExcutor item : selectExcutor) {
                userIds.add(item.getUserId());
            }
        } else {
            if (enterType == 1) {//编辑待办，才去查返回的待办人
                if (AppContacts.EditTODO.getExecutorlist() != null && AppContacts.EditTODO.getExecutorlist().size() > 0) {
                    for (BaseUser item : AppContacts.EditTODO.getExecutorlist()) {
                        userIds.add(item.getUser_id());
                    }
                }

            }

        }
        return userIds;
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
        String[] hourMin;
        if (MyTextUtil.isEmpty(alarm_date.getText().toString())) {
            hourMin = DateUtil.getCurrentDataTime().split(":");
        } else {
            hourMin = alarm_date.getText().toString().split(":");
        }
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


    @OnClick(R.id.upload_img)
    public void uploadImg() {
        new PhotoDialog(mContext);
    }

    private final int PICT_RESULT = 1;

    private final int CAMARA = 0;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == 0x33) {
                if (data.hasExtra("monitor")) {
                    showDatas(data);
                }

            } else if (requestCode == 0x14) {
                targetName = data.getStringExtra(IntentParams.SELECT_Target);
                targetUid = data.getStringExtra(IntentParams.SELECT_TargetId);
                target_name.setText("#" + MyTextUtil.getDecodeStr(targetName) + "#");
            }
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICT_RESULT:
                    // 图片、视频、音频选择结果回调

                    if (imagebox.getAllImages() != null && imagebox.getAllImages().size() >= 9) {
                        ToastUtils.getInstanc().showToast("最多只能上传9张图片");
                        return;
                    }

                    int len = imagebox.getAllImages().size();

                    List<LocalMedia> nowSelect = PictureSelector.obtainMultipleResult(data);
                    if (nowSelect == null || nowSelect.size() < 1)
                        return;

                    imagebox.setVisibility(View.VISIBLE);

                    if (len + nowSelect.size() <= 9) {
                        for (int i = 0; i < nowSelect.size(); i++) {
                            imagebox.addImage(nowSelect.get(i).getCompressPath());
                        }
                    } else {
                        int can = 9 - len;

                        for (int i = 0; i < can; i++) {
                            imagebox.addImage(nowSelect.get(i).getCompressPath());
                        }

                    }


                    break;
                case CAMARA:
                    if (imagebox.getAllImages().size() >= 9) {
                        ToastUtils.getInstanc().showToast("最多只能上传9张图片!");
                        return;
                    }
                    LocalMedia phto = PictureSelector.obtainMultipleResult(data).get(0);
                    imagebox.setVisibility(View.VISIBLE);
                    imagebox.addImage(phto.getCompressPath());
                    break;
            }
        }
    }

    private void showDatas(Intent data) {

        selectExcutor = (List<SeletExcutor>) data.getSerializableExtra("selectExcutor");

        if (selectExcutor == null || selectExcutor.size() < 1)
            return;

        StringBuffer buffer = new StringBuffer();

        StringBuffer ecutorList = new StringBuffer();

        for (SeletExcutor item : selectExcutor) {
            buffer.append(item.getName()).append(",");
            ecutorList.append(item.getUserId()).append(",");
        }
        String finaStr = buffer.substring(0, buffer.length() - 1);

        String monitors = ecutorList.toString();
        excutorList = monitors.substring(0, monitors.length() - 1);

        excuter.setText(finaStr);

    }

    private void commitFinal() {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.InsertTargetExecute");
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());

        String name = toDoName.getText().toString();
        map.put("title", MyTextUtil.getUrl3Encoe(name));
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

                            EventBus.getDefault().post(new XshellEvent(EventConts.HomeFresh));

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
            if (selectExcutor != null && selectExcutor.size() > 0) {
                StringBuffer buffer = new StringBuffer();
                for (SeletExcutor item : selectExcutor) {
                    buffer.append(item.getUserId()).append(",");
                }
                userIdss = buffer.substring(0, buffer.length() - 1);
            }
        }

        return userIdss;
    }


    private void upLoadImg() {
        loadingDailog.show();
        List<String> pathList = imagebox.getAllImages();

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM).addFormDataPart("TOKEN", PreferenceUtil.getInstance(this).getSessonId());
//                .addFormDataPart(TOKEN, token);自定义参数key常量类，即参数名
        //多张图片
        for (int i = 0; i < pathList.size(); i++) {
            File file = new File(pathList.get(i));//filePath 图片地址

            File newFile = CompressHelper.getDefault(this).compressToFile(file);
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), newFile);
            builder.addFormDataPart("imgfile" + i, file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        url.uploadImgs(parts).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UploadUrlBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(UploadUrlBean res) {
                        loadingDailog.dismiss();
                        if (res.getErrno() != 0) {
                            ToastUtils.getInstanc(mContext).showToast("文件上传失败");
                            return;
                        }
                        List<ImImgBean> uploadList = res.getData();
                        if (uploadList == null || uploadList.size() < 1) {
                            return;
                        }
                        watch_img = AppUtils.getMultiStr(uploadList);

                        if (enterType == 0) {
                            commitFinal();
                        } else {
                            editFinal();
                        }


                    }
                });
    }


}




