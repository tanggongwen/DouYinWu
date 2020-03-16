package com.example.qd.douyinwu.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.been.LoadMoreBean;

import me.drakeet.multitype.ItemViewBinder;

public class LoadMoreAdapter extends ItemViewBinder<LoadMoreBean,LoadMoreAdapter.ViewHolder> {

    public interface onLoadMore{
        void onLoadMore(int position);
    }

    public  LoadMoreAdapter.onLoadMore onLoadMoreInterface;

    public void setOnLoadMoreInterface(onLoadMore onLoadMoreInterface) {
        this.onLoadMoreInterface = onLoadMoreInterface;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_comment_footer, parent, false);
        LoadMoreAdapter.ViewHolder viewHolder = new LoadMoreAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull LoadMoreBean item) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onLoadMoreInterface != null){
                    onLoadMoreInterface.onLoadMore(holder.getPosition());
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
