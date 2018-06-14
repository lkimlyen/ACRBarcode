package com.demo.scanacr.screen.create_code_package;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.OrderACREntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetAllDetailForSOACRUsecase;
import com.demo.architect.domain.GetAllSOACRUsecase;
import com.demo.architect.domain.GetDateServerUsecase;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.constants.Constants;
import com.demo.scanacr.manager.UserManager;
import com.demo.scanacr.util.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class CreateCodePackagePresenter implements CreateCodePackageContract.Presenter {

    private final String TAG = CreateCodePackagePresenter.class.getName();
    private final CreateCodePackageContract.View view;
    private final GetAllSOACRUsecase getAllSOACRUsecase;
    private final GetAllDetailForSOACRUsecase getAllDetailForSOACRUsecase;
    private final GetDateServerUsecase getDateServerUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    CreateCodePackagePresenter(@NonNull CreateCodePackageContract.View view,
                               GetAllSOACRUsecase getAllSOACRUsecase,
                               GetAllDetailForSOACRUsecase getAllDetailForSOACRUsecase,
                               GetDateServerUsecase getDateServerUsecase) {
        this.view = view;
        this.getAllSOACRUsecase = getAllSOACRUsecase;
        this.getAllDetailForSOACRUsecase = getAllDetailForSOACRUsecase;
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
    public void getData() {
        view.showProgressBar();
        getAllSOACRUsecase.executeIO(new GetAllSOACRUsecase.RequestValue(),
                new BaseUseCase.UseCaseCallback<GetAllSOACRUsecase.ResponseValue,
                        GetAllSOACRUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetAllSOACRUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        localRepository.deleteAllOrder().subscribe();
                        List<OrderModel> list = new ArrayList<>();
                        //list.add(CoreApplication.getInstance().getString(R.string.text_choose_request_produce));
                        int userId = UserManager.getInstance().getUser().getUserId();
                        for (OrderACREntity entity : successResponse.getEntity()) {
                            OrderModel model = new OrderModel(entity.getId(), entity.getCustomerID(), entity.getCode(), entity.getCodeSX(), entity.getCustomerName(), userId,
                                    ConvertUtils.getDateTimeCurrent());
                            localRepository.addItemAsyns(model).subscribe(new Action1<OrderModel>() {
                                @Override
                                public void call(OrderModel model) {
                                    list.add(model);
                                }
                            });
                        }
                        view.showRequestProduction(list);
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_download_list_prodution_success));

                    }

                    @Override
                    public void onError(GetAllSOACRUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });


    }

    @Override
    public void getRequestProduction() {


    }

    @Override
    public void getProduct(int orderId) {
        view.showProgressBar();
        getAllDetailForSOACRUsecase.executeIO(new GetAllDetailForSOACRUsecase.RequestValue(orderId),
                new BaseUseCase.UseCaseCallback<GetAllDetailForSOACRUsecase.ResponseValue,
                        GetAllDetailForSOACRUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetAllDetailForSOACRUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        localRepository.deleteProduct().subscribe();
                        //localRepository.updateStatusProduct(orderId).subscribe();
                        for (ProductEntity item : successResponse.getEntity()) {
                            ProductModel model = new ProductModel(item.getProductID(), orderId, item.getCodeColor(),
                                    item.getStt(), item.getLength(), item.getWide(), item.getDeep(), item.getGrain(),
                                    item.getNumber(), item.getNumber(), 0);
                            localRepository.addProduct(model).subscribe();
                        }
                    }

                    @Override
                    public void onError(GetAllDetailForSOACRUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });

    }

    private List<ProductModel> list;
    private OrderModel orderModel;

    @Override
    public void checkBarcode(String barcode, int orderId, double latitude, double longitude) {
        list = new ArrayList<>();
        orderModel = new OrderModel();
        if (barcode.contains(CoreApplication.getInstance().getString(R.string.text_minus))) {
            view.showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_type));
            return;
        }
        if (barcode.length() < 10 || barcode.length() > 13) {
            view.showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_lenght));
            return;
        }

        localRepository.findOrder(orderId).subscribe(new Action1<OrderModel>() {
            @Override
            public void call(OrderModel model) {
                orderModel = model;
            }
        });
        localRepository.findProductByOrderId(orderId).subscribe(new Action1<List<ProductModel>>() {
            @Override
            public void call(List<ProductModel> productModels) {
                list = productModels;


            }
        });


        if (list.size() == 0) {
            view.showError(CoreApplication.getInstance().getString(R.string.text_product_empty));
            return;
        }

        int checkBarcode = 0;

        for (ProductModel model : list) {
            String barcodeMain = orderModel.getCodeProduction() + model.getSerial();
            if (barcode.equals(barcodeMain)) {
                checkBarcode++;
                saveBarcode(latitude,
                        longitude, barcode, model);

                break;
            }
        }

        if (checkBarcode == 0) {
            view.showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
        }
    }

    public void saveBarcode(double latitude, double longitude, String barcode, ProductModel product) {
        String deviceTime = ConvertUtils.getDateTimeCurrent();
        int userId = UserManager.getInstance().getUser().getUserId();
        String phone = Settings.Secure.getString(CoreApplication.getInstance().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        getDateServerUsecase.executeIO(new GetDateServerUsecase.RequestValue(),
                new BaseUseCase.UseCaseCallback<GetDateServerUsecase.ResponseValue,
                        GetDateServerUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetDateServerUsecase.ResponseValue successResponse) {
                        LogScanCreatePack model = new LogScanCreatePack(barcode, deviceTime, successResponse.getDate(),
                                latitude, longitude, phone, product.getProductId(), orderModel.getId(), product.getSerial(),
                                0, product.getNumber(), 0, 1, product.getNumberRest(), Constants.WAITING_UPLOAD, -1, userId);


                        localRepository.updateNumberRestProduct(1, orderModel.getId(),
                                product.getProductId(), product.getSerial()).subscribe();
                        localRepository.addLogScanCreatePack(model, orderModel.getId()).subscribe();

                    }

                    @Override
                    public void onError(GetDateServerUsecase.ErrorValue errorResponse) {

                    }
                });
    }

    @Override
    public void getListCreateCode(int orderId) {
        localRepository.findAllLog(orderId).subscribe(new Action1<LogScanCreatePackList>() {
            @Override
            public void call(LogScanCreatePackList logScanCreatePackList) {
                view.showLogScanCreatePack(logScanCreatePackList.getItemList());
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
            }
        });
    }

    @Override
    public void deleteItemLog(LogScanCreatePack item) {
        localRepository.deleteLogScanItem(item).subscribe();
    }

    @Override
    public void updateNumberInput(int number, LogScanCreatePack item, int serial) {
        localRepository.updateNumberRestProduct(number, orderModel.getId(), item.getProductId(), serial).subscribe();
        localRepository.updateNumberLog(number, item.getId()).subscribe();
    }

    @Override
    public void deleteAllItemLog(List<LogScanCreatePack> list) {
        for (LogScanCreatePack item : list) {
            localRepository.deleteLogScanItem(item);
        }
    }

}
