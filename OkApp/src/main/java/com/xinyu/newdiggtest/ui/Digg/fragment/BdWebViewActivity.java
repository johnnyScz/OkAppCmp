package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xinyu.newdiggtest.R;

import com.xinyu.newdiggtest.bean.CommentReturnBean;
import com.xinyu.newdiggtest.net.ApiConfig;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.BaseBean;
import com.xinyu.newdiggtest.net.bean.BasePraiseBean;
import com.xinyu.newdiggtest.ui.BaseActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.DisplayUtils;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BdWebViewActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.webview)
    WebView webView;


//    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;


    @BindView(R.id.add)
    ImageView add;


    @BindView(R.id.zan)
    ImageView zan;

    @BindView(R.id.count_zan)
    TextView count_zan;

    @BindView(R.id.comment_count)
    TextView comment_count;


    @BindView(R.id.praise_ll)
    View praise_ll;

    @BindView(R.id.commet_ll)
    View commet_ll;

    @BindView(R.id.cance_tx)
    View cance_tx;


    @BindView(R.id.bottview)
    View bottview;


    String likeCount = "0";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        initPop();
        initRecycle();

        queryPraiseInfo();

        getPraiselist();
    }

    private void showBotton(boolean ifCancelHaden) {

        if (ifCancelHaden) {
            add.setVisibility(View.VISIBLE);
            praise_ll.setVisibility(View.VISIBLE);
            commet_ll.setVisibility(View.VISIBLE);
            cance_tx.setVisibility(View.INVISIBLE);
        } else {
            add.setVisibility(View.GONE);
            praise_ll.setVisibility(View.GONE);
            commet_ll.setVisibility(View.GONE);
            cance_tx.setVisibility(View.VISIBLE);
        }


    }


    private void initRecycle() {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
//
//
//        layoutManager.setSmoothScrollbarEnabled(true);
//        layoutManager.setAutoMeasureEnabled(true);
//        recyclerView.setNestedScrollingEnabled(false);

        /**
         * todo  后面可能会要追加
         */

//        List<String> datas = new ArrayList<>();
//
//        for (int i = 0; i < 10; i++) {
//            datas.add("条目" + i);
//        }
//
//        TestAdapter adapter = new TestAdapter(R.layout.item_test1, datas);
//
//        recyclerView.setAdapter(adapter);


    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_biaodan;
    }


    private void initView() {


        String url = getIntent().getStringExtra("url");
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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.loadUrl(url);
        //设置不用系统浏览器打开,直接显示在当前Webview
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
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

    @OnClick(R.id.add)
    public void showPop() {
        if (!popupWindow.isShowing()) {

            popupWindow.showAsDropDown(bottview, Gravity.LEFT, 0, DisplayUtils.dp2px(mContext, 200));

            showBotton(false);

        }

    }


    PopupWindow popupWindow;

    private void initPop() {

        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.pop_biaodan, null);

        setListner(view);

        popupWindow = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                DisplayUtils.dp2px(mContext, 300), false);


        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                showBotton(true);


            }
        });


    }

    /**
     * 视图
     *
     * @param view
     */
    private void setListner(LinearLayout view) {

        view.findViewById(R.id.weixin).setOnClickListener(this);
        view.findViewById(R.id.geren).setOnClickListener(this);
        view.findViewById(R.id.qunzu).setOnClickListener(this);
        view.findViewById(R.id.dashang).setOnClickListener(this);
        view.findViewById(R.id.quxian).setOnClickListener(this);
        view.findViewById(R.id.setting).setOnClickListener(this);
        view.findViewById(R.id.logs).setOnClickListener(this);
        view.findViewById(R.id.todo).setOnClickListener(this);

        view.findViewById(R.id.feixin).setOnClickListener(this);

        view.findViewById(R.id.link).setOnClickListener(this);

        view.findViewById(R.id.print).setOnClickListener(this);

        view.findViewById(R.id.alram).setOnClickListener(this);


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


    private void goShareLittleWx() {


        try {

            WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();

            miniProgramObj.webpageUrl = "http://www.qq.com";


            miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2

            miniProgramObj.userName = ApiConfig.WxLittle;     // 小程序原始id

            //TODO 拼接上表单的url
            miniProgramObj.path = "pages/form/form?formUrl=" + getIntent().getStringExtra("url");

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

    @Override
    public void onClick(View v) {

        popupWindow.dismiss();

        int id = v.getId();

        switch (id) {

            case R.id.weixin:

                goShareLittleWx();

                break;

            case R.id.geren:
                ToastUtils.getInstanc().showToast("分享到个人");

                break;

            case R.id.qunzu:
                ToastUtils.getInstanc().showToast("分享到群组");
                break;

            case R.id.dashang:

                ToastUtils.getInstanc().showToast("打赏");

                break;

            case R.id.quxian:
                ToastUtils.getInstanc().showToast("权限控制");

                break;

            case R.id.setting:
                ToastUtils.getInstanc().showToast("设置");
                break;

            case R.id.logs:
                ToastUtils.getInstanc().showToast("日志");
                break;

            case R.id.todo:

                ToastUtils.getInstanc().showToast("新建待办");
                break;


            case R.id.feixin:

                ToastUtils.getInstanc().showToast("飞信");
                break;

            case R.id.link:

                ToastUtils.getInstanc().showToast("链接");
                break;

            case R.id.print:

                ToastUtils.getInstanc().showToast("打印");
                break;


            case R.id.alram:

                ToastUtils.getInstanc().showToast("举报");
                break;

        }

    }


    boolean ifHasPraise;

    /**
     * 查询点赞情况
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


                            likeCount = msg.getLikecount();


                            if (msg.getIslike().equals("true")) {

                                ifHasPraise = true;
                                zan.setImageResource(R.mipmap.bd_zan);

                            } else {
                                ifHasPraise = false;
                                zan.setImageResource(R.mipmap.bd_zan_no);
                            }

                            count_zan.setText(likeCount);
                        }
                    }
                });
    }


    @OnClick(R.id.zan)
    public void IfZan() {

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
                            zan.setImageResource(R.mipmap.bd_zan);
                            int count = Integer.parseInt(count_zan.getText().toString());
                            count_zan.setText((count + 1) + "");

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
                            zan.setImageResource(R.mipmap.bd_zan_no);

                            int count = Integer.parseInt(count_zan.getText().toString());
                            count_zan.setText((count - 1) + "");

                        }
                    }
                });
    }


    @OnClick(R.id.back)
    public void back() {
        finish();
    }


    private void getPraiselist() {

        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        requsMap.put("f_plugin_type_id", getIntent().getStringExtra("chanel_plugin_type_id"));
        requsMap.put("f_plugin_id", getIntent().getStringExtra("contentId"));

        requsMap.put("size", "500");
        requsMap.put("current", "1");

        url.queryCommentList(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommentReturnBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(CommentReturnBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() != null && msg.getData().size() > 0) {

                                comment_count.setText(msg.getData().size() + "");
                            } else {
                                comment_count.setText("0");
                            }
                        }
                    }
                });


    }


    @OnClick(R.id.commet_ll)
    public void goComment() {

        Intent intent = getIntent();
        intent.setClass(mContext, ChanPraiseListActivity.class);
        startActivity(intent);


    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == EventConts.BD_Comment_Refresh) {
            String count = comment_count.getText().toString();
            int cc = Integer.parseInt(count) + 1;
            comment_count.setText(cc + "");
        }

    }


}
