package com.demo.scanacr.screen.print_stemp;

import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.scanacr.app.base.BasePresenter;
import com.demo.scanacr.app.base.BaseView;

import java.util.HashMap;

/**
 * Created by MSI on 26/11/2017.
 */

public interface PrintStempContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showSerialPack(int serial);

        void showOrder(OrderModel model);

        void showListCreatePack(HashMap<LogScanCreatePack, ProductModel> list);

        void showSumPack(int sum);
    }

    interface Presenter extends BasePresenter {
        void getMaxNumberOrder(int orderId);

        void getOrder(int order);

        void getListCreatePack(int orderId);
    }
}
