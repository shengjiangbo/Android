package com.sheng.mvvm

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


/**
 * 创建人：Bobo
 * 创建时间：2021/5/15 9:43
 * 类描述：沉浸式状态栏工具
 */
object ImmersiveStatusBar {

    fun setImmersiveStatusBar(fragment: Fragment, fontIconDark: Boolean) {
        fragment.activity?.let {
            setTranslucentStatus(it)
            if (fontIconDark) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || isMIUI()
                    || isFlyme()
                ) {
                    setStatusBarFontIconDark(true, it)
                }
            }
        }
    }

    fun setImmersiveStatusBar(activity: Activity, fontIconDark: Boolean) {
        activity.let {
            setTranslucentStatus(it)
            if (fontIconDark) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || isMIUI()
                    || isFlyme()
                ) {
                    setStatusBarFontIconDark(true, it)
                }
            }
        }
    }

    /**
     * 设置沉浸式状态栏
     *
     * @param fontIconDark 状态栏字体和图标颜色是否为深色
     */
    fun setImmersiveStatusBar(fontIconDark: Boolean, statusBarPlaceColor: Int, activity: Activity) {
        var statusBarPlaceColor = statusBarPlaceColor
        setTranslucentStatus(activity)
        if (fontIconDark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || isMIUI()
                || isFlyme()
            ) {
                setStatusBarFontIconDark(true, activity)
            } else {
                if (statusBarPlaceColor == Color.WHITE) {
                    statusBarPlaceColor = -0x333334
                    //                    setStatusBarPlaceColor(statusBarPlaceColor);
                }
            }
        }
    }


    /**
     * 设置状态栏透明
     */
    private fun setTranslucentStatus(activity: Activity) {
        // 5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = activity.window.decorView
            val option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            decorView.systemUiVisibility = option
            activity.window.statusBarColor = Color.TRANSPARENT
        } else
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }


    /**
     * 设置Android状态栏的字体颜色，状态栏为亮色的时候字体和图标是黑色，状态栏为暗色的时候字体和图标为白色
     *
     * @param dark 状态栏字体是否为深色
     */
    private fun setStatusBarFontIconDark(dark: Boolean, activity: Activity) {
        // 小米MIUI
        try {
            val window = activity.window
            val clazz: Class<*> = activity.getWindow().javaClass
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod(
                "setExtraFlags",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
            } else {       //清除黑色字体
                extraFlagField.invoke(window, 0, darkModeFlag)
            }
        } catch (e: Exception) {
//            e.printStackTrace();
        }

        // 魅族FlymeUI
        try {
            val window = activity.window
            val lp = window.attributes
            val darkFlag =
                WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            value = if (dark) {
                value or bit
            } else {
                value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            window.attributes = lp
        } catch (e: Exception) {
//            e.printStackTrace();
        }
        // android6.0+系统
        // 这个设置和在xml的style文件中用这个<item name="android:windowLightStatusBar">true</item>属性是一样的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                activity.window.decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            }
        }
    }

    private fun isMIUI(): Boolean {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"))
    }

    private fun isFlyme(): Boolean {
        return try {
            val method = Build::class.java.getMethod("hasSmartBar")
            method != null
        } catch (e: java.lang.Exception) {
            false
        }
    }

    private fun getSystemProperty(propName: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return line
    }

    public fun test(activity: Activity) {
        val insetsController: WindowInsetsControllerCompat =
            ViewCompat.getWindowInsetsController(activity.window.decorView) ?: return
        // 隐藏状态栏、导航栏、标题栏
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
// 显示状态栏、导航栏、标题栏
        insetsController.show(WindowInsetsCompat.Type.systemBars())
// 隐藏导航栏
        insetsController.hide(WindowInsetsCompat.Type.navigationBars())
// 显示导航栏
        insetsController.show(WindowInsetsCompat.Type.navigationBars())
// 显示状态栏
        insetsController.show(WindowInsetsCompat.Type.statusBars())
//隐藏状态栏
        insetsController.hide(WindowInsetsCompat.Type.statusBars())
// 显示键盘
        insetsController.show(WindowInsetsCompat.Type.ime())
// 隐藏键盘
        insetsController.hide(WindowInsetsCompat.Type.ime())
// 控制状态栏字体颜色显示为白色
        insetsController.isAppearanceLightStatusBars = false
// 控制导航栏字体显示为黑色
        insetsController.isAppearanceLightStatusBars = true
// 导航栏颜色显示为白色
        insetsController.isAppearanceLightNavigationBars = false
// 导航栏显示为黑色
        insetsController.isAppearanceLightNavigationBars = true
    }
}