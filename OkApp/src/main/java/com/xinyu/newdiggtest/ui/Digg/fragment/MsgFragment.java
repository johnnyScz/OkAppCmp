package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.MsgAdapter;

import com.xinyu.newdiggtest.bean.HomeMsgBean;
import com.xinyu.newdiggtest.bean.MsgRedBean;
import com.xinyu.newdiggtest.bean.MsgRetBean;

import com.xinyu.newdiggtest.net.AppUrl;

import com.xinyu.newdiggtest.net.RetrofitServiceManager;

import com.xinyu.newdiggtest.ui.TargetNewInfoActivity;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 消息的Fragment
 */

public class MsgFragment extends BaseFragment implements View.OnClickListener {

    public EasyRecyclerView recyView;

    SmartRefreshLayout refreshLayout;

    MsgAdapter adapter;

    ImageView emptyView;

    String tagNo;

    int pageNum = 1;


    @Override
    protected void initView() {
        tagNo = getArguments().getString("tag");
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        recyView = rootView.findViewById(R.id.recyclerView);
        initAdapter();
        emptyView = rootView.findViewById(R.id.emputview);
    }


    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器


    }


    /**
     * Activity 通知更新数据
     *
     * @param type
     */
    public void refreshData(int type) {
        if (type == 1) {
            requestNetTest();
        }
    }

    /**
     * 跳到目标详情
     *
     * @param bean
     */
    private void goTargetDitail(HomeMsgBean bean) {

        Intent intent = new Intent(mContext, TargetNewInfoActivity.class);
        intent.putExtra(IntentParams.DAKA_Target_Id, bean.getAim_id());
        mContext.startActivity(intent);

    }


    private void readTheMsg(final HomeMsgBean data) {
//        RetrofitServiceManager manager = RetrofitServiceManager.getInstance(true);
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", data.getId());
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());

        Observable<MsgRedBean> obsever;

        if (true) {
            obsever = url.readMsg(map);
        } else {
            obsever = url.readMsg1(map);
        }

        obsever.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MsgRedBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(MsgRedBean msg) {
                        if (msg.getOp().getCode().equals("Y")) {

                            notifyCount1Reduce();
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });
    }


    //通知未读消息减1
    private void notifyCount1Reduce() {
        if (mListener != null) {
            mListener.onFragmentInteraction("notice");

        }


    }

    public static MsgFragment newInstance(String info) {
        Bundle args = new Bundle();
        MsgFragment fragment = new MsgFragment();
        args.putString("tag", info);
        fragment.setArguments(args);
        return fragment;
    }


    private void requestNetTest() {
        dialog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("page", pageNum + "");
        map.put("page_size", "30");
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());

        Observable<MsgRetBean> obs;
        if (true) {
            obs = url.getMsgList(map);
        } else {
            obs = url.getMsgList1(map);
        }
        obs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MsgRetBean>() {
                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(MsgRetBean msgRetBean) {
                        dialog.dismiss();
                        if (msgRetBean.getOp().getCode().equals("Y")) {
                            List<HomeMsgBean> dataList = msgRetBean.getData();
                            if (dataList == null || dataList.size() == 0) {
                                showEmpty(true);
                            } else {
                                showEmpty(false);
                                showDatas(dataList);
                            }
                        }
                    }
                });

    }

    private void showDatas(final List<HomeMsgBean> dataList) {
        adapter = new MsgAdapter(dataList);
        recyView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                HomeMsgBean data = dataList.get(position);

                if (data.getIs_read().equals("0")) {
                    readTheMsg(data);
                }
                if (data.getType().equals("17")) {
                    notifyDisDot(position);

                } else {
                    if (data.getRated().equals("1")) {
                        goTargetDitail(data);

                    } else {
                        goIntent(data);
                    }
                }


            }
        });


    }

    private void notifyDisDot(int position) {
        adapter.getData().get(position).setIs_read("1");
        adapter.notifyItemChanged(position);
    }


    private void showEmpty(boolean show) {
        if (show) {
            refreshLayout.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            refreshLayout.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }


    private void goIntent(HomeMsgBean bean) {

        String type = bean.getType();

        switch (type) {

            case "1"://点赞目标
            case "6"://关注目标
            case "7"://奖励目标
            case "2"://评论目标
                goTargetDitail(bean);
                break;


            case "3"://打卡点赞
            case "4"://打卡评论
            case "8"://打卡打赏
                goDakaDetail(bean);
                break;


            case "5"://关注了你,去他的个人空间
                goSelfSpace(bean);

                break;


            case "11":
            case "12":
                goTiaozhanFinish(bean);
                break;
            case "13":
            case "14":
                goVp(bean);
                break;


        }


    }

    private void goVp(HomeMsgBean bean) {
        Intent intent = new Intent(mContext, VipIntroduceActivity.class);
        intent.putExtra(IntentParams.IsVIP, "N");
        startActivity(intent);
    }

    /**
     * 跳打卡详情
     *
     * @param bean
     */
    private void goDakaDetail(HomeMsgBean bean) {

//        Intent mIntent = new Intent(mContext, MySpaceActivity.class);
//
//        mIntent.putExtra(IntentParams.USER_ID, bean.getFrom_id());
//        mIntent.putExtra(IntentParams.USER_head, bean.getFrom_head());
//        mIntent.putExtra(IntentParams.UserName, bean.getFrom_nickname());
//        mIntent.putExtra(IntentParams.STATE, "0");
//
//        startActivity(mIntent);


    }

    /**
     * 去他的个人空间
     *
     * @param bean
     */
    private void goSelfSpace(HomeMsgBean bean) {
//        Intent mIntent = new Intent(mContext, MySpaceActivity.class);
//        mIntent.putExtra(IntentParams.USER_ID, bean.getFrom_id());
//        mIntent.putExtra(IntentParams.USER_head, bean.getFrom_head());
//        mIntent.putExtra(IntentParams.UserName, bean.getFrom_nickname());
//        mIntent.putExtra(IntentParams.STATE, "0");
//        startActivity(mIntent);

    }

    private void goTiaozhanFinish(HomeMsgBean bean) {
        Intent intent = new Intent(mContext, TargetCheckActivity.class);
        intent.putExtra("headUrl", bean.getFrom_head());
        intent.putExtra("fromUser", bean.getFrom_id());
        intent.putExtra("targetName", bean.getAim_name());
        intent.putExtra("startTime", bean.getAim_start_time());
        intent.putExtra("endTime", bean.getAim_end_time());
        intent.putExtra("aim_id", bean.getAim_id());
        intent.putExtra("rate_type", bean.getRate_type());
        intent.putExtra("aim_money", bean.getMoney());
        intent.putExtra("nick_name", bean.getFrom_nickname());
        intent.putExtra("name_content", bean.getName());
        mContext.startActivity(intent);
    }


    @Override
    protected void loadData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (tagNo.equals("2")) {
            requestNetTest();
        }
    }

    @Override
    protected void reLoadData() {

    }

    @Override
    protected int getResId() {
        return R.layout.fragment_test;
    }


    @Override
    public void onClick(View v) {

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String data);
    }


}
