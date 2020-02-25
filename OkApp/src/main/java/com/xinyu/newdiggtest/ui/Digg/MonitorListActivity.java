package com.xinyu.newdiggtest.ui.Digg;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.FenZuAdapter;
import com.xinyu.newdiggtest.bean.MonitorChildBean;
import com.xinyu.newdiggtest.bean.MonitorListBean;
import com.xinyu.newdiggtest.bean.MonitorParentBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.AppContacts;
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

public class MonitorListActivity extends BaseNoEventActivity {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.emputview)
    public View empty;

    private FenZuAdapter adapter;

    @BindView(R.id.ll_parent)
    public LinearLayout llParent;

    @BindView(R.id.ed_search)
    public EditText searchInput;

    MonitorListBean netData;

    List<MonitorParentBean> mdatas;
    List<MonitorChildBean> selectDatas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectDatas.clear();
        initChildClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDatas();
    }


    /**
     * 子View的点击事件
     */
    private void initChildClick() {
        int len = llParent.getChildCount();
        for (int i = 0; i < len; i++) {
            final RelativeLayout child = (RelativeLayout) llParent.getChildAt(i);
            child.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    child.setVisibility(View.GONE);
                    String uid = (String) child.getTag();
                    removeMonitorFromList(uid);
                    //TODO 刷新列表
//                    reloadData();
                }
            });
        }


    }

    private void removeMonitorFromList(String uid) {

        List<MonitorChildBean> removlist = new ArrayList<>();
        removlist.clear();

        for (MonitorChildBean ben : selectDatas) {
            if (!ben.getUser_id().equals(uid)) {
//                selectDatas.remove(ben);
                removlist.add(ben);
            }
        }
        selectDatas = removlist;
    }


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_monitor_list;
    }


    private void getDatas() {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "user.p_QueryAllChatRoomMember");
        map.put("sid", PreferenceUtil.getInstance(this).getSessonId());
        url.getMonitorList(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MonitorListBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(MonitorListBean msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp() == null) {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                            return;
                        }

                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getDatalist() == null || msg.getDatalist().size() == 0) {
                                showEmpty(true);
                                return;
                            }
                            showEmpty(false);
                            netData = msg;
                            mdatas = msg.getDatalist();
                            setDatas(mdatas);
                        }
                    }
                });
    }

    private void showEmpty(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }
    }


    private void setDatas(final List<MonitorParentBean> datalist) {
        final ArrayList<MultiItemEntity> res = new ArrayList<>();
        for (int i = 0; i < datalist.size(); i++) {//设置外层数据
            MonitorParentBean item = datalist.get(i);
            List<MonitorChildBean> subList = item.getMember_list();
            for (int k = 0; k < subList.size(); k++) {
                //设置内层数据
                item.addSubItem(i, subList.get(k));
            }
            res.add(item);
        }

        adapter = new FenZuAdapter(res);
        recyclerView.setAdapter(adapter);

        adapter.setCheckBoxListner(new FenZuAdapter.OnCheckBox() {
            @Override
            public void addImg(MonitorChildBean bean) {
                if (selectDatas.size() >= 5) {
                    ToastUtils.getInstanc(MonitorListActivity.this).showToast("最多只能有5个监督人！");
                    return;
                }
                if (!selectDatas.contains(bean)) {
                    selectDatas.add(bean);
                }
                reloadView(selectDatas);

                //checkBeanStr(bean, true);
            }

            @Override
            public void removeImg(MonitorChildBean bean) {
                Log.e("amtf", "减去监督人");
                if (selectDatas.contains(bean)) {
                    selectDatas.remove(bean);
                }
                reloadView(selectDatas);
            }

            @Override
            public void onSelect(MonitorChildBean bean, boolean isCheck) {

//                resetMydates(bean, isCheck);

            }
        });

//        adapter.expandAll();//默认展开全部 不写就不展开
    }

//    private void resetMydates(MonitorChildBean bean, boolean isCheck) {
//        final List<MultiItemEntity> mdates = adapter.getData();
//        try {
//            for (int i = 0; i < mdates.size(); i++) {
//                MonitorParentBean parentBean = (MonitorParentBean) mdates.get(i);
//                List<MonitorChildBean> childList = parentBean.getMember_list();
//                for (MonitorChildBean child : childList) {
//                    if (bean.getUser_id().equals(child.getUser_id())) {
//                        if (isCheck) {
//                            child.setCheckTag(1);
//                        } else {
//                            child.setCheckTag(0);
//                        }
//
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("amtf", "错误:" + e.getMessage());
//        }
//
//        mhandler.post(new Runnable() {
//            @Override
//            public void run() {
//                adapter = new FenZuAdapter(mdates);
//                recyclerView.setAdapter(adapter);
//            }
//        });
//
//    }

//    Handler mhandler = new Handler();

    private void reloadView(List<MonitorChildBean> selectDatas) {
        int len = selectDatas.size();
        for (int i = 0; i < len; i++) {
            if (i >= 5) {
                break;
            }

            MonitorChildBean item = selectDatas.get(i);
            RelativeLayout child = (RelativeLayout) llParent.getChildAt(i);
            child.setVisibility(View.VISIBLE);
            child.setTag(item.getUser_id());
            ImageView img = (ImageView) child.getChildAt(0);
            Glide.with(this).load(item.getUser().getHead()).into(img);
        }

        for (int k = 0; k < 5; k++) {
            if (k >= len) {
                llParent.getChildAt(k).setVisibility(View.GONE);
            }
        }
    }


    @OnClick(R.id.iv_back)
    public void goBack() {
        finish();
    }

    @OnClick(R.id.title_commit)
    public void goCommit() {
        if (selectDatas.size() == 0) {
            ToastUtils.getInstanc(this).showToast("请选择监督人！");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("monitor", "monitor");
        AppContacts.SELECT_Monitor = selectDatas;
        setResult(0x15, intent);
        finish();
    }


}





