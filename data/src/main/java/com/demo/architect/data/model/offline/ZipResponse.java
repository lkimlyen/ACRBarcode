package com.demo.architect.data.model.offline;

public class ZipResponse {
    private int result;
    private String description;

    public ZipResponse() {
    }

    public ZipResponse(int result, String description) {
        this.result = result;
        this.description = description;
    }

    public int getResult() {
        return result;
    }

    public String getDescription() {
        return description;
    }

}
