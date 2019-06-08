package com.example.goodsmanage;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.goodsmanage.common.entity.Result;
import com.example.goodsmanage.common.utils.BaseUtils;
import com.example.goodsmanage.common.utils.HttpUtils;
import com.example.goodsmanage.common.utils.ToastUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText editUsername, editPassword, editName,
            editTel, editAddress, editEmail, editYear, editMonth, editDay;
    private RadioGroup radioGroup;
    private RadioButton rbMale, rbFemale;
    private Button btnRegister;

    private Context mContext;
    private String sex = "m";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = RegisterActivity.this;
        initView();
    }

    private void initView() {
        editUsername = findViewById(R.id.edit_reg_username);
        editPassword = findViewById(R.id.edit_reg_password);
        editName = findViewById(R.id.edit_reg_name);
        editTel = findViewById(R.id.edit_reg_tel);
        editAddress = findViewById(R.id.edit_reg_address);
        editEmail = findViewById(R.id.edit_reg_email);
        editYear = findViewById(R.id.edit_year);
        editMonth = findViewById(R.id.edit_month);
        editDay = findViewById(R.id.edit_day);
        radioGroup = findViewById(R.id.rg_sex);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);
        btnRegister = findViewById(R.id.btn_reg_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_male:
                        sex = "m";
                        break;
                    case R.id.rb_female:
                        sex = "f";
                        break;
                }
            }
        });
    }

    private void register() {
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
        String name = editName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showMsg(mContext, "请输入姓名");
            return;
        }
        String tel = editTel.getText().toString();
        if (TextUtils.isEmpty(tel) || tel.length() != 11) {
            ToastUtil.showMsg(mContext, "请输入11位电话号码");
            return;
        }
        String address = editAddress.getText().toString();
        if (TextUtils.isEmpty(address)) {
            ToastUtil.showMsg(mContext, "请输入地址");
            return;
        }
        String email = editEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            ToastUtil.showMsg(mContext, "请输入邮箱");
            return;
        }
        int year = editYear.getText().toString().isEmpty() ? 0 : Integer.valueOf(editYear.getText().toString());
        int month = editMonth.getText().toString().isEmpty() ? 0 : Integer.valueOf(editMonth.getText().toString());
        int day = editDay.getText().toString().isEmpty() ? 0 : Integer.valueOf(editDay.getText().toString());
        if (year <= 0 || (month <= 0 || month > 12) || (day <= 0 || day > 31)) {
            ToastUtil.showMsg(mContext, "请输入正确的生日");
            return;
        }

        String extUrl = "?Login=" + username + "&Pass=" + password + "&Name=" + name + "&Sex=" + sex +
                "&Tel=" + tel + "&Address=" + address + "&Birth=" + year + "-" + month + "-" + day +
                "&Mail=" + email;
        final ProgressDialog progressDialog = BaseUtils.showProgressDialog(mContext, "正在注册...");
        HttpUtils.doGet(BaseUtils.BASE_URL + "/userRegister" + extUrl, new Callback() {
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
                        if (result.getSuccess().equals("2")) {
                            ToastUtil.showMsg(mContext, "注册成功");
                            finish();
                        } else if (result.getSuccess().equals("1")) {
                            ToastUtil.showMsg(mContext, "用户名已存在");
                        } else {
                            ToastUtil.showMsg(mContext, "注册失败，请检查用户信息");
                        }
                    }
                });
            }
        });
    }
}
