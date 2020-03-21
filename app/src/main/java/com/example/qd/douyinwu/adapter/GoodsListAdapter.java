package com.example.qd.douyinwu.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.been.ChooseGoodsBean;
import com.example.qd.douyinwu.been.GoodsBean;

import java.util.ArrayList;
import java.util.List;

public class GoodsListAdapter extends BaseQuickAdapter<ChooseGoodsBean, BaseViewHolder> {

    private Context context;
    private String goodsId;

    public GoodsListAdapter(Context context,List<ChooseGoodsBean> chooseGoodsBeanList) {

        super(R.layout.layout_goods_items,chooseGoodsBeanList);
        this.context = context;

    }

    @Override
    protected void convert(BaseViewHolder helper,ChooseGoodsBean item) {
        helper.setText(R.id.tvGoodsName,item.getGoods_name());
        ImageView iv_head = helper.getView(R.id.goodCover);
        Glide.with(context).load(item.getOriginal_img1()).placeholder(R.drawable.ic_launcher).into(iv_head);
    }


    public String getGoodsId() {
        return goodsId;
    }




}
