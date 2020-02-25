package com.xinyu.newdiggtest.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.SelfBean;
import com.xinyu.newdiggtest.bean.UserInfoBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.Digg.HomeDiggActivity;
import com.xinyu.newdiggtest.ui.Digg.PreLoginActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.CashActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.FeedBackActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.PersonalActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.VipIntroduceActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.VipListActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.VipShareFragmentDialog;
import com.xinyu.newdiggtest.ui.Digg.mine.MyWalletActivity;
import com.xinyu.newdiggtest.ui.Digg.target.MySelfFocusTargetActivity;
import com.xinyu.newdiggtest.utils.CircleTransform;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.wxapi.WeixinUtil;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的
 */
public class MySelfFragment extends Fragment implements View.OnClickListener {
    HomeDiggActivity mContext;
    ImageView icon;
    TextView name;
    TextView tv_adress_me;

    String vipInfo = "";

    //---------------------------
    TextView tv_yue, tv_foucs_target, tv_foucus_count;
    TextView vip_alarm, open_vip;

    ImageView chapiom;

    View rl_feedback;

    protected void initView() {

        chapiom = rootVeiw.findViewById(R.id.chapiom);
        icon = rootVeiw.findViewById(R.id.icon_me);
        icon.setOnClickListener(this);
        name = rootVeiw.findViewById(R.id.tv_name_me);
        tv_adress_me = rootVeiw.findViewById(R.id.tv_adress_me);
        tv_yue = rootVeiw.findViewById(R.id.tv_yue);
        tv_foucs_target = rootVeiw.findViewById(R.id.tv_foucs_target);
        tv_foucus_count = rootVeiw.findViewById(R.id.tv_foucus_count);
        vip_alarm = rootVeiw.findViewById(R.id.vip_alarm);
        open_vip = rootVeiw.findViewById(R.id.open_vip);

        rl_feedback = rootVeiw.findViewById(R.id.rl_feedback);
        setListner();


    }


    @Override
    public void onResume() {
        super.onResume();
        requestUserInfo();
        requestSelfInfo();
    }

    private void setListner() {
        rootVeiw.findViewById(R.id.open_vip).setOnClickListener(this);
        rootVeiw.findViewById(R.id.btn_logout).setOnClickListener(this);
        rootVeiw.findViewById(R.id.rl_cash_out).setOnClickListener(this);
        rootVeiw.findViewById(R.id.focus_person).setOnClickListener(this);
        rootVeiw.findViewById(R.id.rl_focus_target).setOnClickListener(this);
        rootVeiw.findViewById(R.id.ll_wallet).setOnClickListener(this);
        rootVeiw.findViewById(R.id.ll_share).setOnClickListener(this);

        rl_feedback.setOnClickListener(this);


    }


    public void requestUserInfo() {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "user.getUserInfo");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("flag", "Y");
        url.getUserInfo(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserInfoBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(UserInfoBean msg) {
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getUser() != null) {
                                upHeadView(msg.getUser());
                            }
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });
    }


    public void requestSelfInfo() {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "ok-api.getFollowUserAndTargetNum");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        url.getSelfInfo(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SelfBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(SelfBean msg) {
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData() != null) {
                                showSelfInfo(msg.getData());
                            }

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });
    }

    /**
     * 账号余额，关注的人数，目标的个数
     *
     * @param data
     */
    private void showSelfInfo(SelfBean.DataBean data) {
        tv_yue.setText(data.getMoney() + "元");
        tv_foucs_target.setText(data.getFollowtarget());
        tv_foucus_count.setText(data.getFollowuser());
    }


    private void upHeadView(UserInfoBean.UserBean user) {

        vipInfo = user.getBecome_vip_date();
        if (MyTextUtil.isEmpty(vipInfo) || vipInfo.equals("0")) {
            open_vip.setVisibility(View.VISIBLE);
            vip_alarm.setText(R.string.vip_no);
            chapiom.setVisibility(View.INVISIBLE);

        } else {
            open_vip.setVisibility(View.GONE);
            vip_alarm.setText(R.string.vip_yes);
            chapiom.setVisibility(View.VISIBLE);
            PreferenceUtil.getInstance(mContext).setIsVip(true);
            setClick();
        }


        if (!MyTextUtil.isEmpty(user.getHead())) {
            Picasso.with(mContext).load(user.getHead()).
                    transform(new CircleTransform()).into(icon);
        } else {
            icon.setImageResource(R.drawable.icon_no_download);
        }
        name.setText(user.getNickname());
        tv_adress_me.setText(user.getProvince());
    }

    private void setClick() {
        vip_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, VipListActivity.class));
            }
        });
    }


    private long firstPressedTime;

    private void exit() {
        if (System.currentTimeMillis() - firstPressedTime < 1000) {
            PreferenceUtil.getInstance(mContext).setUserId("");
            mContext.startActivity(new Intent(mContext, PreLoginActivity.class));

        } else {
            ToastUtils.getInstanc(mContext).showToast("再点一次退出登录");
            firstPressedTime = System.currentTimeMillis();
        }

    }


    View rootVeiw;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (HomeDiggActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootVeiw = inflater.inflate(R.layout.test_layout, null);
        initView();
        return rootVeiw;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_logout:

                exit();
                break;

            case R.id.rl_cash_out:
                startActivity(new Intent(mContext, CashActivity.class));
                break;

            case R.id.focus_person:

                Intent intent = new Intent(mContext, MySelfFocusTargetActivity.class);
                intent.putExtra("current", "0");
                startActivity(intent);
                break;

            case R.id.rl_focus_target:
                Intent intent1 = new Intent(mContext, MySelfFocusTargetActivity.class);
                intent1.putExtra("current", "1");
                startActivity(intent1);
                break;


            case R.id.open_vip:

                if (TextUtils.isEmpty(vipInfo) || vipInfo.equals("0")) {
                    Intent intent2 = new Intent(mContext, VipIntroduceActivity.class);
                    intent2.putExtra(IntentParams.IsVIP, "N");//N 非VIP Y 是vip
                    mContext.startActivity(intent2);
                } else {

                    //TODO 已经是Vip会员,后面要改
                    Intent intent2 = new Intent(mContext, VipListActivity.class);
                    mContext.startActivity(intent2);


                }
                break;

            case R.id.ll_wallet:
                mContext.startActivity(new Intent(mContext, MyWalletActivity.class));
                break;

            case R.id.rl_feedback:

//                startActivity(new Intent(mContext, MonitorListActivity1.class));
                startActivity(new Intent(mContext, FeedBackActivity.class));
                break;

            case R.id.ll_share:
                go2Share();
                break;

            case R.id.icon_me:
                startActivity(new Intent(mContext, PersonalActivity.class));
                break;

        }
    }

    private void go2Share() {
        final VipShareFragmentDialog dialog = new VipShareFragmentDialog();
        FragmentTransaction ft = mContext.getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, "tag");

        dialog.setOnPopListner(new VipShareFragmentDialog.OnPopClickListner() {
            @Override
            public void onCancle() {
                dialog.dismiss();
            }

            @Override
            public void onShareWeixin() {
                dialog.dismiss();
                doShareWxUrl(false);

            }

            @Override
            public void onShareCircle() {
                dialog.dismiss();
                doShareWxUrl(true);
            }
        });

    }

    String downLoadStr = "https://www.pgyer.com/jnlo";

    private void doShareWxUrl(boolean isFrid) {

        String title = "你的好友" + PreferenceUtil.getInstance(mContext).getNickName() + "邀请你使用Ok！";
        String content = "OK！基于社交进行目标管理，通过亲朋好友相互监督、打赏激励的方式帮助大家培养目标感";

        WeixinUtil.getInstance().diggWxShare
                (mContext, downLoadStr, title,
                        content, R.mipmap.ic_launcher, isFrid);


    }


}
