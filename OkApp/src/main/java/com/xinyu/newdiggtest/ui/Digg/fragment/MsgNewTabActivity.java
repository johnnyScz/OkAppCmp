package com.xinyu.newdiggtest.ui.Digg.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.InfoStr;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.DisplayUtils;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MsgNewTabActivity extends BaseNoEventActivity implements MsgFragment.OnFragmentInteractionListener {


    @BindView(R.id.icon_right)
    ImageView icon_right;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_msg;
    }


    @OnClick(R.id.back)
    public void goCommit() {
        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initPop();

    }

    PopupWindow popupWindow;

    private void initPop() {

        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.pop_msg, null);
        setListner(view);
        popupWindow = new PopupWindow(view, DisplayUtils.dp2px(mContext, 135),
                DisplayUtils.dp2px(mContext, 50));

        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
    }

    private void setListner(LinearLayout view) {
        view.findViewById(R.id.note_hasread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

                goSetHaveRead();

            }
        });


    }


    /**
     * 全部消息设置为已读
     */

    private void goSetHaveRead() {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());

        url.readAllMsg(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InfoStr>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(InfoStr msg) {

                        if (msg.getOp().getCode().equals("Y")) {
                            msgFrag.refreshData(1);
                            EventBus.getDefault().post(new XshellEvent(EventConts.HomeFresh));

                        } else {
                            ToastUtils.getInstanc().showToast("消息置已读：服务异常");
                        }


                    }
                });


    }

    MsgFragment msgFrag;

    private void initView() {

        msgFrag = MsgFragment.newInstance("2");

        getSupportFragmentManager()    //
                .beginTransaction()
                .add(R.id.fragment, msgFrag)
                .commit();


    }


    @Override
    public void onFragmentInteraction(String data) {

    }


    @OnClick(R.id.icon_right)
    public void goMore() {

        if (!popupWindow.isShowing()) {
//            popupWindow.showAsDropDown(icon_right, (DisplayUtils.dp2px(mContext, 30)),
//                    DisplayUtils.dp2px(mContext, 30));

            popupWindow.showAsDropDown(icon_right);

        } else {
            popupWindow.dismiss();
        }

    }


}






