package com.xinyu.newdiggtest.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.ChanelAdapter;
import com.xinyu.newdiggtest.app.HomeAppActivity;
import com.xinyu.newdiggtest.bean.ChanelBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.Digg.fragment.BaseFragment;
import com.xinyu.newdiggtest.ui.chanel.TopicChanelActivity;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 频道
 */
public class ChannelFragment extends BaseFragment {
    HomeAppActivity mContext;


    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    ChanelAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (HomeAppActivity) context;
    }


    @Override
    protected void initView() {
        init();
    }

    @Override
    protected void loadData() {
        if (adapter == null) {
            initDatas();
        }
    }

    @Override
    protected void reLoadData() {

    }

    @Override
    protected int getResId() {
        return R.layout.channel_layout;
    }

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {

        if (event.what == EventConts.SoCket_Push_Channel) {
            JSONObject data = event.object;

            checkJson(data);
        }

    }


    private void checkJson(JSONObject json) {

        Log.e("amtf", "ChannelFragment推送数据:" + json.toString());

        try {

            if (!json.toString().contains("type")) {
                return;
            }

            String op = json.getString("op");

            String type = json.getString("type");


            /**
             * 频道相关最新的
             */
            if (op.equals("notice") && type.equals("channel_topic")) {

                String channelId = json.getJSONObject("data").getString("channel_id");

                freshChanneList(channelId);
            }


        } catch (JSONException e) {
            Log.e("amtf", "json异常：" + e.getMessage());

            e.printStackTrace();
        }

    }


    /**
     * 当前的channel 信息+1
     *
     * @param channelId
     */
    private void freshChanneList(String channelId) {

        if (adapter != null) {

            List<ChanelBean.ChanelChilcBean> data = adapter.getData();

            if (data != null && data.size() > 0) {

                int len = data.size();

                for (int k = 0; k < len; k++) {

                    if (data.get(k).getChannelid().equals(channelId)) {
                        addCount(data.get(k), k);
                        break;
                    }
                }


            }
        }
    }


    /**
     * 重置
     *
     * @param pos
     */
    private void resetCuont(int pos) {
        adapter.getData().get(pos).setMsgCount(0);
        adapter.notifyItemChanged(pos);

    }


    private void addCount(ChanelBean.ChanelChilcBean item, int pos) {
        int count = item.getMsgCount();
        item.setMsgCount(++count);
        adapter.notifyItemChanged(pos);
    }


    private void initDatas() {

        dialog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();

        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());


        //ChanelBean
        url.getPublicChanel(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChanelBean>() {
                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {


                    }

                    @Override
                    public void onNext(ChanelBean msg) {


                        if (msg.getOp().getCode().equals("Y")) {

                            List<ChanelBean.ChanelChilcBean> datas = msg.getData();

                            if (datas != null && datas.size() > 0) {

                                adapter = new ChanelAdapter(R.layout.item_chanel, datas);

                                recyclerView.setAdapter(adapter);

                                adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                    @Override
                                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                                        resetCuont(position);

                                        ChanelBean.ChanelChilcBean data = (ChanelBean.ChanelChilcBean) adapter.getData().get(position);

                                        Intent intent = new Intent(mContext, TopicChanelActivity.class);

                                        intent.putExtra("chanel_id", data.getChannelid());

                                        intent.putExtra("createdtime", data.getCreatedtime());
                                        intent.putExtra("description", data.getDescription());
                                        intent.putExtra("channelName", data.getChannelname());
                                        intent.putExtra("groupname", data.getGroupname());
                                        intent.putExtra("createduserid", data.getCreateduserid());

                                        startActivity(intent);
                                    }
                                });


                            }
                        }
                    }
                });
    }


}
