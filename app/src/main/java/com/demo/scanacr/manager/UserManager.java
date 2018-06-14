package com.demo.scanacr.manager;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.UserResponse;
import com.demo.scanacr.app.CoreApplication;

public class UserManager {
    private UserResponse userEntity;
    private static UserManager instance;

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void setUser(UserResponse user) {
        userEntity = user;
        SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushUserObject(userEntity);
    }

    public UserResponse getUser() {
        if (userEntity == null) {
            userEntity = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getUserObject();
        }
        return userEntity;
    }

}
