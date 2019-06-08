package com.example.goodsmanage.acitvity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.example.goodsmanage.LoginActivity;
import com.example.goodsmanage.R;
import com.example.goodsmanage.adapter.AllPublishRecyclerAdapter;
import com.example.goodsmanage.adapter.HomeRecyclerAdapter;
import com.example.goodsmanage.common.entity.Goods;
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

public class AllPublishActivity extends AppCompatActivity {
    private RecyclerView recyclerGoods;

    private Context mContext;
    private List<Goods> goodsList = new ArrayList<>();
    private AllPublishRecyclerAdapter adapter;
    private boolean isSection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);
        mContext = AllPublishActivity.this;
        int userId = BaseUtils.getUserId();
        if (userId <= 0) {
            ToastUtil.showMsg(mContext, "登录失效，请重新登录");
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        initGoodsList(BaseUtils.BASE_URL + "/getUserGoods?UserID=" + userId);
    }

    private void initGoodsList(String url) {
        final ProgressDialog progressDialog = BaseUtils.showProgressDialog(mContext, "正在加载...");
        HttpUtils.doGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                BaseUtils.closeProgressDialog(progressDialog);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BaseUtils.closeProgressDialog(progressDialog);
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
            goodsList = gson.fromJson(responseStr, new TypeToken<ArrayList<Goods>>() {
            }.getType());
        }
    }

    private void initGoods() {

        recyclerGoods = findViewById(R.id.recycler_goods);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerGoods.setLayoutManager(linearLayoutManager);
        adapter = new AllPublishRecyclerAdapter(mContext, goodsList);
        recyclerGoods.setAdapter(adapter);
    }
}
