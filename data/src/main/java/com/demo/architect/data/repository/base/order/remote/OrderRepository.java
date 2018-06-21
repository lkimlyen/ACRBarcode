package com.demo.architect.data.repository.base.order.remote;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ListCodeOutEntityResponse;
import com.demo.architect.data.model.OrderACRResponse;
import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ProductEntity;

import rx.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OrderRepository {
    Observable<OrderACRResponse> getAllSOACR();

    Observable<BaseListResponse<PackageEntity>> getAllPackage();

    Observable<BaseListResponse<OrderRequestEntity>> getAllRequestACR();

    Observable<BaseListResponse<PackageEntity>> getAllPackageForRequest(int requestId);

    Observable<ListCodeOutEntityResponse> getAllScanTurnOutACR(int requestId);

    Observable<BaseResponse> getMaxPackageForSO(int orderId);

    Observable<BaseResponse> addPackageACR(int orderId,int stt, int productId, String codeScan,
                                           int number, double latitude, double longitude,
                                           String dateCreate, int userId);

    Observable<BaseResponse> addLogScanInStoreACR(String phone, int orderId, int packageId,
                                                  String codeScan, int number, double latitude,
                                                  double longitude, String dateCreate, int userId);

    Observable<BaseResponse> addLogScanACR(String phone, int orderId, int packageId,
                                           String codeScan, int number, double latitude,
                                           double longitude, String activity, int times,
                                           String dateCreate, int userId, int requestId);

    Observable<BaseResponse> getMaxTimesACR(int requestId);

    Observable<BaseListResponse<OrderRequestEntity>> getAllRequestINACR();
    Observable<BaseResponse> deletePackageDetailACR(int packageID,int productId, int userId);

    Observable<BaseResponse> deletePackage(int packageID,int userId);

}
