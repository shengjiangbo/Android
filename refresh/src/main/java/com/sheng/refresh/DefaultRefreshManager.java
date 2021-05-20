package com.sheng.refresh;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * Created by anson on 2019/4/7.
 */
public class DefaultRefreshManager extends BaseRefreshManager {

    private TextView mTvRefresh;
    private View mIv;

    public DefaultRefreshManager(Context context) {
        super(context);
    }

    @Override
    public View getHeaderView() {
        View view = mLayoutInflater.inflate(R.layout.ulti_header_layout, null, false);
        mTvRefresh = view.findViewById(R.id.header_text);
        mIv = view.findViewById(R.id.iv);
        return view;
    }

    @Override
    public void downRefresh() {
        mTvRefresh.setText("下拉刷新");

    }

    @Override
    public void downRefreshMore(int y) {
        if (objectAnimator == null) {
            Log.e("DefaultRefreshManager", "downRefreshMore: y:" + y);
            mIv.setRotation(y * 2);
        }
    }

    @Override
    public void releaseRefresh() {
        mTvRefresh.setText("释放刷新");
    }

    @Override
    public void iddleRefresh() {
        mTvRefresh.setText("下拉刷新");
    }

    private ObjectAnimator objectAnimator;

    @Override
    public void refreshing() {
        mTvRefresh.setText("正在刷新");
        objectAnimator = ObjectAnimator.ofFloat(mIv, "rotation", 0, 360f);
//      设置移动时间
        objectAnimator.setDuration(300);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setInterpolator(new LinearInterpolator());
//      开始动画
        objectAnimator.start();
    }

    @Override
    public void onRefreshing() {
        if (objectAnimator != null) {
            objectAnimator.cancel();
            objectAnimator = null;
            mIv.clearAnimation();
        }
    }

}
