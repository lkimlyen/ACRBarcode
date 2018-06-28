package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class OrderModel extends RealmObject {
    @PrimaryKey
    private int id;
    private int customerId;
    private String codeSO;
    private String codeProduction;
    private String customerName;
    private int createBy;
    private String createDate;
    private int orderId;

    public OrderModel() {
    }

    public OrderModel(String codeProduction) {
        this.codeProduction = codeProduction;
    }

    public OrderModel(int id, int customerId, String codeSO, String codeProduction, String customerName, int createBy, String createDate, int status) {
        this.id = id;
        this.customerId = customerId;
        this.codeSO = codeSO;
        this.codeProduction = codeProduction;
        this.customerName = customerName;
        this.createBy = createBy;
        this.createDate = createDate;
        this.orderId = status;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCodeSO() {
        return codeSO;
    }

    public String getCodeProduction() {
        return codeProduction;
    }

    public int getCreateBy() {
        return createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public static void create(Realm realm, OrderModel item) {
        realm.copyToRealmOrUpdate(item);
    }

    public String getCustomerName() {
        return customerName;
    }

    @Override
    public String toString() {
        return codeProduction;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
