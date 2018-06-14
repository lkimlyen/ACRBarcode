package com.demo.architect.data.model.offline;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogScanCreatePackList extends RealmObject {
    @PrimaryKey
    private int orderId;

    @SuppressWarnings("unused")
    private RealmList<LogScanCreatePack> itemList;

    public LogScanCreatePackList() {
    }

    public LogScanCreatePackList(int orderId) {
        this.orderId = orderId;
    }

    public RealmList<LogScanCreatePack> getItemList() {
        return itemList;
    }
}
