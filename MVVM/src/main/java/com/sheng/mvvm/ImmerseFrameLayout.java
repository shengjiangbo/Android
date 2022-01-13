package com.sheng.mvvm;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

/**
 * 实现子控件的 fitsSystemWindows 属性生效
 */
public class ImmerseFrameLayout extends FrameLayout {

    private final String TAG = ImmerseFrameLayout.class.getSimpleName();

    public ImmerseFrameLayout(Context context) {
        super(context);
    }

    public ImmerseFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImmerseFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        WindowInsets result = super.dispatchApplyWindowInsets(insets);
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                result = getChildAt(i).dispatchApplyWindowInsets(insets);
            }
        }
        return result;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        ViewCompat.requestApplyInsets(child);
    }
}