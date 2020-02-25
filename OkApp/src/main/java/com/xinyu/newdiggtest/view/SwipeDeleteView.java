package com.xinyu.newdiggtest.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * 侧滑删除
 */
public class SwipeDeleteView extends ViewGroup{

    private int mScaleTouchSlop;//为了处理单击事件的冲突(超过这个值即判断此次动作是在滑动。我们利用这个值判断是否处于侧滑)
    private int mMaxSpeed;//计算滑动速度
    private int mMaxWidth;//父控件留给自己的最大的水平空间
    private int mRightMenuWidth;//右侧菜单宽度总和(即：最大滑动距离)
    private int mHeight;//自己的高度

    private int mLimit;//滑动判断临界值（这里取右侧菜单宽度的40%为判断基准）手指抬起时，超过这个基准值则侧滑展开，反之，收起
    //增加一个提供可选的开关，左滑还是右滑
    private boolean isLeftSwipe = true;//左滑
    //右滑删除功能的开关，默认开
    private boolean isSwipeEnable = true;//开启右侧菜单
    private VelocityTracker mVelocityTracker;//滑动速度变量（VelocityTracker主要用跟踪触摸屏事件（flinging事件和其他gestures手势事件）的速率）
    /**
     * 仿QQ，侧滑菜单展开时，点击除侧滑菜单之外的区域，关闭侧滑菜单。
     * 增加一个布尔值变量，dispatch函数里，每次down时，为true，move时判断，如果是滑动动作，设为false。
     * 在Intercept函数的up时，判断这个变量，如果仍为true 说明是点击事件，则关闭菜单。
     */
    private boolean isUnMoved = true;
    //防止多只手指一起滑我的flag 在每次down里判断， touch事件结束清空（即使是多手指一起滑，其实也是有先后顺序的）
    private static boolean isTouching;//默认false（但是应该声明为成员变量或是静态变量）


    public SwipeDeleteView(Context context) {
        this(context,null);
    }

    public SwipeDeleteView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwipeDeleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化一些操作
        /**
         * getScaledTouchSlop是一个距离，表示滑动的时候，
         * 手的移动要大于这个距离才开始移动控件。如果小于这个距离就不触发移动控件，如viewpager就是用这个距离来判断用户是否翻页
         */
        mScaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();//系统方法
        mMaxSpeed = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();//最大加速度
    }

    public boolean isSwipeEnable() {
        return isSwipeEnable;
    }

    /**
     * 设置侧滑功能开关
     *
     * @param swipeEnable
     */
    public void setSwipeEnable(boolean swipeEnable) {
        isSwipeEnable = swipeEnable;
    }


    public boolean isIos() {
        return isIos;
    }

    /**
     * 设置是否开启IOS阻塞式交互
     *
     * @param ios
     */
    public SwipeDeleteView setIos(boolean ios) {
        isIos = ios;
        return this;
    }

    public boolean isLeftSwipe() {
        return isLeftSwipe;
    }

    /**
     * 设置是否开启左滑出菜单，设置false 为右滑出菜单
     *
     * @param leftSwipe
     * @return
     */
    public SwipeDeleteView setLeftSwipe(boolean leftSwipe) {
        isLeftSwipe = leftSwipe;
        return this;
    }

    /**
     * 返回ViewCache
     *
     * @return
     */
    public static SwipeDeleteView getViewCache() {
        return mViewCache;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //最大宽度根据父控件计算出，如果没有父控件就用屏幕宽度
        ViewParent parent = getParent();
        if(parent != null && parent instanceof ViewGroup){
            ViewGroup viewGroup = (ViewGroup) parent;
            mMaxWidth = viewGroup.getMeasuredWidth() - viewGroup.getPaddingLeft() - viewGroup.getPaddingRight();//去掉左右内边距（水平方向上）
        }else{
            //屏幕宽度
            mMaxWidth = getResources().getDisplayMetrics().widthPixels;
        }

        mRightMenuWidth = 0;//由于ViewHolder的复用机制，每次这里要手动恢复初始值
        int childCount = getChildCount();
        //为了子View的高，可以设置为matchParent(参考的FrameLayout 和LinearLayout的Horizontal)
        final boolean measureMatchParentChildren = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
        boolean isNeedMeasureChildHeight = false;

        for(int i=0;i<childCount;i++){
            View childView = getChildAt(i);
            if(childView.getVisibility() != GONE){//view对象可见的话
                //某一个子view，多宽，多高, 内部加上了viewGroup的padding值、margin值和传入的宽高wUsed、hUsed
                measureChildWithMargins(childView,widthMeasureSpec,0,heightMeasureSpec,0);// 测量某一个child的宽高，考虑margin、padding值的情况,将margin的值也作为子view对象里
                final MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
                mHeight = Math.max(mHeight, childView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);//如果含有margin值，则取带margin值的数据
                if(measureMatchParentChildren && lp.height == LayoutParams.MATCH_PARENT){
                    isNeedMeasureChildHeight = true;
                }
                if(i > 0){//第一个布局是左边菜单，即：listview的item显示数据，从第二个开始才是侧滑出来的菜单，即：右边菜单
                    //侧滑菜单可能还有：删除、置顶等若干项，它们加起来才等于一个item中RightMenu宽度
                    mRightMenuWidth += childView.getMeasuredWidth();
                }
            }
        }
        setMeasuredDimension(mMaxWidth,mHeight);//报告我们最终计算出的宽高
        mLimit = mRightMenuWidth * 4 / 10;//以侧滑菜单宽度的40%为临界判断

        if(isNeedMeasureChildHeight){//如果子View的height有MatchParent属性的，设置子View高度
            //给设置为MatchParent的子View设置高度
            forceUniformHeight(childCount, widthMeasureSpec);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    private void forceUniformHeight(int count, int widthMeasureSpec) {
        // Pretend that the linear layout has an exact size. This is the measured height of
        // ourselves. The measured height should be the max height of the children, changed
        // to accommodate the heightMeasureSpec from the parent
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(),
                MeasureSpec.EXACTLY);//以父布局高度构建一个Exactly的测量参数
        for (int i = 0; i < count; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    // Temporarily force children to reuse their old measured width
                    // FIXME: this may not be right for something like wrapping text?
                    int oldWidth = lp.width;//measureChildWithMargins 这个函数会用到宽，所以要保存一下
                    lp.width = child.getMeasuredWidth();
                    // Remeasure with new dimensions
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                    lp.width = oldWidth;
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * 确定子View的位置
     * @param changed
     * @param l   left
     * @param t   top
     * @param r   right
     * @param b   bottom
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left = 0 + getPaddingLeft();
        int right = 0;
        for(int i=0;i<childCount;i++){
            View childView = getChildAt(i);
            if(childView.getVisibility() != GONE){//可见
                if(i == 0){//得到的是第一个子View，根据我们的item布局文件可以看出，第一个子View就是显示listview数据的，宽度设置为全屏
                    childView.layout(left,getPaddingTop(),left+mMaxWidth,getPaddingTop()+childView.getMeasuredHeight());
                    left = left + mMaxWidth;
                }else{//表示这些子View是表示侧滑菜单的子View对象
                    if(isLeftSwipe){//左滑
                        childView.layout(left,getPaddingTop(),left+childView.getMeasuredWidth(),getPaddingTop()+childView.getMeasuredHeight());
                        left = left + mMaxWidth;
                    }else{
                        childView.layout(right - childView.getMeasuredWidth(), getPaddingTop(), right, getPaddingTop() + childView.getMeasuredHeight());
                        right = right - childView.getMeasuredWidth();
                    }
                }
            }
        }
    }

    private boolean isIos = true;//IOS类型的开关
    private boolean iosInterceptFlag = false;//IOS效果时，是否拦截事件
    //Point和PointF区别就是：不带"F"的使用int型表示，带"F"的使用单精度float型表示
    private PointF mLastP = new PointF();//上一次的x y

    //用来存储当前正在展开的View
    private static SwipeDeleteView mViewCache;

    private int mPointerId;//多点触摸只算第一根手指的速度
    /**
     *
     * @param ev  事件分发
     * @return
     *           如果返回值返回的是true：表示消费了此次事件，没有谁能再收到这个事件，事件终止
     *           返回false，则回传给父控件的onTouchEvent处理
     *           直接返回super.xxxx 表示如果当前时ViewGroup则直接传递给自己的onInterceptTouchEvent，
     *           如果onInterceptTouchEvent返回false或者super.xxx 则表示继续分发给下一级的dispatchTouchEvent
     *           如果onInterceptTouchEvent返回true，则交给自己的onTouchEvent处理
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(isSwipeEnable){//右滑开启
            //根据一连串手势滑动事件，实时计算出当前速度
            getQuireVelocityTracker(ev);
            final VelocityTracker velocityTracker = mVelocityTracker;
            switch (ev.getAction()){
                case MotionEvent.ACTION_DOWN:
                    isUnMoved = true;//仿QQ，侧滑菜单展开时，点击内容区，关闭侧滑菜单
                    iosInterceptFlag = false;//每次down时，默认是不拦截的
                    if(isTouching){//如果有别的指头摸过了，那么就return false。这样后续的move..等事件也不会再来找这个View了。
                        return false;
                    }else{
                        isTouching = true;//第一个摸的指头，赶紧改变标志，宣誓主权（防止多手指一起触发）
                    }
                    //getX与getRawX的区别：当你触到按钮时，x,y是相对于该按钮左上点（控件本身）的相对位置。而rawx,rawy始终是相对于屏幕左上角的位置
                    mLastP.set(ev.getRawX(), ev.getRawY());//手指按下时相对于屏幕的坐标
                    //如果down(按下)，view和cacheview不一样，则立马让它还原。且把它置为null
                    if(mViewCache != null){//说明有展开的子项
                        if(mViewCache != this){//并且不是自己
                            mViewCache.smoothClose();//关闭
                            mViewCache = null;
                            iosInterceptFlag = isIos;//IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件
                        }
                        //只要有一个侧滑菜单处于打开状态， 就不给外层布局上下滑动了
                        getParent().requestDisallowInterceptTouchEvent(true);//拦截事件
                    }
                    //求第一个触点的id，计算滑动速率用。此时可能有多个手指触摸，即多个触点
                    mPointerId = ev.getPointerId(0);
                    break;
                case MotionEvent.ACTION_MOVE:
                    //IOS模式开启的话，且当前有展开的侧滑菜单，且不是自己的，就该拦截事件咯。滑动也不该出现
                    if(iosInterceptFlag){
                        break;
                    }
                    float gap = mLastP.x - ev.getRawX();
                    //为了在水平滑动中禁止父类ListView等再竖直滑动
                    if (Math.abs(gap) > 10 || Math.abs(getScrollX()) > 10) {//2016 09 29 修改此处，使屏蔽父布局滑动更加灵敏，
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    //仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。begin
                    if (Math.abs(gap) > mScaleTouchSlop) {
                        isUnMoved = false;
                    }
                    //仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。end

                    scrollBy((int) (gap), 0);//滑动使用scrollBy
                    //越界修正
                    if (isLeftSwipe) {//左滑
                        if (getScrollX() < 0) {
                            scrollTo(0, 0);
                        }
                        if (getScrollX() > mRightMenuWidth) {
                            scrollTo(mRightMenuWidth, 0);
                        }
                    } else {//右滑
                        if (getScrollX() < -mRightMenuWidth) {
                            scrollTo(-mRightMenuWidth, 0);
                        }
                        if (getScrollX() > 0) {
                            scrollTo(0, 0);
                        }
                    }

                    mLastP.set(ev.getRawX(), ev.getRawY());
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    //add by 2016 09 11 ，IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。滑动也不该出现
                    if (!iosInterceptFlag) {
                        //求伪瞬时速度
                        velocityTracker.computeCurrentVelocity(1000, mMaxSpeed);
                        final float velocityX = velocityTracker.getXVelocity(mPointerId);
                        if (Math.abs(velocityX) > 1000) {//滑动速度超过阈值
                            if (velocityX < -1000) {
                                if (isLeftSwipe) {//左滑
                                    //平滑展开Menu
                                    smoothExpand();
                                    //展开就加入ViewCache：
                                    mViewCache = this;
                                } else {
                                    //平滑关闭Menu
                                    smoothClose();
                                }
                            } else {
                                if (isLeftSwipe) {//左滑
                                    // 平滑关闭Menu
                                    smoothClose();
                                } else {
                                    //平滑展开Menu
                                    smoothExpand();
                                    //展开就加入ViewCache：
                                    mViewCache = this;
                                }
                            }
                        } else {
                            if (Math.abs(getScrollX()) > mLimit) {//否则就判断滑动距离
                                //平滑展开Menu
                                smoothExpand();
                                //展开就加入ViewCache：
                                mViewCache = this;
                            } else {
                                // 平滑关闭Menu
                                smoothClose();
                            }
                        }
                    }
                    //释放
                    releaseVelocityTracker();
                    //LogUtils.i(TAG, "onTouch A ACTION_UP ACTION_CANCEL:velocityY:" + velocityX);
                    isTouching = false;//没有手指在摸我了
                    break;
                default:
                    break;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                //为了在侧滑时，屏蔽子View的点击事件
                if (isLeftSwipe) {//左滑
                    if (getScrollX() > mScaleTouchSlop) {
                        //解决一个智障问题~ 居然不给点击侧滑菜单 我跪着谢罪
                        //这里判断落点在内容区域屏蔽点击，内容区域外，允许传递事件继续向下的。。。
                        if (ev.getX() < getWidth() - getScrollX()) {
                            //仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                            if (isUnMoved) {
                                smoothClose();
                            }
                            return true;//true表示拦截
                        }
                    }
                } else {//右滑
                    if (-getScrollX() > mScaleTouchSlop) {
                        if (ev.getX() > -getScrollX()) {//点击范围在菜单外 屏蔽
                            //2016 10 22 add , 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                            if (isUnMoved) {
                                smoothClose();
                            }
                            return true;
                        }
                    }
                }
                break;
        }
        //模仿IOS 点击其他区域关闭：
        if (iosInterceptFlag) {
            //IOS模式开启，且当前有菜单的View，且不是自己的 拦截点击事件给子View
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * @param ev 向VelocityTracker添加MotionEvent
     * @see VelocityTracker#obtain()
     * @see VelocityTracker#addMovement(MotionEvent)
     */
    private void getQuireVelocityTracker(MotionEvent ev) {
        if(null == mVelocityTracker){
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
    }
//属性动画
    private ValueAnimator mExpandAnimator,mCloseAnimator;//展开、关闭的属性动画对象
    /**
     * 平滑关闭
     */
    public void smoothClose(){
        mCloseAnimator = ValueAnimator.ofInt(getScrollX(),0);//区间值[getScrollX(),0]
        mCloseAnimator.setDuration(300);//完成动画所需时间
        mCloseAnimator.setInterpolator(new AnticipateInterpolator());//AnticipateInterpolator():开始的时候向后然后向前甩  AnticipateOvershootInterpolator: 开始的时候向后然后向前甩一定值后返回最后的值
        //添加监听
        mCloseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                scrollTo((Integer) animation.getAnimatedValue(),0);
            }
        });
        //开启动画
        mCloseAnimator.start();
    }

    /**
     * 平滑展开
     */
    public void smoothExpand(){
        mExpandAnimator = ValueAnimator.ofInt(getScrollX(),isLeftSwipe ? mRightMenuWidth : -mRightMenuWidth);
        mExpandAnimator.setDuration(300);
        mExpandAnimator.setInterpolator(new OvershootInterpolator());//OvershootInterpolator()：向前甩一定值后再回到原来位置
        mExpandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(),0);
            }
        });
        //开启动画
        mExpandAnimator.start();
    }

    /**
     * * 释放VelocityTracker
     *
     * @see VelocityTracker#clear()
     * @see VelocityTracker#recycle()
     */
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 该方法是在销毁View的时候调用
     * 每次ViewDetach的时候，判断一下 ViewCache是不是自己，如果是自己，关闭侧滑菜单，且ViewCache设置为null，
     * 理由：1 防止内存泄漏(ViewCache是一个静态变量)
     *       2 侧滑删除自己后，这个View被Recycler回收，复用，下一个进入屏幕的View的状态应该是普通状态，而不是展开状态。
     */
    @Override
    protected void onDetachedFromWindow() {
        if (this == mViewCache) {
            mViewCache.smoothClose();
            mViewCache = null;
        }
        super.onDetachedFromWindow();
    }

    /**
     * 展开时，禁止长按
     * @return
     */
    @Override
    public boolean performLongClick() {
        if (Math.abs(getScrollX()) > mScaleTouchSlop) {
            return false;
        }
        return super.performLongClick();
    }

    /**
     * 快速关闭。
     * 用于 点击侧滑菜单上的选项(比如：删除 、置顶等),同时想让它快速关闭。
     * 这个方法在ListView里是必须调用的，
     * 在RecyclerView里，视情况而定，如果是mAdapter.notifyItemRemoved(pos)方法不用调用。
     */
    public void quickClose() {
        if (this == mViewCache) {
            //先取消展开动画
            if (null != mExpandAnimator && mExpandAnimator.isRunning()) {
                mExpandAnimator.cancel();
            }
            mViewCache.scrollTo(0, 0);//关闭
            mViewCache = null;
        }
    }
}
