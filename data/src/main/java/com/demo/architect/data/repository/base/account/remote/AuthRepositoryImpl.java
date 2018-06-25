package com.demo.architect.data.repository.base.account.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.UpdateAppResponse;
import com.demo.architect.data.model.UserResponse;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Skull on 04/01/2018.
 */

public class AuthRepositoryImpl implements AuthRepository {
    private final static String TAG = AuthRepositoryImpl.class.getName();
    private AuthApiInterface mRemoteApiInterface;
    private Context context;
    private String server;

    public AuthRepositoryImpl(AuthApiInterface mRemoteApiInterface, Context context) {
        this.mRemoteApiInterface = mRemoteApiInterface;
        this.context = context;
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
    }

    private void handleLoginResponse(Call<UserResponse> call, Subscriber subscriber) {
        try {
            UserResponse response = call.execute().body();
            if (!subscriber.isUnsubscribed()) {
                if (response != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new Exception("Network Error!"));
                }
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }
    }

    private void handleBaseResponse(Call<BaseResponse> call, Subscriber subscriber) {
        try {
            BaseResponse response = call.execute().body();
            if (!subscriber.isUnsubscribed()) {
                if (response != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new Exception("Network Error!"));
                }
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }
    }

    private void handleUpdateResponse(Call<UpdateAppResponse> call, Subscriber subscriber) {
        try {
            UpdateAppResponse response = call.execute().body();
            if (!subscriber.isUnsubscribed()) {
                if (response != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new Exception("Network Error!"));
                }
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }
    }

    private void handleStringResponse(Call<String> call, Subscriber subscriber) {
        try {
            String response = call.execute().body();
            if (!subscriber.isUnsubscribed()) {
                if (response != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new Exception("Network Error!"));
                }
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }
    }

    @Override
    public Observable<UserResponse> login(final String username, final String password, final String type) {
        return Observable.create(new Observable.OnSubscribe<UserResponse>() {
            @Override
            public void call(Subscriber<? super UserResponse> subscriber) {
                handleLoginResponse(mRemoteApiInterface.login(
                        server + "/WS/api/LoginWS"
                        , username, password, type), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> changePassword(final String userId, final String oldPass, final String newPass) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.changePassWord(
                        server + "/WS/api/ChangePassWord"
                        , userId, oldPass, newPass), subscriber);
            }
        });
    }

    @Override
    public Observable<UpdateAppResponse> getUpdateVersionACR() {
        return Observable.create(new Observable.OnSubscribe<UpdateAppResponse>() {
            @Override
            public void call(Subscriber<? super UpdateAppResponse> subscriber) {
                handleUpdateResponse(mRemoteApiInterface.getUpdateVersionACR(server + "/WS/api/GetUpdateVersionACR?pAppCode=ids"), subscriber);
            }
        });
    }

    @Override
    public Observable<String> getDateServer() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                handleStringResponse(mRemoteApiInterface.getDateServer(), subscriber);
            }
        });
    }


}
