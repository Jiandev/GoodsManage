package com.example.goodsmanage.common.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Author: Jiandev
 * Modify By:
 * Date: 2019/6/5 10:41
 * Description:
 */
public class Goods implements Serializable {

    @SerializedName("ID")
    private int id;

    @SerializedName("GoodsName")
    private String goodsName;

    @SerializedName("UserID")
    private int userId;

    @SerializedName("TypeID")
    private int typeId;

    @SerializedName("GoodsNum")
    private int goodsNum;

    @SerializedName("Price")
    private double price;

    @SerializedName("Img")
    private String imgPath;

    @SerializedName("Intro")
    private String intro;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
