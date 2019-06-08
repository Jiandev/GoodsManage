package com.example.goodsmanage.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goodsmanage.R;
import com.example.goodsmanage.common.entity.Comment;
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

public class AllCommentRecyclerAdapter extends RecyclerView.Adapter<AllCommentRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<Comment> commentList;

    public AllCommentRecyclerAdapter(Context mContext, List<Comment> commentList) {
        this.mContext = mContext;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public AllCommentRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_comment, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllCommentRecyclerAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tvUsername.setText(commentList.get(i).getGoodsName());
        viewHolder.tvCommentTime.setText(String.valueOf(commentList.get(i).getCommentTime()));
        viewHolder.tvOrderNum.setText("购买数量：" + commentList.get(i).getOrderNum());
        viewHolder.tvComment.setText(commentList.get(i).getComment());
        viewHolder.layoutComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                showEditCommentDialog(position, commentList.get(position).getId());
            }
        });
        viewHolder.layoutComment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = viewHolder.getAdapterPosition();
                showDeleteCommentDialog(position, commentList.get(position).getId());
                return true;
            }
        });
    }

    private void showDeleteCommentDialog(final int position, final int id) {
        new QMUIDialog.MessageDialogBuilder(mContext)
                .setTitle("删除评论")
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
                        final ProgressDialog progressDialog = BaseUtils.showProgressDialog(mContext, "正在删除...");
                        HttpUtils.doGet(BaseUtils.BASE_URL + "/commentDelete?ID=" + id, new Callback() {
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
                                            commentList.get(position).setComment("");
                                            commentList.get(position).setCommentTime("");
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

    private void showEditCommentDialog(final int position, final int id) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(mContext);
        builder.setTitle("修改评论")
                .setPlaceholder("请输入你修改后的评论")
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
                            final ProgressDialog progressDialog = BaseUtils.showProgressDialog(mContext, "正在修改...");
                            HttpUtils.doGet(BaseUtils.BASE_URL + "/commentUpdate?ID=" + id + "&Comment=" + comment, new Callback() {
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
                                                commentList.get(position).setComment(comment.toString());
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                                                String date = dateFormat.format(new Date());
                                                commentList.get(position).setCommentTime(date);
                                                notifyDataSetChanged();
                                                ToastUtil.showMsg(mContext, "修改成功");
                                            }else {
                                                ToastUtil.showMsg(mContext, "修改失败");
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
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        TextView tvCommentTime;
        TextView tvOrderNum;
        TextView tvComment;
        LinearLayout layoutComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutComment = (LinearLayout) itemView;
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvCommentTime = itemView.findViewById(R.id.tv_comment_time);
            tvOrderNum = itemView.findViewById(R.id.tv_order_num);
            tvComment = itemView.findViewById(R.id.tv_comment);
        }
    }
}
