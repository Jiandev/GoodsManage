package com.example.goodsmanage.common.entity;

import com.google.gson.annotations.SerializedName;

public class LoginResult {
    @SerializedName("userid")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
