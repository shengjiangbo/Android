package com.shengjiangbo.databindingadapter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 创建人：Bobo
 * 创建时间：2021/6/19 11:07
 * 类描述：
 */
public class MyRecyclerView extends RecyclerView {

    private boolean mTouch;

    public MyRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setTouch(boolean touch){
        mTouch = touch;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e("NestedScrollView", "onTouchEvent: ");
        return mTouch;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("NestedScrollView", "onInterceptTouchEvent: ");
        return mTouch;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("NestedScrollView", "dispatchTouchEvent: ");
        return mTouch;
    }
}
