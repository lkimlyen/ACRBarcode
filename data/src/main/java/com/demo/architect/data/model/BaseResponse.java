package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;

/**
 * Created by uyminhduc on 10/23/16.
 */

public class BaseResponse {

    @Expose
    private int ID;
    @Expose
    private int Number;
    @Expose
    private String CodeScan;
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

    public int getNumber() {
        return Number;
    }

    public int getID() {
        return ID;
    }

    public String getCodeScan() {
        return CodeScan;
    }
}
