package com.xinyu.newdiggtest.ui.calendar;

import android.app.Activity;
import android.util.Log;

import com.xinyu.newdiggtest.bean.AnswerBean;
import com.xinyu.newdiggtest.bean.DakaBottowItem;
import com.xinyu.newdiggtest.bean.FollowListBean;
import com.xinyu.newdiggtest.bean.FollowTestBean;
import com.xinyu.newdiggtest.bean.TargetplanBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class CommonLikesNetUtil {

    Activity mContext;

    CommonLikesNetUtil instance;

    public CommonLikesNetUtil getInstance(Activity context) {
        mContext = context;
        if (instance == null) {
            instance = new CommonLikesNetUtil();
        }
        return instance;
    }


    /**
     * 点赞
     *
     * @param item
     */
    public void dianZan(final FollowListBean item, final int type) {
        if (item != null) {

            RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
            AppUrl api = manager.create(AppUrl.class);

            HashMap<String, String> requsMap = new HashMap<>();
            requsMap.put("object_id", item.getF_plan_id());
            requsMap.put("like_user_id", PreferenceUtil.getInstance(mContext).getUserId());
            requsMap.put("type", "1");
            requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
            requsMap.put("target_name", MyTextUtil.getUrl1Encoe(item.getF_target_name()));
            requsMap.put("start_date", item.getF_target_start_date());
            requsMap.put("end_date", item.getF_target_end_date());
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
                            ToastUtils.getInstanc(mContext).showToast(e.getMessage());

                        }

                        @Override
                        public void onNext(Info msg) {
                            if (msg.getOp().getCode().equals("Y")) {
                                ToastUtils.getInstanc(mContext).showToast("点赞成功");

                                XshellEvent event = new XshellEvent(EventConts.HomeFresh);
                                event.msg = item.getF_plan_id();
                                event.type = type;

                                EventBus.getDefault().post(event);

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
    public void canCelZan(final FollowListBean item, final int type) {

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

                            Log.e("amtf", e.getMessage());

                        }

                        @Override
                        public void onNext(Info msg) {
                            if (msg.getOp().getCode().equals("Y")) {
                                ToastUtils.getInstanc(mContext).showToast("取消成功");

                                XshellEvent event = new XshellEvent(EventConts.HomeFresh);
                                event.msg = item.getF_plan_id();
                                event.type = type;
                                EventBus.getDefault().post(event);

                            } else {
                                ToastUtils.getInstanc(mContext).showToast("服务异常");
                            }
                        }
                    });


        }

    }


    /**
     * 添加用户评论
     */
    public void addComment(final FollowListBean commentBean, String comments, final int type) {
        if (commentBean != null) {
            String content = "";

            content = MyTextUtil.getUrl3Encoe(comments);
            RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
            AppUrl api = manager.create(AppUrl.class);


            HashMap<String, String> requsMap = new HashMap<>();
            requsMap.put("object_id", commentBean.getF_plan_id());
            requsMap.put("comment", content);
            requsMap.put("comment_user_id", PreferenceUtil.getInstance(mContext).getUserId());
            requsMap.put("type", "1");
            requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
            requsMap.put("target_name", MyTextUtil.getUrl2Encoe(commentBean.getF_target_name()));
            requsMap.put("start_date", commentBean.getF_target_start_date());
            requsMap.put("end_date", commentBean.getF_target_end_date());
            requsMap.put("nick_name", PreferenceUtil.getInstance(mContext).getNickName());
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

                                XshellEvent event = new XshellEvent(EventConts.HomeFresh);

                                event.msg = commentBean.getF_plan_id();

                                event.type = type;

                                EventBus.getDefault().post(event);

                            } else {
                                ToastUtils.getInstanc(mContext).showToast("评论失败");
                            }
                        }
                    });

        }
    }




    public void dianZan(FollowTestBean data, TargetplanBean targetInfo) {

        AnswerBean item = data.getAnswerBean();

        if (item != null) {

            RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
            AppUrl api = manager.create(AppUrl.class);

            HashMap<String, String> requsMap = new HashMap<>();
            requsMap.put("object_id", item.getF_answer_plan_id());
            requsMap.put("like_user_id", PreferenceUtil.getInstance(mContext).getUserId());
            requsMap.put("type", "1");
            requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
            //TODO 这些字段还没有,后面加上
            requsMap.put("target_name", MyTextUtil.getUrl2Encoe(targetInfo.getF_name()));
            requsMap.put("start_date", targetInfo.getF_start_date());
            requsMap.put("end_date", targetInfo.getF_end_date());
            requsMap.put("like_user_name", MyTextUtil.getUrl3Encoe(PreferenceUtil.getInstance(mContext).getNickName()));
            requsMap.put("user_id", item.getUser().getUser_id());
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
                                EventBus.getDefault().post(new XshellEvent(EventConts.HomeFresh));
                                //TODO 提示更新

                            } else {
                                ToastUtils.getInstanc(mContext).showToast("点赞失败");
                            }
                        }
                    });


        }

    }


    public void canCelZan(FollowTestBean data) {

        final AnswerBean item = data.getAnswerBean();
        if (item == null)
            return;

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

                            Log.e("amtf", e.getMessage());

                        }

                        @Override
                        public void onNext(Info msg) {
                            if (msg.getOp().getCode().equals("Y")) {
                                ToastUtils.getInstanc(mContext).showToast("取消成功");
                                //TODO 提示更新

                                XshellEvent event = new XshellEvent(EventConts.HomeFresh);
                                event.msg = item.getF_answer_plan_id();
                                EventBus.getDefault().post(event);

                            } else {
                                ToastUtils.getInstanc(mContext).showToast("服务异常");
                            }
                        }
                    });


        }

    }


    /**
     * 添加用户评论
     */
    public void addComment(final FollowTestBean commentBean, String comments, TargetplanBean targetInfo) {
        if (commentBean != null) {
            String content = "";
            try {
                content = URLEncoder.encode(comments, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
            AppUrl api = manager.create(AppUrl.class);

            AnswerBean item = commentBean.getAnswerBean();
            HashMap<String, String> requsMap = new HashMap<>();
            requsMap.put("object_id", item.getF_answer_plan_id());
            requsMap.put("comment", content);
            requsMap.put("comment_user_id", PreferenceUtil.getInstance(mContext).getUserId());
            requsMap.put("type", "1");
            requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

            //TODO 点赞跟目标相关的 暂时没有加字段
            requsMap.put("target_name", MyTextUtil.getUrl2Encoe(targetInfo.getF_name()));
            requsMap.put("start_date", targetInfo.getF_start_date());
            requsMap.put("end_date", targetInfo.getF_end_date());
            requsMap.put("nick_name", MyTextUtil.getUrl3Encoe(PreferenceUtil.getInstance(mContext).getNickName()));
            requsMap.put("user_id", item.getUser().getUser_id());
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

                                XshellEvent event = new XshellEvent(EventConts.HomeFresh);

                                event.msg = commentBean.getAnswerBean().getF_answer_plan_id();

                                EventBus.getDefault().post(event);

                            } else {
                                ToastUtils.getInstanc(mContext).showToast("评论失败");
                            }
                        }
                    });

        }
    }


}
