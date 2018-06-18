package com.demo.architect.data.repository.base.local;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.offline.LogCompleteCreatePack;
import com.demo.architect.data.model.offline.LogCompleteCreatePackList;
import com.demo.architect.data.model.offline.LogDeleteCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.model.offline.ProductModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class DatabaseRealm {

    public DatabaseRealm() {

    }

    public Realm getRealmInstance() {
        return Realm.getDefaultInstance();
    }

    public <T extends RealmObject> T add(T model) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        realm.copyToRealm(model);
        realm.commitTransaction();
        return model;
    }

    public <T extends RealmObject> void addItemAsync(final T item) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(item);
            }
        });
    }

    public <T extends RealmObject> void insertOrUpdate(final T item) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(item);
            }
        });
    }

    public <T extends RealmObject> List<T> findAll(Class<T> clazz) {
        return getRealmInstance().where(clazz).findAll();
    }

    public <T extends RealmObject> T findFirst(Class<T> clazz) {
        return getRealmInstance().where(clazz).findFirst();
    }

    public <T extends RealmObject> T findFirstById(Class<T> clazz, int id) {
        return getRealmInstance().where(clazz).equalTo("id", id).findFirst();
    }

    public void close() {
        getRealmInstance().close();
    }

    public <T extends RealmObject> void delete(Class<T> clazz) {
        Realm realm = getRealmInstance();
        // obtain the results of a query
        final RealmResults<T> results = realm.where(clazz).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();

            }
        });
    }

    public void updateStatusProduct(int serverId) {
        Realm realm = getRealmInstance();
        final LogCompleteCreatePackList result = realm.where(LogCompleteCreatePackList.class).equalTo("id", serverId).findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (result != null) {
                    for (LogCompleteCreatePack item : result.getItemList()) {
                        ProductModel product = realm.where(ProductModel.class).equalTo("orderId", item.getOrderId()).equalTo("productId", item.getProductId()).
                                equalTo("serial", item.getSerial()).findFirst();
                        if (product.getStatus() == Constants.WAITING_UPLOAD) {
                            if (product.getNumberRest() == 0) {
                                product.setStatus(Constants.COMPLETE);
                            } else {
                                product.setStatus(Constants.DOING);
                            }
                        }
                    }

                }
                RealmResults<ProductModel> results = realm.where(ProductModel.class).equalTo("orderId", result.getOrderId()).findAll();
                OrderModel orderModel = realm.where(OrderModel.class).equalTo("id", result.getOrderId()).findFirst();
                int sum = results.sum("numberRest").intValue();
                if (sum == 0) {
                    orderModel.setStatus(Constants.COMPLETE);
                } else {
                    orderModel.setStatus(Constants.DOING);
                }
            }
        });
    }

    public List<ProductModel> findProductByOrderId(int orderId) {
        return getRealmInstance().where(ProductModel.class).equalTo("orderId", orderId).findAll();
    }

    public void addLogScanCreatePackAsync(final LogScanCreatePack item, final int orderId) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanCreatePack.create(realm, item, orderId);
            }
        });
    }

    public void addLogCompleteCreatePackAsync(final int id, final int serverId, final int serial) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanCreatePack scanCreatePack = realm.where(LogScanCreatePack.class).equalTo("id", id)
                        .findFirst();
                LogCompleteCreatePack model = new LogCompleteCreatePack(scanCreatePack.getBarcode(),
                        scanCreatePack.getDeviceTime(), scanCreatePack.getServerTime(), scanCreatePack.getLatitude(),
                        scanCreatePack.getLongitude(), scanCreatePack.getCreateByPhone(), scanCreatePack.getProductId(),
                        scanCreatePack.getOrderId(), scanCreatePack.getSerial(), scanCreatePack.getNumTotal(), scanCreatePack.getNumInput(),
                        scanCreatePack.getNumInput(), scanCreatePack.getCreateBy());
                LogCompleteCreatePack.create(realm, model, serverId, serial);
                ProductModel productMode = realm.where(ProductModel.class)
                        .equalTo("orderId", model.getOrderId())
                        .equalTo("productId", model.getProductId())
                        .equalTo("serial", model.getSerial())
                        .findFirst();
                productMode.setNumCompleteScan(productMode.getNumCompleteScan() + model.getNumInput());
                LogScanCreatePack.delete(realm, id);
            }
        });
    }

    public OrderModel findOrderByOrderId(int orderId) {
        Realm realm = getRealmInstance();
        return realm.where(OrderModel.class).equalTo("id", orderId).findFirst();
    }

    public LogScanCreatePackList findLogById(int orderId) {
        return getRealmInstance().where(LogScanCreatePackList.class).equalTo("orderId", orderId).findFirst();
    }

    public void deleteLogCreateAsync(final int id) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanCreatePack.delete(realm, id);
            }
        });
    }

    public ProductModel findProductByLog(int productId, int orderId, int serial) {
        Realm realm = getRealmInstance();
        return realm.where(ProductModel.class).equalTo("productId", productId)
                .equalTo("orderId", orderId).equalTo("serial", serial).findFirst();
    }

    public void updateNumber(final int id, final int number) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanCreatePack model = realm.where(LogScanCreatePack.class).equalTo("id", id).findFirst();
                ProductModel product = realm.where(ProductModel.class).equalTo("productId", model.getProductId()).findFirst();
                int numberInput = number - model.getNumInput();
                model.setNumInput(number);
                product.setNumberScan(product.getNumberScan() + numberInput);
                product.setNumberRest(product.getNumber() - product.getNumberScan());
                model.setNumRest(product.getNumberRest());

            }
        });
    }

    public void updateNumberRestProduct(final int number, final int orderId, final int productId, final int serial) {
        Realm mRealm = getRealmInstance();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ProductModel model = realm.where(ProductModel.class).equalTo("productId", productId)
                        .equalTo("orderId", orderId).equalTo("serial", serial).findFirst();
                model.setNumberScan(model.getNumberScan() + number);
                model.setNumberRest(model.getNumber() - model.getNumberScan());
            }
        });
    }

    public void updateLogModel(final LogScanCreatePack model) {
        Realm mRealm = getRealmInstance();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanCreatePack current = realm.where(LogScanCreatePack.class).equalTo("id", model.getId()).findFirst();
                final int numberCurrent = current.getNumInput();
                LogScanCreatePack logScanCreatePack = realm.copyToRealmOrUpdate(model);
                ProductModel product = realm.where(ProductModel.class).equalTo("productId", model.getProductId()).findFirst();
                int numberInput = model.getNumInput() - numberCurrent;
                product.setNumberScan(product.getNumberScan() + numberInput);
                product.setNumberRest(product.getNumber() - product.getNumberScan());
                logScanCreatePack.setNumRest(product.getNumberRest());

                LogCompleteCreatePack logCompleteCreatePack = realm.where(LogCompleteCreatePack.class)
                        .equalTo("productId", model.getProductId()).equalTo("orderId", model.getOrderId())
                        .equalTo("serial", model.getSerial()).findFirst();
                if (logCompleteCreatePack != null) {
                    logScanCreatePack.setNumCodeScan(logCompleteCreatePack.getNumInput());
                }
            }
        });
    }

    public int sumNumInputLog(final int orderId) {
        Realm realm = getRealmInstance();
        RealmList<LogScanCreatePack> results = realm.where(LogScanCreatePackList.class).equalTo("orderId", orderId).findFirst().getItemList();
        return results.sum("numInput").intValue();
    }


    public int getIdCurrent() {
        return LogDeleteCreatePack.id(getRealmInstance());
    }

    public void deleteLogAll() {
        Realm realm = getRealmInstance();
        // obtain the results of a query
        final RealmResults<LogScanCreatePack> results = realm.where(LogScanCreatePack.class).equalTo("status", 0).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }

    public LogScanCreatePack findLogByBarcode(final String barcode) {
        Realm realm = getRealmInstance();
        return realm.where(LogScanCreatePack.class).equalTo("barcode", barcode).equalTo("status", 0).findFirst();
    }

    public List<OrderModel> findOrderInLogComplete(){
        Realm realm = getRealmInstance();
        List<OrderModel> results = null;
        RealmResults<LogCompleteCreatePackList> packList = realm.where(LogCompleteCreatePackList.class).distinct("orderId").findAll();
        if (packList.size() > 0){
            for (LogCompleteCreatePackList item : packList){
                results.add(realm.where(OrderModel.class).equalTo("id", item.getOrderId()).findFirst());
            }
        }
        return results;
    }

}
