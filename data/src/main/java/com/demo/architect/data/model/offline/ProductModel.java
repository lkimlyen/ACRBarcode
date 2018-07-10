package com.demo.architect.data.model.offline;

import io.realm.RealmObject;

public class ProductModel extends RealmObject {
    private int productId;
    private int orderId;
    private String codeColor;
    private int serial;
    private int lenght;
    private int wide;
    private int deep;
    private String grain;
    private int number;
    private int status;
    private int numberRest;
    private int numberScan;
    private int numCompleteScan;

    public ProductModel() {
    }

    public ProductModel(int productId, int orderId, String codeColor, int serial, int lenght, int wide, int deep, String grain, int number, int numberRest, int numberScan, int numCompleteScan) {
        this.productId = productId;
        this.orderId = orderId;
        this.codeColor = codeColor;
        this.serial = serial;
        this.lenght = lenght;
        this.wide = wide;
        this.deep = deep;
        this.grain = grain;
        this.number = number;
        this.numberRest = numberRest;
        this.numberScan = numberScan;
        this.numCompleteScan = numCompleteScan;
    }


    public String getCodeColor() {
        return codeColor;
    }

    public int getSerial() {
        return serial;
    }

    public int getLenght() {
        return lenght;
    }

    public int getWide() {
        return wide;
    }

    public int getDeep() {
        return deep;
    }

    public String getGrain() {
        return grain;
    }

    public int getNumber() {
        return number;
    }

    public int getProductId() {
        return productId;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNumberRest() {
        return numberRest;
    }

    public void setNumberRest(int numberRest) {
        this.numberRest = numberRest;
    }

    public int getNumberScan() {
        return numberScan;
    }

    public void setNumberScan(int numberScan) {
        this.numberScan = numberScan;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumCompleteScan() {
        return numCompleteScan;
    }

    public void setNumCompleteScan(int numCompleteScan) {
        this.numCompleteScan = numCompleteScan;
    }
}
