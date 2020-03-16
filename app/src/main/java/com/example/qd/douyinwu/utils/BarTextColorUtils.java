package com.example.qd.douyinwu.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BarTextColorUtils {

    public static int StatusBarTextWhiteLightMode(Activity activity){
        int result=0;
//        //这个方法只支持4.0以上系统
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if(FlymeSetStatusBarLightMode(activity.getWindow(), true)){//判断是不是魅族系统
//                result=2;
//            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//判断当前是不是6.0以上的系统
//                setTranslucentForCoordinatorLayout(activity, 255);
//                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//                result=3;
//            }else {//如果以上都不符合就只能加个半透明的背景色了
//                result=4;
//                setTranslucentForCoordinatorLayout(activity, 255);
//            }
//        }

        L.e("qpf","设置当前状态栏文字颜色 -- " + result);
        return result;
    }

    public static int StatusBarLightMode(Activity activity){
        int result=0;
//        //这个方法只支持4.0以上系统
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if(FlymeSetStatusBarLightMode(activity.getWindow(), true)){//判断是不是魅族系统
//                result=2;
//            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//判断当前是不是6.0以上的系统
//                activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                setTranslucentForCoordinatorLayout(activity, 255);
//                result=3;
//            }else {//如果以上都不符合就只能加个半透明的背景色了
//                result=4;
//                setTranslucentForCoordinatorLayout(activity, 255);
//            }
//        }
        return result;
    }
    //带有透明颜色的状态栏
    public static void setTranslucentForCoordinatorLayout(Activity activity, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(activity);//先将状态栏设置为完全透明
        addTranslucentView(activity, statusBarAlpha);//添加一个自定义透明度的矩形状态栏
    }
    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    /**
     * 添加半透明矩形条
     *
     * @param activity       需要设置的 activity
     * @param statusBarAlpha 透明值
     */
    private static void addTranslucentView(Activity activity, int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        if (contentView.getChildCount() > 1) {
            contentView.getChildAt(1).setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else {
            //contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
        }
    }
    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 修改小米手机系统的
     * @param window
     * @param dark
     * @return
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;
            }catch (Exception e){

            }
        }
        return result;
    }

    /**
     * 魅族手机修改该字体颜色
     * @param window
     * @param dark
     * @return
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }


    //小米手机设置状态栏字体颜色为黑色
    private static void SetStatusBarLightMode(Activity context) {
        if (context.getWindow() != null) {
            Class clazz = context.getWindow().getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(context.getWindow(), darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //获取虚拟按键的高度
    public static int getNavigationBarHeight(Context context) {
        int result = 1;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        L.e("qpf","设置底部 -- " + result);
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }
}



