package com.example.goodsmanage.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * Author: Jiandev
 * Modify By:
 * Date: 2019/6/5 14:20
 * Description:
 */
public class GoodsListFragment extends Fragment {

    private View rootView;
    private RecyclerView recyclerHome;
    private HomeRecyclerAdapter adapter;
    private List<Goods> goodsList = new ArrayList<>();
    private Context mContext;
    private int typeId;

    public GoodsListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goods_list, container, false);
        mContext = container.getContext();
        typeId = getArguments().getInt("typeId");
        initGoodsList();
        return rootView;
    }

    private void initGoodsList() {
        BaseUtils.showProgressDialog(mContext, "加载中...");
        HttpUtils.doGet(BaseUtils.BASE_URL + "/getAllGoods?TypeID=" + typeId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                BaseUtils.closeProgressDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BaseUtils.closeProgressDialog();
                String responseStr = response.body().string();
                parseGoodsList(responseStr);
                getActivity().runOnUiThread(new Runnable() {
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

    private void fitGoods() {
        for (int i = 0; i < 10; i++) {
            Goods goods = new Goods();
            goods.setImgPath("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/、u=271499053,1629098526&fm=27&gp=0.jpg");
            goods.setGoodsName("英语书");
            goods.setGoodsNum(i + 4);
            goods.setPrice(18.5);
            goodsList.add(goods);
        }
    }

    private void initGoods() {

        recyclerHome = rootView.findViewById(R.id.recycler_home);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerHome.setLayoutManager(linearLayoutManager);
        adapter = new HomeRecyclerAdapter(mContext, goodsList);
        recyclerHome.setAdapter(adapter);
    }
}
