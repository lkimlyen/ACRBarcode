package com.demo.scanacr.screen.print_stemp;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.SocketRespone;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.model.offline.PackageModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.repository.base.socket.ConnectSocket;
import com.demo.architect.domain.AddPackageACRUsecase;
import com.demo.architect.domain.AddPackageACRbyJsonUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetMaxPackageForSOUsecase;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.manager.ScanCreatePackManager;
import com.demo.scanacr.util.ConvertUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class PrintStempPresenter implements PrintStempContract.Presenter {

    private final String TAG = PrintStempPresenter.class.getName();
    private final PrintStempContract.View view;
    private final GetMaxPackageForSOUsecase getMaxPackageForSOUsecase;
    private final AddPackageACRbyJsonUsecase addPackageACRbyJsonUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    PrintStempPresenter(@NonNull PrintStempContract.View view, GetMaxPackageForSOUsecase getMaxPackageForSOUsecase,
                         AddPackageACRbyJsonUsecase addPackageACRbyJsonUsecase) {
        this.view = view;
        this.getMaxPackageForSOUsecase = getMaxPackageForSOUsecase;
        this.addPackageACRbyJsonUsecase = addPackageACRbyJsonUsecase;
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
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
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

    @Override
    public void printStemp(int orderId, int serial, int serverId, int numTotal) {

        localRepository.findIPAddress().subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                if (address == null) {
                    view.showError(CoreApplication.getInstance().getString(R.string.text_no_ip_address));
                    return;
                }
                view.showProgressBar();
                ConnectSocket connectSocket = new ConnectSocket(address.getIpAddress(), address.getPortNumber(),
                        serverId, new ConnectSocket.onPostExecuteResult() {
                    @Override
                    public void onPostExecute(SocketRespone respone) {
                        if (respone.getConnect() == 1 && respone.getResult() == 1) {
                            if (serverId == 0) {
                                localRepository.logCreateToJson(orderId).subscribe(new Action1<List<LogScanCreatePack>>() {
                                    @Override
                                    public void call(List<LogScanCreatePack> list) {
                                        List<PackageModel> packageModels = new ArrayList<>();
                                        for (LogScanCreatePack pack : list) {
                                            PackageModel model = new PackageModel(pack.getOrderId(),
                                                    serial, pack.getProductId(), pack.getBarcode(), pack.getNumInput(),
                                                    pack.getLatitude(), pack.getLongitude(), pack.getDeviceTime(), pack.getCreateBy());
                                            packageModels.add(model);
                                        }
                                        Gson gson = new Gson();
                                        String json = gson.toJson(packageModels);
                                        Log.d("PARSEARRAYTOJSON", json);
                                        updateData(orderId, serial, numTotal, json);

                                    }
                                });


                            } else {
                                localRepository.deleteLogCompleteAll().subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {

                                        view.hideProgressBar();
                                        view.backToCreatePack();
                                    }
                                });
                            }
                        } else {
                            view.hideProgressBar();
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_connect_printer));

                        }
                    }
                });

                connectSocket.execute();
            }
        });


    }


    public void updateData(int orderId, int serial, int numTotal, String json) {
        addPackageACRbyJsonUsecase.executeIO(new AddPackageACRbyJsonUsecase.RequestValue(json),
                new BaseUseCase.UseCaseCallback<AddPackageACRbyJsonUsecase.ResponseValue,
                        AddPackageACRbyJsonUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(AddPackageACRbyJsonUsecase.ResponseValue successResponse) {

                        localRepository.addLogCompleteCreatePack(orderId, successResponse.getId(), serial, numTotal, ConvertUtils.getDateTimeCurrent())
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        printStemp(orderId, serial, successResponse.getId(), numTotal);
                                    }
                                });

                    }

                    @Override
                    public void onError(AddPackageACRbyJsonUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });


    }
}
