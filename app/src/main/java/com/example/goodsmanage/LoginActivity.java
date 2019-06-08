package com.example.goodsmanage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.goodsmanage.common.entity.LoginResult;
import com.example.goodsmanage.common.utils.BaseUtils;
import com.example.goodsmanage.common.utils.HttpUtils;
import com.example.goodsmanage.common.utils.ToastUtil;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText editPassword;
    private Button btnLogin, btnRegister;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;
        initView();
    }

    private void initView() {
        editUsername = findViewById(R.id.edit_login_username);
        editPassword = findViewById(R.id.edit_login_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        String username = editUsername.getText().toString();
        if (TextUtils.isEmpty(username)) {
            ToastUtil.showMsg(mContext, "请输入用户名");
            return;
        }
        String password = editPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showMsg(mContext, "请输入密码");
            return;
        }
        final ProgressDialog progressDialog = BaseUtils.showProgressDialog(mContext, "正在登录...");
        HttpUtils.doGet(BaseUtils.BASE_URL + "/userLogin?Login=" + username + "&Pass=" + password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                BaseUtils.closeProgressDialog(progressDialog);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showMsg(mContext, "网络错误");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BaseUtils.closeProgressDialog(progressDialog);
                final String responseStr = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseResult(responseStr);
                    }
                });
            }
        });
    }

    private void parseResult(String responseStr) {
        if (!TextUtils.isEmpty(responseStr)) {
            Gson gson = new Gson();
            LoginResult loginResult = gson.fromJson(responseStr, LoginResult.class);
            if (loginResult.getUserId().equals("0")) {
                ToastUtil.showMsg(mContext, "登陆失败，用户名或密码错误");
            }else {
                BaseUtils.setUserId(Integer.parseInt(loginResult.getUserId()));
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
