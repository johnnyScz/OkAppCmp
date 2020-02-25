package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.CreatRoomId;
import com.xinyu.newdiggtest.bean.RoomIdBean;
import com.xinyu.newdiggtest.bean.UserInfoBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;

import com.xinyu.newdiggtest.utils.CircleCornerForm;
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
 * 私信扫描成功的中间页面
 */
public class SxSwiptActivity extends BaseNoEventActivity {

    @BindView(R.id.name)
    TextView name;


    @BindView(R.id.area)
    TextView area;

    @BindView(R.id.head)
    ImageView head;


    String hisUserId = "";

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_sx_qcode;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVeiw();
    }

    private void initVeiw() {
        hisUserId = getIntent().getStringExtra("userId");

        if (!MyTextUtil.isEmpty(hisUserId)) {
            requestUserInfo();
        }


    }


    public void requestUserInfo() {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "user.getUserInfo");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", hisUserId);
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

    /**
     * 显示用户信息
     *
     * @param user
     */
    private void upHeadView(UserInfoBean.UserBean user) {

        String headUrl = user.getHead();
        if (!MyTextUtil.isEmpty(headUrl))
            Picasso.with(mContext).load(headUrl).error(R.drawable.icon_no_download).
                    transform(new CircleCornerForm()).into(head);

        name.setText(user.getNickname());
        area.setText(user.getProvince());


    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }

    @OnClick(R.id.btn_commit)
    public void goSx() {
        if (!MyTextUtil.isEmpty(hisUserId)) {
            checkIfInRoom(hisUserId);
        }
    }

    /**
     * 看是否在同一个聊天室
     *
     * @param hisUserId
     */
    public void checkIfInRoom(String hisUserId) {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> maps = new HashMap<>();

        maps.put("command", "chat.QueryChatRoomById");
        maps.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        maps.put("chat_user_id", hisUserId);
        maps.put("room_type", "S");


        url.queryChatRoom(maps).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RoomIdBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());


                    }

                    @Override
                    public void onNext(RoomIdBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getDatalist() == null || msg.getDatalist().size() < 1) {
                                firstCreatRoom();

                            } else {
                                thenEnterRoom(msg.getDatalist().get(0).getRoom_id());
                            }

                        } else {
                            ToastUtils.getInstanc().showToast("服务异常");
                        }

                    }
                });

    }


    public void firstCreatRoom() {

        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> maps = new HashMap<>();
        maps.put("command", "chat.AddChatRoom");
        maps.put("room_name", name.getText().toString());
        maps.put("room_property", "normal");
        maps.put("chat_user_ids", creatUnionStr());
        maps.put("union_ids", creatUnionStr());

        maps.put("room_type", "S");
        maps.put("room_head", PreferenceUtil.getInstance(mContext).getHeadUrl());
        maps.put("main_object", PreferenceUtil.getInstance(mContext).getUserId());


        url.creatChatRoom(maps).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreatRoomId>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());

                    }

                    @Override
                    public void onNext(CreatRoomId msg) {

                        if (msg.getOp().getCode().equals("Y")) {
                            if (!MyTextUtil.isEmpty(msg.getRoom_id())) {
                                thenEnterRoom(msg.getRoom_id());
                            }
                        } else {
                            ToastUtils.getInstanc().showToast("服务异常");
                        }

                    }
                });

    }

    private void thenEnterRoom(String room_id) {
//        Intent intent = new Intent(mContext, PersonChatActivity.class);
//        intent.putExtra("roomName", PreferenceUtil.getInstance(mContext).getNickName());
//        APPConstans.PersonChat = true;
//        APPConstans.User_Id = hisUserId;
//        APPConstans.Room_id = room_id;
//        startActivity(intent);

    }


    private String creatUnionStr() {
        String mStr = PreferenceUtil.getInstance(mContext).getUserId();
        return mStr + "," + hisUserId;
    }


}
