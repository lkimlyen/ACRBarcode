package com.demo.scanacr.screen.import_works;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.CodeOutEntity;
import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.offline.ImportWorksModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.AddLogScanACRUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetAllRequestACRInUsecase;
import com.demo.architect.domain.GetAllScanTurnOutUsecase;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.constants.Constants;
import com.demo.scanacr.manager.ListCodeScanManager;
import com.demo.scanacr.manager.ListRequestInManager;
import com.demo.scanacr.manager.UserManager;
import com.demo.scanacr.util.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class ImportWorksPresenter implements ImportWorksContract.Presenter {

    private final String TAG = ImportWorksPresenter.class.getName();
    private final ImportWorksContract.View view;
    private final GetAllRequestACRInUsecase allRequestACRUsecase;
    private final GetAllScanTurnOutUsecase getAllScanTurnOutUsecase;
    private final AddLogScanACRUsecase addLogScanACRUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    ImportWorksPresenter(@NonNull ImportWorksContract.View view,
                         GetAllRequestACRInUsecase allRequestACRUsecase,
                         GetAllScanTurnOutUsecase getAllScanTurnOutUsecase, AddLogScanACRUsecase addLogScanACRUsecase) {
        this.view = view;
        this.allRequestACRUsecase = allRequestACRUsecase;
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
        // getRequest();
    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void checkBarcode(int requestId, String barcode, double latitude, double longitude) {
        if (!barcode.contains("-")) {
            view.showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_type));
            view.startMusicError();
            view.turnOnVibrator();
            return;
        }

        if (barcode.length() < 11 || barcode.length() > 14) {
            view.showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_lenght));
            view.startMusicError();
            view.turnOnVibrator();
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
                        view.startMusicError();
                        view.turnOnVibrator();
                    }
                }
            });
        } else {
            view.showError(CoreApplication.getInstance().getString(R.string.text_package_no_create));
            view.startMusicError();
            view.turnOnVibrator();
        }


    }

    @Override
    public void getRequest() {
        view.showProgressBar();
        allRequestACRUsecase.executeIO(new GetAllRequestACRInUsecase.RequestValue(),
                new BaseUseCase.UseCaseCallback<GetAllRequestACRInUsecase.ResponseValue,
                        GetAllRequestACRInUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetAllRequestACRInUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListRequestInManager.getInstance().setListRequest(successResponse.getEntity());
                        List<OrderRequestEntity> list = new ArrayList<>();
                        list.add(new OrderRequestEntity(CoreApplication.getInstance().getString(R.string.text_choose_request_produce)));
                        list.addAll(successResponse.getEntity());
                        view.showListRequest(list);
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_download_list_prodution_success));
                    }

                    @Override
                    public void onError(GetAllRequestACRInUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        String error = "";
                        if (errorResponse.getDescription().contains(
                                CoreApplication.getInstance().getString(R.string.text_error_network_host))) {
                            error = CoreApplication.getInstance().getString(R.string.text_error_network);
                        } else {
                            error = errorResponse.getDescription();
                        }
                        view.showError(error);
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
                        String error = "";
                        if (errorResponse.getDescription().contains(
                                CoreApplication.getInstance().getString(R.string.text_error_network_host))) {
                            error = CoreApplication.getInstance().getString(R.string.text_error_network);
                        } else {
                            error = errorResponse.getDescription();
                        }
                        view.showError(error);
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

        addLogScanACRUsecase.executeIO(new AddLogScanACRUsecase.RequestValue(phone,
                codeOutEntity.getOrderId(), codeOutEntity.getPackageId(), barcode, 1, latitude,
                longitude, Constants.IN, 0, deviceTime, userId,
                codeOutEntity.getRequestId()), new BaseUseCase.UseCaseCallback<AddLogScanACRUsecase.ResponseValue,
                AddLogScanACRUsecase.ErrorValue>() {
            @Override
            public void onSuccess(AddLogScanACRUsecase.ResponseValue successResponse) {
                view.hideProgressBar();
                ImportWorksModel model = new ImportWorksModel(successResponse.getId(), barcode,
                        deviceTime, deviceTime, latitude, longitude, phone, codeOutEntity.getPackageId(),
                        codeOutEntity.getOrderId(), codeOutEntity.getRequestId(),
                        userId);
                localRepository.addImportWorks(model).subscribe(new Action1<ImportWorksModel>() {
                    @Override
                    public void call(ImportWorksModel model) {
                        view.showListPackage(model);
                        view.startMusicSuccess();
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                    }
                });
            }

            @Override
            public void onError(AddLogScanACRUsecase.ErrorValue errorResponse) {
                view.hideProgressBar();
                String error = "";
                if (errorResponse.getDescription().contains(
                        CoreApplication.getInstance().getString(R.string.text_error_network_host))) {
                    error = CoreApplication.getInstance().getString(R.string.text_error_network);
                } else {
                    error = errorResponse.getDescription();
                }
                view.showError(error);
                view.startMusicError();
            }
        });


    }
}
