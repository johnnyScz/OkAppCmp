package com.xinyu.newdiggtest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

public class MarkedImageView extends android.support.v7.widget.AppCompatImageView {
    private Paint mCirclePanit;
    private Paint mTextPanit;
    private int mMessageNumber;
    private boolean mIsHideMessageMark = false;

    private Context mContext;
    private int mPaddingPx;
    private float mMarkRadius;
    private int mTextSize;

    public MarkedImageView(Context context) {
        this(context, null);
    }

    public MarkedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarkedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mCirclePanit = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePanit.setColor(Color.parseColor("#FF4400"));
        mTextPanit = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPanit.setColor(Color.WHITE);
        mTextPanit.setTextAlign(Paint.Align.CENTER);
        mPaddingPx = DensityUtil.dip2px(mContext, 3);
        mMarkRadius = DensityUtil.dip2px(mContext, 6);
        mTextSize = DensityUtil.dip2px(mContext, 5);
        mTextPanit.setTextSize(mTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mIsHideMessageMark) {
            //8 is checkable padding 8dp should turns to px
            if (mMessageNumber > 0 && mMessageNumber < 100) {
                canvas.drawCircle(getMeasuredWidth() - mMarkRadius, mMarkRadius + mPaddingPx - 8, mMarkRadius, mCirclePanit);
                canvas.drawText(mMessageNumber + "", getMeasuredWidth() - mMarkRadius, mMarkRadius + mTextSize / 3 + mPaddingPx - 8, mTextPanit);
            } else if (mMessageNumber > 99) {
                RectF rectF = new RectF(getMeasuredWidth() - 2 * mMarkRadius, mPaddingPx, getMeasuredWidth() + (2 / 3) * mMarkRadius, 2 * mMarkRadius + mPaddingPx);
                canvas.drawRoundRect(rectF, mMarkRadius, mMarkRadius, mCirclePanit);
                canvas.drawText("99+", getMeasuredWidth() - mMarkRadius, mMarkRadius + mTextSize / 3 + mPaddingPx, mTextPanit);
            }
        }
    }

    public void setMessageNumber(int messageNumber) {
        mMessageNumber = messageNumber;
        invalidate();
    }

    public void setIsHideMessageMark(boolean isHideMessageMark) {
        mIsHideMessageMark = isHideMessageMark;
        invalidate();
    }
}
