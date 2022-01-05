package com.shengjiangbo.databindingadapter;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 创建人：Bobo
 * 创建时间：2021/6/28 18:46
 * 类描述：
 */
public class FishDrawable extends Drawable {
    public FishDrawable() {
        init();
    }

    private Path mPath;
    private Paint mPaint;
    private int OTHER_ALPHA = 110;
    // 鱼头的半径
    private float HEAD_RADIUS = 50;
    // 鱼的开始角度
    private float fishMainAngle = 90;
    // 身体长度
    private float BODY_LENGTH = HEAD_RADIUS * 3.2f;
    // 寻找鱼鳍的起始点坐标的线长
    private float find_fins_length = 0.9f * HEAD_RADIUS;
    // 鱼鳍的长度
    private float fins_length = 1.3f * HEAD_RADIUS;
    // 大圆的半径
    private float big_radius = 0.7f * HEAD_RADIUS;
    // 中圆的半径
    private float middle_radius = 0.6f * big_radius;
    // 小圆的半径
    private float small_radius = 0.4f * middle_radius;
    // --寻找尾部中圆圆心的线长
    private final float FIND_MIDDLE_CIRCLE_LENGTH = big_radius * (0.6f + 1);
    // --寻找尾部小圆圆心的线长
    private final float FIND_SMALL_CIRCLE_LENGTH = middle_radius * (0.4f + 2.7f);
    // --寻找大三角形底边中心点的线长
    private final float FIND_TRIANGLE_LENGTH = middle_radius * 2.7f;

    private float currentValue;

    private PointF middlePoint;
    PointF headPoint;
    public PointF getCenterPoint(){
        return middlePoint;
    }
    public PointF getControlOne(){
        return headPoint;
    }
    public float getHeadRadius(){
        return HEAD_RADIUS;
    }
    private float frequence = 1f;
    public void setFrequence(float frequence) {
        this.frequence = frequence;
    }

    public float getFishMainAngle() {
        return fishMainAngle;
    }

    public void setFishMainAngle(float fishMainAngle) {
        this.fishMainAngle = fishMainAngle;
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setARGB(OTHER_ALPHA, 244, 92, 71);
        // 鱼的中心,PointF 和 Point 的区别是，PointF是float类型
        middlePoint = new PointF(4.19f * HEAD_RADIUS, 4.19f * HEAD_RADIUS);
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 3600f);
        animator.setDuration(15 * 1000);
        // 重复的模式: 重新开始
        animator.setRepeatMode(ValueAnimator.RESTART);
        // 重复的次数，无限次
        animator.setRepeatCount(ValueAnimator.INFINITE);
        // 插值器，匀速
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentValue = (float) valueAnimator.getAnimatedValue();
                invalidateSelf();
            }
        });
        animator.start();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        // 每一个部分都以鱼的中心位置的相对坐标来的
        // 鱼头的计算坐标，x = cosA * c   y = sinA * c
        // 角度转化成弧度 Math.toRadians(), 圆是360度，也就是2π，即 360度=2π
        float fishAngle = (float) (fishMainAngle + Math.sin(Math.toRadians(currentValue) * 10 ));// 左右10度摆动
        // 鱼头的圆心坐标
        headPoint = calculatePoint(middlePoint, BODY_LENGTH / 2, fishAngle);
        canvas.drawCircle(headPoint.x,headPoint.y,HEAD_RADIUS,mPaint);
        // 鱼鳍 是二阶贝塞尔曲线，一个启点，一个终点，一个控制点，总体来说 点比阶数多一
        PointF rightFinsPoint = calculatePoint(headPoint, find_fins_length, fishAngle - 110);
        makeFins(canvas,rightFinsPoint,fishAngle,true);
        // 画左鱼鳍
        PointF leftFinsPoint = calculatePoint(headPoint, find_fins_length, fishAngle + 110);
        makeFins(canvas,leftFinsPoint,fishAngle,false);

        // 梯形下底圆心
        PointF bodyBottomCenterPoint = calculatePoint(headPoint, BODY_LENGTH, fishAngle-180);
        // 画节肢 1
        makeSegment(canvas,bodyBottomCenterPoint,fishAngle);

        PointF middleCenterPoint = calculatePoint(bodyBottomCenterPoint, FIND_MIDDLE_CIRCLE_LENGTH, fishAngle-180);
        //画节支 2
        makeSegment2(canvas,middleCenterPoint,fishAngle);
        // 画尾巴 也就是大三角形
        makeTriangel(canvas,middleCenterPoint,fishAngle);
        // 画尾巴 也就是小三角形
        makeTriangel2(canvas,middleCenterPoint,fishAngle);
        // 画身体
        makeBody(canvas,headPoint,bodyBottomCenterPoint,fishAngle);
    }

    private void makeBody(Canvas canvas, PointF headPoint, PointF bodyBottomCenterPoint,float fishAngle) {
        PointF topLeftPoint = calculatePoint(headPoint, HEAD_RADIUS, fishAngle + 90);
        PointF topRightPoint = calculatePoint(headPoint, HEAD_RADIUS, fishAngle - 90);
        PointF bottomLeftPoint = calculatePoint(bodyBottomCenterPoint, big_radius, fishAngle + 90);
        PointF bottomRightPoint = calculatePoint(bodyBottomCenterPoint, big_radius, fishAngle - 90);
        // 二阶贝塞尔曲线的控制点，决定鱼的胖瘦
        PointF controlLeft = calculatePoint(headPoint, BODY_LENGTH * 0.56f, fishAngle + 130);
        PointF controlRight = calculatePoint(headPoint, BODY_LENGTH * 0.56f, fishAngle - 130);
        mPath.reset();
        mPath.moveTo(topLeftPoint.x,topLeftPoint.y);
        mPath.quadTo(controlLeft.x,controlLeft.y,bottomLeftPoint.x,bottomLeftPoint.y);
        mPath.lineTo(bottomRightPoint.x,bottomRightPoint.y);
        mPath.quadTo(controlRight.x,controlRight.y,topRightPoint.x,topRightPoint.y);
        mPath.lineTo(topLeftPoint.x,topLeftPoint.y);
        canvas.drawPath(mPath,mPaint);
    }

    private void makeTriangel(Canvas canvas, PointF middleCenterPoint, float fishAngle) {
        float triangelAngle = (float) (fishAngle + Math.cos(Math.toRadians(currentValue * 1.5)) * 25);// 左右25度摆动
        // 三角形底边的中心坐标
        PointF centerPoint = calculatePoint(middleCenterPoint, FIND_TRIANGLE_LENGTH, triangelAngle - 180);
        // 底边其实就是大圆的半径
        PointF leftPoint = calculatePoint(centerPoint, big_radius, triangelAngle + 90);
        PointF rightPoint = calculatePoint(centerPoint, big_radius, triangelAngle - 90);
        mPath.reset();
        mPath.moveTo(leftPoint.x,leftPoint.y);
        mPath.lineTo(rightPoint.x,rightPoint.y);
        mPath.lineTo(middleCenterPoint.x,middleCenterPoint.y);
        canvas.drawPath(mPath,mPaint);
    }

    private void makeTriangel2(Canvas canvas, PointF middleCenterPoint, float fishAngle) {
        float triangelAngle = (float) (fishAngle + Math.cos(Math.toRadians(currentValue * 1.5)) * 25);// 左右25度摆动
        // 三角形底边的中心坐标
        PointF centerPoint = calculatePoint(middleCenterPoint, FIND_TRIANGLE_LENGTH-10, triangelAngle - 180);
        // 底边其实就是大圆的半径
        PointF leftPoint = calculatePoint(centerPoint, big_radius-20, triangelAngle + 90);
        PointF rightPoint = calculatePoint(centerPoint, big_radius-20, triangelAngle - 90);
        mPath.reset();
        mPath.moveTo(leftPoint.x,leftPoint.y);
        mPath.lineTo(rightPoint.x,rightPoint.y);
        mPath.lineTo(middleCenterPoint.x,middleCenterPoint.y);
        canvas.drawPath(mPath,mPaint);

    }

    private void makeSegment(Canvas canvas, PointF bottomCenterPoint, float fishAngle) {
        float segmentAngle = (float) (fishAngle + Math.cos(Math.toRadians(currentValue * 1.5)) * 15);// 左右15度摆动
        // 梯形上低圆心
        PointF upperCenterPoint =
                calculatePoint(bottomCenterPoint,FIND_MIDDLE_CIRCLE_LENGTH,segmentAngle-180);
        PointF bottomLeftPoint = calculatePoint(bottomCenterPoint, big_radius, segmentAngle + 90);
        PointF bottomRightPoint = calculatePoint(bottomCenterPoint, big_radius, segmentAngle - 90);
        PointF upperLeftPoint = calculatePoint(upperCenterPoint, middle_radius, segmentAngle + 90);
        PointF upperRightPoint = calculatePoint(upperCenterPoint, middle_radius, segmentAngle - 90);
        // 画大圆
        canvas.drawCircle(bottomCenterPoint.x,bottomCenterPoint.y,big_radius,mPaint);
        // 画中圆
        canvas.drawCircle(upperCenterPoint.x,upperCenterPoint.y,middle_radius,mPaint);
        // 画梯形 reset 就是把 path 之前保存的路径清零
        mPath.reset();
        mPath.moveTo(bottomLeftPoint.x,bottomLeftPoint.y);
        mPath.lineTo(bottomRightPoint.x,bottomRightPoint.y);
        mPath.lineTo(upperRightPoint.x,upperRightPoint.y);
        mPath.lineTo(upperLeftPoint.x,upperLeftPoint.y);
        canvas.drawPath(mPath,mPaint);
    }


    private void makeSegment2(Canvas canvas, PointF middleCenterPoint, float fishAngle) {
        // 注意细节，
        float segmentAngle = (float) (fishAngle + Math.cos(Math.toRadians(currentValue * 1.5)) * 25);// 左右25度摆动
        // 梯形上低圆心
        PointF upperSmallCenterPoint =
                calculatePoint(middleCenterPoint,FIND_SMALL_CIRCLE_LENGTH,segmentAngle-180);
        PointF bottomLeftPoint = calculatePoint(middleCenterPoint, middle_radius, segmentAngle + 90);
        PointF bottomRightPoint = calculatePoint(middleCenterPoint, middle_radius, segmentAngle - 90);
        PointF upperLeftPoint = calculatePoint(upperSmallCenterPoint, small_radius, segmentAngle + 90);
        PointF upperRightPoint = calculatePoint(upperSmallCenterPoint, small_radius, segmentAngle - 90);
        // 画小圆
        canvas.drawCircle(upperSmallCenterPoint.x,upperSmallCenterPoint.y,small_radius,mPaint);
        // 画梯形 reset 就是把 path 之前保存的路径清零
        mPath.reset();
        mPath.moveTo(bottomLeftPoint.x,bottomLeftPoint.y);
        mPath.lineTo(bottomRightPoint.x,bottomRightPoint.y);
        mPath.lineTo(upperRightPoint.x,upperRightPoint.y);
        mPath.lineTo(upperLeftPoint.x,upperLeftPoint.y);
        canvas.drawPath(mPath,mPaint);
    }


    // 画鱼鳍
    private void makeFins(Canvas canvas, PointF startPoint, float fishAngle,boolean isRight) {
        // 鱼鳍的终点，也就是二阶贝塞尔曲线的终点，还差一个控制点
        PointF endPoint = calculatePoint(startPoint,fins_length,fishAngle-180);
        // 控制点
        PointF controlPoint = calculatePoint(startPoint,
                fins_length * 1.8f,
                isRight ? fishAngle - 115 : fishAngle + 115);
        // 绘制
        mPath.reset();
        // 将画笔移动到起始点
        mPath.moveTo(startPoint.x,startPoint.y);
        // 这个就是画二阶贝塞尔曲线的
        mPath.quadTo(controlPoint.x,controlPoint.y,endPoint.x,endPoint.y);
        canvas.drawPath(mPath,mPaint);
    }

    // startPoint 起始点坐标，length 要求的点到起始点的距离---也就是线长，angle 当前的朝向角度，如果是0 度的话那么就是水平朝右
    // 计算每个部位相对于中心点的坐标
    public PointF calculatePoint(PointF startPoint, float length, float angle) {
        // x 坐标
        float x = (float) (Math.cos(Math.toRadians(angle)) * length);
        // y 坐标
        float y = (float) (Math.sin(Math.toRadians(angle - 180)) * length);
        return new PointF(startPoint.x + x, startPoint.y + y);
    }

    // 确定整个Drawable的大小，ImageView 一般设置成Wrap_content，然后又下面来确定
    @Override
    public int getIntrinsicWidth() {
        return (int) (8.38 * HEAD_RADIUS);
    }

    // 确定整个Drawable的大小
    @Override
    public int getIntrinsicHeight() {
        return (int) (8.38 * HEAD_RADIUS);
    }

    @Override
    public void setAlpha(int i) {
        mPaint.setAlpha(i);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
