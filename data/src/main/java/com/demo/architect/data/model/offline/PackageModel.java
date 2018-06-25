package com.demo.architect.data.model.offline;

import retrofit2.http.Field;

public class PackageModel {
    private  int Order_ACR_ID;
    private  int No;
    private  int ProductID;
    private  String CodeScan;
    private  int Number;
    private  double LatGPS;
    private  double LongGPS ;
    private  String DateDevice;
    private  int UserID;

    public PackageModel(int order_ACR_ID, int no, int productID, String codeScan, int number, double latGPS, double longGPS, String dateDevice, int userID) {
        Order_ACR_ID = order_ACR_ID;
        No = no;
        ProductID = productID;
        CodeScan = codeScan;
        Number = number;
        LatGPS = latGPS;
        LongGPS = longGPS;
        DateDevice = dateDevice;
        UserID = userID;
    }
}
