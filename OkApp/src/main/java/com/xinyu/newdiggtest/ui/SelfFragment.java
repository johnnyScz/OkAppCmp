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
import com.xinyu.newdiggtest.app.HomeAppActivity;
import com.xinyu.newdiggtest.ui.Digg.login.AppLoginActivity;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我
 */
public class SelfFragment extends Fragment {
    HomeAppActivity mContext;

    View rootVeiw;


    @BindView(R.id.icon_me)
    public ImageView headIcon;


    @BindView(R.id.tv_name_me)
    public TextView name_me;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (HomeAppActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootVeiw = inflater.inflate(R.layout.slef_new_layout, null);
        ButterKnife.bind(this, rootVeiw);

        init();

        return rootVeiw;
    }

    private void init() {

        if (!MyTextUtil.isEmpty(PreferenceUtil.getInstance(mContext).getHeadUrl())) {
            Picasso.with(mContext).load(PreferenceUtil.getInstance(mContext).getHeadUrl()).error(R.drawable.icon_no_download).
                    transform(new CircleCornerForm()).into(headIcon);
        }

        name_me.setText(PreferenceUtil.getInstance(mContext).getNickName());


    }


    @OnClick(R.id.go_self)
    public void goSelf() {
        startActivity(new Intent(mContext, SelfActivity.class));
    }


    @OnClick(R.id.logout)
    public void loginOut() {


        PreferenceUtil.getInstance(mContext).setUserId("");
        PreferenceUtil.getInstance(mContext).setWxSession("");
        PreferenceUtil.getInstance(mContext).setMobilePhone("");
        PreferenceUtil.getInstance(mContext).setHeadUrl("");
        PreferenceUtil.getInstance(mContext).setNickName("");
        App.getSocket().disconnect();

        Intent mIntent = new Intent(mContext, AppLoginActivity.class);
        mIntent.putExtra("Enter_type", "logout");
        mContext.startActivity(mIntent);

    }


}
