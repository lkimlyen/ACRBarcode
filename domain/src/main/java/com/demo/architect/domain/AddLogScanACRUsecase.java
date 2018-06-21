package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import rx.Observable;
import rx.Subscriber;

public class AddLogScanACRUsecase extends BaseUseCase {
    private static final String TAG = AddLogScanACRUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public AddLogScanACRUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {

        String phone = ((RequestValue) requestValues).phone;
        int orderId = ((RequestValue) requestValues).orderId;
        int packageId = ((RequestValue) requestValues).packageId;
        String codeScan = ((RequestValue) requestValues).codeScan;
        int number = ((RequestValue) requestValues).number;
        double latitude = ((RequestValue) requestValues).latitude;
        double longitude = ((RequestValue) requestValues).longitude;
        String activity = ((RequestValue) requestValues).activity;
        int times = ((RequestValue) requestValues).times;
        String dateCreate = ((RequestValue) requestValues).dateCreate;
        int userId = ((RequestValue) requestValues).userId;
        int requestId = ((RequestValue) requestValues).requestId;

        return remoteRepository.addLogScanACR(phone, orderId, packageId, codeScan, number,
                latitude, longitude, activity, times, dateCreate, userId, requestId);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<BaseResponse>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.toString());
                if (useCaseCallback != null) {
                    useCaseCallback.onError(new ErrorValue(e.toString()));
                }
            }

            @Override
            public void onNext(BaseResponse data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    int result = data.getID();
                    if (data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue(result, data.getCodeScan()));
                    } else {
                        useCaseCallback.onError(new ErrorValue(data.getDescription()));
                    }
                }
            }
        };
    }

    public static final class RequestValue implements RequestValues {
        private final String phone;
        private final int orderId;
        private final int packageId;
        private final String codeScan;
        private final int number;
        private final double latitude;
        private final double longitude;
        private final String activity;
        private final int times;
        private final String dateCreate;
        private final int userId;
        private final int requestId;

        public RequestValue(String phone, int orderId, int packageId, String codeScan,
                            int number, double latitude, double longitude, String activity, int times, String dateCreate, int userId, int requestId) {
            this.phone = phone;
            this.orderId = orderId;
            this.packageId = packageId;
            this.codeScan = codeScan;
            this.number = number;
            this.latitude = latitude;
            this.longitude = longitude;
            this.activity = activity;
            this.times = times;
            this.dateCreate = dateCreate;
            this.userId = userId;
            this.requestId = requestId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private int id;
        private String barcode;

        public ResponseValue(int id, String barcode) {
            this.id = id;
            this.barcode = barcode;
        }

        public int getId() {
            return id;
        }

        public String getBarcode() {
            return barcode;
        }
    }

    public static final class ErrorValue implements ErrorValues {
        private String description;

        public ErrorValue(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
