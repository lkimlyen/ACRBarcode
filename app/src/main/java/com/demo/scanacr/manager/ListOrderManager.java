package com.demo.scanacr.manager;

import com.demo.architect.data.model.OrderACREntity;
import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.offline.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class ListOrderManager {
    private List<OrderModel> listOrder = new ArrayList<>();
    private static ListOrderManager instance;

    public static ListOrderManager getInstance() {
        if (instance == null) {
            instance = new ListOrderManager();
        }
        return instance;
    }

    public List<OrderModel> getListOrder() {
        return listOrder;
    }

    public void setListOrder(List<OrderModel> listOrder) {
        this.listOrder = listOrder;
    }

    public OrderModel getOrderById(int id) {
        for (OrderModel requestEntity : listOrder) {
            if (requestEntity.getId() == id) {
                return requestEntity;
            }
        }
        return null;
    }

}
