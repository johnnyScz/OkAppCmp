package com.xinyu.newdiggtest.ui.chat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.tu.loadingdialog.LoadingDailog;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.preview.ImagePreviewActivity;
import com.nanchen.compresshelper.CompressHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.CreatRoomId;
import com.xinyu.newdiggtest.bean.ImImgBean;
import com.xinyu.newdiggtest.bean.ImItemMsgBean;
import com.xinyu.newdiggtest.bean.ImParentBean;
import com.xinyu.newdiggtest.bean.RecentMsgBean;
import com.xinyu.newdiggtest.bean.RoomIdBean;
import com.xinyu.newdiggtest.bean.UploadUrlBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;

import com.xinyu.newdiggtest.ui.LeakActivity;
import com.xinyu.newdiggtest.utils.DialogUtil;
import com.xinyu.newdiggtest.utils.DisplayUtils;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 随手记
 */
public class SuiShouNoteActivity extends AppCompatActivity {

    PopupWindow addPop;
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
    RelativeLayout emotionLayout;

    private EmotionInputDetector mDetector;
    private ArrayList<Fragment> fragments;

//    private ChatFunctionFragment chatFunctionFragment;
    private CommonFragmentPagerAdapter adapter;

    private HandNoteAdapter chatAdapter;
    private LinearLayoutManager layoutManager;
    //录音相关
    int animationRes = 0;
    int res = 0;
    final int AlBUMReturn = -1;
    AnimationDrawable animationDrawable = null;
    private ImageView animView;

    LoadingDailog loadingDailog;


    @BindView(R.id.SmartRefreshLayout)
    SmartRefreshLayout refreshLayout;

    Activity ctx;
    String fid = "";
    String latestMsgid = "";

    List<ImageInfo> imageInfo = new ArrayList<>();//图片的总容器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuishou);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        ctx = this;
        initView();
        checkPermiss();
        initWidget();
        initAddPicturePop();

        if (MyTextUtil.isEmpty(PreferenceUtil.getInstance(ctx).getHandNote_RoomId())) {
            requestHandVoteRoodId();
        } else {
            queryImMsgList(fid);
        }

    }


    private void requestHandVoteRoodId() {

        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> maps = new HashMap<>();
        maps.put("command", "chat.QueryChatRoomById");
        maps.put("user_id", PreferenceUtil.getInstance(this).getUserId());
        maps.put("chat_user_id", "");
        maps.put("room_type", "M");
        url.queryChatRoom(maps).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RoomIdBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());


                    }

                    @Override
                    public void onNext(RoomIdBean msg) {
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getDatalist() != null && msg.getDatalist().size() > 0) {
                                String roomId = msg.getDatalist().get(0).getRoom_id();

                                if (MyTextUtil.isEmpty(roomId)) {
                                    firstCreatRoom();
                                } else {
                                    PreferenceUtil.getInstance(ctx).setHandNoteRoomId(roomId);
                                }

                            } else {
                                firstCreatRoom();
                            }

                        } else {
                            ToastUtils.getInstanc().showToast("服务异常");
                        }

                    }
                });

    }


    /**
     * 创建聊天室
     */

    private void firstCreatRoom() {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> maps = new HashMap<>();

        maps.put("command", "chat.AddChatRoom");
        maps.put("room_name", "随手记");
        maps.put("room_property", "normal");

        maps.put("chat_user_ids", PreferenceUtil.getInstance(this).getUserId());
        maps.put("union_ids", PreferenceUtil.getInstance(this).getUserId());
        maps.put("main_object", PreferenceUtil.getInstance(this).getUserId());

        maps.put("room_type", "M");
        maps.put("room_head", "");


        url.creatChatRoom(maps).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreatRoomId>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();

                        Log.e("amtf", e.getMessage());

                    }

                    @Override
                    public void onNext(CreatRoomId msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            if (!MyTextUtil.isEmpty(msg.getRoom_id())) {
                                PreferenceUtil.getInstance(ctx).setHandNoteRoomId(msg.getRoom_id());
                                queryImMsgList(fid);
                            }


                        } else {
                            ToastUtils.getInstanc().showToast("服务异常");
                        }

                    }
                });

    }


    private void checkPermiss() {
        RxPermissions rxPermission = new RxPermissions(ctx);
        rxPermission.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {

                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 下次再次启动时，还会提示请求权限的对话框
                            Log.e("amtf", permission.name + " is denied. More info should be provided.用户拒绝了该权限，没有选中『不再询问』");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                        }
                    }
                });

    }


    private void initAddPicturePop() {

        LinearLayout view = (LinearLayout) LayoutInflater.from(SuiShouNoteActivity.this).inflate(R.layout.fragment_chat_function, null);
//点击事件
        setPopListner(view);
        addPop = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                DisplayUtils.dp2px(this, 150));

        addPop.setTouchable(true);
        addPop.setOutsideTouchable(true);

        emotionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!addPop.isShowing()) {
                    addPop.showAtLocation(emotionAdd, Gravity.BOTTOM, 10, 10);

                } else {
                    addPop.dismiss();
                }

            }
        });


    }

    private void setPopListner(LinearLayout view) {
        View view1 = view.findViewById(R.id.chat_function_photo);
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addPop.isShowing()) {
                    addPop.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");//相片类型
                    startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

                }

            }
        });

        View view2 = view.findViewById(R.id.chat_function_photograph);

        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addPop.isShowing()) {
                    checkPemisson();
                    addPop.dismiss();
                }
            }
        });


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
        if (!TextUtils.isEmpty(fid)) {
            queryImMsgList(fid);
        }
    }


    private void initView() {
        loadingDailog = new DialogUtil(this).getInstance();
        initRecycle();
    }


    private void initWidget() {
        fragments = new ArrayList<>();

//        chatFunctionFragment = new ChatFunctionFragment();
//        fragments.add(chatFunctionFragment);
        adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);

        mDetector = EmotionInputDetector.with(this)
                .setEmotionView(emotionLayout)
                .setViewPager(viewpager)
                .bindToContent(chatList)
                .bindToEditText(editText)
//                .bindToEmotionButton(emotionButton)
//                .bindToAddButton(emotionAdd)
                .bindToSendButton(emotionSend)
                .bindToVoiceButton(emotionVoice)
                .bindToVoiceText(voiceText)
                .build();


        GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(this);
        globalOnItemClickListener.attachToEditText(editText);

        chatAdapter = new HandNoteAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(chatAdapter);

        chatAdapter.addItemClickListener(itemClickListener);


        emotionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.getInstanc(ctx).showToast("我被点击了");
            }
        });


    }

    /**
     * item点击事件
     */
    private HandNoteAdapter.onItemClickListener itemClickListener = new HandNoteAdapter.onItemClickListener() {

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
        public void onVoiceClick(ImageView imageView, MessageInfo data) {
            if (animView != null) {
                animView.setImageResource(res);
                animView = null;
            }


            animationRes = R.drawable.voice_left;
            res = R.mipmap.icon_voice_left3;

//            switch (data.getType()) {
//                case 1:
//                    animationRes = R.drawable.voice_left;
//                    res = R.mipmap.icon_voice_left3;
//                    break;
//                case 2:
//                    animationRes = R.drawable.voice_right;
//                    res = R.mipmap.icon_voice_right3;
//                    break;
//            }
            animView = imageView;
            animView.setImageResource(animationRes);
            animationDrawable = (AnimationDrawable) animView.getDrawable();
            animationDrawable.start();
            MediaManager.playSound(data.getFileUrl(), new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    animationDrawable.stop();
                    animView.setImageResource(res);
                }
            }, SuiShouNoteActivity.this);
        }
    };


    /**
     * 查询消息列表
     */
    private void queryImMsgList(String f_id) {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.QueryMsg");
        map.put("room_id", PreferenceUtil.getInstance(ctx).getHandNote_RoomId());
        map.put("user_id", PreferenceUtil.getInstance(this).getUserId());
        if (!TextUtils.isEmpty(f_id)) {
            map.put("position_num", f_id);
        }
        map.put("page.size", "15");
        url.getImMsg(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ImParentBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        refreshLayout.finishRefresh();
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(SuiShouNoteActivity.this).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(ImParentBean msg) {
                        loadingDailog.dismiss();
                        refreshLayout.finishRefresh();
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData() == null || msg.getData().size() == 0) {

                                //TODO 设置空数据
                            } else {
                                loadMyData(msg.getData());
                            }

                        } else {
                            ToastUtils.getInstanc(SuiShouNoteActivity.this).showToast("服务异常");
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
        Collections.reverse(data); // 倒序排列
        if (TextUtils.isEmpty(fid)) {
            latestMsgid = data.get(data.size() - 1).getMsg_id();
        }

        checkImg2putTogether(data);

        messageInfos.clear();
        int len = data.size();
        for (int i = 0; i < len; i++) {
            MessageInfo msgInfo = new MessageInfo();
            ImItemMsgBean bean = data.get(i);
            msgInfo.setHeader(bean.getHead());
            String ssName = MyTextUtil.isEmpty(bean.getName()) ? bean.getNickname() : bean.getName();
            msgInfo.setName(ssName);
            msgInfo.setUserId(bean.getUser_id());
            msgInfo.setContent(bean.getMsg_content().toString());
//            msgInfo.setFilepath(bean.getMsg_content());
            msgInfo.setTime(bean.getMsg_time());
            msgInfo.setFileUrl(bean.getUrl());
            if (!MyTextUtil.isEmpty(bean.getLength())) {
                msgInfo.setVoiceTime(Long.parseLong(bean.getLength()));
            }

            if (bean.getImage() != null) {
                msgInfo.setImageUrl(bean.getImage().toString());
            }

            msgInfo.setMsgType(bean.getMsg_type());

            msgInfo.setTime(bean.getMsg_time());

            messageInfos.add(msgInfo);

        }

        chatAdapter.insertAll(messageInfos, 0);
        chatAdapter.handler.post(new Runnable() {
            @Override
            public void run() {
                chatAdapter.notifyDataSetChanged();
            }
        });

        if (TextUtils.isEmpty(fid)) {
            chatList.scrollToPosition(chatAdapter.getCount() - 1);
        } else {
            chatList.scrollToPosition(15);
        }

        fid = data.get(0).getMsg_id();
    }

    /**
     * 检索图片并保存在一个数组中
     *
     * @param data
     */
    private void checkImg2putTogether(List<ImItemMsgBean> data) {

        List<ImageInfo> datlist = new ArrayList<>();
        for (ImItemMsgBean item : data) {
            if (item.getMsg_type().equals("1")) {//图片类型
                String josonUrl = item.getImage().toString();
                try {
                    JSONObject object = new JSONObject(josonUrl);
                    ImageInfo info = new ImageInfo();
                    info.bigImageUrl = object.getString("original");
                    info.thumbnailUrl = object.getString("thumbnail");
                    datlist.add(info);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (datlist.size() > 0) {
//            imageInfo.addAll(datlist);
            imageInfo.addAll(0, datlist);
        }


    }


    /**
     * 发送消息
     */
    private void sendImMsg(final MessageInfo messageInfo) {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.SendMsg");

        map.put("msg_to", "");
        map.put("room_id", PreferenceUtil.getInstance(ctx).getHandNote_RoomId());
        map.put("chat_topic", "chat");

        map.put("room_name", "随手记");

        map.put("user_id", PreferenceUtil.getInstance(this).getUserId());
        map.put("msg_type", "0");//(0.文本  1图片 2语音 3.视频 4.链接 5文件)
        map.put("msg_from", "app");

//        map.put("msg_content", MyTextUtil.getUrl3Encoe(messageInfo.getContent()));

        map.put("sid", PreferenceUtil.getInstance(this).getSessonId());
        url.sendImMsg(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(SuiShouNoteActivity.this).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            showMySendMsg(messageInfo);
                        } else {
                            ToastUtils.getInstanc(SuiShouNoteActivity.this).showToast("发送失败");
                        }

                    }
                });
    }


    //展示我发的信息
    private void showMySendMsg(final MessageInfo messageInfo) {
        messageInfo.setHeader(PreferenceUtil.getInstance(this).getHeadUrl());
        messageInfo.setTime(new Date().getTime() + "");
        messageInfo.setName(PreferenceUtil.getInstance(this).getNickName());

//        chatAdapter.add(messageInfo);
        chatAdapter.insert(messageInfo, chatAdapter.getCount());
        chatList.scrollToPosition(chatAdapter.getCount() - 1);
        chatAdapter.handler.post(new Runnable() {
            @Override
            public void run() {
                chatAdapter.notifyDataSetChanged();
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

                sendIMPhoto(messageInfo);
                break;

            case "2"://语音
                upLoadImAudio(messageInfo);

                break;
        }

    }


    private void upLoadImAudio(final MessageInfo messageInfo) {
        loadingDailog.show();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM).addFormDataPart("TOKEN", PreferenceUtil.getInstance(this).getSessonId());
//                .addFormDataPart(TOKEN, token);自定义参数key常量类，即参数名

        File file = new File(messageInfo.getFilepath());//filePath 图片地址
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("imgfile", file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        url.uploadImgs(parts).subscribeOn(Schedulers.io())
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
                        List<ImImgBean> date = res.getData();
                        if (date == null || date.size() < 0) {
                            return;
                        }
                        audioUpSuccedNextSend(date.get(0), messageInfo);
                    }
                });
    }

    private void audioUpSuccedNextSend(final ImImgBean data, MessageInfo messageInfo) {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.SendMsg");
        map.put("room_id", PreferenceUtil.getInstance(ctx).getHandNote_RoomId());
        map.put("user_id", PreferenceUtil.getInstance(this).getUserId());
        map.put("msg_type", "2");//(0.文本  1图片 2语音 3.视频 4.链接 5文件)
        map.put("msg_from", "app");
        map.put("msg_content", messageInfo.getFilepath());

        map.put("file_url", data.getOriginal());//地址
        map.put("file_name", "okImAudio_" + System.currentTimeMillis());//文件名字
        map.put("file_type", ".aac");//文件类型

        final String len = getVocieLen(messageInfo.getVoiceTime());
        map.put("length", len);//录音时长

        map.put("msg_to", "");
        map.put("chat_topic", "chat");
        map.put("room_name", "随手记");
        map.put("sid", PreferenceUtil.getInstance(this).getSessonId());

        url.sendImMsg(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(SuiShouNoteActivity.this).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            showMySendAudio(data.getOriginal(), len);
                        } else {
                            ToastUtils.getInstanc(SuiShouNoteActivity.this).showToast("发送失败");
                        }

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


    private void sendIMPhoto(MessageInfo messageInfo) {
        loadingDailog.show();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM).addFormDataPart("TOKEN", PreferenceUtil.getInstance(this).getSessonId());
//                .addFormDataPart(TOKEN, token);自定义参数key常量类，即参数名

        File file = new File(messageInfo.getImageUrl());//filePath 图片地址

        File newFile = CompressHelper.getDefault(this).compressToFile(file);

        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), newFile);
        builder.addFormDataPart("imgfile", file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        url.uploadImgs(parts).subscribeOn(Schedulers.io())
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
                        finaSendPhoto(data.get(0));
                    }
                });
    }


    private void finaSendPhoto(final ImImgBean img) {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.SendMsg");

        map.put("msg_to", "");
        map.put("chat_topic", "chat");
        map.put("room_name", "随手记");

        map.put("room_id", PreferenceUtil.getInstance(ctx).getHandNote_RoomId());
        map.put("user_id", PreferenceUtil.getInstance(this).getUserId());
        map.put("msg_type", "1");//(0.文本  1图片 2语音 3.视频 4.链接 5文件)
        map.put("msg_from", "app");
        map.put("msg_content", creatJson(img));
        map.put("sid", PreferenceUtil.getInstance(this).getSessonId());
        url.sendImMsg(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(SuiShouNoteActivity.this).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
//                            localMsgId = msg.getMsg_id();
                            showMySendPhoto(img.getOriginal());

                            putImgToBox(img.getOriginal());
                        } else {
                            ToastUtils.getInstanc(SuiShouNoteActivity.this).showToast("发送失败");
                        }

                    }
                });
    }

    private void putImgToBox(String original) {

        ImageInfo info = new ImageInfo();
        info.bigImageUrl = original;
        info.thumbnailUrl = original;
        imageInfo.add(info);

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

    private void showMySendPhoto(String img) {
        MessageInfo msgInfo = new MessageInfo();
        msgInfo.setHeader(PreferenceUtil.getInstance(ctx).getHeadUrl());
        msgInfo.setName(PreferenceUtil.getInstance(ctx).getNickName());
        msgInfo.setFilepath(img);
        msgInfo.setImageUrl(img);
        msgInfo.setContent(img);
        msgInfo.setMsgType("1");
        msgInfo.setTime(new Date().getTime() + "");
        msgInfo.setMySend(1);

//        msgInfo.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
//        msgInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
        chatAdapter.insert(msgInfo, chatAdapter.getCount());
        chatAdapter.handler.post(new Runnable() {
            @Override
            public void run() {
                chatAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 录音上传完成后
     *
     * @param filePath
     */
    private void showMySendAudio(String filePath, String len) {


        final MessageInfo messageInfo = new MessageInfo();
        messageInfo.setMsgType("2");
        messageInfo.setHeader(PreferenceUtil.getInstance(ctx).getHeadUrl());
        messageInfo.setName(PreferenceUtil.getInstance(ctx).getNickName());
        messageInfo.setFilepath(filePath);
        messageInfo.setFileUrl(filePath);
        messageInfo.setTime(new Date().getTime() + "");
        messageInfo.setVoiceTime(Long.parseLong(len));
        messageInfo.setContent(filePath);

        chatAdapter.insert(messageInfo, chatAdapter.getCount());

        chatList.scrollToPosition(chatAdapter.getCount() - 1);

        chatAdapter.handler.post(new Runnable() {
            @Override
            public void run() {
                chatAdapter.notifyDataSetChanged();
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
        startActivity(new Intent(this, LeakActivity.class));
        finish();
    }


    private List<ImItemMsgBean> convert(List<RecentMsgBean.EventflowsBean> data) {
        List<ImItemMsgBean> dateList = new ArrayList<>();
        for (RecentMsgBean.EventflowsBean item : data) {
            ImItemMsgBean tt = item.getEvent();
            dateList.add(tt);
        }

        return dateList;
    }


    private static final int CROP_PHOTO = 2;
    private static final int REQUEST_CODE_PICK_IMAGE = 3;


    //------------------------------------------------
    private void takePhoto() {

        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .compress(true)
                .forResult(CROP_PHOTO);

    }


    @Override
    protected void onActivityResult(int req, int responseCode, Intent data) {
        super.onActivityResult(req, responseCode, data);
        switch (req) {
            case CROP_PHOTO:
                if (-1 == Activity.RESULT_OK) {
                    try {
                        LocalMedia phto = PictureSelector.obtainMultipleResult(data).get(0);
                        MessageInfo messageInfo = new MessageInfo();
                        messageInfo.setImageUrl(phto.getCompressPath());
                        messageInfo.setMsgType("1");//图片
                        EventBus.getDefault().post(messageInfo);
                    } catch (Exception e) {
                    }
                } else {
                    Log.d("amtf", "失败");
                }

                break;
            case REQUEST_CODE_PICK_IMAGE:
                if (AlBUMReturn == Activity.RESULT_OK) {
                    try {
                        Uri uri = data.getData();
                        MessageInfo messageInfo = new MessageInfo();
                        messageInfo.setImageUrl(getRealPathFromURI(uri));
                        messageInfo.setMsgType("1");//图片
                        EventBus.getDefault().post(messageInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("amtf", "失败");
                }

                break;

            default:
                break;
        }


    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = ctx.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void checkPemisson() {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            takePhoto();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 下次再次启动时，还会提示请求权限的对话框
                            Log.e("amtf", permission.name + " is denied. More info should be provided.用户拒绝了该权限，没有选中『不再询问』");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                        }
                    }
                });
    }
}
