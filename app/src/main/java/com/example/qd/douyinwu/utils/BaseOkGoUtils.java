package com.example.qd.douyinwu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.been.MyBaseBeen;
import com.example.qd.douyinwu.intef.ResultListener;
import com.example.qd.douyinwu.view.LoadingView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import okhttp3.OkHttpClient;

import static android.content.Context.MODE_PRIVATE;

/**
 * 网络请求公共类
 */
public class BaseOkGoUtils {

    private static HttpParams params;

    public static void getOkGo(final Context context, Map<String, Object> map, String url, final ResultListener listener) {

//        String userInfoStr = KVUtils.query(SPConstant.USER_BASE_INFO);
//        if (!StringUtil.isBlank(userInfoStr)){
//            UserInfo userInfo = new Gson().fromJson(userInfoStr, UserInfo.class);
//            if (userInfo != null){
//                map.put("token",userInfo.getToken());
//            }
//        }

        String ppp = new Gson().toJson(map);

        L.e("qpf","请求地址 -- " + url + "参数 -- " + ppp);
        params = mapParse(map);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(context)));
        OkGo.getInstance().setOkHttpClient(builder.build());
//        HttpHeaders header = new HttpHeaders();
//        header.put("Cookie",";"+KVUtils.query(SPConstant.COOKIE));
        OkGo.<String>get(url)
                .params(params)
//                .headers(header)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try{
                            MyBaseBeen baseBeen = new Gson().fromJson(response.body(),MyBaseBeen.class);
                            if(baseBeen.getCode() == 200){
                                if(baseBeen.getData() != null){
                                    listener.onSucceeded(baseBeen.getData());
                                }else {
                                    listener.onSucceeded(response.body());
                                }
                            }else{
                                if(baseBeen.getCode() == 10001){
                                    listener.onErr(baseBeen.getCode()+"");
                                }else{
                                    listener.onErr(baseBeen.getMsg());
                                }
                                Toast.makeText(context,baseBeen.getMsg(),Toast.LENGTH_SHORT).show();
                                return;
                            }
//                            if ("0".equals(baseBeen.getError_code())){
//                                listener.onSucceeded(baseBeen.getData());
//                            }else if ("10001".equals(baseBeen.getError_code())){
//                                //跳转登录页面
//                                context.startActivity(new Intent(context, LoginActivity.class));
//                            }else if ("1".equals(baseBeen.getError_code())){
//                                listener.onFailed(baseBeen.getMsg());
//                                Toast.makeText(context,baseBeen.getMsg(),Toast.LENGTH_SHORT).show();
//                            }else {
//                                Toast.makeText(context,baseBeen.getMsg(),Toast.LENGTH_SHORT).show();
//                            }
                        }catch (Exception e){

                        }

                        L.e("qpf","获取数据成功 -- " + response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        L.e("qpf","获取数据失败 -- " + response.getException());
                        if (response != null) {
                            listener.onErr(response.getException().toString());
                        }
                    }});
    }


    public static void getMyOkGo(final Context context, Map<String, Object> map, String url, final ResultListener listener) {

//        String userInfoStr = KVUtils.query(SPConstant.USER_BASE_INFO);
//        if (!StringUtil.isBlank(userInfoStr)){
//            UserInfo userInfo = new Gson().fromJson(userInfoStr, UserInfo.class);
//            if (userInfo != null){
//                map.put("token",userInfo.getToken());
//            }
//        }

        String ppp = new Gson().toJson(map);

        L.e("qpf","请求地址 -- " + url + "参数 -- " + ppp);
        params = mapParse(map);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(context)));
        OkGo.getInstance().setOkHttpClient(builder.build());
//        HttpHeaders header = new HttpHeaders();
//        header.put("Cookie",";"+KVUtils.query(SPConstant.COOKIE));
        OkGo.<String>get(url)
                .params(params)
//                .headers(header)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try{
                            MyBaseBeen baseBeen = new Gson().fromJson(response.body(),MyBaseBeen.class);
                            if(baseBeen.getCode() == 200){
                                listener.onSucceeded(response.body());
                            }else{
                                if(baseBeen.getCode() == 10001){
                                    listener.onErr(baseBeen.getCode()+"");
                                }else{
                                    listener.onErr(baseBeen.getMsg());
                                }
                                Toast.makeText(context,baseBeen.getMsg(),Toast.LENGTH_SHORT).show();
                                return;
                            }
//                            if ("0".equals(baseBeen.getError_code())){
//                                listener.onSucceeded(baseBeen.getData());
//                            }else if ("10001".equals(baseBeen.getError_code())){
//                                //跳转登录页面
//                                context.startActivity(new Intent(context, LoginActivity.class));
//                            }else if ("1".equals(baseBeen.getError_code())){
//                                listener.onFailed(baseBeen.getMsg());
//                                Toast.makeText(context,baseBeen.getMsg(),Toast.LENGTH_SHORT).show();
//                            }else {
//                                Toast.makeText(context,baseBeen.getMsg(),Toast.LENGTH_SHORT).show();
//                            }
                        }catch (Exception e){

                        }

                        L.e("qpf","获取数据成功 -- " + response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        L.e("qpf","获取数据失败 -- " + response.getException());
                        if (response != null) {
                            listener.onErr(response.getException().toString());
                        }
                    }});
    }

    public static void postOkGo(final Context context, Map<String, Object> map, String url, final ResultListener listener) {
        String ppp = new Gson().toJson(map);
        L.e("qpf","请求地址 -- " + url + "参数 -- " + ppp);
        params = mapParse(map);
//        HttpHeaders header = new HttpHeaders();
//        header.put("Cookie",";"+KVUtils.query(SPConstant.COOKIE));
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(context)));
        OkGo.getInstance().setOkHttpClient(builder.build());
        OkGo.<String>post(url)
                .params(params)
//                .headers(header)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        L.e("qpf","返回结果 -- " + response.body());
                        try{
                            MyBaseBeen baseBeen = new Gson().fromJson(response.body(),MyBaseBeen.class);
                            if(baseBeen.getCode() == 200){
                                if(baseBeen.getData() != null){
                                    listener.onSucceeded(baseBeen.getData());
                                }else {
                                    listener.onSucceeded(response.body());
                                }
                            }else{
                                if(baseBeen.getCode() == 10001){
                                    listener.onErr(baseBeen.getCode()+"");
                                }else{
                                    listener.onErr(baseBeen.getMsg());
//                                    Toast.makeText(context,baseBeen.getMsg(),Toast.LENGTH_SHORT).show();
                                }
//                                Toast.makeText(context,baseBeen.getMsg(),Toast.LENGTH_SHORT).show();
                                return;
                            }
//                            if ("0".equals(baseBeen.getError_code())){
//                                listener.onSucceeded(baseBeen.getData());
//                            }else if ("10001".equals(baseBeen.getError_code())){
//                                //跳转登录页面
//                                context.startActivity(new Intent(context, LoginActivity.class));
//                            }else if ("1".equals(baseBeen.getError_code())){
//                                listener.onFailed(baseBeen.getMsg());
//                                Toast.makeText(context,baseBeen.getMsg(),Toast.LENGTH_SHORT).show();
//                            }else {
//                                Toast.makeText(context,baseBeen.getMsg(),Toast.LENGTH_SHORT).show();
//                            }
                        }catch (Exception e){

                        }


                        L.e("qpf","获取数据成功 -- " + response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        L.e("qpf","获取数据失败 -- " + response.getException());
                        if (response != null) {
                            listener.onErr(response.getException().toString());
                        }
                    }});
    }

    public static void postOkGo(final Activity context,boolean isShowLoading, Map<String, Object> map, String url, final ResultListener listener) {

        final LoadingView loading = new LoadingView(context, R.style.CustomDialog);
        loading.show();

//        String userInfoStr = KVUtils.query(SPConstant.USER_BASE_INFO);
//        if (!StringUtil.isBlank(userInfoStr)){
//            UserInfo userInfo = new Gson().fromJson(userInfoStr, UserInfo.class);
//            if (userInfo != null){
//                map.put("token",userInfo.getToken());
//            }
//        }
        String ppp = new Gson().toJson(map);
        L.e("qpf","请求地址 -- " + url + "参数 -- " + ppp);
        params = mapParse(map);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(context)));
        OkGo.getInstance().setOkHttpClient(builder.build());
        OkGo.<String>post(url)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        L.e("qpf","返回结果 -- " + response.body());
                        listener.onSucceeded(response);
//                        try{
//                            BaseBeen baseBeen = new Gson().fromJson(response.body(),BaseBeen.class);
//
//                            if ("0".equals(baseBeen.getError_code())){
//                                listener.onSucceeded(baseBeen.getData());
//                            }else if ("10001".equals(baseBeen.getError_code())){
//                                //跳转登录页面
//                                context.startActivity(new Intent(context, LoginActivity.class));
//                            }else if ("1".equals(baseBeen.getError_code())){
//                                listener.onFailed(baseBeen.getMsg());
//                                Toast.makeText(context,baseBeen.getMsg(),Toast.LENGTH_SHORT).show();
//                            }else {
//                                Toast.makeText(context,baseBeen.getMsg(),Toast.LENGTH_SHORT).show();
//                            }
//                        }catch (Exception e){
//
//                        }


                        L.e("qpf","获取数据成功 -- " + response.body());
                        loading.dismiss();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        L.e("qpf","获取数据失败 -- " + response.getException());
                        if (response != null) {
                            listener.onErr(response.getException().toString());
                        }

                        loading.dismiss();
                    }

                });
    }


    //将map放到params
    public static HttpParams mapParse(Map<String, Object> map) {
        HttpParams params = new HttpParams();
        Iterator<Map.Entry<String, Object>> entryIterator = map.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, Object> entry = entryIterator.next();
            params.put(entry.getKey(), entry.getValue().toString());
        }
        return params;
    }



}
