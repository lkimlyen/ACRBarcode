package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class GetMaxPackageForSOUsecase extends BaseUseCase {
    private static final String TAG = GetMaxPackageForSOUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public GetMaxPackageForSOUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        int requestId = ((RequestValue) requestValues).requestId;
        int orderId = ((RequestValue) requestValues).orderId;
        return remoteRepository.getMaxPackageForSO(requestId, orderId);
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
                    int result = data.getNumber();
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

        public RequestValue(int requestId, int orderId) {
            this.requestId = requestId;
            this.orderId = orderId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private int number;

        public ResponseValue(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
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
