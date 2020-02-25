package com.xinyu.newdiggtest.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.FileLoadBean;
import com.xinyu.newdiggtest.bigq.FileAdapter;
import com.xinyu.newdiggtest.bigq.FileBean;
import com.xinyu.newdiggtest.bigq.FileChooseUtil;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.BaseBean;
import com.xinyu.newdiggtest.utils.LogUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
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
 * 意见审批
 */


public class CheckActivity extends BaseNoEventActivity {


    @BindView(R.id.edt_content)
    EditText edt_content;

    @BindView(R.id.file_count)
    TextView file_count;

    @BindView(R.id.hour_tx)
    TextView hour_tx;


    @BindView(R.id.file_tag)
    LinearLayout file_tag;


    @BindView(R.id.docoment)
    RecyclerView docoment;

    FileAdapter adapter;

    ExecutorService excutor = Executors.newCachedThreadPool();

    JSONArray fieDocment;

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_check;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {

        if (getIntent().getStringExtra("f_file_upload").equals("0")) {
            file_tag.setVisibility(View.VISIBLE);
        } else {
            file_tag.setVisibility(View.GONE);
        }
        GridLayoutManager mgr = new GridLayoutManager(mContext, 2);
        docoment.setLayoutManager(mgr);

        List<FileBean> dataslist = new ArrayList<>();
        adapter = new FileAdapter(R.layout.item_file, dataslist);
        adapter.setType(1);
        docoment.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (view.getId() == R.id.delect_img) {
                    adapter.getData().remove(position);
                    adapter.notifyDataSetChanged();

                    checkNum();
                }
            }
        });


        if (MyTextUtil.isEmpty(getIntent().getStringExtra("title"))) {
            edt_content.setHint("请输入审批意见");
        } else {
            edt_content.setHint(getIntent().getStringExtra("title"));
        }

        if (getIntent().hasExtra("duration")) {
            hour_tx.setText(getIntent().getStringExtra("duration"));
        }


    }

    private void checkNum() {
        int len = adapter.getData().size();
        file_count.setText(len + "");
    }


    @OnClick(R.id.iv_back)
    public void goCommit() {
        finish();
    }


    final int CHOOSE_FILE_CODE = 1;

    @OnClick(R.id.icon)
    public void loadImg() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*").addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, CHOOSE_FILE_CODE);

    }

    @OnClick(R.id.btn_commint)
    public void commit() {

//        if (MyTextUtil.isEmpty(edt_content.getText().toString().trim())) {
//            ToastUtils.getInstanc().showToast("请输入审批意见");
//            return;
//        }

        if (adapter == null || adapter.getData().size() < 1) {
            commitParams();

        } else if (adapter.getData().size() > 0) {

            fieDocment = new JSONArray();

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case CHOOSE_FILE_CODE:
                    Uri uri = data.getData();
                    String chooseFilePath = FileChooseUtil.getInstance(this).getChooseFileResultPath(uri);
                    insertList(chooseFilePath);

                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

            adapter.getData().add(bean);
            adapter.notifyDataSetChanged();

            checkNum();

        }


    }


    private void goUpLoad(final FileBean fileBean) {

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM).addFormDataPart("TOKEN", PreferenceUtil.getInstance(this).getSessonId());
//                .addFormDataPart(TOKEN, token);自定义参数key常量类，即参数名

        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileBean.getPath());

        builder.addFormDataPart("imgfile", fileBean.getFname(), imageBody);//"imgfile"+i 后台接收图片流的参数名

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
            object.put("f_title", res.getFname());
            object.put("f_path", url);
            object.put("f_type", res.getDex());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }


    /**
     * 提交 审核
     */
    public void commitParams() {
        loadingDailog.show();

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());


        map.put("f_type", getIntent().getStringExtra("f_type"));//最外层
        map.put("f_finish_id", getIntent().getStringExtra("f_finish_id"));//最外层

        map.put("f_procedure_id", getIntent().getStringExtra("f_procedure_id"));//最后 一条
        map.put("f_todo_id", getIntent().getStringExtra("f_todo_id"));//最后一条的todoid
        map.put("f_process_id", getIntent().getStringExtra("process_id"));//最后一条
        map.put("f_id", getIntent().getStringExtra("f_id"));//最后一条

        if (getIntent().getStringExtra("state").equals("1")) {
            map.put("f_state", "2");//同意
        } else if (getIntent().getStringExtra("state").equals("0")) {
            map.put("f_state", "1");//拒绝
        }
        //TODO 同意还是驳回

        if (adapter.getData().size() > 0) {
            map.put("f_file", fieDocment.toString());
        }

        map.put("f_comment", edt_content.getText().toString().trim());
        map.put("f_title", getIntent().getStringExtra("f_title"));

        map.put("f_plugin_id", getIntent().getStringExtra("f_plugin_id"));//第一条
        map.put("f_form_id", getIntent().getStringExtra("f_form_id"));//第一条


        url.checkProcess(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {
                            EventBus.getDefault().post(new XshellEvent(0x97));//TODO 关闭上一个页面

                            finish();
                        }
                    }
                });
    }


}
