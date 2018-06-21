package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ScanDeliveryList extends RealmObject {
    @PrimaryKey
    private int id;

    private int times;

    private int status;

    private String codeRequest;

    @SuppressWarnings("unused")
    private RealmList<ScanDeliveryModel> itemList;

    public ScanDeliveryList() {
    }

    public ScanDeliveryList(int id, int times, String codeRequest) {
        this.id = id;
        this.codeRequest = codeRequest;
    }

    public RealmList<ScanDeliveryModel> getItemList() {
        return itemList;
    }

    public int getId() {
        return id;
    }

    public int getTimes() {
        return times;
    }

    public int getStatus() {
        return status;
    }


    public void setStatus(int status) {
        this.status = status;
    }

    public String getCodeRequest() {
        return codeRequest;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(ScanDeliveryList.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }
}
