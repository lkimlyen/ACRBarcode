package com.demo.scanacr.screen.confirm_delivery;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.offline.ScanDeliveryList;
import com.demo.architect.data.model.offline.ScanDeliveryModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.AddLogScanACRUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.constants.Constants;
import com.demo.scanacr.manager.ListRequestManager;
import com.demo.scanacr.manager.ScanDeliveryManager;

import java.util.HashMap;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class ConfirmDeliveryPresenter implements ConfirmDeliveryContract.Presenter {

    private final String TAG = ConfirmDeliveryPresenter.class.getName();
    private final ConfirmDeliveryContract.View view;
    private final AddLogScanACRUsecase addLogScanACRUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    ConfirmDeliveryPresenter(@NonNull ConfirmDeliveryContract.View view, AddLogScanACRUsecase addLogScanACRUsecase) {
        this.view = view;
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
    public void getRequest() {
        view.showListRequest(ListRequestManager.getInstance().getListRequest());
    }

    @Override
    public void checkRequest(String codeRequest) {
        localRepository.findScanDeliveryNotComplete(codeRequest).subscribe(new Action1<ScanDeliveryList>() {
            @Override
            public void call(ScanDeliveryList scanDeliveryList) {
                ScanDeliveryManager.getInstance().setDeliveryList(scanDeliveryList);
                view.showListPackage(scanDeliveryList);
            }
        });

    }

    private int count = 0;

    @Override
    public void uploadData() {
        view.showProgressBar();
        ScanDeliveryList deliveryList = ScanDeliveryManager.getInstance().getDeliveryList();
        final int countList = deliveryList.getItemList().size();
        final HashMap<String, Integer> idList = new HashMap<>();
        count = 0;
        for (ScanDeliveryModel model : deliveryList.getItemList()) {
            addLogScanACRUsecase.executeIO(new AddLogScanACRUsecase.RequestValue(model.getCreateByPhone(),
                    model.getOrderId(), model.getPackageId(), model.getBarcode(), 1, model.getLatitude(),
                    model.getLongitude(), Constants.OUT, deliveryList.getTimes(), model.getDeviceTime(), model.getCreateBy(),
                    model.getRequestId()), new BaseUseCase.UseCaseCallback<AddLogScanACRUsecase.ResponseValue,
                    AddLogScanACRUsecase.ErrorValue>() {
                @Override
                public void onSuccess(AddLogScanACRUsecase.ResponseValue successResponse) {
                    view.hideProgressBar();
                    idList.put(successResponse.getBarcode(), successResponse.getId());
                    count++;
                    if (count == countList) {
                        localRepository.updateStatusScanDelivery(deliveryList.getId(),idList).subscribe();
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_upload_success));
                    }
                }

                @Override
                public void onError(AddLogScanACRUsecase.ErrorValue errorResponse) {
                    view.hideProgressBar();
                    view.showError(errorResponse.getDescription());
                }
            });
        }

    }

}
