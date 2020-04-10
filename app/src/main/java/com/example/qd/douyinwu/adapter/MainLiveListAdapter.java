package com.example.qd.douyinwu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.been.LiveData;
import com.example.qd.douyinwu.utils.ToastUtils;
import com.example.qd.douyinwu.xiaozhibo.audience.TCAudienceActivity;
import com.example.qd.douyinwu.xiaozhibo.common.utils.TCConstants;

import java.util.List;

public class MainLiveListAdapter extends BaseQuickAdapter<LiveData, BaseViewHolder> {

    private Context context;

    public MainLiveListAdapter(@Nullable List<LiveData> data, Context context) {
        super(R.layout.main_zhibo_item, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final LiveData item) {
        helper.setText(R.id.tv_name,item.getNick_name());
        helper.setText(R.id.tv_title,item.getTitle());
        ImageView iv_head = helper.getView(R.id.iv_head);
        Glide.with(context).load(item.getHead_pic()).placeholder(R.drawable.ic_launcher).into(iv_head);
        ImageView iv_img = helper.getView(R.id.iv_img);//封面
        Glide.with(context).load(item.getCover()).placeholder(R.drawable.ic_launcher).into(iv_img);
        iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getStatus().equals("0")){
                    ToastUtils.s(context.getApplicationContext(),"直播未开始");
                }else {
                    Intent intent = new Intent(context, TCAudienceActivity.class);
                    intent.putExtra(TCConstants.PLAY_URL, item.getPlay_url());//房间号
                    intent.putExtra(TCConstants.GROUP_ID,item.getRoom());
                    intent.putExtra(TCConstants.PUSHER_AVATAR,item.getHead_pic());
                    intent.putExtra(TCConstants.PUSHER_NAME,item.getNick_name());
                    intent.putExtra(TCConstants.KEYROOM_ID,item.getId());
                    intent.putExtra(TCConstants.COVER_PIC,item.getCover());
                    intent.putExtra(TCConstants.GOODS_NAME,item.getGoods_name());
                    intent.putExtra(TCConstants.GOODS_PRICE,item.getMarket_price());
                    intent.putExtra(TCConstants.GOODS_ID,item.getGoods_id());
                    intent.putExtra(TCConstants.GOODS_PIC,item.getOriginal_img1());
                    intent.putExtra(TCConstants.PEOPLE_NUM,item.getPeoplenum());
                    context.startActivity(intent);
                }

            }
        });
    }
}
