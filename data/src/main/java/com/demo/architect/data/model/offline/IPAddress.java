package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class IPAddress extends RealmObject {
    @PrimaryKey
    private int id;
    private String ipAddress;
    private int portNumber;
    private int createBy;
    private String dateCreate;

    public IPAddress() {
    }

    public IPAddress(int id, String ipAddress, int portNumber, int createBy, String dateCreate) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.createBy = createBy;
        this.dateCreate = dateCreate;
    }

    public int getId() {
        return id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public int getCreateBy() {
        return createBy;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

}
