package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.BaseBean;
import com.xinyu.newdiggtest.net.bean.BasePraiseBean;
import com.xinyu.newdiggtest.ui.BaseActivity;

import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.widget.MarkedImageView;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 频道评论页面
 */
public class ChannelCommentActivity extends BaseActivity {


    @BindView(R.id.webview)
    public WebView webView;

    @BindView(R.id.image_num)
    public MarkedImageView comment;

    @BindView(R.id.praise)
    public MarkedImageView praise;

    @BindView(R.id.send_comment)
    public ImageView send_comment;

    @BindView(R.id.input)
    public EditText input;

    int commentCount = 0, praiseCount = 0;

    boolean ifHasPraise = false;


    //--------------------头部-------------------------

    @BindView(R.id.title)
    public TextView title;


    @BindView(R.id.head)
    public ImageView head;

    @BindView(R.id.name)
    public TextView name;

    @BindView(R.id.time)
    public TextView time;


    public static final String IMG_HEARD = "<html><head><meta name=\"viewport\" content=\"width=device-width, " +
            "initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes\" />" +
            "<style>img{max-width:100% !important;height:auto !important;}</style>"
            + "<style>body{max-width:100% !important;}</style>" + "</head><body>";


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_chanel_comment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queryPraiseInfo();

        commentCount = Integer.parseInt(getNum(getIntent().getStringExtra("comment")));
        comment.setMessageNumber(commentCount);

        praiseCount = Integer.parseInt(getNum(getIntent().getStringExtra("praise")));
        praise.setMessageNumber(praiseCount);

        initView();

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() > 0) {
                    ifShowSend(true);
                } else {
                    ifShowSend(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    private void ifShowSend(boolean isHidden) {

        if (isHidden) {
            comment.setVisibility(View.GONE);
            praise.setVisibility(View.GONE);
            send_comment.setVisibility(View.VISIBLE);
        } else {
            comment.setVisibility(View.VISIBLE);
            praise.setVisibility(View.VISIBLE);
            send_comment.setVisibility(View.GONE);
        }


    }


    private String getNum(String input) {
        if (MyTextUtil.isEmpty(input)) {

            return "0";
        }
        return input;
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    private void initView() {

        initTop();

        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        mWebSettings.setJavaScriptEnabled(true);//是否允许JavaScript脚本运行，默认为false。设置true时，会提醒可能造成XSS漏洞
        mWebSettings.setSupportZoom(true);//是否可以缩放，默认true
        mWebSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        mWebSettings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mWebSettings.setAppCacheEnabled(true);//是否使用缓存
        mWebSettings.setDomStorageEnabled(true);//开启本地DOM存储
        mWebSettings.setLoadsImagesAutomatically(true); // 加载图片
        mWebSettings.setMediaPlaybackRequiresUserGesture(false);//播放音频，多媒体需要用户手动？设置为false为可自动播放


        String detailHtml = getIntent().getStringExtra("url");


        //设置不用系统浏览器打开,直接显示在当前Webview
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


        webView.loadData(IMG_HEARD + detailHtml, "text/html; charset=UTF-8", null);

        this.findViewById(R.id.icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });


        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    //按返回键操作并且能回退网页
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        //后退
                        webView.goBack();
                        return true;
                    }
                }
                return false;
            }


        });


    }

    private void initTop() {

        title.setText(getIntent().getStringExtra("title"));
        name.setText(getIntent().getStringExtra("name"));

        String timeStr = getIntent().getStringExtra("time");
        time.setText(timeStr.substring(0, timeStr.length() - 3));

        String headUrl = getIntent().getStringExtra("head");

        Picasso.with(mContext).load(headUrl).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(head);


    }


    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();

    }


    @OnClick(R.id.image_num)
    public void goComment() {

        Intent intent = getIntent();
        intent.setClass(mContext, ChanPraiseListActivity.class);
        startActivity(intent);

    }


    @OnClick(R.id.send_comment)
    public void addComment() {

        if (MyTextUtil.isEmpty(input.getText().toString())) {
            ToastUtils.getInstanc().showToast("请输入评论内容!");
            return;
        }
        doInsertCms();

    }

    private void doInsertCms() {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("f_plugin_type_id", getIntent().getStringExtra("chanel_plugin_type_id"));
        requsMap.put("f_plugin_id", getIntent().getStringExtra("contentId"));

        requsMap.put("f_comment", input.getText().toString());

        requsMap.put("f_title", getIntent().getStringExtra("title"));
        requsMap.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());
        requsMap.put("f_from_create", getIntent().getStringExtra("userId"));


        url.insetComments(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            input.setText("");

                            freshMsgCount(msg);

                        }
                    }
                });


    }


    /**
     * 刷新评论红点
     *
     * @param msg
     */
    private void freshMsgCount(BaseBean msg) {

        ToastUtils.getInstanc().showToast("评论成功了!");

        commentCount++;
        comment.setMessageNumber(commentCount);

    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == EventConts.BD_Comment_Refresh) {
            commentCount++;
            comment.setMessageNumber(commentCount);
        }

    }


    /**
     * 查询评论情况
     */
    private void queryPraiseInfo() {

        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("f_plugin_type_id", getIntent().getStringExtra("chanel_plugin_type_id"));
        requsMap.put("f_plugin_id", getIntent().getStringExtra("contentId"));

        url.getPraiseInfo(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BasePraiseBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(BasePraiseBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getIslike().equals("true")) {

                                ifHasPraise = true;
                                praise.setImageResource(R.mipmap.bd_zan);
                            } else {
                                ifHasPraise = false;
                                praise.setImageResource(R.mipmap.bd_zan_no);
                            }


                        }
                    }
                });
    }


    /**
     * 检查点赞
     */
    @OnClick(R.id.praise)
    public void checkPraise() {

        if (!ifHasPraise) {
            doPriase();
        } else {
            unDoPriase();
        }
    }


    /**
     * 点赞
     */
    private void doPriase() {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("f_plugin_type_id", getIntent().getStringExtra("chanel_plugin_type_id"));
        requsMap.put("f_plugin_id", getIntent().getStringExtra("contentId"));

        url.doPraise(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            ifHasPraise = true;
                            ToastUtils.getInstanc().showToast("点赞成功!");

                            praiseCount++;
                            praise.setImageResource(R.mipmap.bd_zan);
                            praise.setMessageNumber(praiseCount);

                        }


                    }
                });
    }


    /**
     * 点赞
     */
    private void unDoPriase() {
        loadingDailog.show();

        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("f_plugin_type_id", getIntent().getStringExtra("chanel_plugin_type_id"));
        requsMap.put("f_plugin_id", getIntent().getStringExtra("contentId"));

        url.unDoPraise(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {
                            ifHasPraise = false;
                            ToastUtils.getInstanc().showToast("取消点赞成功!");
                            praiseCount--;
                            praise.setImageResource(R.mipmap.bd_zan_no);
                            praise.setMessageNumber(praiseCount);

                        }
                    }
                });
    }


}




