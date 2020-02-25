package com.xinyu.newdiggtest.ui.chat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.tu.loadingdialog.LoadingDailog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.ImagePreviewActivity;
import com.nanchen.compresshelper.CompressHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.agra.BeCallActivity;
import com.xinyu.newdiggtest.bean.ImImgBean;
import com.xinyu.newdiggtest.bean.ImItemMsgBean;
import com.xinyu.newdiggtest.bean.ImParentBean;
import com.xinyu.newdiggtest.bean.UploadUrlBean;

import com.xinyu.newdiggtest.net.ApiConfig;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.net.bean.SSLSocket;
import com.xinyu.newdiggtest.seriable.TextLongClickListner;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.BaseActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.QunMemberAddReduceActivity;
import com.xinyu.newdiggtest.utils.CallDialog;
import com.xinyu.newdiggtest.utils.DialogUtil;
import com.xinyu.newdiggtest.utils.DisplayUtils;
import com.xinyu.newdiggtest.utils.LogUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import butterknife.BindView;
import butterknife.OnClick;
import io.agora.AgoraAPI;
import io.reactivex.functions.Consumer;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 企业版聊天
 */
public class ChatCompanyActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView title;


    @BindView(R.id.icon_right)
    ImageView icon_right;


    @BindView(R.id.chat_list)
    EasyRecyclerView chatList;
    @BindView(R.id.emotion_voice)
    ImageView emotionVoice;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.voice_text)
    TextView voiceText;
    @BindView(R.id.emotion_button)
    ImageView emotionButton;
    @BindView(R.id.emotion_add)
    ImageView emotionAdd;
    @BindView(R.id.emotion_send)
    StateButton emotionSend;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;
    @BindView(R.id.emotion_layout)
    LinearLayout emotionLayout;

    private EmotionInputDetector mDetector;
    private ArrayList<Fragment> fragments;

    private ChatEmotionFragment chatFunctionFragment;

    private CommonFragmentPagerAdapter adapter;

    private ChatAdapter chatAdapter;
    private LinearLayoutManager layoutManager;

    //录音相关
    int animationRes = 0;
    int res = 0;
    AnimationDrawable animationDrawable = null;
    private ImageView animView;

    LoadingDailog loadingDailog;


    @BindView(R.id.SmartRefreshLayout)
    SmartRefreshLayout refreshLayout;

    Activity ctx;
    String fid = "";
    String chatType = "";


    boolean notPush = true;

    List<ImageInfo> imageInfo = new ArrayList<>();//图片的总容器

    Context mctx;

    String roomId, userId;

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_chat;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mctx = this;
        ctx = this;


        if (getIntent().hasExtra("room_name")) {

            if (getIntent().hasExtra("member_num")) {
                String num = getIntent().getStringExtra("member_num");

                title.setText(getIntent().getStringExtra("room_name") + "(" + num + ")");
            } else {
                title.setText(getIntent().getStringExtra("room_name"));
            }


        }

        roomId = getIntent().getStringExtra("room_id");
        userId = PreferenceUtil.getInstance(mctx).getUserId();

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

        checkPermiss();

        initIntent();


        initWidget();

        initRecycle();

        initSocket();

    }


    private void initIntent() {

        chatType = getIntent().getStringExtra("room_type");

        if (chatType.equals("S")) {
            icon_right.setVisibility(View.GONE);
        } else {
            icon_right.setVisibility(View.VISIBLE);
            icon_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = getIntent();
                    intent.setClass(mctx, QunMemberAddReduceActivity.class);

                    startActivity(intent);
                }
            });

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        queryImMsgList(fid);

        addCallback();

    }


    //跟刷新和加载更多相关的
    private void initRecycle() {
//不用上拉加载更多
        refreshLayout.setEnableLoadMore(false);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshHistoryData();
            }
        });

    }

    /**
     * 加载历史数据
     */
    private void refreshHistoryData() {

        notPush = false;

        if (!TextUtils.isEmpty(fid)) {
            queryImMsgList(fid);
        }
    }

    boolean canTakePhoto;

    private void checkPermiss() {
        RxPermissions rxPermission = new RxPermissions(ChatCompanyActivity.this);
        rxPermission.requestEach(Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {

                        if (permission.name.equals(Manifest.permission.CAMERA)) {

                            if (permission.granted) {
                                canTakePhoto = true;
                            } else {
                                canTakePhoto = false;
                            }
                        } else if (permission.name.equals(Manifest.permission.RECORD_AUDIO)) {

                        }
                    }
                });


    }


    private void initWidget() {

        loadingDailog = new DialogUtil(this).getInstance();

        fragments = new ArrayList<>();
        chatFunctionFragment = new ChatEmotionFragment();
        fragments.add(chatFunctionFragment);

        ChatFunctionChatFragment functionFragment = new ChatFunctionChatFragment();

        if (chatType.equals("S")) {
            functionFragment.setIfPersonal(1);
            functionFragment.setCallBean("");

        } else {
            functionFragment.setIfPersonal(0);
        }


        fragments.add(functionFragment);

        adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);


        emotionVoice.setTag(false);
        mDetector = EmotionInputDetector.with(this)
                .setEmotionView(emotionLayout)
                .setViewPager(viewpager)
                .bindToContent(emotionLayout)
                .bindToEditText(editText)
                .bindToEmotionButton(emotionButton)
                .bindToAddButton(emotionAdd)
                .bindToSendButton(emotionSend)
                .bindToVoiceButton(emotionVoice)
                .bindToVoiceText(voiceText)
                .build();


        GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(this);
        globalOnItemClickListener.attachToEditText(editText);

        chatAdapter = new ChatAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(chatAdapter);

        chatAdapter.addItemClickListener(itemClickListener);

        chatAdapter.setLongClickListner(new TextLongClickListner() {
            @Override
            public void onLongClick(View view, String tx) {

                showWxDialog(view, tx);

            }
        });


    }


    PopupWindow pop;


    /**
     * 长按出选择
     *
     * @param tx
     */
    private void showWxDialog(View textView, String tx) {

        LinearLayout dialogView = null;

        if (dialogView == null)
            dialogView = (LinearLayout) LinearLayout.inflate(ChatCompanyActivity.this, R.layout.dialog_copy, null);

        if (pop == null) {
            initView(dialogView);

            pop = new PopupWindow(dialogView, DisplayUtils.getScreenWidth(mctx) - DisplayUtils.dp2px(this, 40),
                    DisplayUtils.dp2px(this, 35));

            pop.setTouchable(true);
            pop.setOutsideTouchable(true);
            pop.setClippingEnabled(false); //设置为false能够该view越过父类（也就是dialog)大小的限制
            pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialogView.measure(View.MeasureSpec.AT_MOST, View.MeasureSpec.EXACTLY);
        int measuredHeight = dialogView.getMeasuredHeight();
        int[] location = new int[2];
        textView.getLocationOnScreen(location);
        pop.showAtLocation(textView, Gravity.NO_GRAVITY, DisplayUtils.dp2px(this, 20), location[1] - measuredHeight - 80);

    }

    private void initView(LinearLayout dialogView) {

        int count = dialogView.getChildCount();

        for (int i = 0; i < count; i++) {

            dialogView.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    checkView(v);

                }
            });
        }


    }

    private void checkView(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.copy:

                ToastUtils.getInstanc().showToast("我是复制");
                break;


            case R.id.add_todo:
                ToastUtils.getInstanc().showToast("添加待办");
                break;

            case R.id.share:

                ToastUtils.getInstanc().showToast("分享");
                break;

            case R.id.delet:

                ToastUtils.getInstanc().showToast("删除");
                break;
        }

        pop.dismiss();
    }


    /**
     * item点击事件
     */
    private ChatAdapter.onItemClickListener itemClickListener = new ChatAdapter.onItemClickListener() {
        @Override
        public void onHeaderClick(MessageInfo data) {

        }

        @Override
        public void onImageClick(View view, String url) {
            if (imageInfo.size() < 1 || MyTextUtil.isEmpty(url))
                return;

            int len = imageInfo.size();
            int pos = -1;
            for (int i = 0; i < len; i++) {
                ImageInfo item = imageInfo.get(i);
                if (item.getThumbnailUrl().equals(url)) {
                    pos = i;
                    break;
                }
            }
            if (pos == -1)
                return;

            Intent intent = new Intent(ctx, ImagePreviewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
            bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, pos);
            intent.putExtras(bundle);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }

        @Override
        public void onShareClick(JSONObject json) {

        }

        @Override
        public void onVoiceClick(ImageView imageView, final MessageInfo data) {
            if (animView != null) {
                animView.setImageResource(res);
                animView = null;
            }
            switch (data.getType()) {
                case 1:
                    animationRes = R.drawable.voice_left;
                    res = R.mipmap.icon_voice_left3;
                    break;
                case 2:
                    animationRes = R.drawable.voice_right;
                    res = R.mipmap.icon_voice_right3;
                    break;
            }
            animView = imageView;
            animView.setImageResource(animationRes);
            animationDrawable = (AnimationDrawable) animView.getDrawable();

            int isPlaying = data.getPlayState();
            if (isPlaying == 0) {

                notifOther();

                data.setPlayState(1);

                MediaManager.playSound(data.getFileUrl(), new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        animationDrawable.stop();
                        animView.setImageResource(res);
                        data.setPlayState(0);
                    }
                }, ChatCompanyActivity.this);
                animationDrawable.start();

            } else if (isPlaying == 1) {
                data.setPlayState(2);
                MediaManager.pause();
                animationDrawable.stop();
            } else if (isPlaying == 2) {
                data.setPlayState(1);
                MediaManager.resume();
                animationDrawable.start();
            }

        }
    };

    /**
     * 重置其他的音频状态为初始
     */
    private void notifOther() {
        List<MessageInfo> dataList = chatAdapter.getAllData();
        for (MessageInfo tt : dataList) {
            tt.setPlayState(0);
        }
    }


    /**
     * 查询消息列表
     */
    private void queryImMsgList(String f_id) {

        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("room_id", getIntent().getStringExtra("room_id"));
        map.put("user_id", PreferenceUtil.getInstance(mctx).getUserId());
        map.put("sid", PreferenceUtil.getInstance(mctx).getSessonId());
        if (!TextUtils.isEmpty(f_id)) {
            map.put("position_num", f_id);
        }
        map.put("page.size", "15");
        map.put("before_join_msg", "true");


        url.getHistoryIMsg(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ImParentBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        refreshLayout.finishRefresh();


                        LogUtil.e("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(ImParentBean msg) {
                        refreshLayout.finishRefresh();
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData() == null || msg.getData().size() == 0) {

                                //TODO 设置空数据
                            } else {
                                loadMyData(msg.getData());

                                checkImg2putTogether();
                            }

                        } else {
                            ToastUtils.getInstanc(ChatCompanyActivity.this).showToast("服务异常");
                        }

                    }
                });
    }


    private List<MessageInfo> messageInfos = new ArrayList<>();

    /**
     * load 消息列表
     *
     * @param data
     */
    private void loadMyData(List<ImItemMsgBean> data) {
        messageInfos.clear();
        int len = data.size();

        Collections.reverse(data); // 倒序排列
        fid = data.get(0).getMsg_id();

        for (int i = 0; i < len; i++) {
            MessageInfo msgInfo = new MessageInfo();
            ImItemMsgBean bean = data.get(i);
            msgInfo.setHeader(bean.getHead());
            String ssName = MyTextUtil.isEmpty(bean.getName()) ? bean.getNickname() : bean.getName();
            msgInfo.setName(ssName);
            msgInfo.setUserId(bean.getUser_id());
            msgInfo.setContent(bean.getMsg_content());
//            msgInfo.setFilepath(bean.getMsg_content());
            msgInfo.setFileUrl(bean.getUrl());
            if (!MyTextUtil.isEmpty(bean.getLength())) {
                msgInfo.setVoiceTime(Long.parseLong(bean.getLength()));
            }

            if (bean.getImage() != null) {
                String Str = new Gson().toJson(bean.getImage());

                try {
                    JSONObject object = new JSONObject(Str);
                    msgInfo.setImageUrl(object.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            msgInfo.setMsgType(bean.getMsg_type());


            if (bean.getMsg_type().equals("4")) {

                if (MyTextUtil.isEmpty(bean.getOriginal_url())) {
                    msgInfo.setOriginal_url(bean.getMsg_content().toString());
                } else {
                    msgInfo.setOriginal_url(bean.getOriginal_url());
                }
            }

            msgInfo.setTime(bean.getMsg_time());
            if (bean.getUser_id().equals(PreferenceUtil.getInstance(this).getUserId())) {
                msgInfo.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
            } else {
                msgInfo.setType(Constants.CHAT_ITEM_TYPE_LEFT);
            }
            msgInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
            messageInfos.add(msgInfo);

        }

        chatAdapter.insertAll(messageInfos, 0);

        if (notPush)
            chatList.scrollToPosition(chatAdapter.getCount() - 1);

        chatAdapter.handler.post(new Runnable() {
            @Override
            public void run() {
                chatAdapter.notifyDataSetChanged();
            }
        });

    }


    /**
     * 发送消息
     */
    private void sendImMsg(final MessageInfo messageInfo) {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "api2e.SendAndPushChatMsg");

        map.put("user_id", PreferenceUtil.getInstance(mctx).getUserId());
        map.put("room_id", getIntent().getStringExtra("room_id"));
        map.put("chat_topic", "chatgroup");
        map.put("room_type", getIntent().getStringExtra("room_type"));
        map.put("room_name", getIntent().getStringExtra("room_name"));


        String cott = (String) messageInfo.getContent();

        if (cott.startsWith("http")) {
            map.put("msg_type", "4");//(0.文本  1图片 2语音 3.视频 4.链接 5文件)
        } else {
            map.put("msg_type", "0");//(0.文本  1图片 2语音 3.视频 4.链接 5文件)
        }

        map.put("msg_content", cott);
        map.put("msg_from", "app");

        map.put("session_id", PreferenceUtil.getInstance(mctx).getSessonId());

        url.sendCompanyMsg(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(ChatCompanyActivity.this).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {

//                            showMySendMsg(messageInfo);
                        } else {
                            ToastUtils.getInstanc(ChatCompanyActivity.this).showToast("发送失败");
                        }

                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(final MessageInfo messageInfo) {

        String type = messageInfo.getMsgType();
        switch (type) {
            case "0"://文字
                sendImMsg(messageInfo);
                break;


            case "1"://图片
                uploadIMPhoto(messageInfo);
                break;

            case "2"://语音

                upLoadImAudio(messageInfo);

                break;

            case "99"://呼叫


                CallDialog dialog = new CallDialog(ctx);
                dialog.setCallListner(new CallDialog.OnCallListner() {
                    @Override
                    public void onVedioCall() {
                        Log.e("amtf", "视频通话");

                        // App.the().getM_agoraAPI().channelInviteUser("hao123", other_id, 0);//最后一个参数必须为0
//                Intent intent = new Intent(mctx, HostActivity.class);
//                intent.putExtra("channelName", "hao123");
//                String userId = PreferenceUtil.getInstance(mctx).getUserId();
//                intent.putExtra("subscriber", userId);
//                intent.putExtra("beCall", other_id);
//                startActivity(intent);

                    }

                    @Override
                    public void onAudioCall() {
                        Log.e("amtf", "语音通话");
                    }
                });
                break;


            case "1000":

                int count = messageInfo.getMySend();
                title.setText(getIntent().getStringExtra("room_name") + "(" + count + ")");

                break;

        }

    }


    private void upLoadImAudio(final MessageInfo messageInfo) {
        loadingDailog.show();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM).addFormDataPart("TOKEN", PreferenceUtil.getInstance(this).getSessonId());
//                .addFormDataPart(TOKEN, token);自定义参数key常量类，即参数名

        File file = new File(messageInfo.getFilepath());//filePath 音频文件
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        builder.addFormDataPart("audiofile", file.getName(), imageBody);//"imgfile"+i 后台文件
        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance(2);
        AppUrl url = manager.create(AppUrl.class);
        url.uploadOkImgs(parts).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UploadUrlBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(ctx).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(UploadUrlBean res) {
                        loadingDailog.dismiss();
                        if (res.getErrno() != 0) {
                            ToastUtils.getInstanc(ctx).showToast("文件上传失败");
                            return;
                        }

                        List<ImImgBean> data = res.getData();
                        if (data == null || data.size() < 1) {
                            return;
                        }

                        sendAudio(data.get(0), messageInfo);
                    }
                });
    }


    private String getVocieLen(long voiceTime) {
        float voicelen = voiceTime / 1000;
        int len = (int) voicelen;
        if (len < 1)
            len = 1;
        return len + "";
    }


    /**
     * 发送语音
     */

    private void sendAudio(final ImImgBean img, MessageInfo messageInfo) {

        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "api2e.SendAndPushChatMsg");

        map.put("user_id", PreferenceUtil.getInstance(mctx).getUserId());
        map.put("room_id", getIntent().getStringExtra("room_id"));
        map.put("chat_topic", "chatgroup");
        map.put("room_type", getIntent().getStringExtra("room_type"));
        map.put("room_name", getIntent().getStringExtra("room_name"));
        map.put("session_id", PreferenceUtil.getInstance(mctx).getSessonId());


        map.put("msg_type", "2");//(0.文本  1图片 2语音 3.视频 4.链接 5文件)
        map.put("msg_from", "app");
        map.put("msg_content", img.getOriginal());

        map.put("file_size", "1069");//文件大小
        map.put("file_url", img.getOriginal());//地址
        map.put("file_name", "okImAudio_" + System.currentTimeMillis());//文件名字
        map.put("file_type", ".aac");//文件类型
        final String len = getVocieLen(messageInfo.getVoiceTime());
        map.put("length", len);//录音时长


        url.sendCompanyMsg(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(ChatCompanyActivity.this).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {

                        } else {
                            ToastUtils.getInstanc(ChatCompanyActivity.this).showToast("发送失败");
                        }

                    }
                });
    }


    @Override
    public void onBackPressed() {
        if (!mDetector.interceptBackPress()) {
            super.onBackPressed();
        }
    }


    @OnClick(R.id.iv_back)
    public void goBack() {
        leakFinish();
    }


    private void leakFinish() {
//        startActivity(new Intent(ctx, LeakActivity.class));
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        MediaManager.release();

        notifyH5();
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
        mSocket.off(Socket.EVENT_CONNECT);
        mSocket.off(Socket.EVENT_CONNECT_ERROR);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT);
        mSocket.off(Socket.EVENT_DISCONNECT);
        mSocket.off("msg");
        mSocket.off("subresp");
        mSocket.disconnect();

        releseResource();


    }

    private void releseResource() {
        EventBus.getDefault().unregister(this);

        if (animationDrawable != null) {
            animationDrawable.stop();
            animationDrawable = null;
        }

        chatList.removeAllViewsInLayout();
        chatList.removeAllViews();
        chatList = null;

        mctx = null;
        ctx = null;
    }


    private void notifyH5() {

        try {
            JSONObject parent = new JSONObject();
            JSONObject child = new JSONObject();
            child.put("room_id", roomId);
            parent.put("resultCode", 9000);
            parent.put("type", 0);
            parent.put("resultStr", "succed");
            parent.put("msg", child);

//            if (APPConstans.mcallBack != null) {
//                APPConstans.mcallBack.success(parent);
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private Socket mSocket;

    /**
     * socket相关
     */
    private void initSocket() {

        try {
            IO.Options opts = new IO.Options();
            //如果服务端使用的是https 加以下两句代码,文章尾部提供SSLSocket类
            opts.sslContext = SSLSocket.genSSLSocketFactory();
            opts.hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            mSocket = IO.socket(ApiConfig.SOCKET_IP, opts);
        } catch (Exception e) {
        }

        setListner();
        mSocket.connect();
    }

    private void setListner() {
        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                beginEmit();
            }
        });// 连接成功;


        mSocket.on("msg", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject data = (JSONObject) args[0];

                        checkData(data);

                    }
                });


            }
        });


        mSocket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                ToastUtils.getInstanc().showToast("socket链接错误");
            }
        });
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("amtf", "socket链接超时");

            }
        });

        mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mSocket.connect();
            }
        });

        mSocket.on("subresp", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("amtf", "聊天订阅成功:" + ApiConfig.SOCKET_IP);
            }
        });

    }


    private void checkData(JSONObject data) {


        try {

            String json = data.getString("msg");

            JSONObject child1 = new JSONObject(json);

            String str = child1.getString("data");

            String op = child1.getString("op");


            if (op.equals("create")) {

                JSONObject child2 = new JSONObject(str);

                if (child2.toString().contains("room_id")) {
                    String pushRoom = child2.getString("room_id");

                    if (pushRoom.equals(roomId)) {
                        uploadView(child2);
                    }
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    //msg_content

    private void uploadView(JSONObject data) {

        try {
            if (!data.toString().contains("msg_content")) {
                return;
            }
            MessageInfo msgInfo = new MessageInfo();
            msgInfo.setHeader(data.getString("head"));
            msgInfo.setUserId(data.getString("user_id"));
            msgInfo.setMsgType(data.getString("msg_type"));
            msgInfo.setName(checkName(data));
            msgInfo.setContent(data.getString("msg_content"));

            String type = data.getString("msg_type");


            if (data.toString().contains("url") && type.equals("2")) {
                msgInfo.setFileUrl(data.getString("url"));
                msgInfo.setFilepath(data.getString("url"));
            }


            if (data.toString().contains("image") && type.equals("1")) {
                msgInfo.setImageUrl(data.getString("image"));
            }


            if (type.equals("4")) {
                msgInfo.setOriginal_url(data.getString("msg_content"));
            }


            if (data.toString().contains("length")) {
                msgInfo.setVoiceTime(Integer.parseInt(data.getString("length")));//时长
            }


            msgInfo.setTime(data.getString("msg_time"));
            String userId = data.getString("user_id");

            if (userId.equals(PreferenceUtil.getInstance(this).getUserId())) {
                msgInfo.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
            } else {
                msgInfo.setType(Constants.CHAT_ITEM_TYPE_LEFT);
            }

            chatAdapter.add(msgInfo);
            chatList.scrollToPosition(chatAdapter.getCount() - 1);
            chatAdapter.handler.post(new Runnable() {
                @Override
                public void run() {
                    chatAdapter.notifyDataSetChanged();
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

        checkImg2putTogether();

    }

    private String checkName(JSONObject data) {

        String name = "";

        try {
            if (data.toString().contains("nickname")) {
                name = data.getString("nickname");
            } else {
                name = data.getString("name");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return name;
    }

    private void beginEmit() {
        try {
            JSONObject object = new JSONObject();
            JSONArray array = new JSONArray();
            JSONObject child = new JSONObject();
            child.put("userId", PreferenceUtil.getInstance(mctx).getUserId());
            child.put("topic", "ok:chat");
            array.put(child);
            object.put("subs", array);
            mSocket.emit("sub", object);//向服务端发送数据

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void uploadIMPhoto(MessageInfo messageInfo) {
        loadingDailog.show();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM).addFormDataPart("TOKEN", PreferenceUtil.getInstance(this).getSessonId());
//                .addFormDataPart(TOKEN, token);自定义参数key常量类，即参数名

        File file = new File(messageInfo.getImageUrl());//filePath 图片地址

        File newFile = CompressHelper.getDefault(this).compressToFile(file);

        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), newFile);
        builder.addFormDataPart("imgfile", file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance(2);
        AppUrl url = manager.create(AppUrl.class);
        url.uploadOkImgs(parts).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UploadUrlBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(ctx).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(UploadUrlBean res) {
                        loadingDailog.dismiss();
                        if (res.getErrno() != 0) {
                            ToastUtils.getInstanc(ctx).showToast("文件上传失败");
                            return;
                        }

                        List<ImImgBean> data = res.getData();
                        if (data == null || data.size() < 1) {
                            return;
                        }

                        sendPhoto(data.get(0));
                    }
                });
    }

    /**
     * 发送图片
     */

    private void sendPhoto(final ImImgBean img) {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "api2e.SendAndPushChatMsg");

        map.put("user_id", PreferenceUtil.getInstance(mctx).getUserId());
        map.put("room_id", getIntent().getStringExtra("room_id"));
        map.put("chat_topic", "chatgroup");
        map.put("room_type", getIntent().getStringExtra("room_type"));
        map.put("room_name", getIntent().getStringExtra("room_name"));
        map.put("session_id", PreferenceUtil.getInstance(mctx).getSessonId());


        map.put("msg_type", "1");//(0.文本  1图片 2语音 3.视频 4.链接 5文件)
        map.put("msg_from", "app");
        map.put("msg_content", creatJson(img));


        url.sendCompanyMsg(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(ChatCompanyActivity.this).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {

                        } else {
                            ToastUtils.getInstanc(ChatCompanyActivity.this).showToast("发送失败");
                        }

                    }
                });
    }


    private String creatJson(ImImgBean img) {

        JSONObject json = new JSONObject();
        try {
            json.put("type", "1");
            json.put("original", img.getOriginal());
            json.put("thumbnail", img.getThumbnail());
            json.put("height", img.getHeight());
            json.put("width", img.getWidth());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }


    /**
     * 检索图片并保存在一个数组中
     */
    private void checkImg2putTogether() {

        imageInfo.clear();

        List<MessageInfo> data = chatAdapter.getAllData();

        if (data != null && data.size() > 0) {

            for (MessageInfo item : data) {

                if (item.getMsgType().equals("1")) {

                    if (!MyTextUtil.isEmpty(item.getImageUrl())) {

                        putImg(item.getImageUrl());

                    }
                }
            }
        }
    }


    private void putImg(String imageUrl) {

        try {
            JSONObject object = new JSONObject(imageUrl);
            ImageInfo tt = new ImageInfo();
            tt.setBigImageUrl(object.getString("original"));
            tt.setThumbnailUrl(object.getString("thumbnail"));

            tt.setImageViewHeight(400);
            tt.setImageViewHeight(200);

            tt.setImageViewX(50);
            tt.setImageViewY(50);


            imageInfo.add(tt);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void addCallback() {

        App.the().getM_agoraAPI().callbackSet(new AgoraAPI.CallBack() {

            @Override
            public void onLogout(int i) {

                Log.e("amtf", "onLogout:信令退出了");
            }

            @Override
            public void onLoginFailed(final int i) {

                Log.e("amtf", "onLoginFailed" + i);
            }

            @Override
            public void onInviteReceived(String channelID, String account, int uid, String extra) {
                super.onInviteReceived(channelID, account, uid, extra);

                LogUtil.e("uid 固定为0:" + uid);
                Intent intent = new Intent(mctx, BeCallActivity.class);
                intent.putExtra("channelName", channelID);
                intent.putExtra("subscriber", account);
                startActivity(intent);
            }

        });

    }


}
