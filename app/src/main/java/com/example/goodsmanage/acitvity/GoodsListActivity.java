package com.example.goodsmanage.acitvity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.example.goodsmanage.R;
import com.example.goodsmanage.adapter.HomeRecyclerAdapter;
import com.example.goodsmanage.common.entity.Goods;
import com.example.goodsmanage.common.utils.BaseUtils;
import com.example.goodsmanage.common.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GoodsListActivity extends AppCompatActivity {

    private RecyclerView recyclerGoods;

    private String search;
    private Context mContext;
    private List<Goods> goodsList = new ArrayList<>();
    private HomeRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);
        mContext = GoodsListActivity.this;

        search = getIntent().getStringExtra("search");
        initGoodsList();
    }

    private void initGoodsList() {
        BaseUtils.showProgressDialog(mContext, "正在搜索...");
        HttpUtils.doGet(BaseUtils.BASE_URL + "/searchGoods?search=" + search, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                BaseUtils.closeProgressDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BaseUtils.closeProgressDialog();
                String responseStr = response.body().string();
                parseGoodsList(responseStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initGoods();
                    }
                });
            }
        });
    }

    private void parseGoodsList(String responseStr) {
        if (!TextUtils.isEmpty(responseStr)) {
            Gson gson = new Gson();
            goodsList = gson.fromJson(responseStr, new TypeToken<ArrayList<Goods>>(){}.getType());
        }
    }

    private void initGoods() {

        recyclerGoods = findViewById(R.id.recycler_goods);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerGoods.setLayoutManager(linearLayoutManager);
        adapter = new HomeRecyclerAdapter(mContext, goodsList);
        recyclerGoods.setAdapter(adapter);
    }
}
