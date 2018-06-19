package com.demo.scanacr.screen.detail_package;

import com.demo.architect.data.model.offline.LogCompleteCreatePack;
import com.demo.architect.data.model.offline.LogCompleteCreatePackList;
import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.scanacr.app.base.BasePresenter;
import com.demo.scanacr.app.base.BaseView;

import java.util.HashMap;

/**
 * Created by MSI on 26/11/2017.
 */

public interface DetailPackageContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);
        void showSuccess(String message);
        void showOrder(OrderModel model);
        void showListCreatePack(LogCompleteCreatePackList list);
        void showDetailPack(LogCompleteCreatePackList pack);
        void deletePackSuccess();
        void showDialogNumber(ProductModel productModel, String barcode);
    }

    interface Presenter extends BasePresenter {
        void getOrder(int orderId);

        void getListHistory(int logId);

        void deleteCode(int id,int productId, int logId);

        void deletePack(int logId, int orderId);
        void printStemp(int orderId, int serial, int serverId, int logId);
        void updateData(int logId, int orderId, int serial, boolean print);
        void checkBarcode(String barcode, int orderId);
        int countListScan(int logId);
        void saveBarcode(double latitude, double longitude, String barcode, ProductModel product, int logId, int numberInput);
    }
}
