package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.RoomMemberBean;
import com.xinyu.newdiggtest.bean.GroupMemberBean;
import com.xinyu.newdiggtest.bean.JoinBean;
import com.xinyu.newdiggtest.bean.RoomInfoBean;
import com.xinyu.newdiggtest.bean.UserInfoBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;

import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 扫描二维码接受邀请
 */
public class QcodeInviteActivity extends BaseNoEventActivity {

    @BindView(R.id.gr_name)
    public TextView gr_name;

    @BindView(R.id.gr_members)
    public TextView gr_members;

    @BindView(R.id.gr_head)
    public ImageView gr_head;


    @BindView(R.id.invite_info)
    public TextView invite_info;


    String members = "0";

    String inViterName = "";
    boolean isInRoom = false;//是否已经在群组

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        getUserName();

    }

    public void getUserName() {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "user.getUserInfo");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", getIntent().getStringExtra("formUserId"));
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
                                inViterName = msg.getUser().getNickname();
                                getGroupInfo();
                            }
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });
    }

    public void getGroupInfo() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "chat.GetChatRoomById");
        map.put("room_id", getIntent().getStringExtra("roomId"));

        url.getRoomInfo(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RoomInfoBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(RoomInfoBean msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData() != null) {
                                showRoomInfo(msg.getData());
                            }
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });
    }

    /**
     * 加入群组
     */
    public void joinGroup() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "user.p_joinRoom");
        map.put("room_id", getIntent().getStringExtra("roomId"));
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());

        url.joinRoom(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JoinBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(JoinBean msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            goChat();

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });
    }

    /**
     * 开始聊天
     */
    private void goChat() {
//        Intent intent = new Intent(mContext, ChatGrActivity.class);
//        APPConstans.PersonChat = false;
//        APPConstans.Room_id = getIntent().getStringExtra("roomId");
//        intent.putExtra("roomName", gr_name.getText().toString());
//        intent.putExtra("memberNum", members);
//        mContext.startActivity(intent);
//        finish();
    }


    private void showRoomInfo(RoomInfoBean.RoomInfo data) {

        if (!MyTextUtil.isEmpty(inViterName)) {
            invite_info.setText(inViterName + "邀请您加入群聊");
        }

        gr_name.setText(MyTextUtil.getDecodeStr(data.getRoom_name()));

        members = data.getRoom_member_number();
        gr_members.setText(members + "人");
        Picasso.with(mContext).load(data.getRoom_head()).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(gr_head);
    }


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_qcode_invite;
    }


    @OnClick(R.id.icon_back)
    public void goBack() {

        finish();
    }


    @OnClick(R.id.btn_commint)
    public void goInvite() {
        checkIfHaveInRoom();
    }


    /**
     * 查询是否已经加入群组了
     */
    public void checkIfHaveInRoom() {
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "user.p_findRoomMembers");
        map.put("sid", PreferenceUtil.getInstance(this).getSessonId());
        map.put("room_id", getIntent().getStringExtra("roomId"));

        url.getGroupMember(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GroupMemberBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(GroupMemberBean msg) {

                        if (msg.getOp() == null) {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                            return;
                        }
                        if (msg.getOp().getCode().equals("Y")) {
                            List<RoomMemberBean> mlist = msg.getMember_list();
                            if (mlist == null || mlist.size() == 0) {
                                isInRoom = false;
                            } else {
                                isInRoom = iFinTheList(mlist);
                            }

                            if (isInRoom) {
                                goChat();
                            } else {
                                joinGroup();
                            }

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });
    }

    private boolean iFinTheList(List<RoomMemberBean> mlist) {
        for (RoomMemberBean item : mlist) {
            if (item.getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                return true;
            }
        }
        return false;
    }

}




