package com.xinyu.newdiggtest.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xinyu.newdiggtest.R;

public class PictFragment extends Fragment {

    String imgUrl;

    String tip;

    View rootView;

    ImageView imageView;
    TextView tipTx;

    Activity ctx;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = getActivity();
    }

    public void setUrl(String url) {
        this.imgUrl = url;
    }

    public void setTip(String mtip) {
        this.tip = mtip;
    }

    PictFragment getInstance(String mImgUrl) {
        PictFragment frag = new PictFragment();
        frag.setUrl(mImgUrl);
        return new PictFragment();
    }

    @Override
    public void onStart() {
        super.onStart();

        initView();
    }

    private void initView() {
        imageView = rootView.findViewById(R.id.imgs);
        tipTx = rootView.findViewById(R.id.dotsimg);
        Glide.with(this).load(imgUrl).into(imageView);
        tipTx.setText(tip);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.finish();
                ctx.overridePendingTransition(R.anim.anim_out, 0);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_fragment, null);
        return rootView;
    }
}
