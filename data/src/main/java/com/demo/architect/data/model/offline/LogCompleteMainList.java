package com.demo.architect.data.model.offline;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogCompleteMainList extends RealmObject {
    @PrimaryKey
    private int orderId;

    @SuppressWarnings("unused")
    private RealmList<LogCompleteCreatePackList> itemList;

    public LogCompleteMainList() {
    }

    public LogCompleteMainList(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public RealmList<LogCompleteCreatePackList> getItemList() {
        return itemList;
    }
}
