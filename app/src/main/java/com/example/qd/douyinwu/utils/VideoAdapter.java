package com.example.qd.douyinwu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.activity.WebActivity;
import com.example.qd.douyinwu.been.ShortVideo;
import com.qiniu.android.utils.StringUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private Activity mContext;
    private List<ShortVideo> mDatas;

    //为RecyclerView的Item添加监听
    public interface OnItemClickListener {
        void onItemClick(int position, String type, View view, View view1, View view2);
    }

    public interface OnSetLikeListener{
        void isSetLike(boolean isLike);
        void guanzhu();
    }


    public VideoAdapter.OnItemClickListener mOnItemClickListerer;

    public VideoAdapter.OnSetLikeListener onsetlikelistener;

    public void setOnsetlikelistener(OnSetLikeListener onsetlikelistener) {
        this.onsetlikelistener = onsetlikelistener;
    }

    public void setOnItemClickListerer(VideoAdapter.OnItemClickListener listerer) {
        this.mOnItemClickListerer = listerer;
    }

    public VideoAdapter(Activity context, List<ShortVideo> datas) {
        mContext = context;
        mDatas = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_play_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.jzVideo.setUp(mDatas.get(position).getS_video_url(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
        //隐藏全屏按钮、返回按钮
        holder.jzVideo.fullscreenButton.setVisibility(View.GONE);
        holder.jzVideo.backButton.setVisibility(View.GONE);
        //返回
        holder.ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListerer.onItemClick(position, "back", view, view, view);
            }
        });
        //评论
        holder.iv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListerer.onItemClick(position, "commit", view, view, view);
            }
        });
        holder.tv_context.setText(mDatas.get(position).getS_title());//标题
        holder.tv_like.setText(mDatas.get(position).getS_like_num());//点赞数量
        holder.tv_comment_num.setText(mDatas.get(position).getS_comments_num());//评论数量
        holder.tvGoodsName.setText(mDatas.get(position).getGoods_name());
        holder.tvShareCount.setText(mDatas.get(position).getS_share());
        Glide.with(mContext).load(mDatas.get(position).getOriginal_img1()).placeholder(R.drawable.ic_launcher).into(holder.imgGoodsCover);
        Glide.with(mContext).load(mDatas.get(position).getHead_pic()).placeholder(R.drawable.ic_launcher).into(holder.iv_icon);
        holder.tv_name.setText("@"+mDatas.get(position).getNick_name());
        if (!StringUtils.isNullOrEmpty(mDatas.get(position).getMarket_price())){

            holder.tvGoodsPrice.setText("¥"+mDatas.get(position).getMarket_price());
        }else {
            holder.tvGoodsPrice.setText("暂无售价");
        }
        if(mDatas.get(position).getIs_like_video() == 0){
            Glide.with(mContext).load(R.drawable.icon_video_heart).into(holder.iv_heart);
        }else{
            Glide.with(mContext).load(R.drawable.heart3).into(holder.iv_heart);
        }
        if(mDatas.get(position).getIs_like_host() == 0){
            holder.iv_guanzhu.setVisibility(View.VISIBLE);
        }else{
            holder.iv_guanzhu.setVisibility(View.GONE);
        }
        holder.iv_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDatas.get(position).getIs_like_video() == 0){//点赞
                    onsetlikelistener.isSetLike(false);
                }else{//取消点赞
                    onsetlikelistener.isSetLike(true);
                }
            }
        });
        holder.iv_guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onsetlikelistener.guanzhu();
            }
        });
        if (mDatas.get(position).getGoods_id().equals("0")){
            holder.llytGoodsDetail.setVisibility(View.GONE);
        }else {
            holder.llytGoodsDetail.setVisibility(View.VISIBLE);
        }
        holder.llytGoodsDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("url","http://slive.sdyilian.top/View/Goods/goods_detail?user_id="+PersonInfoManager.INSTANCE.getUserId()+"&goods_id="+mDatas.get(position).getGoods_id()+"&from=1");
                mContext.startActivity(intent);
            }
        });
        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengShareManager.INSTANCE.Share(mContext,"http://www.baidu.com","尚直播",R.drawable.ic_launch,"分享内容", SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public JZVideoPlayerStandard jzVideo;
        LinearLayout ll_back;
        ImageView iv_commit;
        TextView tv_context;//标题
        TextView tv_like;//点赞数量
        TextView tv_comment_num;//评论数量
        TextView tvShareCount;
        ImageView iv_icon;//头像
        ImageView imgShare;
        TextView tv_name;//昵称
        ImageView iv_heart;//点赞
        ImageView iv_guanzhu;//关注
        ImageView imgGoodsCover;
        TextView tvGoodsName;
        TextView tvGoodsPrice;
        LinearLayout llytGoodsDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            tvShareCount = itemView.findViewById(R.id.tvShareCount);
            ll_back = itemView.findViewById(R.id.ll_back);
            iv_commit = itemView.findViewById(R.id.iv_commit);
            jzVideo = itemView.findViewById(R.id.jzVideo);
            tv_context = itemView.findViewById(R.id.tv_context);
            tv_like = itemView.findViewById(R.id.tv_like);
            tv_comment_num = itemView.findViewById(R.id.tv_comment_num);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_name = itemView.findViewById(R.id.tv_name);
            iv_heart = itemView.findViewById(R.id.iv_heart);
            iv_guanzhu = itemView.findViewById(R.id.iv_guanzhu);
            imgGoodsCover = itemView.findViewById(R.id.imgGoodCover);
            tvGoodsName = itemView.findViewById(R.id.tvGoodsName);
            tvGoodsPrice = itemView.findViewById(R.id.tvGoodsPrice);
            llytGoodsDetail = itemView.findViewById(R.id.llytGoodsDetail);
            imgShare = itemView.findViewById(R.id.iv_share);
        }
    }
}
