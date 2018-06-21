package com.demo.scanacr.screen.import_works;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.CodeOutEntity;
import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.AddLogScanACRUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetAllRequestACRInUsecase;
import com.demo.architect.domain.GetAllRequestACRUsecase;
import com.demo.architect.domain.GetAllScanTurnOutUsecase;
import com.demo.architect.domain.GetDateServerUsecase;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.constants.Constants;
import com.demo.scanacr.manager.ListCodeScanManager;
import com.demo.scanacr.manager.ListRequestInManager;
import com.demo.scanacr.manager.UserManager;
import com.demo.scanacr.util.ConvertUtils;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class ImportWorksPresenter implements ImportWorksContract.Presenter {

    private final String TAG = ImportWorksPresenter.class.getName();
    private final ImportWorksContract.View view;
    private final GetAllRequestACRInUsecase allRequestACRUsecase;
    private final GetDateServerUsecase getDateServerUsecase;
    private final GetAllScanTurnOutUsecase getAllScanTurnOutUsecase;
    private final AddLogScanACRUsecase addLogScanACRUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    ImportWorksPresenter(@NonNull ImportWorksContract.View view,
                         GetAllRequestACRInUsecase allRequestACRUsecase,
                         GetDateServerUsecase getDateServerUsecase,
                         GetAllScanTurnOutUsecase getAllScanTurnOutUsecase, AddLogScanACRUsecase addLogScanACRUsecase) {
        this.view = view;
        this.allRequestACRUsecase = allRequestACRUsecase;
        this.getDateServerUsecase = getDateServerUsecase;
        this.getAllScanTurnOutUsecase = getAllScanTurnOutUsecase;
        this.addLogScanACRUsecase = addLogScanACRUsecase;
    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");
        getRequest();
    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void checkBarcode(int requestId, String barcode, double latitude, double longitude) {
        if (!barcode.contains("-")) {
            view.showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_type));
            return;
        }

        if (barcode.length() < 11 || barcode.length() > 14) {
            view.showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_lenght));
            return;
        }
        CodeOutEntity codeOutEntity = ListCodeScanManager.getInstance().getCodeScanByBarcode(barcode);
        if (codeOutEntity != null) {
            localRepository.checkExistImportWorks(barcode).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean aBoolean) {
                    if (!aBoolean) {
                        uploadData(codeOutEntity, barcode, latitude, longitude);
                    } else {
                        view.showError(CoreApplication.getInstance().getString(R.string.text_barcode_saved));
                    }
                }
            });
        } else {
            view.showError(CoreApplication.getInstance().getString(R.string.text_package_no_create));
        }


    }

    @Override
    public void getRequest() {
        view.showProgressBar();
        allRequestACRUsecase.executeIO(new GetAllRequestACRUsecase.RequestValue(),
                new BaseUseCase.UseCaseCallback<GetAllRequestACRUsecase.ResponseValue,
                        GetAllRequestACRUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetAllRequestACRUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListRequestInManager.getInstance().setListRequest(successResponse.getEntity());
                        view.showListRequest(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetAllRequestACRUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListRequestInManager.getInstance().setListRequest(null);
                    }
                });
    }

    @Override
    public void getCodeScan(int requestId) {
        view.showProgressBar();
        getAllScanTurnOutUsecase.executeIO(new GetAllScanTurnOutUsecase.RequestValue(requestId),
                new BaseUseCase.UseCaseCallback<GetAllScanTurnOutUsecase.ResponseValue,
                        GetAllScanTurnOutUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetAllScanTurnOutUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListCodeScanManager.getInstance().setListCodeScan(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetAllScanTurnOutUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListCodeScanManager.getInstance().setListCodeScan(null);
                    }
                });
    }


    public void uploadData(CodeOutEntity codeOutEntity, String barcode, double latitude, double longitude) {
        view.showProgressBar();
        String deviceTime = ConvertUtils.getDateTimeCurrent();
        int userId = UserManager.getInstance().getUser().getUserId();
        String phone = Settings.Secure.getString(CoreApplication.getInstance().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        getDateServerUsecase.executeIO(new GetDateServerUsecase.RequestValue(),
                new BaseUseCase.UseCaseCallback<GetDateServerUsecase.ResponseValue,
                        GetDateServerUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetDateServerUsecase.ResponseValue successResponse) {
                        String timeServer = successResponse.getDate();
                        addLogScanACRUsecase.executeIO(new AddLogScanACRUsecase.RequestValue(phone,
                                codeOutEntity.getOrderId(), codeOutEntity.getPackageId(), barcode, 1,latitude,
                                longitude, Constants.IN, 0,deviceTime, userId,
                                codeOutEntity.getRequestId()), new BaseUseCase.UseCaseCallback<AddLogScanACRUsecase.ResponseValue,
                                AddLogScanACRUsecase.ErrorValue>() {
                            @Override
                            public void onSuccess(AddLogScanACRUsecase.ResponseValue successResponse) {
                                view.hideProgressBar();

                            }

                            @Override
                            public void onError(AddLogScanACRUsecase.ErrorValue errorResponse) {
                                view.hideProgressBar();
                                view.showError(errorResponse.getDescription());
                            }
                        });
                    }

                    @Override
                    public void onError(GetDateServerUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });

    }
}
