package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.FileComAdapter;

import com.xinyu.newdiggtest.bean.FileChildRetBean;

import com.xinyu.newdiggtest.net.ApiConfig;

import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.BaseBean;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.MyMiddleDialog;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 表单的文件类型
 */
public class FileActivity extends BaseNoEventActivity {

    FileComAdapter adapter;

    @BindView(R.id.recyclerView)
    public RecyclerView recyView;


    @BindView(R.id.emputview)
    public View emptyView;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_bd_file;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecycle();
    }

    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
    }


    @Override
    protected void onStart() {
        super.onStart();
        quetyList();
    }


    public void quetyList() {

        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("f_group_id", getIntent().getStringExtra("group_id"));
        map.put("size", "500");
        map.put("current", "1");


        url.queryFileComment(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FileChildRetBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(FileChildRetBean msg) {
                        loadingDailog.dismiss();

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() != null && msg.getData().size() > 0) {

                                showUI(msg.getData());

                            } else {
                                showEmpty();
                            }


                        }
                    }
                });
    }


    /**
     * 显示空
     */
    private void showEmpty() {
        recyView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    private void showUI(List<FileChildRetBean.FileChildBean> data) {

        showData(data);
    }


    /**
     * 显示数据
     *
     * @param datalist
     */
    private void showData(final List<FileChildRetBean.FileChildBean> datalist) {
        adapter = new FileComAdapter(R.layout.item_file_com, datalist);
        recyView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter1, View view, int position) {

                FileChildRetBean.FileChildBean fdate = adapter.getData().get(position);

                String objId = fdate.getF_id();

                Intent intent = new Intent(mContext, FileWebViewActivity.class);
                intent.putExtra("f_object_id", objId);

                String object = createData(fdate.getF_id());
                String url = ApiConfig.BDUrl + "from=android" + "&data=" + object;
                intent.putExtra("url", url);

                startActivity(intent);

            }
        });
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @OnClick(R.id.add)
    public void add() {
        showMyDialog();
    }


    MyMiddleDialog myMiddleDialog;

    EditText edt_input;

    private void showMyDialog() {
        if (myMiddleDialog == null) {
            myMiddleDialog = new MyMiddleDialog(this, R.style.MyMiddleDialogStyle) {
                @Override
                protected View getView() {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    View view = inflater.inflate(R.layout.dialog_file, null);
                    initDialogView(view);
                    return view;
                }
            };
        }
        myMiddleDialog.show();
    }

    private void initDialogView(View view) {

        edt_input = view.findViewById(R.id.edt_input);

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMiddleDialog.dismiss();
            }
        });

        view.findViewById(R.id.conform).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MyTextUtil.isEmpty(edt_input.getText().toString())) {
                    ToastUtils.getInstanc().showToast("文件表单不能为空!");
                    return;
                }
                myMiddleDialog.dismiss();
                creatFolder();

            }
        });


    }


    public void creatFolder() {

        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("f_group_id", getIntent().getStringExtra("group_id"));
        map.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());
        map.put("f_title", MyTextUtil.getUrl1Encoe(edt_input.getText().toString()));

        url.creatFolder(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean msg) {
                        loadingDailog.dismiss();
                        edt_input.setText("");
                        if (msg.getOp().getCode().equals("Y")) {
                            quetyList();
                        }
                    }
                });
    }


    /**
     * @param fid
     * @return
     */
    private String createData(String fid) {

        StringBuffer buffer = new StringBuffer();

        buffer.append("%7B%22").append("sid").append("%22%3A%22").
                append(PreferenceUtil.getInstance(mContext).getSessonId()).append("%22").append(",")
                .append("%22id%22%3A%22").append(fid)
                .append("%22,%22uid%22%3A%22").append(PreferenceUtil.getInstance(mContext).getUserId()).append("%22, %22formName%22%3A%22")
                .append("file")
                .append("%22%7D");

        return buffer.toString();
    }


}




