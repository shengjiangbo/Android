package com.sheng.mvvm

import android.view.View
import androidx.databinding.BindingAdapter

object BindData {
    //    /**
    //     * 设置view 圆角
    //     */
    //    @BindingAdapter(value = {"filletRadius"})
    //    public static void setViewFillet(View view, int radius) {
    //        view.setOutlineProvider(new ViewOutlineProvider() {
    //            @Override
    //            public void getOutline(View view, Outline outline) {
    //                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), ConvertUtils.dp2px(radius));
    //            }
    //        });
    //        view.setClipToOutline(true);
    //    }
    //示例 也可以使用
    @BindingAdapter(value = ["isVisibility"])
    fun isVisibility(view: View, isVisibility: Boolean) {
        view.visibility = if (isVisibility) View.VISIBLE else View.GONE
    }

    @BindingAdapter(value = ["select"])
    fun select(view: View, select: Boolean) {
        view.isSelected = select
    }
}