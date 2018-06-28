package com.demo.scanacr.screen.import_works;

import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.offline.ImportWorksModel;
import com.demo.architect.data.model.offline.ScanDeliveryList;
import com.demo.scanacr.app.base.BasePresenter;
import com.demo.scanacr.app.base.BaseView;

import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface ImportWorksContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showListRequest(List<OrderRequestEntity> list);

       void showListPackage(ImportWorksModel model);
        void startMusicError();

        void startMusicSuccess();
    }

    interface Presenter extends BasePresenter {
        void checkBarcode(int requestId, String barcode, double latitude, double longitude);

        void getRequest();

        void getCodeScan(int requestId);
    }
}
