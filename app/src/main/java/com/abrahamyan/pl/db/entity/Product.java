package com.abrahamyan.pl.db.entity;

import com.abrahamyan.pl.util.Constant;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SEVAK on 25.06.2017.
 */

public class Product{

    // ===========================================================
    // Fields
    // ===========================================================

    @SerializedName(Constant.Json.ID)
    String id;

    @SerializedName(Constant.Json.NAME)
    String name;

    @SerializedName(Constant.Json.PRICE)
    int price;

    @SerializedName(Constant.Json.IMAGE)
    String image;

    @SerializedName(Constant.Json.DESCRIPTION)
    String description;

    // ===========================================================
    // Constructors
    // ===========================================================

    public Product() {
    }

    public Product(String id, String name, int price, String image, String description) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
