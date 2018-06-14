package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogScanCreatePack extends RealmObject {
    @PrimaryKey
    private int id;
    private String barcode;
    private String deviceTime;
    private String serverTime;
    private double latitude;
    private double longitude;
    private String createByPhone;
    private int productId;
    private int orderId;
    private int serial;
    private int numCodeScan;
    private int numTotal;
    private int numPack;
    private int numInput;
    private int numRest;
    private int status;
    private int serverId;
    private int createBy;

    public LogScanCreatePack() {
    }

    public LogScanCreatePack(String barcode, String deviceTime, String serverTime, double latitude, double longitude, String createByPhone, int productId, int orderId, int serial, int numCodeScan, int numTotal, int numPack, int numInput, int numRest, int status, int serverId, int createBy) {
        this.productId = productId;
        this.orderId = orderId;
        this.numTotal = numTotal;
        this.numRest = numRest;
        this.createBy = createBy;
        this.barcode = barcode;
        this.deviceTime = deviceTime;
        this.serverTime = serverTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createByPhone = createByPhone;
        this.serial = serial;
        this.numCodeScan = numCodeScan;
        this.numPack = numPack;
        this.numInput = numInput;
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

    public int getNumCodeScan() {
        return numCodeScan;
    }

    public int getNumPack() {
        return numPack;
    }

    public int getNumInput() {
        return numInput;
    }

    public int getStatus() {
        return status;
    }

    public int getServerId() {
        return serverId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreateBy() {
        return createBy;
    }

    public int getNumTotal() {
        return numTotal;
    }

    public void setNumInput(int numInput) {
        this.numInput = numInput;
    }

    public int getProductId() {
        return productId;
    }

    public int getNumRest() {
        return numRest;
    }

    public void setNumRest(int numRest) {
        this.numRest = numRest;
    }

    public int getOrderId() {
        return orderId;
    }

    public static void create(Realm realm, LogScanCreatePack item, int orderId) {
        item.setId(id(realm) + 1);
        LogScanCreatePackList parent = realm.where(LogScanCreatePackList.class).equalTo("orderId", orderId).findFirst();
        if (parent == null) {
            parent = new LogScanCreatePackList(orderId);
            parent = realm.copyToRealmOrUpdate(parent);
        }
        RealmList<LogScanCreatePack> items = parent.getItemList();
        LogScanCreatePack present = realm.copyToRealmOrUpdate(item);
        items.add(present);

    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(LogScanCreatePack.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static void delete(Realm realm, int id) {
        LogScanCreatePack item = realm.where(LogScanCreatePack.class).equalTo("id", id).findFirst();
        // Otherwise it has been deleted already.
        if (item != null) {
            item.deleteFromRealm();
        }
    }


}
