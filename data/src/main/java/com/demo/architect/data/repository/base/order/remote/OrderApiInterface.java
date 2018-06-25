package com.demo.architect.data.repository.base.order.remote;


import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ListCodeOutEntityResponse;
import com.demo.architect.data.model.OrderACRResponse;
import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.PackageEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OrderApiInterface {
    @GET
    Call<OrderACRResponse> getAllSOACR(@Url String url);

    @GET
    Call<BaseListResponse<PackageEntity>> getAllPackage(@Url String url);

    @GET
    Call<BaseListResponse<OrderRequestEntity>> getAllRequestACR(@Url String url);

    @GET
    Call<BaseListResponse<PackageEntity>> getAllPackageForRequest(@Url String url, @Query("pRequestacrID") int requestId);


    @GET
    Call<ListCodeOutEntityResponse> getAllScanTurnOutACR(@Url String url, @Query("_pRequestID") int requestId);

    @GET
    Call<BaseResponse> getMaxPackageForSO(@Url String url, @Query("pOrde_ACR_ID") int orderId);

    @FormUrlEncoded
    @POST
    Call<BaseResponse> addPackageACR(
            @Url String url,
            @Field("pOrde_ACR_ID") int orderId,
            @Field("pNo") int stt,
            @Field("pProductID") int productId,
            @Field("pCodeScan") String codeScan,
            @Field("pNumber") int number,
            @Field("pLatGPS") double latitude,
            @Field("pLongGPS") double longitude,
            @Field("pDateDevice") String dateCreate,
            @Field("pUserID") int userId);

    @FormUrlEncoded
    @POST
    Call<BaseResponse> addLogScanInStoreACR(
            @Url String url,
            @Field("pPhone") String phone,
            @Field("pOrderACRID") int orderId,
            @Field("pPackageID") int packageId,
            @Field("pCodeScan") String codeScan,
            @Field("pNumber") int number,
            @Field("pLatGPS") double latitude,
            @Field("pLongGPS") double longitude,
            @Field("pDeviceDateTime") String dateCreate,
            @Field("pUserID") int userId);

    @FormUrlEncoded
    @POST
    Call<BaseResponse> addLogScanACR(
            @Url String url,
            @Field("pPhone") String phone,
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

    @GET
    Call<BaseResponse> getMaxTimesACR(@Url String url, @Query("pRequestID") int requestId);

    @GET
    Call<BaseListResponse<OrderRequestEntity>> getAllRequestINACR(@Url String url);

    @FormUrlEncoded
    @POST
    Call<BaseResponse> deletePackageDetailACR(
            @Url String url,
            @Field("pPackageID") int packageId,
            @Field("pProductID") int productId,
            @Field("pUserID") int userId);

    @FormUrlEncoded
    @POST
    Call<BaseResponse> deletePackageACR(
            @Url String url,
            @Field("pPackageID") int packageId,
            @Field("pUserID") int userId);

}
