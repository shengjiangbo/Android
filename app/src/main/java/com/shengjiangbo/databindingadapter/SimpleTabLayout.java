package com.shengjiangbo.databindingadapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.shengjiangbo.databindingadapter.databinding.SimpleTabLayoutBinding;

/**
 * 创建人：Bobo
 * 创建时间：2021/6/15 17:03
 * 类描述：
 */
public class SimpleTabLayout extends FrameLayout {
    public SimpleTabLayout(@NonNull Context context) {
        super(context);
        initView(null);
    }

    public SimpleTabLayout(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public SimpleTabLayout(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    public SimpleTabLayout(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        SimpleTabLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.simple_tab_layout, this, true);

    }
}
