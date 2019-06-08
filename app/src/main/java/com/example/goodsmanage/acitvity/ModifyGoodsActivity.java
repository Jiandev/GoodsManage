package com.example.goodsmanage.acitvity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.goodsmanage.LoginActivity;
import com.example.goodsmanage.R;
import com.example.goodsmanage.common.entity.Goods;
import com.example.goodsmanage.common.entity.Result;
import com.example.goodsmanage.common.entity.Type;
import com.example.goodsmanage.common.utils.BaseUtils;
import com.example.goodsmanage.common.utils.HttpUtils;
import com.example.goodsmanage.common.utils.ToastUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ModifyGoodsActivity extends AppCompatActivity {

    private EditText editName, editNum, editPrice, editImg, editIntro;
    private Spinner spinnerType;
    private Button btnModify;
    private Context mContext;
    private List<Type> typeList = new ArrayList<>();
    private int goodsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_goods);
        mContext = ModifyGoodsActivity.this;
        initView();
        initSpinner();

        goodsId = getIntent().getIntExtra(BaseUtils.GOODS_ID, 1);

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
    }

    private void parseGoodsInfo(String responseStr) {
        if (!TextUtils.isEmpty(responseStr)) {
            Gson gson = new Gson();
            Goods goods = gson.fromJson(responseStr, Goods.class);
            editName.setText(goods.getGoodsName());
            editImg.setText(goods.getImgPath());
            editIntro.setText(goods.getIntro());
            editNum.setText(String.valueOf(goods.getGoodsNum()));
            editPrice.setText(String.valueOf(goods.getPrice()));
            spinnerType.setSelection(goods.getTypeId());
        }
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
        editName = findViewById(R.id.edit_modify_goods_name);
        editNum = findViewById(R.id.edit_modify_goods_num);
        editPrice = findViewById(R.id.edit_modify_goods_price);
        editImg = findViewById(R.id.edit_modify_goods_img);
        editIntro = findViewById(R.id.edit_modify_goods_intro);
        spinnerType = findViewById(R.id.spinner_modify_goods_type);
        btnModify = findViewById(R.id.btn_modify_goods);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyGoods();
            }
        });
    }

    private void modifyGoods() {
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
        if (BaseUtils.userId <= 0) {
            ToastUtil.showMsg(mContext, "用户信息失效，请重新登录");
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        String extUrl = "?ID="+ goodsId +"&GoodsName=" + name + "&UserID=" + BaseUtils.userId +
                "&TypeID=" + type + "&GoodsNum=" + num + "&Price=" + price +
                "&Img=" + img + "&Intro=" + intro;
        final ProgressDialog progressDialog = BaseUtils.showProgressDialog(mContext, "正在修改...");
        HttpUtils.doGet(BaseUtils.BASE_URL + "/goodsUpdate" + extUrl, new Callback() {
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
                        Result result = BaseUtils.parseResult(responseStr);
                        if (result.getSuccess().equals("1")) {
                            ToastUtil.showMsg(mContext, "修改成功");
                            PublishGoodsActivity.isModify = true;
                            finish();
                        } else {
                            ToastUtil.showMsg(mContext, "修改失败");
                        }
                    }
                });
            }
        });
    }
}
