package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderACREntity {
    @SerializedName("Code")
    @Expose
    private String code;

    @SerializedName("CodeSX")
    @Expose
    private String codeSX;

    @SerializedName("ID")
    @Expose
    private int id;

    @SerializedName("CustomerID")
    @Expose
    private int customerID;

    @SerializedName("CustomerName")
    @Expose
    private String customerName;

    public String getCode() {
        return code;
    }

    public String getCodeSX() {
        return codeSX;
    }

    public int getId() {
        return id;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }
}
