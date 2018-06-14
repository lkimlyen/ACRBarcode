package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.OrderACREntity;
import com.demo.architect.data.model.OrderACRResponse;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class GetAllSOACRUsecase extends BaseUseCase {
    private static final String TAG = GetAllSOACRUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public GetAllSOACRUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return remoteRepository.getAllSOACR();
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<OrderACRResponse>() {
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
            public void onNext(OrderACRResponse data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<OrderACREntity> result = data.getList();
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

    }

    public static final class ResponseValue implements ResponseValues {
        private List<OrderACREntity> entity;

        public ResponseValue( List<OrderACREntity> entity) {
            this.entity = entity;
        }

        public  List<OrderACREntity> getEntity() {
            return entity;
        }
    }

    public static final class ErrorValue implements ErrorValues {
        private String description;
        public ErrorValue(String description){
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
