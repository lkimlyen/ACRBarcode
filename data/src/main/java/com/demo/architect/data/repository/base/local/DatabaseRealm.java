package com.demo.architect.data.repository.base.local;

import com.demo.architect.data.helper.Constants;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
                            product.setNumCompleteScan(item.getNumInput());
                            if (product.getNumberRest() == 0) {
                                product.setStatus(Constants.COMPLETE);
                            } else {
                                product.setStatus(Constants.DOING);
                            }

                        }
                    }

                }
                OrderModel orderModel = realm.where(OrderModel.class).equalTo("codeProduction", result.getCodeRequest()).findFirst();
                RealmResults<ProductModel> results = realm.where(ProductModel.class).equalTo("orderId", orderModel.getId()).findAll();

                int sum = results.sum("numberRest").intValue();
                if (sum == 0) {
                    orderModel.setStatus(Constants.COMPLETE);
                } else {
                    orderModel.setStatus(Constants.DOING);
                }


            }
        });
    }

    public void updateStatusLog(final int id) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final LogCompleteCreatePack result = realm.where(LogCompleteCreatePack.class).equalTo("id", id).findFirst();
                result.setStatus(Constants.COMPLETE);
                result.getProductModel().setNumCompleteScan(result.getProductModel().getNumCompleteScan() + result.getNumInput());
            }
        });
    }

    public void updateStatusScanDelivery(final int id, final HashMap<String, Integer> map) {
        Realm realm = getRealmInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ScanDeliveryList parent = realm.where(ScanDeliveryList.class).equalTo("id", id).findFirst();
                parent.setStatus(Constants.COMPLETE);
                for (ScanDeliveryModel model : parent.getItemList()) {
                    model.setServerId(map.get(model.getBarcode()));
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

    public ImportWorksModel addImportWorks(final ImportWorksModel item) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        ImportWorksModel importWorksModel = realm.copyToRealm(item);
        realm.commitTransaction();
        return importWorksModel;
    }

    public void addLogCompleteCreatePackAsync(final int id, final int serverId, final int serial, final int numTotal, final String dateCreate) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {


                LogScanCreatePackList packList = realm.where(LogScanCreatePackList.class).equalTo("orderId", id).findFirst();
                for (LogScanCreatePack scanCreatePack : packList.getItemList()) {
                    scanCreatePack.setStatus(Constants.COMPLETE);
                    ProductModel productMode = realm.where(ProductModel.class)
                            .equalTo("orderId", scanCreatePack.getOrderId())
                            .equalTo("productId", scanCreatePack.getProductId())
                            .equalTo("serial", scanCreatePack.getSerial())
                            .findFirst();
                    LogCompleteCreatePack model = new LogCompleteCreatePack(scanCreatePack.getBarcode(),
                            scanCreatePack.getDeviceTime(), scanCreatePack.getServerTime(), scanCreatePack.getLatitude(),
                            scanCreatePack.getLongitude(), scanCreatePack.getCreateByPhone(), scanCreatePack.getProductId(),
                            productMode, scanCreatePack.getOrderId(), scanCreatePack.getSerial(), scanCreatePack.getNumTotal(), scanCreatePack.getNumInput(),
                            scanCreatePack.getNumInput(), scanCreatePack.getCreateBy());
                    LogCompleteCreatePack.create(realm, model, serverId, serial, numTotal, dateCreate);

                    productMode.setNumCompleteScan(productMode.getNumCompleteScan() + model.getNumInput());

                    if (productMode.getStatus() == Constants.WAITING_UPLOAD) {
                        productMode.setNumCompleteScan(model.getNumInput());
                        if (productMode.getNumberRest() == 0) {
                            productMode.setStatus(Constants.COMPLETE);
                        } else {
                            productMode.setStatus(Constants.DOING);
                        }

                    }


                    OrderModel orderModel = realm.where(OrderModel.class).equalTo("id", model.getOrderId()).findFirst();
                    RealmResults<ProductModel> results = realm.where(ProductModel.class).equalTo("orderId", orderModel.getId()).findAll();

                    int sum = results.sum("numberRest").intValue();
                    if (sum == 0) {
                        orderModel.setStatus(Constants.COMPLETE);
                    } else {
                        orderModel.setStatus(Constants.DOING);
                    }

                }
                //LogScanCreatePack.delete(realm, id);
            }
        });
    }

    public void addLogCompleteCreatePackAsync(final LogCompleteCreatePack model, final int serverId) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ProductModel productModel = realm.where(ProductModel.class).equalTo("orderId", model.getOrderId())
                        .equalTo("serial", Integer.parseInt(model.getBarcode().substring(9))).findFirst();
                model.setProductId(productModel.getProductId());
                model.setProductModel(productModel);
                model.setSerial(productModel.getSerial());
                model.setNumTotal(productModel.getNumber());
                LogCompleteCreatePackList parent = realm.where(LogCompleteCreatePackList.class).equalTo("id", serverId).findFirst();
                RealmList<LogCompleteCreatePack> items = parent.getItemList();
                model.setId(LogCompleteCreatePack.id(realm) + 1);
                LogCompleteCreatePack present = realm.copyToRealm(model);
                items.add(present);
                ProductModel productMode = present.getProductModel();
                productMode.setNumCompleteScan(productMode.getNumCompleteScan() + model.getNumInput());
                productMode.setNumberScan(productMode.getNumberScan() - model.getNumInput());
                productMode.setNumberRest(productMode.getNumber() - productMode.getNumberScan());
                parent.setNumTotal(parent.getNumTotal() + present.getNumInput());

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


    public void deleteLogCompleteAsync(final int id, final int logId) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogCompleteCreatePack pack = realm.where(LogCompleteCreatePack.class).equalTo("id", id).findFirst();
                LogCompleteCreatePackList packList = realm.where(LogCompleteCreatePackList.class).equalTo("id", logId).findFirst();
                packList.setNumTotal(packList.getNumTotal() - pack.getNumInput());
                ProductModel productModel = realm.where(ProductModel.class).equalTo("productId", pack.getProductId())
                        .equalTo("orderId", pack.getOrderId()).equalTo("serial", pack.getSerial()).findFirst();
                productModel.setNumberRest(productModel.getNumberRest() - pack.getNumInput());
                productModel.setNumberScan(productModel.getNumber() - productModel.getNumberRest());
                productModel.setNumCompleteScan(productModel.getNumCompleteScan() - pack.getNumInput());

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String newFormat = formatter.format(new Date());
                LogDeleteCreatePack logDelete = new LogDeleteCreatePack(getIdCurrent() + 1, pack.getBarcode(), pack.getOrderId(),
                        pack.getDeviceTime(), pack.getServerTime(), pack.getLatitude(),
                        pack.getLongitude(), pack.getCreateByPhone(),
                        pack.getSerial(), 0,
                        pack.getNumTotal(), 0, pack.getNumInput(), pack.getCreateBy(),
                        newFormat, pack.getStatus(), logId);
                realm.copyToRealm(logDelete);
                // Otherwise it has been deleted already.
                if (pack != null) {
                    pack.deleteFromRealm();
                }
            }
        });
    }

    public void deletePack(final int logId, final int orderId) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogCompleteCreatePackList packList = realm.where(LogCompleteCreatePackList.class).equalTo("id", logId).findFirst();
                for (LogCompleteCreatePack pack : packList.getItemList()) {
                    ProductModel productModel = realm.where(ProductModel.class).equalTo("productId", pack.getProductId())
                            .equalTo("orderId", pack.getOrderId()).equalTo("serial", pack.getSerial()).findFirst();
                    productModel.setNumberRest(productModel.getNumberRest() + pack.getNumInput());
                    productModel.setNumberScan(productModel.getNumber() - productModel.getNumberRest());
                    productModel.setNumCompleteScan(productModel.getNumCompleteScan() - pack.getNumInput());
                    if (productModel.getNumberRest() == productModel.getNumber()) {
                        productModel.setStatus(Constants.WAITING_UPLOAD);
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String newFormat = formatter.format(new Date());
                    LogDeleteCreatePack logDelete = new LogDeleteCreatePack(getIdCurrent() + 1, pack.getBarcode(), pack.getOrderId(),
                            pack.getDeviceTime(), pack.getServerTime(), pack.getLatitude(),
                            pack.getLongitude(), pack.getCreateByPhone(),
                            pack.getSerial(), 0,
                            pack.getNumTotal(), 0, pack.getNumInput(), pack.getCreateBy(),
                            newFormat, pack.getStatus(), logId);
                    realm.copyToRealm(logDelete);
                }
                packList.getItemList().deleteAllFromRealm();
                packList.deleteFromRealm();
                LogCompleteMainList mainList = realm.where(LogCompleteMainList.class).equalTo("orderId", orderId).findFirst();
                OrderModel orderModel = realm.where(OrderModel.class).equalTo("id", orderId).findFirst();
                if (mainList.getItemList().size() == 0) {
                    mainList.deleteFromRealm();
                    orderModel.setStatus(Constants.WAITING_UPLOAD);
                }

            }
        });
    }

    public Boolean checkExistScanWarehousing(String barcode) {
        Realm realm = getRealmInstance();
        RealmResults<ScanWarehousingModel> results = realm.where(ScanWarehousingModel.class).equalTo("barcode", barcode).findAll();
        return results.size() > 0 ? true : false;

    }

    public Boolean checkExistScanDelivery(String barcode) {
        Realm realm = getRealmInstance();
        RealmResults<ScanDeliveryModel> results = realm.where(ScanDeliveryModel.class).equalTo("barcode", barcode).findAll();
        return results.size() > 0 ? true : false;

    }

    public Boolean checkExistImportWorks(String barcode) {
        Realm realm = getRealmInstance();
        RealmResults<ImportWorksModel> results = realm.where(ImportWorksModel.class).equalTo("barcode", barcode).findAll();
        return results.size() > 0 ? true : false;

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

    public void deleteLogCompleteAll() {
        Realm realm = getRealmInstance();
        // obtain the results of a query
        final RealmResults<LogScanCreatePack> results = realm.where(LogScanCreatePack.class).equalTo("status", Constants.COMPLETE).findAll();
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

    public List<OrderModel> findOrderInLogComplete() {
        Realm realm = getRealmInstance();
        List<OrderModel> list = new ArrayList<>();
        RealmResults<LogCompleteMainList> results = realm.where(LogCompleteMainList.class).findAll();
        for (LogCompleteMainList mainList : results) {
            list.add(realm.where(OrderModel.class).equalTo("id", mainList.getOrderId()).findFirst());
        }
        return list;
    }

    public LogCompleteMainList findPackage(int orderId) {
        Realm realm = getRealmInstance();

        return realm.where(LogCompleteMainList.class).equalTo("orderId", orderId).findFirst();
    }

    public Boolean checkExistBarcode(int logId, final String barcode) {
        Realm realm = getRealmInstance();
        LogCompleteCreatePackList packList = realm.where(LogCompleteCreatePackList.class).equalTo("id", logId).findFirst();
        int count = 0;
        for (LogCompleteCreatePack pack : packList.getItemList()) {
            if (pack.getBarcode().equals(barcode)) {
                count++;
            }
        }
        return count > 0 ? true : false;
    }

    public int countCodeNotUpdate(final int logId) {
        Realm realm = getRealmInstance();
        LogCompleteCreatePackList results = realm.where(LogCompleteCreatePackList.class).equalTo("id", logId).findFirst();
        int count = 0;
        for (LogCompleteCreatePack pack : results.getItemList()) {
            if (pack.getStatus() == Constants.WAITING_UPLOAD) {
                count++;
            }
        }
        return count;
    }

    public ScanWarehousingModel addScanWarehousingAsync(final ScanWarehousingModel model) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        ScanWarehousingModel present = realm.copyToRealmOrUpdate(model);
        realm.commitTransaction();
        return present;
    }


    public ScanDeliveryList finScanDeliveryNotComplete(String requestCode) {
        Realm realm = getRealmInstance();
        return realm.where(ScanDeliveryList.class).equalTo("codeRequest", requestCode).equalTo("status", Constants.WAITING_UPLOAD).findFirst();
    }

    public List<ScanDeliveryModel> deliveryToJson(String requestCode) {
        Realm realm = getRealmInstance();
        RealmList<ScanDeliveryModel> realmList = realm.where(ScanDeliveryList.class).equalTo("codeRequest", requestCode).equalTo("status", Constants.WAITING_UPLOAD).findFirst().getItemList();
        return realm.copyFromRealm(realmList);
    }

    public ScanDeliveryList finScanDeliveryAndAdd(int times, String requestCode) {
        Realm realm = getRealmInstance();
        ScanDeliveryList parent = realm.where(ScanDeliveryList.class)
                .equalTo("codeRequest", requestCode)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("times", times + 1).findFirst();
        if (parent == null) {
            parent = new ScanDeliveryList(ScanDeliveryList.id(realm) + 1, times + 1, requestCode);
            realm.beginTransaction();
            parent = realm.copyToRealm(parent);
            realm.commitTransaction();
        }
        return parent;
    }

    public void addScanDelivery(final ScanDeliveryModel model, final int times, final String codeRequest) {
        Realm realm = getRealmInstance();


        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                model.setId(ScanDeliveryModel.id(realm) + 1);
                ScanDeliveryList parent = realm.where(ScanDeliveryList.class)
                        .equalTo("codeRequest", codeRequest)
                        .equalTo("status", Constants.WAITING_UPLOAD)
                        .equalTo("times", times + 1).findFirst();
                RealmList<ScanDeliveryModel> list = parent.getItemList();
                ScanDeliveryModel item = realm.copyToRealm(model);
                list.add(item);
            }
        });

    }

    public List<LogScanCreatePack> logCreateToJson(int orderId) {
        Realm realm = getRealmInstance();
        RealmList<LogScanCreatePack> realmResults = realm.where(LogScanCreatePackList.class).equalTo("orderId", orderId)
                .findFirst().getItemList();
        return realm.copyFromRealm(realmResults);

    }
}
