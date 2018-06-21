package com.demo.scanacr.screen.scan_warehousing;

import com.demo.architect.data.model.offline.ScanWarehousingModel;
import com.demo.scanacr.app.base.BasePresenter;
import com.demo.scanacr.app.base.BaseView;

/**
 * Created by MSI on 26/11/2017.
 */

public interface ScanWarehousingContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showListScanWarehousing(ScanWarehousingModel item);
    }

    interface Presenter extends BasePresenter {
        void checkBarcode(String barcode, double latitude, double longitude);

        void getPackage();
    }
}
