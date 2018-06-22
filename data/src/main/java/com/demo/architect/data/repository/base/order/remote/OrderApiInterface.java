package com.demo.architect.data.repository.base.order.remote;


import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.CodeOutEntity;
import com.demo.architect.data.model.ListCodeOutEntityResponse;
import com.demo.architect.data.model.OrderACRResponse;
import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ProductEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OrderApiInterface {
    @GET("/WS/api/GetAllSOACR")
    Call<OrderACRResponse> getAllSOACR();

    @GET("/WS/api/GetAllPackage")
    Call<BaseListResponse<PackageEntity>> getAllPackage();

    @GET("/WS/api/GetAllRequestACR")
    Call<BaseListResponse<OrderRequestEntity>> getAllRequestACR();

    @GET("/WS/api/GetAllPackageForRequest")
    Call<BaseListResponse<PackageEntity>> getAllPackageForRequest(@Query("pRequestacrID") int requestId);


    @GET("/WS/api/GetAllScanTurnOutACR")
    Call<ListCodeOutEntityResponse> getAllScanTurnOutACR(@Query("_pRequestID") int requestId);

    @GET("/WS/api/GetMaxPackageForSO")
    Call<BaseResponse> getMaxPackageForSO(@Query("pOrde_ACR_ID") int orderId);

    @FormUrlEncoded
    @POST("/WS/api/AddPackageACR")
    Call<BaseResponse> addPackageACR(@Field("pOrde_ACR_ID") int orderId,
                                     @Field("pNo") int stt,
                                     @Field("pProductID") int productId,
                                     @Field("pCodeScan") String codeScan,
                                     @Field("pNumber") int number,
                                     @Field("pLatGPS") double latitude,
                                     @Field("pLongGPS") double longitude,
                                     @Field("pDateDevice") String dateCreate,
                                     @Field("pUserID") int userId);

    @FormUrlEncoded
    @POST("/WS/api/AddLogScanInStoreACR")
    Call<BaseResponse> addLogScanInStoreACR(@Field("pPhone") String phone,
                                            @Field("pOrderACRID") int orderId,
                                            @Field("pPackageID") int packageId,
                                            @Field("pCodeScan") String codeScan,
                                            @Field("pNumber") int number,
                                            @Field("pLatGPS") double latitude,
                                            @Field("pLongGPS") double longitude,
                                            @Field("pDeviceDateTime") String dateCreate,
                                            @Field("pUserID") int userId);

    @FormUrlEncoded
    @POST("/WS/api/AddLogScanACR")
    Call<BaseResponse> addLogScanACR(@Field("pPhone") String phone,
                                     @Field("pOrderACRID") int orderId,
                                     @Field("pPackageID") int packageId,
                                     @Field("pCodeScan") String codeScan,
                                     @Field("pNumber") int number,
                                     @Field("pLatGPS") double latitude,
                                     @Field("pLongGPS") double longitude,
                                     @Field("pActivity") String activity,
                                     @Field("pTimes") int times,
                                     @Field("pDeviceDateTime") String dateCreate,
                                     @Field("pUserID") int userId,
                                     @Field("pRequestACRID") int requestId);

    @GET("/WS/api/GetMaxTimesACR")
    Call<BaseResponse> getMaxTimesACR(@Query("pRequestID") int requestId);

    @GET("/WS/api/GetAllRequestINACR")
    Call<BaseListResponse<OrderRequestEntity>> getAllRequestINACR();

    @FormUrlEncoded
    @POST("/WS/api/DeletePackageDetailACR")
    Call<BaseResponse> deletePackageDetailACR(@Field("pPackageID") int packageId,
                                     @Field("pProductID") int productId,
                                     @Field("pUserID") int userId);

    @FormUrlEncoded
    @POST("/WS/api/DeletePackageACR")
    Call<BaseResponse> deletePackageACR(@Field("pPackageID") int packageId,
                                              @Field("pUserID") int userId);

}
