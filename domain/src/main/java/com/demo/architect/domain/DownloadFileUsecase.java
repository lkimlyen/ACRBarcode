package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.repository.base.account.remote.AuthRepository;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;

public class DownloadFileUsecase extends BaseUseCase {
    private static final String TAG = DownloadFileUsecase.class.getSimpleName();
    private final AuthRepository remoteRepository;

    public DownloadFileUsecase(AuthRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        String url = ((RequestValue) requestValues).url;
        return remoteRepository.downloadFile(url);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.toString());
                if (useCaseCallback != null) {
                    useCaseCallback.onError(new ErrorValue());
                }
            }

            @Override
            public void onNext(ResponseBody data) {
                //  Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    ResponseBody result = data;
                    if (result != null) {
                        useCaseCallback.onSuccess(new ResponseValue(result));
                    } else {
                        useCaseCallback.onError(new ErrorValue());
                    }
                }
            }
        };
    }

    public static final class RequestValue implements RequestValues {
        private final String url;

        public RequestValue(String url) {
            this.url = url;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private ResponseBody body;

        public ResponseValue(ResponseBody body) {
            this.body = body;
        }

        public ResponseBody getBody() {
            return body;
        }
    }

    public static final class ErrorValue implements ErrorValues {

    }
}
