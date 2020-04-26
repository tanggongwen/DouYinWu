package com.example.qd.douyinwu.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.utils.PersonInfoManager;

import java.util.HashMap;
import java.util.Map;

public class MarketFragment extends Fragment {
    private View rootView;
    private WebView webView;
    private MyWebViewListener listener;
    private boolean isFirstLoad = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView ==null){
            rootView=inflater .inflate(R.layout.act_market  ,container,false) ;
        }
        initView();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (MyWebViewListener) context;
    }

    private void initView(){
        webView = rootView.findViewById(R.id.webview);
        listener.onWebViewBack(webView);
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAllowFileAccess(false);
        settings.setGeolocationEnabled(true);
        settings.setSaveFormData(false);
        settings.setSupportZoom(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(false);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        String userId = "";
        if (null!=PersonInfoManager.INSTANCE.getUserBean()){
            userId = PersonInfoManager.INSTANCE.getUserBean().getUser_id();
        }
        webView.loadUrl("http://slive.sdyilian.top/View/SelfStore/mineStore?user_id="+ userId);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Map<String, String> headers = new HashMap<>();
                try {
                    if (url.startsWith("alipays://")) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }else if (url.startsWith("weixin://wap/pay?")){
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } else if (url.contains("wx.tenpay.com")) {
                        //wx.tenpay.com 收银台点击微信时，shouldOverrideUrlLoading会调用两次，这里是第二次
                        headers.put("Referer", "https://slive.sdyilian.top");//第三方支付平台请求头 一般是对方固定
                    } else {
                        //payh5.bbnpay
                        if(!isFirstLoad){
                            //跳转到收银台
                            headers.put("Referer", "https://slive.sdyilian.top");//商户申请H5时提交的授权域名
                            isFirstLoad = true;
                        }else{
                            //收银台点击微信时，shouldOverrideUrlLoading会调用两次，这里是第一次
                            headers.put("Referer", "https://slive.sdyilian.top");//第三方支付平台请求头 一般是对方固定
                        }
                    }
                } catch (Exception e) {
                    return false;
                }
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return false;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return false;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return false;
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            if (null!=webView&&null!=listener){
                listener.onWebViewBack(webView);
            }
        }
    }

    public interface MyWebViewListener{
        public void onWebViewBack(WebView webView);
    }


}
