package com.example.goodsmanage.common.utils;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.*;

/**
 * Author: Jiandev
 * Modify By:
 * Date: 2019/5/31 16:33
 * Description:
 */
public class HttpUtils {

    public static <T> void doPost(String url, T params, okhttp3.Callback callback){  //这里没有返回，也可以返回string
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Gson gson = new Gson();
        String paramsStr = gson.toJson(params);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json;charset=utf-8"), paramsStr);
        Request request = new Request
                .Builder()
                .post(requestBody)
                .url(url)
                .build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public static void doGet(String url, okhttp3.Callback callback){  //这里没有返回，也可以返回string
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }
}
