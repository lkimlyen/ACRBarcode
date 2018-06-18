package com.demo.architect.data.model.offline;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogCompleteCreatePackList extends RealmObject {
    @PrimaryKey
    private int id;

    private int orderId;

    private int serial;

    @SuppressWarnings("unused")
    private RealmList<LogCompleteCreatePack> itemList;

    public LogCompleteCreatePackList() {
    }

    public LogCompleteCreatePackList(int id, int orderId, int serial) {
        this.id = id;
        this.orderId = orderId;
        this.serial = serial;
    }

    public RealmList<LogCompleteCreatePack> getItemList() {
        return itemList;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }
}
