package com.demo.scanacr.manager;

import com.demo.architect.data.model.OrderRequestEntity;

import java.util.ArrayList;
import java.util.List;

public class ListRequestInManager {
    private List<OrderRequestEntity> listRequest = new ArrayList<>();
    private static ListRequestInManager instance;

    public static ListRequestInManager getInstance() {
        if (instance == null) {
            instance = new ListRequestInManager();
        }
        return instance;
    }

    public List<OrderRequestEntity> getListRequest() {
        return listRequest;
    }

    public void setListRequest(List<OrderRequestEntity> listRequest) {
        this.listRequest = listRequest;
    }

    public OrderRequestEntity getRequest(int id) {
        for (OrderRequestEntity requestEntity : listRequest) {
            if (requestEntity.getId() == id) {
                return requestEntity;
            }
        }
        return null;
    }

}
