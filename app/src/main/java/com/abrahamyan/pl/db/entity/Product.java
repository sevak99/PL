package com.abrahamyan.pl.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.abrahamyan.pl.util.Constant;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SEVAK on 25.06.2017.
 */

public class Product implements Parcelable{

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

    public Product() {
    }

    public Product(String id, String name, int price, String image, String description) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
    }

    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        price = in.readInt();
        image = in.readString();
        description = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeString(image);
        dest.writeString(description);
    }
}
