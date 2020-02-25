package com.xinyu.newdiggtest.utils;

import android.content.Context;
import android.util.Log;

import com.xinyu.newdiggtest.bean.CreateTargetBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.BaseSubscriber;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.XshellEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 公用api 接口（一个接口多处使用）
 */
public class NetApiUtil {

    /**
     * @param userId userid 被打赏的人的userid
     *               <p>
     *               type     打赏13，奖励14
     */
    public static void sendAppVipMissNotice(final Context context, String type, String userId) {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> maps = new HashMap<>();

        maps.put("type", type);
        maps.put("from_id", PreferenceUtil.getInstance(context).getUserId());//自己
        maps.put("user_id", userId);//别人


        url.creatVipMissMsg1(maps).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VipMsg>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("amtf", e.getMessage());

                    }

                    @Override
                    public void onNext(VipMsg msg) {
                        if (msg.getOp().getCode().equals("Y")) {
                            Log.w("amtf", "vip 消息发送成功");
                        }

                    }
                });

    }


    public static void readMsg(final Context ctx, String roomId, String topic) {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.ReadMessage");
        map.put("user_id", PreferenceUtil.getInstance(ctx).getUserId());
        map.put("topic", topic + roomId);
        map.put("deleteAll", "true");
        url.readGroupMsg(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.getInstanc(ctx).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        if (msg.getOp().getCode().equals("Y")) {
                            Log.w("amtf", "发送成功");

                        }
                    }
                });
    }


    public static class VipMsg {


        private OpBeanVip op;
        private DataBean data;

        public OpBeanVip getOp() {
            return op;
        }


        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class OpBeanVip {

            private String code;
            private int time;
            private String timestamp;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
                this.time = time;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }
        }

        public static class DataBean {
            /**
             * id : 650
             */

            private String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }


    /**
     * 新建字目标
     *
     * @param names
     * @param endTime
     * @param startTime
     * @param weeks
     * @param mornitors
     * @param showTime
     * @param fine
     * @param pid
     * @param rootId
     * @param ctx
     */
    public void createChildTarget(String names, String endTime, String startTime, String weeks, String mornitors,
                                  String showTime, String fine, String pid, String rootId, final Context ctx) {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("name", names);
//        map.put("class_id", getIntent().getStringExtra(IntentParams.Target_create_postion));
        if (!MyTextUtil.isEmpty(endTime)) {
            map.put("end_date", endTime);
        }

        if (!MyTextUtil.isEmpty(startTime)) {
            map.put("start_date", startTime);
        }
        if (!MyTextUtil.isEmpty(fine)) {
            map.put("fine", fine);
        }

        map.put("type", "2"); //目标完成类型：1：按次数设定；2：按周设定；3：按月设定
        map.put("repeat_date", weeks);
        map.put("sid", PreferenceUtil.getInstance(ctx).getSessonId());
        map.put("user_id", PreferenceUtil.getInstance(ctx).getUserId());
        map.put("is_share", "1");

        if (!MyTextUtil.isEmpty(mornitors)) {
            map.put("supervisor", mornitors);
        }

        if (!MyTextUtil.isEmpty(showTime)) {
            map.put("reminder_time", showTime);
        }
        if (!MyTextUtil.isEmpty(pid)) {
            map.put("pid", pid);
        }
        map.put("rid", rootId);

        url.createTarget(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<CreateTargetBean>(ctx) {
                    @Override
                    public void onNext(CreateTargetBean s) {
                        if (s.getOp().getCode().equals("Y")) {
                            if (s.getData() != null && !MyTextUtil.isEmpty(s.getData().getF_target_id())) {

                                EventBus.getDefault().post(new XshellEvent(EventConts.Creat_Child_Target));

                            }
                        } else {
                            ToastUtils.getInstanc(ctx).showToast("创建失败");
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtils.getInstanc(ctx).showToast("服务异常");
                    }
                });
    }


}
