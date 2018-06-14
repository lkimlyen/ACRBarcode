package com.demo.architect.data.model.offline;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CustomerModel extends RealmObject {
    @PrimaryKey
    private int id;
    private String name;
    private String address1;
    private String address2;

    public CustomerModel() {
    }

    public CustomerModel(int id, String name, String address1, String address2) {
        this.id = id;
        this.name = name;
        this.address1 = address1;
        this.address2 = address2;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }
}
