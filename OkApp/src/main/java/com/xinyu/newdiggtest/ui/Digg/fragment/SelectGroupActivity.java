package com.xinyu.newdiggtest.ui.Digg.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.QunZuListAdapter;
import com.xinyu.newdiggtest.bean.GroupListBean;
import com.xinyu.newdiggtest.bean.RoomListBean;
import com.xinyu.newdiggtest.config.ActyFinishEvent;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyMiddleDialog;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 分享到群页面
 */
public class SelectGroupActivity extends BaseNoEventActivity {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    QunZuListAdapter adapter;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_group_select;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryGroupList();
    }


    private void queryGroupList() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.AppFindRoomList");
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("room_type", "G");
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("curPageNo", "1");
        map.put("pageRowCnt", "50");


        url.getGroupList(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GroupListBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(GroupListBean msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp() == null) {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                            return;
                        }
                        if (msg.getOp().getCode().equals("Y")) {
                            List<RoomListBean> list = msg.getRoom_list();
                            if (list == null || list.size() == 0) {
                                return;
                            }
                            setDatas(list);
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });
    }


    public void setDatas(final List<RoomListBean> datas) {
        adapter = new QunZuListAdapter(R.layout.item_grouplist, datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                RoomListBean data = datas.get(position);
                int len = datas.size();
                showSharePop(data, position, len);
            }
        });

    }

    MyMiddleDialog myMiddleDialog;

    private void showSharePop(final RoomListBean data, final int pos, final int len) {
        if (myMiddleDialog == null) {
            myMiddleDialog = new MyMiddleDialog(mContext, R.style.MyMiddleDialogStyle) {
                @Override
                protected View getView() {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    View view = inflater.inflate(R.layout.dialog_share_group, null);
                    initDialogView(view, data, len);
                    return view;
                }
            };
        }
        myMiddleDialog.show();
    }

    private void initDialogView(View view, final RoomListBean data, final int len) {
        ImageView gIncon = view.findViewById(R.id.icon_group);
        TextView gName = view.findViewById(R.id.tv_gname);
        TextView shareConten = view.findViewById(R.id.tv_share_conten);

        Picasso.with(mContext).load(data.getRoom_head()).into(gIncon);
        gName.setText(MyTextUtil.getDecodeStr(data.getRoom_name())/* + "(" + len + ")"*/);

        String shareInfo = (mContext.getIntent().getStringExtra("show_Info"));//整个的jsonstr
        shareConten.setText(shareInfo);

        TextView cancel = view.findViewById(R.id.cancel);
        TextView conform = view.findViewById(R.id.conform);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMiddleDialog.dismiss();
            }
        });

        conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMiddleDialog.dismiss();
                sendImMsg(data);
            }
        });

    }


    /**
     * 最终分享请求
     */
    private void sendImMsg(RoomListBean item) {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.SendMsg");
        map.put("msg_to", item.getRoom_id());
        map.put("room_id", item.getRoom_id());
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("msg_type", "0");//(0.文本  1图片 2语音 3.视频 4.链接 5文件)
        map.put("msg_from", "app");

        map.put("chat_topic", "chatgroup");

        map.put("msg_content", mContext.getIntent().getStringExtra(IntentParams.Target_Share_Content));
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        url.sendImMsg(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            ActyFinishEvent.FromSelectQun = 1;
                            finish();
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("发送失败");
                        }

                    }
                });
    }


    /**
     * @param stringExtra
     * @return
     */
    private String doSpilt(String stringExtra) {
        if (MyTextUtil.isEmpty(stringExtra) || !stringExtra.contains(" ")) {
            return "";
        }
        String[] sptStr = stringExtra.split(" ");
        return MyTextUtil.getUrl3Encoe(sptStr[0]) + sptStr[1];
    }


    private String subJson(String stringExtra) {
        if (MyTextUtil.isEmpty(stringExtra)) {
            return "";
        }
        String conent = "";
        try {
            JSONObject object = new JSONObject(stringExtra);
            conent = object.getString("content");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return conent;
    }


    @OnClick(R.id.icon_back)
    public void goback() {
        ActyFinishEvent.FromSelectQun = 1;
        finish();
    }

}
