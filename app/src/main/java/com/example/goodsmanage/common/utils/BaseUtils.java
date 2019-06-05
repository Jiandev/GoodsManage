package com.example.goodsmanage.common.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Author: Jiandev
 * Modify By:
 * Date: 2019/6/5 11:51
 * Description:
 */
public class BaseUtils {

    public static final String GOODS_ID = "goodsId";
    public static final String BASE_URL = "http://192.168.0.119:8080/GoodsManage/servlet";

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
}
