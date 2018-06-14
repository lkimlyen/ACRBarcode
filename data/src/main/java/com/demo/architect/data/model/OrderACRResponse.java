package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderACRResponse {

    @SerializedName("listorderacr")
    @Expose
    private List<OrderACREntity> list;

    @SerializedName("Status")
    @Expose
    private int status;

    @SerializedName("Description")
    @Expose
    private String description;

    public List<OrderACREntity> getList() {
        return list;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
