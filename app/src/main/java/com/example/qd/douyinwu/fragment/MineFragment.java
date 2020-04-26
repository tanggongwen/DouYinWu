package com.example.qd.douyinwu.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.been.UserBean;
import com.example.qd.douyinwu.been.WechatBean;
import com.example.qd.douyinwu.utils.GsonUtil;
import com.example.qd.douyinwu.utils.JsonUtils;
import com.example.qd.douyinwu.utils.L;
import com.example.qd.douyinwu.utils.PersonInfoManager;
import com.example.qd.douyinwu.utils.UmengShareManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MineFragment extends Fragment {
    private View rootView;
    private WebView webView;
    public ValueCallback<Uri> mFilePathCallback;
    public ValueCallback<Uri[]> mFilePathCallbackArray;
    public static final int PICK_REQUEST = 1;
    private boolean isFirstLoad = false;
    private MarketFragment.MyWebViewListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView ==null){
            rootView=inflater .inflate(R.layout.fragment_mine  ,container,false) ;
        }
        initView();
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (MarketFragment.MyWebViewListener) context;
    }

    private void initView(){
        webView = rootView.findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        listener.onWebViewBack(webView);
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
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (mFilePathCallbackArray != null) {
                    mFilePathCallbackArray.onReceiveValue(null);
                }
                mFilePathCallbackArray = filePathCallback;
                handleup(filePathCallback);
                return true;
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
        @JavascriptInterface
        public void loginByWechat(){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UmengShareManager.INSTANCE.loginByWechat(getActivity(),authListener );
                }
            });
        }
    }

    private void handleup(ValueCallback<Uri[]> uploadFile) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_REQUEST) {
            if (null != data) {
                Uri uri = data.getData();
                handleCallback(uri);
            } else {
                // 取消了照片选取的时候调用
                handleCallback(null);
            }
        } else {
            // 取消了照片选取的时候调用
            handleCallback(null);
        }
    }
    private void handleCallback(Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mFilePathCallbackArray != null) {
                if (uri != null) {
                    mFilePathCallbackArray.onReceiveValue(new Uri[]{uri});
                } else {
                    mFilePathCallbackArray.onReceiveValue(null);
                }
                mFilePathCallbackArray = null;
            }
        } else {
            if (mFilePathCallback != null) {
                if (uri != null) {
                    String url = getFilePathFromContentUri(uri, getActivity().getContentResolver());
                    Uri u = Uri.fromFile(new File(url));

                    mFilePathCallback.onReceiveValue(u);
                } else {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = null;
            }
        }
    }

    public static String getFilePathFromContentUri(Uri selectedVideoUri,
                                                   ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    private UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            L.e("tanggongwen","start");
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            L.e("tanggongwen","complete");
            if (share_media.equals(SHARE_MEDIA.WEIXIN)){
                String nickname = map.get("name")+",";
                String icon =  map.get("iconurl")+",";
               String uid = map.get("uid");
                String jsMethod = "javascript:loginByWx(\""+nickname+icon+uid+"\")";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript(jsMethod, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                }else {
                    webView.loadUrl(jsMethod);
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            L.e("tanggongwen","onError");
            L.e("tanggongwen",throwable.toString());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            L.e("tanggongwen","onCancel");
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            if (null!=webView&&null!=listener){
                listener.onWebViewBack(webView);
            }
        }
    }

}
