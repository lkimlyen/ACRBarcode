package com.demo.scanacr.screen.scan_warehousing;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.offline.ScanWarehousingModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.AddLogScanInStoreACRUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetAllPackageUsecase;
import com.demo.architect.domain.GetCodeSXForInStoreUseCase;
import com.demo.architect.domain.GetPackageForInStoreUseCase;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.manager.ListPackageManager;
import com.demo.scanacr.manager.ListRequestManager;
import com.demo.scanacr.manager.UserManager;
import com.demo.scanacr.util.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class ScanWarehousingPresenter implements ScanWarehousingContract.Presenter {

    private final String TAG = ScanWarehousingPresenter.class.getName();
    private final ScanWarehousingContract.View view;
    private final GetAllPackageUsecase getAllPackageUsecase;
    private final AddLogScanInStoreACRUsecase addLogScanInStoreACRUsecase;
    private final GetPackageForInStoreUseCase getPackageForInStoreUseCase;
    private final GetCodeSXForInStoreUseCase getCodeSXForInStoreUseCase;
    @Inject
    LocalRepository localRepository;

    @Inject
    ScanWarehousingPresenter(@NonNull ScanWarehousingContract.View view, GetAllPackageUsecase getAllPackageUsecase,
                             AddLogScanInStoreACRUsecase addLogScanInStoreACRUsecase,
                             GetPackageForInStoreUseCase getPackageForInStoreUseCase,
                             GetCodeSXForInStoreUseCase getCodeSXForInStoreUseCase) {
        this.view = view;
        this.getAllPackageUsecase = getAllPackageUsecase;
        this.addLogScanInStoreACRUsecase = addLogScanInStoreACRUsecase;
        this.getPackageForInStoreUseCase = getPackageForInStoreUseCase;
        this.getCodeSXForInStoreUseCase = getCodeSXForInStoreUseCase;
    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");
        // getPackage();
    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void checkBarcode(String barcode, double latitude, double longitude) {
        if (!barcode.contains("-")) {
            view.showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_type));
            view.startMusicError();
            return;
        }

        if (barcode.length() < 11 || barcode.length() > 14) {
            view.showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_lenght));
            view.startMusicError();
            return;
        }
        String[] packageList = barcode.split("-");
        String requestCode = packageList[0];
        int serial = Integer.parseInt(packageList[1]);

        PackageEntity packageEntity = ListPackageManager.getInstance().getPackageByBarcode(requestCode, serial);
        if (packageEntity != null) {
            localRepository.checkExistBarcodeInWarehousing(barcode).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean aBoolean) {
                    if (!aBoolean) {
                        uploadData(packageEntity, barcode, latitude, longitude);
                    } else {
                        view.showError(CoreApplication.getInstance().getString(R.string.text_barcode_saved));
                        view.startMusicError();
                    }
                }
            });
        } else {
            view.showError(CoreApplication.getInstance().getString(R.string.text_package_no_create));
            view.startMusicError();
        }

    }

    @Override
    public void getPackage(int orderId, String codeProduce) {
        view.showProgressBar();
        getPackageForInStoreUseCase.executeIO(new GetPackageForInStoreUseCase.RequestValue(orderId, codeProduce),
                new BaseUseCase.UseCaseCallback<GetPackageForInStoreUseCase.ResponseValue,
                        GetPackageForInStoreUseCase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetPackageForInStoreUseCase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListPackageManager.getInstance().setListPackage(successResponse.getEntity());
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_download_list_package_success));
                    }

                    @Override
                    public void onError(GetPackageForInStoreUseCase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        String error = "";
                        if (errorResponse.getDescription().contains(
                                CoreApplication.getInstance().getString(R.string.text_error_network_host))) {
                            error = CoreApplication.getInstance().getString(R.string.text_error_network);
                        } else {
                            error = errorResponse.getDescription();
                        }
                        view.showError(error);
                        ListPackageManager.getInstance().setListPackage(null);
                    }
                });
    }

    @Override
    public void getCodeProduce() {
        view.showProgressBar();
        getCodeSXForInStoreUseCase.executeIO(new GetCodeSXForInStoreUseCase.RequestValue(),
                new BaseUseCase.UseCaseCallback<GetCodeSXForInStoreUseCase.ResponseValue,
                        GetCodeSXForInStoreUseCase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetCodeSXForInStoreUseCase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListRequestManager.getInstance().setListRequest(successResponse.getEntity());
                        List<OrderRequestEntity> list = new ArrayList<>();
                        list.add(new OrderRequestEntity(CoreApplication.getInstance().getString(R.string.text_choose_request_produce)));
                        list.addAll(successResponse.getEntity());
                        view.showListRequest(list);
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_download_list_prodution_success));
                    }

                    @Override
                    public void onError(GetCodeSXForInStoreUseCase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        String error = "";
                        if (errorResponse.getDescription().contains(
                                CoreApplication.getInstance().getString(R.string.text_error_network_host))) {
                            error = CoreApplication.getInstance().getString(R.string.text_error_network);
                        } else {
                            error = errorResponse.getDescription();
                        }
                        view.showError(error);
                        ListPackageManager.getInstance().setListPackage(null);
                    }
                });
    }


    public void uploadData(PackageEntity packageEntity, String barcode, double latitude, double longitude) {
        view.showProgressBar();
        String deviceTime = ConvertUtils.getDateTimeCurrent();
        int userId = UserManager.getInstance().getUser().getUserId();
        String phone = Settings.Secure.getString(CoreApplication.getInstance().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        addLogScanInStoreACRUsecase.executeIO(new AddLogScanInStoreACRUsecase.RequestValue(phone, packageEntity.getOrderID(),
                        packageEntity.getId(), barcode, 1, latitude, longitude, deviceTime, userId),
                new BaseUseCase.UseCaseCallback<AddLogScanInStoreACRUsecase.ResponseValue,
                        AddLogScanInStoreACRUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(AddLogScanInStoreACRUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ScanWarehousingModel model = new ScanWarehousingModel(successResponse.getId(),
                                barcode, deviceTime, deviceTime, latitude, longitude, phone,
                                packageEntity.getId(), packageEntity.getOrderID(), packageEntity.getSTT(), userId);
                        localRepository.addScanWareHousing(model).subscribe(new Action1<ScanWarehousingModel>() {
                            @Override
                            public void call(ScanWarehousingModel scanWarehousingModel) {
                                view.showListScanWarehousing(scanWarehousingModel);
                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                                view.startMusicSuccess();

                            }
                        });
                    }

                    @Override
                    public void onError(AddLogScanInStoreACRUsecase.ErrorValue errorResponse) {
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

