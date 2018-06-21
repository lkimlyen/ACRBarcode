package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ScanDeliveryModel extends RealmObject {
    @PrimaryKey
    private int id;
    private String barcode;
    private String deviceTime;
    private String serverTime;
    private double latitude;
    private double longitude;
    private String createByPhone;
    private int packageId;
    private int orderId;
    private int requestId;
    private int serial;
    private int createBy;
    private int serverId;
    public ScanDeliveryModel() {
    }

    public ScanDeliveryModel(String barcode, String deviceTime, String serverTime, double latitude, double longitude, String createByPhone, int packageId, int orderId, int requestId, int serial, int createBy) {

        this.barcode = barcode;
        this.deviceTime = deviceTime;
        this.serverTime = serverTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createByPhone = createByPhone;
        this.packageId = packageId;
        this.orderId = orderId;
        this.requestId = requestId;
        this.serial = serial;
        this.createBy = createBy;
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

    public int getPackageId() {
        return packageId;
    }

    public int getOrderId() {
        return orderId;
    }


    public int getCreateBy() {
        return createBy;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(ScanDeliveryModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

}
