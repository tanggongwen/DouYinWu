package com.example.qd.douyinwu;

import android.app.Application;

import com.example.qd.douyinwu.utils.UmengShareManager;
import com.example.qd.douyinwu.utils.Utils;
import com.example.qd.douyinwu.xiaozhibo.TCGlobalConfig;
import com.example.qd.douyinwu.xiaozhibo.liteav.demo.lvb.liveroom.MLVBLiveRoomImpl;
import com.example.qd.douyinwu.xiaozhibo.login.TCUserMgr;
import com.qiniu.pili.droid.shortvideo.PLShortVideoEnv;
import com.tencent.rtmp.TXLiveBase;

public class ShortVideoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // init resources needed by short video sdk
        PLShortVideoEnv.init(getApplicationContext());
        // 必须：初始化 LiteAVSDK Licence。 用于直播推流鉴权。
        TXLiveBase.getInstance().setLicence(this, TCGlobalConfig.LICENCE_URL, TCGlobalConfig.LICENCE_KEY);

        // 必须：初始化 MLVB 组件
        MLVBLiveRoomImpl.sharedInstance(this);

        UmengShareManager.INSTANCE.init(getApplicationContext());
        Utils.init(getApplicationContext());

        // 必须：初始化全局的 用户信息管理类，记录个人信息。
        TCUserMgr.getInstance().initContext(getApplicationContext());

    }
}
