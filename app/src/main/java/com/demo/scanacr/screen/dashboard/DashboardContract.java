package com.demo.scanacr.screen.dashboard;

import com.demo.architect.data.model.UserResponse;
import com.demo.scanacr.app.base.BasePresenter;
import com.demo.scanacr.app.base.BaseView;

/**
 * Created by MSI on 26/11/2017.
 */

public interface DashboardContract {
    interface View extends BaseView<Presenter> {
        void showUser(UserResponse user);
        void showDeliveryNotComplete(int count);
    }

    interface Presenter extends BasePresenter {
        void getUser();
        void logout();
        void countDeliveryNotComplete();
    }
}
