package com.sheng.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人：Bobo
 * 创建时间：2021/6/23 11:55
 * 类描述：
 */
public class FlowLayout extends ViewGroup {


    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //主题style
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private int mHorizontalSpacing = 0;
    private int mVerticalSpacing = 0;

    public int getHorizontalSpacing() {
        return mHorizontalSpacing;
    }

    /**
     * @param horizontalSpacing 设置横线每个item的边距
     */
    public void setHorizontalSpacing(int horizontalSpacing) {
        mHorizontalSpacing = horizontalSpacing;
    }

    public int getVerticalSpacing() {
        return mVerticalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        mVerticalSpacing = verticalSpacing;
    }

    public void initMeasureParent() {
        if (lineHeights == null) {
            lineHeights = new ArrayList<>();
        } else {
            lineHeights.clear();
        }
        if (allLines == null) {
            allLines = new ArrayList<>();
        } else {
            allLines.clear();

        }
    }

    public interface OnItemViewListener<T> {
        View getView(T itemView);//设置布局

        void onItemClick(View view, T itemView);//点击事件
    }

    public <T> void setListData(List<T> listData, OnItemViewListener<T> onItemViewListener) {
        removeAllViews();
        for (T itemView : listData) {
            View view = onItemViewListener.getView(itemView);
            if (view != null) {
                addView(view);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemViewListener.onItemClick(v, itemView);
                    }
                });
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initMeasureParent();
        int childCount = getChildCount();//获取孩子的数量
        //保存一行中所有的View
        List<View> lineViews = new ArrayList<>();
        int lineWidthUsed = 0;//记录这行已经使用多宽的size
        int lineHeight = 0;//一行的高
        int selfWidth = MeasureSpec.getSize(widthMeasureSpec);//自己宽度
        int selfHeight = MeasureSpec.getSize(heightMeasureSpec);//自己高度
        int parentNeededHeight = 0;//measure过程中,子view要求的的父ViewGroup的高
        int parentNeededWidth = 0;
        allLines.clear();
        lineHeights.clear();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            //获取子view的宽高
            int childMeasuredHeight = childView.getMeasuredHeight();
            int childMeasuredWidth = childView.getMeasuredWidth();

            //通过宽度判断是否换行
            if (childMeasuredWidth + lineWidthUsed + mHorizontalSpacing > selfWidth) {
                allLines.add(lineViews);
                lineHeights.add(lineHeight);
                //一单换行 记录下宽高
                parentNeededHeight = parentNeededHeight + lineHeight + mVerticalSpacing;
                parentNeededWidth = Math.max(parentNeededWidth, lineWidthUsed + mHorizontalSpacing);

                lineViews = new ArrayList<>();
                lineWidthUsed = 0;
                lineHeight = 0;
            }

            lineViews.add(childView);//记录每一行的view
//            每一行自己的宽高
            lineWidthUsed = lineWidthUsed + childMeasuredWidth + mHorizontalSpacing;
            lineHeight = Math.max(lineHeight, childMeasuredHeight);
        }
        if (lineViews.size() > 0) {//在循环最后一行可能没有加进去
            allLines.add(lineViews);
            lineHeights.add(lineHeight);
            //一单换行 记录下宽高
            parentNeededHeight = parentNeededHeight + lineHeight + mVerticalSpacing;
            parentNeededWidth = Math.max(parentNeededWidth, lineWidthUsed + mHorizontalSpacing);
        }
        //根据父亲提供的宽高测量
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int realWidth = (widthMode == MeasureSpec.EXACTLY) ? selfWidth : parentNeededWidth;
        int realHeight = (heightMode == MeasureSpec.EXACTLY) ? selfHeight : parentNeededHeight;
        //测量自己
        setMeasuredDimension(realWidth, realHeight);
    }

    private List<List<View>> allLines;
    private List<Integer> lineHeights;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //获取行数
        int lineCount = lineHeights.size();

        int curL = getPaddingLeft();
        int curT = getPaddingTop();
        for (int i = 0; i < lineCount; i++) {
            List<View> lineViews = allLines.get(i);
            int lineHeight = lineHeights.get(i);
            for (int x = 0; x < lineViews.size(); x++) {//布局一行
                View view = lineViews.get(x);
                int left = curL;
                int top = curT;

                int right = left + view.getMeasuredWidth();
                int bottom = top + view.getMeasuredHeight();
                view.layout(left, top, right, bottom);
                curL = right + mHorizontalSpacing;
            }
            curL = getPaddingLeft();
            curT = curT + lineHeight + mVerticalSpacing;
        }
    }
}
