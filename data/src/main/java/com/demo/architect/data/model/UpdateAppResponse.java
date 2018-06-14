package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateAppResponse {
    @SerializedName("Status")
    @Expose
    private int status;

    @SerializedName("Description")
    @Expose
    private String description;

    @SerializedName("Link")
    @Expose
    private String link;

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }
}
