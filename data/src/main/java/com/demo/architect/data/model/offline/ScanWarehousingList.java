package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ScanWarehousingList extends RealmObject {
    @PrimaryKey
    private int id;

    @SuppressWarnings("unused")
    private RealmList<ScanWarehousingModel> itemList;

    public ScanWarehousingList() {
    }

    public ScanWarehousingList(int id) {
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public RealmList<ScanWarehousingModel> getItemList() {
        return itemList;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(ScanWarehousingList.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }
}
