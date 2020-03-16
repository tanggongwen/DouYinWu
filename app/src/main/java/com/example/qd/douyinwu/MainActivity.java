package com.example.qd.douyinwu;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.qd.douyinwu.activity.ActVideoPlay;
import com.example.qd.douyinwu.activity.ActZhiBoList;
import com.example.qd.douyinwu.activity.NativeBaseActivity;
import com.example.qd.douyinwu.xiaozhibo.anchor.TCCameraAnchorActivity;
import com.example.qd.douyinwu.xiaozhibo.anchor.prepare.TCAnchorPrepareActivity;
import com.example.qd.douyinwu.xiaozhibo.common.utils.TCConstants;
import com.example.qd.douyinwu.xiaozhibo.login.TCUserMgr;

public class MainActivity extends NativeBaseActivity{

    private TextView tv_zhibolist;
    private TextView tv_shortvideoplay;
    private TextView tv_zhibotuiliu;//直播推流
    private TextView tv_zhiboplay;//直播播放页

    @Override
    public int setLayoutResourceID() {
        return R.layout.act_main;
    }

    @Override
    public void setUpView() {
        tv_zhibolist = findViewById(R.id.tv_zhibolist);
        tv_shortvideoplay = findViewById(R.id.tv_shortvideoplay);
        tv_zhibolist.setOnClickListener(this);
        tv_shortvideoplay.setOnClickListener(this);
        tv_zhibotuiliu = findViewById(R.id.tv_zhibotuiliu);
        tv_zhibotuiliu.setOnClickListener(this);
        tv_zhiboplay = findViewById(R.id.tv_zhiboplay);
        tv_zhiboplay.setOnClickListener(this);
    }

    @Override
    public void setUpData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_zhibolist:
                startActivity(new Intent(MainActivity.this, ActZhiBoList.class));
                break;
            case R.id.tv_shortvideoplay:
                startActivity(new Intent(MainActivity.this, ActVideoPlay.class));
                break;
            case R.id.tv_zhibotuiliu:
//                startPublish();
                startActivity(new Intent(MainActivity.this, TCAnchorPrepareActivity.class));
                break;
            case R.id.tv_zhiboplay:

                break;
        }
    }


    /**
     * 发起推流
     *
     */
    private void startPublish() {
        Intent intent = null;
//        if (mRecordType == TCConstants.RECORD_TYPE_SCREEN) {
//            //录屏
//            intent = new Intent(this, TCScreenAnchorActivity.class);
//        } else {
            intent = new Intent(this, TCCameraAnchorActivity.class);
//        }

        if (intent != null) {
            intent.putExtra(TCConstants.ROOM_TITLE,
                    TextUtils.isEmpty("房间111") ? TCUserMgr.getInstance().getNickname() : "房间111");
            intent.putExtra(TCConstants.USER_ID, TCUserMgr.getInstance().getUserId());
            intent.putExtra(TCConstants.USER_NICK, TCUserMgr.getInstance().getNickname());
            intent.putExtra(TCConstants.USER_HEADPIC, TCUserMgr.getInstance().getAvatar());
            intent.putExtra(TCConstants.COVER_PIC, TCUserMgr.getInstance().getCoverPic());
            intent.putExtra(TCConstants.USER_LOC,"");
//                    mTvLocation.getText().toString().equals(getString(R.string.text_live_lbs_fail)) ||
//                            mTvLocation.getText().toString().equals(getString(R.string.text_live_location)) ?
//                            getString(R.string.text_live_close_lbs) : mTvLocation.getText().toString());
            startActivity(intent);
            finish();
        }
    }
}


