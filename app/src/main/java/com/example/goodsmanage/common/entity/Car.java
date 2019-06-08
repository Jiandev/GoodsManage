package com.example.goodsmanage.common.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Car implements Serializable {

    @SerializedName("ID")
    private int id;

    @SerializedName("UserID")
    private int userId;

    @SerializedName("GoodsID")
    private int goodsId;

    @SerializedName("GoodsName")
    private String goodsName;

    @SerializedName("CartNum")
    private int cartNum;

    @SerializedName("Price")
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getCartNum() {
        return cartNum;
    }

    public void setCartNum(int cartNum) {
        this.cartNum = cartNum;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
