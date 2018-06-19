package com.demo.architect.data.model.offline;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogCompleteCreatePackList extends RealmObject {
    @PrimaryKey
    private int id;

    private String codeRequest;

    private int serial;

    private int numTotal;

    private String dateCreate;

    @SuppressWarnings("unused")
    private RealmList<LogCompleteCreatePack> itemList;

    public LogCompleteCreatePackList() {
    }

    public LogCompleteCreatePackList(int id, String codeRequest, int serial, int numTotal,String dateCreate) {
        this.id = id;
        this.codeRequest = codeRequest;
        this.serial = serial;
        this.numTotal = numTotal;
        this.dateCreate = dateCreate;
    }

    public RealmList<LogCompleteCreatePack> getItemList() {
        return itemList;
    }

    public int getId() {
        return id;
    }

    public String getCodeRequest() {
        return codeRequest;
    }

    public int getSerial() {
        return serial;
    }

    public int getNumTotal() {
        return numTotal;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setNumTotal(int numTotal) {
        this.numTotal = numTotal;
    }
}
