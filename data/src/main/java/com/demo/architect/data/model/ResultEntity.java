package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultEntity {
    @SerializedName("ID")
    @Expose
    private int id;

    @SerializedName("CodeScan")
    @Expose
    private String barcode;

    public int getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }
}
