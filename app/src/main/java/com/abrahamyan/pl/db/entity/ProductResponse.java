package com.abrahamyan.pl.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.abrahamyan.pl.util.Constant;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by SEVAK on 29.06.2017.
 */

public class ProductResponse implements Parcelable {

    // ===========================================================
    // Constants
    // ===========================================================

    public static final Parcelable.Creator<ProductResponse> CREATOR = new Parcelable.Creator<ProductResponse>() {
        @Override
        public ProductResponse createFromParcel(Parcel source) {
            return new ProductResponse(source);
        }

        @Override
        public ProductResponse[] newArray(int size) {
            return new ProductResponse[size];
        }
    };

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

    protected ProductResponse(Parcel in) {
        this.products = in.createTypedArrayList(Product.CREATOR);
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

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.products);
    }
}
