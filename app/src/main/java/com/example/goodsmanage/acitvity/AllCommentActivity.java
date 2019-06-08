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
import com.example.goodsmanage.adapter.CommentRecyclerAdapter;
import com.example.goodsmanage.common.entity.Comment;
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

public class AllCommentActivity extends AppCompatActivity {

    private RecyclerView recyclerAllComment;
    private List<Comment> commentList = new ArrayList<>();
    private AllCommentRecyclerAdapter adapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comment);
        mContext = AllCommentActivity.this;
        recyclerAllComment = findViewById(R.id.recycler_all_comment);
        initComments();
    }

    private void initComments() {
        int userId = BaseUtils.getUserId();
        if (userId <= 0) {
            ToastUtil.showMsg(mContext, "登录失效，请重新登录");
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        final ProgressDialog progressDialog = BaseUtils.showProgressDialog(mContext, "正在加载...");
        HttpUtils.doGet(BaseUtils.BASE_URL + "/getUserComment?UserID=" + userId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                BaseUtils.closeProgressDialog(progressDialog);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BaseUtils.closeProgressDialog(progressDialog);
                final String responseStr = response.body().string();
                parseCommentList(responseStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerAllComment.setLayoutManager(layoutManager);
                        adapter = new AllCommentRecyclerAdapter(mContext, commentList);
                        recyclerAllComment.setAdapter(adapter);
                    }
                });
            }
        });
    }

    private void parseCommentList(String responseStr) {
        if (!TextUtils.isEmpty(responseStr)) {
            Gson gson = new Gson();
            commentList = gson.fromJson(responseStr, new TypeToken<ArrayList<Comment>>(){}.getType());
        }
    }
}
