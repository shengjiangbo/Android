package com.shengjiangbo.databindingadapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.sheng.refresh.RefreshScrollingUtil;

import java.util.Timer;
import java.util.TimerTask;

public class BounceScrollView extends NestedScrollView {
    private ViewGroup inner;
    private float y;    //记录按下时的y坐标
    private float tempDown, tempUp;     //布局滑动距离
    private Rect normal = new Rect();
    private static int radio = 3;   //滑动比例
    private int mActivePointerId;
    private static boolean isPull = false; //拉动时不进行其他操作
    private boolean isBackUp = true; //控制靠近顶部／底部
    private OnRefreshListener mListener;
    private int mHeadHeight = 0;

    public void setOnPullListener(OnPullListener onPullListener) {
        this.onPullListener = onPullListener;
    }

    private OnPullListener onPullListener;

    public BounceScrollView(Context context) {
        this(context, null);
    }

    public BounceScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BounceScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        onPullListener = new OnPullListener() {
            @Override
            public void pullUp(int dif_y) {
                Log.d("txy", "pullUp");
            }

            @Override
            public void pullDown(int dif_y) {
                Log.d("txy", "pullDown");
            }

            @Override
            public void backDown() {

            }

            @Override
            public void backUp() {

            }
        };
//        this.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return isPull;
//            }
//        });
    }

    /**
     * 获得第一个view
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            if (getChildAt(0) instanceof ViewGroup) {
                inner = ((ViewGroup) getChildAt(0));
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (inner == null) {
            return super.onTouchEvent(event);
        } else {
            commOnTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
//        View refreshHead = mListener.getRefreshHead();
//        if (refreshHead != null) {
//            refreshHead.setTag("head");
//            if (inner == null) {
//                if (getChildCount() > 0) {
//                    inner = (ViewGroup) getChildAt(0);
//                } else {//依旧没有拿到孩子
//                    return;
//                }
//            }
//            mHeadHeight = refreshHead.getLayoutParams().height;
//            if (inner.getChildCount() > 0) {
//                if (!"head".equals(inner.getChildAt(0).getTag())) {
//                    inner.addView(refreshHead, 0);
//                    FrameLayout.LayoutParams layoutParams = (LayoutParams) inner.getLayoutParams();
//                    layoutParams.topMargin = -mHeadHeight;
//                    inner.setLayoutParams(layoutParams);
//                }
//            } else {
//                inner.addView(refreshHead, 0);
//                FrameLayout.LayoutParams layoutParams = (LayoutParams) inner.getLayoutParams();
//                layoutParams.topMargin = -mHeadHeight;
//                inner.setLayoutParams(layoutParams);
//            }
//        }
    }

    /**
     * 添加手势响应事件
     *
     * @param ev
     */
    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();
                mActivePointerId = ev.getPointerId(0);
                if (normal.isEmpty()) {
                    // 保存正常的布局位置
                    normal.set(inner.getLeft(), inner.getTop(),
                            inner.getRight(), inner.getBottom());
                }
                break;
            case MotionEvent.ACTION_UP:
                hide();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                // 多点触碰
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                final float preY = y;
                float nowY = ev.getY(activePointerIndex);
                // 根据下拉距离改变比例
                radio = (int) (3 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
                        * Math.abs(inner.getTop())));
                int deltaY = (int) (preY - nowY) / radio;
                y = nowY;
                int offset = inner.getMeasuredHeight() - getHeight();
                int scrollY = getScrollY();
                int i = inner.getTop() - deltaY;
                int innerScrollY = inner.getScrollY();

                Log.e("deltaY", "commOnTouchEvent: " + deltaY + "innerScrollY:" + innerScrollY);
                //当正在拉动时，加锁
                if (scrollY == 0) {
                    if (i > 0) {
                        isPull = true;
                    }
                }
                if (scrollY == offset) {
                    if (i < 0) {
                        isPull = true;
                    }
                }
                //这里移动布局
                if (mListener != null) {
                    mListener.onScrollY(inner.getTop());
                }
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isNeedMove()) {
                    inner.layout(inner.getLeft(), i, inner.getRight(),
                            inner.getBottom() - deltaY);
                }
                break;
        }

    }


    /**
     * 布局回弹到初始位置
     */
    private void hide() {
        tempDown = inner.getTop() - normal.top;
        tempUp = normal.bottom - inner.getBottom();
        reboundView();
    }

    private void reboundView() {
        float v = tempDown > 0 ? tempDown : tempUp;
        int top = 0;
        if (refreshState == RefreshState.FREED) {
            top = mHeadHeight;
            refreshState = RefreshState.LOADING;
        } else if (refreshState == RefreshState.LOADING) {
            if (tempDown > 0) {
                top = mHeadHeight;
            } else {
                top = -mHeadHeight;
            }
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) v, top);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                animatedValue = tempDown > 0 ? animatedValue : -animatedValue;
                inner.layout(normal.left, (int) (normal.top + animatedValue), normal.right, (int) (normal.bottom + animatedValue));
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isPull = false;
            }
        });
        valueAnimator.setDuration((long) (v / normal.bottom * 500));
        valueAnimator.start();
    }

    public static class OnRefreshListener {

        /**
         * 开始刷新
         */
        public void onRefresh() {

        }

        /**
         * 正在下拉
         */
        public void onDropDown() {

        }

        /**
         * 释放刷新
         */
        public void onFreed() {

        }

        /**
         * 刷新完成
         */
        public void onRefreshing() {

        }

        /**
         * @param scrollY 下拉上拉的偏移量
         */
        public void onScrollY(int scrollY) {

        }

        /**
         * 自定义头布局
         *
         * @return
         */
        public View getRefreshHead() {
            return null;
        }
    }

    public void setRefreshState(RefreshState state) {
        if (mListener != null) {
            switch (state) {
                case LOADING://刷新
                    mListener.onRefresh();
                    break;
                case DROPDOWN://下拉中
                    mListener.onDropDown();
                    break;
                case FREED://释放刷新中
                    mListener.onFreed();
                    break;
                case DEFAULT:
                    mListener.onRefreshing();
                    break;
            }
        }

    }

    private RefreshState refreshState = RefreshState.DEFAULT;

    //定义下拉刷新的状态 ，依次为  正在刷新、下拉刷新、释放刷新、刷新完成默认模式
    private enum RefreshState {
        LOADING, DROPDOWN, FREED, DEFAULT
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
                MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            y = ev.getY(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    // 是否需要移动布局
    public boolean isNeedMove() {
        return handleChildViewIsTop();
    }


    //判断子View 是否是 滑动到顶端的
    private boolean handleChildViewIsTop() {

        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        Log.d("txy", "offset:" + offset);
        if (scrollY == 0) {
            return true;
        }
        return false;
    }


    public static interface OnPullListener {
        void pullUp(int dif_y);

        void pullDown(int dif_y);

        void backDown();

        void backUp();
    }
}