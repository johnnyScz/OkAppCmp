package com.xinyu.newdiggtest.ui.Digg.fragment;


import android.content.Context;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import android.widget.EditText;

import com.xinyu.newdiggtest.R;

import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.BaseBean;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;

import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.IntentParams;

import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditGroupNameActity extends BaseNoEventActivity {

    @BindView(R.id.et_groupname)
    public EditText grname;


    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initView();
    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_edit_grp_name;
    }

    private void initView() {

        grname.setHint(getIntent().getStringExtra(IntentParams.GROUP_NAME));

        grname.setFocusable(true);
        grname.setFocusableInTouchMode(true);
        grname.requestFocus();//获取焦点 光标出现

    }

    @OnClick(R.id.tv_cancel)
    public void cancel() {
        finish();
    }

    @OnClick(R.id.tv_save)
    public void save() {
        editGrName();
    }

    private void editGrName() {

        if (TextUtils.isEmpty(grname.getText().toString().trim())) {
            ToastUtils.getInstanc(this).showToast("请输入群名称");
            return;
        }
        commitEdit();
    }


    public void commitEdit() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(this).getSessonId());
        map.put("f_title", grname.getText().toString().trim());
        map.put("f_id", getIntent().getStringExtra("f_id"));
        map.put("f_function_ids", getIntent().getStringExtra("f_function_ids"));
        map.put("f_plugins", getIntent().getStringExtra("f_plugins"));
        map.put("isokr", getIntent().getStringExtra("isokr"));
        map.put("f_start_date", getIntent().getStringExtra("f_start_date"));
        map.put("f_end_date", getIntent().getStringExtra("f_end_date"));


        map.put("f_desc", getIntent().getStringExtra("f_desc"));

        url.updateAffair(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        ToastUtils.getInstanc(context).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            notifyGroup();

                        } else {
                            ToastUtils.getInstanc().showToast("编辑失败");
                        }


                    }
                });
    }


    public void notifyGroup() {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("group_id", getIntent().getStringExtra("f_id"));
        map.put("msg_type", "1");
        map.put("msg_content", getContent());
        map.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());
        map.put("sid", PreferenceUtil.getInstance(this).getSessonId());

        url.notify(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        ToastUtils.getInstanc(context).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean msg) {
                        if (msg.getOp().getCode().equals("Y")) {
                            XshellEvent event = new XshellEvent(EventConts.MSG_Refresh_GName);
                            event.msg = grname.getText().toString();
                            EventBus.getDefault().postSticky(event);
                            finish();
                        }
                    }
                });
    }


    /**
     * 发布的内容
     *
     * @return
     */
    private String getContent() {


        String content = PreferenceUtil.getInstance(mContext).getNickName() + "修改了群组名称,变更为" + "\"" + grname.getText().toString() + "\"";


        return content;
    }


}
