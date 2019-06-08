package com.example.goodsmanage.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.goodsmanage.LoginActivity;
import com.example.goodsmanage.R;
import com.example.goodsmanage.acitvity.GoodsActivity;
import com.example.goodsmanage.common.entity.Car;
import com.example.goodsmanage.common.entity.Goods;
import com.example.goodsmanage.common.entity.Result;
import com.example.goodsmanage.common.utils.BaseUtils;
import com.example.goodsmanage.common.utils.HttpUtils;
import com.example.goodsmanage.common.utils.ToastUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<Car> carList;

    public CartRecyclerAdapter(Context mContext, List<Car> carList) {
        this.mContext = mContext;
        this.carList = carList;
    }

    @NonNull
    @Override
    public CartRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_car, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartRecyclerAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tvName.setText(carList.get(i).getGoodsName());
        viewHolder.tvPrice.setText("价格："  + carList.get(i).getPrice() + "元");
        viewHolder.tvNum.setText("添加数量：" + carList.get(i).getCartNum());

        viewHolder.layoutCar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = viewHolder.getAdapterPosition();
                showDeleteCommentDialog(position, carList.get(position).getId());
                return true;
            }
        });
    }

    private void showDeleteCommentDialog(final int position, final int id) {
        new QMUIDialog.MessageDialogBuilder(mContext)
                .setTitle("移除商品")
                .setMessage("确定要移除吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "移除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(final QMUIDialog dialog, int index) {
                        final ProgressDialog progressDialog = BaseUtils.showProgressDialog(mContext, "正在删除...");
                        final Activity activity = (Activity) mContext;
                        int userId = BaseUtils.getUserId();
                        if (userId <= 0) {
                            ToastUtil.showMsg(mContext, "登录失效，请重新登录");
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                            return;
                        }
                        HttpUtils.doGet(BaseUtils.BASE_URL + "/cartDelete?UserID=" + userId + "&GoodsID=" + id, new Callback() {
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
                                Activity activity = (Activity) mContext;
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Result result = BaseUtils.parseResult(responseStr);
                                        if (result.getSuccess().equals("1")) {
                                            carList.remove(position);
                                            notifyDataSetChanged();
                                            ToastUtil.showMsg(mContext, "移除成功");
                                        }else {
                                            ToastUtil.showMsg(mContext, "移除失败");
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
        return carList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvNum, tvPrice;
        LinearLayout layoutCar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutCar = (LinearLayout) itemView;
            tvName = itemView.findViewById(R.id.tv_car_goods_name);
            tvPrice = itemView.findViewById(R.id.tv_car_goods_price);
            tvNum = itemView.findViewById(R.id.tv_car_goods_num);
        }
    }
}
