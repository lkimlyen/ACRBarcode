package com.demo.architect.data.repository.base.order.remote;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ListCodeOutEntityResponse;
import com.demo.architect.data.model.OrderACRResponse;
import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ProductEntity;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Skull on 04/01/2018.
 */

public class OrderRepositoryImpl implements OrderRepository {
    private final static String TAG = OrderRepositoryImpl.class.getName();

    private OrderApiInterface mRemoteApiInterface;

    public OrderRepositoryImpl(OrderApiInterface mRemoteApiInterface) {
        this.mRemoteApiInterface = mRemoteApiInterface;
    }

    private void handleOrderResponse(Call<OrderACRResponse> call, Subscriber subscriber) {
        try {
            OrderACRResponse response = call.execute().body();
            if (!subscriber.isUnsubscribed()) {
                if (response != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new Exception("Network Error!"));
                }
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }
    }

    private void handleBaseResponse(Call<BaseResponse> call, Subscriber subscriber) {
        try {
            BaseResponse response = call.execute().body();
            if (!subscriber.isUnsubscribed()) {
                if (response != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new Exception("Network Error!"));
                }
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }
    }

    private void handlePackageResponse(Call<BaseListResponse<PackageEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<PackageEntity> response = call.execute().body();
            if (!subscriber.isUnsubscribed()) {
                if (response != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new Exception("Network Error!"));
                }
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }
    }

    private void handleCodeOutResponse(Call<ListCodeOutEntityResponse> call, Subscriber subscriber) {
        try {
            ListCodeOutEntityResponse response = call.execute().body();
            if (!subscriber.isUnsubscribed()) {
                if (response != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new Exception("Network Error!"));
                }
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }
    }

    private void handleOrderRequestResponse(Call<BaseListResponse<OrderRequestEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<OrderRequestEntity> response = call.execute().body();
            if (!subscriber.isUnsubscribed()) {
                if (response != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new Exception("Network Error!"));
                }
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }
    }


    @Override
    public Observable<OrderACRResponse> getAllSOACR() {
        return Observable.create(new Observable.OnSubscribe<OrderACRResponse>() {
            @Override
            public void call(Subscriber<? super OrderACRResponse> subscriber) {
                handleOrderResponse(mRemoteApiInterface.getAllSOACR(), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<PackageEntity>> getAllPackage() {
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<PackageEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<PackageEntity>> subscriber) {
                handlePackageResponse(mRemoteApiInterface.getAllPackage(), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<OrderRequestEntity>> getAllRequestACR() {
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<OrderRequestEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<OrderRequestEntity>> subscriber) {
                handleOrderRequestResponse(mRemoteApiInterface.getAllRequestACR(), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<PackageEntity>> getAllPackageForRequest(final int requestId) {
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<PackageEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<PackageEntity>> subscriber) {
                handlePackageResponse(mRemoteApiInterface.getAllPackageForRequest(requestId), subscriber);
            }
        });
    }

    @Override
    public Observable<ListCodeOutEntityResponse> getAllScanTurnOutACR(final int requestId) {
        return Observable.create(new Observable.OnSubscribe<ListCodeOutEntityResponse>() {
            @Override
            public void call(Subscriber<? super ListCodeOutEntityResponse> subscriber) {
                handleCodeOutResponse(mRemoteApiInterface.getAllScanTurnOutACR(requestId), subscriber);
            }
        });
    }


    @Override
    public Observable<BaseResponse> getMaxPackageForSO(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.getMaxPackageForSO(orderId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> addPackageACR(final int orderId, final int stt, final
    int productId, final String codeScan, final int number, final double latitude, final double longitude,
                                                  final String dateCreate, final int userId) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.addPackageACR(orderId, stt, productId, codeScan,
                        number, latitude, longitude, dateCreate, userId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> addLogScanInStoreACR(final String phone, final int orderId, final int packageId, final String codeScan,
                                                         final int number, final double latitude, final double longitude, final String dateCreate,
                                                         final int userId) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.addLogScanInStoreACR(phone, orderId, packageId, codeScan,
                        number, latitude, longitude, dateCreate, userId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> addLogScanACR(final String phone, final int orderId, final int packageId,
                                                  final String codeScan, final int number, final double latitude,
                                                  final double longitude, final String activity, final int times,
                                                  final String dateCreate, final int userId, final int requestId) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.addLogScanACR(phone, orderId, packageId, codeScan,
                        number, latitude, longitude, activity, times, dateCreate, userId, requestId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> getMaxTimesACR(final int requestId) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.getMaxTimesACR(requestId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<OrderRequestEntity>> getAllRequestINACR() {
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<OrderRequestEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<OrderRequestEntity>> subscriber) {
                handleOrderRequestResponse(mRemoteApiInterface.getAllRequestINACR(), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> deletePackageDetailACR(final int packageId, final int productId, final int userId) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.deletePackageDetailACR(packageId, productId, userId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> deletePackage(final int packageID, final int userId) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.deletePackageACR(packageID, userId), subscriber);
            }
        });
    }
}
