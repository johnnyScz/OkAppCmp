package com.xinyu.newdiggtest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;

import com.xinyu.newdiggtest.bean.MobileBean;
import com.xinyu.newdiggtest.bean.WxUserBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;

import com.xinyu.newdiggtest.ui.Digg.fragment.RelationTodoFragment;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 个人空间
 */
public class MySpaceActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    public TextView title;

    @BindView(R.id.name)
    public TextView myName;

    @BindView(R.id.img)
    public ImageView headImg;

    @BindView(R.id.tv_adress)
    public TextView tv_adress;


    @BindView(R.id.target_tab)
    public TabLayout tabLayout;

    @BindView(R.id.target_pager)
    public ViewPager viewPager;

    private String[] titles = {"待办", "群组", "个人信息"};
    private List<Fragment> fragmentList;


    SelfInfoFragment selfInfoFragment;

    String userid;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_space;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userid = getIntent().getStringExtra("userId");

        title.setText("个人空间");

        initData();
        initView();

    }

    private void initView() {
        getInfo(userid, PreferenceUtil.getInstance(mContext).getSessonId());

    }


    public void initData() {
        fragmentList = new ArrayList<Fragment>();

        selfInfoFragment = new SelfInfoFragment();
        selfInfoFragment.setUserId(userid);

//
//        InnerQunFragment fragment = new InnerQunFragment();
//        fragment.setUserId(userid);

        fragmentList.add(RelationTodoFragment.newInstance(userid));//与我相关的待办
//        fragmentList.add(fragment);
        fragmentList.add(selfInfoFragment);


        MyPagetAdapter adapter = new MyPagetAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //绑定
        tabLayout.setupWithViewPager(viewPager);

    }

    class MyPagetAdapter extends FragmentPagerAdapter {

        public MyPagetAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        //重写这个方法，将设置每个Tab的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {


    }


    @OnClick(R.id.iv_back)
    public void goCommit() {
        finish();
    }


    /**
     * 获取用户的mobile
     *
     * @param userId
     * @param sessionid
     */
    public void getInfo(final String userId, final String sessionid) {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", sessionid);
        map.put("user_id", userId);
        map.put("flag", "Y");

        url.getWxMobile(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MobileBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(MobileBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getUser() != null) {


                                showUi(msg.getUser());


                            }

                        } else {
                            ToastUtils.getInstanc().showToast("服务异常");
                        }

                    }
                });
    }


    private void showUi(WxUserBean user) {

        myName.setText(user.getNickname());

        String head = MyTextUtil.isEmpty(user.getHead()) ? user.getCustom_head() : user.getHead();

        if (!MyTextUtil.isEmpty(head)) {

            Picasso.with(mContext).load(head).error(R.drawable.icon_no_download).
                    transform(new CircleCornerForm()).into(headImg);
        } else {
            headImg.setImageResource(R.drawable.icon_no_download);
        }


        if (!MyTextUtil.isEmpty(user.getCity())) {
            tv_adress.setText(user.getProvince() + " " + user.getCity());
        }


    }


}



