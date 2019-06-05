package com.example.goodsmanage.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by Jiandev on 2018/2/26.
 */

public class ToastUtil {

    private static  Toast toast;

    @SuppressLint("ShowToast")
    public static void showMsg(Context context, String msg){
        if (TextUtils.isEmpty(msg)) return;
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }else {
            toast.setText(msg);
        }
        toast.show();
    }
}
