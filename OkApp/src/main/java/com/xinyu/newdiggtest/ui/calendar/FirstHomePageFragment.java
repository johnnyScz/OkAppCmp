package com.xinyu.newdiggtest.ui.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.HomeMsgCountBean;
import com.xinyu.newdiggtest.bean.MsgCountRetBean;

import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.Digg.fragment.BaseFragment;
import com.xinyu.newdiggtest.ui.Digg.fragment.FeixinActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.GroupBusiActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.NoreadMsgActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;

import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FirstHomePageFragment extends BaseFragment {

    @BindView(R.id.msg_tip)
    TextView msg_tip;

    @BindView(R.id.conent_todo)
    TextView conent_todo;

    @BindView(R.id.content2)
    TextView content2;//群组消息

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.time2)
    TextView time2;

    String msgCount = "0";


    //TODO 消息数量

    @BindView(R.id.todo_count)
    TextView todo_count;

    @BindView(R.id.group_count)
    TextView group_count;

    @BindView(R.id.msg_fx)
    TextView msg_fx;


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
    protected void initView() {

        //TODO 后面换成banner

//        BannerFragment fragment = new BannerFragment();
//
//        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.banner_ly, fragment).commit();

    }

    @Override
    protected void loadData() {


    }


    @Override
    public void onResume() {
        super.onResume();

        queryMsgNum();

        getTodoGroupCount();
    }

    /**
     * 消息数据
     */

    public void queryMsgNum() {
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        final NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        url.queryMsgUnreadMun(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MsgCountRetBean>() {//
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(MsgCountRetBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {


                            msgCount = msg.getOp().getCount();

                            if (Integer.parseInt(msgCount) > 0) {
                                msg_tip.setVisibility(View.VISIBLE);
                                msg_tip.setText(msgCount + "条消息 >");


                                msg_tip.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(mContext, NoreadMsgActivity.class));
                                    }
                                });

                            } else {
                                msg_tip.setVisibility(View.GONE);
                            }


                        } else {
                            Log.e("amtf", "消息数量查询异常");
                        }

                    }
                });
    }


    @Override
    protected void reLoadData() {

    }

    @Override
    protected int getResId() {
        return R.layout.home_banner;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {

        if (event.what == EventConts.SoCket_Push) {
            JSONObject data = event.object;
            checkJson(data);
        }

    }

    private void checkJson(JSONObject json) {

        Log.e("amtf", "FirstHomePageFragment推送数据:" + json.toString());

        try {

            String op = json.getString("op");

            /**
             * 待办相关最新的
             */
            if (op.equals("notice")) {

                if (json.toString().contains("f_type")) {
                    String type = json.getJSONObject("data").getString("f_type");

                    if (type.equals("todo")) {
                        goTodoMsg(json.getJSONObject("data"));
                    }

                    if (type.equals("todo") || type.equals("form")) {
                        getTodoGroupCount();
                    }

                } else if (json.toString().contains("type")) {

                    String type = json.getString("type");

                    //群组相关
                    if (type.equals("group_operation_record")) {

                        String content = json.getJSONObject("data").getString("msg");

                        content2.setText(content);

                        String timetap = json.getJSONObject("data").getString("create_time");

                        time2.setText(timetap.substring(timetap.length() - 8, timetap.length() - 3));
                    }

                    if (type.equals("group_operation_record") || type.equals("group")) {
                        getTodoGroupCount();

                    }

                }


            }


        } catch (JSONException e) {
            Log.e("amtf", "json异常：" + e.getMessage());

            e.printStackTrace();
        }


    }


    /**
     * 展示待办消息
     *
     * @param json
     */
    private void goTodoMsg(JSONObject json) {

        try {
            String content = json.getString("f_title");

            String name = json.getJSONObject("f_create_by_info").getString("nickname");

            String timeStap = json.getString("f_create_date_timestamp");

            conent_todo.setText(name + " : " + content);

            String timeStr = DateUtil.longToDateMMss(Long.parseLong(timeStap));
            time.setText(timeStr.substring(timeStr.length() - 5, timeStr.length()));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void getTodoGroupCount() {
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        final NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("day", DateUtil.getCurrentData());
        url.queryMsgCount(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeMsgCountBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(HomeMsgCountBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {
                            freshCount(msg);

                        }
                    }
                });
    }


    /**
     * 查询最新数量和内容 TOdo
     *
     * @param msg
     */
    private void freshCount(HomeMsgCountBean msg) {

        HomeMsgCountBean.XhintBean xhint = msg.getXhint();
        HomeMsgCountBean.GroupBeanHome groupMsg = msg.getGroup();
        HomeMsgCountBean.TodoMsgBean todo = msg.getTodo();

        if (xhint != null) {
            String xhintCount = xhint.getCount();

            if (Integer.parseInt(xhintCount) > 0) {
                msg_fx.setVisibility(View.VISIBLE);
                msg_fx.setText("(" + xhintCount + ")");
            } else {
                msg_fx.setVisibility(View.GONE);
            }


        }
        if (groupMsg != null) {
            String groupCount = groupMsg.getGroupcount();

            if (Integer.parseInt(groupCount) > 0) {
                group_count.setText("(" + groupCount + ")");
                group_count.setVisibility(View.VISIBLE);

                String content = groupMsg.getMsgcontent();

                if (!MyTextUtil.isEmpty(content)) {
                    content2.setText(content);
                }

            } else {
                group_count.setVisibility(View.GONE);
            }

        }

        if (todo != null) {
            String todoCount = todo.getTodocount();

            if (Integer.parseInt(todoCount) > 0) {
                todo_count.setVisibility(View.VISIBLE);
                todo_count.setText("(" + todoCount + ")");

                String name = todo.getUserinfo().getNickname();
                conent_todo.setText(name + " : " + todo.getF_msg());

            } else {
                todo_count.setVisibility(View.GONE);
            }


        }

    }


    @OnClick(R.id.todo)
    public void goCommit() {

        startActivity(new Intent(mContext, TodoNewHomeActivity.class));


    }


    @OnClick(R.id.group)
    public void goGroup() {

        startActivity(new Intent(mContext, GroupBusiActivity.class));
    }


    @OnClick(R.id.tiaozhan)
    public void goTiaozhan() {
        ToastUtils.getInstanc().showToast("敬请等待后续开发");
    }


    @OnClick(R.id.msg_relation)
    public void relationMsg() {
        startActivity(new Intent(mContext, FeixinActivity.class));
    }

}
