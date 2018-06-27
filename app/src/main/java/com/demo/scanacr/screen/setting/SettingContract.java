package com.demo.scanacr.screen.setting;

import com.demo.architect.data.model.offline.IPAddress;
import com.demo.scanacr.app.base.BasePresenter;
import com.demo.scanacr.app.base.BaseView;

/**
 * Created by MSI on 26/11/2017.
 */

public interface SettingContract {
    interface View extends BaseView<Presenter> {
        void installApp(String path);
        void showVersion(String version);
        void showIPAddress(IPAddress model);
        void showSuccess(String message);
        void showError(String message);
        void uploadFile(String path, int userId, String userName, String phone);
    }

    interface Presenter extends BasePresenter {
        void updateApp();
        String getVersion();
        void saveIPAddress(String ipAddress, int port);
        void getIPAddress();
        void cloneDataAndSendMail();
    }
}
