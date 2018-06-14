package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageEntity {

    @SerializedName("OrderID")
    @Expose
    private int orderID;

    @SerializedName("ID")
    @Expose
    private int id;

    @SerializedName("STT")
    @Expose
    private int STT;

    @SerializedName("CodeSX")
    @Expose
    private String codeSX;

    public int getOrderID() {
        return orderID;
    }

    public int getId() {
        return id;
    }

    public int getSTT() {
        return STT;
    }

    public String getCodeSX() {
        return codeSX;
    }
}
