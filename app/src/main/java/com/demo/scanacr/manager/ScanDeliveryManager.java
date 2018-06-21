package com.demo.scanacr.manager;

import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.ScanDeliveryList;
import com.demo.architect.data.model.offline.ScanDeliveryModel;

import java.util.List;

public class ScanDeliveryManager {
    private ScanDeliveryList deliveryList;
    private static ScanDeliveryManager instance;

    public static ScanDeliveryManager getInstance() {
        if (instance == null) {
            instance = new ScanDeliveryManager();
        }
        return instance;
    }

    public void setDeliveryList(ScanDeliveryList deliveryList) {
        this.deliveryList = deliveryList;
    }

    public ScanDeliveryList getDeliveryList() {
        return deliveryList;
    }
    public List<ScanDeliveryModel> getDeliveryModelList() {
        return deliveryList.getItemList();
    }


}
