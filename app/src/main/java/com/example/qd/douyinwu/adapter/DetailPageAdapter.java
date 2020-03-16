package com.example.qd.douyinwu.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.qd.douyinwu.been.LiveTypeBeen;
import com.example.qd.douyinwu.fragment.HomeDetailInfoFragment;

import java.util.List;

public class DetailPageAdapter extends FragmentPagerAdapter {
    private static int PAGE_COUNT;//表示要展示的页面数量
    private Context mContext;
    private List<LiveTypeBeen> list;

    public DetailPageAdapter(Context context, FragmentManager fm, List<LiveTypeBeen> list) {
        super(fm);
        this.mContext = context;
        this.list = list;
        PAGE_COUNT=list.size();

    }

    @Override
    public Fragment getItem(int position) {
        return HomeDetailInfoFragment.newInstance(list.get(position).getT_id());
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {//设置标题
//        switch (position) {
//            case 0:
//                return "页面一";
//            case 1:
//                return "页面二";
//            case 2:
//                return "页面三";
//            case 3:
//                return "页面四";
//            default:break;
//
//        }
        return list.get(position).getT_name();
    }
}
