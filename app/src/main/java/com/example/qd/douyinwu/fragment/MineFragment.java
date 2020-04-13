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
import com.example.qd.douyinwu.utils.PersonInfoManager;
import com.google.gson.Gson;

import java.io.File;

public class MineFragment extends Fragment {
    private View rootView;
    private WebView webView;
    public ValueCallback<Uri> mFilePathCallback;
    public ValueCallback<Uri[]> mFilePathCallbackArray;
    public static final int PICK_REQUEST = 1;
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
}
