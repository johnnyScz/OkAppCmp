package com.xinyu.newdiggtest.bigq;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.TodoMemberDeletAdapter;
import com.xinyu.newdiggtest.adapter.viewhelper.TotalMemberActivity;
import com.xinyu.newdiggtest.bean.FileLoadBean;
import com.xinyu.newdiggtest.bean.InsertTodoBean;
import com.xinyu.newdiggtest.bean.MemberBean;
import com.xinyu.newdiggtest.bean.MemberRetBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
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
 * 待办页面
 */
public class ToDoActivity extends BaseNoEventActivity {

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


    ExecutorService excutor = Executors.newCachedThreadPool();


    @BindView(R.id.chaosong_recycle)
    public RecyclerView chaoSongRecyclerView;


    List<FileBean> dataslist;

    FileAdapter adapter;


    DatePickerDialog dialog;

    int currentYear, month, day;

    //
    List<MemberRetBean.MemberOutBean> chaosong = new ArrayList<>();//抄送

    @Override
    protected int getLayoutResouce() {
        return R.layout.todo_layout;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initHrizonCycle();
        intiRecycle();
    }

    private void initView() {
        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        begin_date.setText(getIntent().getStringExtra("date"));


    }

    List<MemberRetBean.MemberOutBean> originData = new ArrayList<>();


    private void initHrizonCycle() {
        LinearLayoutManager hoziLayountManager = new LinearLayoutManager(mContext);
        hoziLayountManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        todoRecyclerView.setLayoutManager(hoziLayountManager);//给RecyclerView设置适配器
        initOriginal();


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

    private void initOriginal() {

        MemberRetBean.MemberOutBean mySelf = new MemberRetBean.MemberOutBean();

        MemberBean bean = new MemberBean();
        bean.setHead(PreferenceUtil.getInstance(mContext).getHeadUrl());
        bean.setNickname(PreferenceUtil.getInstance(mContext).getNickName());
        bean.setUser_id(PreferenceUtil.getInstance(mContext).getUserId());
        mySelf.setUserinfo(bean);
        originData.add(mySelf);

        //TODO 如果安排别人
        if (getIntent().hasExtra("signer")) {

            MemberRetBean.MemberOutBean fromIntent = new MemberRetBean.MemberOutBean();
            MemberBean bb = new MemberBean();
            bb.setHead(getIntent().getStringExtra("head"));
            bb.setNickname(getIntent().getStringExtra("name"));
            bb.setUser_id(getIntent().getStringExtra("signer"));
            fromIntent.setUserinfo(bb);
            originData.add(fromIntent);


        }


        todoAdapter = new TodoMemberDeletAdapter(R.layout.item_delet_member, originData);
        todoRecyclerView.setAdapter(todoAdapter);

        todoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                notifyTodo(position);
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


            int pix = fileName.lastIndexOf(".");
            String dcc = fileName.substring(pix + 1, fileName.length()).toLowerCase();
            String nack = fileName.substring(0, pix);
//            bean.setFileType(dcc);
            bean.setNacketName(nack);

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

        Intent intent = new Intent(mContext, TotalMemberActivity.class);
        intent.putExtra("aready", getStrId(1));
        startActivityForResult(intent, 0x11);


    }

    @OnClick(R.id.add_chaosong)
    public void joinChaosong() {


        Intent intent = new Intent(mContext, TotalMemberActivity.class);
        intent.putExtra("aready", getStrId(2));
        startActivityForResult(intent, 0x21);

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
                        if (fieDocment.length() == adapter.getData().size()) {
                            commitParams();

                        }
                    }
                });
    }

    private JSONObject creatJson(FileBean res, String url) {

        JSONObject object = new JSONObject();
        try {
            object.put("f_title", res.getNacketName());
            object.put("f_path", url);
            object.put("f_type", res.getDex());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 新建待办提交
     */

    private void commitParams() {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("f_source_id", "0");
        map.put("f_title", inputStr.getText().toString());
        map.put("org_id", getOrgId());
        map.put("f_start_date", begin_date.getText().toString() + " " + DateUtil.getCurrentHms());


        if (!end_date.getText().toString().equals("选择日期")) {
            map.put("f_end_date", end_date.getText().toString() + " " + DateUtil.getCurrentHms());
        }

        map.put("f_company_id", getCompanyId());
        map.put("attachments", getFujianParams());
        map.put("f_cc", getChaosongParams());
        map.put("f_assign", getTodoParams());


        url.commitInsertTodo(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InsertTodoBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(InsertTodoBean msg) {
                        loadingDailog.dismiss();

                        ToastUtils.getInstanc().showToast("新建待办成功!");
                        finish();
                    }
                });
    }

    private String getFujianParams() {

        if (fieDocment == null || fieDocment.length() < 1) {

            JSONArray obj = new JSONArray();
            return obj.toString();

        } else {
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

    private String getCompanyId() {

        return MyTextUtil.isEmpty(PreferenceUtil.getInstance(mContext).getCompanyId()) ? "1" : PreferenceUtil.getInstance(mContext).getCompanyId();

    }


    @OnClick(R.id.txt_finish)
    public void goUpload() {

        if (MyTextUtil.isEmpty(inputStr.getText().toString())) {
            ToastUtils.getInstanc().showToast("请输入待办内容");
            return;
        }

        if (todoAdapter.getData().size() < 1) {
            ToastUtils.getInstanc().showToast("请选择待办负责人");
            return;
        }


        fieDocment = new JSONArray();

        if (adapter == null || adapter.getData().size() < 1) {
            commitParams();

        } else if (adapter.getData().size() > 0) {

            final List<FileBean> fileList = adapter.getData();
            excutor.execute(new Runnable() {
                @Override
                public void run() {
                    for (FileBean item : fileList) {
                        goUpLoad(item);
                    }
                }
            });


        }
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


}
