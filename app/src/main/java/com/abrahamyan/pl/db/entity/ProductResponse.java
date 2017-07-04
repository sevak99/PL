package com.abrahamyan.pl.db.entity;

import com.abrahamyan.pl.util.Constant;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by SEVAK on 29.06.2017.
 */

public class ProductResponse {

    // ===========================================================
    // Fields
    // ===========================================================

    @SerializedName(Constant.Json.PRODUCTS)
    private ArrayList<Product> products;

    // ===========================================================
    // Constructors
    // ===========================================================

    public ProductResponse() {
    }

    public ProductResponse(ArrayList<Product> products) {
        this.products = products;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
}
