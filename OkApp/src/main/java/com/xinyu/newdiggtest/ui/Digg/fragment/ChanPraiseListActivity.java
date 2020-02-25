package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.widget.EditText;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.CommentAdapter;
import com.xinyu.newdiggtest.bean.CommentReturnBean;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.BaseBean;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;


import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 频道评论列表
 */
public class ChanPraiseListActivity extends BaseNoEventActivity {


    @BindView(R.id.recyclerView)
    public RecyclerView recyView;


    @BindView(R.id.input)
    public EditText input;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_praise_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initRecycle();

        if (getIntent().hasExtra("ifFile")) {
            getFilePraiselist();
        } else {
            getPraiselist(0);
        }

    }

    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    private void getPraiselist(final int tag) {
        if (tag != 1)
            loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        requsMap.put("f_plugin_type_id", getIntent().getStringExtra("chanel_plugin_type_id"));
        requsMap.put("f_plugin_id", getIntent().getStringExtra("contentId"));


        requsMap.put("size", "500");
        requsMap.put("current", "1");

        url.queryCommentList(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommentReturnBean>() {
                    @Override
                    public void onCompleted() {
                        if (tag != 1)
                            loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(CommentReturnBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() != null && msg.getData().size() > 0) {

                                showUI(msg.getData());

                            }
                        }
                    }
                });


    }


    /**
     * 文件类型
     */

    private void getFilePraiselist() {

        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        requsMap.put("ok_type", "FILE_GROUP");
        requsMap.put("f_object_id", getIntent().getStringExtra("f_object_id"));

        requsMap.put("size", "500");
        requsMap.put("current", "1");

        url.getFileComment(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommentReturnBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(CommentReturnBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() != null && msg.getData().size() > 0) {
                                showUI(msg.getData());
                            }
                        }
                    }
                });


    }


    /**
     * 显示数据
     *
     * @param data
     */
    private void showUI(List<CommentReturnBean.CommentBean> data) {
        CommentAdapter adapter = new CommentAdapter(R.layout.item_commt, data);
        recyView.setAdapter(adapter);
    }


    @OnClick(R.id.insert_comment)
    public void insetCms() {

        if (MyTextUtil.isEmpty(input.getText().toString())) {
            ToastUtils.getInstanc().showToast("请输入回复内容!");
            return;
        }
        if (getIntent().hasExtra("ifFile")) {
            insertFilecommets();
        } else {
            doInsertCms();
        }

    }


    private void doInsertCms() {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("f_plugin_type_id", getIntent().getStringExtra("chanel_plugin_type_id"));
        requsMap.put("f_plugin_id", getIntent().getStringExtra("contentId"));

        requsMap.put("f_comment", input.getText().toString());

//        requsMap.put("f_title", getIntent().getStringExtra("title"));
        requsMap.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());
        requsMap.put("f_from_create", PreferenceUtil.getInstance(mContext).getUserId());


        url.insetComments(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            input.setText("");

                            getPraiselist(1);

                            //TODO 可以通知刷新
                            EventBus.getDefault().postSticky(new XshellEvent(EventConts.BD_Comment_Refresh));

                        }
                    }
                });


    }

    /**
     * 插入评论
     */

    private void insertFilecommets() {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("ok_type", "FILE_GROUP");
        requsMap.put("f_comment", MyTextUtil.getUrl1Encoe(input.getText().toString()));
        requsMap.put("f_object_id", getIntent().getStringExtra("f_object_id"));


        url.insetFileComments(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            input.setText("");

                            getFilePraiselist();

                            EventBus.getDefault().postSticky(new XshellEvent(EventConts.BD_Comment_Refresh));

                        }
                    }
                });


    }

}




