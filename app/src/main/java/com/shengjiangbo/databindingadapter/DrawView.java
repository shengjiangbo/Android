package com.shengjiangbo.databindingadapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 创建人：sjb
 * 创建时间：2023/3/1 11:25
 * 类描述：
 */
public class DrawView extends View {

    private Paint mPaint;
    private RectF mRectF;
    private RectF mRectF1;
    private RectF mRectF2;
    private RectF mRectF3;
    private int mWidth;

    public DrawView(Context context) {
        super(context);
        initView();
    }

    public DrawView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DrawView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public DrawView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView() {
        mPaint = new Paint();
        mWidth = getResources().getDimensionPixelSize(R.dimen.dp_2);
        mPaint.setStrokeWidth(mWidth);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //设置抗锯齿
        mPaint.setAntiAlias(true);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF = new RectF(-w + mWidth / 2f * 3f, -h + mWidth / 2f * 3f, w - mWidth / 2f, h - mWidth / 2f);
        mRectF1 = new RectF(mWidth / 2f, -h + mWidth / 2f * 3f, w + w - mWidth / 2f * 3f, h - mWidth / 2f);
        mRectF2 = new RectF(mWidth / 2f, mWidth / 2f, w * 2 - mWidth / 2f * 3f, h * 2 - mWidth / 2f * 3f);
        mRectF3 = new RectF(-w + mWidth / 2f * 3f, mWidth / 2f, w - mWidth / 2f, h * 2 - mWidth / 2f * 3f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRectF != null) {
            canvas.drawArc(mRectF, 0, 90, false, mPaint);
            canvas.drawArc(mRectF1, 90, 90, false, mPaint);
            canvas.drawArc(mRectF2, 180, 90, false, mPaint);
            canvas.drawArc(mRectF3, 270, 90, false, mPaint);
        }
    }
}
