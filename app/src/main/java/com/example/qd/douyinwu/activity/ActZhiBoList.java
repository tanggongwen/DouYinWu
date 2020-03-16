package com.example.qd.douyinwu.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.adapter.DetailPageAdapter;
import com.example.qd.douyinwu.been.LiveTypeBeen;
import com.example.qd.douyinwu.intef.ResultListener;
import com.example.qd.douyinwu.utils.BaseOkGoUtils;
import com.example.qd.douyinwu.utils.JsonUtils;
import com.example.qd.douyinwu.utils.L;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.example.qd.douyinwu.constant.HttpConstant.GET_ZHIBO_TYPE;

public class ActZhiBoList extends NativeBaseActivity {

    private TabLayout tabs;
    private ViewPager viewpager;
    private FragmentManager fm;
    private List<LiveTypeBeen> mData;

    @Override
    public int setLayoutResourceID() {
        return R.layout.act_zhibo_list;
    }

    @Override
    public void setUpView() {
        setVisibleTitleBar( "#ffffff","直播");
        initView();
    }

    @Override
    public void setUpData() {
        //请求直播列表分类
        getZhiboListType();
    }

    private void getZhiboListType(){
        Map<String, Object> map = new HashMap<>();
        BaseOkGoUtils.postOkGo(ActZhiBoList.this,map, GET_ZHIBO_TYPE, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    L.e("qpf","直播分类获取成功 -- " + object.toString());
                    mData = JsonUtils.objBeanToList(object, LiveTypeBeen.class);
                    setAdapter();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void setAdapter(){
        //        为viewpager设置适配器
        viewpager.setAdapter(new DetailPageAdapter(ActZhiBoList.this, fm,mData));

        tabs.setupWithViewPager(viewpager);
    }

    private void initView(){
        tabs = findViewById(R.id.sliding_tabs);
        viewpager = findViewById(R.id.viewpager);
        fm = getSupportFragmentManager();
    }

}
