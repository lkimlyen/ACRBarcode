package com.demo.scanacr.screen.scan_delivery;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.offline.ScanDeliveryList;
import com.demo.architect.data.model.offline.ScanDeliveryModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetAllPackageForRequestUsecase;
import com.demo.architect.domain.GetAllRequestACRUsecase;
import com.demo.architect.domain.GetMaxTimesACRUsecase;
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

public class ScanDeliveryPresenter implements ScanDeliveryContract.Presenter {

    private final String TAG = ScanDeliveryPresenter.class.getName();
    private final ScanDeliveryContract.View view;
    private final GetAllRequestACRUsecase allRequestACRUsecase;
    private final GetAllPackageForRequestUsecase getAllPackageForRequestUsecase;
    private final GetMaxTimesACRUsecase getMaxTimesACRUsecase;
    private int times;
    @Inject
    LocalRepository localRepository;

    @Inject
    ScanDeliveryPresenter(@NonNull ScanDeliveryContract.View view,
                          GetAllRequestACRUsecase allRequestACRUsecase,
                          GetAllPackageForRequestUsecase getAllPackageForRequestUsecase,
                          GetMaxTimesACRUsecase getMaxTimesACRUsecase) {
        this.view = view;
        this.allRequestACRUsecase = allRequestACRUsecase;
        this.getAllPackageForRequestUsecase = getAllPackageForRequestUsecase;
        this.getMaxTimesACRUsecase = getMaxTimesACRUsecase;
    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");
        //   getRequest();
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
        String[] packageList = barcode.split("-");
        String requestCode = packageList[0];
        int serial = Integer.parseInt(packageList[1]);

        PackageEntity packageEntity = ListPackageManager.getInstance().getPackageByBarcode(requestCode, serial);
        if (packageEntity != null) {
            localRepository.checkExistBarcodeInDelivery(barcode).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean aBoolean) {
                    if (!aBoolean) {
                        saveBarcode(barcode, latitude, longitude, requestId, packageEntity);
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
        allRequestACRUsecase.executeIO(new GetAllRequestACRUsecase.RequestValue(),
                new BaseUseCase.UseCaseCallback<GetAllRequestACRUsecase.ResponseValue,
                        GetAllRequestACRUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetAllRequestACRUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListRequestManager.getInstance().setListRequest(successResponse.getEntity());
                        List<OrderRequestEntity> list = new ArrayList<>();
                        list.add(new OrderRequestEntity(CoreApplication.getInstance().getString(R.string.text_choose_request_produce)));
                        list.addAll(successResponse.getEntity());
                        view.showListRequest(list);
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_download_list_prodution_success));
                    }

                    @Override
                    public void onError(GetAllRequestACRUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        String error = "";
                        if (errorResponse.getDescription().contains(
                                CoreApplication.getInstance().getString(R.string.text_error_network_host))) {
                            error = CoreApplication.getInstance().getString(R.string.text_error_network);
                        } else {
                            error = errorResponse.getDescription();
                        }
                        view.showError(error);
                    }
                });
    }

    @Override
    public void getPackageForRequest(int requestId) {
        view.showProgressBar();
        getAllPackageForRequestUsecase.executeIO(new GetAllPackageForRequestUsecase.RequestValue(requestId),
                new BaseUseCase.UseCaseCallback<GetAllPackageForRequestUsecase.ResponseValue,
                        GetAllPackageForRequestUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetAllPackageForRequestUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListPackageManager.getInstance().setListPackage(successResponse.getEntity());
                        if (successResponse.getEntity().size() == 0) {
                            view.showWarning(CoreApplication.getInstance().getString(R.string.text_code_null));
                        } else {
                            view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_package_success));
                        }
                    }

                    @Override
                    public void onError(GetAllPackageForRequestUsecase.ErrorValue errorResponse) {
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
    public void getMaxTimes(int requestId, String requestCode) {


        view.showProgressBar();
        getMaxTimesACRUsecase.executeIO(new GetMaxTimesACRUsecase.RequestValue(requestId),
                new BaseUseCase.UseCaseCallback<GetMaxTimesACRUsecase.ResponseValue,
                        GetMaxTimesACRUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetMaxTimesACRUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        times = successResponse.getNumber();

                        localRepository.findScanDelivery(successResponse.getNumber(), requestCode).subscribe(new Action1<ScanDeliveryList>() {
                            @Override
                            public void call(ScanDeliveryList list) {
                                view.showListPackage(list);
                            }
                        });
                    }

                    @Override
                    public void onError(GetMaxTimesACRUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        String error = "";
                        if (errorResponse.getDescription().contains(
                                CoreApplication.getInstance().getString(R.string.text_error_network_host))) {
                            error = CoreApplication.getInstance().getString(R.string.text_error_network);
                        } else {
                            error = errorResponse.getDescription();
                        }
                        view.showError(error);

                    }
                });
    }


    public void saveBarcode(String barcode, double latitude, double longitude, int requestId, PackageEntity packageEntity) {
        view.showProgressBar();
        String deviceTime = ConvertUtils.getDateTimeCurrent();
        int userId = UserManager.getInstance().getUser().getUserId();
        String phone = Settings.Secure.getString(CoreApplication.getInstance().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        ScanDeliveryModel model = new ScanDeliveryModel(barcode, deviceTime, deviceTime, latitude, longitude, phone,
                packageEntity.getId(), packageEntity.getOrderID(), requestId, packageEntity.getSTT(), userId);
        localRepository.addScanDelivery(model, times, packageEntity.getCodeSX()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
                view.hideProgressBar();
            }
        });

    }

}
