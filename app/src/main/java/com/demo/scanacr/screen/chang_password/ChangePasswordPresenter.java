package com.demo.scanacr.screen.chang_password;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.ChangePasswordUsecase;
import com.demo.scanacr.manager.UserManager;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class ChangePasswordPresenter implements ChangePasswordContract.Presenter {

    private final String TAG = ChangePasswordPresenter.class.getName();
    private final ChangePasswordContract.View view;
    private final ChangePasswordUsecase changePasswordUsecase;

    @Inject
    LocalRepository localRepository;

    @Inject
    ChangePasswordPresenter(@NonNull ChangePasswordContract.View view,
                            ChangePasswordUsecase changePasswordUsecase) {
        this.view = view;
        this.changePasswordUsecase = changePasswordUsecase;
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
    public void changePass(String oldPass, String newPass) {
        view.showProgressBar();
        int userId = UserManager.getInstance().getUser().getUserId();
        changePasswordUsecase.executeIO(new ChangePasswordUsecase.RequestValue(userId, oldPass, newPass),
                new BaseUseCase.UseCaseCallback<ChangePasswordUsecase.ResponseValue,
                        ChangePasswordUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(ChangePasswordUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        view.changePassSuccess();
                    }

                    @Override
                    public void onError(ChangePasswordUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.changeError(errorResponse.getDescription());
                    }
                });
    }
}
