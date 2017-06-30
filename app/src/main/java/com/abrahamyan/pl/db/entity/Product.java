package com.abrahamyan.pl.db.entity;

import com.abrahamyan.pl.util.Constant;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SEVAK on 25.06.2017.
 */

public class Product {

    @SerializedName(Constant.Json.ID)
    String id;

    @SerializedName(Constant.Json.NAME)
    String name;

    @SerializedName(Constant.Json.PRICE)
    int price;

    @SerializedName(Constant.Json.IMAGE)
    String image;

    public Product() {
    }

    public Product(String id, String name, int price, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
