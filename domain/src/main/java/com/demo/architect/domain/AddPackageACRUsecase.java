package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class AddPackageACRUsecase extends BaseUseCase {
    private static final String TAG = AddPackageACRUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public AddPackageACRUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        int requestId = ((RequestValue) requestValues).requestId;
        int orderId = ((RequestValue) requestValues).orderId;
        int stt = ((RequestValue) requestValues).stt;
        int productId = ((RequestValue) requestValues).productId;
        String codeScan = ((RequestValue) requestValues).codeScan;
        int number = ((RequestValue) requestValues).number;
        float latitude = ((RequestValue) requestValues).latitude;
        float longitude = ((RequestValue) requestValues).longitude;
        String dateCreate = ((RequestValue) requestValues).dateCreate;
        int userId = ((RequestValue) requestValues).userId;

        return remoteRepository.addPackageACR(requestId, orderId, stt, productId, codeScan, number,
                latitude, longitude, dateCreate, userId);
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
                        useCaseCallback.onSuccess(new ResponseValue(result));
                    } else {
                        useCaseCallback.onError(new ErrorValue(data.getDescription()));
                    }
                }
            }
        };
    }

    public static final class RequestValue implements RequestValues {
        private final int requestId;
        private final int orderId;
        private final int stt;
        private final int productId;
        private final String codeScan;
        private final int number;
        private final float latitude;
        private final float longitude;
        private final String dateCreate;
        private final int userId;

        public RequestValue(int requestId, int orderId, int stt, int productId, String codeScan,
                            int number, float latitude, float longitude, String dateCreate, int userId) {
            this.requestId = requestId;
            this.orderId = orderId;
            this.stt = stt;
            this.productId = productId;
            this.codeScan = codeScan;
            this.number = number;
            this.latitude = latitude;
            this.longitude = longitude;
            this.dateCreate = dateCreate;
            this.userId = userId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private int id;

        public ResponseValue(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
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
