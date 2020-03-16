package com.example.qd.douyinwu.utils;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * activity堆栈式管理
 */
public class AppManager {
    private static Stack<Activity> activityStack = new Stack<Activity>();

    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static synchronized AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }


    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (int i = 0;i<activityStack.size();i++){
            Activity activity = activityStack.get(i);
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                //				break;
            }
        }
//        for (Activity activity : activityStack) {
//            if (activity.getClass().equals(cls)) {
//                finishActivity(activity);
//                //				break;
//            }
//        }
    }

    /**
     * 返回activity数量
     */
    public int getCount() {
        if (activityStack != null) {
            return activityStack.size();
        } else {
            return 0;
        }
    }

    /**
     * 除了指定类名的Activity，结束所有activity
     *  private static Stack<Activity> activityStack = new Stack<Activity>();
     * mainactivity   bondetailactivity loginactivity  registeractivity
     * 数组2  registeractivity   mainactivity loginactivity
     */
    public void finishAssignActivity(Class<?>[] cls) {
        for (Activity activity : activityStack) {
            if (activity != null) {
                for (int i = 0; i < cls.length; i++) {
                    Class cl = cls[i];
                    if (!activity.getClass().equals(cls[i])) {
                        if (!activity.getClass().equals(cls[i]) && i == cls.length - 1) {
                            activity.finish();
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

    /**
     * 结束除当前传入以外所有Activity
     */
    public void finishOthersActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (!activity.getClass().equals(cls)) {
                    activity.finish();
                }
            }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (Activity activity : activityStack) {
            if (activity != null)
                activity.finish();
        }
        activityStack.clear();
    }

    /**
     * 获取指定的Activity
     *
     * @author kymjs
     */
    public Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            // 杀死该应用进程
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);
        } catch (Exception e) {
        }
    }


    public void exit() {
        try {
            for (Activity activity : activityStack) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }



    //TODO HUANGJUN
//    /**
//     * 获得显示进度的activity
//     *
//     * @return
//     */
//
//    public ProgressDialogShowing getDialogShowing() {
//        ProgressDialogShowing progressDialogShowing = (ProgressDialogShowing) currentActivity();
//        return progressDialogShowing;
//    }
}