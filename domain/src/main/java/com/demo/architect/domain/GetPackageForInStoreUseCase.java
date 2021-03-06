package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class GetPackageForInStoreUseCase extends BaseUseCase {
    private static final String TAG = GetPackageForInStoreUseCase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public GetPackageForInStoreUseCase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        int orderId = ((RequestValue) requestValues).orderId;
        String codeProduce = ((RequestValue) requestValues).codeProduce;
        return remoteRepository.getPackageForInStore(orderId,codeProduce);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<BaseListResponse<PackageEntity>>() {
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
            public void onNext(BaseListResponse<PackageEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<PackageEntity> result = data.getList();
                    if (result != null && data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue(result));
                    } else {
                        useCaseCallback.onError(new ErrorValue(data.getDescription()));
                    }
                }
            }
        };
    }

    public static final class RequestValue implements RequestValues {
        private final int orderId;
        private final String codeProduce;

        public RequestValue(int orderId, String codeProduce) {
            this.orderId = orderId;
            this.codeProduce = codeProduce;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private List<PackageEntity> entity;

        public ResponseValue(List<PackageEntity> entity) {
            this.entity = entity;
        }

        public List<PackageEntity> getEntity() {
            return entity;
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
