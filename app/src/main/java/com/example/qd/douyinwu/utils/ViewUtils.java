package com.example.qd.douyinwu.utils;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/10/26.
 */

public class ViewUtils {

    //获取状态栏的高度
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }



}
