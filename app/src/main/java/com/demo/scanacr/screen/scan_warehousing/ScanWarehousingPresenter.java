package com.demo.scanacr.screen.scan_warehousing;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.offline.ScanWarehousingList;
import com.demo.architect.data.model.offline.ScanWarehousingModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.AddLogScanInStoreACRUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetAllPackageUsecase;
import com.demo.architect.domain.GetDateServerUsecase;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.manager.ListPackageManager;
import com.demo.scanacr.manager.UserManager;
import com.demo.scanacr.util.ConvertUtils;

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
    private final GetDateServerUsecase getDateServerUsecase;
    private int idList = 0;
    private boolean getList = false;
    @Inject
    LocalRepository localRepository;

    @Inject
    ScanWarehousingPresenter(@NonNull ScanWarehousingContract.View view, GetAllPackageUsecase getAllPackageUsecase, AddLogScanInStoreACRUsecase addLogScanInStoreACRUsecase, GetDateServerUsecase getDateServerUsecase) {
        this.view = view;
        this.getAllPackageUsecase = getAllPackageUsecase;
        this.addLogScanInStoreACRUsecase = addLogScanInStoreACRUsecase;
        this.getDateServerUsecase = getDateServerUsecase;
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
    public void checkBarcode(String barcode, double latitude, double longitude) {
        if (!barcode.contains("-")) {
            view.showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_type));
            return;
        }

        if (barcode.length() < 11 || barcode.length() > 14) {
            view.showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_lenght));
            return;
        }
        String[] packageList = barcode.split("-");
        String requestCode = packageList[0];
        int serial = Integer.parseInt(packageList[1]);

        PackageEntity packageEntity = ListPackageManager.getInstance().getPackageByBarcode(requestCode, serial);
        if (packageEntity != null) {
            localRepository.checkExistBarcode(barcode, idList).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean aBoolean) {
                    if (!aBoolean) {
                        if (idList == 0) {
                            localRepository.getIdScanWarehousingList().subscribe(new Action1<Integer>() {
                                @Override
                                public void call(Integer integer) {
                                    idList += integer;
                                }
                            });
                        }
                        uploadData(packageEntity, barcode, latitude, longitude);
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
    public void getPackage() {
        view.showProgressBar();
        getAllPackageUsecase.executeIO(new GetAllPackageUsecase.RequestValue(),
                new BaseUseCase.UseCaseCallback<GetAllPackageUsecase.ResponseValue,
                        GetAllPackageUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetAllPackageUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListPackageManager.getInstance().setListPackage(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetAllPackageUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
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
        getDateServerUsecase.executeIO(new GetDateServerUsecase.RequestValue(),
                new BaseUseCase.UseCaseCallback<GetDateServerUsecase.ResponseValue,
                        GetDateServerUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetDateServerUsecase.ResponseValue successResponse) {
                        String timeServer = successResponse.getDate();
                        addLogScanInStoreACRUsecase.executeIO(new AddLogScanInStoreACRUsecase.RequestValue(phone, packageEntity.getOrderID(),
                                        packageEntity.getId(), barcode, 1, latitude, longitude, deviceTime, userId),
                                new BaseUseCase.UseCaseCallback<AddLogScanInStoreACRUsecase.ResponseValue,
                                        AddLogScanInStoreACRUsecase.ErrorValue>() {
                                    @Override
                                    public void onSuccess(AddLogScanInStoreACRUsecase.ResponseValue successResponse) {
                                        ScanWarehousingModel model = new ScanWarehousingModel(successResponse.getId(),
                                                barcode, deviceTime, timeServer, latitude, longitude, phone,
                                                packageEntity.getId(), packageEntity.getOrderID(), packageEntity.getSTT(), 1, userId);
                                        localRepository.addScanWareHousing(model, idList).subscribe(new Action1<String>() {
                                            @Override
                                            public void call(String s) {
                                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_into_warehousing_success));
                                                if (!getList) {
                                                    getListScanWarehousing(idList);
                                                    getList = true;
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(AddLogScanInStoreACRUsecase.ErrorValue errorResponse) {
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

    public void getListScanWarehousing(int idList) {
        localRepository.findScanWarehousingList(idList).subscribe(new Action1<ScanWarehousingList>() {
            @Override
            public void call(ScanWarehousingList list) {
                view.showListScanWarehousing(list);
            }
        });
    }

}
