package com.example.qd.douyinwu.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.qd.douyinwu.utils.BarTextColorUtils;
import com.example.qd.douyinwu.utils.ViewUtils;
import com.example.qd.douyinwu.view.LoadingView;

public abstract class NativeBaseFragment extends Fragment {
    private View mContentView;
    private Context mContext;
    private FrameLayout mRlContent;
    protected View mRootView;

    protected TextView mTvTitleRight;
    protected TextView mTvTitleCenter;
    protected ImageView mIvTitleLift;
    protected TextView mTvSingleStatusBar;

    private LinearLayout mLlCommentTopTitle;
    private RelativeLayout mRlTitle;
    protected ImageView mIvTitleRight;
    protected ImageView mIvTitleCenter;

    public RelativeLayout mRlLoading;
    protected ImageView mIvLoading;
    protected RelativeLayout mRlNoData;
    private LoadingView loading;
    private View mView = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView != null){
            ViewGroup parent = (ViewGroup) mView.getParent();
            if(parent != null){
                parent.removeView(mView);
            }
            return mView;
        }
        mRootView = null;
//        if (mRootView == null){
        mRootView = inflater.inflate(R.layout.fragment_base_native, container, false);
        mContext = getActivity();
        mRlNoData = mRootView.findViewById(R.id.rl_no_data);
        mRlLoading = mRootView.findViewById(R.id.rl_loading);
        mIvLoading = mRootView.findViewById(R.id.iv_loading);
        Glide.with(this).load(R.mipmap.loading).into(mIvLoading);

        mRlContent = (FrameLayout) mRootView.findViewById(R.id.content1);
        mContentView = LayoutInflater.from(mContext).inflate(setLayoutResourceID(), null);
        mRlContent.addView(mContentView);

        init();
        initTitleView();
        setUpView();
        setUpData();
//        }
        mView = mRootView;
        return mRootView;
    }

    protected void showLoading(){
        if (loading == null){
            loading = new LoadingView(getActivity(), R.style.CustomDialog);
        }
        loading.show();
    }
    protected void hintLoading(){
        if (loading != null && loading.isShowing()){
            loading.dismiss();
        }
    }


    private void initTitleView() {
        //初始化标题
        mLlCommentTopTitle = mRootView.findViewById(R.id.ll_comment_top_title);
        mTvTitleRight = mRootView.findViewById(R.id.tv_title_right);
        mTvTitleCenter = mRootView.findViewById(R.id.tv_comment_center);
        mIvTitleLift = mRootView.findViewById(R.id.iv_title_left);
        mTvSingleStatusBar = mRootView.findViewById(R.id.tv_single_statusBar);
        mRlTitle = mRootView.findViewById(R.id.rl_title);
        mIvTitleRight = mRootView.findViewById(R.id.iv_title_Right);
        mIvTitleCenter = mRootView.findViewById(R.id.iv_title_center);

    }



    //显示状态栏
    protected void setVisibleStatusBar(String colorStr){
        int color = Color.parseColor(colorStr);
        ViewGroup.LayoutParams params = mTvSingleStatusBar.getLayoutParams();
        params.height = ViewUtils.getStatusBarHeight(getMContext());
        mTvSingleStatusBar.setLayoutParams(params);
//        mTvSingleStatusBar.setVisibility(View.VISIBLE);

        //设置状态栏的颜色
        if (color == Color.WHITE){
            BarTextColorUtils.StatusBarLightMode(getActivity());
        }

        BarTextColorUtils.StatusBarTextWhiteLightMode(getActivity());

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
        mIvTitleLift.setVisibility(View.GONE);
    }


    /**
     * 此方法用于返回Fragment设置ContentView的布局文件资源ID
     *
     * @return 布局文件资源ID
     */
    protected abstract int setLayoutResourceID();

    /**
     * 一些View的相关操作
     */
    protected abstract void setUpView();

    /**
     * 一些Data的相关操作
     */
    protected abstract void setUpData();

    /**
     * 此方法用于初始化成员变量及获取Intent传递过来的数据
     * 注意：这个方法中不能调用所有的View，因为View还没有被初始化，要使用View在initView方法中调用
     */
    protected void init() {}

    public View getContentView() {
        return mContentView;
    }

    //设置标题

    public Context getMContext() {
        return mContext;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        hintLoading();
    }
}
