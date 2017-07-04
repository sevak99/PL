package com.abrahamyan.pl.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.abrahamyan.pl.util.Constant;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEVAK on 29.06.2017.
 */

public class ProductResponse implements Parcelable {

    @SerializedName(Constant.Json.PRODUCTS)
    private List<Product> products;

    public ProductResponse() {
    }

    protected ProductResponse(Parcel in) {
        products = in.createTypedArrayList(Product.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(products);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductResponse> CREATOR = new Creator<ProductResponse>() {
        @Override
        public ProductResponse createFromParcel(Parcel in) {
            return new ProductResponse(in);
        }

        @Override
        public ProductResponse[] newArray(int size) {
            return new ProductResponse[size];
        }
    };

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public ProductResponse(ArrayList<Product> products) {
        this.products = products;
    }
}
