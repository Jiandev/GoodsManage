package com.example.goodsmanage.acitvity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.goodsmanage.R;
import com.example.goodsmanage.common.entity.Result;
import com.example.goodsmanage.common.entity.Type;
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

public class AddGoodsActivity extends AppCompatActivity {

    private EditText editName, editNum, editPrice, editImg, editIntro;
    private Spinner spinnerType;
    private Button btnAdd;
    private Context mContext;
    private List<Type> typeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);
        mContext = AddGoodsActivity.this;
        initView();
        initSpinner();
    }

    private void initSpinner() {

        HttpUtils.doGet(BaseUtils.BASE_URL + "/getAllGoodsType", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showMsg(mContext, "加载商品类型失败");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initType();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseStr = response.body().string();
                typeList = BaseUtils.parseType(responseStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initType();
                    }
                });
            }
        });
    }

    private void initType() {
        String[] typeStrs = new String[typeList.size() + 1];
        typeStrs[0] = "请选择商品类型";
        for (int i = 0; i < typeList.size(); i++) {
            typeStrs[i + 1] = typeList.get(i).getType();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, typeStrs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
    }

    private void initView() {
        editName = findViewById(R.id.edit_goods_name);
        editNum = findViewById(R.id.edit_goods_num);
        editPrice = findViewById(R.id.edit_goods_price);
        editImg = findViewById(R.id.edit_goods_img);
        editIntro = findViewById(R.id.edit_goods_intro);
        spinnerType = findViewById(R.id.spinner_goods_type);
        btnAdd = findViewById(R.id.btn_add_goods);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGoods();
            }
        });
    }

    private void addGoods() {
        String name = editName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showMsg(mContext, "请输入商品名称");
            return;
        }
        int type = (int) spinnerType.getSelectedItemId();
        if (type <= 0) {
            ToastUtil.showMsg(mContext, "请选择商品类型");
            return;
        }
        int num = Integer.parseInt(editNum.getText().toString().isEmpty() ? "-1" : editNum.getText().toString());
        if (num <= 0) {
            ToastUtil.showMsg(mContext, "请输入商品数量");
            return;
        }
        double price = Double.parseDouble(editPrice.getText().toString().isEmpty() ? "-1" : editPrice.getText().toString());
        if (price <= 0) {
            ToastUtil.showMsg(mContext, "请输入商品价格");
            return;
        }
        String img = editImg.getText().toString();
        if (TextUtils.isEmpty(img)) {
            ToastUtil.showMsg(mContext, "请输入商品图片地址");
            return;
        }
        String intro = editIntro.getText().toString();
        if (TextUtils.isEmpty(intro)) {
            ToastUtil.showMsg(mContext, "请输入商品介绍");
            return;
        }

        String extUrl = "?GoodsName=" + name + "&UserID=" + BaseUtils.userId +
                "&TypeID=" + type + "&GoodsNum=" + num + "&Price=" + price +
                "&Img=" + img + "&Intro=" + intro;
        BaseUtils.showProgressDialog(mContext, "正在发布...");
        HttpUtils.doGet(BaseUtils.BASE_URL + "/goodsIssue" + extUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                BaseUtils.closeProgressDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BaseUtils.closeProgressDialog();
                String responseStr = response.body().string();
                parseResult(responseStr);
            }
        });
    }

    private void parseResult(String responseStr) {
        if (!TextUtils.isEmpty(responseStr)) {
            Gson gson = new Gson();
            Result result = gson.fromJson(responseStr, Result.class);
            if (result.getSuccess().equals("1")) {
                ToastUtil.showMsg(mContext, "发布成功");
                finish();
            } else {
                ToastUtil.showMsg(mContext, "发布失败");
            }
        }
    }
}
