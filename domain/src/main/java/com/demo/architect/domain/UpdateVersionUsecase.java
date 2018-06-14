package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.UpdateAppResponse;
import com.demo.architect.data.repository.base.account.remote.AuthRepository;

import rx.Observable;
import rx.Subscriber;

public class UpdateVersionUsecase extends BaseUseCase {
    private static final String TAG = UpdateVersionUsecase.class.getSimpleName();
    private final AuthRepository remoteRepository;

    public UpdateVersionUsecase(AuthRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return remoteRepository.getUpdateVersionACR();
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<UpdateAppResponse>() {
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
            public void onNext(UpdateAppResponse data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    String result = data.getLink();
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
        private String link;

        public ResponseValue(String link) {
            this.link = link;
        }

        public String getLink() {
            return link;
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
