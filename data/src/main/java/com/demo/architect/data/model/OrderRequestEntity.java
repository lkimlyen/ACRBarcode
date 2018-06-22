package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderRequestEntity {
    @SerializedName("ID")
    @Expose
    private int id;

    @SerializedName("CodeRequest")
    @Expose
    private String codeRequest;

    @SerializedName("OrderACRID")
    @Expose
    private int orderACRID;

    @SerializedName("CodeSX")
    @Expose
    private String codeSX;

    public OrderRequestEntity(String codeSX) {
        this.codeSX = codeSX;
    }

    public int getId() {
        return id;
    }

    public String getCodeRequest() {
        return codeRequest;
    }

    public int getOrderACRID() {
        return orderACRID;
    }

    public String getCodeSX() {
        return codeSX;
    }

    @Override
    public String toString() {
        return codeSX;
    }
}
