package com.demo.architect.data.model.offline;

import android.app.Activity;

public class DeliveryModel {
    private String CodeScan;
    private String DeviceDateTime;
    private Double LatGPS;
    private Double LongGPS;
    private String Phone;
    private int Number;
    private int OrderACRID;
    private int PackageID;
    private  String Activity = "OUT";
    private int Times;
    private int RequestACRID;
    private int UserID;

    public DeliveryModel(String codeScan, String deviceDateTime, Double latGPS, Double longGPS, String phone, int number, int orderACRID, int packageID, int times, int requestACRID, int userID) {
        CodeScan = codeScan;
        DeviceDateTime = deviceDateTime;
        LatGPS = latGPS;
        LongGPS = longGPS;
        Phone = phone;
        Number = number;
        OrderACRID = orderACRID;
        PackageID = packageID;
        Times = times;
        RequestACRID = requestACRID;
        UserID = userID;
    }
}
