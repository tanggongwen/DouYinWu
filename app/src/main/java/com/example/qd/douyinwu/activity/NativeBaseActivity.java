package com.example.qd.douyinwu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.utils.AppManager;
import com.example.qd.douyinwu.utils.BarTextColorUtils;
import com.example.qd.douyinwu.utils.ViewUtils;
import com.example.qd.douyinwu.view.LoadingView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class NativeBaseActivity extends AppCompatActivity implements View.OnClickListener {

    private View mContentView;
    protected TextView mTvTitleRight;
    private TextView mTvTitleCenter;
    protected ImageView mIvTitleLift;
    protected TextView mTvSingleStatusBar;
    private LinearLayout mLlCommentTopTitle;
    protected TextView mTvBottom;

    protected ImageView mIvLoading;
    public RelativeLayout mRlLoading;

    private RelativeLayout mRlTitle;
    public View mRlNoData;
    private boolean isRestart;
    private LoadingView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        CommonHeadUtils.fullScreen(this);
        savedInstanceState = null;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_native_base);

        FrameLayout content = findViewById(R.id.content);
        content.removeAllViews();

        mContentView = LayoutInflater.from(this).inflate(setLayoutResourceID(),null);
        content.addView(mContentView);

        //初始化标题
        mTvTitleRight = findViewById(R.id.tv_title_right);
        mTvTitleCenter = findViewById(R.id.tv_comment_center);
        mIvTitleLift = findViewById(R.id.iv_title_left);
        mTvSingleStatusBar = findViewById(R.id.tv_single_statusBar);
        mLlCommentTopTitle = findViewById(R.id.ll_comment_top_title);
        mRlTitle = findViewById(R.id.rl_title);

        mTvBottom= findViewById(R.id.tv_bottom);

        mIvLoading = findViewById(R.id.iv_loading);
        mRlLoading = findViewById(R.id.rl_loading);
        mRlNoData = findViewById(R.id.rl_no_data);

        Glide.with(this).load(R.mipmap.loading).into(mIvLoading);

        init();
        setUpView();
        setUpData();

//        BarTextColorUtils.StatusBarTextWhiteLightMode(this);
//        setBottomNull();

        findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();
            }
        });
        findViewById(R.id.hint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hintLoading();
            }
        });


    }

    protected void showLoading(){
        if (loading == null){
            loading = new LoadingView(this, R.style.CustomDialog);
        }
      loading.show();
    }
    protected void hintLoading(){
        if (loading != null && loading.isShowing()){
            loading.dismiss();
        }
    }


    //显示状态栏
    protected void setVisibleStatusBar(String colorStr){
        int color = Color.parseColor(colorStr);
        ViewGroup.LayoutParams params = mTvSingleStatusBar.getLayoutParams();
        params.height = ViewUtils.getStatusBarHeight(this);
        mTvSingleStatusBar.setLayoutParams(params);
//        mTvSingleStatusBar.setVisibility(View.VISIBLE);
        //设置状态栏的颜色
        if (color == Color.WHITE){
            BarTextColorUtils.StatusBarLightMode(this);

            //字体设为灰色
            mTvTitleCenter.setTextColor(getResources().getColor(R.color.common_textcolor_black_33));

            mIvTitleLift.setImageResource(R.mipmap.ic_back_black);
        }
        mLlCommentTopTitle.setBackgroundColor(color);
    }

    //显示标题栏
    protected void setVisibleTitleBar(String colorStr,String title){
        //显示状态栏
        setVisibleStatusBar(colorStr);
        //显示标题
        mRlTitle.setVisibility(View.VISIBLE);
        //设置标题
        mTvTitleCenter.setText(title);
        //点击返回键
        mIvTitleLift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //显示标题栏
    protected void setVisibleTitleBar(String colorStr,String title,String rightTitle){
        //显示状态栏
        setVisibleStatusBar(colorStr);
        //显示标题
        mRlTitle.setVisibility(View.VISIBLE);
        mTvTitleRight.setTextColor(getResources().getColor(R.color.white));
        mTvTitleRight.setVisibility(View.VISIBLE);
        mTvTitleRight.setText(rightTitle);
        //设置标题
        mTvTitleCenter.setText(title);
        //点击返回键
        mIvTitleLift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    //设置底部
    protected void setBottomNull(){
        //设置底部按钮的高度
        int navigationBarHeight = BarTextColorUtils.getNavigationBarHeight(this);
        ViewGroup.LayoutParams paramsBottom = mTvBottom.getLayoutParams();
        paramsBottom.height = navigationBarHeight;
        mTvBottom.setLayoutParams(paramsBottom);
        mTvBottom.setVisibility(View.VISIBLE);
    }



    /**
     * 此方法用于返回Fragment设置ContentView的布局文件资源ID
     *
     * @return 布局文件资源ID
     */
    public abstract int setLayoutResourceID();

    /**
     * 一些View的相关操作
     */
    public abstract void setUpView();

    /**
     * 一些Data的相关操作
     */
    public abstract void setUpData();

    /**
     * 此方法用于初始化成员变量及获取Intent传递过来的数据
     * 注意：这个方法中不能调用所有的View，因为View还没有被初始化，要使用View在initView方法中调用
     */
    public void init() {
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 设置 app 不随着系统字体的调整而变化
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hintLoading();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private static boolean isWifiProxy(Context mContext) {
        final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        } else {
            proxyAddress = android.net.Proxy.getHost(mContext);
            proxyPort = android.net.Proxy.getPort(mContext);
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }

    private boolean fixOrientation(){
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo)field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isTranslucentOrFloating(){
        boolean isTranslucentOrFloating = false;
        try {
            int [] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean)m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

}
