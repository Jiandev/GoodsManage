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
import com.example.goodsmanage.adapter.AllCommentRecyclerAdapter;
import com.example.goodsmanage.adapter.AllOrderRecyclerAdapter;
import com.example.goodsmanage.common.entity.Order;
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

public class AllOrderActivity extends AppCompatActivity {

    private RecyclerView recyclerAllOrder;
    private AllOrderRecyclerAdapter adapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_order);
        mContext = AllOrderActivity.this;
        recyclerAllOrder = findViewById(R.id.recycler_all_order);
        initOrder();
    }

    private void initOrder() {
        int userId = BaseUtils.getUserId();
        if (userId <= 0) {
            ToastUtil.showMsg(mContext, "登录失效，请重新登录");
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        final ProgressDialog progressDialog = BaseUtils.showProgressDialog(mContext, "正在加载...");
        HttpUtils.doGet(BaseUtils.BASE_URL + "/getUserOrder?UserID=" + userId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                BaseUtils.closeProgressDialog(progressDialog);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BaseUtils.closeProgressDialog(progressDialog);
                final String responseStr = response.body().string();
                final List<Order> orderList = parseOrderList(responseStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerAllOrder.setLayoutManager(layoutManager);
                        adapter = new AllOrderRecyclerAdapter(mContext, orderList);
                        recyclerAllOrder.setAdapter(adapter);
                    }
                });
            }
        });
    }

    private List<Order> parseOrderList(String responseStr) {
        List<Order> orderList = new ArrayList<>();
        if (!TextUtils.isEmpty(responseStr)) {
            Gson gson = new Gson();
            orderList = gson.fromJson(responseStr, new TypeToken<ArrayList<Order>>(){}.getType());
        }
        return orderList;
    }
}
