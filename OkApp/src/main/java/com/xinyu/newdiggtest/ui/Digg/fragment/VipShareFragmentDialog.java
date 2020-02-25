package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.xinyu.newdiggtest.R;

public class VipShareFragmentDialog extends DialogFragment implements View.OnClickListener {


    OnPopClickListner listner;


    public void setOnPopListner(OnPopClickListner mlistner) {
        this.listner = mlistner;
    }


    public interface OnPopClickListner {

        void onCancle();

        void onShareWeixin();

        void onShareCircle();


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cancel:

                if (listner != null)
                    listner.onCancle();
                break;


            case R.id.ll_share_wechat:
                if (listner != null)
                    listner.onShareWeixin();
                break;

            case R.id.ll_share_circle:
                if (listner != null)
                    listner.onShareCircle();
                break;
        }
    }

    View dialogView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogView = inflater.inflate(R.layout.share_vip_dialog, container);
        setListner(dialogView);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialogView;

    }

    private void setListner(View root) {
        root.findViewById(R.id.cancel).setOnClickListener(this);
        LinearLayout paretn = root.findViewById(R.id.parent);
        int len = paretn.getChildCount();
        for (int i = 0; i < len; i++) {
            paretn.getChildAt(i).setOnClickListener(this);
        }

    }
}
