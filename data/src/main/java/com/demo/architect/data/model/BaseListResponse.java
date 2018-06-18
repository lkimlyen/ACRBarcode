package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by uyminhduc on 10/23/16.
 */

public class BaseListResponse<T> {

    @Expose
    private List<T> list;
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

    public List<T> getList() {
        return list;
    }
}
