package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.DeleteAdapter;
import com.xinyu.newdiggtest.adapter.RoomMemberBean;
import com.xinyu.newdiggtest.bean.GroupMemberBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 删除好友
 */
public class DeletMemberActivity extends BaseNoEventActivity {

    DeleteAdapter adapter;

    @BindView(R.id.recyclerView)
    public RecyclerView recyView;

    List<RoomMemberBean> selectMembers = new ArrayList<>();

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_delet_member;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecycle();
        selectMembers.clear();
    }

    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
    }


    @Override
    protected void onStart() {
        super.onStart();
        getMemberList();
    }


    /**
     * 获取群成员
     */
    public void getMemberList() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "user.p_findRoomMembers");
        map.put("sid", PreferenceUtil.getInstance(this).getSessonId());
        map.put("room_id", getIntent().getStringExtra("room_id"));

        url.getGroupMember(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GroupMemberBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(GroupMemberBean msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp() == null) {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                            return;
                        }
                        if (msg.getOp().getCode().equals("Y")) {
                            List<RoomMemberBean> mlist = msg.getMember_list();
                            if (mlist == null || mlist.size() == 0) {
                                return;
                            } else {
                                showView(mlist);
                            }

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });
    }

    private void showView(List<RoomMemberBean> mlist) {
        int len = mlist.size();
        for (int i = len - 1; len >= 0; i--) {
            if (mlist.get(i).getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                mlist.remove(i);
                break;
            }
        }
        adapter = new DeleteAdapter(R.layout.item_delet, mlist);
        recyView.setAdapter(adapter);

        adapter.setCheckBoxListner(new DeleteAdapter.OnCheckBoxListner() {
            @Override
            public void addSelcet(RoomMemberBean bean) {

                if (!selectMembers.contains(bean)) {
                    selectMembers.add(bean);
                }

            }

            @Override
            public void removeSelcet(RoomMemberBean bean) {
                if (selectMembers.contains(bean)) {
                    int len = selectMembers.size();
                    for (int i = len - 1; i >= 0; i--) {
                        if (selectMembers.get(i).getUser_id().equals(bean.getUser_id())) {
                            selectMembers.remove(i);
                            break;
                        }
                    }
                }


            }
        });
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @OnClick(R.id.commit)
    public void commit() {
        if (selectMembers.size() < 1) {
            ToastUtils.getInstanc().showToast("请选择好友");
            return;
        }

        String members = getTogetherStr();

        if (!MyTextUtil.isEmpty(members)) {
            deletGroup(members);
        }


    }

    private String getTogetherStr() {
        StringBuffer buffer = new StringBuffer();
        for (RoomMemberBean item : selectMembers) {
            buffer.append(item.getUser_id()).append(",");
        }
        String str = buffer.toString();
        return str.substring(0, str.length() - 1);
    }


    public void deletGroup(String userIds) {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.BatchDelChatRoomMember");
        map.put("room_id", getIntent().getStringExtra("room_id"));
        map.put("user_ids", userIds);

        url.deletMembers(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            ToastUtils.getInstanc().showToast("删除成功");
                            finish();
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });
    }


}




