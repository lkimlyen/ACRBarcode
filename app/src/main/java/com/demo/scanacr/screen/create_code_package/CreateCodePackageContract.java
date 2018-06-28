package com.demo.scanacr.screen.create_code_package;

import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.scanacr.app.base.BasePresenter;
import com.demo.scanacr.app.base.BaseView;

import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface CreateCodePackageContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showRequestProduction(List<OrderModel> list);

        void showLogScanCreatePack(LogScanCreatePackList list);

        void startMusicError();

        void startMusicSuccess();
    }

    interface Presenter extends BasePresenter {
        void getData();

        void getRequestProduction();

        void getProduct(int orderId);

        void checkBarcode(String barcode, int orderId, double latitude, double longitude);

        void getListCreateCode(int orderId);

        void deleteItemLog(LogScanCreatePack item);

        void updateNumberInput(int id, int number);

        void deleteAllItemLog();

        int countListScan(int orderId);

    }
}
