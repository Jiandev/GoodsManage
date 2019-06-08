package com.example.goodsmanage.adapter;

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
import com.example.goodsmanage.R;
import com.example.goodsmanage.acitvity.PublishGoodsActivity;
import com.example.goodsmanage.common.entity.Goods;
import com.example.goodsmanage.common.utils.BaseUtils;

import java.util.List;

/**
 * Author: Jiandev
 * Modify By:
 * Date: 2019/6/5 10:25
 * Description:
 */
public class AllPublishRecyclerAdapter extends RecyclerView.Adapter<AllPublishRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<Goods> goodsList;

    public AllPublishRecyclerAdapter(Context mContext, List<Goods> goodsList) {
        this.mContext = mContext;
        this.goodsList = goodsList;
    }

    @NonNull
    @Override
    public AllPublishRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_goods, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllPublishRecyclerAdapter.ViewHolder viewHolder, int i) {
        Glide.with(mContext)
                .load(goodsList.get(i).getImgPath())
                .into(viewHolder.imgGoods);
        viewHolder.tvName.setText(goodsList.get(i).getGoodsName());
        viewHolder.tvPrice.setText("价格："  + goodsList.get(i).getPrice() + "元");
        viewHolder.tvNum.setText("剩余数量：" + goodsList.get(i).getGoodsNum());
        viewHolder.itemGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Intent intent = new Intent(mContext, PublishGoodsActivity.class);
                intent.putExtra(BaseUtils.GOODS_ID, goodsList.get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGoods;
        TextView tvName;
        TextView tvPrice;
        TextView tvNum;
        LinearLayout itemGoods;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemGoods = (LinearLayout) itemView;
            imgGoods = itemView.findViewById(R.id.img_goods);
            tvName = itemView.findViewById(R.id.tv_goods_name);
            tvPrice = itemView.findViewById(R.id.tv_goods_price);
            tvNum = itemView.findViewById(R.id.tv_goods_num);
        }
    }
}
