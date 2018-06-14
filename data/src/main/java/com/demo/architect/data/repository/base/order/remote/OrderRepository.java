package com.demo.architect.data.repository.base.order.remote;

import com.demo.architect.data.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.OrderACRResponse;
import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.PackageEntity;

import rx.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OrderRepository {
    Observable<OrderACRResponse> getAllSOACR();

    Observable<BaseListResponse<PackageEntity>> getAllPackage();

    Observable<BaseListResponse<OrderRequestEntity>> getAllRequestACR();

    Observable<BaseListResponse<PackageEntity>> getAllPackageForRequest(int requestId);

    Observable<BaseResponse> getMaxPackageForSO(int requestId, int orderId);

    Observable<BaseResponse> addPackageACR(int requestId, int orderId,
                                           int stt, int productId, String codeScan,
                                           int number, float latitude, float longitude,
                                           String dateCreate, int userId);

    Observable<BaseResponse> addLogScanInStoreACR(String phone, int orderId, int packageId,
                                                  String codeScan, int number, float latitude,
                                                  float longitude, String dateCreate, int userId);

    Observable<BaseResponse> addLogScanACR(String phone, int orderId, int packageId,
                                           String codeScan, int number, float latitude,
                                           float longitude, String activity, int times,
                                           String dateCreate, int userId, int requestId);

    Observable<BaseResponse> getMaxTimesACR(int requestId);

    Observable<BaseListResponse<OrderRequestEntity>> getAllRequestINACR();
}
