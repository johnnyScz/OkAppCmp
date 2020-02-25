//package com.xinyu.newdigg.ui.circle;
//
//
//import android.graphics.Rect;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewTreeObserver;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.xinyu.newdigg.R;
//import com.xinyu.newdigg.ui.BaseActivity;
//import com.xinyu.newdigg.ui.XshellEvent;
//import com.xinyu.newdigg.utils.CommonUtils;
//import com.xinyu.newdigg.utils.DisplayUtils;
//import com.xinyu.newdigg.utils.ToastUtils;
//import com.xinyu.newdigg.widget.DivItemDecoration;
//
//
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.List;
//
//import butterknife.BindView;
//
//public class CircleActivity extends BaseActivity {
//
//    RecyclerView recyclerView;
//    private LinearLayoutManager layoutManager;
//
//    CircleAdapter circleAdapter;
//
//    @BindView(R.id.editText_keyboard)
//    public LinearLayout edittextbody;
//
//    @BindView(R.id.circleEt)
//    public EditText editText;
//
//    @BindView(R.id.sendIv)
//    public ImageView sendIv;
//
//    @BindView(R.id.bodyLayout)
//    public RelativeLayout bodyLayout;
//
//    //-----------控制高度----------------
//    private int screenHeight;
//    private int editTextBodyHeight;
//    private int currentKeyboardH;
//    private int selectCircleItemH;
//    private int selectCommentItemOffset;
//    private CommentConfig commentConfig;
//
//
//    @Override
//    protected int getLayoutResouce() {
//        return R.layout.activity_cicle;
//    }
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        initView();
//
//        List<CircleItem> datae = DemoDatasUtil.createCircleDatas();
//        circleAdapter.setDatas(datae);
//        circleAdapter.notifyDataSetChanged();
//    }
//
//    private void initView() {
//        recyclerView = findViewById(R.id.recyclerView);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new DivItemDecoration(2, true));
//
//
//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (edittextbody.getVisibility() == View.VISIBLE) {
//                    updateEditTextBodyVisible(View.GONE);
//                    return true;
//                }
//                return false;
//            }
//        });
//
//
//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    Glide.with(mContext).resumeRequests();
//                } else {
//                    Glide.with(mContext).pauseRequests();
//                }
//
//            }
//        });
//
//        circleAdapter = new CircleAdapter(this);
//
//        recyclerView.setAdapter(circleAdapter);
//
//        sendIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //发布评论
//                String content = editText.getText().toString().trim();
//                if (TextUtils.isEmpty(content)) {
//                    ToastUtils.getInstanc(mContext).showToast("评论内容不能为空");
//                    return;
//                }
//                //TODO 发表评论
//
//                goComment();
//
//                updateEditTextBodyVisible(View.GONE);
//            }
//        });
//        setViewTreeObserver();
//    }
//
//
//    /**
//     * 网络请求，发表评论
//     */
//    private void goComment() {
//
//
//    }
//
//
//    public void updateEditTextBodyVisible(int visibility) {
//        edittextbody.setVisibility(visibility);
//        //  measureCircleItemHighAndCommentItemOffset(commentConfig);
//        if (View.VISIBLE == visibility) {
//            editText.requestFocus();
//            //弹出键盘
//            CommonUtils.showSoftInput(editText.getContext(), editText);
//
//        } else if (View.GONE == visibility) {
//            //隐藏键盘
//            CommonUtils.hideSoftInput(editText.getContext(), editText);
//        }
//    }
//
//
//    private void setViewTreeObserver() {
//        final ViewTreeObserver swipeRefreshLayoutVTO = bodyLayout.getViewTreeObserver();
//        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//
//                Rect r = new Rect();
//                bodyLayout.getWindowVisibleDisplayFrame(r);
//                int statusBarH = getStatusBarHeight();//状态栏高度
//                int screenH = bodyLayout.getRootView().getHeight();
//                if (r.top != statusBarH) {
//                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
//                    r.top = statusBarH;
//                }
//                int keyboardH = screenH - (r.bottom - r.top);
//
//                if (keyboardH == currentKeyboardH) {//有变化时才处理，否则会陷入死循环
//                    return;
//                }
//
//                currentKeyboardH = keyboardH;
//                screenHeight = screenH;//应用屏幕的高度
//                editTextBodyHeight = edittextbody.getHeight();
//
//                if (keyboardH < 150) {//说明是隐藏键盘的情况
//                    updateEditTextBodyVisible(View.GONE);
//                    return;
//                }
//                //偏移listview
//                if (layoutManager != null && commentConfig != null) {
//                    layoutManager.scrollToPositionWithOffset(commentConfig.circlePosition /*+1*/, getListviewOffset(commentConfig));
//                }
//            }
//        });
//    }
//
//
//    /**
//     * 测量偏移量
//     *
//     * @param commentConfig
//     * @return
//     */
//    private int getListviewOffset(CommentConfig commentConfig) {
//        if (commentConfig == null)
//            return 0;
//        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
//        //int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight;
//        //TODO 减去titleBar的高度
//        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight - /*titleBar.getHeight()*/
//                DisplayUtils.dp2px(this, 40);
//
//        return listviewOffset;
//    }
//
//
//    /**
//     * 获取状态栏高度
//     *
//     * @return
//     */
//    private int getStatusBarHeight() {
//        int result = 0;
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = getResources().getDimensionPixelSize(resourceId);
//        }
//        return result;
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onXshellEvent(XshellEvent event) {
//
//    }
//}
//
//
//
//
