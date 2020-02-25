package com.xinyu.newdiggtest.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.UserInfoBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.Digg.HomeDiggActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.FilmListActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.PersonalActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.VipIntroduceActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.VipListActivity;
import com.xinyu.newdiggtest.ui.Digg.mine.MyWalletActivity;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的
 */
public class NewSelfFragment extends Fragment {
    HomeDiggActivity mContext;

    View rootVeiw;


    @BindView(R.id.tv_adress_me)
    public TextView tv_adress_me;

    @BindView(R.id.vip_iv)
    public ImageView vip_iv;


    @BindView(R.id.tv_name_me)
    public TextView tv_name_me;

    @BindView(R.id.icon_me)
    public ImageView icon_me;


    @Override
    public void onResume() {
        super.onResume();
        requestUserInfo();
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


    private void upHeadView(UserInfoBean.UserBean user) {


        tv_name_me.setText(user.getNickname());

        if (!MyTextUtil.isEmpty(user.getHead())) {
            Picasso.with(mContext).load(user.getHead()).
                    transform(new CircleCornerForm()).into(icon_me);
        } else {
            icon_me.setImageResource(R.drawable.icon_no_download);
        }

        if (!MyTextUtil.isEmpty(user.getProvince())) {
            tv_adress_me.setText(user.getProvince());
        }

        String vipInfo = user.getBecome_vip_date();

        if (MyTextUtil.isEmpty(vipInfo) || vipInfo.equals("0")) {
            PreferenceUtil.getInstance(mContext).setIsVip(false);
        } else {
            PreferenceUtil.getInstance(mContext).setIsVip(true);

        }


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (HomeDiggActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootVeiw = inflater.inflate(R.layout.self_layout, null);
        ButterKnife.bind(this, rootVeiw);

        initView();
        return rootVeiw;
    }

    private void initView() {
        if (PreferenceUtil.getInstance(mContext).getIsVip()) {
//            vip_iv.setImageResource(R.mipmap.wo_vip);
        } else {
            vip_iv.setImageResource(R.mipmap.icon_vip);
        }
    }


    @OnClick(R.id.icon_me)
    public void personInfo() {

        mContext.startActivity(new Intent(mContext, PersonalActivity.class));

    }


    @OnClick(R.id.vip_iv)
    public void vip() {
        if (PreferenceUtil.getInstance(mContext).getIsVip()) {
            startActivity(new Intent(mContext, VipListActivity.class));


        } else {
            Intent intent = new Intent(mContext, VipIntroduceActivity.class);
            intent.putExtra(IntentParams.IsVIP, "N");
            startActivity(intent);
        }
    }

    @OnClick(R.id.ll_wallet)
    public void goWallet() {
        mContext.startActivity(new Intent(mContext, MyWalletActivity.class));
    }


    @OnClick(R.id.rl_movie)
    public void goFilm() {
        mContext.startActivity(new Intent(mContext, FilmListActivity.class));
    }

}
