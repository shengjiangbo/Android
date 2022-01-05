package com.shengjiangbo.databindingadapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 创建人：Bobo
 * 创建时间：2021/6/29 14:48
 * 类描述：
 */
public class TwoBezierView extends View {

    private Path mPath;
    private Paint mPaint;
    private PointF start;
    private PointF end;
    private PointF control;
    private int centerX;
    private int centerY;
    private int mControl = 0;

    public TwoBezierView(Context context) {
        super(context);
        init();
    }


    public TwoBezierView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TwoBezierView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TwoBezierView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);//填充
        mPaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.dp_2));
        mPaint.setAntiAlias(true);//设置抗锯齿
        mPaint.setDither(true);//防抖
        start = new PointF(0, 0);//开始坐标
        end = new PointF(0, 0);//结束坐标
        control = new PointF(0, 0);//控制坐标

    }

    /**
     * @param control 0 控制坐标 1 开始坐标 2end
     */
    public void setControl(int control) {
        mControl = control;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        //初始化数据点和控制点的位置
        start.x = centerX - 200;
        start.y = centerY;
        end.x = centerX + 200;
        end.y = centerY;
        control.x = centerX;
        control.y = centerY - 100;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //绘制数据点和控制点
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(20);
        canvas.drawPoint(start.x, start.y, mPaint);
        canvas.drawPoint(end.x, end.y, mPaint);
        canvas.drawPoint(control.x, control.y, mPaint);

        //绘制辅助线
        mPaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.dp_2));
        canvas.drawLine(start.x, start.y, control.x, control.y, mPaint);
        canvas.drawLine(control.x, control.y, end.x, end.y, mPaint);

        mPaint.setColor(Color.RED);
        //绘制二阶贝塞尔曲线
        mPath.reset();
        mPath.moveTo(start.x, start.y);
        mPath.quadTo(control.x, control.y, end.x, end.y);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                switch (mControl) {
                    case 0:
                        control.x = event.getX();
                        control.y = event.getY();
                        break;
                    case 1:
                        start.x = event.getX();
                        start.y = event.getY();
                        break;
                    case 2:
                        end.x = event.getX();
                        end.y = event.getY();
                        break;
                }
                invalidate();
                break;
        }
        return true;
    }
}
