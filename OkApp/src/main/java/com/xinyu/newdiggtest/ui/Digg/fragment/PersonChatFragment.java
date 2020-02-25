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
import com.xinyu.newdiggtest.adapter.PersonChatAdapter;
import com.xinyu.newdiggtest.bean.PersonChatBean;
import com.xinyu.newdiggtest.bean.RoomPersonListBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;

import com.xinyu.newdiggtest.ui.LazySingleFragment;

import com.xinyu.newdiggtest.utils.DialogUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 私聊
 */
public class PersonChatFragment extends LazySingleFragment {
    /**
     * 空消息需要过滤 掉
     */


    @BindView(R.id.recyclerView)
    RecyclerView recyView;

    @BindView(R.id.emputview)
    ImageView emputview;


    PersonChatAdapter adapter;

    LoadingDailog dialog;
    List<RoomPersonListBean> listData = new ArrayList<>();


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_person_chat;
    }


    @Override
    protected void initView() {
        initAdapter();
        dialog = new DialogUtil(mContext).buildDialog("加载中...");
    }

    @Override
    public void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            if (listData.size() < 1) {
                Log.e("amtf", "加载好友");
                queryGroupList();
            }
        }
    }


    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
    }


    public void setDatas(final List<RoomPersonListBean> datas) {

        listData = filterEmpty(datas);
        adapter = new PersonChatAdapter(R.layout.item_grouplist, datas);
        recyView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                RoomPersonListBean data = datas.get(position);
                int len = datas.size();
                goChatActy(position, data, len);
            }
        });

    }

    private List<RoomPersonListBean> filterEmpty(List<RoomPersonListBean> datas) {
        int len = datas.size();
        for (int i = len - 1; i >= 0; i--) {
            if (MyTextUtil.isEmpty(datas.get(i).getLatstMsg())) {
                datas.remove(i);
            }
        }
        return datas;
    }

    private void goChatActy(int position, RoomPersonListBean data, int len) {
//        String roomId = data.getRoom_id();
//
//        NetApiUtil.readMsg(mContext, roomId, "chat");//读取消息红点
//
//        String roomName = data.getRoom_name();
//        String unionId = data.getUnion_id();
//        Intent intent = new Intent(mContext, PersonChatActivity.class);
//        APPConstans.PersonChat = true;
//        APPConstans.User_Id = data.getFriend_id();
//        APPConstans.Room_id = roomId;
//
//        intent.putExtra("roomName", roomName);
//        intent.putExtra("memberNum", len + "");
//        mContext.startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        countime.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        countime.cancel();
    }

    private void queryGroupList() {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("command", "ok-api.AppFindRoomList");
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("room_type", "S");
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("curPageNo", "1");
        map.put("pageRowCnt", "30");

        url.personChatList(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PersonChatBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(PersonChatBean msg) {
                        if (msg.getOp() == null) {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                            return;
                        }
                        if (msg.getOp().getCode().equals("Y")) {
                            List<RoomPersonListBean> list = msg.getRoom_list();
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


    //参数1 总时间
    //参数二 时间 间隔

    CountDownTimer countime = new CountDownTimer(4000 * 3600, 4000) {
        @Override
        public void onTick(long millisUntilFinished) {
            queryGroupList();
        }

        @Override
        public void onFinish() {


        }
    };

}
