package com.example.qd.douyinwu.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    private ItemClick itemClick;

    public GoodsListAdapter(Context context,List<ChooseGoodsBean> chooseGoodsBeanList,ItemClick itemClick) {

        super(R.layout.layout_goods_items,chooseGoodsBeanList);
        this.context = context;
        this.itemClick = itemClick;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ChooseGoodsBean item) {
        helper.setText(R.id.tvGoodsName,item.getGoods_name());
        ImageView iv_head = helper.getView(R.id.goodCover);
        ImageView imgTag = helper.getView(R.id.imgTag);
        LinearLayout llytItem = helper.getView(R.id.llytGoods);
        if (item.isSeleted()){
            imgTag.setVisibility(View.VISIBLE);
        }else {
            imgTag.setVisibility(View.GONE);
        }
        Glide.with(context).load(item.getOriginal_img1()).placeholder(R.drawable.ic_launcher).into(iv_head);
        llytItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onClickItem(item);
            }
        });
    }


    public interface ItemClick{
        void onClickItem(ChooseGoodsBean chooseGoodsBean);
    }

    public String getGoodsId() {
        return goodsId;
    }




}
