package com.example.qd.douyinwu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.activity.ActZhiBoList;
import com.example.qd.douyinwu.adapter.DetailPageAdapter;
import com.example.qd.douyinwu.been.LiveTypeBeen;
import com.example.qd.douyinwu.intef.ResultListener;
import com.example.qd.douyinwu.utils.BaseOkGoUtils;
import com.example.qd.douyinwu.utils.JsonUtils;
import com.example.qd.douyinwu.utils.L;
import com.example.qd.douyinwu.utils.PersonInfoManager;
import com.example.qd.douyinwu.utils.ToastUtils;
import com.example.qd.douyinwu.utils.Utils;
import com.example.qd.douyinwu.xiaozhibo.anchor.prepare.TCAnchorPrepareActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.qd.douyinwu.constant.HttpConstant.GET_ZHIBO_TYPE;

public class LiveFragment extends Fragment {
    private View rootView;
    private TextView tvStartLive;
    private TabLayout tabs;
    private ViewPager viewpager;
    private FragmentManager fm;
    private List<LiveTypeBeen> mData;
    private DetailPageAdapter detailPageAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView ==null){
            rootView=inflater .inflate(R.layout.act_zhibo_list  ,container,false) ;
        }
        initView();
        getZhiboListType();
        return rootView;
    }



    private void initView(){
        tabs = rootView.findViewById(R.id.sliding_tabs);
        viewpager = rootView.findViewById(R.id.viewpager);
        tvStartLive = rootView.findViewById(R.id.startLive);
        tvStartLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null==PersonInfoManager.INSTANCE.getUserBean()){
                    ToastUtils.s(Utils.getContext(),"请先登录账号");
                }else {
                    startActivity(new Intent(getActivity(), TCAnchorPrepareActivity.class));
                }

            }
        });
        fm = getChildFragmentManager();
    }

    private void setAdapter(){
        //        为viewpager设置适配器
        detailPageAdapter = new DetailPageAdapter(getContext(),fm,mData);
        viewpager.setAdapter(detailPageAdapter);

        tabs.setupWithViewPager(viewpager);
    }


    private void getZhiboListType(){
        Map<String, Object> map = new HashMap<>();
        BaseOkGoUtils.postOkGo(getContext(),map, GET_ZHIBO_TYPE, new ResultListener() {
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
//          getZhiboListType();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
