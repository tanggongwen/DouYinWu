package com.example.qd.douyinwu;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qd.douyinwu.adapter.TabFragmentPagerAdapter;
import com.example.qd.douyinwu.fragment.ActVideoPlayFragment;
import com.example.qd.douyinwu.activity.ActZhiBoList;
import com.example.qd.douyinwu.activity.NativeBaseActivity;
import com.example.qd.douyinwu.fragment.LiveFragment;
import com.example.qd.douyinwu.fragment.MarketFragment;
import com.example.qd.douyinwu.fragment.MineFragment;
import com.example.qd.douyinwu.xiaozhibo.anchor.TCCameraAnchorActivity;
import com.example.qd.douyinwu.xiaozhibo.anchor.prepare.TCAnchorPrepareActivity;
import com.example.qd.douyinwu.xiaozhibo.common.utils.TCConstants;
import com.example.qd.douyinwu.xiaozhibo.login.TCUserMgr;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends NativeBaseActivity implements MarketFragment.MyWebViewListener {

    private TextView tvHome;
    private TextView tvLive;
    private TextView tvMarket;//直播推流
    private TextView tvMine;//直播播放页
    private ViewPager viewPager;
    private TabFragmentPagerAdapter adapter;
    private List<Fragment> list;
    private ActVideoPlayFragment actVideoPlayFragment = new ActVideoPlayFragment();
    private long mExitTime;
    private MineFragment mineFragment = new MineFragment();
    private WebView webView;

    @Override
    public int setLayoutResourceID() {
        return R.layout.act_main;
    }

    @Override
    public void setUpView() {
        tvHome = findViewById(R.id.tvHome);
        tvLive = findViewById(R.id.tvLive);
        tvMarket = findViewById(R.id.tvMarket);
        tvMine = findViewById(R.id.tvMine);
        viewPager = (ViewPager) findViewById(R.id.myViewPager);
        //绑定点击事件
        //把Fragment添加到List集合里面
        list = new ArrayList<>();
        list.add(actVideoPlayFragment);
        list.add(new LiveFragment());
        list.add(new MarketFragment());
        list.add(mineFragment);
        tvHome.setOnClickListener(this);
        tvMine.setOnClickListener(this);
        tvMarket.setOnClickListener(this);
        tvLive.setOnClickListener(this);
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i==0){
                    actVideoPlayFragment.resume();
                }else {
                    actVideoPlayFragment.pause();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(0);  //初始化显示第一个页面

    }

    @Override
    public void setUpData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvHome:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tvLive:
                viewPager.setCurrentItem(1);
                actVideoPlayFragment.pause();
                break;
            case R.id.tvMarket:
                viewPager.setCurrentItem(2);
                actVideoPlayFragment.pause();
                break;
            case R.id.tvMine:
                viewPager.setCurrentItem(3);
                actVideoPlayFragment.pause();
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null!=webView&&webView.canGoBack()){
                webView.goBack();
            }else {
                //与上次点击返回键时刻作差
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    //大于2000ms则认为是误操作，使用Toast进行提示
                    Toast.makeText(this,"再按一次退出应用",Toast.LENGTH_SHORT).show();
                    //并记录下本次点击“返回键”的时刻，以便下次进行判断
                    mExitTime = System.currentTimeMillis();
                } else {
                    //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                    finish();
//                System.exit(0);
                }
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mineFragment.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onWebViewBack(WebView webView) {
        this.webView = webView;
    }
}


