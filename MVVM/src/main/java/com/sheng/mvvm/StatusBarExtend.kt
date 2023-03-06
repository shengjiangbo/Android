package com.sheng.mvvm

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Immerse 沉浸。设置沉浸的方式
 *
 * @param type Type.systemBars(),Type.statusBars(),Type.navigationBars()
 * @param statusIsBlack 专栏文字 true 黑色,false 白色
 * @param navigationIsBlack 导航栏按钮 true 黑色,false 白色
 * @param color 状态和导航栏的背景颜色
 */
fun Activity.immerse(
    @WindowInsetsCompat.Type.InsetsType type: Int = WindowInsetsCompat.Type.systemBars(),
    statusIsBlack: Boolean = true,
    navigationIsBlack: Boolean = false,
    @ColorInt color: Int = Color.TRANSPARENT
) {
//    window.decorView  兼容低版本findViewById<FrameLayout>(android.R.id.content)
    when (type) {
//        WindowInsetsCompat.Type.systemBars() -> { //沉浸入导航栏和状态栏
//            WindowCompat.setDecorFitsSystemWindows(window, false)
//            window.statusBarColor = color
//            window.navigationBarColor = color
//            ViewCompat.getWindowInsetsController(findViewById<FrameLayout>(android.R.id.content))
//                ?.let { controller ->
//                    controller.isAppearanceLightStatusBars = statusIsBlack
//                    controller.isAppearanceLightNavigationBars = navigationIsBlack
//                }
//            findViewById<FrameLayout>(android.R.id.content).apply {
//                setPadding(0, 0, 0, 0)
//            }
//        }
        WindowInsetsCompat.Type.statusBars() -> {//只沉浸入状态栏
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = color
            ViewCompat.getWindowInsetsController(findViewById<FrameLayout>(android.R.id.content))
                ?.let { controller ->
                    controller.isAppearanceLightStatusBars = statusIsBlack
                    findViewById<FrameLayout>(android.R.id.content).apply {
                        post {
                            setPadding(0, 0, 0, getNavigationBarsHeight())
                        }
                    }
                }
        }
        WindowInsetsCompat.Type.navigationBars() -> {//只沉浸入导航栏
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.navigationBarColor = color
            ViewCompat.getWindowInsetsController(findViewById<FrameLayout>(android.R.id.content))
                ?.let { controller ->

                    controller.isAppearanceLightNavigationBars = navigationIsBlack
                    findViewById<FrameLayout>(android.R.id.content).apply {
                        post {
                            setPadding(0, getStatusBarsHeight(), 0, 0)
                        }
                    }
                }
        }
        else -> {
            // no work
        }
    }
}

/**
 *
 * @receiver Activity 全屏
 */
fun Activity.hideSystemUI() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    ViewCompat.getWindowInsetsController(findViewById<FrameLayout>(android.R.id.content))
        ?.let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            //底部导航栏
//            controller.hide(WindowInsetsCompat.Type.navigationBars())
        }
}

/**
 * 退出全屏
 * @receiver Activity
 */
fun Activity.showSystemUI() {
    WindowCompat.setDecorFitsSystemWindows(window, true)
    ViewCompat.getWindowInsetsController(findViewById<FrameLayout>(android.R.id.content))
        ?.let { controller ->
            controller.show(WindowInsetsCompat.Type.systemBars())
            //底部导航栏
//            controller.show(WindowInsetsCompat.Type.navigationBars())
        }
}

/**
 *
 * @receiver Activity
 * @param statusBarColor Int 状态栏背景颜色
 * @param light Boolean 状态栏文字颜色
 */
fun Activity.showStatusBar(statusBarColor: Int, light: Boolean) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = statusBarColor //设置底色
    ViewCompat.getWindowInsetsController(findViewById<FrameLayout>(android.R.id.content))
        ?.let { controller ->
            controller.show(WindowInsetsCompat.Type.statusBars())
            controller.isAppearanceLightStatusBars = light//true字体黑色,false白色
        }
}

val Activity.windowInsetsCompat: WindowInsetsCompat?
    get() = ViewCompat.getRootWindowInsets(findViewById(android.R.id.content))

fun Activity.getNavigationBarsHeight(): Int {
    val windowInsetsCompat = windowInsetsCompat ?: return 0
    return windowInsetsCompat.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
}

fun Activity.getStatusBarsHeight(): Int {
    val windowInsetsCompat = windowInsetsCompat ?: return 0
    return windowInsetsCompat.getInsets(WindowInsetsCompat.Type.statusBars()).top
}


/**
 *
 * @receiver Activity 键盘显示隐藏
 */
fun Activity.hideSoftKeyboard() {
    ViewCompat.getWindowInsetsController(findViewById<FrameLayout>(android.R.id.content))
        ?.hide(WindowInsetsCompat.Type.ime())
}

fun Activity.showSoftKeyboard() {
    ViewCompat.getWindowInsetsController(findViewById<FrameLayout>(android.R.id.content))
        ?.show(WindowInsetsCompat.Type.ime())
}

/**
 * 监听键盘高度变化
 * @param view View
 * @param onAction Function1<[@kotlin.ParameterName] Int, Unit>
 */
fun View.addKeyBordHeightChangeCallBack(onAction: (height: Int) -> Unit) {
    var posBottom: Int
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val cb = object : WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
            override fun onProgress(
                insets: WindowInsets, animations: MutableList<WindowInsetsAnimation>
            ): WindowInsets {
                posBottom =
                    insets.getInsets(WindowInsetsCompat.Type.ime()).bottom + insets.getInsets(
                        WindowInsetsCompat.Type.systemBars()
                    ).bottom
                onAction.invoke(posBottom)
                return insets
            }
        }
        setWindowInsetsAnimationCallback(cb)
    } else {
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            posBottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom + insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            ).bottom
            onAction.invoke(posBottom)
            insets
        }
    }
}




