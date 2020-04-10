package com.example.qd.douyinwu.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tencent.cos.xml.utils.StringUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 基础工具类，包含了单位转换等
 */
public class Utils {


    /**
     * Created by goldze on 2017/5/14.
     * 常用工具类
     */

        private static SimpleDateFormat formatterYmd = new SimpleDateFormat("MM-dd");//初始化Formatter的转换格式。
        private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");//初始化Formatter的转换格式。

        @SuppressLint("StaticFieldLeak")
        private static Context context;

        private Utils() {
            throw new UnsupportedOperationException("u can't instantiate me...");
        }

        /**
         * 初始化工具类
         *
         * @param context 上下文
         */
        public static void init(@NonNull final Context context) {
            Utils.context = context.getApplicationContext();
        }

        /**
         * 获取ApplicationContext
         *
         * @return ApplicationContext
         */
        public static Context getContext() {
            if (context != null) {
                return context;
            }
            throw new NullPointerException("should be initialized in application");
        }



        /**
         * 秒转换为指定格式的日期
         * @param second
         * @param patten
         * @return
         */
        public static String secondToDate(long second,String patten) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(second);//转换为毫秒
            Date date = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat(patten);
            String dateString = format.format(date);
            return dateString;
        }



        /**
         *
         * @param time 单位秒
         * @return
         */
        public static String getTimeTip(String time,String timeTag){
            String tip="0";

            if (StringUtils.isEmpty(time)){
                return tip;
            }
            int s = Integer.parseInt(time);
            if (s>60&&s<3600){
                tip = s/60+"分钟前";
            }else if (s>3600&&s<3600*24){
                tip = s/3600+"小时前";
            } else if (s<60){
                tip = "刚刚";
            } else {
                tip = getTimeStr(timeTag);
            }
            return tip;
        }

        /**
         *
         * @param time 单位秒
         * @return
         */
        public static String getCommentTimeTip(String time,String timeTag){
            String tip="0";

            if (StringUtils.isEmpty(time)){
                return tip;
            }
            int s = Integer.parseInt(time);
            if (s>60&&s<3600){
                tip = s/60+"分钟前";
            }else if (s>3600&&s<3600*24){
                tip = s/3600+"小时前";
            } else if (s<60){
                tip = "刚刚";
            } else {
                tip = getTimeHMSStr(timeTag);
            }
            return tip;
        }


        public static String getTimeHMSStr(String time){
            if (StringUtils.isEmpty(time)){
                return "0";
            }
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return formatter.format(new Date(Long.valueOf(time+"000")));
        }

        public static String getTimeStr(String time){
            if (StringUtils.isEmpty(time)){
                return "0";
            }
            formatterYmd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return formatterYmd.format(new Date(Long.valueOf(time+"000")));
        }

        public static String getViewCountFormat(String s){
            if (StringUtils.isEmpty(s)){
                return "0";
            }
            int viewCount = Integer.parseInt(s);
            if (viewCount<10000){
                return viewCount+"";
            }
            DecimalFormat df = new DecimalFormat("0.00");//格式化小数
            String num = df.format((float)Integer.parseInt(s)/10000);//返回的是String类型
            return num+"万";
        }


    public static String getRealPathFromURI(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        if (scheme.contains("http")){
            return "";
        }

        String realPath = null;
        if (scheme == null)
            realPath = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            realPath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA},
                    null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        realPath = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        if (TextUtils.isEmpty(realPath)) {
            if (uri != null) {
                String uriString = uri.toString();
                int index = uriString.lastIndexOf("/");
                String imageName = uriString.substring(index);
                File storageDir;

                storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File file = new File(storageDir, imageName);
                if (file.exists()) {
                    realPath = file.getAbsolutePath();
                } else {
                    storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File file1 = new File(storageDir, imageName);
                    realPath = file1.getAbsolutePath();
                }
            }
        }
        return realPath;

    }


}
