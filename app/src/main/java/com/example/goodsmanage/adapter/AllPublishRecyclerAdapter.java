package com.example.goodsmanage.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.example.goodsmanage.LoginActivity;
import com.example.goodsmanage.R;
import com.example.goodsmanage.acitvity.PublishGoodsActivity;
import com.example.goodsmanage.common.entity.Goods;
import com.example.goodsmanage.common.entity.Result;
import com.example.goodsmanage.common.utils.BaseUtils;
import com.example.goodsmanage.common.utils.HttpUtils;
import com.example.goodsmanage.common.utils.ToastUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
    public void onBindViewHolder(@NonNull final AllPublishRecyclerAdapter.ViewHolder viewHolder, final int i) {
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
        viewHolder.itemGoods.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = viewHolder.getAdapterPosition();
                showDeleteCommentDialog(position, goodsList.get(i).getId());
                return true;
            }
        });
    }

    private void showDeleteCommentDialog(final int position, final int goodsId) {
        new QMUIDialog.MessageDialogBuilder(mContext)
                .setTitle("删除商品")
                .setMessage("确定要删除吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(final QMUIDialog dialog, int index) {
                        final Activity activity = (Activity) mContext;
                        final ProgressDialog progressDialog = BaseUtils.showProgressDialog(mContext, "正在删除...");
                        int userId = BaseUtils.getUserId();
                        if (userId <= 0) {
                            ToastUtil.showMsg(mContext, "登录失效，请重新登录");
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                            return;
                        }
                        HttpUtils.doGet(BaseUtils.BASE_URL + "/goodsDelete?ID=" + goodsId + "&UserID=" + userId, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                BaseUtils.closeProgressDialog(progressDialog);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showMsg(mContext, "网络错误");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                BaseUtils.closeProgressDialog(progressDialog);
                                dialog.dismiss();
                                final String responseStr = response.body().string();
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Result result = BaseUtils.parseResult(responseStr);
                                        if (result.getSuccess().equals("1")) {
                                            goodsList.remove(position);
                                            notifyDataSetChanged();
                                            ToastUtil.showMsg(mContext, "删除成功");
                                        }else {
                                            ToastUtil.showMsg(mContext, "删除失败");
                                        }
                                    }
                                });
                            }
                        });
                    }
                })
                .create().show();
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
