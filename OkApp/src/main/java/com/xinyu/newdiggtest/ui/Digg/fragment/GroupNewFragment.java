package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.tu.loadingdialog.LoadingDailog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.QunZuListAdapter;
import com.xinyu.newdiggtest.bean.GroupListBean;
import com.xinyu.newdiggtest.bean.RoomListBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.LazySingleFragment;

import com.xinyu.newdiggtest.utils.DialogUtil;

import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GroupNewFragment extends LazySingleFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyView;

    @BindView(R.id.emputview)
    ImageView emputview;


    QunZuListAdapter adapter;

    LoadingDailog dialog;
    List<RoomListBean> listData = new ArrayList<>();

    private CountDownTimer mTimer;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_group_new;
    }

    @Override
    protected void initView() {
        initAdapter();

        dialog = new DialogUtil(mContext).buildDialog("加载中...");
    }

    @Override
    public void onFragmentVisibleChange(boolean isVisible) {

    }


    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
    }


    public void setDatas(final List<RoomListBean> datas) {
        listData = filtPersonChat(datas);
        adapter = new QunZuListAdapter(R.layout.item_grouplist, listData);
        recyView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                RoomListBean data = datas.get(position);
                String len = data.getRoom_member_number();
                goChatActy(position, data, len);
            }
        });

    }

    /**
     * 过滤掉私聊
     *
     * @param datas
     * @return
     */
    private List<RoomListBean> filtPersonChat(List<RoomListBean> datas) {
        List<RoomListBean> retList = new ArrayList<>();
        for (RoomListBean item : datas) {
            if (item.getRoom_type().equals("G")) {
                retList.add(item);
            }
        }
        return retList;
    }


    private void goChatActy(int position, RoomListBean data, String len) {
        String roomId = data.getRoom_id();
        readMsg(roomId, position);
        String roomName = data.getRoom_name();

//        Intent intent = new Intent(mContext, ChatGrActivity.class);
//        APPConstans.PersonChat = false;
//        APPConstans.Room_id = roomId;
//        intent.putExtra("roomName", MyTextUtil.getDecodeStr(roomName));
//        intent.putExtra("memberNum", len);
//        intent.putExtra("headUrl", data.getRoom_head());
//        intent.putExtra(IntentParams.Main_Object, data.getMain_object());
//        mContext.startActivity(intent);
    }


    /**
     * 读取群消息
     *
     * @param roomId
     */
    private void readMsg(String roomId, final int pos) {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.ReadMessage");
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("topic", "group" + roomId);
        map.put("deleteAll", "true");
        url.readGroupMsg(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        if (msg.getOp().getCode().equals("Y")) {
                            notifyMyData(pos);
                        }
                    }
                });
    }

    private void notifyMyData(int pos) {
        adapter.getData().get(pos).setMsgCount("0");
        adapter.notifyItemChanged(pos);
    }




    private void queryGroupList() {
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.AppFindRoomList");
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("room_type", "G");
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("curPageNo", "1");
        map.put("pageRowCnt", "50");

        url.getGroupList(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GroupListBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(GroupListBean msg) {
                        if (msg.getOp() == null) {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                            return;
                        }
                        if (msg.getOp().getCode().equals("Y")) {
                            List<RoomListBean> list = msg.getRoom_list();
                            if (list == null || list.size() == 0) {
                                showEmptyView(true);
                                return;
                            }
                            showEmptyView(false);
                            setDatas(list);
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });
    }


    private void showEmptyView(boolean show) {
        if (show) {
            recyView.setVisibility(View.GONE);
            emputview.setVisibility(View.VISIBLE);

        } else {
            recyView.setVisibility(View.VISIBLE);
            emputview.setVisibility(View.GONE);
        }

    }


//    private void notify(RoomListBean data, int pos) {
//        adapter.getData().get(pos).setMsgCount(data.getMsgCount());
//        adapter.getData().get(pos).setLatstMsg(data.getLatstMsg());
//        adapter.notifyItemChanged(pos);
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


    /**
     * 更新列表中的红点和消息
     */


    private void timerCountBegin() {
        if (mTimer == null) {
            mTimer = new CountDownTimer(200000 * 1000, 5000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    queryGroupList();
                }

                @Override
                public void onFinish() {


                }
            }.start();

        }
    }

}










