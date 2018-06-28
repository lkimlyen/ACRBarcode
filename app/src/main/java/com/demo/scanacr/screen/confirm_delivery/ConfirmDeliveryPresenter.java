package com.demo.scanacr.screen.confirm_delivery;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.ResultEntity;
import com.demo.architect.data.model.offline.DeliveryModel;
import com.demo.architect.data.model.offline.ScanDeliveryList;
import com.demo.architect.data.model.offline.ScanDeliveryModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.AddLogScanACRUsecase;
import com.demo.architect.domain.AddLogScanbyJsonUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetAllRequestACRUsecase;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.constants.Constants;
import com.demo.scanacr.manager.ListRequestManager;
import com.demo.scanacr.manager.ScanDeliveryManager;
import com.demo.scanacr.manager.ServerManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class ConfirmDeliveryPresenter implements ConfirmDeliveryContract.Presenter {

    private final String TAG = ConfirmDeliveryPresenter.class.getName();
    private final ConfirmDeliveryContract.View view;
    private final AddLogScanACRUsecase addLogScanACRUsecase;
    private final GetAllRequestACRUsecase allRequestACRUsecase;
    private final AddLogScanbyJsonUsecase addLogScanbyJsonUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    ConfirmDeliveryPresenter(@NonNull ConfirmDeliveryContract.View view, AddLogScanACRUsecase addLogScanACRUsecase, GetAllRequestACRUsecase allRequestACRUsecase, AddLogScanbyJsonUsecase addLogScanbyJsonUsecase) {
        this.view = view;
        this.addLogScanACRUsecase = addLogScanACRUsecase;
        this.allRequestACRUsecase = allRequestACRUsecase;
        this.addLogScanbyJsonUsecase = addLogScanbyJsonUsecase;
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
                    }

                    @Override
                    public void onError(GetAllRequestACRUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
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
    public void uploadData(String codeRequest) {
        view.showProgressBar();
        ScanDeliveryList deliveryList = ScanDeliveryManager.getInstance().getDeliveryList();
        final HashMap<String, Integer> idList = new HashMap<>();
        localRepository.deliveryToJson(codeRequest).subscribe(new Action1<List<ScanDeliveryModel>>() {
            @Override
            public void call(List<ScanDeliveryModel> scanDeliveryModels) {
                List<DeliveryModel> list = new ArrayList<>();
                for (ScanDeliveryModel item : scanDeliveryModels) {

                    DeliveryModel model = new DeliveryModel(item.getBarcode(), item.getDeviceTime(),
                            item.getLatitude(), item.getLongitude(), item.getCreateByPhone(), 1,
                            item.getOrderId(), item.getPackageId(), deliveryList.getTimes(), item.getRequestId(), item.getCreateBy()
                    );

                    list.add(model);
                }
                Gson gson = new Gson();
                String json = gson.toJson(list);
                addLogScanbyJsonUsecase.executeIO(new AddLogScanbyJsonUsecase.RequestValue(json),
                        new BaseUseCase.UseCaseCallback<AddLogScanbyJsonUsecase.ResponseValue,
                                AddLogScanbyJsonUsecase.ErrorValue>() {
                            @Override
                            public void onSuccess(AddLogScanbyJsonUsecase.ResponseValue successResponse) {
                                view.hideProgressBar();
                                for (ResultEntity result : successResponse.getEntityList()) {
                                    idList.put(result.getBarcode(), result.getId());
                                }
                                localRepository.updateStatusScanDelivery(deliveryList.getId(), idList).subscribe();
                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_upload_success));

                            }

                            @Override
                            public void onError(AddLogScanbyJsonUsecase.ErrorValue errorResponse) {
                                view.hideProgressBar();
                                view.showError(errorResponse.getDescription());
                            }
                        });
            }
        });


    }

}
