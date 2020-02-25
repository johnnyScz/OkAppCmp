package com.xinyu.newdiggtest.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.DakaBottowItem;
import com.xinyu.newdiggtest.bean.DakaInfoBean;
import com.xinyu.newdiggtest.bean.FollowListBean;
import com.xinyu.newdiggtest.bean.NineImgBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.net.bean.InfoData;
import com.xinyu.newdiggtest.ui.Digg.fragment.MyDialog;
import com.xinyu.newdiggtest.ui.Digg.fragment.RewardActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.SelectGroupActivity;
import com.xinyu.newdiggtest.ui.circle.FavortItem;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyMiddleDialog;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.NetApiUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.view.CommentItem;
import com.xinyu.newdiggtest.view.CommentListView;
import com.xinyu.newdiggtest.widget.CommentPopupWindow;
import com.xinyu.newdiggtest.widget.DakaDashangListView;
import com.xinyu.newdiggtest.widget.PraiseListView;
import com.xinyu.newdiggtest.widget.SoftInputPopUtil;
import com.xinyu.newdiggtest.wxapi.WeixinUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 首页打卡-进入打卡详情
 */
public class DakaInfoActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.iv_share)
    public ImageView share;

    //分享相关
    String targetName = "", startTime = "", endTime = "";

    String dakaContent = "", headUrl, wechatContent;

    String vip = "";

    String userId = "", planId = "";
    //----重新设置item----

    @BindView(R.id.iv_target_icon)
    public ImageView icon;

    @BindView(R.id.tv_daka_title)
    public TextView dakaTitle;

    @BindView(R.id.tv_descrep)
    public TextView content;//打卡内容

    @BindView(R.id.mycontent)
    public TextView commetn;//评论

    @BindView(R.id.nineGrid)
    public NineGridView nineGridView;//打卡图片

    @BindView(R.id.time_tv)
    public TextView time;//打卡时间


    @BindView(R.id.iv_common_more)
    public ImageView commIcon;//评论


    @BindView(R.id.digCommentBody)
    public LinearLayout commentBody;//评论打赏体

    @BindView(R.id.praiseListView)
    public PraiseListView praiseTx;//点赞

    @BindView(R.id.dashanglistview)
    public DakaDashangListView dashangTx;//打赏

    @BindView(R.id.commentList)
    public CommentListView commonTx;//评论

    FollowListBean netBean;

    ExecutorService excutor = Executors.newCachedThreadPool();

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_daka_info;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListner();
    }

    private void initListner() {
        icon.setOnClickListener(this);
        dakaTitle.setOnClickListener(this);
    }


    private void initView() {
        planId = getIntent().getStringExtra(IntentParams.DAKA_Target_Id);
        userId = getIntent().getStringExtra(IntentParams.TO_USER);

    }


    @Override
    protected void onResume() {
        super.onResume();
        requestTheInfo();
    }

    /**
     *
     */

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
                goShareGroup();
            }

            @Override
            public void onShareFriend() {
                dialog.dismiss();
                ToastUtils.getInstanc(mContext).showToast("我是分享朋友");
            }

            @Override
            public void onShareWeixin() {
                dialog.dismiss();
                getLinkUrl(0);
            }

            @Override
            public void onShareCircle() {
                dialog.dismiss();
                getLinkUrl(1);
            }
        });

    }


    /**
     * 分享到群
     */
    private void goShareGroup() {
        Intent intent = new Intent(mContext, SelectGroupActivity.class);
        JSONObject object = new JSONObject();
        try {
            String content = "打卡！" + dakaContent;
            object.put("content", content);
            object.put("f_createname", netBean.getUserBean().getNickname());//目标创建者
            object.put("user_id", userId);
            object.put("class_id", netBean.getF_class_id());
            object.put("type", "daily");
            object.put("uuid", planId);
            object.put("name", targetName);
            object.put("state", "0");//0 立即结算 1 等待结算

        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent.putExtra(IntentParams.Target_Share_Content, object.toString());//分享内容
        startActivity(intent);
    }


    //请求数据
    private void requestTheInfo() {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("plan_id", planId);
        map.put("userId", userId);
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        url.getDakaInfo(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DakaInfoBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(DakaInfoBean msg) {
                        loadingDailog.dismiss();

                        String code = msg.getOp().getCode();
                        if (code.equals("Y")) {
                            if (msg.getData() != null) {
                                netBean = msg.getData();
                                showView(netBean);
                            }

                        } else if (code.equals("notfound")) {
                            showHasNoDialog();
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
//
                    }
                });
    }


    @OnClick(R.id.iv_back)
    public void goback() {
        finish();
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == 100) {
            ToastUtils.getInstanc(mContext).showToast("分享成功");
        }

    }

    Bitmap bitmap = null;

    private void goShareWechat(final String url) {

        final String content = MyTextUtil.isEmpty(wechatContent) ? "用户" + netBean.getUserBean().getNickname() + " " +
                netBean.getF_plan_date() + "发布了打卡" : wechatContent;

        excutor.execute(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                try {
                    imageurl = new URL(headUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bitmap != null) {
                        shareFinal(content, bitmap, url);
                    }
                }
            }
        });


    }

    private void shareFinal(String content, Bitmap bitmap, String url) {
        WeixinUtil.getInstance().
                diggWxShare
                        (this, url, targetName,
                                content, bitmap, false);
    }


    /**
     * TODO 如果是分享目标
     * <p>
     * 就是传target_uuid,类项传３
     */

    private void getLinkUrl(final int isCircle) {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_type", "customer");
        map.put("use_to", "4");
        map.put("user_id", userId);
        map.put("plan_id", planId);
        url.getLinkUrl(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InfoData>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(InfoData msg) {
                        if (!MyTextUtil.isEmpty(msg.getUrl())) {
                            if (isCircle == 0) {
                                goShareWechat(msg.getUrl());
                            } else if (isCircle == 1) {
                                goCircle(msg.getUrl());
                            }

                        }
                    }
                });
    }


    /**
     * 分享到朋友圈
     *
     * @param pageUrl
     */
    private void goCircle(String pageUrl) {

        WeixinUtil.getInstance().sendWebPage(mContext, pageUrl, "一起OK！",
                "快来围观我的打卡~" +
                        targetName + " #" +
                        DateUtil.timeOnlyDot(startTime) + "-" + DateUtil.timeOnlyDot(endTime) + " #", R.mipmap.ic_launcher, true);


    }


    private void showView(FollowListBean data) {

        initShare(data);

        getTime(data);

        headUrl = data.getUserBean().getHead();

        Picasso.with(mContext).load(headUrl).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(icon);


        targetName = TextUtils.isEmpty(data.getF_target_name()) ? "今日打卡" : data.getF_target_name();
        dakaContent = "#" + targetName + " " + getTargetShowTime(data) + "#";

        wechatContent = data.getF_content();
        dakaTitle.setText("#" + targetName + " " + getTargetShowTime(data) + "#");

        String contentTx = null;
        try {
            contentTx = URLDecoder.decode(data.getF_content(), "UTF-8");
            content.setText(contentTx);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        String showContent = "";
        if (!MyTextUtil.isEmpty(data.getF_comment())) {
            try {
                showContent = URLDecoder.decode(data.getF_comment(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            commetn.setText(showContent);
        }


        if (TextUtils.isEmpty(data.getF_img())) {
            nineGridView.setVisibility(View.GONE);
        } else {
            nineGridView.setVisibility(View.VISIBLE);
            String temp = data.getF_img();
            List<NineImgBean> imgList = new ArrayList<>();
            try {
                JSONArray array = new JSONArray(temp);
                if (array == null || array.length() < 1)
                    return;
                int len = array.length();

                for (int i = 0; i < len; i++) {
                    JSONObject object = array.getJSONObject(i);
                    NineImgBean nineBean = new NineImgBean(object.getString("original"), object.getString("thumbnail"));
                    imgList.add(nineBean);
                }
                showImgs(nineGridView, imgList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (TextUtils.isEmpty(data.getF_finish_time()) || data.getF_finish_time() == null) {
            time.setVisibility(View.GONE);
        } else {
            time.setVisibility(View.VISIBLE);
            String timeTx = data.getF_finish_time();
            String nTime = timeTx.substring(timeTx.length() - 8, timeTx.length() - 3);
            time.setText(nTime);
        }

//        /**
//         *评论和点赞
//         */
//
        initPop(data);

        boolean noLikes = (data.getTargetlikes() == null || data.getTargetlikes().size() == 0);
        boolean noComment = (data.getTargetcomment() == null || data.getTargetcomment().size() == 0);
        boolean noDashangMoney = (data.getExcitation() == null || data.getExcitation().size() == 0);
//

        if (noLikes && noComment && noDashangMoney) {
            commentBody.setVisibility(View.GONE);
        } else {
            commentBody.setVisibility(View.VISIBLE);
        }
//

//
        if (data.getTargetlikes() != null && data.getTargetlikes().size() > 0) {
            praiseTx.setVisibility(View.VISIBLE);
            praiseTx.setDatas(convertPaise(data.getTargetlikes()));

        } else {
            praiseTx.setVisibility(View.GONE);
        }

        if (data.getExcitation() == null || data.getExcitation().size() == 0) {
            dashangTx.setVisibility(View.GONE);
        } else {
            dashangTx.setVisibility(View.VISIBLE);
            dashangTx.setDatas(data.getExcitation());
        }
        if (data.getTargetcomment() != null && data.getTargetcomment().size() > 0) {
            commonTx.setVisibility(View.VISIBLE);
            commonTx.setDatas(conVertData(data.getTargetcomment(), "1"));
        } else {
            commonTx.setVisibility(View.GONE);
        }
//
    }

    private void initShare(FollowListBean data) {
        String dakaState = data.getF_state();
        vip = MyTextUtil.isEmpty(data.getUserBean().getBecome_vip_date()) ? "0" : "1";
        if (dakaState.equals("0")) {
            share.setVisibility(View.GONE);
        } else if (dakaState.equals("1")) {
            share.setVisibility(View.VISIBLE);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    go2Share();
                }
            });
        }


    }

    private void getTime(FollowListBean data) {
        startTime = data.getF_target_start_date();
        endTime = data.getF_target_end_date();
    }

    private void initPop(final FollowListBean data) {
        String type = "";
        if (data.getTargetlikes() != null && data.getTargetlikes().size() > 0) {
            List<DakaBottowItem> dakaitem = data.getTargetlikes();
            for (DakaBottowItem kk : dakaitem) {
                if (kk.getF_like_user().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                    type = "1";
                    break;
                } else {
                    type = "0";
                }
            }

        } else {
            type = "0";
        }

        boolean isShow;
        if (data.getF_executor().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
            isShow = false;
        } else {
            isShow = true;
        }
        final CommentPopupWindow pop = new CommentPopupWindow(mContext, type, isShow);
        pop.update();
        pop.setmItemClickListener(new CommentPopupWindow.OnItemClickListener() {
            @Override
            public void onItemClick(String content) {
                if (content.equals("赞") || content.equals("取消")) {
                    if (content.equals("赞")) {
                        dianZan(data);
                    } else if (content.equals("取消")) {
                        canCelZan(data);
                    }
                } else if (content.equals("打赏")) {
                    if (!vip.equals("1")) {
                        NetApiUtil.sendAppVipMissNotice(mContext, "13", data.getF_executor());
                        ToastUtils.getInstanc().showToast("您的好友不是Vip用户，不能被打赏哦~");
                        return;
                    }
                    Intent intent = new Intent(mContext, RewardActivity.class);
                    intent.putExtra(IntentParams.Target_Name, data.getF_target_name());
                    intent.putExtra(IntentParams.DAKA_Target_Id, data.getF_plan_id());
                    intent.putExtra(IntentParams.STATE_DATE, data.getF_target_start_date());
                    intent.putExtra(IntentParams.END_DATE, data.getF_target_end_date());
                    intent.putExtra(IntentParams.Intent_Enter_Type, "daka_dashang");//打卡打赏
                    intent.putExtra(IntentParams.TO_USER, data.getF_executor());
                    mContext.startActivity(intent);

                } else if (content.equals("评论")) {
                    SoftInputPopUtil.liveCommentEdit(mContext, commIcon, new SoftInputPopUtil.liveCommentResult() {
                        @Override
                        public void onResult(boolean confirmed, String comment) {
                            doRequestComment(data, comment);
                        }
                    });

                }
            }
        });


        this.findViewById(R.id.iv_common_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.showPopupWindow(v);
            }
        });
    }

    private List<FavortItem> convertPaise(List<DakaBottowItem> likes) {
        List<FavortItem> mdate = new ArrayList<>();
        FavortItem tt;
        for (DakaBottowItem item : likes) {
            tt = new FavortItem();
            tt.setName(item.getF_nick_name());
            tt.setContent(item.getF_comment());
            mdate.add(tt);
        }
        return mdate;
    }

    private List<CommentItem> conVertData(List<DakaBottowItem> targetcomment, String type) {
        int len = targetcomment.size();
        List<CommentItem> list = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            DakaBottowItem daka = targetcomment.get(i);
            CommentItem item = new CommentItem();
            item.setCommentUser(daka.getF_nick_name());
            item.setType(type);
            try {
                String content = URLDecoder.decode(daka.getF_comment(), "UTF-8");
                item.setContent(content);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            list.add(item);
        }
        return list;
    }

    private void showImgs(NineGridView netView, List<NineImgBean> imageDetails) {
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        if (imageDetails != null) {
            for (NineImgBean imageDetail : imageDetails) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(imageDetail.getThumbnail());
                info.setBigImageUrl(imageDetail.getOriginal());
                imageInfo.add(info);
            }
        }
        netView.setAdapter(new NineGridViewClickAdapter(mContext, imageInfo));
        //单张图大小
        netView.setSingleImageSize(dp2px(mContext, 150));
    }

    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private String getTargetShowTime(FollowListBean item) {
        //TODO 不同类型 拿时间的字段不同
        String start = item.getF_target_start_date();
        String end = item.getF_target_end_date();
        if (start == null || TextUtils.isEmpty(start) || end == null || TextUtils.isEmpty(end)) {
            return "";
        }
        String relax = start.substring(4, 5);
        String nStart = start.replaceAll(relax, ".");
        String nEnd = end.replaceAll(relax, ".");
        return nStart.substring(5, start.length()) + "-" + nEnd.substring(5, start.length());
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_target_icon:
//                Intent mIntent = new Intent(mContext, MySpaceActivity.class);
//                mIntent.putExtra(IntentParams.USER_ID, netBean.getF_executor());
//                mIntent.putExtra(IntentParams.USER_head, headUrl);
//                mIntent.putExtra(IntentParams.UserName, netBean.getUserBean().getNickname());
//                mIntent.putExtra(IntentParams.STATE, "0");
//                startActivity(mIntent);
                break;

            case R.id.tv_daka_title:
                Intent intent = new Intent(mContext, TargetNewInfoActivity.class);
                intent.putExtra(IntentParams.DAKA_Target_Id, netBean.getF_target_uuid());

                startActivity(intent);
                break;
        }

    }


    private void dianZan(FollowListBean item) {
        if (item != null) {

            RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
            AppUrl api = manager.create(AppUrl.class);


            HashMap<String, String> requsMap = new HashMap<>();
            requsMap.put("object_id", item.getF_plan_id());
            requsMap.put("like_user_id", PreferenceUtil.getInstance(mContext).getUserId());
            requsMap.put("type", "1");
            requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
            requsMap.put("target_name", item.getF_target_name());
            requsMap.put("start_date", item.getF_target_start_date());
            requsMap.put("end_date", item.getF_target_end_date());
            requsMap.put("like_user_name", PreferenceUtil.getInstance(mContext).getNickName());
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
                                requestTheInfo();
                            } else {
                                ToastUtils.getInstanc(mContext).showToast("点赞失败");
                            }
                        }
                    });


        }


    }

    /**
     * 添加用户评论
     */
    private void doRequestComment(FollowListBean commentBean, String comments) {
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
            requsMap.put("start_date", commentBean.getF_target_start_date());
            requsMap.put("end_date", commentBean.getF_target_end_date());
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
                                requestTheInfo();
                            } else {
                                ToastUtils.getInstanc(mContext).showToast("评论失败");
                            }
                        }
                    });

        }
    }


    private void canCelZan(FollowListBean item) {

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
//                            ToastUtils.getInstanc(mContext).showToast(e.getMessage());

                        }

                        @Override
                        public void onNext(Info msg) {
                            if (msg.getOp().getCode().equals("Y")) {
                                ToastUtils.getInstanc(mContext).showToast("取消成功");
                                requestTheInfo();
                            } else {
                                ToastUtils.getInstanc(mContext).showToast("服务异常");
                            }
                        }
                    });


        }


    }


    MyMiddleDialog myMiddleDialog;


    /**
     * 展示目标已结束的dialog
     */
    private void showHasNoDialog() {
        if (myMiddleDialog == null) {
            myMiddleDialog = new MyMiddleDialog(this, R.style.MyMiddleDialogStyle) {
                @Override
                protected View getView() {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    View view = inflater.inflate(R.layout.dialog_target_delet, null);
                    initDialogView(view);
                    return view;
                }
            };
        }
        myMiddleDialog.show();
    }

    private void initDialogView(View view) {
        view.findViewById(R.id.conform).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMiddleDialog.dismiss();
                finish();

            }
        });

    }


}






