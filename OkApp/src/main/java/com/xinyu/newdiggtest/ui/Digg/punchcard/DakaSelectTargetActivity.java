package com.xinyu.newdiggtest.ui.Digg.punchcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.MyDakaSelectAdapter;
import com.xinyu.newdiggtest.bean.DakaSelectBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseActivity;
import com.xinyu.newdiggtest.ui.CreatDakaActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DakaSelectTargetActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;


    @BindView(R.id.emputview)
    public ImageView emptyView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecycle();
        if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("HomeFragment")) {
            AppContacts.SELECT_Tag = "";
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestdatas();
    }


    private void requestdatas() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        url.getTargetList(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DakaSelectBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(DakaSelectBean msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData() == null || msg.getData().size() == 0) {
                                emptyView.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                return;
                            } else {
                                emptyView.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                //TODO 设置数据
                                setDatas(msg.getData());
                            }

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });
    }


    /**
     * TODO
     * <p>
     * 设置数据
     *
     * @param data
     */
    private void setDatas(final List<DakaSelectBean.ShowBean> data) {

        MyDakaSelectAdapter adapter = new MyDakaSelectAdapter(R.layout.item_daka_select, data);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("HomeFragment")) {
                    Intent intent = new Intent(mContext, CreatDakaActivity.class);
                    intent.putExtra(IntentParams.SELECT_Target, data.get(position).getTarget_name());
                    intent.putExtra(IntentParams.SELECT_TargetId, data.get(position).getTarget_uuid());
                    AppContacts.SELECT_Tag = data.get(position).getTarget_uuid();
                    startActivity(intent);
                    finish();

                } else {
                    Intent intent = new Intent();
                    intent.putExtra(IntentParams.SELECT_Target, data.get(position).getTarget_name());
                    intent.putExtra(IntentParams.SELECT_TargetId, data.get(position).getTarget_uuid());
                    setResult(0x14, intent);
                    AppContacts.SELECT_Tag = data.get(position).getTarget_uuid();
                    finish();
                }


            }
        });
    }


    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
    }


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_daka_select_target;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {

    }


    @OnClick(R.id.iv_back)
    public void goBack() {
        finish();
    }


}

