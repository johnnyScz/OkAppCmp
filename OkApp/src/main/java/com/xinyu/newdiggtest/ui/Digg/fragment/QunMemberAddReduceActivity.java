package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.MemberAddReduceAdapter;
import com.xinyu.newdiggtest.adapter.RoomMemberBean;
import com.xinyu.newdiggtest.adapter.viewhelper.TotalMemberActivity;
import com.xinyu.newdiggtest.bean.GroupMemberBean;
import com.xinyu.newdiggtest.net.ApiConfig;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;

import com.xinyu.newdiggtest.ui.BaseActivity;

import com.xinyu.newdiggtest.ui.chat.MessageInfo;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.wxapi.WeixinUtil;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 群 加减好友
 */
public class QunMemberAddReduceActivity extends BaseActivity {


    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;


    @BindView(R.id.title)
    public TextView title;


    @BindView(R.id.group_name)
    public TextView group_name;//群名称

    @BindView(R.id.btn_delet)
    public Button btn_delet;


    MemberAddReduceAdapter adapter;


    String roomId, tudoId, companyId;

    int memberCount = 0;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_member_add_reduce;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        queryDatas();
    }


    private void initView() {

        group_name.setText(getIntent().getStringExtra("room_name"));

        companyId = PreferenceUtil.getInstance(mContext).getCompanyId();

        roomId = getIntent().getStringExtra("room_id");
        tudoId = getIntent().getStringExtra("todo_id");


        GridLayoutManager mgr = new GridLayoutManager(mContext, 6);
        recyclerView.setLayoutManager(mgr);
        recyclerView.setNestedScrollingEnabled(false);

    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onXshellEvent(MessageInfo event) {


    }


    private void queryDatas() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("room_id", roomId);

        url.queryZuMembers(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GroupMemberBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(GroupMemberBean msg) {
                        loadingDailog.dismiss();

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getMember_list() != null && msg.getMember_list().size() > 0) {
                                showDatas(msg.getMember_list());

                            } else {
                                ToastUtils.getInstanc(mContext).showToast("没有数据！");
                            }

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });

    }


    /**
     * 设置数据
     *
     * @param datas
     */
    private void showDatas(final List<RoomMemberBean> datas) {

        memberCount = datas.size();


        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setMsgType("1000");//聊天室的人数
        messageInfo.setMySend(memberCount);
        EventBus.getDefault().post(messageInfo);


        title.setText("聊天消息(" + memberCount + ")");


        for (RoomMemberBean dd : datas) {
            dd.setvType(0);
        }
        RoomMemberBean addBean = new RoomMemberBean();
        addBean.setvType(1);
        datas.add(addBean);

        /**
         * Todo 如果自己是创建者，可以删
         */
        if (getIntent().getStringExtra("creater").equals(PreferenceUtil.getInstance(mContext).getUserId())) {
            RoomMemberBean reduceBean = new RoomMemberBean();
            reduceBean.setvType(2);
            datas.add(reduceBean);
        }

        adapter = new MemberAddReduceAdapter(datas, mContext);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {


                switch (view.getId()) {

                    case R.id.add:

                        Intent intent = getIntent();
                        intent.setClass(mContext, TotalMemberActivity.class);
                        intent.putExtra("add_type", "1");
                        startActivity(intent);

                        break;

                    case R.id.reduce:

                        Intent it11 = getIntent();
                        it11.setClass(mContext, TotalMemberActivity.class);
                        it11.putExtra("add_type", "-1");     //TODO  -1 是删除 其他是增加
                        startActivity(it11);

                        break;


                }
            }
        });

    }


    /**
     *
     */
    @OnClick(R.id.share)
    public void goShare() {

        String share = "";


        share = "2";

        if (share.equals("2")) {

            go2Share();


        } else {

            String url = ApiConfig.OK_ShareUrl + "weShare/#/pages/enjoy/chatgrant/?f_id=" + tudoId + "&room_id=" + roomId + "&company_id=" + companyId;


            WeixinUtil.getInstance().diggWxShare(this, url, "OK!讨论组",
                    title.getText().toString(), R.mipmap.ic_launcher, false);
        }


    }

    private void goShareLittleWx() {

        try {

            WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();

            miniProgramObj.webpageUrl = ApiConfig.OK_ShareUrl + "?" + "fid=" + tudoId + "&roomid=" + roomId + "&companyid=" + companyId;


            miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2

            miniProgramObj.userName = ApiConfig.WxLittle;     // 小程序原始id

            miniProgramObj.path = "pages/index/index?fid=" + tudoId + "&roomid=" + roomId + "&companyid=" + companyId;


            //小程序页面路径
            WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
            msg.title = "OK!讨论组";                    // 小程序消息title
            msg.description = getIntent().getStringExtra("room_name");               // 小程序消息desc
            msg.thumbData = getThumb();

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;  // 目前只支持会话
            req.transaction = "miniProgram";

            IWXAPI api = WXAPIFactory.createWXAPI(this, ApiConfig.WXAPP_ID);
            api.sendReq(req);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public byte[] getThumb() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length > 32 * 1024 && options != 10) {
            output.reset(); //清空output
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            options -= 10;
        }
        return output.toByteArray();
    }


    private void go2Share() {
        final MyDialog dialog = new MyDialog();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, "tag");

        dialog.setOnPopListner(new MyDialog.OnPopClickListner() {
            @Override
            public void onCancle() {
                dialog.dismiss();
            }

            @Override
            public void onShareQun() {
                dialog.dismiss();

            }

            @Override
            public void onShareFriend() {
                dialog.dismiss();
            }

            @Override
            public void onShareWeixin() {
                dialog.dismiss();
                goShareLittleWx();
            }

            @Override
            public void onShareCircle() {
                dialog.dismiss();
            }
        });

    }


}




