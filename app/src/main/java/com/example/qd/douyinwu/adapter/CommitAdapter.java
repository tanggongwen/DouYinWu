package com.example.qd.douyinwu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.been.MainComment;

import me.drakeet.multitype.ItemViewBinder;

/**
 * author: wu
 * date: on 2018/5/3.
 * describe:评论
 */

public class CommitAdapter extends ItemViewBinder<MainComment,CommitAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;

   
    @Override
    public ViewHolder onCreateViewHolder( LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.item_video_commit, viewGroup, false);
        CommitAdapter.ViewHolder viewHolder = new CommitAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final MainComment item) {
        Glide.with(context).load(item.getHead_pic()).placeholder(R.drawable.ic_launcher).into(holder.iv_icon);
        holder.tv_name.setText(item.getNick_name());
        holder.tv_context.setText(item.getC_content()+"   "+item.getC_time());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListerer != null){
                    mOnItemClickListerer.onItemClick(item);
                }
            }
        });
        if(item.getIs_like() == 1){
            Glide.with(context).load(R.drawable.heart3).placeholder(R.drawable.heart3).into(holder.iv_comment_like);
        }else{
            Glide.with(context).load(R.drawable.icon_like_png).placeholder(R.drawable.icon_like_png).into(holder.iv_comment_like);
        }
        holder.tv_comment_like_num.setText(item.getC_like_nums()+"");
        holder.iv_comment_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OnCommentLike != null){
                    if(item.getIs_like() == 1){//已点赞  再次点击就要取消点赞
                        OnCommentLike.commentLike(false,item,getPosition(holder));
                    }else{//进行点赞
                        OnCommentLike.commentLike(true,item,getPosition(holder));
                    }
                }
            }
        });
    }

    public interface OnItemClickListener {
//        void onItemClick(int position, String Url);
        void onItemClick(MainComment item);
    }
    public interface OnCommentLike{
        void commentLike(boolean like,MainComment item, int position);
    }

    public CommitAdapter.OnItemClickListener mOnItemClickListerer;

    public CommitAdapter.OnCommentLike OnCommentLike;

    public void setOnCommentLike(CommitAdapter.OnCommentLike onCommentLike) {
        OnCommentLike = onCommentLike;
    }

    public void setmOnItemClickListerer(CommitAdapter.OnItemClickListener listerer) {
        this.mOnItemClickListerer = listerer;
    }

    public CommitAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_icon;//头像
        TextView tv_name;//昵称
        TextView tv_context;//内容和时间
        ImageView iv_comment_like;//是否点赞
        TextView tv_comment_like_num;//点赞数目

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_context = itemView.findViewById(R.id.tv_context);
            iv_comment_like = itemView.findViewById(R.id.iv_comment_like);
            tv_comment_like_num = itemView.findViewById(R.id.tv_comment_like_num);
        }
    }
}
