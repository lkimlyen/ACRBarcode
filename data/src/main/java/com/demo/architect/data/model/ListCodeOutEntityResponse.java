package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListCodeOutEntityResponse {
    @SerializedName("ListSimpleCodeOut")
    @Expose
    private List<CodeOutEntity> list;

    public List<CodeOutEntity> getList() {
        return list;
    }
    @Expose
    private int Status;
    @Expose
    private String Description;

    public int getStatus() {
        return Status;
    }

    public String getDescription() {
        return Description;
    }
}
