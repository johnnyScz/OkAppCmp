package com.xinyu.newdiggtest.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.ninegrid.NineGridView;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.CommonDakaAdapter;

import com.xinyu.newdiggtest.bean.DakaBottowItem;
import com.xinyu.newdiggtest.bean.FocusBean;
import com.xinyu.newdiggtest.bean.FollowListBean;

import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;

import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.Digg.fragment.BaseFragment;

import com.xinyu.newdiggtest.ui.Digg.fragment.RewardActivity;

import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.NetApiUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.widget.SoftInputPopUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 通用相关打卡fragment
 */
public class CommonDakaFragment extends BaseFragment implements View.OnClickListener {

    Context ctx;

    RecyclerView recyclerView;//
    ImageView emputview;//

    FollowListBean commentBean;


    CommonDakaAdapter adapter;

    String param = "", enterType = "";


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {

        param = getArguments().getString("param");
        enterType = getArguments().getString("enterType");

        recyclerView = rootView.findViewById(R.id.chat_list);
//        freshLayout = rootView.findViewById(R.id.refreshLayout);
        emputview = rootView.findViewById(R.id.emputview);

        recyclerView.setNestedScrollingEnabled(false);

        initRecycle();

    }


    public static CommonDakaFragment newInstance(String info, String enterType) {
        Bundle args = new Bundle();
        CommonDakaFragment fragment = new CommonDakaFragment();
        args.putString("param", info);//网络请求的类型
        args.putString("enterType", enterType);//网络请求的类型
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

    private void requestFocusList(final int pos) {
        dialog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("target_uuid", param);
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        url.getTargetFocusList(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FocusBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(FocusBean msg) {
                        dialog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            List<FollowListBean> data = msg.getData();
                            if (data == null || data.size() == 0) {
                                showEmpyt(true);
                                return;
                            } else {
                                showEmpyt(false);
                                setDatas(data, pos);
                            }

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });

    }

    private void showEmpyt(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.GONE);
            emputview.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emputview.setVisibility(View.GONE);
        }

    }


    private void setDatas(final List<FollowListBean> data, int pos) {
        putToUserId(data);
        adapter = new CommonDakaAdapter(R.layout.item_daka, data, mContext);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                switch (id) {
                    case R.id.rl_item1:
                        ToastUtils.getInstanc(mContext).showToast("我被点击" + position);
                        break;

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
                if (enterType.equals("group_target")) {

                    if (MyTextUtil.isEmpty(item.getBecome_vip_date()) || item.getBecome_vip_date().equals("0")) {
                        NetApiUtil.sendAppVipMissNotice(mContext, "13", item.getF_executor());
                        ToastUtils.getInstanc().showToast("您的好友不是Vip会员，不能被打赏哦~");
                        return;
                    }
                    Intent intent = new Intent(mContext, RewardActivity.class);
                    intent.putExtra(IntentParams.Target_Name, item.getF_target_name());
                    intent.putExtra(IntentParams.DAKA_Target_Id, item.getF_plan_id());
                    intent.putExtra(IntentParams.STATE_DATE, item.getF_start_date());
                    intent.putExtra(IntentParams.END_DATE, item.getF_start_date());
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

                        doRequestComment(commentBean, comment);

                    }
                });

            }
        });

        recyclerView.setAdapter(adapter);


    }

    private void putToUserId(List<FollowListBean> data) {
        for (FollowListBean item : data) {
            if (MyTextUtil.isEmpty(item.getF_toUser())) {
                item.setF_toUser(item.getUser_id());
            }
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
            requsMap.put("target_name", item.getF_target_name());
            requsMap.put("start_date", item.getF_start_date());
            requsMap.put("end_date", item.getF_end_date());
            requsMap.put("like_user_name", PreferenceUtil.getInstance(mContext).getNickName());
            requsMap.put("user_id", item.getF_toUser());
            requsMap.put("command", "ok-api.InsertTargetLikes");

            api.addZan(requsMap).subscribeOn(Schedulers.io())
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


    @Override
    public void onStart() {
        super.onStart();
        requestFocusList(1);
        //TODO 请求数据
    }

    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器


//        freshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
//
//                mhandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        freshLayout.finishLoadMore();
//
//                        //TODO doing something
//
//                    }
//                }, 2000);
//            }
//        });


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {

    }


    @Override
    protected void reLoadData() {

    }

    @Override
    protected int getResId() {
        return R.layout.fragment_common_everywhere;
    }

    @Override
    public void onClick(View v) {

    }


    /**
     * 添加用户评论
     */
    private void doRequestComment(final FollowListBean commentBean, String comments) {
        if (commentBean != null) {
            String content = "";
            try {
                content = URLEncoder.encode(comments, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
            AppUrl api = manager.create(AppUrl.class);

            HashMap<String, String> requsMap = new HashMap<>();
            requsMap.put("object_id", commentBean.getF_plan_id());
            requsMap.put("comment", content);
            requsMap.put("comment_user_id", PreferenceUtil.getInstance(mContext).getUserId());
            requsMap.put("type", "1");
            requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
            requsMap.put("target_name", commentBean.getF_target_name());
            requsMap.put("start_date", commentBean.getF_start_date());
            requsMap.put("end_date", commentBean.getF_end_date());
            requsMap.put("nick_name", PreferenceUtil.getInstance(mContext).getNickName());
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
