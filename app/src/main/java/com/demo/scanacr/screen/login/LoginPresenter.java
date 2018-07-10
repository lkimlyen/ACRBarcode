package com.demo.scanacr.screen.login;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.LoginUsecase;
import com.demo.architect.domain.UpdateSoftUsecase;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.manager.ServerManager;
import com.demo.scanacr.manager.UserManager;
import com.demo.scanacr.util.ConvertUtils;
import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private final String TAG = LoginPresenter.class.getName();
    private final LoginContract.View view;
    private final LoginUsecase loginUsecase;
    private final UpdateSoftUsecase updateSoftUsecase;

    @Inject
    LocalRepository localRepository;

    @Inject
    LoginPresenter(@NonNull LoginContract.View view,
                   LoginUsecase loginUsecase, UpdateSoftUsecase updateSoftUsecase) {
        this.view = view;
        this.loginUsecase = loginUsecase;
        this.updateSoftUsecase = updateSoftUsecase;
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
               boolean aBoolean =  SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getBoolean(Constants.KEY_UPLOAD, false);
               if (!aBoolean){
                   uploadVersionApp();
               }else {
                   view.hideProgressBar();
                   view.startDashboardActivity();
               }

            }

            @Override
            public void onError(LoginUsecase.ErrorValue errorResponse) {
                view.hideProgressBar();
                String error = "";
                if(errorResponse.getDescription().contains(
                        CoreApplication.getInstance().getString(R.string.text_error_network_host))){
                    error = CoreApplication.getInstance().getString(R.string.text_error_network);
                }else {
                    error = errorResponse.getDescription();
                }
                view.loginError(error);
            }
        });
    }

    @Override
    public void saveServer(String server) {
        ServerManager.getInstance().setServer(server);
    }

    private void uploadVersionApp() {
        PackageManager manager = CoreApplication.getInstance().getPackageManager();
        PackageInfo info;
        String version = "";
        try {
            info = manager.getPackageInfo(
                    CoreApplication.getInstance().getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String phone = Settings.Secure.getString(CoreApplication.getInstance().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        updateSoftUsecase.executeIO(new UpdateSoftUsecase.RequestValue(UserManager.getInstance().getUser().getUserId(),
                        version, 0, ConvertUtils.getDateTimeCurrent(), phone),
                new BaseUseCase.UseCaseCallback<UpdateSoftUsecase.ResponseValue, UpdateSoftUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(UpdateSoftUsecase.ResponseValue successResponse) {
                        SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushBoolean(Constants.KEY_UPLOAD, true);
                        view.hideProgressBar();
                        view.startDashboardActivity();

                    }

                    @Override
                    public void onError(UpdateSoftUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.startDashboardActivity();
                    }
                });
    }
}
