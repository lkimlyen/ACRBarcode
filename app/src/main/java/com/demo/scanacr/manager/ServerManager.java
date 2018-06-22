package com.demo.scanacr.manager;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.UserResponse;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.constants.Constants;

public class ServerManager {
    private String server = null;
    private static ServerManager instance;

    public static ServerManager getInstance() {
        if (instance == null) {
            instance = new ServerManager();
        }
        return instance;
    }

    public void setServer(String server) {
        this.server = server;
        SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushString(Constants.KEY_SERVER, server);
    }

    public String getServer() {
        if (server == null) {
            server = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getString(Constants.KEY_SERVER,"");
        }
        return server;
    }

}
