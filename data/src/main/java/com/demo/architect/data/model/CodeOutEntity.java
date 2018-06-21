package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CodeOutEntity {
    @SerializedName("OrderACRID")
    @Expose
    private int orderId;

    @SerializedName("CodeScan")
    @Expose
    private String barcode;

    @SerializedName("RequestID")
    @Expose
    private int requestId;

    @SerializedName("PackageID")
    @Expose
    private int packageId;

    public int getOrderId() {
        return orderId;
    }

    public String getBarcode() {
        return barcode;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getPackageId() {
        return packageId;
    }
}
