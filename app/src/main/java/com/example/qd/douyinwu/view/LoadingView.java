package com.example.qd.douyinwu.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.qd.douyinwu.R;


/**
 *
 */
public class LoadingView extends ProgressDialog {

    private final Activity mContext;

    public LoadingView(Activity context) {
        super(context);
        this.mContext = context;
    }
    public LoadingView(Activity context, int theme) {
        super(context, theme);
        this.mContext = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }
    private void init(Context context) {
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.loading);//loading的xml文件
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }
    @Override
    public void show() {//开启
        super.show();
    }
    @Override
    public void dismiss() {//关闭
        if (mContext != null && !mContext.isFinishing())
            super.dismiss();
    }
}