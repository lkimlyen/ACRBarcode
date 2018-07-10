package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogDeleteCreatePack extends RealmObject {
    @PrimaryKey
    private int id;
    private String barcode;
    private int orderId;
    private ProductModel productModel;
    private String deviceTime;
    private String serverTime;
    private double latitude;
    private double longitude;
    private String createByPhone;
    private int serial;
    private int numTotal;
    private int numInput;
    private int createBy;
    private String timeDelete;
    private int status;
    private int serverId;

    public LogDeleteCreatePack() {
    }

    public LogDeleteCreatePack(int id, String barcode, int orderId, ProductModel productModel, String deviceTime, String serverTime, double latitude, double longitude, String createByPhone, int serial, int numTotal, int numInput, int createBy, String timeDelete, int status, int serverId) {
        this.id = id;
        this.barcode = barcode;
        this.orderId = orderId;
        this.productModel = productModel;
        this.deviceTime = deviceTime;
        this.serverTime = serverTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createByPhone = createByPhone;
        this.serial = serial;
        this.numTotal = numTotal;
        this.numInput = numInput;
        this.createBy = createBy;
        this.timeDelete = timeDelete;
        this.status = status;
        this.serverId = serverId;
    }

    public int getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getDeviceTime() {
        return deviceTime;
    }

    public String getServerTime() {
        return serverTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCreateByPhone() {
        return createByPhone;
    }


    public int getSerial() {
        return serial;
    }


    public int getNumTotal() {
        return numTotal;
    }


    public int getNumInput() {
        return numInput;
    }

    public int getCreateBy() {
        return createBy;
    }

    public String getTimeDelete() {
        return timeDelete;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(LogDeleteCreatePack.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }
}
