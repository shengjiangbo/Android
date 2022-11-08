package com.shengjiangbo.databindingadapter;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.splashscreen.SplashScreenViewProvider;

import com.sheng.mvvm.BaseBindActivity;
import com.shengjiangbo.databindingadapter.databinding.ActivitySplashBinding;

/**
 * 创建人：Bobo
 * 创建时间：2022/1/20 15:44
 * 类描述：
 */
public class SplashActivity extends BaseBindActivity<ActivitySplashBinding> {
    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void setLayoutView(int layoutId) {

    }

    @Override
    public void setImmersiveStatusBar() {
        super.setImmersiveStatusBar();
        Log.e("setImmersiveStatusBar", "setImmersiveStatusBar: ");
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setOnExitAnimationListener(new SplashScreen.OnExitAnimationListener() {
            @Override
            public void onSplashScreenExit(@NonNull SplashScreenViewProvider splashScreenViewProvider) {
                finish();
                openActivity(MainActivity.class);
            }
        });
    }
}
