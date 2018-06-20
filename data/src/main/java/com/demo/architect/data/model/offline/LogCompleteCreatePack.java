package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogCompleteCreatePack extends RealmObject {
    @PrimaryKey
    private int id;
    private String barcode;
    private String deviceTime;
    private String serverTime;
    private double latitude;
    private double longitude;
    private String createByPhone;
    private int productId;
    private ProductModel productModel;
    private int orderId;
    private int serial;
    private int numTotal;
    private int numInput;
    private int status;
    private int createBy;

    public LogCompleteCreatePack() {
    }

    public LogCompleteCreatePack(String barcode, String deviceTime, String serverTime, double latitude,
                                 double longitude, String createByPhone, int productId, ProductModel productModel,
                                 int orderId, int serial, int numTotal, int numInput, int status, int createBy) {
        this.productId = productId;
        this.productModel = productModel;
        this.orderId = orderId;
        this.numTotal = numTotal;
        this.createBy = createBy;
        this.barcode = barcode;
        this.deviceTime = deviceTime;
        this.serverTime = serverTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createByPhone = createByPhone;
        this.serial = serial;
        this.numInput = numInput;
        this.status = status;
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

    public int getNumInput() {
        return numInput;
    }

    public int getStatus() {
        return status;
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

    public int getOrderId() {
        return orderId;
    }


    public static void create(Realm realm, LogCompleteCreatePack item, int id, int serial, int numTotal, String dateCreate) {
        item.setId(id(realm) + 1);

        LogCompleteMainList mainList = realm.where(LogCompleteMainList.class).equalTo("orderId", item.getOrderId()).findFirst();
        if (mainList == null) {
            mainList = new LogCompleteMainList(item.getOrderId());
            mainList = realm.copyToRealmOrUpdate(mainList);
        }
        RealmList<LogCompleteCreatePackList> itemList = mainList.getItemList();
        LogCompleteCreatePackList parent = realm.where(LogCompleteCreatePackList.class).equalTo("id", id).findFirst();
        if (parent == null) {
            OrderModel orderModel = realm.where(OrderModel.class).equalTo("id", item.getOrderId()).findFirst();
            parent = new LogCompleteCreatePackList(id, orderModel.getCodeProduction(), serial, numTotal, dateCreate);
            parent = realm.copyToRealmOrUpdate(parent);
            itemList.add(parent);
        }
        RealmList<LogCompleteCreatePack> items = parent.getItemList();
        LogCompleteCreatePack present = realm.copyToRealm(item);
        items.add(present);

    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(LogCompleteCreatePack.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static void delete(Realm realm, int id) {
        LogCompleteCreatePack item = realm.where(LogCompleteCreatePack.class).equalTo("id", id).findFirst();
        // Otherwise it has been deleted already.
        if (item != null) {
            item.deleteFromRealm();
        }
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setDeviceTime(String deviceTime) {
        this.deviceTime = deviceTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setCreateByPhone(String createByPhone) {
        this.createByPhone = createByPhone;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public void setNumTotal(int numTotal) {
        this.numTotal = numTotal;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }
}
