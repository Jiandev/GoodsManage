package com.example.goodsmanage.acitvity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.goodsmanage.R;
import com.example.goodsmanage.common.entity.Goods;
import com.example.goodsmanage.common.utils.BaseUtils;
import com.example.goodsmanage.common.utils.HttpUtils;
import com.google.gson.Gson;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        goodsId = getIntent().getIntExtra(BaseUtils.GOODS_ID, 1);
        mContext = GoodsActivity.this;
        initView();
        BaseUtils.showProgressDialog(mContext, "加载中...");
        HttpUtils.doGet(BaseUtils.BASE_URL + "/getOneGood?ID=" + goodsId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                BaseUtils.closeProgressDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BaseUtils.closeProgressDialog();
                final String responseStr = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseGoodsInfo(responseStr);
                    }
                });
            }
        });
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
    }
}
