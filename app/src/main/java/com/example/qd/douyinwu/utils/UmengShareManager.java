package com.example.qd.douyinwu.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;



import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

public enum UmengShareManager {
    INSTANCE;
    private UMShareAPI shareAPI;
   private String appId = "wx43af10a49bc9501c"; // 填应用AppId

    public void init(Context context){
        UMConfigure.init(context,"5e857ee10cafb24478000192","umeng1",UMConfigure.DEVICE_TYPE_PHONE,"");
        UMConfigure.setLogEnabled(true);
        PlatformConfig.setWeixin(appId,"17024fb3b01d32d9557ae17ccc5cb571");
        shareAPI = UMShareAPI.get(context);
    }

    /**
     * qq登录
     * @param activity
     * @param authListener
     */
    public void loginByQQ(Activity activity, UMAuthListener authListener){
        if (shareAPI.isInstall(activity,SHARE_MEDIA.QQ)){
            shareAPI.getPlatformInfo(activity, SHARE_MEDIA.QQ,authListener);
        }else {
            Toast.makeText(activity.getBaseContext(),"请先安装QQ客户端",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 微信登录
     * @param activity
     * @param authListener
     */
    public void loginByWechat(Activity activity, UMAuthListener authListener){
        if (shareAPI.isInstall(activity,SHARE_MEDIA.WEIXIN)){
            shareAPI.getPlatformInfo(activity,SHARE_MEDIA.WEIXIN,authListener);
        }else {
            Toast.makeText(activity.getBaseContext(),"请先安装微信客户端",Toast.LENGTH_SHORT).show();
        }
    }

    public void Share(Activity activity,String url,String title,String thumb,String des,SHARE_MEDIA shareType){
        UMWeb web = new UMWeb(url);
        UMImage image = new UMImage(activity, thumb);//网络图片
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(des);//描述
        new ShareAction(activity).withMedia(web).setCallback(shareListener).setPlatform(shareType).share();
    }

    public void Share(Activity activity,String url,String title,int thumb,String des,SHARE_MEDIA shareType){
        UMWeb web = new UMWeb(url);
        UMImage image = new UMImage(activity, thumb);//网络图片
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(des);//描述
        new ShareAction(activity).withMedia(web).setCallback(shareListener).setPlatform(shareType).share();
    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Log.d("tanggongwen",throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };


}
