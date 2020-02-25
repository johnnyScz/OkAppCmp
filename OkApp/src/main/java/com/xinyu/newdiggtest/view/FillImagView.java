package com.xinyu.newdiggtest.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;


public class FillImagView extends android.support.v7.widget.AppCompatImageView {

    public FillImagView(Context context) {
        super(context);
    }

    public FillImagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();

        int imgWidth = MeasureSpec.getSize(widthMeasureSpec);

        int imgHeight = MeasureSpec.getSize(heightMeasureSpec);

        int demesinWith = 0;
        int demesinHeight = 0;

        if (d.getIntrinsicWidth() / imgWidth > d.getIntrinsicHeight() / imgHeight) {

            //高度根据使得图片的宽度充满屏幕计算而得
            demesinHeight = (int) ((imgHeight * (float) d.getIntrinsicWidth() / imgWidth));

            demesinWith = d.getIntrinsicWidth();

        } else {
            demesinWith = (int) ((imgWidth * (float) d.getIntrinsicHeight() / imgHeight));
            demesinHeight = d.getIntrinsicHeight();
        }
        setMeasuredDimension(demesinWith, demesinHeight);

//        if (d != null) {
//            // ceil not round - avoid thin vertical gaps along the left/right edges
//            int height = MeasureSpec.getSize(heightMeasureSpec);
//            //高度根据使得图片的宽度充满屏幕计算而得
//            int width = (int) Math.ceil((float) height * (float) d.getIntrinsicWidth() / (float) d.getIntrinsicHeight());
//            setMeasuredDimension(width, height);
//        } else {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        }

    }

}