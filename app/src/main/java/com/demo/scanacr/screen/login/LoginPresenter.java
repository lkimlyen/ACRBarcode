package com.demo.scanacr.screen.login;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.LoginUsecase;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.manager.UserManager;
import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private final String TAG = LoginPresenter.class.getName();
    private final LoginContract.View view;
    private final LoginUsecase loginUsecase;

    @Inject
    LocalRepository localRepository;

    @Inject
    LoginPresenter(@NonNull LoginContract.View view,
                   LoginUsecase loginUsecase) {
        this.view = view;
        this.loginUsecase = loginUsecase;
    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");


    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void login(String username, String password) {

        view.showProgressBar();
        loginUsecase.executeIO(new LoginUsecase.RequestValue(username, password), new BaseUseCase.UseCaseCallback
                <LoginUsecase.ResponseValue, LoginUsecase.ErrorValue>() {
            @Override
            public void onSuccess(LoginUsecase.ResponseValue successResponse) {
                Log.d(TAG, new Gson().toJson(successResponse.getEntity()));
                //Save user entity to shared preferences
                UserManager.getInstance().setUser(successResponse.getEntity());
                view.hideProgressBar();
                view.startDashboardActivity();
            }

            @Override
            public void onError(LoginUsecase.ErrorValue errorResponse) {
                view.hideProgressBar();
                view.loginError(errorResponse.getDescription());
            }
        });
    }


}
