package com.demo.scanacr.screen.login;

import com.demo.scanacr.app.base.BasePresenter;
import com.demo.scanacr.app.base.BaseView;

/**
 * Created by MSI on 26/11/2017.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void loginError(String content);
        void startDashboardActivity();
    }

    interface Presenter extends BasePresenter {
        void login(String phone, String password);
        void saveServer(String server);

    }
}
