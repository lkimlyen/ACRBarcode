package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogDeleteCreatePack extends RealmObject {
    @PrimaryKey
    private int id;
    private String barcode;
    private int orderId;
    private String deviceTime;
    private String serverTime;
    private double latitude;
    private double longitude;
    private String createByPhone;
    private int serial;
    private int numCodeScan;
    private int numTotal;
    private int numPack;
    private int numInput;
    private int createBy;
    private String timeDelete;

    public LogDeleteCreatePack() {
    }

    public LogDeleteCreatePack(int id, String barcode, int orderId, String deviceTime, String serverTime, double latitude, double longitude, String createByPhone, int serial, int numCodeScan, int numTotal, int numPack, int numInput, int createBy, String timeDelete) {
        this.id = id;
        this.barcode = barcode;
        this.orderId = orderId;
        this.deviceTime = deviceTime;
        this.serverTime = serverTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createByPhone = createByPhone;
        this.serial = serial;
        this.numCodeScan = numCodeScan;
        this.numTotal = numTotal;
        this.numPack = numPack;
        this.numInput = numInput;
        this.createBy = createBy;
        this.timeDelete = timeDelete;
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

    public int getNumCodeScan() {
        return numCodeScan;
    }

    public int getNumTotal() {
        return numTotal;
    }

    public int getNumPack() {
        return numPack;
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
}
