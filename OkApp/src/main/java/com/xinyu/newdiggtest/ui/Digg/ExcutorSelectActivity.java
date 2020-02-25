package com.xinyu.newdiggtest.ui.Digg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.ChooseAdapter;
import com.xinyu.newdiggtest.adapter.ExcutorSelectAdapter;
import com.xinyu.newdiggtest.bean.MonitorChildBean;
import com.xinyu.newdiggtest.bean.MonitorListBean;
import com.xinyu.newdiggtest.bean.MonitorParentBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.seriable.SeletExcutor;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ExcutorSelectActivity extends BaseNoEventActivity {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.horiz_recycle)
    public RecyclerView horiRecycle;


    @BindView(R.id.title_name)
    public TextView title_name;


    @BindView(R.id.emputview)
    public View empty;


    List<MonitorChildBean> allDatas = new ArrayList<>();

    List<MonitorChildBean> selectDatas = new ArrayList<>();//被选中的

    ExcutorSelectAdapter adapter;

    ChooseAdapter choseAdapter;

    List<String> selectUserid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//横向RecycleView
        if (getIntent().hasExtra(IntentParams.Intent_Enter_Type)
                && getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("target")) {
            title_name.setText("选择监督人");
        }

        initHrizonRecy();

        getDatas();

        selectUserid = getIntent().getStringArrayListExtra("befor_select");//之前选好的
    }


    private void initHrizonRecy() {
        LinearLayoutManager ms = new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        horiRecycle.setLayoutManager(ms);
        choseAdapter = new ChooseAdapter(R.layout.item_choose, selectDatas);
        horiRecycle.setAdapter(choseAdapter);

        choseAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MonitorChildBean data = selectDatas.get(position);
                reduce2notifyOtherRecycleview(data);
            }
        });
    }


    /**
     * 删除当前并通知好友列表
     *
     * @param data
     */
    private void reduce2notifyOtherRecycleview(MonitorChildBean data) {

        notifyReduce(data);
        adapter.setBeforSelct(selectUserid);

        int len = allDatas.size();

        for (int i = 0; i < len; i++) {
            if (allDatas.get(i).getUser_id().equals(data.getUser_id())) {
                allDatas.get(i).setCheckTag(0);
                adapter.notifyItemChanged(i);
                break;
            }
        }


    }


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_excutor;
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

                            List<MonitorParentBean> parentData = msg.getDatalist();
                            setDatas(parentData);
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
        allDatas.clear();
        for (int i = 0; i < datalist.size(); i++) {
            MonitorParentBean item = datalist.get(i);
            List<MonitorChildBean> subList = item.getMember_list();
            for (int k = 0; k < subList.size(); k++) {
                check2PutData(subList.get(k));
            }

        }

        if (getIntent().hasExtra(IntentParams.Intent_Enter_Type)) {
            clearMyself();
        }

        adapter = new ExcutorSelectAdapter(R.layout.item_item, allDatas);

        if (selectUserid != null && selectUserid.size() > 0) {
            adapter.setBeforSelct(selectUserid);

            findOutSeltctTop(selectUserid, allDatas);

        }

        recyclerView.setAdapter(adapter);

        adapter.setCheckBoxListner(new ExcutorSelectAdapter.OnCheckBoxListner() {
            @Override
            public void onNoSelect(MonitorChildBean bean) {
                if (!selectDatas.contains(bean)) {
                    selectDatas.add(bean);
                    notifyData();
                }
            }

            @Override
            public void onSelect(MonitorChildBean bean) {

                notifyReduce(bean);

            }
        });

    }

    private void findOutSeltctTop(List<String> selectUserid, List<MonitorChildBean> allDatas) {

        for (String selct : selectUserid) {
            for (MonitorChildBean item : allDatas) {
                if (selct.equals(item.getUser().getUser_id())) {
                    selectDatas.add(item);
                    break;
                }
            }
        }

        if (selectDatas.size() > 0)
            choseAdapter.notifyDataSetChanged();

    }

    private void clearMyself() {

        if (allDatas.size() < 1) {
            return;
        }

        int len = allDatas.size();
        for (int i = len - 1; i >= 0; i--) {
            if (allDatas.get(i).getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                allDatas.remove(i);
                break;
            }
        }
    }

    private void notifyReduce(MonitorChildBean bean) {
        int len = selectDatas.size();
        for (int i = len - 1; i >= 0; i--) {
            if (selectDatas.get(i).getUser_id().equals(bean.getUser_id())) {
                selectDatas.remove(i);
                break;
            }
        }

        if (selectUserid != null) {
            selectUserid.clear();
        } else {
            selectUserid = new ArrayList<>();
        }

        for (MonitorChildBean item : selectDatas) {
            selectUserid.add(item.getUser().getUser_id());
        }

        notifyData();
    }

    /**
     * 刷新横向recycleview
     */
    private void notifyData() {
        choseAdapter.notifyDataSetChanged();
    }

    int isHave = -1;

    private void check2PutData(MonitorChildBean itemData) {
        isHave = -1;
        int len = allDatas.size();
        if (len < 1) {
            allDatas.add(itemData);
        } else {
            for (MonitorChildBean tt : allDatas) {
                if (tt.getUser_id().equals(itemData.getUser_id())) {
                    isHave = 1;
                }
            }
            if (isHave == -1) {
                allDatas.add(itemData);
            }

        }

    }


    @OnClick(R.id.iv_back)
    public void goBack() {
        finish();
    }

    @OnClick(R.id.title_commit)
    public void goCommit() {

        if (selectDatas.size() == 0 && getIntent().hasExtra(IntentParams.Intent_Have_FINE)) {
            ToastUtils.getInstanc().showToast("你已设置挑战金，请至少设置一位监督人!");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("monitor", "monitor");


        List<SeletExcutor> datalist = getList(selectDatas);

        intent.putExtra("selectExcutor", (Serializable) datalist);

        setResult(0x33, intent);
        finish();
    }

    private List<SeletExcutor> getList(List<MonitorChildBean> selectDatas) {
        List<SeletExcutor> list = new ArrayList<>();

        if (selectDatas == null || selectDatas.size() == 0) {
            return list;
        }
        for (MonitorChildBean data : selectDatas) {
            SeletExcutor excutor = new SeletExcutor(data.getUser().getNickname(), data.getUser().getUser_id());
            list.add(excutor);

        }
        return list;
    }


}





