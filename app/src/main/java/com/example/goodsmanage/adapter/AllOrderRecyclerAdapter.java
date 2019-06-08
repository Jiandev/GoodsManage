package com.example.goodsmanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.goodsmanage.R;
import com.example.goodsmanage.common.entity.Order;

import java.util.List;

/**
 * Author: Jiandev
 * Modify By:
 * Date: 2019/6/5 10:25
 * Description:
 */
public class AllOrderRecyclerAdapter extends RecyclerView.Adapter<AllOrderRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<Order> orderList;

    public AllOrderRecyclerAdapter(Context mContext, List<Order> orderList) {
        this.mContext = mContext;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public AllOrderRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_order, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllOrderRecyclerAdapter.ViewHolder viewHolder, int i) {
        Order order = orderList.get(i);
        viewHolder.tvUsername.setText(order.getLogin());
        viewHolder.tvName.setText(order.getName());
        viewHolder.tvSex.setText(order.getSex().equals("m") ? "男" : "女");
        viewHolder.tvTel.setText(order.getTel());
        viewHolder.tvAddress.setText(order.getAddress());
        viewHolder.tvGoodsName.setText(order.getGoodsName());
        viewHolder.tvOrderNum.setText(String.valueOf(order.getOrderNum()));
        viewHolder.tvSum.setText(order.getSum() + "元");
        viewHolder.tvOrderTime.setText(order.getOrderTime());
        viewHolder.tvComment.setText(order.getComment());
        viewHolder.tvCommentTime.setText(order.getCommentTime());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvName, tvSex, tvTel, tvAddress;
        TextView tvGoodsName, tvOrderNum, tvSum, tvOrderTime, tvComment, tvCommentTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_order_username);
            tvName = itemView.findViewById(R.id.tv_order_name);
            tvSex = itemView.findViewById(R.id.tv_order_sex);
            tvTel = itemView.findViewById(R.id.tv_order_tel);
            tvAddress = itemView.findViewById(R.id.tv_order_address);
            tvGoodsName = itemView.findViewById(R.id.tv_order_goods_name);
            tvOrderNum = itemView.findViewById(R.id.tv_order_order_num);
            tvSum = itemView.findViewById(R.id.tv_order_sum);
            tvOrderTime = itemView.findViewById(R.id.tv_order_time);
            tvComment = itemView.findViewById(R.id.tv_order_comment);
            tvCommentTime = itemView.findViewById(R.id.tv_order_comment_time);
        }
    }
}
