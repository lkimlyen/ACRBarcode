package com.demo.scanacr.screen.history_pack;

import com.demo.architect.data.model.offline.LogCompleteMainList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.scanacr.app.base.BasePresenter;
import com.demo.scanacr.app.base.BaseView;

import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface HistoryPackageContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);
        void showRequestProduction(List<OrderModel> list);
        void showListHistory(LogCompleteMainList list);
    }

    interface Presenter extends BasePresenter {
        void getRequestProduce();
        void search(int orderId);
    }
}
