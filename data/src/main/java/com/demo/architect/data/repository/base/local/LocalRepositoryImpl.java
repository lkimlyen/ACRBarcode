package com.demo.architect.data.repository.base.local;

import android.content.Context;

import com.demo.architect.data.model.MessageModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.ImportWorksModel;
import com.demo.architect.data.model.offline.LogCompleteCreatePack;
import com.demo.architect.data.model.offline.LogCompleteCreatePackList;
import com.demo.architect.data.model.offline.LogCompleteMainList;
import com.demo.architect.data.model.offline.LogDeleteCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.model.offline.ScanDeliveryList;
import com.demo.architect.data.model.offline.ScanDeliveryModel;
import com.demo.architect.data.model.offline.ScanWarehousingModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class LocalRepositoryImpl implements LocalRepository {

    DatabaseRealm databaseRealm;

    public LocalRepositoryImpl(Context context) {
        databaseRealm = new DatabaseRealm(context);
    }

    @Override
    public Observable<String> add(final MessageModel model) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.add(model);

                    subscriber.onNext(model.getUuid());
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<MessageModel>> findAll() {
        return Observable.create(new Observable.OnSubscribe<List<MessageModel>>() {
            @Override
            public void call(Subscriber<? super List<MessageModel>> subscriber) {
                try {
                    List<MessageModel> models = databaseRealm.findAll(MessageModel.class);

                    subscriber.onNext(models);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<OrderModel> addItemAsyns(final OrderModel model) {
        return Observable.create(new Observable.OnSubscribe<OrderModel>() {
            @Override
            public void call(Subscriber<? super OrderModel> subscriber) {
                try {
                    OrderModel orderModel = databaseRealm.findFirstById(OrderModel.class, model.getId());
                    if (orderModel == null) {
                        databaseRealm.addItemAsync(model);
                    }
                    LogScanCreatePackList packList = new LogScanCreatePackList(model.getId());
                    databaseRealm.insertOrUpdate(packList);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<OrderModel>> findAllOrder() {
        return Observable.create(new Observable.OnSubscribe<List<OrderModel>>() {
            @Override
            public void call(Subscriber<? super List<OrderModel>> subscriber) {
                try {
                    List<OrderModel> models = databaseRealm.findAll(OrderModel.class);

                    subscriber.onNext(models);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteAllOrder() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {

                    databaseRealm.delete(OrderModel.class);

                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<CustomerModel> addCustomer(final CustomerModel customerModel) {
        return Observable.create(new Observable.OnSubscribe<CustomerModel>() {
            @Override
            public void call(Subscriber<? super CustomerModel> subscriber) {
                try {
                    databaseRealm.addItemAsync(customerModel);

                    subscriber.onNext(customerModel);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<ProductModel> addProduct(final ProductModel model) {
        return Observable.create(new Observable.OnSubscribe<ProductModel>() {
            @Override
            public void call(Subscriber<? super ProductModel> subscriber) {
                try {
                    ProductModel product = databaseRealm.findProductByLog(model.getProductId(),
                            model.getOrderId(), model.getSerial());
                    if (product != null) {
                        // databaseRealm.updateNumberProduct(model, model.getNumber());
                    } else {
                        databaseRealm.addItemAsync(model);
                    }
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusAndNumberProduct(final int serverId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusProduct(serverId);
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusLog(final int id) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusLog(id);
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<ProductModel>> findProductByOrderId(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<List<ProductModel>>() {
            @Override
            public void call(Subscriber<? super List<ProductModel>> subscriber) {
                try {
                    List<ProductModel> models = databaseRealm.findProductByOrderId(orderId);

                    subscriber.onNext(models);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addLogScanCreatePack(final ProductModel product, final OrderModel orderModel, final LogScanCreatePack item, final int orderId, final String barcode) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    // databaseRealm.updateNumberRestProduct(item.getNumInput(), orderId, item.getProductId(), item.getTimes());
                    LogScanCreatePack model = databaseRealm.findLogByBarcode(barcode);

                    if (model != null) {
                        item.setId(model.getId());
                        item.setNumInput(model.getNumInput() + item.getNumInput());
                        databaseRealm.updateLogModel(item,product);
                    } else {
                        databaseRealm.addLogScanCreatePackAsync(product, orderModel, item, orderId);
                    }


                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addLogCompleteCreatePack(final int id, final int serverId, final int serial, final int numTotal, final String dateCreate) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    // databaseRealm.updateNumberRestProduct(item.getNumInput(), orderId, item.getProductId(), item.getTimes());

                    databaseRealm.addLogCompleteCreatePackAsync(id, serverId, serial, numTotal, dateCreate);
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> addLogCompleteCreatePack(final ProductModel productModel, final LogCompleteCreatePack model, final int serverId) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    // databaseRealm.updateNumberRestProduct(item.getNumInput(), orderId, item.getProductId(), item.getTimes());

                    int num = databaseRealm.addLogCompleteCreatePackAsync(productModel,model, serverId);
                    subscriber.onNext(num);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<OrderModel> findOrder(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<OrderModel>() {
            @Override
            public void call(Subscriber<? super OrderModel> subscriber) {
                try {
                    OrderModel model = databaseRealm.findOrderByOrderId(orderId);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LogScanCreatePackList> findAllLog(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<LogScanCreatePackList>() {
            @Override
            public void call(Subscriber<? super LogScanCreatePackList> subscriber) {
                try {
                    LogScanCreatePackList model = databaseRealm.findLogById(orderId);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LogScanCreatePackList> findLogPrint(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<LogScanCreatePackList>() {
            @Override
            public void call(Subscriber<? super LogScanCreatePackList> subscriber) {
                try {


                    LogScanCreatePackList model = databaseRealm.findLogById(orderId);

                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteLogScanItem(final int id) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {

                    LogScanCreatePack item = databaseRealm.findFirstById(LogScanCreatePack.class, id);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String newFormat = formatter.format(new Date());
                    LogDeleteCreatePack model = new LogDeleteCreatePack(databaseRealm.getIdCurrent() + 1, item.getBarcode(), item.getOrderId(),
                            null, item.getDeviceTime(), item.getServerTime(), item.getLatitude(),
                            item.getLongitude(), item.getCreateByPhone(),
                            item.getSerial(),
                            item.getNumTotal(), item.getNumInput(), item.getCreateBy(),
                            newFormat, item.getStatus(), -1);
                    databaseRealm.updateNumberRestProduct(-item.getNumInput(), item.getOrderId(), item.getProductId(), item.getSerial());
                    //databaseRealm.addItemAsync(model);
                    databaseRealm.deleteLogCreateAsync(id, model);
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    @Override
    public Observable<String> updateNumberLog(final int id, final int number) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateNumber(id, number);
                    subscriber.onNext(String.valueOf(id));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteProduct() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.delete(ProductModel.class);
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<IPAddress> insertOrUpdateIpAddress(final IPAddress model) {
        return Observable.create(new Observable.OnSubscribe<IPAddress>() {
            @Override
            public void call(Subscriber<? super IPAddress> subscriber) {
                try {
                    databaseRealm.insertOrUpdate(model);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<IPAddress> findIPAddress() {
        return Observable.create(new Observable.OnSubscribe<IPAddress>() {
            @Override
            public void call(Subscriber<? super IPAddress> subscriber) {
                try {
                    IPAddress ipAddress = databaseRealm.findFirst(IPAddress.class);
                    subscriber.onNext(ipAddress);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteAllLog() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteLogAll();
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteLogCompleteAll() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteLogCompleteAll();
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> getSumLogPack(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    int sum = databaseRealm.sumNumInputLog(orderId);
                    subscriber.onNext(sum);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<OrderModel>> findOrderByLogComplete() {
        return Observable.create(new Observable.OnSubscribe<List<OrderModel>>() {
            @Override
            public void call(Subscriber<? super List<OrderModel>> subscriber) {
                try {
                    List<OrderModel> list = databaseRealm.findOrderInLogComplete();
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    @Override
    public Observable<LogCompleteMainList> findPackage(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<LogCompleteMainList>() {
            @Override
            public void call(Subscriber<? super LogCompleteMainList> subscriber) {
                try {
                    LogCompleteMainList list = databaseRealm.findPackage(orderId);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LogCompleteCreatePackList> findLogCreatePack(final int logId) {
        return Observable.create(new Observable.OnSubscribe<LogCompleteCreatePackList>() {
            @Override
            public void call(Subscriber<? super LogCompleteCreatePackList> subscriber) {
                try {

                    LogCompleteCreatePackList model = databaseRealm.findFirstById(LogCompleteCreatePackList.class, logId);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LogCompleteCreatePackList> findLogCompletById(final int logId) {
        return Observable.create(new Observable.OnSubscribe<LogCompleteCreatePackList>() {
            @Override
            public void call(Subscriber<? super LogCompleteCreatePackList> subscriber) {
                try {

                    LogCompleteCreatePackList model = databaseRealm.findFirstById(LogCompleteCreatePackList.class, logId);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> deleteLogComplete(final int id, final int logId) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {

                    int num = databaseRealm.deleteLogCompleteAsync(id, logId);
                    subscriber.onNext(num);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> getNumTotalPack(final int logId) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {

                    int num = databaseRealm.getNumTotalPack(logId);
                    subscriber.onNext(num);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deletePack(final int logId, final int orderId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {

                    databaseRealm.deletePack(logId, orderId);
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> checkExistCode(final int logId, final String barcode) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {

                    Boolean aBoolean = databaseRealm.checkExistBarcode(logId, barcode);
                    subscriber.onNext(aBoolean);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> countCodeNotUp(final int logId) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Integer count = databaseRealm.countCodeNotUpdate(logId);
                    subscriber.onNext(count);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> countDeliveryNotComplete() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Integer count = databaseRealm.countDeliveryNotComplete();
                    subscriber.onNext(count);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> checkExistBarcodeInWarehousing(final String barcode) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    Boolean aBoolean = databaseRealm.checkExistScanWarehousing(barcode);
                    subscriber.onNext(aBoolean);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> checkExistBarcodeScanCreate(final String barcode) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    Boolean aBoolean = databaseRealm.checkExistScanCreate(barcode);
                    subscriber.onNext(aBoolean);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> checkExistImportWorks(final String barcode) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    Boolean aBoolean = databaseRealm.checkExistImportWorks(barcode);
                    subscriber.onNext(aBoolean);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> checkExistBarcodeInDelivery(final String barcode) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {

                    Boolean aBoolean = databaseRealm.checkExistScanDelivery(barcode);
                    subscriber.onNext(aBoolean);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<ScanWarehousingModel> addScanWareHousing(final ScanWarehousingModel scanWarehousingModel) {
        return Observable.create(new Observable.OnSubscribe<ScanWarehousingModel>() {
            @Override
            public void call(Subscriber<? super ScanWarehousingModel> subscriber) {
                try {

                    ScanWarehousingModel model = databaseRealm.addScanWarehousingAsync(scanWarehousingModel);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addScanDelivery(final ScanDeliveryModel model, final int times, final String codeRequest) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addScanDelivery(model, times, codeRequest);
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<ScanDeliveryList> findScanDelivery(final int times, final String requestCode) {
        return Observable.create(new Observable.OnSubscribe<ScanDeliveryList>() {
            @Override
            public void call(Subscriber<? super ScanDeliveryList> subscriber) {
                try {
                    ScanDeliveryList model = databaseRealm.finScanDeliveryAndAdd(times, requestCode);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    @Override
    public Observable<ScanDeliveryList> findScanDeliveryNotComplete(final String requestCode) {
        return Observable.create(new Observable.OnSubscribe<ScanDeliveryList>() {
            @Override
            public void call(Subscriber<? super ScanDeliveryList> subscriber) {
                try {

                    ScanDeliveryList model = databaseRealm.finScanDeliveryNotComplete(requestCode);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusScanDelivery(final int id, final HashMap<String, Integer> map) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {

                    databaseRealm.updateStatusScanDelivery(id, map);
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<ImportWorksModel> addImportWorks(final ImportWorksModel model) {
        return Observable.create(new Observable.OnSubscribe<ImportWorksModel>() {
            @Override
            public void call(Subscriber<? super ImportWorksModel> subscriber) {
                try {

                    ImportWorksModel item = databaseRealm.addImportWorks(model);
                    subscriber.onNext(item);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<LogScanCreatePack>> logCreateToJson(final int id) {
        return Observable.create(new Observable.OnSubscribe<List<LogScanCreatePack>>() {
            @Override
            public void call(Subscriber<? super List<LogScanCreatePack>> subscriber) {
                try {

                    List<LogScanCreatePack> list = databaseRealm.logCreateToJson(id);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<LogCompleteCreatePack>> logCompleteToJson(final int logId) {
        return Observable.create(new Observable.OnSubscribe<List<LogCompleteCreatePack>>() {
            @Override
            public void call(Subscriber<? super List<LogCompleteCreatePack>> subscriber) {
                try {

                    List<LogCompleteCreatePack> list = databaseRealm.logCompleteToJson(logId);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<ScanDeliveryModel>> deliveryToJson(final String request) {
        return Observable.create(new Observable.OnSubscribe<List<ScanDeliveryModel>>() {
            @Override
            public void call(Subscriber<? super List<ScanDeliveryModel>> subscriber) {
                try {

                    List<ScanDeliveryModel> list = databaseRealm.deliveryToJson(request);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> checkStatus(final int id) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {

                    Boolean aBoolean = databaseRealm.checkStatus(id);
                    subscriber.onNext(aBoolean);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteCodeNotComplete(final int logId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {

                    databaseRealm.deleteLogNotComplete(logId);
                    subscriber.onNext(String.valueOf(logId));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


}
