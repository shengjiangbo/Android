package com.shengjiangbo.databindingadapter;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.sheng.mvvm.BaseBindVMActivity;
import com.sheng.mvvm.BaseViewModel;
import com.shengjiangbo.databindingadapter.databinding.ActivityBounceBinding;

/**
 * 创建人：Bobo
 * 创建时间：2021/6/18 9:54
 * 类描述：
 */
public class BounceActivity extends BaseBindVMActivity<BaseViewModel, ActivityBounceBinding> {
    @Override
    protected void initView() {
        binding.view.setOnRefreshListener(new BounceScrollView.OnRefreshListener() {
            @Override
            public View getRefreshHead() {
                View view = new View(mContext);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.YELLOW);
                return view;
            }

            @Override
            public void onScrollY(int scrollY) {
                super.onScrollY(scrollY);
                Log.e("scrollY", "onScrollY: "+scrollY );
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bounce;
    }
}
