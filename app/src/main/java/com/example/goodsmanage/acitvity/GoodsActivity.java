package com.example.goodsmanage.acitvity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.goodsmanage.LoginActivity;
import com.example.goodsmanage.R;
import com.example.goodsmanage.adapter.CommentRecyclerAdapter;
import com.example.goodsmanage.common.entity.Comment;
import com.example.goodsmanage.common.entity.Goods;
import com.example.goodsmanage.common.entity.Result;
import com.example.goodsmanage.common.utils.BaseUtils;
import com.example.goodsmanage.common.utils.HttpUtils;
import com.example.goodsmanage.common.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GoodsActivity extends AppCompatActivity {

    private int goodsId;
    private ImageView imgGoods;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvNum;
    private TextView tvIntro;
    private TextView tvId;
    private Context mContext;
    private Button btnAddCar;

    private RecyclerView recyclerComment;
    private List<Comment> commentList = new ArrayList<>();
    private CommentRecyclerAdapter commentRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        goodsId = getIntent().getIntExtra(BaseUtils.GOODS_ID, 1);
        mContext = GoodsActivity.this;
        initView();
        final ProgressDialog progressDialog = BaseUtils.showProgressDialog(mContext, "加载中...");
        HttpUtils.doGet(BaseUtils.BASE_URL + "/getOneGood?ID=" + goodsId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                BaseUtils.closeProgressDialog(progressDialog);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BaseUtils.closeProgressDialog(progressDialog);
                final String responseStr = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseGoodsInfo(responseStr);
                    }
                });
            }
        });
        initComments();
    }

    private void initComments() {
        HttpUtils.doGet(BaseUtils.BASE_URL + "/searchGoodComment?GoodsID=" + goodsId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr = response.body().string();
                parseCommentList(responseStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerComment.setLayoutManager(layoutManager);
                        commentRecyclerAdapter = new CommentRecyclerAdapter(mContext, commentList);
                        recyclerComment.setAdapter(commentRecyclerAdapter);
                    }
                });
            }
        });
    }

    private void parseCommentList(String responseStr) {
        if (!TextUtils.isEmpty(responseStr)) {
            Gson gson = new Gson();
            commentList = gson.fromJson(responseStr, new TypeToken<ArrayList<Comment>>() {
            }.getType());
        }
    }

    private void parseGoodsInfo(String responseStr) {
        if (!TextUtils.isEmpty(responseStr)) {
            Gson gson = new Gson();
            Goods goods = gson.fromJson(responseStr, Goods.class);
            Glide.with(this)
                    .load(goods.getImgPath())
                    .into(imgGoods);
            tvId.setText(String.valueOf(goods.getId()));
            tvName.setText(goods.getGoodsName());
            tvPrice.setText(goods.getPrice() + "元");
            tvNum.setText(String.valueOf(goods.getGoodsNum()));
            tvIntro.setText(goods.getIntro());
        }
    }

    private void initView() {
        imgGoods = findViewById(R.id.img_goods_big);
        tvId = findViewById(R.id.tv_goods_id_info);
        tvName = findViewById(R.id.tv_goods_name_info);
        tvPrice = findViewById(R.id.tv_goods_price_info);
        tvNum = findViewById(R.id.tv_goods_num_info);
        tvIntro = findViewById(R.id.tv_goods_intro_info);
        recyclerComment = findViewById(R.id.recycler_comment);
        btnAddCar = findViewById(R.id.btn_add_car);
        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = BaseUtils.showProgressDialog(mContext, "正在删除...");
                int userId = BaseUtils.getUserId();
                if (userId <= 0) {
                    ToastUtil.showMsg(mContext, "登录失效，请重新登录");
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
                HttpUtils.doGet(BaseUtils.BASE_URL + "/cartAdd?UserID=" + userId + "&GoodsID=" + goodsId + "&CartNum=1", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        BaseUtils.closeProgressDialog(progressDialog);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showMsg(mContext, "网络错误");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        BaseUtils.closeProgressDialog(progressDialog);
                        final String responseStr = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Result result = BaseUtils.parseResult(responseStr);
                                if (result.getSuccess().equals("1")) {
                                    ToastUtil.showMsg(mContext, "添加购物车成功");
                                } else {
                                    ToastUtil.showMsg(mContext, "添加购物车失败");
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
