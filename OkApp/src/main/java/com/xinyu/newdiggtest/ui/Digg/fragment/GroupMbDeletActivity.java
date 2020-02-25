package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.RoomMemberBean;
import com.xinyu.newdiggtest.adapter.TotalMemberAdapter;
import com.xinyu.newdiggtest.bean.GroupMemberBean;
import com.xinyu.newdiggtest.bean.MemberBean;
import com.xinyu.newdiggtest.bean.MemberRetBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
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
 * 删除/增加好友
 */
public class GroupMbDeletActivity extends BaseNoEventActivity {


    @BindView(R.id.recyclerView)
    public RecyclerView recyView;

    String type = "1";//1 增加  -1 删除

    TotalMemberAdapter searchAdaper;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_delet_member;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecycle();

        queryData();
    }


    public void queryData() {

        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("room_id", getIntent().getStringExtra("room_id"));

        url.queryZuMembers(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GroupMemberBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(GroupMemberBean msg) {
                        loadingDailog.dismiss();

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getMember_list() != null && msg.getMember_list().size() > 0) {

                                handleDatas(msg.getMember_list());

                            } else {
                                ToastUtils.getInstanc(mContext).showToast("没有数据！");
                            }

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });
    }


    private void handleDatas(List<RoomMemberBean> data) {

        List<MemberRetBean.MemberOutBean> res = checkDatas(data);
        searchAdaper = new TotalMemberAdapter(R.layout.item_totalmember, res);
        recyView.setAdapter(searchAdaper);
    }

    private List<MemberRetBean.MemberOutBean> checkDatas(List<RoomMemberBean> data) {

        List<MemberRetBean.MemberOutBean> newDatas = new ArrayList<>();

        for (RoomMemberBean bean : data) {
            MemberRetBean.MemberOutBean item = new MemberRetBean.MemberOutBean();
            item.setUserinfo(creatInfo(bean));
            newDatas.add(item);
        }

        return newDatas;
    }

    private MemberBean creatInfo(RoomMemberBean bean) {

        MemberBean kk = new MemberBean();

        kk.setName(MyTextUtil.isEmpty(bean.getUser().getName()) ? bean.getUser().getNickname() : bean.getUser().getName());
        kk.setHead(bean.getUser().getHead());
        kk.setUser_id(bean.getUser().getUser_id());

        return kk;
    }


    private void initRecycle() {

        type = getIntent().getStringExtra("add_type");

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


}




