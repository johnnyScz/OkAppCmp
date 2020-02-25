package com.xinyu.newdiggtest.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.ninegrid.NineGridView;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.CommonDakaAdapter;
import com.xinyu.newdiggtest.bean.DakaBottowItem;
import com.xinyu.newdiggtest.bean.FollowListBean;
import com.xinyu.newdiggtest.bean.SpaceDakaReturnBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.Digg.fragment.BaseFragment;
import com.xinyu.newdiggtest.ui.Digg.fragment.RewardActivity;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.NetApiUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.widget.SoftInputPopUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 个人空间打卡fragment
 */
public class SpaceDakaFragment extends BaseFragment {

    Context ctx;

    RecyclerView recyclerView;//

    ImageView emputview;//


    FollowListBean commentBean;


    CommonDakaAdapter adapter;

    String param = "", enterType = "";


    String become_vip_date = "";


    @Override
    protected void initView() {
        param = getArguments().getString("param");
        enterType = getArguments().getString("enterType");
        recyclerView = rootView.findViewById(R.id.chat_list);
        emputview = rootView.findViewById(R.id.emputview);
        initRecycle();

    }


    @Override
    public void onResume() {
        super.onResume();
        requestFocusList(1);

    }


    /**
     * 跳订单详情
     *
     * @param dakaInfo
     */
    private void goActy(FollowListBean dakaInfo) {
        Intent intent = new Intent(mContext, TargetNewInfoActivity.class);
        intent.putExtra(IntentParams.DAKA_Target_Id, dakaInfo.getF_target_uuid());

        startActivity(intent);
    }


    public static SpaceDakaFragment newInstance(String info, String enterType) {
        Bundle args = new Bundle();
        SpaceDakaFragment fragment = new SpaceDakaFragment();
        args.putString("param", info);
        args.putString("enterType", enterType);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        NineGridView.setImageLoader(new NineGridView.ImageLoader() {
            @Override
            public void onDisplayImage(Context context, ImageView imageView, String url) {
                Glide.with(context).load(url)
                        .into(imageView);
            }

            @Override
            public Bitmap getCacheImage(String url) {
                return null;
            }
        });
    }


    @Override
    protected void loadData() {

    }

    /**
     * 个人空间 请求我的打卡
     *
     * @param pos
     */
    private void requestFocusList(final int pos) {
        dialog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();

        requsMap.put("user_id", param);
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("page_num", "1");
        requsMap.put("page_size", "30");

        url.getSpaceDakatList(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SpaceDakaReturnBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(SpaceDakaReturnBean msg) {

                        dialog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData() == null) {
                                showEmpty(true);
                                return;
                            }
                            become_vip_date = msg.getData().getBecome_vip_date();
                            List<FollowListBean> data = msg.getData().getTargetexecute();
                            if (data == null || data.size() == 0) {
                                showEmpty(true);
                            } else {
                                showEmpty(false);
                                setDatas(data, pos, msg.getData());
                            }


                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });

    }

    private void showEmpty(boolean isShow) {
        if (isShow) {
            emputview.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emputview.setVisibility(View.GONE);
        }
    }


    private void setDatas(final List<FollowListBean> data, int pos, SpaceDakaReturnBean.DataBean date) {

        int vip = MyTextUtil.isEmpty(date.getBecome_vip_date()) || date.getBecome_vip_date().equals("0") ? 0 : 1;

        for (FollowListBean it : data) {
            it.setUser_id(date.getUser_id());
            it.setNick_name(date.getNick_name());
            it.setHead(date.getHead());
            it.setIsVip(vip);
        }

        adapter = new CommonDakaAdapter(R.layout.item_daka1, data, mContext);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.tv_daka_title) {
                    goActy(data.get(position));
                }
            }
        });


        adapter.setPopListner(new CommonDakaAdapter.onPopClick() {
            @Override
            public void onZanClick(FollowListBean item, String content) {
                if (content.equals("赞")) {
                    dianZan(item);
                } else if (content.equals("取消")) {
                    canCelZan(item);
                }

            }

            @Override
            public void onDaShangClick(FollowListBean item) {

                if (enterType.equals("space_daka")) {
                    if (MyTextUtil.isEmpty(become_vip_date)) {
                        NetApiUtil.sendAppVipMissNotice(mContext, "13", item.getF_executor());
                        ToastUtils.getInstanc().showToast("您的好友不是Vip用户，不能被打赏哦~");
                        return;
                    }

                    Intent intent = new Intent(mContext, RewardActivity.class);
                    intent.putExtra(IntentParams.DAKA_Target_Id, item.getF_plan_id());

                    String name = MyTextUtil.isEmpty(item.getF_target_name()) ? item.getF_title() : item.getF_target_name();

                    intent.putExtra(IntentParams.Target_Name, MyTextUtil.getDecodeStr(name));

                    if (!MyTextUtil.isEmpty(item.getF_start_date())) {
                        intent.putExtra(IntentParams.STATE_DATE, item.getF_start_date());
                    } else {
                        intent.putExtra(IntentParams.STATE_DATE, DateUtil.getCurrentData());
                    }

                    if (!MyTextUtil.isEmpty(item.getF_end_date())) {
                        intent.putExtra(IntentParams.END_DATE, item.getF_end_date());
                    } else {
                        intent.putExtra(IntentParams.END_DATE, DateUtil.getCurrentData());
                    }

                    intent.putExtra(IntentParams.Intent_Enter_Type, "daka_dashang");//打卡打赏
                    intent.putExtra(IntentParams.TO_USER, item.getF_executor());
                    mContext.startActivity(intent);

                }

            }

            @Override
            public void onCommentClick(FollowListBean item, View view) {
                commentBean = item;


                SoftInputPopUtil.liveCommentEdit(mContext, view, new SoftInputPopUtil.liveCommentResult() {
                    @Override
                    public void onResult(boolean confirmed, String comment) {

                        doRequestComment(comment);

                    }
                });

            }
        });
        recyclerView.setAdapter(adapter);
        if (adapter.getData().size() > 2) {
            recyclerView.scrollToPosition(pos - 2);
        }
    }


    /**
     * 点赞
     *
     * @param item
     */
    private void dianZan(final FollowListBean item) {
        if (item != null) {
            RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
            AppUrl api = manager.create(AppUrl.class);

            HashMap<String, String> requsMap = new HashMap<>();
            requsMap.put("object_id", item.getF_plan_id());
            requsMap.put("like_user_id", PreferenceUtil.getInstance(mContext).getUserId());
            requsMap.put("type", "1");
            requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

            if (!MyTextUtil.isEmpty(item.getF_target_name())) {
                requsMap.put("target_name", MyTextUtil.getUrl2Encoe(item.getF_target_name()));
                requsMap.put("start_date", item.getF_start_date());
                requsMap.put("end_date", item.getF_end_date());
            } else {
                requsMap.put("target_name", "");
                requsMap.put("start_date", "");
                requsMap.put("end_date", "");
            }
            requsMap.put("like_user_name", MyTextUtil.getUrl3Encoe(PreferenceUtil.getInstance(mContext).getNickName()));
            requsMap.put("user_id", item.getF_executor());
            requsMap.put("command", "ok-api.InsertTargetLikes");
            api.addZan(requsMap).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Info>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("amtf", e.getMessage());
                        }

                        @Override
                        public void onNext(Info msg) {
                            if (msg.getOp().getCode().equals("Y")) {
                                ToastUtils.getInstanc(mContext).showToast("点赞成功");

                                requestFocusList(item.getEditPos());

                            } else {
                                ToastUtils.getInstanc(mContext).showToast("点赞失败");
                            }
                        }
                    });
        }


    }


    /**
     * 取消点赞
     *
     * @param item
     */
    private void canCelZan(final FollowListBean item) {

        List<DakaBottowItem> fidList = item.getTargetlikes();
        String fid = "";
        for (DakaBottowItem ta : fidList) {
            if (ta.getF_like_user().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                fid = ta.getF_uuid();
                break;
            }

        }

        if (item != null) {

            RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
            AppUrl api = manager.create(AppUrl.class);

            HashMap<String, String> requsMap = new HashMap<>();
            requsMap.put("likes_uuid", fid);
            requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
            api.cancelZan(requsMap).subscribeOn(Schedulers.io())
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
                                ToastUtils.getInstanc(mContext).showToast("取消成功");
                                requestFocusList(item.getEditPos());
                            } else {
                                ToastUtils.getInstanc(mContext).showToast("服务异常");
                            }
                        }
                    });


        }


    }


    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {

    }


    @Override
    protected void reLoadData() {

    }

    @Override
    protected int getResId() {
        return R.layout.fragment_space_private;
    }


    /**
     * 添加用户评论
     */
    private void doRequestComment(String comments) {
        if (commentBean != null) {

            RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
            AppUrl api = manager.create(AppUrl.class);

            HashMap<String, String> requsMap = new HashMap<>();
            requsMap.put("object_id", commentBean.getF_plan_id());
            requsMap.put("comment", MyTextUtil.getUrl3Encoe(comments));
            requsMap.put("comment_user_id", PreferenceUtil.getInstance(mContext).getUserId());
            requsMap.put("type", "1");
            requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

            if (!MyTextUtil.isEmpty(commentBean.getF_target_name())) {
                requsMap.put("target_name", MyTextUtil.getUrl2Encoe(commentBean.getF_target_name()));
                requsMap.put("start_date", commentBean.getF_start_date());
                requsMap.put("end_date", commentBean.getF_end_date());
            } else {
                requsMap.put("target_name", "");
                requsMap.put("start_date", "");
                requsMap.put("end_date", "");
            }

            requsMap.put("nick_name", MyTextUtil.getUrl3Encoe(PreferenceUtil.getInstance(mContext).getNickName()));
//            requsMap.put("user_id", commentBean.getF_toUser());
            requsMap.put("user_id", commentBean.getF_executor());
            requsMap.put("command", "ok-api.InsertTargetComment");
            api.addComments(requsMap).subscribeOn(Schedulers.io())
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
                                ToastUtils.getInstanc(mContext).showToast("评论成功");
                                requestFocusList(commentBean.getEditPos());

                            } else {
                                ToastUtils.getInstanc(mContext).showToast("评论失败");
                            }
                        }
                    });

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
