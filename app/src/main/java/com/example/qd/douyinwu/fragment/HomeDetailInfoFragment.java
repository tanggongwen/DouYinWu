package com.example.qd.douyinwu.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.adapter.MainLiveListAdapter;
import com.example.qd.douyinwu.been.LiveData;
import com.example.qd.douyinwu.intef.ResultListener;
import com.example.qd.douyinwu.utils.BaseOkGoUtils;
import com.example.qd.douyinwu.utils.JsonUtils;
import com.example.qd.douyinwu.utils.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.qd.douyinwu.constant.HttpConstant.GET_ZHIBO_LITS;

public class HomeDetailInfoFragment extends NativeBaseFragment {

    private static final String ARG_PARAM = "param";
    private String mParam;//用来表示当前需要展示的是哪一页  id
    private RecyclerView recylerView;//直播列表
    private int PAGE = 1;
    private List<LiveData> list = new ArrayList<>();
    private MainLiveListAdapter adapter;
    private boolean isFirst = true;

    public static HomeDetailInfoFragment newInstance(String param) {
        HomeDetailInfoFragment fragment = new HomeDetailInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.home_detail_frag;
    }

    @Override
    protected void setUpView() {
        recylerView = getContentView().findViewById(R.id.recylerView);
        recylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MainLiveListAdapter(list,getActivity());
        recylerView.setAdapter(adapter); // 布局用 main_zhibo_item
    }

    @Override
    protected void setUpData() {
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }
//        //根据mParam来判断当前展示的是哪一页，根据页数的不同展示不同的信息
//        switch (mParam){
//            case 0:
//                detail_text.setText("内容1");
//                break;
//            case 1:
//                detail_text.setText("内容2");
//                break;
//            case 2:
//                detail_text.setText("内容3");
//                break;
//            case 3:
//                detail_text.setText("内容4");
//                break;
//            default:break;
//        }
        getMyZhiboList();
    }


    public void getMyZhiboList(){
        Map<String, Object> map = new HashMap<>();
        map.put("t_id",mParam);
        map.put("page",PAGE);
        map.put("num",10);
        BaseOkGoUtils.postOkGo(getActivity(),map, GET_ZHIBO_LITS, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    L.e("qpf","直播列表获取成功 -- " + object.toString());
                    list = JsonUtils.objBeanToList(object, LiveData.class);
                    if(PAGE == 1){
                        adapter.setNewData(list);
                    }else{
                        adapter.addData(list);
                        adapter.notifyDataSetChanged();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            if (isFirst){
                isFirst = false;
                return;
            }
            getMyZhiboList();
        }
    }
}
