package com.example.goodsmanage.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.goodsmanage.R;
import com.example.goodsmanage.common.entity.Order;
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
        viewHolder.btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                showEditCommentDialog(position, orderList.get(position).getId());
            }
        });
    }

    private void showEditCommentDialog(final int position, final int id) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(mContext);
        builder.setTitle("添加评论")
                .setPlaceholder("请输入你要添加的评论")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(final QMUIDialog dialog, int index) {
                        final CharSequence comment = builder.getEditText().getText();
                        if (comment != null && comment.length() > 0) {
                            final ProgressDialog progressDialog = BaseUtils.showProgressDialog(mContext, "正在添加...");
                            HttpUtils.doGet(BaseUtils.BASE_URL + "/commentAdd?ID=" + id + "&Comment=" + comment, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    BaseUtils.closeProgressDialog(progressDialog);
                                    Activity activity = (Activity) mContext;
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
                                                orderList.get(position).setComment(comment.toString());
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                                                String date = dateFormat.format(new Date());
                                                orderList.get(position).setCommentTime(date);
                                                notifyDataSetChanged();
                                                ToastUtil.showMsg(mContext, "添加成功");
                                            }else {
                                                ToastUtil.showMsg(mContext, "添加失败");
                                            }
                                        }
                                    });
                                }
                            });
                        } else {
                            ToastUtil.showMsg(mContext, "请输入修改后的评论");
                        }
                    }
                })
                .create().show();
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvName, tvSex, tvTel, tvAddress;
        TextView tvGoodsName, tvOrderNum, tvSum, tvOrderTime, tvComment, tvCommentTime;
        Button btnAddComment;

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
            btnAddComment = itemView.findViewById(R.id.btn_add_comment);
        }
    }
}
