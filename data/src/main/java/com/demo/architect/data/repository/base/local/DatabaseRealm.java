package com.demo.architect.data.repository.base.local;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.offline.LogDeleteCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.model.offline.ProductModel;

import java.util.List;

import io.realm.Realm;
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

    public <T extends RealmObject> void deleteAll(Class<T> clazz) {
        Realm realm = getRealmInstance();
        // obtain the results of a query
        final RealmResults<T> results = realm.where(clazz).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }

    public void updateStatusProduct(int orderId) {
        Realm realm = getRealmInstance();
        final RealmResults<ProductModel> results = realm.where(ProductModel.class).equalTo("orderId", orderId).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (ProductModel item : results) {
                    if (item.getStatus() == Constants.WAITING_UPLOAD) {
                        item.setStatus(Constants.DISABLE);
                    }
                }
            }
        });
    }

    public List<ProductModel> findProductByOrderId(int orderId) {
        return getRealmInstance().where(ProductModel.class).equalTo("orderId", orderId).equalTo("status", Constants.WAITING_UPLOAD).findAll();
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

    public void updateNumber(final int number, final int id) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanCreatePack model = realm.where(LogScanCreatePack.class).equalTo("id", id).findFirst();
                ProductModel product = realm.where(ProductModel.class).equalTo("productId", model.getProductId()).findFirst();
                model.setNumInput(number);
                model.setNumRest(product.getNumberRest());
            }
        });
    }

    public ProductModel updateNumberRestProduct(final int number, final int orderId, final int productId, final int serial) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ProductModel model = realm.where(ProductModel.class).equalTo("productId", productId)
                        .equalTo("orderId", orderId).equalTo("serial", serial).findFirst();
                model.setNumberScan(model.getNumberScan() + number);
                model.setNumberRest(model.getNumber() - model.getNumberScan());
            }
        });
        return realm.where(ProductModel.class).equalTo("productId", productId)
                .equalTo("orderId", orderId).equalTo("serial", serial).findFirst();

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
}
