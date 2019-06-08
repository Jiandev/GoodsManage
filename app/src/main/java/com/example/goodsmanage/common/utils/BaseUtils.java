package com.example.goodsmanage.common.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.example.goodsmanage.common.entity.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Jiandev
 * Modify By:
 * Date: 2019/6/5 11:51
 * Description:
 */
public class BaseUtils {

    public static final String GOODS_ID = "goodsId";
    public static final String BASE_URL = "http://192.168.199.222:8080/GoodsManage/servlet";
    public static int userId = 1;

    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Context mContext, String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage(msg);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    public static void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static List<Type> parseType(String responseStr) {
        List<Type> typeList = new ArrayList<>();
        if (!TextUtils.isEmpty(responseStr)) {
            Gson gson = new Gson();
            typeList = gson.fromJson(responseStr, new TypeToken<ArrayList<Type>>(){}.getType());
        }
        return typeList;
    }

}
