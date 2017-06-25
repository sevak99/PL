package com.abrahamyan.pl.db.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SEVAK on 25.06.2017.
 */

public class Product {

    @SerializedName("product_id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("price")
    int price;

    @SerializedName("image")
    String image;

    public Product() {
    }

    public Product(int id, String name, int price, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public void setId(int id) {
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

    public int getId() {

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
