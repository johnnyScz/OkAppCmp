package com.xinyu.newdiggtest.bigq;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.TodoMemberDeletAdapter;
import com.xinyu.newdiggtest.adapter.viewhelper.TotalMemberActivity;
import com.xinyu.newdiggtest.bean.DeletTodoBean;
import com.xinyu.newdiggtest.bean.FileLoadBean;
import com.xinyu.newdiggtest.bean.MemberBean;
import com.xinyu.newdiggtest.bean.MemberRetBean;
import com.xinyu.newdiggtest.bean.RetListBean;
import com.xinyu.newdiggtest.bean.TodoRetBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.CommBean;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.LogUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 待办编辑页面
 */
public class ToDoEditActivity extends BaseNoEventActivity {

    @BindView(R.id.add_file)
    public View addFile;


    @BindView(R.id.filelist)
    public RecyclerView recyclerView;


    @BindView(R.id.todo_recycle)
    public RecyclerView todoRecyclerView;


    @BindView(R.id.begin_date)
    public TextView begin_date;


    @BindView(R.id.end_date)
    public TextView end_date;

    @BindView(R.id.edt_content)
    public EditText inputStr;


    TodoMemberDeletAdapter todoAdapter;

    TodoMemberDeletAdapter chaoSongAdapter;


    @BindView(R.id.chaosong_recycle)
    public RecyclerView chaoSongRecyclerView;


    List<FileBean> dataslist;

    List<FileBean> needUpload;

    FileAdapter adapter;

    ExecutorService excutor = Executors.newCachedThreadPool();
    DatePickerDialog dialog;

    int currentYear, month, day;
    String curentTime;


    String todoId = "";


    //
    List<MemberRetBean.MemberOutBean> chaosong = new ArrayList<>();//抄送

    @Override
    protected int getLayoutResouce() {
        return R.layout.todo_edit_layout;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initHrizonCycle();
        intiRecycle();

        queryCurentInfo();
    }

    private void initView() {

        todoId = getIntent().getStringExtra("todoId");

        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        curentTime = DateUtil.getCurrentDataTime();

    }


    public void queryCurentInfo() {
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("f_id", todoId);
        url.getDodoInById(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TodoRetBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(TodoRetBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() != null && msg.getData().size() > 0) {
                                handleData(msg.getData());
                            }
                        }
                    }
                });
    }


    private void handleData(List<RetListBean> data) {

        RetListBean mdata = data.get(0);

        String time = MyTextUtil.isEmpty(mdata.getF_todo_date()) ? mdata.getF_create_date() : mdata.getF_todo_date();
        begin_date.setText(time);
        inputStr.setText(mdata.getF_title());


        String dateStr = MyTextUtil.isEmpty(mdata.getF_finish_date()) ? mdata.getF_end_date() : mdata.getF_finish_date();

        if (!MyTextUtil.isEmpty(dateStr) && dateStr.length() > 10) {
            end_date.setText(dateStr.substring(0, 10));
        }

        if (mdata.getInvites() != null && mdata.getInvites().size() > 0) {
            List<RetListBean.InvitesBean> invits = mdata.getInvites();

            List<MemberRetBean.MemberOutBean> redata = convertData(invits);

            todoAdapter.getData().addAll(redata);
            todoAdapter.notifyDataSetChanged();
        }

        if (mdata.getCcs() != null && mdata.getCcs().size() > 0) {
            List<RetListBean.InvitesBean> chongs = mdata.getCcs();
            chaoSongAdapter.getData().addAll(convertData(chongs));
            chaoSongAdapter.notifyDataSetChanged();

        }


        if (mdata.getAttachment() != null && mdata.getAttachment().size() > 0) {

            List<RetListBean.DocumentBean> datas = mdata.getAttachment();
            adapter.getData().addAll(convertFile(datas));
            adapter.notifyDataSetChanged();
        }


    }

    private List<FileBean> convertFile(List<RetListBean.DocumentBean> datas) {

        List<FileBean> fileList = new ArrayList<>();

        for (RetListBean.DocumentBean tt : datas) {
            FileBean flile = new FileBean();
            flile.setFname(tt.getF_title());

            String dcc = tt.getF_type();
            flile.setDex(dcc);

            if (dcc.equals("png") || dcc.equals("jpg") || dcc.equals("jpeg")) {
                flile.setType(1);
            } else if (dcc.equals("pdf") || dcc.equals("pptx") || dcc.equals("ppt") || dcc.equals("txt") || dcc.equals("doc") || dcc.equals("docx") || dcc.equals("xls") || dcc.equals("xlsx")) {
                flile.setType(2);
            } else {
                flile.setType(3);
            }
            flile.setFileUrl(tt.getF_path());
            fileList.add(flile);
        }


        return fileList;
    }

    private List<MemberRetBean.MemberOutBean> convertData(List<RetListBean.InvitesBean> origData) {

        List<MemberRetBean.MemberOutBean> datas = new ArrayList<>();

        for (RetListBean.InvitesBean tt : origData) {

            MemberRetBean.MemberOutBean bean = new MemberRetBean.MemberOutBean();

            MemberBean child = new MemberBean();
            child.setHead(tt.getOwnermap().getHead());
            child.setUser_id(tt.getOwnermap().getUser_id());
            child.setNickname(tt.getOwnermap().getNickname());
            child.setName(tt.getOwnermap().getName());
            bean.setUserinfo(child);
            datas.add(bean);
        }


        return datas;
    }


    List<MemberRetBean.MemberOutBean> originData = new ArrayList<>();


    private void initHrizonCycle() {
        LinearLayoutManager hoziLayountManager = new LinearLayoutManager(mContext);
        hoziLayountManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        todoRecyclerView.setLayoutManager(hoziLayountManager);//给RecyclerView设置适配器

        todoAdapter = new TodoMemberDeletAdapter(R.layout.item_delet_member, originData);
        todoRecyclerView.setAdapter(todoAdapter);

        todoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                notifyTodo(position);
            }
        });


        LinearLayoutManager hoziLayountManager11 = new LinearLayoutManager(mContext);
        hoziLayountManager11.setOrientation(LinearLayoutManager.HORIZONTAL);
        chaoSongRecyclerView.setLayoutManager(hoziLayountManager11);//给RecyclerView设置适配器

        chaoSongAdapter = new TodoMemberDeletAdapter(R.layout.item_delet_member, chaosong);
        chaoSongRecyclerView.setAdapter(chaoSongAdapter);

        chaoSongAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                notifyChaoSong(position);
            }
        });


    }


    private void intiRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器

        dataslist = new ArrayList<>();
        adapter = new FileAdapter(R.layout.item_file, dataslist);
        adapter.setType(1);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (view.getId() == R.id.delect_img) {
                    adapter.getData().remove(position);
                    adapter.notifyDataSetChanged();

                }
            }
        });

    }


    @OnClick(R.id.add_file)
    public void addFile() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*").addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, CHOOSE_FILE_CODE);
    }

    final int CHOOSE_FILE_CODE = 1;

    @Override
// 文件选择完之后，自动调用此函数
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case CHOOSE_FILE_CODE:
                    Uri uri = data.getData();
                    String chooseFilePath = FileChooseUtil.getInstance(this).getChooseFileResultPath(uri);
                    insertList(chooseFilePath);


                    break;

                case 0x11:

                    List<MemberRetBean.MemberOutBean> at = new ArrayList<>();

                    for (MemberRetBean.MemberOutBean tt : AppContacts.SelectData) {
                        at.add(tt);
                    }
                    todoAdapter.setNewData(at);

                    break;

                case 0x21:

                    if (AppContacts.SelectData.size() < 1)
                        return;

                    List<MemberRetBean.MemberOutBean> Cc = new ArrayList<>();

                    for (MemberRetBean.MemberOutBean tt : AppContacts.SelectData) {
                        Cc.add(tt);
                    }

                    checkIfhasAt(Cc);


                    chaoSongAdapter.setNewData(Cc);

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 是否已经At 了，不能再抄送了
     *
     * @param cc
     */
    private void checkIfhasAt(List<MemberRetBean.MemberOutBean> cc) {

        List<MemberRetBean.MemberOutBean> join = todoAdapter.getData();

        for (MemberRetBean.MemberOutBean item : cc) {

            for (MemberRetBean.MemberOutBean cheItem : join) {

                if (cheItem.getUserinfo().getUser_id().equals(item.getUserinfo().getUser_id())) {
                    cc.remove(item);
                    ToastUtils.getInstanc().showToast(item.getUserinfo().getNickname() + "已经被@,不用重复抄送");
                    break;
                }
            }
        }
    }


    private void notifyTodo(int position) {
        todoAdapter.getData().remove(position);
        todoAdapter.notifyDataSetChanged();
    }

    private void notifyChaoSong(int position) {
        chaoSongAdapter.getData().remove(position);
        chaoSongAdapter.notifyDataSetChanged();
    }


    /**
     * 插入到列表中
     *
     * @param chooseFilePath
     */
    private void insertList(String chooseFilePath) {

        File file = new File(chooseFilePath);
        if (file.exists()) {

            String fileName = file.getName();

            FileBean bean = new FileBean();
            bean.setFile(file);
            bean.setFname(fileName);


            int pix = fileName.lastIndexOf(".");
            String dcc = fileName.substring(pix + 1, fileName.length()).toLowerCase();
            String nack = fileName.substring(0, pix);
            bean.setFname(nack);
            bean.setDex(dcc);


            if (dcc.equals("png") || dcc.equals("jpg") || dcc.equals("jpeg")) {
                bean.setType(1);
            } else if (dcc.equals("pdf") || dcc.equals("pptx") || dcc.equals("ppt") || dcc.equals("txt") || dcc.equals("doc") || dcc.equals("docx") || dcc.equals("xls") || dcc.equals("xlsx")) {
                bean.setType(2);
            } else {
                bean.setType(3);
            }

            goRecycle(bean);

        }


    }

    private void goRecycle(FileBean bean) {
        adapter.addData(bean);
    }


    @OnClick(R.id.iv_back)
    public void goBack() {
        finish();
    }


    @OnClick(R.id.add_todo)
    public void joinTodo() {

        Intent mintent = new Intent(mContext, TotalMemberActivity.class);

        mintent.putExtra("aready", getStrId(1));//传当前已经选好的UerId

        startActivityForResult(mintent, 0x11);

    }

    @OnClick(R.id.add_chaosong)
    public void joinChaosong() {

        Intent intent = new Intent(mContext, TotalMemberActivity.class);
        intent.putExtra("aready", getStrId(2));

        startActivityForResult(intent, 0x21);
    }


    private String getStrId(int tag) {

        AppContacts.intentData.clear();

        String id = "";
        List<MemberRetBean.MemberOutBean> datas = null;
        if (tag == 1) {
            datas = todoAdapter.getData();
        } else if (tag == 2) {
            datas = chaoSongAdapter.getData();
        }

        AppContacts.intentData.addAll(datas);

        if (datas != null && datas.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (MemberRetBean.MemberOutBean item : datas) {
                buffer.append(item.getUserinfo().getUser_id()).append(":");
            }
            String temp = buffer.toString();
            return temp.substring(0, temp.length() - 1);
        }

        return id;
    }


    @OnClick(R.id.begin_time)
    public void beginDate() {
        dateType = 0;
        if (dialog == null) {
            dialog = new DatePickerDialog(mContext,
                    0, listener, currentYear, month, day);
            //后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        }
        dialog.show();
    }


    @OnClick(R.id.end_time)
    public void endDate() {
        dateType = 1;
        if (dialog == null) {
            dialog = new DatePickerDialog(mContext,
                    0, listener, currentYear, month, day);
            //后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        }
        dialog.show();
    }


    int dateType = 0;//0 开始 1结束


    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {

            String daytr = "";

            if (day < 10) {
                daytr = "0" + day;
            } else {
                daytr = "" + day;
            }

            String selecTime = "";
            if (month < 9) {
                selecTime = year + "-0" + (month + 1) + "-" + daytr;
            } else {
                selecTime = year + "-" + (month + 1) + "-" + daytr;
            }

//            String btime = DateUtil.getCurrentData();
//            if (DateUtil.end2beginTime(btime, selecTime)) {
//                ToastUtils.getInstanc().showToast("开始时间不能早于今天");
//                return;
//            }

            if (dateType == 0) {
                if (!end_date.getText().toString().equals("选择日期")) {
                    String begDate = end_date.getText().toString();
                    if (DateUtil.end2beginTime(selecTime, begDate)) {
                        ToastUtils.getInstanc().showToast("开始时间不能晚于结束时间");
                        return;
                    }
                }
                begin_date.setText(selecTime);
            } else {

                String begDate = begin_date.getText().toString();
                if (DateUtil.end2beginTime(begDate, selecTime)) {
                    ToastUtils.getInstanc().showToast("截止时间不能早于开始日期");
                    return;
                }

                end_date.setText(selecTime);
            }

        }

    };


    JSONArray fieDocment;


    private void goUpLoad(final FileBean fileBean) {

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM).addFormDataPart("TOKEN", PreferenceUtil.getInstance(this).getSessonId());
//                .addFormDataPart(TOKEN, token);自定义参数key常量类，即参数名

//        File newFile = CompressHelper.getDefault(App.mContext).compressToFile(fileBean.mFile);//TODO 不知文件类型，直接上传

        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileBean.getPath());

        builder.addFormDataPart("imgfile", fileBean.mFile.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名

        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        url.uploadFile(parts).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FileLoadBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        LogUtil.e("上传异常:" + e.getMessage());
                    }

                    @Override
                    public void onNext(FileLoadBean res) {

                        if (res.getErrno().equals("0")) {

                            JSONObject object = creatJson(fileBean, res.getData().get(0));
                            fieDocment.put(object);
                        }
//
                        if (fieDocment.length() == needUpload.size()) {
                            commitUpdate();
                        }
                    }
                });
    }

    private JSONObject creatJson(FileBean res, String url) {

        JSONObject object = new JSONObject();
        try {
            object.put("f_title", res.getFname());
            object.put("f_path", url);
            object.put("f_type", res.getDex());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    private String getStrType(String fileName) {
        int pix = fileName.lastIndexOf(".");
        String dcc = fileName.substring(pix + 1, fileName.length()).toLowerCase();
        return dcc;
    }


    private String getFujianParams() {

        if (fieDocment == null || fieDocment.length() < 1) {

            if (adapter.getData().size() < 1) {
                JSONArray obj = new JSONArray();
                return obj.toString();
            } else {
                List<FileBean> orignalData = adapter.getData();
                JSONArray array = new JSONArray();

                for (FileBean tt : orignalData) {

                    JSONObject object = creatJson(tt, tt.getFileUrl());
                    array.put(object);
                }
                return array.toString();
            }


        } else {

            List<FileBean> orignalData = adapter.getData();

            for (FileBean tt : orignalData) {
                if (!MyTextUtil.isEmpty(tt.getFileUrl())) {
                    JSONObject object = creatJson(tt, tt.getFileUrl());
                    fieDocment.put(object);
                }
            }
            return fieDocment.toString();
        }
    }

    private String getChaosongParams() {

        List<MemberRetBean.MemberOutBean> chaosong = chaoSongAdapter.getData();

        if (chaosong == null || chaosong.size() == 0) {
            return "";
        } else if (chaosong.size() == 1) {
            return chaosong.get(0).getUserinfo().getUser_id();
        } else {

            StringBuffer buffer = new StringBuffer();

            for (MemberRetBean.MemberOutBean itme : chaosong) {
                buffer.append(itme.getUserinfo().getUser_id()).append(",");
            }

            return buffer.toString();
        }


    }

    private String getTodoParams() {
        List<MemberRetBean.MemberOutBean> todo = todoAdapter.getData();

        if (todo == null || todo.size() == 0) {
            return "";
        } else if (todo.size() == 1) {
            return todo.get(0).getUserinfo().getUser_id();
        } else {
            StringBuffer buffer = new StringBuffer();

            for (MemberRetBean.MemberOutBean itme : todo) {
                buffer.append(itme.getUserinfo().getUser_id()).append(",");
            }
            return buffer.toString();
        }

    }

    private String getOrgId() {
        return "";
    }


    @OnClick(R.id.btn_commint)
    public void goCommit() {


        if (todoAdapter.getData().size() < 1) {
            ToastUtils.getInstanc().showToast("请选择待办负责人");
            return;
        }

        if (adapter.getData().size() > 0) {

            List<FileBean> dataslist = adapter.getData();
            needUpload = filter(dataslist);
            if (needUpload.size() < 1) {
                commitUpdate();
            } else {

                fieDocment = new JSONArray();

                excutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        for (FileBean item : needUpload) {
                            goUpLoad(item);
                        }
                    }
                });
            }
        } else {
            commitUpdate();
        }


    }

    private List<FileBean> filter(List<FileBean> dataslist) {

        List<FileBean> needload = new ArrayList<>();
        for (FileBean data : dataslist) {
            if (MyTextUtil.isEmpty(data.getFileUrl())) {
                needload.add(data);
            }
        }
        return needload;
    }


    public void commitUpdate() {

        if (MyTextUtil.isEmpty(inputStr.getText().toString())) {
            ToastUtils.getInstanc().showToast("请输入待办内容");
            return;
        }

        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("f_id", todoId);
        map.put("f_source_id", "");
        map.put("f_title", inputStr.getText().toString());
        map.put("org_id", "");

        map.put("f_start_date", begin_date.getText().toString().trim() + " " + DateUtil.getCurrentHms());


        if (!end_date.getText().toString().equals("选择日期")) {
            map.put("f_end_date", end_date.getText().toString().trim() + " " + DateUtil.getCurrentHms());
        }


        map.put("f_cc", getChaosongParams());
        map.put("f_assign", getTodoParams());
        map.put("attachments", getFujianParams());
        map.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());


        url.upDateTodo(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(CommBean msg) {
                        loadingDailog.dismiss();

                        if (msg.getOp().getCode().equals("Y")) {
                            ToastUtils.getInstanc().showToast("待办编辑成功！");
                            finish();
                        } else {
                            ToastUtils.getInstanc().showToast("待办编辑失败，请稍后再试!");
                        }


                    }
                });
    }


    @OnClick(R.id.delet)
    public void goDelet() {


        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("温馨提示")
                .setMessage("你确定要删除这条待办吗？")
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
                        deleteTodo();
                    }
                })
                .show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.button_vip));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }


    public void deleteTodo() {

        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("f_id", todoId);
        map.put("org_id", "");
        map.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());
        map.put("deletechild", "false");

        url.deletTodo(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DeletTodoBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(DeletTodoBean msg) {
                        loadingDailog.dismiss();

                        if (msg.getOp().getCode().equals("Y")) {
                            ToastUtils.getInstanc().showToast("删除成功!");
                            finish();

                        } else {
                            ToastUtils.getInstanc().showToast("删除失败");
                        }

                    }
                });
    }


}
