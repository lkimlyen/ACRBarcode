package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductEntity {
    @SerializedName("ProductID")
    @Expose
    private int productID;

    @SerializedName("STT")
    @Expose
    private int stt;

    @SerializedName("CodeColor")
    @Expose
    private String codeColor;

    @SerializedName("Lengh")
    @Expose
    private int length;

    @SerializedName("Wide")
    @Expose
    private int wide;

    @SerializedName("Deep")
    @Expose
    private int deep;

    @SerializedName("Grain")
    @Expose
    private String grain;

    @SerializedName("Number")
    @Expose
    private int number;

    public int getProductID() {
        return productID;
    }

    public int getStt() {
        return stt;
    }

    public String getCodeColor() {
        return codeColor;
    }

    public int getLength() {
        return length;
    }

    public int getWide() {
        return wide;
    }

    public int getDeep() {
        return deep;
    }

    public String getGrain() {
        return grain;
    }

    public int getNumber() {
        return number;
    }
}
