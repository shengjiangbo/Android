package com.sheng.mvvm;

import android.app.Activity;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 */

public class ActivityStack {
    private static Stack<Activity> activityStack;
    private static ActivityStack instance;

    private ActivityStack() {
    }

    /**
     * 单一实例
     */
    public static ActivityStack create() {
        if (instance == null) {
            synchronized (ActivityStack.class) {
                if (instance == null) {
                    instance = new ActivityStack();
                }
            }
        }
        return instance;
    }

    public Stack<Activity> getActivityStack() {
        return activityStack;
    }

    /**
     * 添加Activity到堆栈
     */
    public void add(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 移除堆栈中指定Activity
     */
    public void remove(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.remove(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity top() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finish() {
        Activity activity = activityStack.lastElement();
        finish(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finish(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finish(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finish(activity);
                break;
            }
        }
    }

    public void finish(String name) {
        for (Activity activity : activityStack) {
            if (activity.getClass().getSimpleName().equals(name)) {
                finish(activity);
                break;
            }
        }
    }

    /**
     * 结束多个Activity
     *
     * @param cls
     */
    public void finish(Class<?>... cls) {
        List<Activity> list = new ArrayList<>();
        for (Activity activity : activityStack) {
            for (Class<?> cl : cls) {
                if (activity.getClass().equals(cl)) {
                    list.add(activity);
                    break;
                }
            }
        }
        for (Activity activity : list) {
            finish(activity);
        }
    }


    /**
     * 结束所有Activity
     */
    public void finishAll() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            Activity activity = activityStack.get(i);
            if (null != activity && !activity.isFinishing()) {
                activity.finish();
                activity = null;
            }
        }
        activityStack.clear();
    }

    /**
     * 结束所有Activity
     *
     * @param cls 传入的activity不结束
     */
    public void finishAll(Class<?> cls) {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            Activity activity = activityStack.get(i);
            if (null != activity && !activity.isFinishing() && !activity.getClass().equals(cls)) {
                activity.finish();
                activity = null;
            }
        }
        activityStack.clear();
    }

    /**
     * 获取指定的Activity
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        }
        return null;
    }


    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAll();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
