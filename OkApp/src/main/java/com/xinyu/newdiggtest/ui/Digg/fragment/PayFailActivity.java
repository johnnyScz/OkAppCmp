package com.xinyu.newdiggtest.ui.Digg.fragment;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.BaseActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 支付失败
 */
public class PayFailActivity extends BaseActivity {


    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        // goTargetBangFirst();
    }


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_pay_fail;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {

    }

    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @OnClick(R.id.btn_return)
    public void goCommit() {
        //TODO 重新支付
        finish();

    }


    //TODO 挑战金/打赏/奖励 都要设置0
    private void goTargetBangFirst() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("target_id", getIntent().getStringExtra(IntentParams.DAKA_Target_Id));
        requsMap.put("fine", "0");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        url.targetPayFinish(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });

    }


}




