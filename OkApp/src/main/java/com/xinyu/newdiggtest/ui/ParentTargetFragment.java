package com.xinyu.newdiggtest.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.ViewPagerTwoAdapter;
import com.xinyu.newdiggtest.ui.Digg.TargetGroupActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.TargetFragment;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;


public class ParentTargetFragment extends Fragment implements View.OnClickListener {


    TargetFragment frag1, frag2;
    Context mctx;

    ViewPager viewpager;
    List<Fragment> list;
    ViewPagerTwoAdapter adapter;

    TextView now;
    TextView history;

    ImageView more;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_page, null);
        initView(view);
        initChebox(view);
        return view;
    }

    private void initChebox(View view) {
        now = view.findViewById(R.id.now);
        history = view.findViewById(R.id.history);

        now.setOnClickListener(this);
        history.setOnClickListener(this);
    }


    private void initView(View view) {

        viewpager = view.findViewById(R.id.viewpager);
        more = view.findViewById(R.id.iv_addtarget);
        more.setOnClickListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();

        resetTop();


        list = new ArrayList<>();
        frag1 = TargetFragment.newInstance("0", PreferenceUtil.getInstance(mctx).getUserId(), "0");
        frag2 = TargetFragment.newInstance("1", PreferenceUtil.getInstance(mctx).getUserId(), "0");


        list.add(frag1);
        list.add(frag2);
        adapter = new ViewPagerTwoAdapter(getChildFragmentManager());
        adapter.setdata(list);
//        tab.setupWithViewPager(viewpager);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);

    }

    private void resetTop() {
        now.setBackgroundResource(R.drawable.shaper_left_botton);
        now.setTextColor(getResources().getColor(R.color.mall_mainColor));
        history.setBackground(null);
        history.setTextColor(getResources().getColor(R.color.white));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mctx = context;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.now:
                resetTop();

                viewpager.setCurrentItem(0);

                break;

            case R.id.history:
                history.setBackgroundResource(R.drawable.shaper_right_botton);
                history.setTextColor(getResources().getColor(R.color.mall_mainColor));

                now.setBackground(null);
                now.setTextColor(getResources().getColor(R.color.white));

                viewpager.setCurrentItem(1);

                break;

            case R.id.iv_addtarget:
                mctx.startActivity(new Intent(mctx, TargetGroupActivity.class));
                break;

        }
    }


//    private void showDialog() {
//        if (!popupWindow.isShowing()) {
//            popupWindow.showAsDropDown(more, (DisplayUtils.dp2px(mctx, 60)),
//                    DisplayUtils.dp2px(mctx, 30));
//
//        } else {
//            popupWindow.dismiss();
//        }
//    }

//
//    private void setListner(LinearLayout view) {
//        int childCount = view.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            view.getChildAt(i).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int viewId = v.getId();
//                    switch (viewId) {
//                        case R.id.ll_creat_group:
//                            mctx.startActivity(new Intent(mctx, TargetGroupActivity.class));
//                            popupWindow.dismiss();
//                            break;
//
//
//                        case R.id.ll_swipt:
//                            startActivity(new Intent(mctx, CaptureActivity.class));
//                            popupWindow.dismiss();
//                            break;
//
//
//                    }
//
//                }
//            });
//        }
//
//    }

//    PopupWindow popupWindow;

//    private void initPop() {
//        LinearLayout view = (LinearLayout) LayoutInflater.from(mctx).inflate(R.layout.dialog_target, null);
//        setListner(view);
//        popupWindow = new PopupWindow(view, DisplayUtils.dp2px(mctx, 135),
//                DisplayUtils.dp2px(mctx, 100));
//
//        popupWindow.setTouchable(true);
//        popupWindow.setOutsideTouchable(true);
//
//
//    }


}
