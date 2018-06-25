package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ResultEntity;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class AddLogScanbyJsonUsecase extends BaseUseCase {
    private static final String TAG = AddLogScanbyJsonUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public AddLogScanbyJsonUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        String listDetail = ((RequestValue) requestValues).listDetail;

        return remoteRepository.addLogScanACRByJSON(listDetail);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<BaseListResponse<ResultEntity>>() {
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
            public void onNext(BaseListResponse<ResultEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<ResultEntity> result = data.getList();
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
        private final String listDetail;

        public RequestValue(String listDetail) {
            this.listDetail = listDetail;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private List<ResultEntity> entityList;

        public ResponseValue(List<ResultEntity> entityList) {
            this.entityList = entityList;
        }

        public List<ResultEntity> getEntityList() {
            return entityList;
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
