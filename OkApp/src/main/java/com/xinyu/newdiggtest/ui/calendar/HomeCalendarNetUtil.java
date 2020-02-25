package com.xinyu.newdiggtest.ui.calendar;

import android.content.Context;

import com.xinyu.newdiggtest.bean.DingRetBean;
import com.xinyu.newdiggtest.bean.DingReturnBean;
import com.xinyu.newdiggtest.bean.FollowListBean;
import com.xinyu.newdiggtest.bean.HomeMsgRetBean;
import com.xinyu.newdiggtest.bean.MsgNewHomeBean;
import com.xinyu.newdiggtest.bean.PersonChatBean;
import com.xinyu.newdiggtest.bean.RoomPersonListBean;
import com.xinyu.newdiggtest.bean.TodoDingBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.utils.PreferenceUtil;


import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HomeCalendarNetUtil {

    Context mContext;

    HomeCalendarNetUtil instance;

    public HomeCalendarNetUtil getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new HomeCalendarNetUtil();
        }
        return instance;
    }


    /**
     * 私信列表
     */
    public void queryWxList(String pageNo, String pageCount) {
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.AppFindRoomList");
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("room_type", "S");
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("curPageNo", pageNo);
        map.put("pageRowCnt", pageCount);

        url.personChatList(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PersonChatBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        if (dataListner != null) {
                            dataListner.onSxData(null);
                        }
                    }

                    @Override
                    public void onNext(PersonChatBean msg) {

                        if (msg.getOp() == null) {
                            if (dataListner != null) {
                                dataListner.onSxData(null);
                            }
                            return;
                        }
                        if (msg.getOp().getCode().equals("Y")) {
                            List<RoomPersonListBean> list = msg.getRoom_list();
                            if (dataListner != null) {
                                dataListner.onSxData(list);
                            }
                        }
                    }
                });
    }


    /**
     * 消息列表
     *
     * @param day
     */
    public void getLatestMsg(String day) {

        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "ok-api.queryByDay");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("day", day);
//        requsMap.put("topics", "");
        url.getHomeMsg(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeMsgRetBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (dataListner != null) {
                            dataListner.onMsgFeed(null);
                        }
                    }

                    @Override
                    public void onNext(HomeMsgRetBean msg) {
                        if (msg.getOp().getCode().equals("Y")) {

                            List<MsgNewHomeBean> dates = msg.getData();
                            if (dataListner != null) {
                                dataListner.onMsgFeed(dates);
                            }

                        } else {
                            if (dataListner != null) {
                                dataListner.onMsgFeed(null);
                            }
                        }
                    }
                });

    }


    /**
     * 关注列表
     *
     * @param dateStr
     */
    public void getFocusList(String dateStr) {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("plan_date", dateStr);
        map.put("page_num", "1");
        map.put("page_size", "20");
        //
        url.getFoucsPerson2Target(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DingRetBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (dataListner != null) {
                            dataListner.onFocusData(null);
                        }
                    }

                    @Override
                    public void onNext(DingRetBean msg) {

                        if (msg.getOp() == null) {
                            if (dataListner != null) {
                                dataListner.onFocusData(null);
                            }
                            return;
                        }
                        if (msg.getOp().getCode().equals("Y")) {
                            List<FollowListBean> list = msg.getData();

                            if (dataListner != null) {
                                dataListner.onFocusData(list);
                            }

                        }
                    }
                });
    }


    public void getTodoList(String dateStr) {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("plan_date", dateStr);
        map.put("command", "ok-api.SelectTargetExecuteList");

        url.getTodoList(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DingReturnBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (dataListner != null) {
                            dataListner.onTodoData(null);
                        }
                    }

                    @Override
                    public void onNext(DingReturnBean msg) {

                        if (msg.getOp() == null) {
                            if (dataListner != null) {
                                dataListner.onTodoData(null);
                            }
                            return;
                        }
                        if (msg.getOp().getCode().equals("Y")) {
                            List<TodoDingBean> list = msg.getData();
                            if (dataListner != null) {
                                dataListner.onTodoData(list);
                            }

                        }
                    }
                });
    }


    OnPuchDataListner dataListner;

    public void setDataListner(OnPuchDataListner listner) {
        this.dataListner = listner;
    }

    public interface OnPuchDataListner {

        void onSxData(List<RoomPersonListBean> list);


        void onMsgFeed(List<MsgNewHomeBean> dataList);

        void onFocusData(List<FollowListBean> list);

        void onTodoData(List<TodoDingBean> list);


    }


}
