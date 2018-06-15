package com.demo.architect.data.repository.base.order.remote;


import com.demo.architect.data.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.OrderACRResponse;
import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.PackageEntity;

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
    @GET("http://acctest.imark.com.vn/WS/api/GetAllSOACR")
    Call<OrderACRResponse> getAllSOACR();

    @GET("http://acctest.imark.com.vn/WS/api/GetAllPackage")
    Call<BaseListResponse<PackageEntity>> getAllPackage();

    @GET("http://acctest.imark.com.vn//WS/api/GetAllRequestACR")
    Call<BaseListResponse<OrderRequestEntity>> getAllRequestACR();

    @GET("http://acctest.imark.com.vn//WS/api/GetAllPackageForRequest?pRequestacrID={id}")
    Call<BaseListResponse<PackageEntity>> getAllPackageForRequest(@Path("id") int requestId);

    @GET("http://acctest.imark.com.vn/WS/api/GetMaxPackageForSO")
    Call<BaseResponse> getMaxPackageForSO(@Query("pOrde_ACR_ID") int orderId);

    @FormUrlEncoded
    @POST("http://acctest.imark.com.vn/WS/api/AddPackageACR")
    Call<BaseResponse> addPackageACR(@Field("pRequest_ACR_ID") int requestId,
                                     @Field("pOrde_ACR_ID") int orderId,
                                     @Field("pNo") int stt,
                                     @Field("pProductID") int productId,
                                     @Field("pCodeScan") String codeScan,
                                     @Field("pNumber") int number,
                                     @Field("pLatGPS") float latitude,
                                     @Field("pLongGPS") float longitude,
                                     @Field("pDateDevice") String dateCreate,
                                     @Field("pUserID") int userId);

    @FormUrlEncoded
    @POST("http://acctest.imark.com.vn/WS/api/AddLogScanInStoreACR")
    Call<BaseResponse> addLogScanInStoreACR(@Field("pPhone") String phone,
                                            @Field("pOrderACRID") int orderId,
                                            @Field("pPackageID") int packageId,
                                            @Field("pCodeScan") String codeScan,
                                            @Field("pNumber") int number,
                                            @Field("pLatGPS") float latitude,
                                            @Field("pLongGPS") float longitude,
                                            @Field("pDeviceDateTime") String dateCreate,
                                            @Field("pUserID") int userId);

    @FormUrlEncoded
    @POST("http://acctest.imark.com.vn/WS/api/AddLogScanACR")
    Call<BaseResponse> addLogScanACR(@Field("pPhone") String phone,
                                     @Field("pOrderACRID") int orderId,
                                     @Field("pPackageID") int packageId,
                                     @Field("pCodeScan") String codeScan,
                                     @Field("pNumber") int number,
                                     @Field("pLatGPS") float latitude,
                                     @Field("pLongGPS") float longitude,
                                     @Field("Activity") String activity,
                                     @Field("Times") int times,
                                     @Field("pDeviceDateTime") String dateCreate,
                                     @Field("pUserID") int userId,
                                     @Field("pRequestACRID") int requestId);

    @GET("http://acctest.imark.com.vn/WS/api/GetMaxTimesACR?pRequestID={requestId}")
    Call<BaseResponse> getMaxTimesACR(@Path("requestId") int requestId);

    @GET("http://acctest.imark.com.vn//WS/api/GetAllRequestINACR")
    Call<BaseListResponse<OrderRequestEntity>> getAllRequestINACR();
}
