package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("UserInfo")
    @Expose
    private UserInfo userInfo;

    @SerializedName("Status")
    @Expose
    private int status;

    @SerializedName("Description")
    @Expose
    private String description;

    public class UserInfo{
        @SerializedName("UserID")
        @Expose
        private int userId;

        @SerializedName("Phone")
        @Expose
        private String phone;

        @SerializedName("UserName")
        @Expose
        private String userName;

        @SerializedName("FullName")
        @Expose
        private String fullName;

        @SerializedName("UserRoleID")
        @Expose
        private int userRoleID;

        @SerializedName("UserRoleName")
        @Expose
        private String userRoleName;


    }
    public int getUserId() {
        return userInfo.userId;
    }

    public String getPhone() {
        return userInfo.phone;
    }

    public String getUserName() {
        return userInfo.userName;
    }

    public String getFullName() {
        return userInfo.fullName;
    }

    public int getUserRoleID() {
        return userInfo.userRoleID;
    }

    public String getUserRoleName() {
        return userInfo.userRoleName;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
