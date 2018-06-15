package com.demo.scanacr.screen.print_stemp;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetMaxPackageForSOUsecase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class PrintStempPresenter implements PrintStempContract.Presenter {

    private final String TAG = PrintStempPresenter.class.getName();
    private final PrintStempContract.View view;
    private final GetMaxPackageForSOUsecase getMaxPackageForSOUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    PrintStempPresenter(@NonNull PrintStempContract.View view, GetMaxPackageForSOUsecase getMaxPackageForSOUsecase) {
        this.view = view;
        this.getMaxPackageForSOUsecase = getMaxPackageForSOUsecase;
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
    public void getMaxNumberOrder(int orderId) {
        view.showProgressBar();
        getMaxPackageForSOUsecase.executeIO(new GetMaxPackageForSOUsecase.RequestValue(orderId),
                new BaseUseCase.UseCaseCallback<GetMaxPackageForSOUsecase.ResponseValue,
                        GetMaxPackageForSOUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetMaxPackageForSOUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        view.showSerialPack(successResponse.getNumber() + 1);
                    }

                    @Override
                    public void onError(GetMaxPackageForSOUsecase.ErrorValue errorResponse) {

                    }
                });
    }

    @Override
    public void getOrder(int orderId) {
        localRepository.findOrder(orderId).subscribe(new Action1<OrderModel>() {
            @Override
            public void call(OrderModel model) {
                view.showOrder(model);
            }
        });

        localRepository.getSumLogPack(orderId).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
           view.showSumPack(integer);
            }
        });
    }

    @Override
    public void getListCreatePack(int orderId) {
        localRepository.findLogPrint(orderId).subscribe(new Action1<HashMap<LogScanCreatePack, ProductModel>>() {
            @Override
            public void call(HashMap<LogScanCreatePack, ProductModel> logScanCreatePackProductModelHashMap) {
                view.showListCreatePack(logScanCreatePackProductModelHashMap);
            }
        });

    }
}
