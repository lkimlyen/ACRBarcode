package com.demo.architect.data.repository.base.order.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ListCodeOutEntityResponse;
import com.demo.architect.data.model.OrderACRResponse;
import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ResultEntity;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Skull on 04/01/2018.
 */

public class OrderRepositoryImpl implements OrderRepository {
    private final static String TAG = OrderRepositoryImpl.class.getName();

    private OrderApiInterface mRemoteApiInterface;
    private Context context;
    private String server;

    public OrderRepositoryImpl(OrderApiInterface mRemoteApiInterface, Context context) {
        this.mRemoteApiInterface = mRemoteApiInterface;
        this.context = context;
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

    private void handleResultResponse(Call<BaseListResponse<ResultEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<ResultEntity> response = call.execute().body();
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

        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<OrderACRResponse>() {
            @Override
            public void call(Subscriber<? super OrderACRResponse> subscriber) {
                handleOrderResponse(mRemoteApiInterface.getAllSOACR(server + "/WS/api/GetAllSOACR"), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<PackageEntity>> getAllPackage() {

        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<PackageEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<PackageEntity>> subscriber) {
                handlePackageResponse(mRemoteApiInterface.getAllPackage(server + "/WS/api/GetAllPackage"), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<OrderRequestEntity>> getAllRequestACR() {

        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<OrderRequestEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<OrderRequestEntity>> subscriber) {
                handleOrderRequestResponse(mRemoteApiInterface.getAllRequestACR(server + "/WS/api/GetAllRequestACR"), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<PackageEntity>> getAllPackageForRequest(final int requestId) {

        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<PackageEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<PackageEntity>> subscriber) {
                handlePackageResponse(mRemoteApiInterface.getAllPackageForRequest(
                        server + "/WS/api/GetAllPackageForRequest",
                        requestId), subscriber);
            }
        });
    }

    @Override
    public Observable<ListCodeOutEntityResponse> getAllScanTurnOutACR(final int requestId) {

        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<ListCodeOutEntityResponse>() {
            @Override
            public void call(Subscriber<? super ListCodeOutEntityResponse> subscriber) {
                handleCodeOutResponse(mRemoteApiInterface.getAllScanTurnOutACR(
                        server + "/WS/api/GetAllScanTurnOutACR", requestId), subscriber);
            }
        });
    }


    @Override
    public Observable<BaseResponse> getMaxPackageForSO(final int orderId) {

        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.getMaxPackageForSO(
                        server + "/WS/api/GetMaxPackageForSO", orderId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> addPackageACR(final int orderId, final int stt, final
    int productId, final String codeScan, final int number, final double latitude, final double longitude,
                                                  final String dateCreate, final int userId) {

        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.addPackageACR(server + "/WS/api/AddPackageACR",
                        orderId, stt, productId, codeScan,
                        number, latitude, longitude, dateCreate, userId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> addPackageACRByJSON(final String listdetail) {

        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.addPackageACRByJSON(server + "/WS/api/AddPackageACRByJSON",
                        listdetail), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<ResultEntity>> addLogScanACRByJSON(final String listLog) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<ResultEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<ResultEntity>> subscriber) {
                handleResultResponse(mRemoteApiInterface.addLogScanACRByJSON(server + "/WS/api/AddLogScanACRByJSON",
                        listLog), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> addLogScanInStoreACR(final String phone, final int orderId, final int packageId, final String codeScan,
                                                         final int number, final double latitude, final double longitude, final String dateCreate,
                                                         final int userId) {

        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.addLogScanInStoreACR(
                        server + "/WS/api/AddLogScanInStoreACR"
                        , phone, orderId, packageId, codeScan,
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
                handleBaseResponse(mRemoteApiInterface.addLogScanACR(
                        server + "/WS/api/AddLogScanACR"
                        , phone, orderId, packageId, codeScan,
                        number, latitude, longitude, activity, times, dateCreate, userId, requestId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> getMaxTimesACR(final int requestId) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.getMaxTimesACR(
                        server + "/WS/api/GetMaxTimesACR",
                        requestId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<OrderRequestEntity>> getAllRequestINACR() {
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<OrderRequestEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<OrderRequestEntity>> subscriber) {
                handleOrderRequestResponse(mRemoteApiInterface.getAllRequestINACR(server + "/WS/api/GetAllRequestINACR"), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> deletePackageDetailACR(final int packageId, final int productId, final int userId) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.deletePackageDetailACR(
                        server + "/WS/api/DeletePackageDetailACR",
                        packageId, productId, userId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> deletePackage(final int packageID, final int userId) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.deletePackageACR(
                        server + "/WS/api/DeletePackageACR",
                        packageID, userId), subscriber);
            }
        });
    }
}
