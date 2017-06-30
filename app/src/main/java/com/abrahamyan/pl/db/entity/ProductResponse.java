package com.abrahamyan.pl.db.entity;

import com.abrahamyan.pl.util.Constant;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by SEVAK on 29.06.2017.
 */

public class ProductResponse {

    @SerializedName(Constant.Json.PRODUCTS)
    private ArrayList<Product> products;

    public ProductResponse() {
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ProductResponse(ArrayList<Product> products) {
        this.products = products;
    }
}
