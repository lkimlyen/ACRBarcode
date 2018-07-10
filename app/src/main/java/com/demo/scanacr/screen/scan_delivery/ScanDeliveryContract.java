package com.demo.scanacr.screen.scan_delivery;

import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.offline.ScanDeliveryList;
import com.demo.scanacr.app.base.BasePresenter;
import com.demo.scanacr.app.base.BaseView;

import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface ScanDeliveryContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showWarning(String message);

        void showListRequest(List<OrderRequestEntity> list);

        void showListPackage(ScanDeliveryList list);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();
    }

    interface Presenter extends BasePresenter {
        void checkBarcode(int requestId, String barcode, double latitude, double longitude);

        void getRequest();

        void getPackageForRequest(int requestId);

        void getMaxTimes(int requestId, String requestCode);

    }
}
