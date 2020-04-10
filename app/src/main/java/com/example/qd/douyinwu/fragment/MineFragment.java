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
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.been.UserBean;
import com.example.qd.douyinwu.utils.PersonInfoManager;
import com.google.gson.Gson;

public class MineFragment extends Fragment {
    private View rootView;
    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView ==null){
            rootView=inflater .inflate(R.layout.fragment_mine  ,container,false) ;
        }
        initView();
        return rootView;
    }



    private void initView(){
        webView = rootView.findViewById(R.id.webview);
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
        webView.addJavascriptInterface(new JsBridge(getContext()),"JsBridge");
        webView.loadUrl("http://slive.sdyilian.top/index.php/Personal/Index/index");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if (url.startsWith("alipays://")) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        return true;
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

    class JsBridge {
        private Context context;

        public JsBridge(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void saveUserData(String userData) {
            UserBean userBean = new Gson().fromJson(userData,UserBean.class);
            PersonInfoManager.INSTANCE.setUserBean(userBean);
        }
    }



}
