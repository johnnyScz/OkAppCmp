package com.xinyu.newdiggtest.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.AnswerBean;
import com.xinyu.newdiggtest.bean.DingReturnBean;
import com.xinyu.newdiggtest.bean.FollowTestBean;
import com.xinyu.newdiggtest.bean.TargetplanBean;
import com.xinyu.newdiggtest.bean.TodoDingBean;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.Digg.fragment.RewardActivity;
import com.xinyu.newdiggtest.ui.calendar.CommonLikesNetUtil;
import com.xinyu.newdiggtest.ui.calendar.NoDelectAdapter;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.NetApiUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.widget.SoftInputPopUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 目标详情相关打卡
 */
public class TargetInfoDakafragment extends LazySingleFragment {

    Context ctx;

    @BindView(R.id.chat_list)
    RecyclerView recyclerView;//

    @BindView(R.id.emputview)
    ImageView emputview;//

    NoDelectAdapter adapter;

    List<FollowTestBean> datalist;

    String targetId = "";

    TargetplanBean targetInfo;

    public void setTargetInfo(TargetplanBean info) {
        this.targetInfo = info;
    }


    public void setTargetId(String uid) {
        this.targetId = uid;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_common_everywhere;
    }

    @Override
    protected void initView() {
        initRecycle();
    }

    @Override
    public void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            requestDatas();
        }
    }

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == EventConts.HomeFresh) {
            requestDatas();
        }

    }


    private void requestDatas() {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("target_uuid", targetId);
        map.put("command", "ok-api.SelectExecuteForTarget");
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        url.getTargetInfoDakaList(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DingReturnBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(DingReturnBean msg) {

                        String code = msg.getOp().getCode();
                        if (code.equals("Y")) {
                            if (msg.getData() == null || msg.getData().size() < 1) {
                                setDatas(null);
                                return;
                            }
                            uiLoad(msg.getData());

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });

    }

    private void uiLoad(List<TodoDingBean> data) {
        datalist = new ArrayList<>();
        datalist.clear();
        for (TodoDingBean item : data) {
            List<AnswerBean> answerlist = item.getAnswer();
            if (answerlist != null && answerlist.size() > 0) {
                for (AnswerBean anserbean : answerlist) {
                    FollowTestBean ff = new FollowTestBean();
                    ff.setViewType(1);
                    ff.setAskType(1);
                    ff.setAnswerBean(anserbean);
                    datalist.add(ff);
                }
            }
        }
        if (datalist.size() > 0) {
            setDatas(datalist);
        }
    }


    private void setDatas(List<FollowTestBean> msg) {
        if (msg == null || msg.size() == 0) {
            showEmpty(true);

        } else {
            showEmpty(false);
            fillData(msg);
        }


    }

    private void showEmpty(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.GONE);
            emputview.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emputview.setVisibility(View.GONE);
        }
    }

    /**
     * 填充数据
     */
    private void fillData(List<FollowTestBean> data) {
        adapter = new NoDelectAdapter(data, mContext);
        recyclerView.setAdapter(adapter);

        View footer = LayoutInflater.from(mContext).inflate(R.layout.test_footer, null);
        adapter.setFooterView(footer);

        setCallbacks();

    }

    private void setCallbacks() {
        final CommonLikesNetUtil api = new CommonLikesNetUtil().getInstance(mContext);
        adapter.setPopListner(new NoDelectAdapter.PopClick() {
            @Override
            public void onZanClick(FollowTestBean item, String content) {

                if (content.equals("赞")) {

                    api.dianZan(item, targetInfo);

                } else if (content.equals("取消")) {

                    api.canCelZan(item);

                }

            }

            @Override
            public void onCommentClick(final FollowTestBean item, final View view) {

                SoftInputPopUtil.liveCommentEdit(mContext, view, new SoftInputPopUtil.liveCommentResult() {
                    @Override
                    public void onResult(boolean confirmed, String comment) {
                        api.addComment(item, comment, targetInfo);
                    }
                });


            }

            @Override
            public void onDashagn(FollowTestBean item) {

                if (MyTextUtil.isEmpty(targetInfo.getBecome_vip_date())) {
                    ToastUtils.getInstanc().showToast("您的好友不是vip用户，不能被打赏哦~");
                    NetApiUtil.sendAppVipMissNotice(mContext, "13", targetInfo.getF_createuser());

                } else {
                    Intent intent = new Intent(mContext, RewardActivity.class);
                    intent.putExtra(IntentParams.Target_Name, MyTextUtil.getDecodeStr(targetInfo.getF_name()));
                    intent.putExtra(IntentParams.DAKA_Target_Id, item.getAnswerBean().getF_answer_plan_id());
                    intent.putExtra(IntentParams.STATE_DATE, targetInfo.getF_start_date());
                    intent.putExtra(IntentParams.END_DATE, targetInfo.getF_end_date());
                    intent.putExtra(IntentParams.Intent_Enter_Type, "daka_dashang");//打卡打赏
                    intent.putExtra(IntentParams.TO_USER, targetInfo.getF_createuser());
                    mContext.startActivity(intent);
                }

            }
        });


    }


    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
    }


}
