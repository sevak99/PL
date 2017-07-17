package com.abrahamyan.pl.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.abrahamyan.pl.util.Constant;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SEVAK on 25.06.2017.
 */

public class Product implements Parcelable {

    // ===========================================================
    // Constants
    // ===========================================================

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    // ===========================================================
    // Fields
    // ===========================================================

    @SerializedName(Constant.Json.ID)
    private long id;

    @SerializedName(Constant.Json.NAME)
    private String name;

    @SerializedName(Constant.Json.PRICE)
    private long price;

    @SerializedName(Constant.Json.IMAGE)
    private String image;

    @SerializedName(Constant.Json.DESCRIPTION)
    private String description;

    private boolean favorite;

    private boolean isUser;

    // ===========================================================
    // Constructors
    // ===========================================================

    public Product() {
    }

    public Product(long id,
                   String name,
                   long price,
                   String image,
                   String description,
                   boolean favorite,
                   boolean isUser) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.favorite = favorite;
        this.isUser = isUser;
    }

    protected Product(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.price = in.readLong();
        this.image = in.readString();
        this.description = in.readString();
        this.favorite = in.readByte() != 0;
        this.isUser = in.readByte() != 0;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        this.isUser = user;
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
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeLong(this.price);
        dest.writeString(this.image);
        dest.writeString(this.description);
        dest.writeByte(this.favorite ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isUser ? (byte) 1 : (byte) 0);
    }
}
