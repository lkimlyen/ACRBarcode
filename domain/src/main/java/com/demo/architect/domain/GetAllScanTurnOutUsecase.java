package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.CodeOutEntity;
import com.demo.architect.data.model.ListCodeOutEntityResponse;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class GetAllScanTurnOutUsecase extends BaseUseCase {
    private static final String TAG = GetAllScanTurnOutUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public GetAllScanTurnOutUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        int requestId = ((RequestValue) requestValues).requestId;
        return remoteRepository.getAllPackageForRequest(requestId);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<ListCodeOutEntityResponse>() {
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
            public void onNext(ListCodeOutEntityResponse data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<CodeOutEntity> result = data.getList();
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
        private final int requestId;

        public RequestValue(int requestId) {
            this.requestId = requestId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private List<CodeOutEntity> entity;

        public ResponseValue(List<CodeOutEntity> entity) {
            this.entity = entity;
        }

        public List<CodeOutEntity> getEntity() {
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
