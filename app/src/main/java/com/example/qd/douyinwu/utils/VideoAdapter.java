package com.example.qd.douyinwu.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qd.douyinwu.activity.ActVideoPlay;
import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.been.ShortVideo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.JZVideoPlayerStandard;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private ActVideoPlay mContext;
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

    public VideoAdapter(ActVideoPlay context, List<ShortVideo> datas) {
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
        Glide.with(mContext).load(mDatas.get(position).getHead_pic()).placeholder(R.drawable.ic_launcher).into(holder.iv_icon);
        holder.tv_name.setText("@"+mDatas.get(position).getNick_name());
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
        ImageView iv_icon;//头像
        TextView tv_name;//昵称
        ImageView iv_heart;//点赞
        ImageView iv_guanzhu;//关注

        public ViewHolder(View itemView) {
            super(itemView);
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
        }
    }
}
