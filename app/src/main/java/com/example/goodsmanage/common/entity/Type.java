package com.example.goodsmanage.common.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Author: Jiandev
 * Modify By:
 * Date: 2019/6/5 14:57
 * Description:
 */
public class Type implements Serializable {

    @SerializedName("ID")
    private int id;

    @SerializedName("Type")
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
