package com.xinyu.newdiggtest.bigq;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.DeletTodoBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 编辑交办
 */
public class AddDoneActivity extends BaseNoEventActivity {


    String enterType = "0";


    @BindView(R.id.edt_content)
    public EditText edtitext;

    @BindView(R.id.title)
    public TextView title;


    String fid = "";

    @Override
    protected int getLayoutResouce() {
        return R.layout.todo_done;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

    }

    String oldContent;

    private void initView() {

        enterType = getIntent().getStringExtra("enter_type");//0 新建 1编辑

        fid = getIntent().getStringExtra("todoId");


        if (enterType.equals("1")) {
            title.setText("修改回复结果");
            oldContent = getIntent().getStringExtra("oldContent");
            edtitext.setText(oldContent);

        } else {
            title.setText("新增回复");

        }


    }

    @OnClick(R.id.btn_commint)
    public void goCreate() {

        if (MyTextUtil.isEmpty(edtitext.getText().toString())) {
            ToastUtils.getInstanc().showToast("请输入回复内容");
            return;
        }

        if (enterType.equals("1")) {
            uploadNote();
        } else {
            addNote();
        }

    }


    @OnClick(R.id.iv_back)
    public void goback() {
        finish();
    }


    public void addNote() {

        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("f_todo_id", fid);

        map.put("f_title", edtitext.getText().toString());


        url.addTodoNote(map).subscribeOn(Schedulers.io())
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
                            ToastUtils.getInstanc().showToast("新建回复成功！");
                            finish();
                        }


                    }
                });
    }


    public void uploadNote() {

        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("f_id", fid);

        map.put("f_title", edtitext.getText().toString());


        url.upLoadTodoNote(map).subscribeOn(Schedulers.io())
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
                            ToastUtils.getInstanc().showToast("编辑回复成功！");
                            finish();
                        }


                    }
                });
    }


}
