package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.repository.base.account.remote.AuthRepository;

import rx.Observable;
import rx.Subscriber;

public class ChangePasswordUsecase extends BaseUseCase {
    private static final String TAG = ChangePasswordUsecase.class.getSimpleName();
    private final AuthRepository remoteRepository;

    public ChangePasswordUsecase(AuthRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        int userId = ((RequestValue) requestValues).userId;
        String oldPass = ((RequestValue) requestValues).oldPass;
        String newPass = ((RequestValue) requestValues).newPass;
        return remoteRepository.changePassword(userId+"", oldPass, newPass);
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
                    if (data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue(data.getDescription()));
                    } else {
                        useCaseCallback.onError(new ErrorValue(data.getDescription()));
                    }
                }
            }
        };
    }

    public static final class RequestValue implements RequestValues {
        public final int userId;
        public final String oldPass;
        public final String newPass;

        public RequestValue(int userId, String oldPass, String newPass) {
            this.userId = userId;
            this.oldPass = oldPass;
            this.newPass = newPass;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private String description;
        public ResponseValue(String description){
            this.description = description;
        }

        public String getDescription() {
            return description;
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
