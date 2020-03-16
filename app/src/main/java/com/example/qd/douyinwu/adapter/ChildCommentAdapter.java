package com.example.qd.douyinwu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.been.SunComment;

import me.drakeet.multitype.ItemViewBinder;

public class ChildCommentAdapter extends ItemViewBinder<SunComment,ChildCommentAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;

    public interface OnItemClickListener {
        //        void onItemClick(int position, String Url);
        void onItemClick(SunComment item,int position);
    }

    public interface OnCommentLike{
        void commentLike(boolean like,SunComment item,int position);
    }

    public ChildCommentAdapter.OnCommentLike OnCommentLike;

    public void setOnCommentLike(ChildCommentAdapter.OnCommentLike onCommentLike) {
        OnCommentLike = onCommentLike;
    }

    public ChildCommentAdapter.OnItemClickListener mOnItemClickListerer;

    public void setmOnItemClickListerer(ChildCommentAdapter.OnItemClickListener listerer) {
        this.mOnItemClickListerer = listerer;
    }


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.act_video_sun_comment, parent, false);
        ChildCommentAdapter.ViewHolder viewHolder = new ChildCommentAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final SunComment item) {
        Glide.with(context).load(item.getHead_pic()).placeholder(R.drawable.ic_launcher).into(holder.iv_icon);
        holder.tv_name.setText(item.getNick_name());
        if(!TextUtils.isEmpty(item.getTo_nick_name())){
            holder.tv_context.setText("回复 "+item.getTo_nick_name()+":"+
                    item.getC_content() + "  " + item.getC_time());
        }else {
            holder.tv_context.setText(item.getC_content() + "   " + item.getC_time());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListerer != null){
                    mOnItemClickListerer.onItemClick(item,getPosition(holder));
                }
            }
        });
        if(item.getIs_like() == 1){
            Glide.with(context).load(R.drawable.heart3).placeholder(R.drawable.heart3).into(holder.iv_comment_like);
        }else{
            Glide.with(context).load(R.drawable.icon_like_png).placeholder(R.drawable.icon_like_png).into(holder.iv_comment_like);
        }
        holder.tv_comment_like_num.setText(item.getC_like_nums() + "");
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
    public ChildCommentAdapter(Context context) {
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
