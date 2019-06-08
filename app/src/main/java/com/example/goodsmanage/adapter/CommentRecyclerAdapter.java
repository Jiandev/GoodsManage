package com.example.goodsmanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.goodsmanage.R;
import com.example.goodsmanage.common.entity.Comment;

import java.util.List;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<Comment> commentList;

    public CommentRecyclerAdapter(Context mContext, List<Comment> commentList) {
        this.mContext = mContext;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_comment, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tvUsername.setText(commentList.get(i).getLogin());
        viewHolder.tvCommentTime.setText(String.valueOf(commentList.get(i).getCommentTime()));
        viewHolder.tvOrderNum.setText("购买数量：" + commentList.get(i).getOrderNum());
        viewHolder.tvComment.setText(commentList.get(i).getComment());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        TextView tvCommentTime;
        TextView tvOrderNum;
        TextView tvComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvCommentTime = itemView.findViewById(R.id.tv_comment_time);
            tvOrderNum = itemView.findViewById(R.id.tv_order_num);
            tvComment = itemView.findViewById(R.id.tv_comment);
        }
    }
}
