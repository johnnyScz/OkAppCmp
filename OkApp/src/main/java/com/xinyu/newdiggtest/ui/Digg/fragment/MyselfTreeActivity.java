package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.AskRoomIdBean;
import com.xinyu.newdiggtest.bean.MobileBean;
import com.xinyu.newdiggtest.bean.WxUserBean;
import com.xinyu.newdiggtest.bigq.ToDoActivity;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.chat.ChatCompanyActivity;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 通讯录进个人中心
 */

public class MyselfTreeActivity extends BaseNoEventActivity {


    @BindView(R.id.tv_adress)
    TextView tv_adress;


    @BindView(R.id.img)
    ImageView headImg;

    @BindView(R.id.phone_tx)
    TextView textPhone;

    @BindView(R.id.name)
    TextView myName;

    @BindView(R.id.email)
    TextView email;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_self;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getInfo();

    }

    /**
     * 获取用户的信息
     */
    public void getInfo() {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", PreferenceUtil.getInstance(App.mContext).getSessonId());
        map.put("user_id", getIntent().getStringExtra("userId"));
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
                                setPersonData(msg.getUser());
                            }

                        } else {
                            ToastUtils.getInstanc().showToast("服务异常");
                        }

                    }
                });
    }

    public void setPersonData(WxUserBean personData) {

        showTop(personData);

        String mobie = personData.getMobile();

        if (!MyTextUtil.isEmpty(mobie)) {
            textPhone.setText(mobie);
        }

        if (!MyTextUtil.isEmpty(personData.getEmail())) {
            email.setText(personData.getEmail());
        }

        if (!MyTextUtil.isEmpty(personData.getProvince())) {

            tv_adress.setText(personData.getProvince() + " " + personData.getCity());
        }

    }


    String name, headUrl;

    private void showTop(WxUserBean user) {


        myName.setText(user.getNickname());
        headUrl = MyTextUtil.isEmpty(user.getHead()) ? user.getCustom_head() : user.getHead();

        if (getIntent().hasExtra("headUrl")) {
            headUrl = getIntent().getStringExtra("headUrl");
        }

        name = user.getNickname();


        if (!MyTextUtil.isEmpty(headUrl)) {

            Picasso.with(mContext).load(headUrl).error(R.drawable.icon_no_download).
                    transform(new CircleCornerForm()).into(headImg);
        } else {
            headImg.setImageResource(R.drawable.icon_no_download);
        }


        if (!MyTextUtil.isEmpty(user.getCity())) {
            tv_adress.setText(user.getProvince() + " " + user.getCity());
        }

    }


    @OnClick(R.id.iv_back)
    public void goCommit() {
        finish();
    }


    @OnClick(R.id.dianban)
    public void goTOdo() {

        Intent mintent = new Intent(mContext, RelationTodoActivity.class);
        mintent.putExtra("userId", getIntent().getStringExtra("userId"));
        mintent.putExtra("userName", name);
        mintent.putExtra("head", headUrl);

        startActivity(mintent);
    }


    @OnClick(R.id.msg_handnote)
    public void goTiaozhan() {
        ToastUtils.getInstanc().showToast("该功能暂未开放!");
    }

    @OnClick(R.id.msg_feixin)
    public void gofeixin() {
        getOrCreateRoodId();
    }


    public void getOrCreateRoodId() {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        final AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());


        map.put("friend_id", getIntent().getStringExtra("userId"));
        map.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());
        url.getOrCreateChatRoom(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AskRoomIdBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(AskRoomIdBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            AskRoomIdBean.RoomRetBean data = msg.getData();

                            if (data == null || MyTextUtil.isEmpty(data.getRoom_id())) {
                                return;
                            }

                            Intent mIntent = new Intent(mContext, ChatCompanyActivity.class);
                            mIntent.putExtra("room_id", data.getRoom_id());
                            mIntent.putExtra("room_name", data.getRoom_name());
                            mIntent.putExtra("room_type", "S");
                            startActivity(mIntent);
                        }

                    }
                });
    }


    @OnClick({R.id.send_file, R.id.send_img, R.id.call, R.id.send_email, R.id.send_hongbao})
    public void onclicEvent() {

        ToastUtils.getInstanc().showToast("暂无开放该功能!");

    }


    @OnClick(R.id.create_todo)
    public void creatTodo() {
        Intent intent = new Intent(mContext, ToDoActivity.class);
        intent.putExtra("date", DateUtil.getCurrentData());
        intent.putExtra("signer", getIntent().getStringExtra("userId"));

        intent.putExtra("head", headUrl);
        intent.putExtra("name", name);


        startActivity(intent);

    }

}
